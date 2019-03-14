package com.bsoft.hospital.pub.suzhoumh.model;

import com.bsoft.hospital.pub.suzhoumh.picker.model.IPickerViewData;

import java.util.ArrayList;

/**
 */
public class CityVO implements IPickerViewData {

    /**
     * des : 市辖区
     * code : 110100
     * children : [{"des":"东城区","code":"110101"},{"des":"西城区","code":"110102"},{"des":"朝阳区","code":"110105"},{"des":"丰台区","code":"110106"},{"des":"石景山区","code":"110107"},{"des":"海淀区","code":"110108"},{"des":"门头沟区","code":"110109"},{"des":"房山区","code":"110111"},{"des":"通州区","code":"110112"},{"des":"顺义区","code":"110113"},{"des":"昌平区","code":"110114"},{"des":"大兴区","code":"110115"},{"des":"怀柔区","code":"110116"},{"des":"平谷区","code":"110117"},{"des":"密云县","code":"110128"},{"des":"延庆县","code":"110129"}]
     */

    public String des;
    public String code;
    public ArrayList<DistrictVO> children;

    public CityVO() {
    }

    public CityVO(String des, String code) {
        this.des = des;
        this.code = code;
    }

    @Override
    public String getPickerViewText() {
        return des;
    }
}
