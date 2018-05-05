package com.softartdev.noteroom.db;

import android.content.Context;

import java.io.File;
import java.util.Arrays;

import io.realm.Realm;
import io.realm.RealmConfiguration;
import timber.log.Timber;

abstract class RealmDbRepository implements DbStore {
    private static final String DEFAULT_FILE_NAME = RealmConfiguration.DEFAULT_REALM_NAME; // default.realm
    private static final String TEMP_FILE_NAME = "temp.realm";
    private Realm mRealm;

    RealmDbRepository(Context context) {
        Realm.init(context);
    }

    Realm getRealm() {
        return mRealm;
    }

    @Override
    public boolean isEncryption() {
        return !checkPass(null);
    }

    @Override
    public boolean checkPass(String password) {
        closeAllOpenedRealmInstances();
        try {
            RealmConfiguration.Builder builder = new RealmConfiguration.Builder();
            if (password != null) {
                builder.encryptionKey(getSecureKey(password));
            }
            builder.schemaVersion(1);
            builder.migration(new NoteMigration());
            RealmConfiguration realmConfiguration = builder.build();
            mRealm = Realm.getInstance(realmConfiguration);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public void changePass(String oldPass, String newPass) {
        byte[] oldKey = getSecureKey(oldPass);
        byte[] newKey = getSecureKey(newPass);

        RealmConfiguration defaultConfig = mRealm.getConfiguration();
        byte[] key = defaultConfig.getEncryptionKey();
        if (!Arrays.equals(oldKey, key)) {
            Timber.d("Original: %s", Arrays.toString(key));
            Timber.d("Typed:    %s", Arrays.toString(oldKey));
            throw new RuntimeException("Wrong old password");
        }

        File dir = defaultConfig.getRealmDirectory();
        File tempFile = new File(dir, TEMP_FILE_NAME);
        mRealm.writeEncryptedCopyTo(tempFile, newKey);

        closeAllOpenedRealmInstances();

        if (Realm.deleteRealm(defaultConfig)) {
            Timber.d("%s deleted", DEFAULT_FILE_NAME);
        }
        Realm.removeDefaultConfiguration();

        File defaultFile = new File(dir, DEFAULT_FILE_NAME);
        if (tempFile.renameTo(defaultFile)) {
            Timber.d("%s renamed to %s", TEMP_FILE_NAME, DEFAULT_FILE_NAME);
        }

        RealmConfiguration.Builder builder = new RealmConfiguration.Builder();
        builder.name(DEFAULT_FILE_NAME);
        if (newKey != null) {
            builder.encryptionKey(newKey);
        }
        builder.schemaVersion(1);
        builder.migration(new NoteMigration());
        defaultConfig = builder.build();
        Realm.setDefaultConfiguration(defaultConfig);
        mRealm = Realm.getDefaultInstance();
    }

    private void closeAllOpenedRealmInstances() {
        if (mRealm != null) {
            RealmConfiguration defaultConfig = mRealm.getConfiguration();
            int count = Realm.getGlobalInstanceCount(defaultConfig);
            while (count != 0) {
                Timber.d("Open %s instances of Realm", count);
                mRealm.close();
                count = Realm.getGlobalInstanceCount(defaultConfig);
            }
        }
    }

    private byte[] getSecureKey(String pass){
        if (pass == null) {
            return null;
        }
        byte[] source = pass.getBytes();
        byte[] encryptionKey = new byte[64];
        System.arraycopy(source, 0, encryptionKey, 0, source.length);
        return encryptionKey;
    }
}
