package com.books.urlstream;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;

import com.forfuture.urlstream.CharTools;



public class oldurlconnector
{
	private String ecoding; //编码方式
	// 一个public方法，返回字符串，错误则返回"error open url"
	public oldurlconnector(String ecoding)
	{
		this.ecoding=ecoding;
	}
	public oldurlconnector()
	{
		this.ecoding="UTF-8";
	}
	public  String getContent(String strUrl)
	{
		try
		{
			URL url = new URL(strUrl);
			URLConnection uc = url.openConnection();
			/* 识别网页编码类型 */
//			if (uc.getContentType().contains("utf-8"))
//				ecoding = "utf-8";
//			else
//				if (uc.getContentType().contains("gb2312"))
//					ecoding = "gb2312";
//				else
//					if (uc.getContentType().contains("gbk"))
//						ecoding = "gbk";
			//
			// System.out.println("++++++++++++++++++++"+uc.getContentType());
			BufferedReader br = new BufferedReader(new InputStreamReader(url.openStream(), ecoding));
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
			return -1;
		}
		try
		{
			is = url.openStream();
		}
		catch (IOException e1)
		{
			return -1;
		}
		File f = new File(FolderName);
		f.mkdirs();
		try
		{
			os = new FileOutputStream(FolderName + "\\" + fileName);
			int bytesRead = 0;
			byte[] buffer = new byte[8192];
			while ((bytesRead = is.read(buffer, 0, 8192)) != -1)
			{
				os.write(buffer, 0, bytesRead);
			}
		}
		catch (FileNotFoundException e)
		{
			//System.out.println("wkq  找不到文件1n  " + FolderName + "\\" + fileName);
			return -1;
		}
		catch (IOException e)
		{
			e.printStackTrace();
			return -1;
		}
		return 2;
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
