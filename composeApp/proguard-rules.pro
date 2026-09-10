-dontwarn com.vividsolutions.jts.**
-dontwarn android.**
-dontwarn org.conscrypt.**
-dontwarn org.bouncycastle.jsse.**
-dontwarn org.openjsse.**
-dontwarn org.joda.convert.**
-dontwarn edu.umd.cs.findbugs.annotations.**
-keep class com.sun.jna.** { *; }
-keepclassmembers class com.sun.jna.** { *; }
-dontwarn com.sun.jna.**
-keep class oshi.** { *; }
-keepclassmembers class oshi.** { *; }
-dontwarn oshi.**
-keep class org.freedesktop.dbus.** { *; }
-keepclassmembers class org.freedesktop.dbus.** { *; }
-dontwarn org.freedesktop.dbus.**
-keep class * extends com.sun.jna.Structure { *; }
-keepclassmembers class * extends com.sun.jna.Structure {
    <fields>;
}
-keep class * implements com.sun.jna.Callback { *; }

-keep class widgets.domain.JNA.** { *; }
-keepclassmembers class widgets.domain.JNA.** { *; }
-dontwarn widgets.domain.JNA.**
-keep class io.ktor.serialization.kotlinx.** { *; }
-keepclassmembers class io.ktor.serialization.kotlinx.** { *; }
-dontwarn io.ktor.serialization.kotlinx.**
-keep class io.ktor.** implements io.ktor.serialization.kotlinx.KotlinxSerializationExtensionProvider { *; }

-keep class coil3.** { *; }
-keep interface coil3.** { *; }
-keepclassmembers class coil3.** { *; }
-dontwarn coil3.**
-dontoptimize