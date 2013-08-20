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

public class CopyOf获取出版社2 {

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
		String html = "";
		for (int i = 1; i < 564; i++) {
			String url = "http://www.cocobook.net/ziliao/chuban-"+i+".htm";
				String pre= "";
				String province="";
				String hhh = tools.getContent(url);
				pre = tools.getStrByREX(hhh, "<td\\s+[^>]+#0C62B8[^>]*><strong>.+?</strong>");
				pre = tools.Html2Text(pre);
				province = tools.getStrByREX(hhh, "((地)|(社))((&nbsp;)|(\\s*)){0,}((点)|(址))((：)|(:)).+?((<BR>)|(<\\s*br\\s*/\\s*>))");
				System.out.println(province);
				if (province==null)
					continue;
				province = tools.Html2Text(province).replace("\\s+", "").replaceAll("((地)|(社))((&nbsp;)|(\\s*)){0,}((点)|(址))((：)|(:))", "").replace(":", "").replace("：", "").trim();	
				press.setProvince(province);
				press.setPress(pre);
				dbd.insert(press);
			}
		System.out.println("操作完成！！！！");
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
	}