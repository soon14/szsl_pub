package com.bsoft.hospital.pub.suzhoumh.activity.base;

import com.bsoft.hospital.pub.suzhoumh.model.ChoiceItem;
import com.bsoft.hospital.pub.suzhoumh.model.my.CityCode;

public abstract class TitleBaseActivity extends BaseActivity {

	//区数据
	public CityCode regionResult;

	//市数据
	public ChoiceItem cityResult;

	public abstract void doTitleChose(int type, CityCode vo);

	public void changeTitle(String title) {
		actionBar.setTitle(title);
	}

}
