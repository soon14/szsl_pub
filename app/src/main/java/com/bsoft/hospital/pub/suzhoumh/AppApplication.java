package com.bsoft.hospital.pub.suzhoumh;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;

import android.content.Context;
import android.provider.Settings.Secure;
import android.support.multidex.MultiDex;
import android.telephony.TelephonyManager;

import com.alibaba.fastjson.JSON;
import com.app.tanklib.AppContext;
import com.app.tanklib.BaseApplication;
import com.app.tanklib.Preferences;
import com.app.tanklib.util.StringUtil;
import com.baidu.android.pushservice.PushManager;
import com.baidu.location.BDLocation;
import com.baidu.mapapi.SDKInitializer;
import com.bsoft.hospital.pub.suzhoumh.api.AppHttpClient;
import com.bsoft.hospital.pub.suzhoumh.api.HttpApi;
import com.bsoft.hospital.pub.suzhoumh.api.HttpNewApi;
import com.bsoft.hospital.pub.suzhoumh.api.YBHttpApi;
import com.bsoft.hospital.pub.suzhoumh.cache.ModelCache;
import com.bsoft.hospital.pub.suzhoumh.model.DeptModelVo;
import com.bsoft.hospital.pub.suzhoumh.model.IndexDemographicinfoVo;
import com.bsoft.hospital.pub.suzhoumh.model.LoginUser;
import com.bsoft.hospital.pub.suzhoumh.model.SettingMsgVo;
import com.bsoft.hospital.pub.suzhoumh.model.moitor.MonitorSetting;
import com.bsoft.hospital.pub.suzhoumh.model.my.HosVo;
import com.bsoft.hospital.pub.suzhoumh.model.my.PersonVo;
import com.bsoft.hospital.pub.suzhoumh.model.my.RelationVo;
import com.bsoft.hospital.pub.suzhoumh.push.Utils;
import com.bsoft.hospital.pub.suzhoumh.util.ContextUtils;
import com.bsoft.hospital.pub.suzhoumh.util.aes.AESUtil;
import com.bsoft.hospital.pub.suzhoumh.view.sort.SortModel;
import com.daoyixun.ipsmap.IpsMapSDK;

/**
 * _oo0oo_
 * o8888888o
 * 88" . "88
 * (| -_- |)
 * 0\  =  /0
 * ___/`---'\___
 * .' \\|     |// '.
 * / \\|||  :  |||// \
 * / _||||| -:- |||||- \
 * |   | \\\  -  /// |   |
 * | \_|  ''\---/''  |_/ |
 * \  .-\__  '-'  ___/-. /
 * ___'. .'  /--.--\  `. .'___
 * ."" '<  `.___\_<|>_/___.' >' "".
 * | | :  `- \`.;`\ _ /`;.`/ - ` : | |
 * \  \ `_.   \_ __\ /__ _/   .-` /  /
 * =====`-.____`.___ \_____/___.-`___.-'=====
 * `=---='
 * <p>
 * <p>
 * ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
 * <p>
 * 佛祖保佑         永无BUG
 */

/**
 * @author Tank E-mail:zkljxq@126.com
 * @类说明
 */
public class AppApplication extends BaseApplication {

    public static LoginUser loginUser;

    public BDLocation location;

    public SettingMsgVo settingMsgVo;

    public IndexDemographicinfoVo demographicinfoVo;

    public ArrayList<MonitorSetting> monitorSetting;

    // 历史搜索医院
    public ArrayList<HosVo> hisSearchHos;
    // 历史搜索城市
    public ArrayList<SortModel> hisSearchCity;
    // 历史预约医院
    public ArrayList<HosVo> hisAppointHos;
    // 历史预约科室
    public ArrayList<DeptModelVo> hisAppointDept;

    public static List<RelationVo> relationlist;
    // 总未读数
    public static int messageCount = 0;

    private TelephonyManager telephonyManager;

    public String deviceId;

    public static PersonVo person = new PersonVo();//预约，查询报告的时候用
    private Object mPut;

    // 清除个人相关信息,退出时调用
    public void clearInfo() {
        hisSearchHos = null;
        hisSearchCity = null;
        hisAppointHos = null;
        hisAppointDept = null;
        PushManager.stopWork(this);//停止百度推送云服务
        Utils.setBind(this, false);//取消绑定
        setLoginUser(null);
        setLocation(null);
        setSettingMsg(null);
        setDemographicinfoVo(null);
        Constants.setHospitalName(Constants.typeHospitalNameEastern);
        Constants.setHospitalID(Constants.HOSPITAL_ID_Eastern);
        Constants.setHttpUrl(Constants.HttpUrlEasternDistrict);
    }


    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        MultiDex.install(this);
    }

    public String getUserIdentification(String key) {
        return key + ((null == getLoginUser()) ? "" : getLoginUser().id);
    }

    public void clearHisSearchHos() {
        this.hisSearchHos = null;
        Preferences.getInstance().setStringData(
                getUserIdentification("hisSearchHos"), null);
    }

    public ArrayList<HosVo> getHisAppointHos() {
        if (null != hisAppointHos) {
            return hisAppointHos;
        }
        String json = Preferences.getInstance().getStringData(
                getUserIdentification("hisAppointHos"));
        if (StringUtil.isEmpty(json)) {
            return null;
        }
        try {
            JSONArray arr = new JSONArray(json);
            if (null != arr && arr.length() > 0) {
                hisAppointHos = new ArrayList<HosVo>();
                for (int i = 0; i < arr.length(); i++) {
                    HosVo vo = new HosVo();
                    vo.buideJson(arr.getJSONObject(i));
                    hisAppointHos.add(vo);
                }
                return hisAppointHos;
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }

    public void addHisAppointHos(HosVo vo) {
        if (null == hisAppointHos) {
            hisAppointHos = new ArrayList<HosVo>();
        }
        ArrayList<HosVo> datas = new ArrayList<HosVo>();
        datas.add(vo);
        int i = 0;
        for (HosVo hvo : hisAppointHos) {
            if (!hvo.title.equals(vo.title)) {
                datas.add(hvo);
                i++;
            }
            if (i == 2) {
                break;
            }
        }
        hisAppointHos = datas;
        if (null != hisAppointHos && hisAppointHos.size() > 0) {
            JSONArray arr = new JSONArray();
            for (HosVo hvo : hisAppointHos) {
                arr.put(hvo.toJson());
            }
            Preferences.getInstance().setStringData(
                    getUserIdentification("hisAppointHos"), arr.toString());
        }
    }


    public ArrayList<DeptModelVo> getHisAppointDept() {
        if (null != hisAppointDept) {
            return hisAppointDept;
        }
        String json = Preferences.getInstance().getStringData(
                getUserIdentification("hisAppointDept"));
        if (StringUtil.isEmpty(json)) {
            return null;
        }
        try {
            JSONArray arr = new JSONArray(json);
            if (null != arr && arr.length() > 0) {
                hisAppointDept = new ArrayList<DeptModelVo>();
                for (int i = 0; i < arr.length(); i++) {
                    DeptModelVo vo = new DeptModelVo();
                    vo.buideJson(arr.getJSONObject(i));
                    hisAppointDept.add(vo);
                }
                return hisAppointDept;
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }

    public void addHisAppointDept(DeptModelVo vo) {
        if (null == hisAppointDept) {
            hisAppointDept = new ArrayList<DeptModelVo>();
        }
        ArrayList<DeptModelVo> datas = new ArrayList<DeptModelVo>();
        datas.add(vo);
        int i = 0;
        for (DeptModelVo hvo : hisAppointDept) {
            if (!hvo.title.equals(vo.title)) {
                datas.add(hvo);
                i++;
            }
            if (i == 2) {
                break;
            }
        }
        hisAppointDept = datas;
        if (null != hisAppointDept && hisAppointDept.size() > 0) {
            JSONArray arr = new JSONArray();
            for (DeptModelVo hvo : hisAppointDept) {
                arr.put(hvo.toJson());
            }
            Preferences.getInstance().setStringData(
                    getUserIdentification("hisAppointDept"), arr.toString());
        }
    }

    public ArrayList<HosVo> getHisSearchHos() {
        if (null != hisSearchHos) {
            return hisSearchHos;
        }
        String json = Preferences.getInstance().getStringData(
                getUserIdentification("hisSearchHos"));
        if (StringUtil.isEmpty(json)) {
            return null;
        }
        try {
            JSONArray arr = new JSONArray(json);
            if (null != arr && arr.length() > 0) {
                hisSearchHos = new ArrayList<HosVo>();
                for (int i = 0; i < arr.length(); i++) {
                    HosVo vo = new HosVo();
                    vo.buideJson(arr.getJSONObject(i));
                    hisSearchHos.add(vo);
                }
                return hisSearchHos;
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }

    public void addHisSearchHos(HosVo vo) {
        if (null == hisSearchHos) {
            hisSearchHos = new ArrayList<HosVo>();
        }
        ArrayList<HosVo> datas = new ArrayList<HosVo>();
        datas.add(vo);
        int i = 0;
        for (HosVo hvo : hisSearchHos) {
            if (!hvo.title.equals(vo.title)) {
                datas.add(hvo);
                i++;
            }
            if (i == 4) {
                break;
            }
        }
        hisSearchHos = datas;
        if (null != hisSearchHos && hisSearchHos.size() > 0) {
            JSONArray arr = new JSONArray();
            for (HosVo hvo : hisSearchHos) {
                arr.put(hvo.toJson());
            }
            Preferences.getInstance().setStringData(
                    getUserIdentification("hisSearchHos"), arr.toString());
        }
    }

    public void clearHisSearchCity() {
        this.hisSearchCity = null;
        Preferences.getInstance().setStringData(
                getUserIdentification("hisSearchCity"), null);
    }

    public ArrayList<SortModel> getHisSearchCity() {
        if (null != hisSearchCity) {
            return hisSearchCity;
        }
        String json = Preferences.getInstance().getStringData(
                getUserIdentification("hisSearchCity"));
        if (StringUtil.isEmpty(json)) {
            return null;
        }
        try {
            JSONArray arr = new JSONArray(json);
            if (null != arr && arr.length() > 0) {
                hisSearchCity = new ArrayList<SortModel>();
                for (int i = 0; i < arr.length(); i++) {
                    SortModel vo = new SortModel();
                    vo.buideJson(arr.getJSONObject(i));
                    hisSearchCity.add(vo);
                }
                return hisSearchCity;
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }

    public void addHisSearchCity(SortModel vo) {
        if (null == hisSearchCity) {
            hisSearchCity = new ArrayList<SortModel>();
        }
        ArrayList<SortModel> datas = new ArrayList<SortModel>();
        datas.add(vo);
        int i = 0;
        for (SortModel hvo : hisSearchCity) {
            if (!hvo.name.equals(vo.name)) {
                datas.add(hvo);
                i++;
            }
            if (i == 4) {
                break;
            }
        }
        hisSearchCity = datas;
        if (null != hisSearchCity && hisSearchCity.size() > 0) {
            JSONArray arr = new JSONArray();
            for (SortModel hvo : hisSearchCity) {
                arr.put(hvo.toJson());
            }
            Preferences.getInstance().setStringData(
                    getUserIdentification("hisSearchCity"), arr.toString());
        }
    }

    public IndexDemographicinfoVo getDemographicinfoVo() {
        if (null != demographicinfoVo) {
            return demographicinfoVo;
        }
        String json = Preferences.getInstance().getStringData(
                "demographicinfoVo");
        if (StringUtil.isEmpty(json)) {
            return null;
        }
        demographicinfoVo = new IndexDemographicinfoVo();
        demographicinfoVo.buideJson(json);
        return demographicinfoVo;
    }

    public void setDemographicinfoVo(IndexDemographicinfoVo demographicinfoVo) {
        this.demographicinfoVo = demographicinfoVo;
        if (null != demographicinfoVo) {
            Preferences.getInstance().setStringData("demographicinfoVo",
                    demographicinfoVo.toJsonString());
        } else {
            Preferences.getInstance().setStringData("demographicinfoVo", null);
        }
    }

    public void setSettingMsg(SettingMsgVo msgVo) {
        this.settingMsgVo = msgVo;
        if (null != settingMsgVo) {
            Preferences.getInstance().setStringData("settingMsgVo",
                    settingMsgVo.toJsonString());
        } else {
            Preferences.getInstance().setStringData("settingMsgVo", null);
        }
    }

    public SettingMsgVo getSettingMsg() {
        if (null != settingMsgVo) {
            return settingMsgVo;
        }
        String json = Preferences.getInstance().getStringData("settingMsgVo");
        if (StringUtil.isEmpty(json)) {
            return null;
        }
        settingMsgVo = new SettingMsgVo();
        settingMsgVo.buideJson(json);
        return settingMsgVo;
    }

    public ArrayList<MonitorSetting> getMonitorSetting() {
        if (null == monitorSetting) {
            String json = Preferences.getInstance().getStringData(
                    getUserIdentification("monitorSetting"));
            if (StringUtil.isEmpty(json)) {
                monitorSetting = new ArrayList<MonitorSetting>();
                monitorSetting.add(new MonitorSetting(
                        "血压", 1, 1, "mmhg"));
                monitorSetting.add(new MonitorSetting(
                        "血糖", 0, 2, "mmol/L"));
                monitorSetting.add(new MonitorSetting(
                        "体重", 1, 3, "Kg"));
                monitorSetting.add(new MonitorSetting(
                        "身高", 0, 4, "m"));
                monitorSetting.add(new MonitorSetting(
                        "BMI", 0, 5, "Kg/m^2"));
                monitorSetting.add(new MonitorSetting(
                        "心率", 0, 6, "bpm"));
                monitorSetting.add(new MonitorSetting(
                        "腰围", 0, 7, "cm"));
                monitorSetting.add(new MonitorSetting(
                        "运动指数", 0, 8, "步"));
                monitorSetting.add(new MonitorSetting(
                        "血氧", 0, 9, "%"));
                if (null != monitorSetting && monitorSetting.size() > 0) {
                    JSONArray arr = new JSONArray();
                    for (MonitorSetting ms : monitorSetting) {
                        arr.put(ms.toJson());
                    }
                    Preferences.getInstance().setStringData(
                            getUserIdentification("monitorSetting"),
                            arr.toString());
                }
                return monitorSetting;
            }
            try {
                monitorSetting = (ArrayList<MonitorSetting>) JSON.parseArray(json,
                        MonitorSetting.class);
            } catch (Exception e) {
                e.printStackTrace();
            }
            return monitorSetting;
        }
        return monitorSetting;
    }

    public void changeMonitor(int index, int flag) {
        if (null != monitorSetting && monitorSetting.size() > 0) {
            monitorSetting.get(index).flag = flag;
            JSONArray arr = new JSONArray();
            for (MonitorSetting ms : monitorSetting) {
                arr.put(ms.toJson());
            }
            Preferences.getInstance().setStringData(
                    getUserIdentification("monitorSetting"), arr.toString());
        }

    }

    public BDLocation getLocation() {
        return location;
    }

    public void setLocation(BDLocation location) {
        this.location = location;
    }

    public void setLoginUser(LoginUser loginUser) {
        AppApplication.loginUser = loginUser;
        if (null != loginUser) {
//			Preferences.getInstance().setStringData("loginUser",
//					loginUser.toJsonString());
            Preferences.getInstance().setStringData("loginUser",
                    AESUtil.encrypt(JSON.toJSONString(loginUser)));
        } else {
            Preferences.getInstance().setStringData("loginUser", null);
        }
    }

    public LoginUser getLoginUser() {
        if (null != loginUser) {
            return loginUser;
        }
        String json = Preferences.getInstance().getStringData("loginUser");
        if (StringUtil.isEmpty(json)) {
            return null;
        } else {
            json = AESUtil.decrypt(json);
            if (StringUtil.isEmpty(json)) {
                return null;
            }
        }
        try {
            loginUser = JSON.parseObject(json, LoginUser.class);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
        return loginUser;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        ContextUtils.init(this);
        //FrontiaApplication.initFrontiaApplication(getApplicationContext());
        // 注册App异常崩溃处理器 (开发时，最好注释一下，影响错误信息打印)
        // Thread.setDefaultUncaughtExceptionHandler(AppException.getAppExceptionHandler());
        telephonyManager = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
        // 在使用 SDK 各组间之前初始化 context 信息，传入 ApplicationContext
        SDKInitializer.initialize(this);
        Preferences preferences = new Preferences(this);
        HttpApi httpApi = new HttpApi(new AppHttpClient());
        YBHttpApi ybHttpApi = new YBHttpApi(new AppHttpClient());
        HttpNewApi httpNewApi = new HttpNewApi(new AppHttpClient());
        ModelCache modelCache = new ModelCache();
        serviceMap.put("com.bsoft.app.preferences", preferences);
        serviceMap.put("com.bsoft.app.httpapi", httpApi);
        serviceMap.put("com.bsoft.app.ybhttpapi", ybHttpApi);
        serviceMap.put("com.bsoft.app.service.modelcache", modelCache);
        if (StringUtil.isEmpty(Preferences.getInstance().getStringData(
                "deviceId"))) {
            // 设置设备唯一号
            Preferences.getInstance().setStringData("deviceId",
                    getSysDeviceId(getApplicationContext()));
        }

        //使用默认配置信息
        IpsMapSDK.init(this, Constants.IPSMAP_APP_KEY);


    }

    public String getCallPhone() {
        if (-1 != location.getAddrStr().indexOf("杭州")) {
            return "4008869660";
        } else if (-1 != location.getAddrStr().indexOf("上海")) {
            return "4006272323";
        } else if (-1 != location.getAddrStr().indexOf("深圳")) {
            return "4008509511";
        }
        return "4008869660";
    }

    private String getSysDeviceId(Context context) {
        deviceId = telephonyManager.getDeviceId();
        if (deviceId == null || deviceId.trim().length() == 0
                || deviceId.matches("0+")) {
            deviceId = telephonyManager.getSimSerialNumber();
            if (null == deviceId || deviceId.trim().length() == 0) {
                deviceId = telephonyManager.getSubscriberId();
                if (null == deviceId || deviceId.trim().length() == 0) {
                    deviceId = Secure.getString(context.getContentResolver(),
                            Secure.ANDROID_ID);
                    if (null == deviceId || deviceId.trim().length() == 0) {
                        deviceId = String.valueOf(System.currentTimeMillis());
                    }
                }
            }
        }
        return deviceId;
    }

    @Override
    public String getTag() {
        return "bsoft_zssz_pub";//文件存储根路径要注意修改
    }

    Setting setting;

    public Setting getSetting() {
        if (null == setting) {
            String json = StringUtil.readFileFromRaw(AppContext.getContext(),
                    R.raw.setting);
            if (!StringUtil.isEmpty(json)) {
                setting = JSON.parseObject(json, Setting.class);
            }
        }
        return setting;
    }


}
