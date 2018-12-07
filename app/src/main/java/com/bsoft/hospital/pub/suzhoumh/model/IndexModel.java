package com.bsoft.hospital.pub.suzhoumh.model;

/**
 * @author Tank E-mail:zkljxq@126.com
 * @version 创建时间：2014-5-28 下午1:44:28
 * @类说明
 */
public class IndexModel {

	//f4f4f4
	public int drawable;
	//1 普通intent   0 呼叫中心
	public int type=1;
	public String name;
	public Class classZ;
	
	
	

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getDrawable() {
		return drawable;
	}

	public void setDrawable(int drawable) {
		this.drawable = drawable;
	}

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}

	public IndexModel(int drawable , String name) {
		this.drawable = drawable;
		this.name = name;
	}
	
	public IndexModel(int drawable, int type , String name,Class clz) {
		this.drawable = drawable;
		this.type = type;
		this.name = name;
		this.classZ=clz;
	}
	
	public IndexModel(int drawable, int type , String name)
	{
		this.drawable = drawable;
		this.type = type;
		this.name = name;
	}

}
