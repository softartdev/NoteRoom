#!/bin/sh

# Decrypt the file
# --batch to prevent interactive command --yes to assume "yes" for questions

gpg --quiet --batch --yes --decrypt --passphrase="$LARGE_SECRET_PASSPHRASE" \
--output ./keystore.properties ./.github/secrets/keystore.properties.gpg

gpg --quiet --batch --yes --decrypt --passphrase="$LARGE_SECRET_PASSPHRASE" \
--output ./note_room_key_store.jks ./.github/secrets/note_room_key_store.jks.gpg

gpg --quiet --batch --yes --decrypt --passphrase="$LARGE_SECRET_PASSPHRASE" \
--output ./app/google-services.json ./.github/secrets/google-services.json.gpg
