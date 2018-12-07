package com.bsoft.hospital.pub.suzhoumh.model.record;

import java.util.ArrayList;

import org.json.JSONObject;

import com.app.tanklib.model.AbsBaseVoSerializ;


/**
 * 
 * 
 * @author zkl
 * 
 *         "chiefcomplaint": "图图他yg", "clinicid": "ecard911421906674366",
 *         "createdate": 1421906674000, "createuserid": 91, "deptcode": "5",
 *         "deptname": "外科", "dtorname": "将计就计", "hospcode": "5064", "hospname":
 *         "杭州市中医院", "orid": 12910459, "phrid": "91", "source": 0, "status": 1,
 *         "visitdatetime": 1421827200000
 */

public class MyRecordDetailVo extends AbsBaseVoSerializ {
	private static final long serialVersionUID = 1L;

	public MyRecordDetail record;

	public ArrayList<MyRecipeVo> recipes;
	public ArrayList<MyReportVo> reports;

	@Override
	public void buideJson(JSONObject job) {
//		try {
//			if (!job.isNull("record")) {
//				JSONObject ob = job.getJSONObject("record");
//				if (!ob.isNull("orid")) {
//					this.orid = ob.getString("orid");
//				}
//				if (!ob.isNull("chiefcomplaint")) {
//					this.chiefcomplaint = ob.getString("chiefcomplaint");
//				}
//				if (!ob.isNull("clinicid")) {
//					this.clinicid = ob.getString("clinicid");
//				}
//				if (!ob.isNull("deptcode")) {
//					this.deptcode = ob.getString("deptcode");
//				}
//				if (!ob.isNull("deptname")) {
//					this.deptname = ob.getString("deptname");
//				}
//				if (!ob.isNull("dtorname")) {
//					this.dtorname = ob.getString("dtorname");
//				}
//				if (!ob.isNull("hospcode")) {
//					this.hospcode = ob.getString("hospcode");
//				}
//				if (!ob.isNull("hospname")) {
//					this.hospname = ob.getString("hospname");
//				}
//				if (!ob.isNull("treatopinion")) {
//					this.treatopinion = ob.getString("treatopinion");
//				}
//				if (!ob.isNull("vdate")) {
//					this.vdate = ob.getString("vdate");
//				}
//			}
//			if (!job.isNull("recipes")) {
//				JSONArray labarr = job.getJSONArray("recipes");
//				if (null != labarr && labarr.length() > 0) {
//					recipes = new ArrayList<MyRecipeVo>();
//					for (int i = 0; i < labarr.length(); i++) {
//						MyRecipeVo vo = new MyRecipeVo();
//						vo.buideJson(labarr.getJSONObject(i));
//						recipes.add(vo);
//					}
//				}
//			}
//			if (!job.isNull("reports")) {
//				JSONArray labarr = job.getJSONArray("reports");
//				if (null != labarr && labarr.length() > 0) {
//					reports = new ArrayList<MyReportVo>();
//					for (int i = 0; i < labarr.length(); i++) {
//						MyReportVo vo = new MyReportVo();
//						vo.buideJson(labarr.getJSONObject(i));
//						reports.add(vo);
//					}
//				}
//			}
//		} catch (JSONException e) {
//			e.printStackTrace();
//		}
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
