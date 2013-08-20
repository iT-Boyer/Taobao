package com.books.urlstream;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

import org.springframework.beans.propertyeditors.URLEditor;

import com.forfuture.urlstream.urlconnector;

public class tt
{

	/**
	 * @param args
	 * @throws IOException 
	 */
	public static void main(String[] args) throws IOException
	{
//		System.out.println(URLEncoder.encode("黑暗","gb2312"));
		String text = "图书Url:http://www.eywedu.com/kehuanxiaoshuo/018/index.htm图书版图：http://www.eywedu.com/kehuanxiaoshuo/01a/18.bmp图书名：地心世界猎奇记";
		String []list = text.split("图书Url:");
		System.out.println(list[0]+"@@@@@"+list.length+"@@@@@"+list[1]);
		String []luj = list[1].split("图书版图：");
		System.out.println(luj[0]+"@@@@@@@@@@"+luj[1]);
	}

}
