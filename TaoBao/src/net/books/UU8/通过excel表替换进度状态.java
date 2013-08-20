package net.books.UU8;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.List;

import org.nutz.dao.Chain;
import org.nutz.dao.Cnd;

import com.forfuture.autoconfig.dto.newspaperdecr;

import jxl.read.biff.BiffException;
import net.books.pojo.Books_Info;
import net.exexcel.readexcels.ReadExcel;
import net.rile.sql.DataBaseDao;


public class 通过excel表替换进度状态 {

	/**
	 * @param args
	 * @throws IOException 
	 * @throws BiffException 
	 * @throws ParseException 
	 */
	public static void main(String[] args) throws BiffException, IOException 
	{
		DataBaseDao dbd = new DataBaseDao();
		List<Books_Info> books_Infos = dbd.query(Books_Info.class,null, null);
		ReadExcel re=new ReadExcel("E:/dd.xls");
		int id = 0;
		for (int i = 0; i < 12; i++) 
		{
			//re.read(X,Y); 
			String str="11";
			String bookName = re.read(0, i);
			if(str.equals("11")){
//				id = Integer.parseInt(re.read(1, i));
				dbd.update(Books_Info.class, Chain.make("进度", "清2"), Cnd.where("书名","=",bookName));
				System.out.println(books_Infos.get(id).getBookname());
				id++;
			}
		}
	}
}
