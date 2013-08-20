package net.books.main;

import java.io.IOException;

import net.rile.sql.DataBase;
import net.rile.sql.DataBaseDao;

public class DataBasetest {

	/**
	 * @param args
	 * @throws IOException 
	 */
	public static void main(String[] args) throws IOException
	{
		DataBase db=new DataBase(); //这定义了一个操作数据库的类，这个类可以在任何地方定义，因为他在初始化的时候就从线程池中去了一个链接。
		//这是查询
		if(db.query("select * from stutent"))
		{
			while(db.next())
			{
				System.out.println(db.getString("studentName"));
			}
		}
		//这是执行一个sql
		db.executeInt("update ...");  //具体的例子以后你可以问我。
		//最后要关闭。
		db.close();
		//我们还有一个DataBaseDao类，具有简单的orm功能。这个类是继承了NutzDao，我把Nutzdao的说明文档发给你就知道了。
		
	}

}
