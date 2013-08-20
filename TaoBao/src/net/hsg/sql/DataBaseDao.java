// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi space 
// Source File Name:   DataBaseDao.java

package net.hsg.sql;

import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import org.nutz.dao.Condition;
import org.nutz.dao.entity.Record;
import org.nutz.dao.impl.NutDao;
import org.nutz.dao.pager.Pager;

import net.hsg.cache.CacheManager;

/**
 * 添加cache缓存机制
 * */
public class DataBaseDao extends NutDao
{
	public DataBaseDao()
		throws IOException
	{
		super(DbManager.getInstance().getDs());
	}

	public <T> T fetch(Class<T> classOfT, Condition cnd,String cache_name, Serializable key) 
	{
		T value=CacheManager.get(classOfT,cache_name, key);
		if(value==null)	
		{
			value=super.fetch(classOfT, cnd);
			CacheManager.set(cache_name, key, (Serializable)value);
		}
		return value;
	}

	public <T> T fetch(Class<T> classOfT, long id,String cache_name, Serializable key) {
		T value=CacheManager.get(classOfT,cache_name, key);
		if(value==null)	
		{
			value=super.fetch(classOfT, id);
			CacheManager.set(cache_name, key, (Serializable)value);
		}
		return value;
	}

	public <T> T fetch(Class<T> classOfT, String name,String cache_name, Serializable key) {
		T value=CacheManager.get(classOfT,cache_name, key);
		if(value==null)	
		{
			value=super.fetch(classOfT, name);
			CacheManager.set(cache_name, key, (Serializable)value);
		}
		return value;
	}

	public <T> T fetch(Class<T> classOfT,String cache_name, Serializable key) {
		// TODO Auto-generated method stub
		T value=CacheManager.get(classOfT,cache_name, key);
		if(value==null)	
		{
			value=super.fetch(classOfT);
			CacheManager.set(cache_name, key, (Serializable)value);
		}
		return value;
	}

	public Record fetch(String tableName, Condition cnd,String cache_name, Serializable key) {
		// TODO Auto-generated method stub
		Record value=CacheManager.get(Record.class,cache_name, key);
		if(value==null)	
		{
			value=super.fetch(tableName, cnd);
			CacheManager.set(cache_name, key, (Serializable)value);
		}
		return value;
	}

	public <T> T fetch(T obj,String cache_name, Serializable key) {
		// TODO Auto-generated method stub
		if(obj!=null)
		{
			T value=(T) CacheManager.get(cache_name, key);
			if(value==null)
			{
				value=super.fetch(obj);
				return value;
			}
			else
				return value;
		}
		else
		{
			return super.fetch(obj);
		}
	}

	public <T> List<T> query(Class<T> classOfT, Condition cnd, Pager pager,String cache_name, Serializable key) {
		// TODO Auto-generated method stub
		List<T> value=CacheManager.get(ArrayList.class, cache_name, key);
		if(value==null)
		{
			value=(List<T>) super.query(classOfT, cnd, pager);
			CacheManager.set(cache_name, key, (Serializable)value);
		}
		return value;
	}

	public <T> List<T> query(Class<T> classOfT, Condition cnd,String cache_name, Serializable key) {
		// TODO Auto-generated method stub
		List<T> value=CacheManager.get(ArrayList.class,cache_name, key);
		if(value==null)
		{
			value=(List<T>) super.query(classOfT, cnd);
			CacheManager.set(cache_name, key, (Serializable)value);
		}
		return value;
	}

	public List<Record> query(String tableName, Condition cnd, Pager pager,String cache_name, Serializable key) {
		// TODO Auto-generated method stub
//		return super.query(tableName, cnd, pager);
		List<Record> value=CacheManager.get(ArrayList.class,cache_name, key);
		if(value==null)
		{
			value= (List<Record>) super.query(tableName, cnd, pager);
			CacheManager.set(cache_name, key, (Serializable)value);
		}
		return value;
	}

	public List<Record> query(String tableName, Condition cnd,String cache_name, Serializable key) {
		// TODO Auto-generated method stub
		List<Record> value=CacheManager.get(ArrayList.class, cache_name, key);
		if(value==null)
		{
			value=(List<Record>) super.query(tableName, cnd);
			CacheManager.set(cache_name, key,(Serializable) value);
		}
		return super.query(tableName, cnd);
	}
	
}
