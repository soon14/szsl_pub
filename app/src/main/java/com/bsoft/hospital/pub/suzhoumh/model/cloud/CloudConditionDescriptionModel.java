package com.bsoft.hospital.pub.suzhoumh.model.cloud;

import com.app.tanklib.model.AbsBaseVoSerializ;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * @author :lizhengcao
 * @date :2019/3/4
 * E-mail:lizc@bsoft.com.cn
 * @类说明 病情上传实体类
 */
public class CloudConditionDescriptionModel extends AbsBaseVoSerializ {

    public String submitTime;//提交时间戳
    public String illId;//病情ID
    public String regId;//预约记录ID
    public String lastModifyTime;//最后修改时间戳
    public String refuseUploadFileCount;//文件上传拒绝数量 保留字段
    public String regUserId;//上传病情用户ID
    public String illName;//所患疾病名称
    public String illDescribe;//疾病描叙
    public String refuseUploadDesc;//文件上传拒绝理由 保留字段

    @Override
    public void buideJson(JSONObject job) throws JSONException {

    }

    @Override
    public JSONObject toJson() {
        return null;
    }
}
