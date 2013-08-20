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

public class FromZhusanjiao extends Thread  implements FromInterface
{
	public FromZhusanjiao()
	{}
	String url="http://books.gdlink.net.cn/search?Field=all&channel=search&sw=";
	@Override
	public ArrayList<AboutBook> getBybookName(String bookName,int maxNum) 
	{
		ArrayList<AboutBook> aboutBooksList=new ArrayList<AboutBook>();
		try {
			bookName=URLEncoder.encode(bookName,"gb2312");
		} catch (UnsupportedEncodingException e3) {
			// TODO Auto-generated catch block
			e3.printStackTrace();
		}
		String html=null;
		try 
		{
			System.out.println("珠江搜索："+url+bookName);
			html=tools.getContentByWebClient(url+bookName);

		} catch (IOException e) 
		{
			try {
				sleep(1000);
			} catch (InterruptedException e2) {
				// TODO Auto-generated catch block
				e2.printStackTrace();
			}
			try {
				html=tools.getContentByWebClient(url+bookName);
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		}
			ArrayList<String> resultList=tools.getByREX(html,"<div id=\"bb\">.+?<div class=\"get\">");
			int i=0;
			for (Iterator iterator = resultList.iterator(); iterator.hasNext();) 
			{
				HsgTools hsgTools = new HsgTools();
				if(i>=maxNum)
					break;
				String string = (String) iterator.next();
				//抓简介
				String jianjie=tools.getStrByREX(string,"((<br\\s*/>)|(<\\s*br\\s*>))简介.+?<div class=\"get\">");
				if (jianjie!=null&&jianjie.trim().equals(""))
						jianjie = hsgTools.jianjiechuli(jianjie);
				//抓图
				String img=tools.getStrByREX(string,"<IMG.+?>");
				//
				string=tools.getStrByREX(string,"<a.+?</a>");
				LinkTag lt =tools.getLinkTagFromLinkStr(string, url+bookName);
				string=lt.getLink();
				if(string!=null)
				{
					AboutBook abt=getByLink(string,jianjie,img);
					if(abt!=null)
					{
						aboutBooksList.add(abt);
						i++;
					}
				}
			}
		return aboutBooksList;
	}
	public AboutBook getByLink(String link,String jianjie,String img)
	{
		AboutBook aboutBook=new AboutBook();
		aboutBook.website=link;
		String html=null;
		try 
		{
			html=tools.getContentByWebClient(link.replace("amp;",""));
			if(html.contains("您没有足够的权限访问该网页"))
			{
				try {
					sleep(1000);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				html=tools.getContentByWebClient(link.replace("amp;",""));
			}

		} 
		catch (IOException e) 
		{
			try {
				sleep(1000);
			} catch (InterruptedException e2) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			try {
				html=tools.getContentByWebClient(link.replace("amp;",""));
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		}
			String infoStr=tools.getStrByREX(html,"<input\\s*type=\"hidden\"\\s*name=\\s*\"content\"[^<>]*/>");
			if(infoStr==null)
			{
				return null;
			}
			infoStr=infoStr.replace("<input", "<a")+"</a>";
			LinkTag lt=tools.getLinkTagFromLinkStr(infoStr,link);
			infoStr=lt.getAttribute("value").trim();
			///////////////
//			String zuozhe = tools.getStrByREX(infoStr, "【作\\s*者】[^\\\\]+?【");
//			String chuban = tools.getStrByREX(infoStr, "【出版项】[^\\\\]+?【");
//			String isbn = tools.getStrByREX(infoStr, "【ISBN号】.+?【");
//			String yesh = tools.getStrByREX(infoStr, "【形态项】.+?【");
			////////
			//获取书名
			String bookName=tools.getStrByREX(html,"<input type=\"hidden\" name=\"title\"[^<>]*/>");
			if(bookName!=null&&!bookName.trim().equals(""))
			{
				bookName=bookName.replace("<input","<a")+"</a>";
				bookName=tools.getLinkTagFromLinkStr(bookName,link).getAttribute("value").trim();
				aboutBook.setName(bookName.trim());
			}
			//获取作者,译者
			String zuozhe=tools.getStrByREX(infoStr, "【作　者】.+?【");
			if(zuozhe!=null&&!zuozhe.trim().equals(""))
			{
				zuozhe=zuozhe.replaceAll("【作　者】","").replace("【","").trim();
				aboutBook.setZuozhe(zuozhe.trim());
			}
			//获取isbn
			String isbn=tools.getStrByREX(infoStr, "【ISBN号】.+?$");
			if(isbn!=null&&!isbn.trim().equals(""))
			{
				isbn=isbn.replaceAll("【ISBN号】","").replace("【","").replace("-","");
				aboutBook.setIsbn(isbn.trim());
			}
			//获取出版社
			String chubanshe=tools.getStrByREX(infoStr, "【出版项】.+?【");
			if(chubanshe!=null&&!chubanshe.trim().equals(""))
			{
				chubanshe=chubanshe.replaceAll("【出版项】","").replace("【", "");
				aboutBook.setChubanshe(chubanshe.trim());
			}
			//获取页数
			String yeshu=tools.getStrByREX(infoStr, "【形态项】.+?【");
			if(yeshu!=null&&!yeshu.trim().equals(""))
			{
				yeshu=yeshu.replaceAll("【形态项】","").replace("【", "");
				aboutBook.setYeshu(yeshu.trim());
			}
			//获取出版年
//			String chubannian=tools.getStrByREX(html,"<p>出版时间.+?</p>");
//			if(chubannian!=null&&!chubannian.trim().equals(""))
//			{
//				chubannian=chubannian.replaceAll("出版时间","").replace(":","").replace("：","");
//				chubannian=tools.Html2Text(chubannian);
//				aboutBook.setChubannian(chubannian.trim());
//			}
			if(jianjie!=null&&!jianjie.trim().equals(""))
			{
				HsgTools hsgTools = new HsgTools();
				jianjie = hsgTools.jianjiechuli(jianjie);
				aboutBook.setJianjie(tools.Html2Text(jianjie));
			}
			if(img!=null&&!img.trim().equals(""))
			{
				aboutBook.setImgSrc(tools.getSrcFromImgtag(img,link));
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
		System.out.println("########dou zhusan跳出");
		this.end.countDown();
	}
	public FromZhusanjiao(ArrayList<AboutBook> abs,String bookname,CountDownLatch end1,int resultNum)
	{
//		this.begin=begin1;
		this.end=end1;
		this.alArrayList=abs;
		this.bookName=bookname;
		this.resultNum=resultNum;
	}
}
