package com.bsoft.hospital.pub.suzhoumh.model;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.app.tanklib.model.AbsBaseVoSerializ;

public class DeptDoctorVo extends AbsBaseVoSerializ {
	private static final long serialVersionUID = 1L;

	public String title;
	public ArrayList<DoctorVo> list=new ArrayList<DoctorVo>();

	@Override
	public void buideJson(JSONObject job) throws JSONException {
		try {
			if (!job.isNull("list")) {
				JSONArray arr = job.getJSONArray("list");
				if (null != arr && arr.length() > 0) {
					for (int i = 0; i < arr.length(); i++) {
						DoctorVo vo = new DoctorVo();
						vo.buideJson(arr.getJSONObject(i));
						list.add(vo);
					}
				}
			}
			if (!job.isNull("title")) {
				title = job.getString("title");
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
