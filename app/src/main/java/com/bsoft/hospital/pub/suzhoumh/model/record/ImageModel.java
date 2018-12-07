package com.bsoft.hospital.pub.suzhoumh.model.record;

import java.io.Serializable;

import android.graphics.Bitmap;

/**
 * @author Tank   E-mail:zkljxq@126.com
 * 
 * @类说明
 */
public class ImageModel implements Serializable {

	public int type;
	public String uploadPath;
	public String storePath;
	public Bitmap bitmap;

}
