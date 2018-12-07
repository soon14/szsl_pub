package com.bsoft.hospital.pub.suzhoumh.util;

import android.content.Context;
import android.content.pm.PackageManager;
import android.content.pm.Signature;


/**
 * 签名工具类
 * Created by Administrator on 2016/4/22.
 */
public class SignUtil {
    public static final int LOCAL_SIGN_HASCODE = 1094463294;

    public static int getSign(Context context) throws PackageManager.NameNotFoundException {
        Signature[] sigs = context.getPackageManager().getPackageInfo(context.getPackageName(), PackageManager.GET_SIGNATURES).signatures;
        if(sigs.length > 0)
            return sigs[0].hashCode();
        return 0;
    }
    
//    public static String getSignInfo(Context context){
//        try {
//            PackageInfo packageInfo = context.getPackageManager().getPackageInfo(context.getPackageName(), PackageManager.GET_SIGNATURES);
//            Signature[] signs = packageInfo.signatures;
//            return parseSignature(signs[0].toByteArray());
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//        return null;
//    }
//
//    private static String parseSignature(byte[] signature) {
//        try {
//            CertificateFactory certFactory = CertificateFactory.getInstance("X.509");
//            X509Certificate cert = (X509Certificate) certFactory.generateCertificate(new ByteArrayInputStream(signature));
//            String s = new String(cert.getEncoded());
//            return s;
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//        return null;
//    }



}
