package com.books.test;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.htmlparser.tags.LinkTag;
import org.nutz.dao.Chain;
import org.nutz.dao.Cnd;
import org.springframework.dao.PessimisticLockingFailureException;

import com.forfuture.autoconfig.dto.newspaperdecr;
import com.forfuture.tools.Tools;

import net.aboutbooks.gethtmlsource.FromDangdang;
import net.aboutbooks.gethtmlsource.FromDouban;
import net.aboutbooks.gethtmlsource.FromZhusanjiao;
import net.aboutbooks.pipei.Pipei;
import net.aboutbooks.pipei.PipeiThrad;
import net.aboutbooks.pojo.AboutBook;
import net.books.pojo.Books_Info;
import net.books.pojo.Press;
import net.rile.sql.DataBaseDao;

public class 获取出版社 {

	/**
	 * @param args
	 * @throws IOException 
	 */
	public static void main(String[] args) throws IOException 
	{
		DataBaseDao dbd = new DataBaseDao();
//		dbd.create(Press.class, true);
//		Press press = new Press();
//		Tools tools =new Tools();
//		String url = "http://www.yidoc.net/list/guangdong.php";
//		String html = "";
//		html = tools.getContent(url, "gbk");
//		String hhh = tools.getStrByREX(html, "<td colspan=\"4\" rowspan=\"10\">.+?</table>");
//		ArrayList<String> alist = tools.getByREX(hhh, "<a[^>]+>.+?</a>");
//		for (int i = 0; i < alist.size(); i++) {
//			LinkTag lt = tools.getLinkTagFromLinkStr(alist.get(i), url);
//			String province = lt.getAttribute("linktxt");
//			String prourl = lt.getLink();
//			String prohtml = tools.getContent(prourl, "gbk");
//			ArrayList<String> all = tools.getByREX(prohtml, "<a\\s+[^>]+www[^>]+v2[^>]+>.+?</a>");
//			for (int j = 0; j < all.size(); j++) {
//				String pre= "";
//				pre = tools.Html2Text(all.get(j));
//				press.setProvince(province);
//				press.setPress(pre);
//				dbd.insert(press);
//			}
//		}
//		dbd.update(Press.class, Chain.make("省市","合肥市:"),Cnd.where("省市","=", "安徽:"));
		  File f=new File("e:/图书/");
          f.mkdirs();
		FileWriter fw=new FileWriter("e:/图书/press.txt",false);
		File ff = new File("d:/Chubanshe.java");
		FileReader fr = new FileReader(ff);
		char[] cs = new char[(int) ff.length()];
		fr.read(cs);
		String tuwenStr = new String(cs);
		List<Press> pressList = dbd.query(Press.class,null,null);
		System.out.println(pressList.size());
		int j=0;
		for (int i = 0; i < pressList.size(); i++) {
			if (tuwenStr.contains(pressList.get(i).getPress()))
				continue;
			fw.write("hm.put(\""+pressList.get(i).getPress()+"\", \""+pressList.get(i).getProvince()+"\");\r\n");
			j++;
//			fw.write("hm.add(\""+pressList.get(i).getPress()+"\")\r\n");
		}
		fw.close();
		System.out.println(j+"操作完成！！！！");
	}
	
}
