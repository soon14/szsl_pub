package com.app.tanklib.bitmap;

import com.app.tanklib.util.LruCache;
import com.app.tanklib.util.MD5;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;


/**
 * LRU缓存，用于ListView的内存管理
 * 
 * 链接保存不可重复
 * 
 * 
 */

public class IndexUrlCache {

	LruCache<Integer, HashSet<String>> cache;

	public IndexUrlCache() {
		initLru(15);
	}

	public IndexUrlCache(int max) {
		initLru(max);
	}

	void initLru(int max) {
		cache = new LruCache<Integer, HashSet<String>>(max) {

			@Override
			protected int sizeOf(Integer key, HashSet<String> value) {
				return 1;
			}

			@Override
			protected void entryRemoved(boolean evicted, Integer key,
					HashSet<String> oldValue, HashSet<String> newValue) {
				// 销毁对应item的bitmap
				for (String url : oldValue) {
					CacheManage.getInstance().release(url);
				}
			}
		};
	}

	public void add(String url) {
		add(-1, url);
	}

	public void add(int itemNum, String url) {
		synchronized (cache) {
			if (!CacheManage.getInstance().getBitCounter().containsKey(MD5.getMD5(url))) {
				if (cache.containsKey(itemNum)) {
					cache.get(itemNum).add(url);
				} else {
					HashSet<String> list = new HashSet<String>();
					list.add(url);
					cache.put(itemNum, list);
				}
			}
		}
	}

	// 清空
	public void cleanAll() {
		Map<Integer, HashSet<String>> map = cache.getMap();
		Iterator iter = map.entrySet().iterator();
		HashSet<String> set = null;
		Integer key = 0;
		while (iter.hasNext()) {
			Map.Entry entry = (Map.Entry) iter.next();
			set = (HashSet<String>) entry.getValue();
			key = Integer.valueOf(entry.getKey().toString());
			for (String url : set) {
				System.out.println("urlmap  cleanAll " + url);
				CacheManage.getInstance().release(url);
			}
		}
		cache.clean();
	}
}
