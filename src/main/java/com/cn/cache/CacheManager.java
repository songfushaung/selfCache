package com.cn.cache;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * 
 * 
 * @classDesc: 功能描述:缓存api
 * @author: 宋付双
 * @createTime: 2017年9月27日
 * @version: v1.0
 * @copyright:
 */
public class CacheManager {
	//存放缓存数据
	private Map<String, Cache> cacheMap = new HashMap<>();
	    /**
		 * 
		 * @methodDesc: 功能描述:向缓存中存储数据,但没有过期时间
		 * @author: 宋付双
		 * @param: 
		 * @createTime:2017年9月27日
		 * @returnType:@param 
		 * @copyright:
	 */
	public synchronized void put(String key, Object oj) {
		put(key, oj, null);
	}
    /**
		 * 
		 * @methodDesc: 功能描述:向缓存中存储数据,但有过期时间
		 * @author: 宋付双
		 * @param: 
		 * @createTime:2017年9月27日
		 * @returnType:@param 
		 * @copyright:
	 */
	public synchronized void put(String key, Object object, Long timeOut) {
		if (object == null) {
			return;
		}
		Cache cache = new Cache();
		cache.setKey(key);
		if (timeOut != null)
			cache.setTimeOut(timeOut + System.currentTimeMillis());
		cache.setValue(object);
		
		cacheMap.put(key, cache);
	}
	/**
	 * 
		 * 
		 * @methodDesc: 功能描述:获取缓存值
		 * @author: 宋付双
		 * @param: 
		 * @createTime:2017年9月27日
		 * @returnType:@param 
		 * @copyright:
	 */
	public synchronized Object get(String key) {
		Cache cache = cacheMap.get(key);
		Object oj = null;
		if (cache != null) {
			oj = cache.getValue();
		}
		return oj;
	}
	/**
		 * 
		 * @methodDesc: 功能描述:删除缓存
		 * @author: 宋付双
		 * @param: 
		 * @createTime:2017年9月27日
		 * @returnType:@param 
		 * @copyright:
	 */
	public synchronized void deleteCache(String key) {
		cacheMap.remove(key);
	}
	/**
		 * 
		 * @methodDesc: 功能描述:检查有效期
		 * @author: 宋付双
		 * @param: 
		 * @createTime:2017年9月27日
		 * @returnType:@param 
		 * @copyright:
	 */
	public synchronized void checkValidityData() {
		for (String key : cacheMap.keySet()) {
			Cache cache = cacheMap.get(key);
			Long timeOut = cache.getTimeOut();
			if (timeOut == null) {
				return;
			}
			long currentTime = System.currentTimeMillis();
			long endTime = timeOut;
			long result = (currentTime - endTime);
			if (result > 0) {
				System.out.println("clean:"+key);
				cacheMap.remove(key);
			}
		}
	}
	public static void main(String[] args) throws InterruptedException {
		CacheManager cacheManager = new CacheManager();
		// cacheManager.put("lisi", 0);
		cacheManager.put("zhangsan", "jj", 5000l);
		ScheduledExecutorService scheduledThreadPool = Executors.newScheduledThreadPool(5);
		scheduledThreadPool.schedule(new Runnable() {
			public void run() {
				cacheManager.checkValidityData();
			}
		}, 5000, TimeUnit.MILLISECONDS);
		Thread.sleep(5000);
		System.out.println(cacheManager.get("zhangsan"));
	}
}
