package com.bsoft.hospital.pub.suzhoumh.model.my;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.app.tanklib.model.AbsBaseVoSerializ;


/**
 * 
 * 
 * @author zkl 医院
 * 
 *         address": "北京市海淀区永定路69号 ", "id": 5064, "kinds": 1, "latitude":
 *         39.916957, "longitude": 116.270927, "rank": 2, "region": "76468",
 *         "status": 1, "telephone": "医院总机：010-57976688；急诊电话：010-68213333",
 *         "title": "杭州市中医院"
 */

public class HosVo extends AbsBaseVoSerializ {
	private static final long serialVersionUID = 1L;

	public String id;
	public String kinds;
	public String rank;
	public String region;
	public String title;
	public String distance;
	public String telephone;
	
	//现在用到的
	public String introduce;//医院介绍
	public double latitude;//纬度
	public double longitude;//经度
	public String serverphone;//服务电话
	public String traffic;//医院交通
	public String fax;//医院传真
	public String website;//医院网址
	public String picurl = "";//医院首页
	public String address;//医院地址
	public ArrayList<DeptVo> dept;//科室列表
	
	@Override
	public void buideJson(JSONObject job) {
		try {
			if (!job.isNull("address")) {
				this.address = job.getString("address");
			}
			if (!job.isNull("id")) {
				this.id = job.getString("id");
			}
			if (!job.isNull("latitude")) {
				this.latitude = job.getDouble("latitude");
			}
			if (!job.isNull("longitude")) {
				this.longitude = job.getDouble("longitude");
			}
			if (!job.isNull("region")) {
				this.region = job.getString("region");
			}
			if (!job.isNull("title")) {
				this.title = job.getString("title");
			}
			if (!job.isNull("distance")) {
				this.distance = job.getString("distance");
			}
			if (!job.isNull("rank")) {
				this.rank = job.getString("rank");
			}
			if (!job.isNull("introduce")) {
				this.introduce = job.getString("introduce");
			}
			if (!job.isNull("telephone")){
				this.telephone = job.getString("telephone");
			}
			if (!job.isNull("dept")) {
				JSONArray cardarr = job.getJSONArray("dept");
				if (null != cardarr && cardarr.length() > 0) {
					this.dept = new ArrayList<DeptVo>();
					for (int i = 0; i < cardarr.length(); i++) {
						DeptVo vo = new DeptVo();
						vo.buideJson(cardarr.getJSONObject(i));
						dept.add(vo);
					}
				}
			}
			if (!job.isNull("serverphone")){
				this.serverphone = job.getString("serverphone");
			}
			if (!job.isNull("traffic")){
				this.traffic = job.getString("traffic");
			}
			if (!job.isNull("fax")){
				this.fax = job.getString("fax");
			}
			if (!job.isNull("website")){
				this.website = job.getString("website");
			}
			if (!job.isNull("picurl")){
				this.picurl = job.getString("picurl");
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}
	}
	

	@Override
	public JSONObject toJson() {
		JSONObject ob=new JSONObject();
		try {
			ob.put("title", title);
			ob.put("latitude", latitude);
			ob.put("longitude", longitude);
			ob.put("address", address);
			ob.put("id", id);
			ob.put("rank", rank);
			ob.put("region", region);
			ob.put("introduce", introduce);
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
