package com.bsoft.hospital.pub.suzhoumh.model.record;

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
 */

public class RecordDetailVo extends AbsBaseVoSerializ {
	private static final long serialVersionUID = 1L;

	public ArrayList<LabVo> lab;
	public ArrayList<RecipeVo> recipe;

	@Override
	public void buideJson(JSONObject job) {
		try {
			if (!job.isNull("lab")) {
				JSONArray labarr = job.getJSONArray("lab");
				if (null != labarr && labarr.length() > 0) {
					lab = new ArrayList<LabVo>();
					for (int i = 0; i < labarr.length(); i++) {
						LabVo vo=new LabVo();
						vo.buideJson(labarr.getJSONObject(i));
						lab.add(vo);
					}
				}
			}
			if (!job.isNull("recipe")) {
				JSONArray recipearr = job.getJSONArray("recipe");
				if (null != recipearr && recipearr.length() > 0) {
					recipe = new ArrayList<RecipeVo>();
					for (int i = 0; i < recipearr.length(); i++) {
						RecipeVo vo=new RecipeVo();
						vo.buideJson(recipearr.getJSONObject(i));
						recipe.add(vo);
					}
				}
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
