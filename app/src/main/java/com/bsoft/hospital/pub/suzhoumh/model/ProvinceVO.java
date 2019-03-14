package com.bsoft.hospital.pub.suzhoumh.model;


import com.bsoft.hospital.pub.suzhoumh.picker.model.IPickerViewData;

import java.util.ArrayList;

/**
 */
public class ProvinceVO implements IPickerViewData {

    /**
     * des : 北京市
     * code : 110000
     * children :
     */

    public String des;
    public String code;
    public ArrayList<CityVO> children;

    public ProvinceVO() {
    }

    public ProvinceVO(String des, String code) {
        this.des = des;
        this.code = code;
    }

    @Override
    public String getPickerViewText() {
        return des;
    }
}
