package com.bsoft.hospital.pub.suzhoumh.model.my;

import java.util.List;

import org.json.JSONException;
import org.json.JSONObject;

import com.app.tanklib.model.AbsBaseVoSerializ;


/**
 * 
 * 
 * @author zkl
 * {"activated":0,"favicon":"","idcard":"110102199701017134","name":"ghjj 
","relationId":39,"relationStr":"3","uid":152}
 */

public class MyFamilyVo extends AbsBaseVoSerializ {
	private static final long serialVersionUID = 1L;

	public String id;
	public String uid;
	public String mobile;
	public String realname;
	public int sexcode;
	public String nature;//病人性质
	public String idcard;
	public int activated;
	public String relation;//与本人关系 0 本人 1 配偶 2 子 3 女 4 孙 5 父母 6 祖辈 7 兄弟姐妹 8 其他 9 非亲属
	public String header;//不用
	public List<Cards> accountcards;
	
	@Override
	public void buideJson(JSONObject job) {
		/*try {
			if (!job.isNull("id")) {
				this.id = job.getString("id");
			}
			if (!job.isNull("activated")) {
				this.activated = job.getInt("activated");
			}
			if (!job.isNull("realname")) {
				this.realname = job.getString("realname");
			}
			if (!job.isNull("idcard")) {
				this.idcard = job.getString("idcard");
			}
			if (!job.isNull("relation")) {
				this.relation = job.getString("relation");
			}
			if (!job.isNull("header")) {
				this.header = job.getString("header");
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}*/
	}

	@Override
	public JSONObject toJson() {
		return null;
	}

	public class Cards extends AbsBaseVoSerializ
	{
		public String id;
		public String cardtype;
		public String cardnum;
		public String flag;
		public String belong;
		public String belongname;
		public String createuser;
		public String createdate;
		public String uid;
		public String fid;
		
		@Override
		public void buideJson(JSONObject job) throws JSONException {
			// TODO Auto-generated method stub
			
		}
		@Override
		public JSONObject toJson() {
			// TODO Auto-generated method stub
			return null;
		}
	}
}
