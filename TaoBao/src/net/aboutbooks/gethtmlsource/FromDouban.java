package net.aboutbooks.gethtmlsource;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.concurrent.CountDownLatch;

import org.htmlparser.tags.LinkTag;

import net.aboutbooks.pojo.AboutBook;
import net.hsg.tools.HsgTools;

public class FromDouban extends Thread  implements FromInterface
{
	public FromDouban()
	{}
	String url="http://book.douban.com/subject_search?search_text=";
	@Override
	public ArrayList<AboutBook> getBybookName(String bookName,int maxNum) 
	{
		ArrayList<AboutBook> aboutBooksList=new ArrayList<AboutBook>();
		try 
		{
			bookName=URLEncoder.encode(bookName,"utf-8");
		}
		catch (UnsupportedEncodingException e) 
		{
			e.printStackTrace();
			bookName=URLEncoder.encode(bookName);
		}
		String html=null;
		try 
		{
			System.out.println("豆瓣搜索："+url+bookName);
			html=tools.getContent(url+bookName);
		} catch (IOException e) 
		{
			try 
			{
				try {
					sleep(1000);
				} catch (InterruptedException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				html=tools.getContent(url+bookName);
			} catch (IOException e1) 
			{
				e1.printStackTrace();
				return null;
			}
		}
			ArrayList<String> resultList=tools.getByREX(html,"<a class=\"nbg\".+?>");
			int i=0;
			for (Iterator iterator = resultList.iterator(); iterator.hasNext();) 
			{
				if(i>=maxNum)
					break;
				String string = (String) iterator.next();
				LinkTag lt =tools.getLinkTagFromLinkStr(string, url+bookName);
				string=lt.getLink();
				aboutBooksList.add(getByLink(string));
				i++;
			}
			return aboutBooksList;
	}
	public AboutBook getByLink(String link)
	{
		AboutBook aboutBook=new AboutBook();
		aboutBook.website=link;
		String html=null;
		try 
		{
			html=tools.getContent(link);

		} 
		catch (IOException e) 
		{
			try{
				html=tools.getContent(link);
			}
			catch (IOException e1) 
			{
				e.printStackTrace();
				return null;
			}
		}
			//获取简介
			String jianjie=tools.getStrByREX(html,"<div class=\"related_info\">.+?内容简介.+?</div>");
			if(jianjie!=null&&!jianjie.trim().equals(""))
			{
				HsgTools hsgTools = new HsgTools();
				jianjie = tools.getStrByREX(jianjie, "((<div class=\"indent\">)|(内容简介\\s*：)).+?</div>");
				if(jianjie !=null&&!jianjie.trim().equals("")){
					jianjie = hsgTools.jianjiechuli(jianjie);
					aboutBook.setJianjie(jianjie);	
				}
			}
			//获取图片
			String img=tools.getStrByREX(html,"<img[^<>]*rel=\"v:photo\"[^<>]*>");
			if(img!=null&&!img.trim().equals(""))
			{
				img=tools.getSrcFromImgtag(img,link);
				aboutBook.setImgSrc(img);
			}
			//获取书名
			String bookName=tools.getStrByREX(html,"<span property=\"v:itemreviewed\">.+?</span>");
			if(bookName!=null&&!bookName.trim().equals(""))
			{
//				LinkTag lt=tools.getLinkTagFromLinkStr(bookName,link);
//				bookName=lt.getAttribute("title");
//				aboutBook.setName(bookName.trim());
				aboutBook.setName(tools.Html2Text(bookName));
			}
			//获取作者,译者
			String zuozhe=tools.getStrByREX(html,"作\\s*者</span>.+?</a>");
			if(zuozhe!=null&&!zuozhe.trim().equals(""))
			{
				zuozhe=zuozhe.replace("\\s+", "").replace("作者","").replace(":","").replace("：","");
				zuozhe=tools.Html2Text(zuozhe);
				aboutBook.setZuozhe(zuozhe.trim());
			}
			String yizhe=tools.getStrByREX(html,"译\\s*者</span>.+?</a>");
			if(yizhe!=null&&!yizhe.trim().equals(""))
			{
//				yizhe=yizhe.replace("译者","").replace(":","").replace("：","");
				yizhe=tools.Html2Text(yizhe);
				aboutBook.setZuozhe(aboutBook.getZuozhe()+" "+yizhe.trim());
			}
			//获取isbn
			String isbn=tools.getStrByREX(html,"ISBN:</span>.+?<br\\s*/>");
			if(isbn!=null&&!isbn.trim().equals(""))
			{
				isbn=isbn.replace("ISBN:","").replace(":","").replace("：","").replace("-","");
				isbn=tools.Html2Text(isbn);
				aboutBook.setIsbn(isbn.trim());
			}
			//获取出版社
			String chubanshe=tools.getStrByREX(html,"出版社:</span>.+?<br\\s*/>");
			if(chubanshe!=null&&!chubanshe.trim().equals(""))
			{
				chubanshe=chubanshe.replace("出版社:","").replace(":","");
				chubanshe=tools.Html2Text(chubanshe);
				aboutBook.setChubanshe(chubanshe.trim());
			}
			//获取页数
			String yeshu=tools.getStrByREX(html,"页数:</span>.+?<br\\s*/>");
			if(yeshu!=null&&!yeshu.trim().equals(""))
			{
				yeshu=yeshu.replace("页数","").replace(":","");
				yeshu=tools.Html2Text(yeshu);
				aboutBook.setYeshu(yeshu.trim());
			}
			//获取出版年
			String chubannian=tools.getStrByREX(html,"出版年:</span>.+?<br\\s*/>");
			if(chubannian!=null&&!chubannian.trim().equals(""))
			{
				chubannian=chubannian.replace("出版年","").replace(":","");
				chubannian=tools.Html2Text(chubannian);
				aboutBook.setChubannian(chubannian.trim());
				if(aboutBook.getChubanshe()!=null&&!aboutBook.getChubanshe().trim().equals(""))
				{
					aboutBook.setChubanshe(aboutBook.getChubanshe()+"，"+aboutBook.getChubannian());	
				}else {
					aboutBook.setChubanshe(aboutBook.getChubannian());
				}
				
			}
		return aboutBook;
	}
	private CountDownLatch end;
	private ArrayList<AboutBook> alArrayList;
	private String bookName;
	private int resultNum=2;
	@Override
	public void run() 
	{
		ArrayList<AboutBook> als=getBybookName(this.bookName,this.resultNum);
		if(als!=null)
		{
			synchronized (this.alArrayList) 
			{
				this.alArrayList.addAll(als);
			}
		}
		System.out.println("########dou 豆瓣跳出");
		this.end.countDown();
	}
	public FromDouban(ArrayList<AboutBook> abs,String bookname,CountDownLatch end1,int resultNum)
	{
//		this.begin=begin1;
		this.end=end1;
		this.alArrayList=abs;
		this.bookName=bookname;
		this.resultNum=resultNum;
	}
}
