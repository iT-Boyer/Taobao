package net.aboutbooks.chubanshe;

import java.io.IOException;
import java.util.List;

import org.nutz.dao.Chain;
import org.nutz.dao.Cnd;
import org.nutz.el.opt.custom.Trim;
import org.stringtemplate.v4.compiler.STParser.notConditional_return;

import com.forfuture.autoconfig.dto.newspaperdecr;

import net.books.pojo.Books_Info;
import net.books.pojo.Press;
import net.rile.sql.DataBaseDao;


public class test {

	/**
	 * @param args
	 * @throws IOException 
	 */
	public static void main(String[] args) throws IOException 
	{
		Panduan pd=new Panduan();
//		DataBaseDao dbd = new DataBaseDao();
////		dbd.create(Press.class, true);
////		Press press = new Press();
//		List<Books_Info> presList = dbd.query(Books_Info.class,Cnd.where("YY������", "not is", null).and("YY������", "<>", ""), null);
//		System.out.println(presList.size());
//		int t = 0;
//		for (int i = 0; i < presList.size(); i++)
//		{
//			String pressold = presList.get(i).getYYchubanxiang();
//			if(pressold.replaceAll("\\d{1,}", "").trim().length()<4){
//				pressold = presList.get(i).getChubanxiang()+pressold;
//				System.out.println(pressold);
//				t++;
//			}
//			String pressnew="";
//			if(!pressold.contains("��:")&&!pressold.contains("�У�")){
//				pressnew = pd.panduan(pressold)+pressold;
//			}
//			dbd.update(Books_Info.class, Chain.make("YY������", pressnew), Cnd.where("YY������", "=", presList.get(i).getYYchubanxiang()));
////			press.setPress(pressold);
////			press.setProvince(pressnew);
////			dbd.insert(press);
//		}
//		System.out.println(t+"��ɣ�������");
		System.out.println(pd.panduan("���������"));
	}
}
