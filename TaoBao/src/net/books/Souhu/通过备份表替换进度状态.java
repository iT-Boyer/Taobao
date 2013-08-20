package net.books.Souhu;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.List;

import org.nutz.dao.Chain;
import org.nutz.dao.Cnd;

import com.forfuture.autoconfig.dto.newspaperdecr;

import jxl.read.biff.BiffException;
import net.books.pojo.Books_Info;
import net.books.pojo.Books_Info33;
import net.exexcel.readexcels.ReadExcel;
import net.rile.sql.DataBaseDao;


public class 通过备份表替换进度状态 {

	/**
	 * @param args
	 * @throws IOException 
	 * @throws BiffException 
	 * @throws ParseException 
	 */
	public static void main(String[] args) throws BiffException, IOException 
	{
		DataBaseDao dbd = new DataBaseDao();
		List<Books_Info> books_Infos = dbd.query(Books_Info .class,Cnd.where("id", "<", 226), null);
		List<Books_Info33> books_Info33s= dbd.query(Books_Info33.class, Cnd.where("id", "<", 226), null);
		System.out.println(books_Info33s.size());
		for (int i = 0; i < books_Info33s.size(); i++) 
		{
			String str=books_Infos.get(i).getBookname();
			String str2 = books_Info33s.get(i).getBookname();
			if(str.equals(str2)){
				dbd.update(Books_Info.class, Chain.make("进度", books_Info33s.get(i).getZt()), Cnd.where("书名","=",str2));
				System.out.println(str+"&&&&&&&"+str2);
			}
		}
	}
}
