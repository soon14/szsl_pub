package com.bsoft.hospital.pub.suzhoumh.model;

import java.io.Serializable;

/**
 * 选择对象
 * 
 * @author xing
 * 
 */
public class ChoiceItem implements Serializable {
	public String index;
	public String itemName;
	public boolean isChoice;

	public ChoiceItem(String index) {
		this.index = index;
	}

	public ChoiceItem(String index, String itemName) {
		this.index = index;
		this.itemName = itemName;
	}

	@Override
	public boolean equals(Object o) {
		return this.index.equals(((ChoiceItem) o).index);
	}

}
