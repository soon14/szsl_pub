# Add project specific ProGuard rules here.
# You can control the set of applied configuration files using the
# proguardFiles setting in build.gradle.
#
# For more details, see
#   http://developer.android.com/guide/developing/tools/proguard.html

# If your project uses WebView with JS, uncomment the following
# and specify the fully qualified class name to the JavaScript interface
# class:
#-keepclassmembers class fqcn.of.javascript.interface.for.webview {
#   public *;
#}

# Uncomment this to preserve the line number information for
# debugging stack traces.
#-keepattributes SourceFile,LineNumberTable

# If you keep the line number information, uncomment this to
# hide the original source file name.
#-renamesourcefileattribute SourceFile

#-ignorewarnings  #忽略警告
#-keepattributes Exceptions
#-------------------------------------------基本不用动区域--------------------------------------------
 #---------------------------------基本指令区----------------------------------
 #指定压缩级别
 -optimizationpasses 5
 #不跳过非公共的库的类成员
 -dontskipnonpubliclibraryclassmembers
 -printmapping proguardMapping.txt
 #混淆时采用的算法
 -optimizations !code/simplification/cast,!field/*,!class/merging/*
 -keepattributes *Annotation*,InnerClasses
 -keepattributes Signature
 #把混淆类中的方法名也混淆了
 -useuniqueclassmembernames

 #优化时允许访问并修改有修饰符的类和类的成员
 -allowaccessmodification

 #将文件来源重命名为“SourceFile”字符串
 -renamesourcefileattribute SourceFile
 #保留行号
 -keepattributes SourceFile,LineNumberTable
 #----------------------------------------------------------------------------


 #---------------------------------默认保留区---------------------------------
 #继承activity,application,service,broadcastReceiver,contentprovider....不进行混淆
 -keep public class * extends android.app.Activity
 -keep public class * extends android.app.Application
 -keep public class * extends android.support.multidex.MultiDexApplication
 -keep public class * extends android.app.Service
 -keep public class * extends android.content.BroadcastReceiver
 -keep public class * extends android.content.ContentProvider
 -keep public class * extends android.app.backup.BackupAgentHelper
 -keep public class * extends android.preference.Preference
 -keep public class * extends android.view.View
 -keep public class com.android.vending.licensing.ILicensingService
 -keep class android.support.** {*;}
 #Fragment不需要在AndroidManifest.xml中注册，需要额外保护下
 -keep public class * extends android.support.v4.app.Fragment
 -keep public class * extends android.app.Fragment

 # 保持测试相关的代码
 -dontnote junit.framework.**
 -dontnote junit.runner.**
 -dontwarn android.test.**
 -dontwarn android.support.test.**
 -dontwarn org.junit.**

 -keep public class * extends android.view.View{
     *** get*();
     void set*(***);
     public <init>(android.content.Context);
     public <init>(android.content.Context, android.util.AttributeSet);
     public <init>(android.content.Context, android.util.AttributeSet, int);
 }

 #保持自定义控件类不被混淆
 -keepclasseswithmembers class * {
     public <init>(android.content.Context, android.util.AttributeSet);
     public <init>(android.content.Context, android.util.AttributeSet, int);
 }
 #这个主要是在layout 中写的onclick方法android:onclick="onClick"，不进行混淆
 -keepclassmembers class * extends android.app.Activity {
    public void *(android.view.View);
 }

 #保持所有实现 Serializable 接口的类成员
 -keepclassmembers class * implements java.io.Serializable {
     static final long serialVersionUID;
     private static final java.io.ObjectStreamField[] serialPersistentFields;
     private void writeObject(java.io.ObjectOutputStream);
     private void readObject(java.io.ObjectInputStream);
     java.lang.Object writeReplace();
     java.lang.Object readResolve();
 }
 #不混淆资源类
 -keep class **.R$* {
  *;
 }

 -keepclassmembers class * {
     void *(*Event);
 }

 -keepclassmembers enum * {
     public static **[] values();
     public static ** valueOf(java.lang.String);
 }

 #// natvie 方法不混淆
 -keepclasseswithmembernames class * {
     native <methods>;
 }

 #保持 Parcelable 不被混淆
 -keep class * implements android.os.Parcelable {
   public static final android.os.Parcelable$Creator *;
 }


 -dontwarn android.net.**
 -keep class android.net.SSLCertificateSocketFactory{*;}

 -keepattributes SourceFile,LineNumberTable
 -keep class com.parse.*{ *; }
 -dontwarn com.parse.**
 -dontwarn com.squareup.picasso.**
 -keepclasseswithmembernames class * {
     native <methods>;
 }
#############################################
#
# 运行错误
#
#############################################
-keep class okhttp3.** { *; }
-keep interface okhttp3.** { *; }
-dontwarn okhttp3.**

-keep class okio.** { *; }
-keep interface okio.** { *; }
-dontwarn okio.**

#############################################

# webView处理，项目中没有使用到webView忽略即可
-keepclassmembers class fqcn.of.javascript.interface.for.webview {
    public *;
}
-keepclassmembers class * extends android.webkit.webViewClient {
    public void *(android.webkit.WebView, java.lang.String, android.graphics.Bitmap);
    public boolean *(android.webkit.WebView, java.lang.String);
}
-keepclassmembers class * extends android.webkit.webViewClient {
    public void *(android.webkit.webView, jav.lang.String);
}

#支付宝
-keep class com.alipay.android.app.IAlixPay{*;}
-keep class com.alipay.android.app.IAlixPay$Stub{*;}
-keep class com.alipay.android.app.IRemoteServiceCallback{*;}
-keep class com.alipay.android.app.IRemoteServiceCallback$Stub{*;}
-keep class com.alipay.sdk.app.PayTask{ public *;}
-keep class com.alipay.sdk.app.AuthTask{ public *;}
-keep class com.alipay.sdk.app.H5PayCallback {
    <fields>;
    <methods>;
}
-keep class com.alipay.android.phone.mrpc.core.** { *; }
-keep class com.alipay.apmobilesecuritysdk.** { *; }
-keep class com.alipay.mobile.framework.service.annotation.** { *; }
-keep class com.alipay.mobilesecuritysdk.face.** { *; }
-keep class com.alipay.tscenter.biz.rpc.** { *; }
-keep class org.json.alipay.** { *; }
-keep class com.alipay.tscenter.** { *; }
-keep class com.ta.utdid2.** { *;}
-keep class com.ut.device.** { *;}


-dontwarn com.baidu.**
-keep class com.baidu.** { *;}

-dontwarn vi.com.gdi.bgl.android.java.**
-keep class vi.com.gdi.bgl.android.java.** { *;}

-dontwarn org.dom4j.**
-keep class org.dom4j.** { *;}

-dontwarn com.github.mikephil.charting.**
-keep class com.github.mikephil.charting.** { *;}

-dontwarn com.nineoldandroids.**
-keep class com.nineoldandroids.** { *;}

-dontwarn com.hp.hpl.sparta.**
-keep class com.hp.hpl.sparta.** { *;}

-dontwarn net.sourceforge.pinyin4j.**
-keep class net.sourceforce.pinyin4j.** { *;}

-dontwarn com.nostra13.universalimageloader.**
-keep class com.nostra13.universalimageloader.** { *;}

-dontwarn demo.Pinyin4jAppletDemo*
-keepclasseswithmembers class demo.Pinyin4jAppletDemo {
    <fields>;
    <methods>;
}

-keep class com.bsoft.hospital.pub.suzhoumh.model.** { *;}
-keep class * extends com.app.tanklib.model.AbsBaseVoSerializ { *;}

-keep class com.bsoft.hospital.pub.suzhoumh.util.** { *;}

-dontwarn com.alibaba.fastjson.**
-keep class com.alibaba.fastjson.** { *;}

-dontwarn android.support.v4.**
-keep class android.support.v4.** { *;}

-dontwarn com.app.tanklib.**
-keep class com.app.tanklib.** { *;}

#-keep class com.bsoft.hospital.pub.suzhoumh.api.** { *;}

-keep public class com.xxx.util.model.AppInfo{ *;}

# Gson specific classes
-keep class sun.misc.Unsafe { *; }
#-keep class com.google.gson.stream.** { *; }

# Application classes that will be serialized/deserialized over Gson
-keep class com.google.gson.examples.android.model.** { *; }

-keepclassmembers class * implements android.os.Parcel { public *;}

# 院内导航
-dontwarn com.baidu.**
-keep class com.baidu.** {*;}
-dontwarn com.iflytek.**
-keep class com.iflytek.**{*;}
-keep public class com.sails.engine.patterns.IconPatterns

# hirondelle.date4j
-keep class hirondelle.date4j.**{*;}

# butterknife
-keep class butterknife.*
-keepclasseswithmembernames class * { @butterknife.* <methods>; }
-keepclasseswithmembernames class * { @butterknife.* <fields>; }

# glide
-keep public class * implements com.bumptech.glide.module.GlideModule
-keep public enum com.bumptech.glide.load.resource.bitmap.ImageHeaderParser$** {
  **[] $VALUES;
  public *;
}

#BaseRecyclerViewAdapterHelper
-keep class com.chad.library.adapter.** {
*;
}
-keep public class * extends com.chad.library.adapter.base.BaseQuickAdapter
-keep public class * extends com.chad.library.adapter.base.BaseViewHolder
-keepclassmembers  class **$** extends com.chad.library.adapter.base.BaseViewHolder {
     <init>(...);
}

#eventBus
-keepattributes *Annotation*
-keepclassmembers class * {
    @org.greenrobot.eventbus.Subscribe <methods>;
}
-keep enum org.greenrobot.eventbus.ThreadMode { *; }

# Only required if you use AsyncExecutor
-keepclassmembers class * extends org.greenrobot.eventbus.util.ThrowableFailureEvent {
    <init>(java.lang.Throwable);
}

