package com.bsoft.hospital.pub.suzhoumh.model;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.app.tanklib.model.AbsBaseVoSerializ;


/**
 * 
 * 
 * @author zkl
 * 
 * 
 *         {"code":0,"data":{"demographicinfo":{"bloodtype":1,"createdate":
 *         1420041600000
 *         ,"createunit":"1","createuser":"91","drugallergen":"101,102"
 *         ,"drugallergy"
 *         :1,"flag":1,"id":1,"idcard":"330482198703090030","mpi":"zkl"
 *         ,"phrid":"zkl"
 *         ,"pycode":"zkl","rhblood":1,"sexcode":1,"title":"ttt"},"events"
 *         :[{"date"
 *         :"","title":"请到【我的】完善信息，方便就医功能的使用","type":"2"},{"date":"","title"
 *         :"上传病历数据，掌上管理健康数据"
 *         ,"type":"3"},{"date":"","title":"请上传个人头像，方便朋友认出你","type"
 *         :"1"},{"date":
 *         "2014-12-10","title":"预约挂号：请于XXXX-XX-XX XX:XX准时就诊","type"
 *         :"4"},{"date"
 *         :"2014-12-10","title":"您已预约XXXX-XX-XX XX:XX的陪诊服务","type":
 *         "5"}],"optRecord"
 *         :{"chiefcomplaint":"骨内发炎","hospname":"杭州第一人民医院","treatopinion"
 *         :"建议在家多休息","visitdatetime":1418112000000}},"msg":"success"}
 */

public class IndexVo extends AbsBaseVoSerializ {
	private static final long serialVersionUID = 1L;

	public ArrayList<IndexMsgVo> events;
	public IndexDemographicinfoVo demographicinfoVo;
	public int msgCount=0;

	@Override
	public void buideJson(JSONObject job) {
		try {
			if (!job.isNull("events")) {
				JSONArray arr = job.getJSONArray("events");
				if (null != arr && arr.length() > 0) {
					events = new ArrayList<IndexMsgVo>();
					for (int i = 0; i < arr.length(); i++) {
						IndexMsgVo vo = new IndexMsgVo();
						vo.buideJson(arr.getJSONObject(i));
						events.add(vo);
					}
				}
			}
			if (!job.isNull("msgCount")) {
				msgCount=job.getInt("msgCount");
			}
			if (!job.isNull("demographicinfo")) {
				demographicinfoVo = new IndexDemographicinfoVo();
				demographicinfoVo.buideJson(job
						.getJSONObject("demographicinfo"));
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}
	}

	@Override
	public JSONObject toJson() {
		JSONObject ob = new JSONObject();
		try {
			if (null != events && events.size() > 0) {
				JSONArray arr = new JSONArray();
				for (IndexMsgVo msg : events) {
					arr.put(msg.toJson());
				}
				ob.put("events", arr);
			}
			if (null != demographicinfoVo) {
				ob.put("demographicinfo", demographicinfoVo.toJson());
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return ob;
	}

	// @Override
	// public boolean equals(Object obj) {
	// return this.showId == ((DynamicShow) obj).showId;
	// }

}
