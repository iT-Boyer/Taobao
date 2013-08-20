package com.books.test;

import java.io.File;
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

public class 获取出版社2 {

	/**
	 * @param args
	 * @throws IOException 
	 */
	public static void main(String[] args) throws IOException 
	{
		DataBaseDao dbd = new DataBaseDao();
		dbd.create(Press.class, true);
		Press press = new Press();
		Tools tools =new Tools();
		String url = "http://www.cocobook.net/ziliao/chubansheminglu.asp";
		String html = "";
		html = tools.getContent(url, "gbk");
		ArrayList<String> alist = tools.getByREX(html, "<table[^>]+#D8E6EE[^>]*>.+?</table>");
		for (int i = 1; i < alist.size(); i++) {
			ArrayList<String> all = tools.getByREX(alist.get(i), "<td\\s+[^>]+#EDF8FF[^>]*>.+?</td>");
			System.out.println(all.size());
			for (int j = 0; j < all.size(); j++) {
				ArrayList<String> allink = tools.getByREX(all.get(j), "<a\\s+[^>]+chuban[^>]+>.+?</a>");
				String pre= "";
				String province="";
				pre = tools.Html2Text(allink.get(0));
					String prourl = tools.getLinkTagFromLinkStr(allink.get(1), url).getLink();
					System.out.println("获取出版社地址："+prourl);
					String hhh = tools.getContent(prourl);
					province = tools.getStrByREX(hhh, "((地)|(社))((&nbsp;)|(\\s*)){0,}((点)|(址))((：)|(:)).+?((<BR>)|(<\\s*br\\s*/\\s*>))");
					System.out.println(province);
					if (province!=null){
						province = tools.Html2Text(province).replace("\\s+", "").replaceAll("((地)|(社))((&nbsp;)|(\\s*)){0,}((点)|(址))((：)|(:))", "").replace(":", "").replace("：", "").trim();	
						press.setProvince(province);
					}
				press.setPress(pre);
				dbd.insert(press);
			}
		}
//		dbd.update(Press.class, Chain.make("省市","合肥市:"),Cnd.where("省市","=", "安徽:"));
//		  File f=new File("e:/图书/");
//          f.mkdirs();
//		FileWriter fw=new FileWriter("e:/图书/press.txt",false);
//		List<Press> pressList = dbd.query(Press.class,null,null);
//		for (int i = 0; i < pressList.size(); i++) {
////			fw.write("hm.put(\""+pressList.get(i).getPress()+"\", \""+pressList.get(i).getProvince()+"\");\r\n");
//			fw.write("hm.add(\""+pressList.get(i).getPress()+"\")\r\n");
//		}
//		fw.close();
		System.out.println("操作完成！！！！");
	}
	
}
