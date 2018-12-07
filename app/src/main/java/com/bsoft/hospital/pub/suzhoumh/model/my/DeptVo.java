package com.bsoft.hospital.pub.suzhoumh.model.my;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.app.tanklib.model.AbsBaseVoSerializ;

public class DeptVo extends AbsBaseVoSerializ{

	public String id;
	public String orgid;
	public String orgname;
	public String title;
	public String intro;
	public String code;
	public ArrayList<DeptVo> child;
	
	@Override
	public void buideJson(JSONObject job) throws JSONException {
		// TODO Auto-generated method stub
		try
		{
			if(!job.isNull("id"))
			{
				this.id = job.getString("id");
			}
			if(!job.isNull("title"))
			{
				this.title = job.getString("title");
			}
			if(!job.isNull("intro"))
			{
				this.intro = job.getString("intro");
			}
			if (!job.isNull("child")) {
				try
				{
					JSONArray cardarr = job.getJSONArray("child");
					if (null != cardarr && cardarr.length() > 0) {
						this.child = new ArrayList<DeptVo>();
						for (int i = 0; i < cardarr.length(); i++) {
							DeptVo vo = new DeptVo();
							vo.buideJson(cardarr.getJSONObject(i));
							child.add(vo);
						}
					}
				}catch(Exception e)
				{
					e.printStackTrace();
				}
			}
		}catch(Exception e)
		{
			e.printStackTrace();
		}
	}

	@Override
	public JSONObject toJson() {
		// TODO Auto-generated method stub
		return null;
	}

}
