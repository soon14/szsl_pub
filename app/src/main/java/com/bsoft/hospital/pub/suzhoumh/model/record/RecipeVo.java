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

public class RecipeVo extends AbsBaseVoSerializ {
	private static final long serialVersionUID = 1L;

	public String drugUsePathwaysTitle;
	public String drugname;
	public String drugusedays;
	public String drugusedoseunit;
	public String drugusepathwayscode;
	public String drugusingrate;
	public String drugusingrateTitle;

	@Override
	public void buideJson(JSONObject job) {
		try {
			if (!job.isNull("drugUsePathwaysTitle")) {
				this.drugUsePathwaysTitle = job.getString("drugUsePathwaysTitle");
			}
			if (!job.isNull("drugname")) {
				this.drugname = job.getString("drugname");
			}
			if (!job.isNull("drugusedays")) {
				this.drugusedays = job.getString("drugusedays");
			}
			if (!job.isNull("drugusedoseunit")) {
				this.drugusedoseunit = job.getString("drugusedoseunit");
			}
			if (!job.isNull("drugusepathwayscode")) {
				this.drugusepathwayscode = job.getString("drugusepathwayscode");
			}
			if (!job.isNull("drugusingrate")) {
				this.drugusingrate = job.getString("drugusingrate");
			}
			if (!job.isNull("drugusingrateTitle")) {
				this.drugusingrateTitle = job.getString("drugusingrateTitle");
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
