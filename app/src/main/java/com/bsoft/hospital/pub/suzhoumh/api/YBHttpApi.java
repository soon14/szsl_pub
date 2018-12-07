package com.bsoft.hospital.pub.suzhoumh.api;

import android.content.Context;

import com.alibaba.fastjson.JSON;
import com.app.tanklib.AppContext;
import com.app.tanklib.Preferences;
import com.app.tanklib.http.BsoftNameValuePair;
import com.app.tanklib.util.MD5;
import com.bsoft.hospital.pub.suzhoumh.Constants;
import com.bsoft.hospital.pub.suzhoumh.model.LoginUser;
import com.bsoft.hospital.pub.suzhoumh.model.CheckCodeVo;
import com.bsoft.hospital.pub.suzhoumh.util.AppLogger;
import com.bsoft.hospital.pub.suzhoumh.util.aes.AESUtil;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * 类说明
 *
 * Author: 嘿嘿抛物线
 * Email: 497635745@qq.com
 * Date: 2016-8-14 11:07
 */
public class YBHttpApi {

    AppHttpClient httpClient;

    public YBHttpApi() {

    }

    public YBHttpApi(AppHttpClient httpClient) {
        this.httpClient = httpClient;
    }

    public static YBHttpApi getInstance() {
        Context localContext = AppContext.getContext();
        YBHttpApi api = (YBHttpApi) localContext.getSystemService("com.bsoft.app.ybhttpapi");
        if (api == null)
            api = (YBHttpApi) localContext.getApplicationContext().getSystemService("com.bsoft.app.ybhttpapi");
        if (api == null)
            throw new IllegalStateException("api not available");
        return api;
    }

    //HIS url 封装
    private String postHis(String url, BsoftNameValuePair... nameValuePairs) {
        try {
            String json = httpClient.doHttpPost(url, getPair(nameValuePairs));
            AppLogger.i(json);
            return json;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public ArrayList<BsoftNameValuePair> getPair(BsoftNameValuePair... nameValuePairs) {
        ArrayList<BsoftNameValuePair> temp = new ArrayList<BsoftNameValuePair>();
        for (int i = 0; i < nameValuePairs.length; i++) {
            if (!"id".equals(nameValuePairs[i].getName()) && !"sn".equals(nameValuePairs[i].getName())) {
                temp.add(nameValuePairs[i]);
            }
        }

        LoginUser loginUser = null;
        try {
            String json = Preferences.getInstance().getStringData("loginUser");
            json = AESUtil.decrypt(json);
            loginUser = JSON.parseObject(json, LoginUser.class);
        } catch (Exception e) {

        }
        temp.add(new BsoftNameValuePair("timestamp", String.valueOf(System.currentTimeMillis())));
        temp.add(new BsoftNameValuePair("utype", "1"));
        if (loginUser != null)
            temp.add(new BsoftNameValuePair("token", loginUser.token));
//		temp.add(new BsoftNameValuePair("sn",loginUser.sn));
        temp.add(new BsoftNameValuePair("device", Preferences.getInstance().getStringData("deviceId")));

        Collections.sort(temp);
        StringBuffer signSb = new StringBuffer();
        for (BsoftNameValuePair p : temp) {
            signSb.append(p.getValue());
        }
        String signUtf8 = "";
        try {
            String ss = signSb.toString();
            AppLogger.i(ss);
            String signStr = new String(signSb.toString().getBytes("UTF-8"));
            signUtf8 = URLEncoder.encode(signStr, "UTF-8");
            AppLogger.i("utf-8 编码：" + signUtf8);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        for (BsoftNameValuePair vo : temp) {
            System.out.println(vo.getName() + "=" + vo.getValue());
        }


        ArrayList<BsoftNameValuePair> pa = new ArrayList<BsoftNameValuePair>(temp);
        pa.add(new BsoftNameValuePair("sign", MD5.getMD5(signUtf8)));
        if (loginUser != null)
            pa.add(new BsoftNameValuePair("sn", loginUser.sn));


//		ArrayList<BsoftNameValuePair> pa = new ArrayList<BsoftNameValuePair>();
//
//		String timestamp = System.currentTimeMillis() + "";
//		String sign = "";
//
//		StringBuffer signSb = new StringBuffer();
//		signSb.append(timestamp);
//
//		if (null != nameValuePairs && nameValuePairs.length > 0) {
//			// 排序
//			if (nameValuePairs.length > 1) {
//				Arrays.sort(nameValuePairs);
//			}
//			for (int i = 0; i < nameValuePairs.length; i++) {
//				if ("id".equals(nameValuePairs[i].getName())) {
//					signSb.append(";").append(nameValuePairs[i].getValue());
//				} else if ("sn".equals(nameValuePairs[i].getName())) {
////					signSb.append(";").append(nameValuePairs[i].getValue());
//					pa.add(nameValuePairs[i]);
//				} else {
//					pa.add(nameValuePairs[i]);
//				}
//			}
//		}
//		sign = MD5.getMD5(signSb.toString());
//		pa.add(new BsoftNameValuePair("timestamp", timestamp));
//		pa.add(new BsoftNameValuePair("sign", sign));
//		pa.add(new BsoftNameValuePair("utype", "1"));
        return pa;
    }

    /**
     * 调用His接口解析成对象
     *
     * @param
     * @param method
     * @return
     */
    public CheckCodeVo parserData_His(String method, Map<String, String> map, BsoftNameValuePair... nameValuePairs) {
        CheckCodeVo result = new CheckCodeVo();
        String json = null;
        try {
            String url = Constants.getHttpUrl() + method;
            ArrayList<BsoftNameValuePair> pa = new ArrayList<BsoftNameValuePair>();
            if (nameValuePairs != null && nameValuePairs.length > 0) {
                for (int i = 0; i < nameValuePairs.length; i++) {
                    pa.add(nameValuePairs[i]);
                }
            }

            if (map != null) {
                map = setMapData(map);
                for (Map.Entry<String, String> entry : map.entrySet()) {
                    pa.add(new BsoftNameValuePair(entry.getKey(), entry.getValue()));
                }
            }
            BsoftNameValuePair[] pairs = new BsoftNameValuePair[pa.size()];
            for (int i = 0; i < pairs.length; i++) {
                pairs[i] = pa.get(i);
            }
            json = postHis(url, pairs);

        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
        if (null != json) {
            try {
                System.out.println(json);
                result = JSON.parseObject(json, CheckCodeVo.class);
            } catch (Exception e) {
            }
            return result;
        } else {
            return null;
        }
    }

    /**
     * 苏州中医院调用his接口特有的方式
     *
     * @param map
     */
    private Map<String, String> setMapData(Map<String, String> map) {
        HashMap<String, String> map2 = new HashMap<String, String>();
        try {
            String params = "";
            String values = "";
            Set<Map.Entry<String, String>> sets = map.entrySet();
            int i = 1;
            for (Map.Entry<String, String> entry : sets) {
                if (entry.getKey().equals("method")) {
                    map2.put("method", entry.getValue());
                } else {
                    params += entry.getKey() + ",";
                    values += entry.getValue() + ",";
                }
                i++;
            }
            if (!params.equals("")) {
                params = params.substring(0, params.length() - 1);
            }
            if (!values.equals("")) {
                values = values.substring(0, values.length() - 1);
            }
            map2.put("params", params);
            map2.put("values", values);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return map2;
    }
}
