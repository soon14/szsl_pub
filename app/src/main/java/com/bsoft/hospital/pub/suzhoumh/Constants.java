package com.bsoft.hospital.pub.suzhoumh;


import com.bsoft.hospital.pub.suzhoumh.util.ContextUtils;
import com.bsoft.hospital.pub.suzhoumh.util.SpUtil;

/**
 * @author Tank E-mail:zkljxq@126.com
 * @类说明
 */
public class Constants {


    //院内导航地图key
    public static final String IPSMAP_APP_KEY = "C1IhuBCaZF";
    public static final String IPSMAP_MAP_ID = "Ts3J5kAJuU";

    public static final String CALENDAR = "CALDROID_SAVED_STATE";
    public static final String CLOUD_TYPE = "cloud_type";
    public static final String CLOUD_IDEPART = "1";//1-互联网诊室  0-全部
    public static final int APPOINTMENT_DATE_DELAY_AMOUNT = 35; //日历实行排班的延迟日期范围


    /**
     * 本部IP , 机构ID
     */
    public static final String HttpUrlHeadquarters = "http://api.eeesys.com:18010/";
    public static final String HOSPITAL_ID_Headquarters = "9858";
    public static final String typeHospitalNameHeadquarters = "苏州市立医院本部";
    public static final String[] LongitudeAndHeadquarters = {"120.627795", "31.308143"};
    /**
     * 北区IP , 机构ID
     */
    public static final String HttpUrlNorthDistrict = "http://58.210.234.99:8091/";
    public static final String HOSPITAL_ID_North = "9859";
    public static final String typeHospitalNameNorth = "苏州市立医院北区";
    public static final String[] LongitudeAndLatitudeNorth = {"120.609669", "31.328131"};
    /**
     * 东区IP , 机构ID http://172.20.17.31:8090 测试服务器的地址
     */
//    public static final String HttpUrlEasternDistrict = "http://218.4.142.107:8010/";
    public static final String HttpUrlEasternDistrict = "http://172.20.17.31:8094/";
     /*public static final String HttpUrlEasternDistrict = "http://192.168.1.106:8080/";*/
    public static final String HOSPITAL_ID_Eastern = "9858";//机构id
    public static final String typeHospitalNameEastern = "苏州市立医院东区";
    public static final String[] LongitudeAndLatitudeEastern = {"120.633178", "31.324441"};

    /**
     * ip的key，机构id的key
     */
    private static String httpUrlKey = "url_ip_key";
    private static String hospitalIdKey = "hospital_id_key";
    private static String typeHospitalNameKey = "hospital_name_key";


    /**
     * 默认的IP和机构Id，因东区是首版本，防止用户出问题，故东区的IP，ID默认
     */

    /**
     * 服务器IP
     *
     * @return
     */
    public static String getHttpUrl() {
        return SpUtil.getString(ContextUtils.getContext(), httpUrlKey, HttpUrlEasternDistrict);
    }

    public static void setHttpUrl(String url) {
        SpUtil.putString(ContextUtils.getContext(), httpUrlKey, url);
    }

    /**
     * 医院机构ID
     */
    public static String getHospitalID() {
        return SpUtil.getString(ContextUtils.getContext(), hospitalIdKey, HOSPITAL_ID_Eastern);
    }

    public static void setHospitalID(String id) {
        SpUtil.putString(ContextUtils.getContext(), hospitalIdKey, id);
    }

    /**
     * 医院的名称
     *
     * @return
     */
    public static String getHospitalName() {
        return SpUtil.getString(ContextUtils.getContext(), typeHospitalNameKey, typeHospitalNameEastern);
    }

    public static void setHospitalName(String name) {
        SpUtil.putString(ContextUtils.getContext(), typeHospitalNameKey, name);
    }

    /**
     * 获得医生头像url
     *
     * @return
     */
    public static String getHeadUrl() {
        return getHttpUrl() + "upload/header/doc/";
    }

    // 阿里支付宝
    public static String Alipay_notify = getHttpUrl() + "alipay/notify";

    // 关闭
    public static final String CLOSE_ACTION = "com.bsoft.mhealthp.close.action";
    // 退出
    public static final String Logout_ACTION = "com.bsoft.mhealthp.logout.action";
    // 切换主tab页
    public static final String BACK_ACTION = "com.bsoft.mhealthp.Back";
    // 天气刷新

    public static final String HomeWeather_ACTION = "com.bsoft.mhealthp.home.weather";
    // 首页头像刷新
    public static final String HomeHeader_ACTION = "com.bsoft.mhealthp.home.header";
    // 首页
    public static final String HomeRecord_ACTION = "com.bsoft.mhealthp.home.record";
    // 首页
    public static final String Name_ACTION = "com.bsoft.mhealthp.my.name";
    // 首页
    public static final String HomeMyInfo_ACTION = "com.bsoft.mhealthp.home.myinfo";
    // 首页更新
    public static final String HomeUpdate_ACTION = "com.bsoft.mhealthp.home.update";
    // 孕妇提醒添加
    public static final String PregnantRemindAdd_ACTION = "com.bsoft.mhealthp.pregnant.remind.add";
    // 儿保提醒添加
    public static final String BabyRemindAdd_ACTION = "com.bsoft.mhealthp.baby.remind.add";
    // 动态评论增加
    public static final String Dynamic_comment_ACTION = "com.bsoft.mhealthp.dynamic.comment";
    // 主页消息数刷新
    public static final String HomeMessageCount_ACTION = "com.bsoft.mhealthp.message.homecount";
    // 消息数全部清空
    public static final String MessageCountClear_ACTION = "com.bsoft.mhealthp.message.count.clear";
    // 消息数刷新
    public static final String MessageCount_ACTION = "com.bsoft.mhealthp.message.count";
    // 消息页刷新
    public static final String MessageHome_ACTION = "com.bsoft.mhealthp.message.home";
    // 消息推送-刷新
    public static final String PushMessage_ACTION = "com.bsoft.mhealthp.push.message";
    public static final String PushHome_ACTION = "com.bsoft.mhealthp.push.home";
    // 头像刷新
    public static final String Header_ACTION = "com.bsoft.mhealthp.action.header";
    //咨询刷新
    public static final String CONSULT_ACTION = "com.bsoft.mhealthp.action.consult";
    // 完善资料
    public static final String MyInfo_ACTION = "com.bsoft.mhealthp.my.info";
    // 卡管理
    public static final String MyCard_ACTION = "com.bsoft.mhealthp.my.card";
    public static final String MyCardAdd_ACTION = "com.bsoft.mhealthp.my.card.add";
    public static final String MyCardEdit_ACTION = "com.bsoft.mhealthp.my.card.edit";
    public static final String MyCardDel_ACTION = "com.bsoft.mhealthp.my.card.del";
    public static final String MyCardAddCardType_ACTION = "com.bsoft.mhealthp.my.card.add.cardtype";
    public static final String MyCardAddCity_ACTION = "com.bsoft.mhealthp.my.card.add.city";
    public static final String MyCardAddHos_ACTION = "com.bsoft.mhealthp.my.card.add.hos";
    // 联系人管理
    public static final String MyContacts_ACTION = "com.bsoft.mhealthp.my.contacts";
    public static final String MyContactsAdd_ACTION = "com.bsoft.mhealthp.my.contacts.add";
    public static final String MyContactsEdit_ACTION = "com.bsoft.mhealthp.my.contacts.edit";
    public static final String MyContactsDel_ACTION = "com.bsoft.mhealthp.my.contacts.del";
    // 地址
    public static final String MyAddress_ACTION = "com.bsoft.mhealthp.my.address";
    // 家庭管理
    public static final String MyFamily_ACTION = "com.bsoft.mhealthp.my.family";
    public static final String MyFamilyActivate_ACTION = "com.bsoft.mhealthp.my.family.activate";
    public static final String MyFamilyAddSex_ACTION = "com.bsoft.mhealthp.my.family.add.sex";
    public static final String MyFamilyCardType_ACTION = "com.bsoft.mhealthp.my.family.cardtype";
    public static final String MyFamilyAddressCity_ACTION = "com.bsoft.mhealthp.my.family.address.city";
    public static final String MyFamilyAddressHos_ACTION = "com.bsoft.mhealthp.my.family.address.hos";
    public static final String MyFamilyAddRelation_ACTION = "com.bsoft.mhealthp.my.family.add.relation";
    // 档案
    public static final String MyRecord_ACTION = "com.bsoft.mhealthp.my.record";
    public static final String MyRecordHos_ACTION = "com.bsoft.mhealthp.my.record.hos";
    public static final String MyRecordDept_ACTION = "com.bsoft.mhealthp.my.record.dept";
    // 便捷寻医
    public static final String SeekCity_ACTION = "com.bsoft.mhealthp.app.seek.city";
    public static final String SeekHos_ACTION = "com.bsoft.mhealthp.app.seek.hos";
    public static final String SeekRegion_ACTION = "com.bsoft.mhealthp.app.seek.region";
    public static final String SeekLevel_ACTION = "com.bsoft.mhealthp.app.seek.level";
    // 分诊咨询
    public static final String Appoint_ACTION = "com.bsoft.mhealthp.app.appoint";
    // 报告查询已读
    public static final String ReportRead_ACTION = "com.bsoft.mhealthp.app.report.read";
    // 健康监测模块修改
    public static final String MonitorChange_ACTION = "com.bsoft.mhealthp.app.monitor.change";

    // 回到顶页
    public static final String Main_Index_Click = "Main_Index_Click";
    public static final String Main_Dynamic_Click = "Main_Dynamic_Click";
    public static final String Main_Message_Click = "Main_Message_Click";
    public static final String Main_My_Click = "Main_My_Click";

    // 回到列表顶部
    public static final String Main_Index_List_Click = "Main_Index_List_Click";
    public static final String Main_Dynamic_List_Click = "Main_Dynamic_List_Click";
    public static final String Main_Message_List_Click = "Main_Message_List_Click";
    public static final String Main_My_List_Click = "Main_My_List_Click";

    // 回到顶页和第一行(上两个的组合)
    public static final String Main_Index = "Main_Index";

    //预约完成后关闭前一级页面
    public static final String ACTION_APPOINT_CLOSE = "com.bsoft.hospital.pub.close";

    //百度推送的消息
    public static String MESSAGE_TYPE_1 = "11";//咨询消息
    public static String MESSAGE_TYPE_2 = "12";//院内通知
    public static String MESSAGE_TYPE_3 = "13";//消息
    public static String MESSAGE_TYPE_4 = "14";//单点登录

    //支付类型
    public static final int PAY_TYPE_ALIPAY = 1;//支付宝
    public static final int PAY_TYPE_WEIXIN = 2;//微信
    public static final int PAY_TYPE_BANK = 3;//银联
    public static final int PAY_TYPE_ACCOUNT = 4;//账户
    public static final int PAY_TYPE_SUYY = 5;//苏州银行
    public static final int PAY_TYPE_YQYB = 6;//园区医保
    public static final int PAY_TYPE_SZYB = 7;//苏州医保

    public static final int PAY_TYPE_FXJ = 0;//非现金

    //支付payChannel
    public static final String PAY_TYPE_BANK_CHANNEl = "6006";//银联
    public static final String PAY_TYPE_SUYY_CHANNEl = "6003";//苏州银行
    public static final String PAY_TYPE_ALIPAY_CHANNEl = "6010";//支付宝
    //支付业务
    public static final int PAY_BUS_TPYE_ZJZF = 1;//诊间支付
    public static final int PAY_BUS_TPYE_CZ = 2;//充值
    public static final int PAY_BUS_TPYE_ZYYJJ = 3;//住院预交金
    public static final int PAY_BUS_TPYE_YYGH = 4;//预约挂号

    //1、app诊间支付、2、app账户充值、3app住院预交金、4app预约挂号
    public static final String PAY_BUS_TYPE_ZJZF_NAME = "App诊间支付";
    public static final String PAY_BUS_TPYE_CZ_NAME = "App账户充值";
    public static final String PAY_BUS_TPYE_ZYYJJ_NAME = "App住院预交金";
    public static final String PAY_BUS_TPYE_YYGH_NAME = "App预约挂号";


    public static final String PAY_STATE_UNPAYED = "0";//未支付
    public static final String PAY_STATE_PAYED = "1";//已支付

    //支付结束广播
    public static final String PAY_FINISH_ACTION = "com.bsoft.hospital.pub.pay";
    //支付成功广播
    public static final String PAY_SUCCESS_ACTION = "com.bsoft.hospital.pub.pay.success";

    public static final String CARD_JZH_NUM = "1";//就诊卡号
    public static final String CARD_ZYH_NUM = "3";//住院号
}
