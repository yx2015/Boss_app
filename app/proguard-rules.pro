# Add project specific ProGuard rules here.
# By default, the flags in this file are appended to flags specified
# in F:\workspace\AndroidStudio\sdk/tools/proguard/proguard-android.txt
# You can edit the include path and order by changing the proguardFiles
# directive in build.gradle.
#
# For more details, see
#   http://developer.android.com/guide/developing/tools/proguard.html

# Add any project specific keep options here:

# If your project uses WebView with JS, uncomment the following
# and specify the fully qualified class name to the JavaScript interface
# class:
-keepclassmembers class fqcn.of.javascript.interface.for.webview {
   public *;
}
-ignorewarnings						# 忽略警告，避免打包时某些警告出现
-optimizationpasses 5				# 指定代码的压缩级别
-dontusemixedcaseclassnames			# 是否使用大小写混合
-dontskipnonpubliclibraryclasses	# 是否混淆第三方jar
-dontpreverify                      # 混淆时是否做预校验
-verbose                            # 混淆时是否记录日志
-optimizations !code/simplification/arithmetic,!field/*,!class/merging/*        # 混淆时所采用的算法

-dontwarn android.support.v4.**
-dontwarn android.support.v7.**
-dontwarn android.os.**
-keep class android.support.v4.** { *; }
-keep class android.support.v7.** { *; }
-keep class android.os.**{*;}

-keep class com.google.** { *; }
-keepclassmembers class * extends java.io.Serializable {
    static final long serialVersionUID;
    private static final java.io.ObjectStreamField[] serialPersistentFields;
    private void writeObject(java.io.ObjectOutputStream);
    private void readObject(java.io.ObjectInputStream);
    java.lang.Object writeReplace();
    java.lang.Object readResolve();
}
##---------------Begin: proguard configuration for Gson  ----------
# Gson uses generic type information stored in a class file when working with fields. Proguard
# removes such information by default, so configure it to keep all of it.
-keepattributes Signature

# For using GSON @Expose annotation
-keepattributes *Annotation*

# Gson specific classes
-keep class sun.misc.Unsafe { *; }
#-keep class com.google.gson.stream.** { *; }

# Application classes that will be serialized/deserialized over Gson
-keep class com.xyl.boss_app.bean.** { *; }

-keep class com.google.gson.** { *; }
##---------------End: proguard configuration for Gson  ----------

#友盟统计
-dontwarn com.umeng.**
-keep public interface com.umeng.analytics.**
-keep public class com.umeng.analytics.* {*;}
-keepclassmembers class * {
   public <init>(org.json.JSONObject);
}

-keep interface android.support.v4.app.** { *; }
-keep interface android.support.v7.app.** { *; }
-keep public class * extends android.support.v4.**
-keep public class * extends android.support.v7.**
-keep public class * extends android.app.Fragment

-keep public class * extends android.app.Activity
-keep public class * extends android.app.Application
-keep public class * extends android.app.Service
-keep public class * extends android.content.BroadcastReceiver
-keep public class * extends android.content.ContentProvider
-keep public class * extends android.support.v4.widget
-keep public class * extends android.support.v7.widget
-keep public class * extends com.sqlcrypt.database
-keep public class * extends com.sqlcrypt.database.sqlite


-keepclasseswithmembernames class * {		# 保持 native 方法不被混淆
    native <methods>;
}

-keepclasseswithmembers class * {			 # 保持自定义控件类不被混淆
    public <init>(android.content.Context, android.util.AttributeSet);
}

-keepclasseswithmembers class * {			 # 保持自定义控件类不被混淆
    public <init>(android.content.Context, android.util.AttributeSet, int);
}

-keepclassmembers class * extends android.app.Activity { # 保持类成员
   public void *(android.view.View);
}

-keepclassmembers enum * {					# 保持枚举 enum 类不被混淆
    public static **[] values();
    public static ** valueOf(java.lang.String);
}

-keep class * implements android.os.Parcelable {	# 保持 Parcelable 不被混淆
  public static final android.os.Parcelable$Creator *;
}

-assumenosideeffects class android.util.Log {
    public static *** e(...);
    public static *** w(...);
    public static *** wtf(...);
    public static *** d(...);
    public static *** v(...);
}

#####################################
######### 第三方库或者jar包 ###########
#####################################
-dontwarn butterknife.**
-dontwarn butterknife.internal.**
-keep class butterknife.** { *; }
-keep class **$$ViewBinder { *; }

-keepclasseswithmembernames class * {
    @butterknife.* <fields>;
}

-keepclasseswithmembernames class * {
    @butterknife.* <methods>;
}
-keep class **$$ViewInjector { *; }
-keepnames class * { @butterknife.InjectView *;}

#-dontwarn com.daimajia.numberprogressbar.**
#-keep class com.daimajia.numberprogressbar.** { *; }

-dontwarn de.**
-keep class de.** { *; }
-keepclassmembers class ** {
    public void onEvent*(**);
    void onEvent*(**);
}

#-dontwarn com.bumptech.glide.**
#-keep class com.bumptech.glide.** { *; }
#
#-dontwarn com.appeaser.sublimepickerlibrary.**
#-keep class com.appeaser.sublimepickerlibrary.** { *; }
#
#-dontwarn com.nineoldandroids.**
#-keep class com.nineoldandroids.** { *; }
#
#-dontwarn com.android.volley.**
#-keep class com.android.volley.** { *; }
#
#-dontwarn com.hxl.universallibrary.**
#-keep class com.hxl.universallibrary.** { *; }
#
#-dontwarn com.prolificinteractive.materialcalendarview.**
#-keep class com.prolificinteractive.materialcalendarview.** { *; }
#
#-dontwarn com.hxl.multi_image_selector.**
#-keep class com.hxl.multi_image_selector.** { *; }

-dontwarn com.squareup.**
-keep class com.squareup.** { *; }

#-dontwarn okio.**
#-keep class okio.** { *; }

#-dontwarn com.readystatesoftware.systembartint.**
#-keep class com.readystatesoftware.systembartint.** { *; }

-dontwarn com.lidroid.xutils.**
-keep class com.lidroid.xutils.** { *; }

-dontwarn com.github.mikephil.charting.**
-keep class com.github.mikephil.charting.** { *; }

#错误提示不混淆
-keepattributes SourceFile,LineNumberTable


