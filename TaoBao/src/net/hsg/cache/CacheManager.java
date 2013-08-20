package net.hsg.cache;

import java.io.Serializable;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * ��������
 * @author Winter Lau
 */
public class CacheManager 
{

	public static final String CATCH_NAME_CUSTOMER_USESSIONID="usessionid";
	public static final String CATCH_NAME_LOGIN_TIMES="logintime";
	public static final String CATCH_NAME_ROLES="roles";
	public static final String CACHE_NAME_GETSHOPBYID="getshopbyid";
	public static final String CACHE_NAME_GETGOODSTYPEOFSHOP="getGoodsTypeOfShop";
	public static final String CACHE_NAME_GETGOODSBYTYPE="getGoodsByType";
	private final static Log log = LogFactory.getLog(CacheManager.class);
	private static CacheProvider provider;

	static 
	{
		log.info("---------��ʼ��cache-------------");
		initCacheProvider("com.ifangbian.caches.EhCacheProvider");
	}
	
	private static void initCacheProvider(String prv_name){
		try{
			CacheManager.provider = (CacheProvider)Class.forName(prv_name).newInstance();
			CacheManager.provider.start();
			log.info("Using CacheProvider : " + provider.getClass().getName());
		}catch(Exception e){
			log.fatal("Unabled to initialize cache provider:" + prv_name + ", using ehcache default.", e);
			CacheManager.provider = new EhCacheProvider();
		}
	}

	private final static Cache _GetCache(String cache_name, boolean autoCreate) {
		if(provider == null){
			provider = new EhCacheProvider();
		}
		return provider.buildCache(cache_name, autoCreate);
	}

	/**
	 * ��ȡ�����е����
	 * @param name
	 * @param key
	 * @return
	 */
	public final static Object get(String name, Serializable key){
		//System.out.println("GET1 => " + name+":"+key);
		if(name!=null && key != null)
			return _GetCache(name, true).get(key);
		return null;
	}
	
	/**
	 * ��ȡ�����е����
	 * @param <T>
	 * @param resultClass
	 * @param name
	 * @param key
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public final static <T> T get(Class<T> resultClass, String name, Serializable key){
		//System.out.println("GET2 => " + name+":"+key);
		if(name!=null && key != null)
			return (T)_GetCache(name, true).get(key);
		return null;
	}
	
	/**
	 * д�뻺��
	 * @param name
	 * @param key
	 * @param value
	 */
	public final static void set(String name, Serializable key, Serializable value){
		//System.out.println("SET => " + name+":"+key+"="+value);
		if(name!=null && key != null && value!=null)
			_GetCache(name, true).put(key, value);		
	}
	
	/**
	 * �����е�ĳ�����
	 * @param name
	 * @param key
	 */
	public final static void evict(String name, Serializable key){
		if(name!=null && key != null)
			_GetCache(name, true).remove(key);		
	}

	/**
	 * �����е�ĳ�����
	 * @param name
	 * @param key
	 */
	public final static void justEvict(String name, Serializable key){
		if(name!=null && key != null){
			Cache cache = _GetCache(name, false);
			if(cache != null)
				cache.remove(key);
		}
	}

}
