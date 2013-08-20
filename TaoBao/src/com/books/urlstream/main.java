package com.books.urlstream;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.forfuture.urlstream.DetectEcoding;



public class main
{
	/**
	 * @param args
	 * @throws MalformedURLException 
	 */
	public static void main(String[] args) throws MalformedURLException
	{
		DetectEcoding de=new DetectEcoding();
		System.out.println(de.getEcoding("http://epaper.wjol.net.cn:8080/masrb/20100926/5f82409cc2b41a106aecb97470dc95c2.xml"));
		System.out.println(Charset.isSupported("UTF8"));
	}
}