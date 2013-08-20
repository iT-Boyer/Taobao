package net.aboutbooks.test;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;

import com.forfuture.tools.Tools;

import net.aboutbooks.gethtmlsource.FromZhusanjiao;
import net.aboutbooks.pojo.AboutBook;

public class main3 {

	/**
	 * @param args
	 * @throws IOException 
	 */
	public static void main(String[] args) throws IOException 
	{
//		FromZhusanjiao fz=new FromZhusanjiao();
//		ArrayList<AboutBook> ab=fz.getBybookName("java", 2);
//		for (Iterator iterator = ab.iterator(); iterator.hasNext();) {
//			AboutBook aboutBook = (AboutBook) iterator.next();
//			System.out.println(aboutBook.getImgSrc());
//		}
		Tools tools=new Tools();
		System.out.println(tools.getContent("http://books.gdlink.net.cn/views/specific/2929/bookDetail.jsp?dxNumber=000008030064&d=CB36CBF6B47C4990FAEBDA96951228AC&fenlei=1817040302"));
	}

}
