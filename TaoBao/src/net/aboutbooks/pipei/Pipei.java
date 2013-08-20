package net.aboutbooks.pipei;

import java.util.ArrayList;
import java.util.Iterator;

import org.stringtemplate.v4.compiler.STParser.list_return;

import com.forfuture.autoconfig.dto.newspaperdecr;

import net.aboutbooks.gethtmlsource.FromDangdang;
import net.aboutbooks.gethtmlsource.FromDouban;
import net.aboutbooks.gethtmlsource.FromZhusanjiao;
import net.aboutbooks.pojo.AboutBook;
import net.aboutbooks.xiangsidu.LD;

public class Pipei 
{
	//һЩ����
	public boolean ZHUSANJIAO=true;
	public boolean DANGDANG=true;
	public boolean DOUBAN=true;
	public int RESULT_NUM=2;
	//��Ա��
	private FromDangdang fromDangdang;
	private FromDouban fromDouban;
	private FromZhusanjiao fromZhusanjiao;
	private LD ld=new LD();
	public Pipei()
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
			ArrayList<AboutBook> dangdangs=fromDangdang.getBybookName(comBooks.getName(),RESULT_NUM);
			abBooks.addAll(dangdangs);
		}
		if(DOUBAN)
		{
			ArrayList<AboutBook> doubans=fromDouban.getBybookName(comBooks.getName(),RESULT_NUM);
			abBooks.addAll(doubans);
		}
		if(ZHUSANJIAO)
		{
			ArrayList<AboutBook> zhusanjiaos=fromZhusanjiao.getBybookName(comBooks.getName(),RESULT_NUM);
			abBooks.addAll(zhusanjiaos); 
		}
		if(comBooks.getName()!=null&&!comBooks.getName().trim().equals(""))
		{
			System.out.println("������Ϊ�գ��Ա�����......");
			pfByName(comBooks, abBooks);
		}
		if(comBooks.getZuozhe()!=null&&!comBooks.getZuozhe().trim().equals(""))
		{
			System.out.println("���߲�Ϊ�գ��Ա�����......");
			pfByZuozhe(comBooks, abBooks);
		}
		if(comBooks.getIsbn()!=null&&!comBooks.getIsbn().trim().equals(""))
		{
			System.out.println("isbn��Ϊ�գ��Ա�����......");
			pfByIsbn(comBooks, abBooks);
		}
		if(comBooks.getChubanshe()!=null&&!comBooks.getChubanshe().trim().equals(""))
		{
			System.out.println("�����粻Ϊ�գ��Ա�����......");
			pfByChubanshe(comBooks, abBooks);
		}
		// �������
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
	/**
	 * ������������
	 * */
	private void pfByName(AboutBook comBooks,ArrayList<AboutBook> abBooks)
	{
		for (Iterator iterator = abBooks.iterator(); iterator.hasNext();) 
		{
			AboutBook aboutBook = (AboutBook) iterator.next();
			//#########################�����ڴ˸���Ȩ��
			aboutBook.fenshu=aboutBook.fenshu+ld.compare(comBooks.getName(),aboutBook.getName());
		}	
	}
	/**
	 * ������������
	 * */
	private void pfByZuozhe(AboutBook comBooks,ArrayList<AboutBook> abBooks)
	{
		for (Iterator iterator = abBooks.iterator(); iterator.hasNext();) 
		{
			AboutBook aboutBook = (AboutBook) iterator.next();
			//#########################�����ڴ˸���Ȩ��
			aboutBook.fenshu=aboutBook.fenshu+ld.compare(comBooks.getZuozhe(),aboutBook.getZuozhe());
		}	
	}
	/**
	 * ���ճ���������
	 * */
	private void pfByChubanshe(AboutBook comBooks,ArrayList<AboutBook> abBooks)
	{
		for (Iterator iterator = abBooks.iterator(); iterator.hasNext();) 
		{
			AboutBook aboutBook = (AboutBook) iterator.next();
			//#########################�����ڴ˸���Ȩ��
			aboutBook.fenshu=aboutBook.fenshu+ld.compare(comBooks.getChubanshe(),aboutBook.getChubanshe());
		}	
	}
	/**
	 * ����isbn����
	 * */
	private void pfByIsbn(AboutBook comBooks,ArrayList<AboutBook> abBooks)
	{
		for (Iterator iterator = abBooks.iterator(); iterator.hasNext();) 
		{
			AboutBook aboutBook = (AboutBook) iterator.next();
			//#########################�����ڴ˸���Ȩ��
			double re=ld.compare(comBooks.getIsbn(),aboutBook.getIsbn());
			if(re>=1)
				re=re*100;
			aboutBook.fenshu=aboutBook.fenshu+re;
		}	
	}
}
