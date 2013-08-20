package net.aboutbooks.gethtmlsource;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.concurrent.CountDownLatch;

import net.aboutbooks.pojo.AboutBook;

import org.htmlparser.tags.LinkTag;

public class FromDangdang extends Thread implements FromInterface
{
	public FromDangdang()
	{}
	String url="http://search.dangdang.com/search_pub.php?key=";
	@Override
	public ArrayList<AboutBook> getBybookName(String bookName,int maxNum) 
	{
		ArrayList<AboutBook> aboutBooksList=new ArrayList<AboutBook>();
		try {
			bookName=URLEncoder.encode(bookName,"gb2312");
		} catch (UnsupportedEncodingException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		try 
		{
			System.out.println("当当搜索："+url+bookName);
			String html=tools.getContent(url+bookName);
			if(html.contains("&times;"))  //判断是否是当当没有搜索到此书。如果有这个判断子的话基本上就是没有搜到了。本来是“在全部商品下，没有找到”，害怕有乱么，就改成这个
			{
				html=tools.getContent(url+bookName);
				if(html.contains("&times;"))
				{
					html=tools.getContent(url+bookName);
				}
			}
			ArrayList<String> resultList=tools.getByREX(html,"<a[^<>]*name=\"p_name\".+?<div class=\"panel operate\">");
			int i=0;
			for (Iterator iterator = resultList.iterator(); iterator.hasNext();) 
			{
				if(i>=maxNum)
					break;
				String string = (String) iterator.next();
				LinkTag lt =tools.getLinkTagFromLinkStr2(string, url+bookName);
				String content=tools.getStrByREX(string,"<li class=\"describ\">.+?<div class=\"panel operate\">");
				string=lt.getLink();
				aboutBooksList.add(getByLink(string,content));
				i++;
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return getBybookName(bookName,maxNum); 
		}
		return aboutBooksList;
	}
	public AboutBook getByLink(String link,String jianjie)
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
			try 
			{
				html=tools.getContent(link);
			} 
			catch (IOException e1) 
			{
				return null;
			}
		}
			String bookName=tools.getStrByREX(html,"<div class=\"h1_title book_head\">.+?</h1>");
//			String jianjie=tools.getStrByREX(html,"<meta name=\"description\".+?/>");
			String img=tools.getStrByREX(html,"<div class=\"show book\">.+?</div>");
			html=tools.getStrByREX(html,"<div class=\"info book_r\">.+?<div class=\"num clearfix\">");
			//获取书名
			if(bookName!=null&&!bookName.trim().equals(""))
			{
				bookName=tools.Html2Text(bookName);
				aboutBook.setName(bookName.trim());
			}
			//获取图片
			if(img!=null&&!img.trim().equals(""))
			{
				img=tools.getStrByREX(img,"<img.+?>");
				img=tools.getSrcFromImgtag(img,link);
				aboutBook.setImgSrc(img);
			}
			//获取简介
			if(jianjie!=null&&!jianjie.trim().equals(""))
			{
				jianjie=tools.Html2Text(jianjie).trim();
				aboutBook.setJianjie(jianjie);
			}
			//获取作者,译者
			String zuozhe=tools.getStrByREX(html,"<p>作\\s*者.+?</p>");
			if(zuozhe!=null&&!zuozhe.trim().equals(""))
			{
				zuozhe=zuozhe.replaceAll("作\\s*者","").replace(":","").replace("：","");
				zuozhe=tools.Html2Text(zuozhe);
				aboutBook.setZuozhe(zuozhe.trim());
			}
//			String yizhe=tools.getStrByREX(html,"译者</span>.+?</a>");
//			if(yizhe!=null&&!yizhe.trim().equals(""))
//			{
////				yizhe=yizhe.replace("译者","").replace(":","").replace("：","");
//				yizhe=tools.Html2Text(yizhe);
//				aboutBook.setZuozhe(aboutBook.getZuozhe()+" "+yizhe.trim());
//			}
			//获取isbn
			String isbn=tools.getStrByREX(html,"I\\s*S\\s*B\\s*N.+?</span>");
			if(isbn!=null&&!isbn.trim().equals(""))
			{
				isbn=isbn.replaceAll("I\\s*S\\s*B\\s*N","").replace(":","").replace("：","").replace("-","");
				isbn=tools.Html2Text(isbn);
				aboutBook.setIsbn(isbn.trim());
			}
			//获取出版社
			String chubanshe=tools.getStrByREX(html,"出\\s*版\\s*社\\s*.+?<p>");
			if(chubanshe!=null&&!chubanshe.trim().equals(""))
			{
				chubanshe=chubanshe.replaceAll("出\\s+版\\s+社","").replace(":","").replace("：","");
				chubanshe=tools.Html2Text(chubanshe);
				aboutBook.setChubanshe(chubanshe.trim());
			}
			//获取页数
			String yeshu=tools.getStrByREX(html,"页\\s*数.+?</span>");
			if(yeshu!=null&&!yeshu.trim().equals(""))
			{
				yeshu=yeshu.replaceAll("页\\s*数","").replace(":","").replace("：","");
				yeshu=tools.Html2Text(yeshu);
				aboutBook.setYeshu(yeshu.trim());
			}
			//获取出版年
			String chubannian=tools.getStrByREX(html,"<p>出版时间.+?</p>");
			if(chubannian!=null&&!chubannian.trim().equals(""))
			{
				chubannian=chubannian.replaceAll("出版时间","").replace(":","").replace("：","");
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
//	private CountDownLatch begin;
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
		this.end.countDown();
	}
	public FromDangdang(ArrayList<AboutBook> abs,String bookname,CountDownLatch end1,int resultNum)
	{
//		this.begin=begin1;
		this.end=end1;
		this.alArrayList=abs;
		this.bookName=bookname;
		this.resultNum=resultNum;
	}
}
