package net.aboutbooks.test;

import java.util.ArrayList;
import java.util.Iterator;

import net.aboutbooks.gethtmlsource.FromDangdang;
import net.aboutbooks.gethtmlsource.FromDouban;
import net.aboutbooks.gethtmlsource.FromZhusanjiao;
import net.aboutbooks.pipei.Pipei;
import net.aboutbooks.pipei.PipeiThrad;
import net.aboutbooks.pojo.AboutBook;

public class Main1 {

	/**
	 * @param args
	 */
	public static void main(String[] args) 
	{
		PipeiThrad pp=new PipeiThrad();
		pp.ZHUSANJIAO=false;  //关闭珠三角查询  。还能关闭当当、豆瓣，想关闭那个就讲相应的设为false
//		pp.DOUBAN=false;
		pp.DANGDANG=false;
		AboutBook ab=new AboutBook();  //先定义一个aboutbook，里边放上你们抓取的信息。想对那个评分，就将那个设置上。
		ab.setName("残水浒");  //设置了name（书名），也就会对书名进行评分。
//		ab.setChubanshe("华文出版社");    //这句话注释掉了，也就是说没有对ab设置出版社，就不会对出版社进行平分了，否则会对出版社评分。
//		ab.setZuozhe("朱自清");       //其他属性也是这样。
//		ab.setIsbn("20667638");
//		ArrayList<AboutBook> abbs=pp.comparByXSDList(ab);
		System.out.println("最佳结果");
//		for (Iterator iterator = abbs.iterator(); iterator.hasNext();) {
//			AboutBook abb = (AboutBook) iterator.next();
//			if(abb!=null)
//				System.out.println(abb.fenshu+":"+abb.website+":"+abb.getName()+"\n"+abb.getJianjie());
//		}
		AboutBook abb = pp.comparByXSD(ab);
		System.out.println("内容简介￥￥￥￥￥￥￥￥￥￥￥￥￥￥："+abb.getJianjie());
		
	}
	
}
