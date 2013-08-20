package net.books.main;

import java.io.IOException;

import net.rile.sql.DataBase;
import net.rile.test.dbMain;

public class 删除数据库重复的值
{
	/**
	 * @param args
	 * @throws IOException 
	 */
	public static void main(String[] args) throws IOException
	{
		DataBase dd=new DataBase();
		DataBase db=new DataBase();
		if(dd.query("select * from  tbbooks where id not in(select id from tbbooks group by 书名)"))
		{
			while(dd.next())
			{
				System.out.println(dd.getString("书名")+":"+dd.getString("id"));
				db.execute("delete from tbbooks where id="+dd.getString("id"));
			}
		}
		dd.close();
		db.close();
		
	}
}
