# Add project specific ProGuard rules here.
# By default, the flags in this file are appended to flags specified
# in /home/student/android-sdk-linux/tools/proguard/proguard-android.txt
# You can edit the include path and order by changing the proguardFiles
# directive in build.gradle.
#
# For more details, see
#   http://developer.android.com/guide/developing/tools/proguard.html

# Add any project specific keep options here:

# If your project uses WebView with JS, uncomment the following
# and specify the fully qualified class name to the JavaScript interface
# class:
#-keepclassmembers class fqcn.of.javascript.interface.for.webview {
#   public *;
#}

-keepattributes *Annotation*, InnerClasses
-dontnote kotlinx.serialization.SerializationKt
-keep,includedescriptorclasses class com.kjipo.**$$serializer { *; } # <-- change package name to your app's
-keepclassmembers class com.kjipo.** { # <-- change package name to your app's
    *** Companion;
}
-keepclasseswithmembers class com.kjipo.** { # <-- change package name to your app's
    kotlinx.serialization.KSerializer serializer(...);
}
