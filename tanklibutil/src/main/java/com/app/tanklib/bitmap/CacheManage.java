package com.app.tanklib.bitmap;

import android.content.Context;
import android.graphics.Bitmap;

import com.app.tanklib.AppContext;
import com.app.tanklib.bitmap.task.ImageRequest;
import com.app.tanklib.util.MD5;

import java.io.File;

/**
 * 图片内存管理总类
 * 
 * 头像类的图片放入内存，大图放入内存存储器，其他规格暂与大图相同操作
 * 
 * @author zkl
 * 
 */
public class CacheManage {

	public static final int IMAGE_TYPE_BIG = 1;
	public static final int IMAGE_TYPE_HEADER = 2;
	public static final int IMAGE_TYPE_SMALL = 3;
	public static final int IMAGE_TYPE_PROGRESS = 4;

	// 硬盘缓存
	DiskCache diskCache;
	// 图片计数器管理
	BitmapCounterManager countManager;

	public CacheManage(Context context) {
		this.diskCache = new DiskCache(context);
		this.countManager = new BitmapCounterManager();
	}
	
	public BitmapCounterManager getBitCounter(){
		return countManager;
	}
	

	public File getCachePath() {
		return diskCache.getCacheFolder();
	}

	public static CacheManage getInstance() {
		Context localContext = AppContext.getContext();
		CacheManage cache = (CacheManage) localContext
				.getSystemService("com.szzy.app.service.cachemanage");
		if (cache == null)
			cache = (CacheManage) localContext.getApplicationContext()
					.getSystemService("com.szzy.app.service.cachemanage");
		if (cache == null)
			throw new IllegalStateException("cache not available");
		return cache;
	}

	public Bitmap getBitmap(ImageRequest request) {
		String key = MD5.getMD5(request.imageUrl);
		Bitmap data = null;
		if (countManager.containsKey(key)) {
			data = countManager.getBitmap(key);
		}
		if (null == data && diskCache.containsKey(key)) {
			data = diskCache.getBitmap(key, request);
//			if (null != data) {
//				countManager.put(key, data);
//			}
		}
		return data;
	}
	
	

	public synchronized void put(String _key, Bitmap data, int type) {
		String key = MD5.getMD5(_key);
		System.out.println("CacheManage  put " + key);
		countManager.put(key, data);
		if (!diskCache.containsKey(key)) {
			diskCache.put(key, data);
		}
	}

	public void clearCache() {
		countManager.clearCache();
		// diskCache.clearCache();
	}

	public boolean containsKey(String _key, int type) {
		String key = MD5.getMD5(_key);
		return countManager.containsKey(key) || diskCache.containsKey(key);
	}

	public DiskLruCache.Editor getEditor(String key) {
		return diskCache.getEditor(MD5.getMD5(key));
	}

	public void deskflush() {
		diskCache.deskflush();
	}

	public void release(String _key) {
		countManager.release(MD5.getMD5(_key));
	}

	// public synchronized void addBitMapVo(String url, String activityName,
	// int position) {
	// BitMapVo vo = new BitMapVo();
	// vo.activityName = activityName;
	// vo.position = position;
	// if (mBmpCountMap.containsKey(url)) {
	// mBmpCountMap.get(url).add(vo);
	// } else {
	// HashSet<BitMapVo> blist = new HashSet<BitMapVo>();
	// blist.add(vo);
	// mBmpCountMap.put(url, blist);
	// }
	// }
	//
	// public synchronized void releaseBitmap(HashSet<StatckBitMapVo> set,
	// String activityName) {
	// HashSet<BitMapVo> list = null;
	// for (StatckBitMapVo urlVo : set) {
	// if (mBmpCountMap.containsKey(urlVo.url)) {
	// list = mBmpCountMap.get(urlVo.url);
	// if (list.size() == 1) {
	// memoryCache.realeaseBitmap(MD5Util.getMD5(urlVo.url));
	// mBmpCountMap.remove(urlVo.url);
	// continue;
	// } else {
	// for (BitMapVo bmv : list) {
	// if (bmv.activityName.equals(activityName)
	// && bmv.position == urlVo.position) {
	// list.remove(bmv);
	// break;
	// }
	// }
	// }
	// }
	// }
	// }
	//
	// /**
	// * 用于LRUCahce的bitmap释放
	// *
	// * @param url
	// * @param activityName
	// * @param position
	// */
	// public void releaseBitmapByLRUCache(String url, String activityName,
	// int position) {
	// synchronized (mBmpCountMap) {
	// HashSet<BitMapVo> list = null;
	// if (mBmpCountMap.containsKey(url)) {
	// list = mBmpCountMap.get(url);
	// if (list.size() == 1) {
	// // System.out.println("realeaseBitmap1 : " + url);
	// memoryCache.realeaseBitmap(MD5Util.getMD5(url));
	// mBmpCountMap.remove(url);
	// } else {
	// for (BitMapVo bmv : list) {
	// if (bmv.activityName.equals(activityName)
	// && bmv.position == position) {
	// list.remove(bmv);
	// break;
	// }
	// }
	// }
	// }
	// }
	// }
	//
	// public void realeaseImageUrl(String url) {
	// memoryCache.realeaseBitmap(MD5Util.getMD5(url));
	// }
	//
	// public void cleanCache() {
	// mBmpCountMap.clear();
	// }
}
