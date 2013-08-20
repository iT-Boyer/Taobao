// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi space 
// Source File Name:   DbManager.java

package net.hsg.sql;

import java.io.IOException;
import java.io.PrintStream;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Properties;


import org.apache.commons.dbcp.BasicDataSource;

public class DbManager
{

	private String driver;
	private String url;
	private String Name;
	private String Password;
	private String maxActive;
	private BasicDataSource ds;
	private String properties;
	private static DbManager dbm = null;

	public DbManager()
		throws IOException
	{
		driver = "com.mysql.jdbc.Driver";
		url = "jdbc:mysql://localhost:3306/";
		Name = "root";
		Password = "zhao";
		maxActive = "10";
		ds = null;
		properties = "d:/database/databasecfg.properties";
		startPool();
	}

	public static DbManager getInstance()
		throws IOException
	{
		if (dbm == null)
			dbm = new DbManager();
		return dbm;
	}

	public BasicDataSource getDs()
	{
		return ds;
	}

	private void startPool()
		throws IOException
	{
		loadProperties();
		ds = new BasicDataSource();
		ds.setDriverClassName(driver.trim());
		ds.setUsername(Name);
		ds.setPassword(Password);
		ds.setUrl(url);
		ds.setMaxIdle(0);
		ds.setMaxWait(60000);
		ds.setTestOnBorrow(true);
		ds.setValidationQuery("select 1");
		ds.setMaxActive(Integer.parseInt(maxActive));
		System.out.println("@databasesource started...............");
	}

	private void loadProperties()
		throws IOException
	{
		Properties props = new Properties();
		props.load(getClass().getClassLoader().getResourceAsStream("databasecfg.properties"));
		driver = props.getProperty("DB_DRIVER");
		url = props.getProperty("DB_URL");
		Name = props.getProperty("DB_LOGIN_NAME");
		Password = props.getProperty("DB_LOGIN_PASSWORD");
	}

	public void ShutdownPool()
		throws SQLException
	{
		ds.close();
	}

	public Connection getConnection()
		throws SQLException, IOException
	{
		Connection conn = null;
		if (ds == null)
			startPool();
		conn = ds.getConnection();
		return conn;
	}

}
