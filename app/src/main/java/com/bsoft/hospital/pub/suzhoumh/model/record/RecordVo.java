package com.bsoft.hospital.pub.suzhoumh.model.record;

import org.json.JSONException;
import org.json.JSONObject;

import com.app.tanklib.model.AbsBaseVoSerializ;


/**
 * 
 * 
 * @author zkl
 * 
 *         "chiefcomplaint": "扁桃体发眼", "clinicid": "ecard1420008212351",
 *         "deptcode": "123", "deptname": "内科", "hospcode": "123", "hospname":
 *         "省人民", "orid": 16, "source": 0, "visitdatetime": 1413014400000
 */

public class RecordVo extends AbsBaseVoSerializ {
	private static final long serialVersionUID = 1L;

	public String chiefcomplaint;
	public String clinicid;
	public String deptcode;
	public String deptname;
	public String hospcode;
	public String hospname;
	public String orid;
	public int source;
	public long visitdatetime;

	@Override
	public void buideJson(JSONObject job) {
		try {
			if (!job.isNull("chiefcomplaint")) {
				this.chiefcomplaint = job.getString("chiefcomplaint");
			}
			if (!job.isNull("clinicid")) {
				this.clinicid = job.getString("clinicid");
			}
			if (!job.isNull("deptcode")) {
				this.deptcode = job.getString("deptcode");
			}
			if (!job.isNull("deptname")) {
				this.deptname = job.getString("deptname");
			}
			if (!job.isNull("hospcode")) {
				this.hospcode = job.getString("hospcode");
			}
			if (!job.isNull("hospname")) {
				this.hospname = job.getString("hospname");
			}
			if (!job.isNull("orid")) {
				this.orid = job.getString("orid");
			}
			if (!job.isNull("source")) {
				this.source = job.getInt("source");
			}
			if (!job.isNull("visitdatetime")) {
				this.visitdatetime = job.getLong("visitdatetime");
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}
	}

	@Override
	public JSONObject toJson() {
		return null;
	}

	// @Override
	// public boolean equals(Object obj) {
	// return this.showId == ((DynamicShow) obj).showId;
	// }

}
