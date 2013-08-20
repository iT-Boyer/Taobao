package com.books.urlstream;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;

import com.forfuture.urlstream.CharTools;

import net.rile.sql.DataBase;



public class testUrlconnector
{
	private String ecoding; //编码方式
	// 一个public方法，返回字符串，错误则返回"error open url"
	public testUrlconnector(String ecoding)
	{
		this.ecoding=ecoding;
	}
	public testUrlconnector()
	{
		this.ecoding="UTF-8";
	}
	public  String getContent(String strUrl)
	{
		try
		{
			URL url = new URL(strUrl);
			HttpURLConnection  uc = (HttpURLConnection) url.openConnection();
			String eco=uc.getRequestProperty("");
			System.out.println("##################:"+eco);
			BufferedReader br = new BufferedReader(new InputStreamReader(uc.getInputStream(), ecoding));
			String s = "";
			StringBuffer sb = new StringBuffer("");
			while ((s = br.readLine()) != null)
			{
				sb.append(s + "\r\n");
			}
			br.close();
			return sb.toString();
		}
		catch (Exception e)
		{
			return "error open url:";
		}
	}

	/* 保存图片 */
	public int getSaveImage(String strUrl, String Folder, String fileName)
	{
		CharTools charTools = new CharTools();
		URL url = null;
		InputStream is = null;
		OutputStream os = null;
		String FolderName = Folder;
		strUrl=charTools.Utf8URLencode(strUrl);
		try
		{
			url = new URL(strUrl);
		}
		catch (MalformedURLException e2)
		{
			insertLog(strUrl,Folder.trim()+"/"+fileName.trim(),e2.getMessage());
			return -4;
		}
		try
		{
			is = url.openStream();
		}
		catch (IOException e1)
		{
			insertLog(strUrl,Folder.trim()+"/"+fileName.trim(),e1.getMessage());
			return -3;
		}
		File f = new File(FolderName);
		f.mkdirs();
		try
		{
			os = new FileOutputStream(FolderName.trim() + "/" + fileName.trim());
			java.io.ByteArrayOutputStream baos =new java.io.ByteArrayOutputStream();  //内存缓存字节流
			int bytesRead = 0;
			byte[] buffer = new byte[8192];
			while ((bytesRead = is.read(buffer, 0, 8192)) != -1)
			{
				baos.write(buffer, 0, bytesRead);
//				os.write(buffer, 0, bytesRead);
			}
			os.write(baos.toByteArray(),0,baos.toByteArray().length);
		}
		catch (FileNotFoundException e)
		{
			//System.out.println("wkq  找不到文件1n  " + FolderName + "\\" + fileName);
			e.printStackTrace();
			insertLog(strUrl,Folder.trim()+"/"+fileName.trim(),e.getMessage());
			return -1;
		}
		catch (IOException e)
		{
			e.printStackTrace();
			insertLog(strUrl,Folder.trim()+"/"+fileName.trim(),e.getMessage());
			return -2;
		}
		return 2;
	}
	private void insertLog(String website,String locals,String type)
	{
		DataBase db=new DataBase();
		String sql="insert into savefilelog(website,locals,types) values('"+website+"','"+locals+"','"+type+"')";
		db.setSql(sql);
		if(!db.execute())
		{
			System.err.println("插入日志错误！sql："+sql);
		}
		db.close();
	}
	public String getEcoding()
	{
		return ecoding;
	}
	public void setEcoding(String ecoding)
	{
		this.ecoding = ecoding;
	}
}
