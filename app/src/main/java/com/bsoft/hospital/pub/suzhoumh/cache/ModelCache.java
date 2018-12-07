package com.bsoft.hospital.pub.suzhoumh.cache;

import java.util.ArrayList;

import org.json.JSONArray;

import android.content.Context;

import com.bsoft.hospital.pub.suzhoumh.model.ChoiceItem;
import com.bsoft.hospital.pub.suzhoumh.model.DynamicShow;
import com.alibaba.fastjson.JSON;
import com.app.tanklib.AppContext;
import com.app.tanklib.Preferences;
import com.app.tanklib.util.StringUtil;

/**
 * @author Tank E-mail:zkljxq@126.com
 * 
 * @类说明
 */
public class ModelCache {
	ArrayList<DynamicShow> dynamicList;
	// 0 本人 1 配偶 2 子 3 女 4 孙 5 父母 6 祖辈 7 兄弟姐妹 8 其他 9 非亲属
	ArrayList<ChoiceItem> relationList;
	ArrayList<ChoiceItem> familyRelationList;
	ArrayList<ChoiceItem> sexList;
	ArrayList<ChoiceItem> cardTypeList;
	ArrayList<ChoiceItem> hosLevelList;

	public static ModelCache getInstance() {
		Context localContext = AppContext.getContext();
		ModelCache cache = (ModelCache) localContext
				.getSystemService("com.bsoft.app.service.modelcache");
		if (cache == null)
			cache = (ModelCache) localContext.getApplicationContext()
					.getSystemService("com.bsoft.app.service.modelcache");
		if (cache == null)
			throw new IllegalStateException("cache not available");
		return cache;
	}

	public void setDynamicShows(ArrayList<DynamicShow> list) {
		this.dynamicList = list;
		if (null != list && list.size() > 0) {
			JSONArray arr = new JSONArray();
			for (DynamicShow t : list) {
				arr.put(t.toJson());
			}
			Preferences.getInstance().setStringData("dynamic", arr.toString());
		}
	}

	public ArrayList<DynamicShow> getDynamicShows() {
		if (null != dynamicList && dynamicList.size() > 0) {
			return dynamicList;
		} else {
			String json = Preferences.getInstance().getStringData("dynamic");
			if (StringUtil.isEmpty(json)) {
				return null;
			} else {
				return (ArrayList<DynamicShow>) JSON.parseArray(json, DynamicShow.class);
			}
		}
	}

	// 0 本人 1 配偶 2 子 3 女 4 孙 5 父母 6 祖辈 7 兄弟姐妹 8 其他 9 非亲属
	public ArrayList<ChoiceItem> getRelationList() {
		if (null == relationList) {
			relationList = new ArrayList<ChoiceItem>();
			relationList.add(new ChoiceItem("0", "本人"));
			relationList.add(new ChoiceItem("1", "配偶"));
			relationList.add(new ChoiceItem("2", "子"));
			relationList.add(new ChoiceItem("3", "女"));
			relationList.add(new ChoiceItem("4", "孙"));
			relationList.add(new ChoiceItem("5", "父母"));
			relationList.add(new ChoiceItem("6", "祖辈"));
			relationList.add(new ChoiceItem("7", "兄弟姐妹"));
			relationList.add(new ChoiceItem("88", "其他"));
			relationList.add(new ChoiceItem("99", "非亲属"));
		}
		return relationList;
	}
	
	//  1 配偶 2 子 3 女 4 孙 5 父母 6 祖辈 7 兄弟姐妹 8 其他 9 非亲属
	public ArrayList<ChoiceItem> getFamilyRelationList() {
		if (null == familyRelationList) {
			familyRelationList = new ArrayList<ChoiceItem>();
			familyRelationList.add(new ChoiceItem("1", "配偶"));
			familyRelationList.add(new ChoiceItem("2", "子"));
			familyRelationList.add(new ChoiceItem("3", "女"));
			familyRelationList.add(new ChoiceItem("4", "孙"));
			familyRelationList.add(new ChoiceItem("5", "父母"));
			familyRelationList.add(new ChoiceItem("6", "祖辈"));
			familyRelationList.add(new ChoiceItem("7", "兄弟姐妹"));
			familyRelationList.add(new ChoiceItem("88", "其他"));
			familyRelationList.add(new ChoiceItem("99", "非亲属"));
		}
		return familyRelationList;
	}
	
	public ArrayList<ChoiceItem> getSexList() {
		if (null ==sexList) {
			sexList = new ArrayList<ChoiceItem>();
			sexList.add(new ChoiceItem("1", "男"));
			sexList.add(new ChoiceItem("2", "女"));
		}
		return sexList;
	}
	
	//三级医院 1、三甲医院 2、三乙医院3、三丙医院 4、二级医院 5、一级医院 6 二级甲等 7 一级甲等8
	//三级医院 1、三甲医院 2、三乙医院3、三丙医院 4、二级医院 5、一级医院 6 二级甲等 7 一级甲等8
	public ArrayList<ChoiceItem> getHosLevelList() {
		if (null ==hosLevelList) {
			hosLevelList = new ArrayList<ChoiceItem>();
			hosLevelList.add(new ChoiceItem("0", "全部"));
			hosLevelList.add(new ChoiceItem("1", "三级医院"));
			hosLevelList.add(new ChoiceItem("2", "三甲医院"));
			hosLevelList.add(new ChoiceItem("3", "三乙医院"));
			hosLevelList.add(new ChoiceItem("4", "三丙医院"));
			hosLevelList.add(new ChoiceItem("5", "二级医院"));
			hosLevelList.add(new ChoiceItem("6", "一级医院"));
			hosLevelList.add(new ChoiceItem("7", "二级甲等"));
			hosLevelList.add(new ChoiceItem("8", "一级甲等"));
			
		}
		return hosLevelList;
	}
	
	public ArrayList<ChoiceItem> getCardTypeList() {
		if (null ==cardTypeList) {
			cardTypeList = new ArrayList<ChoiceItem>();
			cardTypeList.add(new ChoiceItem("1", "医保卡"));
			cardTypeList.add(new ChoiceItem("2", "就诊卡"));
		}
		return cardTypeList;
	}

	public String getRelationName(String index) {
		if("0".equals(index)){
			return "本人";
		}
		int _index = getRelationList().indexOf(new ChoiceItem(index));
		if (-1 != _index) {
			return getRelationList().get(_index).itemName;
		}
		return "";
	}

}
