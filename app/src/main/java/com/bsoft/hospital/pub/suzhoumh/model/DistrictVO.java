package com.bsoft.hospital.pub.suzhoumh.model;


import com.bsoft.hospital.pub.suzhoumh.picker.model.IPickerViewData;

/**
 */
public class DistrictVO implements IPickerViewData {

    /**
     * des : 东城区
     * code : 110101
     */

    public String des;
    public String code;

    public DistrictVO() {
    }

    public DistrictVO(String des, String code) {
        this.des = des;
        this.code = code;
    }

    @Override
    public String getPickerViewText() {
        return des;
    }
}
