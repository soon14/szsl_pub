package com.bsoft.hospital.pub.suzhoumh.model.consult;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.app.tanklib.model.AbsBaseVoSerializ;

public class ConsultInfo extends AbsBaseVoSerializ{

	public String id;
	public String title;
	public String content;
	public String userid;
	public String username;
	public String deptid;
	public String deptname;
	public String docid;
	public String docname;
	public String creattime;
	public String updatetime;
	public String state;
	public String sexcode;
	public String endAnswerContent;//最后答复内容
	public String endAnswerTime;//答复时间
	public String isRead;//是否已读 0 未读 1已读
	public ArrayList<SysFiles> sysFiles;//附件集合
	
	/**
	 * 回复问题集合
	 */
	public ArrayList<InteractServants> interactServants;
	
	public class InteractServants extends AbsBaseVoSerializ
	{
		public String id;
		public String qid;
		public String acontent;
		public String creattime;
		public String type;
		public ArrayList<SysFiles> sysFiles;
		@Override
		public void buideJson(JSONObject job) throws JSONException {
			// TODO Auto-generated method stub
			if(!job.isNull("id"))
			{
				this.id = job.getString("id");
			}
			if(!job.isNull("qid"))
			{
				this.qid = job.getString("qid");
			}
			if(!job.isNull("acontent"))
			{
				this.acontent = job.getString("acontent");
			}
			if(!job.isNull("creattime"))
			{
				this.creattime = job.getString("creattime");
			}
			if(!job.isNull("type"))
			{
				this.type = job.getString("type");
			}
			if(!job.isNull("sysFiles"))
			{
				JSONArray filearr = job.getJSONArray("sysFiles");
				if (null != filearr && filearr.length() > 0) {
					sysFiles = new ArrayList<SysFiles>();
					for (int i = 0; i < filearr.length(); i++) {
						SysFiles vo = new SysFiles();
						vo.buideJson(filearr.getJSONObject(i));
						sysFiles.add(vo);
					}
				}
			}
		}
		@Override
		public JSONObject toJson() {
			// TODO Auto-generated method stub
			return null;
		}
	}
	
	public class SysFiles extends AbsBaseVoSerializ
	{
		public String id;
		public String mid;
		public String filefullpath;
		public String filename;
		public String filesize;
		public String creattime;
		public String tablename;
		@Override
		public void buideJson(JSONObject job) throws JSONException {
			// TODO Auto-generated method stub
			if(!job.isNull("id"))
			{
				this.id = job.getString("id");
			}
			if(!job.isNull("mid"))
			{
				this.mid = job.getString("mid");
			}
			if(!job.isNull("filefullpath"))
			{
				this.filefullpath = job.getString("filefullpath");
			}
			if(!job.isNull("filename"))
			{
				this.filename = job.getString("filename");
			}
			if(!job.isNull("filesize"))
			{
				this.filesize = job.getString("filesize");
			}
			if(!job.isNull("createtime"))
			{
				this.creattime = job.getString("createtime");
			}
			if(!job.isNull("tablename"))
			{
				this.tablename = job.getString("tablename");
			}
		}
		@Override
		public JSONObject toJson() {
			// TODO Auto-generated method stub
			return null;
		}
		
	}

	@Override
	public void buideJson(JSONObject job) throws JSONException {
		// TODO Auto-generated method stub
		if(!job.isNull("id"))
		{
			this.id = job.getString("id");
		}
		if(!job.isNull("title"))
		{
			this.title = job.getString("title");
		}
		if(!job.isNull("content"))
		{
			this.content = job.getString("content");
		}
		if(!job.isNull("userid"))
		{
			this.userid = job.getString("userid");
		}
		if(!job.isNull("username"))
		{
			this.username = job.getString("username");
		}
		if(!job.isNull("deptid"))
		{
			this.deptid = job.getString("deptid");
		}
		if(!job.isNull("docid"))
		{
			this.docid = job.getString("docid");
		}
		if(!job.isNull("deptname"))
		{
			this.deptname = job.getString("deptname");
		}
		if(!job.isNull("docname"))
		{
			this.docname = job.getString("docname");
		}
		if(!job.isNull("state"))
		{
			this.state = job.getString("state");
		}
		if(!job.isNull("endAnswerContent"))
		{
			this.endAnswerContent = job.getString("endAnserContent");
		}
		if(!job.isNull("endAnswerTime"))
		{
			this.endAnswerTime = job.getString("endAnswerTime");
		}
		if(!job.isNull("isRead"))
		{
			this.isRead = job.getString("isRead");
		}
		if(!job.isNull("sysFiles"))
		{
			JSONArray filearr = job.getJSONArray("sysFiles");
			if (null != filearr && filearr.length() > 0) {
				sysFiles = new ArrayList<SysFiles>();
				for (int i = 0; i < filearr.length(); i++) {
					SysFiles vo = new SysFiles();
					vo.buideJson(filearr.getJSONObject(i));
					sysFiles.add(vo);
				}
			}
		}
		if(!job.isNull("interactServants"))
		{
			JSONArray filearr = job.getJSONArray("interactServants");
			if (null != filearr && filearr.length() > 0) {
				interactServants = new ArrayList<InteractServants>();
				for (int i = 0; i < filearr.length(); i++) {
					InteractServants vo = new InteractServants();
					vo.buideJson(filearr.getJSONObject(i));
					interactServants.add(vo);
				}
			}
		}
	}

	@Override
	public JSONObject toJson() {
		// TODO Auto-generated method stub
		return null;
	}
}
