package net.books.main;

import java.io.IOException;


import net.books.pojo.Books;
import net.books.pojo.BooksZT;
import net.rile.sql.DataBaseDao;
//import net.rile2.

public class creatTable
{

	/**
	 * @param args
	 * @throws IOException 
	 */
	public static void main(String[] args) throws IOException
	{
		DataBaseDao dd=new DataBaseDao();
		dd.create(Books.class, true);
//		dd.create(BooksZT.class,true);
//		java.util.List<BooksZT> booklist= dd.query(BooksZT.class, null, null);
//		int t=0;	
//		while (t<=booklist.size()) {
//			System.out.println(booklist.get(3).getState());
//			t++;
//			}
//		System.out.println(t);
		System.out.println("数据库表生成完成！！");
	}
}
