package com.bsoft.hospital.pub.suzhoumh.util;

import java.util.Comparator;

import com.bsoft.hospital.pub.suzhoumh.model.app.appoint.AppointDayOfDateVo;

public class SortClass implements Comparator{

	@Override
	public int compare(Object lhs, Object rhs) {
		// TODO Auto-generated method stub
		AppointDayOfDateVo date0 = (AppointDayOfDateVo)lhs;  
		AppointDayOfDateVo date2 = (AppointDayOfDateVo)rhs;  
        int flag = date0.gzrq.compareTo(date2.gzrq);  
        return flag; 
	}

}
