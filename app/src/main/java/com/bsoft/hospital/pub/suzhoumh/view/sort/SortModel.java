package com.bsoft.hospital.pub.suzhoumh.view.sort;

import java.io.Serializable;

import org.json.JSONException;
import org.json.JSONObject;

import com.bsoft.hospital.pub.suzhoumh.model.my.CityCode;

public class SortModel implements Serializable {

	public String code;
	public String name;
	public String sortLetters;

	public SortModel(CityCode code, CharacterParser characterParser) {
		this.name = code.Title;
		this.code = code.ID;
		// 汉字转换成拼音
		String sortString = characterParser.getSelling(this.name)
				.substring(0, 1).toUpperCase();

		// 正则表达式，判断首字母是否是英文字母
		if (sortString.matches("[A-Z]")) {
			sortLetters = sortString.toUpperCase();
		} else {
			sortLetters = "#";
		}
	}

	public SortModel(String name, String code) {
		this.name = name;
		this.code = code;
	}

	public SortModel() {

	}

	public void buideJson(JSONObject job) {
		try {
			if (!job.isNull("name")) {
				this.name = job.getString("name");
			}
			if (!job.isNull("code")) {
				this.code = job.getString("code");
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}
	}

	public JSONObject toJson() {
		JSONObject ob = new JSONObject();
		try {
			ob.put("name", name);
			ob.put("code", code);
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return ob;
	}

}
