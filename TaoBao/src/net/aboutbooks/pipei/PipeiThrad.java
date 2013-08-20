package net.aboutbooks.pipei;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.CountDownLatch;

import org.stringtemplate.v4.compiler.STParser.list_return;

import com.forfuture.GuiTools.start;
import com.forfuture.autoconfig.dto.newspaperdecr;

import net.aboutbooks.gethtmlsource.FromDangdang;
import net.aboutbooks.gethtmlsource.FromDouban;
import net.aboutbooks.gethtmlsource.FromZhusanjiao;
import net.aboutbooks.pojo.AboutBook;
import net.aboutbooks.xiangsidu.LD;

public class PipeiThrad 
{
	//一些配置
	public boolean ZHUSANJIAO=true;
	public boolean DANGDANG=true;
	public boolean DOUBAN=true;
	public int RESULT_NUM=2;
	//成员类
	private FromDangdang fromDangdang;
	private FromDouban fromDouban;
	private FromZhusanjiao fromZhusanjiao;
	private int threadNum=0;
	private LD ld=new LD();
	public PipeiThrad()
	{
		fromDangdang=new FromDangdang();
		fromDouban=new FromDouban();
		fromZhusanjiao=new FromZhusanjiao();
	}
	public AboutBook comparByXSD(AboutBook comBooks)
	{
		if(comBooks==null||comBooks.getName()==null)
			return null;
		ArrayList<AboutBook> abBooks=new ArrayList<AboutBook>();
		if(DANGDANG)
		{
			threadNum++;
		}
		if(DOUBAN)
		{
			threadNum++;
		}
		if(ZHUSANJIAO)
		{
			threadNum++;
		}
		//
		System.out.println("##########:"+threadNum);
		CountDownLatch countDownLatch=new CountDownLatch(threadNum);
		if(DANGDANG)
		{
			threadNum++;
			new FromDangdang(abBooks, comBooks.getName(), countDownLatch, 2).start();
		}
		if(DOUBAN)
		{
			threadNum++;
			new FromDouban(abBooks, comBooks.getName(), countDownLatch, 2).start();
		}
		if(ZHUSANJIAO)
		{
			threadNum++;
			new FromZhusanjiao(abBooks, comBooks.getName(), countDownLatch, 2).start();
		}
		try {
			countDownLatch.await();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		System.out.println("#####################返回结果："+abBooks.size());
		//
		if(comBooks.getName()!=null&&!comBooks.getName().trim().equals(""))
		{
			System.out.println("书名不为空，对比书名......");
			pfByName(comBooks, abBooks);
		}
		if(comBooks.getZuozhe()!=null&&!comBooks.getZuozhe().trim().equals(""))
		{
			System.out.println("作者不为空，对比书名......");
			pfByZuozhe(comBooks, abBooks);
		}
		if(comBooks.getIsbn()!=null&&!comBooks.getIsbn().trim().equals(""))
		{
			System.out.println("isbn不为空，对比书名......");
			pfByIsbn(comBooks, abBooks);
		}
		if(comBooks.getChubanshe()!=null&&!comBooks.getChubanshe().trim().equals(""))
		{
			System.out.println("出版社不为空，对比书名......");
			pfByChubanshe(comBooks, abBooks);
		}
		// 输出评分
		AboutBook max=null;
		double maxFenshu=0;
		for (Iterator iterator = abBooks.iterator(); iterator.hasNext();) 
		{
			AboutBook aboutBook = (AboutBook) iterator.next();
			if(aboutBook.fenshu>maxFenshu)
			{
				maxFenshu=aboutBook.fenshu;
				max=aboutBook;
			}
			System.out.println(aboutBook.fenshu+":"+aboutBook.website+":"+aboutBook.getName());
		}
		return max;
	}
	public ArrayList<AboutBook> comparByXSDList(AboutBook comBooks)
	{
		if(comBooks==null||comBooks.getName()==null)
			return null;
		ArrayList<AboutBook> abBooks=new ArrayList<AboutBook>();
		if(DANGDANG)
		{
			threadNum++;
		}
		if(DOUBAN)
		{
			threadNum++;
		}
		if(ZHUSANJIAO)
		{
			threadNum++;
		}
		//
		System.out.println("##########:"+threadNum);
		CountDownLatch countDownLatch=new CountDownLatch(threadNum);
		if(DANGDANG)
		{
			threadNum++;
			new FromDangdang(abBooks, comBooks.getName(), countDownLatch, 2).start();
		}
		if(DOUBAN)
		{
			threadNum++;
			new FromDouban(abBooks, comBooks.getName(), countDownLatch, 2).start();
		}
		if(ZHUSANJIAO)
		{
			threadNum++;
			new FromZhusanjiao(abBooks, comBooks.getName(), countDownLatch, 2).start();
		}
		try {
			countDownLatch.await();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		System.out.println("#####################返回结果："+abBooks.size());
		//
		if(comBooks.getName()!=null&&!comBooks.getName().trim().equals(""))
		{
			System.out.println("书名不为空，对比书名......");
			pfByName(comBooks, abBooks);
		}
		if(comBooks.getZuozhe()!=null&&!comBooks.getZuozhe().trim().equals(""))
		{
			System.out.println("作者不为空，对比书名......");
			pfByZuozhe(comBooks, abBooks);
		}
		if(comBooks.getIsbn()!=null&&!comBooks.getIsbn().trim().equals(""))
		{
			System.out.println("isbn不为空，对比书名......");
			pfByIsbn(comBooks, abBooks);
		}
		if(comBooks.getChubanshe()!=null&&!comBooks.getChubanshe().trim().equals(""))
		{
			System.out.println("出版社不为空，对比书名......");
			pfByChubanshe(comBooks, abBooks);
		}
		// 输出评分
//		AboutBook max=null;
//		double maxFenshu=0;
//		for (Iterator iterator = abBooks.iterator(); iterator.hasNext();) 
//		{
//			AboutBook aboutBook = (AboutBook) iterator.next();
//			if(aboutBook.fenshu>maxFenshu)
//			{
//				maxFenshu=aboutBook.fenshu;
//				max=aboutBook;
//			}
//			System.out.println(aboutBook.fenshu+":"+aboutBook.website+":"+aboutBook.getName());
//		}
		return abBooks;
	}
	/**
	 * 按照书名评分
	 * */
	private void pfByName(AboutBook comBooks,ArrayList<AboutBook> abBooks)
	{
		for (Iterator iterator = abBooks.iterator(); iterator.hasNext();) 
		{
			AboutBook aboutBook = (AboutBook) iterator.next();
			//#########################可以在此更改权重
			aboutBook.fenshu=aboutBook.fenshu+ld.compare(comBooks.getName(),aboutBook.getName());
		}	
	}
	/**
	 * 按照作者评分
	 * */
	private void pfByZuozhe(AboutBook comBooks,ArrayList<AboutBook> abBooks)
	{
		for (Iterator iterator = abBooks.iterator(); iterator.hasNext();) 
		{
			AboutBook aboutBook = (AboutBook) iterator.next();
			//#########################可以在此更改权重
			aboutBook.fenshu=aboutBook.fenshu+ld.compare(comBooks.getZuozhe(),aboutBook.getZuozhe());
		}	
	}
	/**
	 * 按照出版社评分
	 * */
	private void pfByChubanshe(AboutBook comBooks,ArrayList<AboutBook> abBooks)
	{
		for (Iterator iterator = abBooks.iterator(); iterator.hasNext();) 
		{
			AboutBook aboutBook = (AboutBook) iterator.next();
			//#########################可以在此更改权重
			aboutBook.fenshu=aboutBook.fenshu+ld.compare(comBooks.getChubanshe(),aboutBook.getChubanshe());
		}	
	}
	/**
	 * 按照isbn评分
	 * */
	private void pfByIsbn(AboutBook comBooks,ArrayList<AboutBook> abBooks)
	{
		for (Iterator iterator = abBooks.iterator(); iterator.hasNext();) 
		{
			AboutBook aboutBook = (AboutBook) iterator.next();
			//#########################可以在此更改权重
			double re=ld.compare(comBooks.getIsbn(),aboutBook.getIsbn());
			if(re>=1)
				re=re*100;
			aboutBook.fenshu=aboutBook.fenshu+re;
		}	
	}
}
