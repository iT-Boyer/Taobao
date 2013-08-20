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
import net.exexcel.readexcels.ReadExcel;
import net.rile.sql.DataBaseDao;


public class ͨ��excel���滻����״̬ {

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
		ReadExcel re=new ReadExcel("E:/tbbooks_info.xls");
		for (int i = 1; i < 1800; i++) 
		{
			//re.read(X,Y); x�ǵڼ��У���0��ʼ��y�ǵڼ��У�Ҳ�Ǵ�0��ʼ��
			String str=re.read(0, i);
			int id = 0;
			if(str.equals("���")){
				id = Integer.parseInt(re.read(1, i));
				dbd.update(Books_Info.class, Chain.make("����", "@@"), Cnd.where("id","=",id));
				System.out.println(books_Infos.get(id).getZt());
			}
		}
	}
}
