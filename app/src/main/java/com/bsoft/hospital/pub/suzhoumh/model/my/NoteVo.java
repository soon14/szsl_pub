package com.bsoft.hospital.pub.suzhoumh.model.my;

import org.json.JSONException;
import org.json.JSONObject;

import com.app.tanklib.model.AbsBaseVoSerializ;


/**
 * 
 * 
 * @author zkl
 "createdate": 1420524611000,
                "createdatefmt": "2015-01-06",
                "createuser": 91,
                "depname": "外科",
                "deptype": "专家门诊",
                "docname": "张东",
                "hospid": 5047,
                "hospname": "省人民医院",
                "id": 3,
                "kinds": "预约挂号",
                "latitude": 30.2902,
                "longitude": 120.174271,
                "orderdate": 1420646400000,
                "orderdatefmt": "2015-01-08",
                "status": 1,
                "timeperiod": "10:00-10:30",
                "uid": 91,
                "uiid": 1

 */

public class NoteVo extends AbsBaseVoSerializ {
	private static final long serialVersionUID = 1L;

	public String createdatefmt;
	public String hospname;
	public String docname;
	public String deptype;
	public String depname;
	public String orderdatefmt;
	public String timeperiod;

	@Override
	public void buideJson(JSONObject job) {
		try {
			if (!job.isNull("createdatefmt")) {
				this.createdatefmt = job.getString("createdatefmt");
			}
			if (!job.isNull("hospname")) {
				this.hospname = job.getString("hospname");
			}
			if (!job.isNull("docname")) {
				this.docname = job.getString("docname");
			}
			if (!job.isNull("deptype")) {
				this.deptype = job.getString("deptype");
			}
			if (!job.isNull("depname")) {
				this.depname = job.getString("depname");
			}
			if (!job.isNull("orderdatefmt")) {
				this.orderdatefmt = job.getString("orderdatefmt");
			}
			if (!job.isNull("timeperiod")) {
				this.timeperiod = job.getString("timeperiod");
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}
	}

	@Override
	public JSONObject toJson() {
		return null;
	}

}
