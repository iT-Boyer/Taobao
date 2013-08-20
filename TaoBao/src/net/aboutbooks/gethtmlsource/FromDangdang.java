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
			System.out.println("����������"+url+bookName);
			String html=tools.getContent(url+bookName);
			if(html.contains("&times;"))  //�ж��Ƿ��ǵ���û�����������顣���������ж��ӵĻ������Ͼ���û���ѵ��ˡ������ǡ���ȫ����Ʒ�£�û���ҵ�������������ô���͸ĳ����
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
			//��ȡ����
			if(bookName!=null&&!bookName.trim().equals(""))
			{
				bookName=tools.Html2Text(bookName);
				aboutBook.setName(bookName.trim());
			}
			//��ȡͼƬ
			if(img!=null&&!img.trim().equals(""))
			{
				img=tools.getStrByREX(img,"<img.+?>");
				img=tools.getSrcFromImgtag(img,link);
				aboutBook.setImgSrc(img);
			}
			//��ȡ���
			if(jianjie!=null&&!jianjie.trim().equals(""))
			{
				jianjie=tools.Html2Text(jianjie).trim();
				aboutBook.setJianjie(jianjie);
			}
			//��ȡ����,����
			String zuozhe=tools.getStrByREX(html,"<p>��\\s*��.+?</p>");
			if(zuozhe!=null&&!zuozhe.trim().equals(""))
			{
				zuozhe=zuozhe.replaceAll("��\\s*��","").replace(":","").replace("��","");
				zuozhe=tools.Html2Text(zuozhe);
				aboutBook.setZuozhe(zuozhe.trim());
			}
//			String yizhe=tools.getStrByREX(html,"����</span>.+?</a>");
//			if(yizhe!=null&&!yizhe.trim().equals(""))
//			{
////				yizhe=yizhe.replace("����","").replace(":","").replace("��","");
//				yizhe=tools.Html2Text(yizhe);
//				aboutBook.setZuozhe(aboutBook.getZuozhe()+" "+yizhe.trim());
//			}
			//��ȡisbn
			String isbn=tools.getStrByREX(html,"I\\s*S\\s*B\\s*N.+?</span>");
			if(isbn!=null&&!isbn.trim().equals(""))
			{
				isbn=isbn.replaceAll("I\\s*S\\s*B\\s*N","").replace(":","").replace("��","").replace("-","");
				isbn=tools.Html2Text(isbn);
				aboutBook.setIsbn(isbn.trim());
			}
			//��ȡ������
			String chubanshe=tools.getStrByREX(html,"��\\s*��\\s*��\\s*.+?<p>");
			if(chubanshe!=null&&!chubanshe.trim().equals(""))
			{
				chubanshe=chubanshe.replaceAll("��\\s+��\\s+��","").replace(":","").replace("��","");
				chubanshe=tools.Html2Text(chubanshe);
				aboutBook.setChubanshe(chubanshe.trim());
			}
			//��ȡҳ��
			String yeshu=tools.getStrByREX(html,"ҳ\\s*��.+?</span>");
			if(yeshu!=null&&!yeshu.trim().equals(""))
			{
				yeshu=yeshu.replaceAll("ҳ\\s*��","").replace(":","").replace("��","");
				yeshu=tools.Html2Text(yeshu);
				aboutBook.setYeshu(yeshu.trim());
			}
			//��ȡ������
			String chubannian=tools.getStrByREX(html,"<p>����ʱ��.+?</p>");
			if(chubannian!=null&&!chubannian.trim().equals(""))
			{
				chubannian=chubannian.replaceAll("����ʱ��","").replace(":","").replace("��","");
				chubannian=tools.Html2Text(chubannian);
				aboutBook.setChubannian(chubannian.trim());
				if(aboutBook.getChubanshe()!=null&&!aboutBook.getChubanshe().trim().equals(""))
				{
					aboutBook.setChubanshe(aboutBook.getChubanshe()+"��"+aboutBook.getChubannian());	
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
