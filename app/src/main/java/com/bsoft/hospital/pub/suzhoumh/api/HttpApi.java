package com.bsoft.hospital.pub.suzhoumh.api;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.alibaba.fastjson.JSON;
import com.app.tanklib.AppContext;
import com.app.tanklib.Preferences;
import com.app.tanklib.bitmap.CacheManage;
import com.app.tanklib.http.BsoftNameValuePair;
import com.app.tanklib.util.MD5;
import com.app.tanklib.util.StringUtil;
import com.bsoft.hospital.pub.suzhoumh.AppApplication;
import com.bsoft.hospital.pub.suzhoumh.Constants;
import com.bsoft.hospital.pub.suzhoumh.model.LoginUser;
import com.bsoft.hospital.pub.suzhoumh.model.NullModel;
import com.bsoft.hospital.pub.suzhoumh.model.PageList;
import com.bsoft.hospital.pub.suzhoumh.model.ResultModel;
import com.bsoft.hospital.pub.suzhoumh.model.Statue;
import com.bsoft.hospital.pub.suzhoumh.push.PushInfo;
import com.bsoft.hospital.pub.suzhoumh.util.AppLogger;
import com.bsoft.hospital.pub.suzhoumh.util.ContextUtils;
import com.bsoft.hospital.pub.suzhoumh.util.SpUtil;
import com.bsoft.hospital.pub.suzhoumh.util.aes.AESUtil;

/**
 * @author Tank E-mail:zkljxq@126.com
 * @类说明
 */
public class HttpApi {

//    public static String url = Constants.HttpApiUrl;


//	public static String url_his = Constants.HttpHisUrl;

    AppHttpClient httpClient;

    public HttpApi() {

    }

    public HttpApi(AppHttpClient httpClient) {
        this.httpClient = httpClient;
    }

    public static HttpApi getInstance() {
        Context localContext = AppContext.getContext();
        HttpApi api = (HttpApi) localContext.getSystemService("com.bsoft.app.httpapi");
        if (api == null)
            api = (HttpApi) localContext.getApplicationContext().getSystemService("com.bsoft.app.httpapi");
        if (api == null)
            throw new IllegalStateException("api not available");
        return api;
    }

    /**
     * 得到链接地址
     *
     * @param url
     * @return
     */
    public static String getUrl(String url) {
        return getImageUrl(url, null, 0);
    }

    /**
     * 得到图片地址，因为平台提供的图片地址是相对地址
     *
     * @param url
     * @return
     */
    public static String getImageUrl(String url, int type) {
        return getImageUrl(url, null, type);
    }

    /**
     * 得到图片地址，带有后缀
     *
     * @param url
     * @param suffix
     * @return
     */
    public static String getImageUrl(String url, String suffix, int type) {
        if (StringUtil.isEmpty(url)) {
            return null;
        }
        switch (type) {
            case CacheManage.IMAGE_TYPE_PROGRESS:
                if (AppApplication.getWidthPixels() <= 750) {
                    suffix = "_750x500";
                } else {
                    suffix = "_1200x800";
                }
                break;
            default:
                break;
        }
        if (StringUtil.isEmpty(suffix)) {
            StringBuffer sb = new StringBuffer();
//            sb.append(Constants.HttpUrl).append(url);
            sb.append(Constants.getHttpUrl()).append(url);
            return sb.toString();
        } else {
            StringBuffer sb = new StringBuffer();
//            sb.append(Constants.HttpUrl);
            sb.append(Constants.getHttpUrl());
            sb.append(url.substring(0, url.lastIndexOf(".")));
            sb.append(suffix);
            sb.append(url.substring(url.lastIndexOf("."), url.length()));
            return sb.toString();
        }
    }

    // 健康平台Post请求封装
    public String post(String method, BsoftNameValuePair... nameValuePairs) {
        try {
//            String json = httpClient.doHttpPost(url + method, getPair(nameValuePairs));
            String json = httpClient.doHttpPost(Constants.getHttpUrl() + "api/" + method, getPair(nameValuePairs));
            // String json = httpClient.executeHttpRequests(newUrl + method,
            // getPair(nameValuePairs));
//			System.out.println(" json  " + json);
            AppLogger.e(json);
            return json;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }


    public String postOther(String method, BsoftNameValuePair... nameValuePairs) {
        try {
//            String json = httpClient.doHttpPost(Constants.HttpUrl + method, getPair(nameValuePairs));
            String json = httpClient.doHttpPost(Constants.getHttpUrl() + method, getPair(nameValuePairs));
            // String json = httpClient.executeHttpRequests(newUrl + method,
            // getPair(nameValuePairs));
//			System.out.println(" json  " + json);
            AppLogger.e(json);
            return json;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    // 上传头像
    public String postHeader(String method, String header, BsoftNameValuePair... nameValuePairs) {
        try {
            ArrayList<BsoftNameValuePair> pa = new ArrayList<BsoftNameValuePair>();
            if (null != nameValuePairs && nameValuePairs.length > 0) {
                for (int i = 0; i < nameValuePairs.length; i++) {
                    pa.add(nameValuePairs[i]);
                }
            }

            String json = httpClient.doHttpPostHeader(Constants.getHttpUrl() + "api/" + method, header, pa);
//			System.out.println(" json  " + json);
            return json;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    // 上传就诊记录
    public String postEhr(String method, String[] recipes, String[] reports, BsoftNameValuePair... nameValuePairs) {
        try {
            ArrayList<BsoftNameValuePair> pa = new ArrayList<BsoftNameValuePair>();
            if (null != nameValuePairs && nameValuePairs.length > 0) {
                for (int i = 0; i < nameValuePairs.length; i++) {
                    pa.add(nameValuePairs[i]);
                }
            }
            String json = httpClient.doHttpPostEhr(Constants.getHttpUrl() + "api/" + method, recipes, reports, pa);
//			System.out.println(" json  " + json);
            return json;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    // 上传咨询图片
    public String postConsultPic(String method, String[] sysfiles, BsoftNameValuePair... nameValuePairs) {
        try {
            /*ArrayList<BsoftNameValuePair> pa = new ArrayList<BsoftNameValuePair>();
            if (null != nameValuePairs && nameValuePairs.length > 0) {
				for (int i = 0; i < nameValuePairs.length; i++) {
					pa.add(nameValuePairs[i]);
				}
			}*/
            String json = httpClient.doHttpPostConsultPic(Constants.getHttpUrl() + "api/" + method, sysfiles, getPair(nameValuePairs));
//			System.out.println(" json  " + json);
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
            AppLogger.e(ss);
            String signStr = new String(signSb.toString().getBytes("UTF-8"));
            signUtf8 = URLEncoder.encode(signStr, "UTF-8");
            AppLogger.e("utf-8 编码：" + signUtf8);
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
        return pa;
    }


    // 状态判断
    public boolean status(JSONObject ob) {
        /*
         * if (null != ob && !ob.isNull("status")) { try { return
		 * ob.getInt("status") == 1; } catch (JSONException e) {
		 * e.printStackTrace(); } }
		 */
        boolean result = false;
        if (null != ob) {
            if (ob.optInt("code") == -200 || ob.optInt("code") == -500) {
                PushInfo info = new PushInfo();
                info.description = "您的账号在其他设备上登陆!";
                info.title = "提示";
                info.login = 1;
                Intent logoutIntent = new Intent(Constants.Logout_ACTION);
                logoutIntent.putExtra("pushInfo", info);
                AppContext.getContext().sendBroadcast(logoutIntent);
//                AppContext.getContext().sendBroadcast(
//                        new Intent(Constants.CLOSE_ACTION));
                return false;
            }
            if (ob.optInt("code") == 403) {
                PushInfo info = new PushInfo();
                info.description = "账号验证失败，请重新登录!";
                info.title = "提示";
                info.login = 1;
                Intent logoutIntent = new Intent(Constants.Logout_ACTION);
                logoutIntent.putExtra("pushInfo", info);
                AppContext.getContext().sendBroadcast(logoutIntent);
//                AppContext.getContext().sendBroadcast(
//                        new Intent(Constants.CLOSE_ACTION));
                return false;
            }
            result = ob.optInt("code", -1) == 1;
        }
        return result;
    }

    /**
     * @param nameValuePairs
     * @return
     */
    public PageList<NullModel> postEhr(PageList<NullModel> mList, String _method, String[] recipes, String[] reports, BsoftNameValuePair... nameValuePairs) {
        String json = null;
        try {
            json = postEhr(_method, recipes, reports, nameValuePairs);
        } catch (Exception e) {
            e.printStackTrace();
            mList.statue = Statue.NET_ERROR;
            return mList;
        }
        if (null != json) {
            try {
                JSONObject ob = new JSONObject(json);
                if (status(ob)) {
                    mList.statue = Statue.SUCCESS;
                } else {
                    mList.statue = Statue.ERROR;
                    if (!ob.isNull("msg")) {
                        mList.message = ob.getString("msg");
                    }
                    return mList;
                }
            } catch (JSONException e) {
                e.printStackTrace();
                mList.statue = Statue.PARSER_ERROR;
                return mList;
            }
        }
        return mList;
    }

    /**
     * 上传头像
     *
     * @param nameValuePairs
     * @return
     */
    public ResultModel<String> postHeader(String header, BsoftNameValuePair... nameValuePairs) {
        ResultModel<String> mList = new ResultModel<String>();
        String json = null;
        try {
            json = postHeader("upload/header", header, nameValuePairs);
        } catch (Exception e) {
            e.printStackTrace();
            mList.statue = Statue.NET_ERROR;
            return mList;
        }
        if (null != json) {
            try {
                JSONObject ob = new JSONObject(json);
                if (status(ob)) {
                    if (!ob.isNull("data")) {
                        mList.statue = Statue.SUCCESS;
                        mList.data = ob.getString("data");
                    } else {
                        mList.statue = Statue.ERROR;
                        if (!ob.isNull("msg")) {
                            mList.message = ob.getString("msg");
                        }
                        return mList;
                    }
                } else {
                    mList.statue = Statue.ERROR;
                    if (!ob.isNull("msg")) {
                        mList.message = ob.getString("msg");
                    }
                    return mList;
                }
            } catch (JSONException e) {
                e.printStackTrace();
                mList.statue = Statue.PARSER_ERROR;
                return mList;
            }
        }
        return mList;
    }

    public String get(String _url) throws Exception {
        return httpClient.executeHttpRequests(_url);
    }

    public void get(String _method, BsoftNameValuePair... nameValuePairs) {
        String json = null;
        try {
            json = post(_method, nameValuePairs);
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (null != json) {
            try {
                JSONObject ob = new JSONObject(json);
                status(ob);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 调用平台接口解析成对象
     *
     * @param clazz
     * @param method
     * @param nameValuePairs
     * @return
     */
    public ResultModel parserData(Class clazz, String method, BsoftNameValuePair... nameValuePairs) {
        ResultModel mList = new ResultModel();
        String json = null;
        try {
            json = post(method, nameValuePairs);
        } catch (Exception e) {
            e.printStackTrace();
            mList.statue = Statue.NET_ERROR;
            return mList;
        }
        if (null != json) {
            try {
//				System.out.println(json);
                JSONObject ob = new JSONObject(json);
                if (status(ob)) {
                    mList.statue = Statue.SUCCESS;
                    if (!ob.isNull("warn")) {
                        mList.warn = ob.getString("warn");
                    }
                    if (!ob.isNull("data")) {
                        mList.data = JSON.parseObject(ob.getString("data"), clazz);
                        return mList;
                    } else {
                        mList.message = "数据为空";
                        return mList;
                    }
                } else {
                    mList.statue = Statue.ERROR;
                    if (!ob.isNull("msg")) {
                        mList.message = ob.getString("msg");
                    }
                    return mList;
                }
            } catch (JSONException e) {
                e.printStackTrace();
                mList.statue = Statue.PARSER_ERROR;
                return mList;
            }
        }
        return mList;
    }


    /**
     * 调用平台接口解析成列表
     *
     * @param clazz
     * @param method
     * @param nameValuePairs
     * @return
     */
    public ResultModel parserArray(Class clazz, String method, BsoftNameValuePair... nameValuePairs) {
        ResultModel mList = new ResultModel();
        String json = null;
        try {
            json = post(method, nameValuePairs);
        } catch (Exception e) {
            e.printStackTrace();
            mList.statue = Statue.NET_ERROR;
            return mList;
        }
        if (null != json) {
            try {
//				System.out.println(json);
                JSONObject ob = new JSONObject(json);
                if (status(ob)) {
//					System.out.println(json);
                    mList.statue = Statue.SUCCESS;
                    if (!ob.isNull("data")) {
                        mList.list = JSON.parseArray(ob.getString("data"), clazz);
                        if (!ob.isNull("totalPage")) {
                            mList.totalPage = Integer.valueOf(ob.getString("totalPage"));
                        }
                        return mList;
                    } else {
                        mList.message = "数据为空";
                        return mList;
                    }
                } else {
                    mList.statue = Statue.ERROR;
                    if (!ob.isNull("msg")) {
                        mList.message = ob.getString("msg");
                    }
                    return mList;
                }
            } catch (JSONException e) {
                e.printStackTrace();
                mList.statue = Statue.PARSER_ERROR;
                return mList;
            }
        }
        return mList;
    }


    public ResultModel parserArray_other(Class clazz, String method, BsoftNameValuePair... nameValuePairs) {
        ResultModel mList = new ResultModel();
        String json = null;
        try {
            json = postOther(method, nameValuePairs);
        } catch (Exception e) {
            e.printStackTrace();
            mList.statue = Statue.NET_ERROR;
            return mList;
        }
        if (null != json) {
            try {
//				System.out.println(json);

                JSONObject ob = new JSONObject(json);
                if (status(ob)) {
//					System.out.println(json);
                    mList.statue = Statue.SUCCESS;
                    if (!ob.isNull("data")) {
                        mList.list = JSON.parseArray(ob.getString("data"), clazz);
                        if (!ob.isNull("totalPage")) {
                            mList.totalPage = Integer.valueOf(ob.getString("totalPage"));
                        }
                        return mList;
                    } else {
                        mList.message = "数据为空";
                        return mList;
                    }
                } else {
                    mList.statue = Statue.ERROR;
                    if (!ob.isNull("msg")) {
                        mList.message = ob.getString("msg");
                    } else if (!ob.isNull("message")) {
                        mList.message = ob.getString("message");
                    }
                    return mList;
                }
            } catch (JSONException e) {
                e.printStackTrace();
                mList.statue = Statue.PARSER_ERROR;
                return mList;
            }
        }
        return mList;
    }

    public ResultModel parserData_other(Class clazz, String method, BsoftNameValuePair... nameValuePairs) {
        ResultModel mList = new ResultModel();
        String json = null;
        try {
            json = postOther(method, nameValuePairs);
        } catch (Exception e) {
            e.printStackTrace();
            mList.statue = Statue.NET_ERROR;
            return mList;
        }
        if (null != json) {
            try {
//				System.out.println(json);
                JSONObject ob = new JSONObject(json);
                if (status(ob)) {
//					System.out.println(json);
                    mList.statue = Statue.SUCCESS;
                    if (!ob.isNull("data")) {
                        mList.data = JSON.parseObject(ob.getString("data"), clazz);
                        if (!ob.isNull("totalPage")) {
                            mList.totalPage = Integer.valueOf(ob.getString("totalPage"));
                        }
                        return mList;
                    } else {
                        mList.message = "数据为空";
                        return mList;
                    }
                } else {
                    mList.statue = Statue.ERROR;
                    if (!ob.isNull("msg")) {
                        mList.message = ob.getString("msg");
                    }
                    return mList;
                }
            } catch (JSONException e) {
                e.printStackTrace();
                mList.statue = Statue.PARSER_ERROR;
                return mList;
            }
        }
        return mList;
    }

    /**
     * 调用His接口解析成对象
     *
     * @param clazz
     * @param method
     * @return
     */
    public ResultModel parserData_His(Class clazz, String method, Map<String, String> map, BsoftNameValuePair... nameValuePairs) {
        ResultModel result = new ResultModel();
        String json = null;
        json = "{\"code\":\"1\",\"message\":\"请在自助机上选择要打印的发票!\"}";
        try {
//            String url = Constants.HttpUrl + method;
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
            result.statue = Statue.NET_ERROR;
            return result;
        }
        if (null != json) {
            try {
//				System.out.println(json);
                JSONObject ob = new JSONObject(json);
                if (status(ob)) {
                    result.statue = Statue.SUCCESS;
                    if (!ob.isNull("data")) {
                        try {
                            result.data = JSON.parseObject(ob.getString("data"), clazz);
                        } catch (Exception e) {
                            result.data = ob.getString("data");
                        }
                        return result;
                    } else {
                        result.message = "数据为空";
                        return result;
                    }
                } else {
                    result.statue = Statue.ERROR;
                    if (!ob.isNull("msg")) {
                        result.message = ob.getString("msg");
                    } else if (!ob.isNull("message")) {
                        result.message = ob.getString("message");
                    }
                    return result;
                }
            } catch (JSONException e) {
                e.printStackTrace();
                result.statue = Statue.PARSER_ERROR;
                return result;
            }
        }
        return result;
    }

    //HIS url 封装
    private String postHis(String url, BsoftNameValuePair... nameValuePairs) {
        try {
            String json = httpClient.doHttpPost(url, getPair(nameValuePairs));
            // String json = httpClient.executeHttpRequests(newUrl + method,
            // getPair(nameValuePairs));
//			System.out.println(" json  " + json);
            AppLogger.e(json);
            return json;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }


    /**
     * 调用His接口解析成列表
     *
     * @param clazz
     * @param method
     * @param nameValuePairs
     * @return
     */
    public ResultModel parserArray_His(Class clazz, String method, Map<String, String> map, BsoftNameValuePair... nameValuePairs) {
        ResultModel result = new ResultModel();
        String json = null;
        try {

//            String url = Constants.HttpUrl + method;
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
            result.statue = Statue.NET_ERROR;
            return result;
        }
        if (null != json) {
            try {
//				System.out.println(json);
                JSONObject ob = new JSONObject(json);
                if (status(ob)) {
                    result.statue = Statue.SUCCESS;
                    if (!ob.isNull("data")) {
                        result.list = JSON.parseArray(ob.getString("data"), clazz);
                        if (!ob.isNull("totalPage")) {
                            result.totalPage = Integer.valueOf(ob.getString("totalPage"));
                        }
                        return result;
                    } else {
                        result.message = "数据为空";
                        return result;
                    }
                } else {
                    result.statue = Statue.ERROR;
                    if (!ob.isNull("msg")) {
                        result.message = ob.getString("msg");
                    } else if (!ob.isNull("message")) {
                        result.message = ob.getString("message");
                    }
                    return result;
                }
            } catch (JSONException e) {
                e.printStackTrace();
                result.statue = Statue.PARSER_ERROR;
                return result;
            }
        }
        return result;
    }

    /**
     * 拼接Url地址
     *
     * @param paramsMap
     * @return
     */
    private String getUrl(String url, Map<String, String> paramsMap) {
        if (paramsMap != null && paramsMap.size() > 0) {
            url += "?";
            Set<String> key = paramsMap.keySet();
            for (Iterator<String> it = key.iterator(); it.hasNext(); ) {
                String s = it.next();
                if (s != null) {
                    try {
                        url += s + "=" + URLEncoder.encode(paramsMap.get(s), "UTF-8");
                    } catch (UnsupportedEncodingException e) {
                        e.printStackTrace();
                    }
                    if (it.hasNext()) {
                        url += "&";
                    }
                }
            }
        }
//		System.out.println("url====" + url);
        return url;
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
            Set<Entry<String, String>> sets = map.entrySet();
            int i = 1;
            for (Entry<String, String> entry : sets) {
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

    /**
     * 支付的时候获取数据调用的接口
     *
     * @param clazz
     * @param method
     * @param map
     * @return
     */
    public ResultModel parserArray_Getx(Class clazz, String method, Map<String, String> map) {
        ResultModel result = new ResultModel();
        String json = null;
        try {
            //json = post(method, nameValuePairs);
//            json = httpClient.executeHttpRequests(Constants.HttpUrl + getUrl(method, map));
            json = httpClient.executeHttpRequests(Constants.getHttpUrl() + getUrl(method, map));
//			System.out.println(json);
        } catch (Exception e) {
            e.printStackTrace();
            result.statue = Statue.NET_ERROR;
            return result;
        }
        if (null != json) {
            try {
//				System.out.println(json);
                JSONObject ob = new JSONObject(json);
                if (status(ob)) {
                    result.statue = Statue.SUCCESS;
                    if (!ob.isNull("data")) {
                        result.list = JSON.parseArray(ob.getString("data"), clazz);
                        return result;
                    } else {
                        result.message = "数据为空";
                        return result;
                    }
                } else {
                    result.statue = Statue.ERROR;
                    if (!ob.isNull("msg")) {
                        result.message = ob.getString("msg");
                    } else if (!ob.isNull("message")) {
                        result.message = ob.getString("message");
                    }
                    return result;
                }
            } catch (JSONException e) {
                e.printStackTrace();
                result.statue = Statue.PARSER_ERROR;
                return result;
            }
        }
        return result;
    }

    /**
     * 支付的时候获取数据调用的接口
     *
     * @param clazz
     * @param method
     * @param map
     * @return
     */
    public ResultModel parserData_Getx(Class clazz, String method, Map<String, String> map) {
        ResultModel result = new ResultModel();
        String json = null;
        try {
//            json = httpClient.executeHttpRequests(Constants.HttpUrl + getUrl(method, map));
            json = httpClient.executeHttpRequests(Constants.getHttpUrl() + getUrl(method, map));
//			System.out.println(json);
        } catch (Exception e) {
            e.printStackTrace();
            result.statue = Statue.NET_ERROR;
            return result;
        }
        if (null != json) {
            try {
                JSONObject ob = new JSONObject(json);
                if (status(ob)) {
                    result.statue = Statue.SUCCESS;
                    if (!ob.isNull("data")) {
                        result.data = JSON.parseObject(ob.getString("data"), clazz);
                        return result;
                    } else {
                        result.message = "数据为空";
                        return result;
                    }
                } else {
                    result.statue = Statue.ERROR;
                    if (!ob.isNull("msg")) {
                        result.message = ob.getString("msg");
                    } else if (!ob.isNull("message")) {
                        result.message = ob.getString("message");
                    }
                    return result;
                }
            } catch (JSONException e) {
                e.printStackTrace();
                result.statue = Statue.PARSER_ERROR;
                return result;
            }
        }
        return result;
    }
}
