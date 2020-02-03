-include proguard-rules.pro

-dontoptimize
-dontobfuscate
-dontpreverify
-printusage

-keep interface kotlinx.coroutines.internal.MainDispatcherFactory

-keep class kotlinx.coroutines.test.internal.** {
    *;
}

-keepclassmembers class * {
    static final %                                  *;
    static final java.lang.Iterable                 *;
    static final kotlin.sequences.Sequence          *;
    static final kotlin.collections.CollectionsKt   *;
}

-keep class kotlin.collections.CollectionsKt {
    *;
}

-keep public class kotlin.collections.** {
    *;
}

-keep class androidx.test.espresso.IdlingRegistry {
    *;
}

-keep class androidx.test.internal.runner.junit4.AndroidJUnit4ClassRunner {
    *;
}

-keep class androidx.test.runner.AndroidJUnitRunner {
    *;
}