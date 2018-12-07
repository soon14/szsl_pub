package com.bsoft.hospital.pub.suzhoumh.model;

import org.json.JSONException;
import org.json.JSONObject;

import com.app.tanklib.model.AbsBaseVoSerializ;

/**
 * 
 * 
 * @author zkl "bloodinfo": "A型", "drugallergens": "青霉素类抗生素,磺胺类抗生素",
 *         "lifestyle": { "drink": 0, "eatEhabit": "2", "smoke": 1, "train": 1
 *         }, "rhbloodinfo": "1"
 */

public class IndexDemographicinfoVo extends AbsBaseVoSerializ {
	private static final long serialVersionUID = 1L;

	public String bloodinfo;
	public String drugallergens;
	public int rhbloodinfo;
	public int drink;
	public int eatEhabit;
	public int smoke;
	public int train;

	@Override
	public void buideJson(JSONObject job) {
		try {
			if (!job.isNull("bloodinfo")) {
				this.bloodinfo = job.getString("bloodinfo");
			}
			if (!job.isNull("drugallergens")) {
				this.drugallergens = job.getString("drugallergens");
			}
			if (!job.isNull("rhbloodinfo")) {
				this.rhbloodinfo = job.getInt("rhbloodinfo");
			}
			if (!job.isNull("lifestyle")) {
				JSONObject ob = job.getJSONObject("lifestyle");
				if (!ob.isNull("drink")) {
					this.drink = ob.getInt("drink");
				}
				if (!ob.isNull("eatEhabit")) {
					this.eatEhabit = ob.getInt("eatEhabit");
				}
				if (!ob.isNull("smoke")) {
					this.smoke = ob.getInt("smoke");
				}
				if (!ob.isNull("train")) {
					this.train = ob.getInt("train");
				}
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}
	}

	@Override
	public JSONObject toJson() {
		JSONObject ob=new JSONObject();
		try {
			ob.put("bloodinfo", bloodinfo);
			ob.put("drugallergens", drugallergens);
			ob.put("rhbloodinfo", rhbloodinfo);
			ob.put("drink", drink);
			ob.put("eatEhabit", eatEhabit);
			ob.put("smoke", smoke);
			ob.put("train", train);
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return ob;
	}

}
