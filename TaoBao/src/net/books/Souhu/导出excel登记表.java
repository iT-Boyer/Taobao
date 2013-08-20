package net.books.Souhu;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.List;

import org.nutz.dao.Chain;
import org.nutz.dao.Cnd;

import com.forfuture.autoconfig.dto.newspaperdecr;

import jxl.read.biff.BiffException;
import jxl.write.WriteException;
import net.books.pojo.Books_Info;
import net.exexcel.excels.Excel;
import net.exexcel.readexcels.ReadExcel;
import net.rile.sql.DataBaseDao;


public class ����excel�ǼǱ� {

	/**
	 * @param args
	 * @throws IOException 
	 * @throws BiffException 
	 * @throws WriteException 
	 * @throws ParseException 
	 */
	public static void main(String[] args) throws BiffException, IOException, WriteException 
	{
		DataBaseDao dbd = new DataBaseDao();
		List<Books_Info> books_Infos = dbd.query(Books_Info.class,Cnd.where("����", "like", "%�Ǽ�%"), null);
		Excel e = new Excel("�ǼǼ�¼", "e:/�ǼǼ�¼.xls");
		String []title = {"����","id","����","YY����","����","YY����","Դ��վurl","YYԴ��վurl","������","YY������","ISBN","ҳ��","������Ҫ"};
		e.setTitles(title);
		for (int i = 0; i < books_Infos.size(); i++)
		{
			e.insert(0, i+2, books_Infos.get(i).getZt());
			e.insert(1, i+2, ""+books_Infos.get(i).getId());
			e.insert(2, i+2, books_Infos.get(i).getBookname());
			e.insert(3, i+2, books_Infos.get(i).getYYbookname());
			e.insert(4, i+2, books_Infos.get(i).getAuthor());
			e.insert(5, i+2, books_Infos.get(i).getYYauthor());
			e.insert(6, i+2, books_Infos.get(i).getUrl());
			e.insert(7, i+2, books_Infos.get(i).getYYurl());
			e.insert(8, i+2, books_Infos.get(i).getChubanxiang());
			e.insert(9, i+2, books_Infos.get(i).getYYchubanxiang());
			e.insert(10, i+2, books_Infos.get(i).getISBN());
			e.insert(11, i+2, ""+books_Infos.get(i).getPagesum());
			e.insert(12, i+2, books_Infos.get(i).getSummary());
		}
		e.write();
		System.out.println("�����ɹ�������");
	}
}
