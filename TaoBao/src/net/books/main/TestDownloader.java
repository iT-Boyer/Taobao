package net.books.main;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;

import net.books.pojo.Books;
import net.rile.sql.DataBaseDao;

import org.htmlparser.tags.LinkTag;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;


import com.forfuture.tools.Tools;

public class TestDownloader
{
	private Tools tools=new Tools();
	public void Catch(String url) throws IOException
	{
		int pagesum=0;
		DataBaseDao dd=new DataBaseDao();
		while(url!=null)
		{
			pagesum++;
			System.out.println("第"+pagesum+"页    获取源码："+url);
			Document doc=Jsoup.connect(url).get();
			String html=doc.outerHtml();
//			System.out.println(html);
			//抓取书名链接
			ArrayList<String> als=tools.getByREX(html,"<dt>\\s*<a.+?<dd>");
			System.out.println(als.size());
			for (Iterator iterator = als.iterator(); iterator.hasNext();)
			{
				String string = (String) iterator.next();
				string=string.replace("<dd>", "").replace("<dt>", "").replace("</dd>", "").replace("</dt>", "").trim();
				string=tools.getLinkTagFromLinkStr(string,url).getLink();  //string是树的链接
				System.out.println(string);
				/***
				 * 在这里开始写代码抓取书。
				 */
				String htmlPage=tools.getContent(string);
				//开始写抓取书的源码
				Books books=new Books();
				String bookname=tools.Html2Text(tools.getStrByREX(htmlPage,"<label>.+?</label>")).replace("TXT下载","").trim();
				String summary=tools.Html2Text(tools.getStrByREX(htmlPage ,"<table.+?</table>")).trim();
				books.setBookname(bookname);
				books.setSummary(summary);
				books.setUrl(string);
				dd.insert(books);
				String txturl=tools.getStrByREX(htmlPage,"<a href=\"[^<>]*\" target=\"_blank\">本电子书普通下载点</a>");
				txturl=tools.getLinkTagFromLinkStr(txturl,string).getLink();
				System.out.println("下载 获取到的txt链接地址是："+txturl);
				try
				{
					tools.saveFile(txturl,"d:/books/" ,bookname+".txt");
				} catch (Exception e)
				{
					// TODO: handle exception
				}
				System.out.println("下载 完毕："+bookname);
			}
			//获取到下一页的链接,并将下一页的链接赋值给url
			String next=tools.getStrByREX(html, "<a href=[^<>]*>下一页</a> ");
			LinkTag nextt=tools.getLinkTagFromLinkStr(next,url);
			url=nextt.getLink();
		}
	}
}
