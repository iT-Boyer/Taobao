package net.books.main;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;

import org.htmlparser.tags.LinkTag;

import com.forfuture.tools.Tools;

public class Shuoming {

	/**
	 * @param args
	 * @throws IOException 
	 */
	public static void main(String[] args) throws IOException 
	{
		//我发给你的jar包中有一个Tools类，是我们的一些工具的集成。
		Tools tools=new Tools();
		//获取到网页源码
		String htmls=tools.getContent("http://www.baidu.com");
		System.out.println(htmls);
		//执行正则表达式
		//1:从一个字符串中取出匹配的一个字符串
		String as1=tools.getStrByREX(htmls,"<a.+?>"); //这个函数取出第一个匹配的字符串。
		System.out.println("链接"+as1);
		//2:从一个字符串中取出匹配的列表
		ArrayList<String> aslist=tools.getByREX(htmls,"<a.+?>");
		for (Iterator iterator = aslist.iterator(); iterator.hasNext();) 
		{
			String string = (String) iterator.next();
			System.out.println(string);
		}
		//将一个字符串，类似这样的 <a href="index.jsp" >首页</a>,中的href取出来
		String link="<a href=index.jsp >首页</a>";
		LinkTag lt=tools.getLinkTagFromLinkStr(link,"http://www.baidu.com");  //第二个参数是父链接
		System.out.println(lt.getLink());
		System.out.println(lt.getLinkText());
		//从<img src="http://img1.cache.netease.com/catchpic/7/7A/7AC5DFE6E5D220EA678E8DDE64A34B15.jpg" /> 中取出src
		String imgs="<img src=\"http://img1.cache.netease.com/catchpic/7/7A/7AC5DFE6E5D220EA678E8DDE64A34B15.jpg\" />";
		System.out.println(tools.getSrcFromImgtag(imgs, "http://img1.cache.netease.com/catchpic/7/7A/"));  
		//下载文件
		tools.saveFile("http://img1.cache.netease.com/catchpic/7/7A/7AC5DFE6E5D220EA678E8DDE64A34B15.jpg", "d:/", "test.jpg");//第一个参数是文件的链接，第二个是放到哪个文件夹中，第三个是保存到本地的文件名字
	}

}
