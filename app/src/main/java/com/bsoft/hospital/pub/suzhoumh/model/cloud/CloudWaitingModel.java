package com.bsoft.hospital.pub.suzhoumh.model.cloud;

import com.app.tanklib.model.AbsBaseVoSerializ;

import org.json.JSONException;
import org.json.JSONObject;


/**
 * @author :lizhengcao
 * @date :2019/1/29
 * E-mail:lizc@bsoft.com.cn
 * @类说明 云诊室实体类
 */
public class CloudWaitingModel extends AbsBaseVoSerializ {

    public String regId;
    public String deptId;
    public String deptName;
    public String regDeptId;
    public String regDeptName;
    public String doctorId;
    public String doctorName;
    public String visitAddress;
    public String regTime;
    public String scheduleStatus;
    public String sourceStartTime;
    public String sourceEndTime;
    public String meeingNo;
    public String meeingId;
    public String visitState;
    public String visitStateDesc;
    public String visitNumber;
    public String currentNumber;
    public String currentUserId;
    public String currentUserVisitState;
    public String currentUserVisitStateDesc;


    @Override
    public void buideJson(JSONObject job) throws JSONException {

    }

    @Override
    public JSONObject toJson() {
        return null;
    }
}
