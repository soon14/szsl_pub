package com.bsoft.hospital.pub.suzhoumh.cache;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

import com.app.tanklib.AppContext;
import com.bsoft.hospital.pub.suzhoumh.model.my.CityCode;
import com.bsoft.hospital.pub.suzhoumh.view.sort.CharacterParser;
import com.bsoft.hospital.pub.suzhoumh.view.sort.SortModel;

/**
 * @author Tank E-mail:zkljxq@126.com
 * 
 * @类说明
 */
public class CityCache {

	public static CityCache cache = new CityCache();
	public static HashMap<String, CityCode> allMap;
	// 市级列表
	public static ArrayList<SortModel> sortCityList;
	// 1级 省列表
	public static ArrayList<CityCode> cityList;
	// 2级 市
	public static HashMap<String, ArrayList<CityCode>> cityMap1;
	// 3级 区
	public static HashMap<String, ArrayList<CityCode>> cityMap2;
	// 4级
	public static HashMap<String, ArrayList<CityCode>> cityMap3;

	public static CharacterParser characterParser = CharacterParser
			.getInstance();

	public CityCache() {

	}

	public static CityCache getInstance() {
		if (null == allMap) {
			try {
				parser(AppContext.getContext().getAssets().open("region.xml"));
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return cache;
	}

	public String getCityName(String id, String id1, String id2) {
		StringBuffer sb = new StringBuffer();
		if (allMap.containsKey(id)) {
			sb.append(allMap.get(id).Title);
		}
		if (allMap.containsKey(id1)) {
			sb.append(allMap.get(id1).Title);
		}
		if (allMap.containsKey(id2)) {
			sb.append(allMap.get(id2).Title);
		}
		return sb.toString();
	}

	public String getCityName(String id) {
		if (allMap.containsKey(id)) {
			return allMap.get(id).Title;
		}
		return "";
	}

	public static void parser(InputStream input) {
		allMap = new HashMap<String, CityCode>();
		cityList = new ArrayList<CityCode>();
		sortCityList = new ArrayList<SortModel>();
		cityMap1 = new HashMap<String, ArrayList<CityCode>>();
		cityMap2 = new HashMap<String, ArrayList<CityCode>>();
		cityMap3 = new HashMap<String, ArrayList<CityCode>>();
		SAXReader reader = new SAXReader();
		Document document = null;
		try {
			document = reader.read(input);
		} catch (DocumentException e1) {
			e1.printStackTrace();
		}
		Element root = document.getRootElement();
		for (Iterator<Element> iterator = root.elementIterator(); iterator
				.hasNext();) {
			Element element3 = iterator.next();
			Iterator<Element> schemaIterator = element3.elementIterator();
			while (schemaIterator.hasNext()) {
				Element schemaElement = schemaIterator.next();
				CityCode code = new CityCode();
				code.Title = schemaElement.elementText("Title");
				code.ID = schemaElement.elementText("ID");
				code.Level = schemaElement.elementText("Level");
				code.LongCode = schemaElement.elementText("LongCode");
				code.PID = schemaElement.elementText("PID");
				allMap.put(code.ID, code);
				if ("2".equals(code.Level)) {
					cityList.add(code);
				}
				if ("3".equals(code.Level)) {
					SortModel sortModel = new SortModel(code, characterParser);
					sortCityList.add(sortModel);
					if (cityMap1.containsKey(code.PID)) {
						cityMap1.get(code.PID).add(code);
					} else {
						ArrayList<CityCode> list = new ArrayList<CityCode>();
						list.add(code);
						cityMap1.put(code.PID, list);
					}
				}
				if ("4".equals(code.Level)) {
					if (cityMap2.containsKey(code.PID)) {
						cityMap2.get(code.PID).add(code);
					} else {
						ArrayList<CityCode> list = new ArrayList<CityCode>();
						list.add(code);
						cityMap2.put(code.PID, list);
					}
				}
				if ("5".equals(code.Level)) {
					if (cityMap3.containsKey(code.PID)) {
						cityMap3.get(code.PID).add(code);
					} else {
						ArrayList<CityCode> list = new ArrayList<CityCode>();
						list.add(code);
						cityMap3.put(code.PID, list);
					}
				}
			}
		}
		try {
			input.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
