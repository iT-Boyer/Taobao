package net.books.UU8;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

import org.htmlparser.tags.LinkTag;
import org.nutz.dao.Chain;
import org.nutz.dao.Cnd;
import org.nutz.dao.Condition;
import org.nutz.dao.util.cri.Static;
import org.springframework.asm.commons.EmptyVisitor;

import antlr.collections.List;
import bsh.This;

import com.forfuture.autoconfig.dto.newspaperdecr;
import com.forfuture.tools.Tools;

import net.aboutbooks.pipei.PipeiThrad;
import net.aboutbooks.pojo.AboutBook;
import net.books.pojo.Books;
import net.books.pojo.BooksZT;
import net.books.pojo.Books_Info;
import net.books.words.ExportWord;
import net.hsg.tools.*;
import net.rile.sql.DataBaseDao;

public class CopyOfshubenword_Copy1 
{
	/**
	 * @param args
	 */
	private static ExportWord ew = new ExportWord();
	public static Tools tools = new Tools();
	public static HsgTools hsgTools = new HsgTools();
	public static void main(String[] args) throws Exception {
		DataBaseDao dbd=new DataBaseDao();
//		java.util.List<Books_Info> booklist= dbd.query(Books_Info.class, Cnd.where("进度", ">", 0.5), null);
		java.util.List<Books_Info> booklist= dbd.query(Books_Info.class, Cnd.where("id", "=", 7), null);
		System.out.println("剩余未下的图书本数："+booklist.size());
		int t=0;
		String html=null;
		while ( t<booklist.size()) {
	    boolean breakTrue = false;
		String Ztvalue = "";
			ew.clean();
			ew.id = booklist.get(t).getId();
			System.out.println("&&&&&&&&&"+ew.id);
			ew.shuming = booklist.get(t).getBookname().replaceAll("((《)|(》)|(\\s+))", "");	//图书网作者
			ew.url = booklist.get(t).getUrl();			//图书网路径
			if(booklist.get(t).getAuthor()!=null)
			{
				ew.zuozhe = booklist.get(t).getAuthor();		//图书网作者
			}else {
				ew.zuozhe = booklist.get(t).getYYauthor();		//图书网作者
			}
			ew.yeshu = ""+booklist.get(t).getPagesum();
			ew.chubanxiang = booklist.get(t).getYYchubanxiang();	//已整理后的出版项为主，尽量将出版项的精确信息整理到数据表的"YY出版项"字段里
			ew.isbn = booklist.get(t).getISBN();				//ISBN号
			ew.banbenshuoming ="";					//版本说明
			ew.neirongtiyao = booklist.get(t).getSummary();   //添加图书简介
			String fileName = ew.shuming.replaceAll("((\\\\)|(/)|(:)|(\")|(<)|(>)|(\\*)|(\\?)|(\\|))", "");
			try {
				html =tools.getContentOld(ew.url,"gbk");
			} catch (Exception e) {
				System.out.println("获取不到的任何内容："+ew.url);
				t++;
				continue;
			}
			ew.fileroot = "e:/UUs8/"+fileName+".doc";
			String fenmian = tools.getStrByREX(html, "<img[^>]+cover[^>]+>");
			fenmian = tools.getSrcFromImgtag(fenmian, ew.url);
			if(fenmian!=null&&!fenmian.trim().equals("")){
				ew.fengmian = ew.url+"/"+tools.getStrByREX(fenmian, "cover\\..+");	//图书网封面路径
			  }else{
				System.out.println("原封面图片加载失败："+fenmian);
				String tupurl = booklist.get(t).getFengmianUrl();
				if(tupurl!=null&&!tupurl.trim().equals("")){
					ew.fengmian = tupurl;
				}else {
					System.err.println("数据库中封面图片加载失败:"+ew.shuming);
					Ztvalue = "封面图片不存在！";
					t++;
					continue;
					}
			  }
 			ew.addHeader();//将上边定义的头部信息添加到word中
	        //  开始增加正文和章节
 			String htmlstr = tools.getStrByREX(html, "<div id=\"youli2\">.+?</div>");
			ArrayList<String> al = tools.getByREX(htmlstr, "<a\\s+[^>]+>.+?</a>");//匹配得到zhc的集合
			System.out.println(al.size());
			String zhcRex = "";
			String chapter_info = "";
			for(int j=0;j<al.size();j++)
			{
				LinkTag lt = tools.getLinkTagFromLinkStr(al.get(j), ew.url);
				String jieUrl = ew.url+"/"+tools.getStrByREX(lt.getLink(), "\\d{1,}\\.htm");
				String zhcname = lt.getAttribute("linktxt").trim();
				String zhc = zhcname.replaceAll("\\s+", "").replaceAll("（.+", "");
				if (hsgTools.zhcnameRex(zhc))
				{
					if(zhc.contains("简介")||zhc.contains("提要")) {
						ew.neirongtiyao = booklist.get(t).getSummary();
						ew.addHeader();
						continue;
						}
				}
				if(zhc.startsWith("卷")&&!zhc.contains("卷结")){
					ew.addParse(zhcname);
				}else if(zhc.startsWith("第")&&zhc.contains("章"))
				{
					ew.addZhang(zhcname);
				}else if(zhc.contains("前言")||zhc.contains("序")
						||zhc.contains("跋")||zhc.contains("引言")
						||zhc.contains("导言")||zhc.contains("后记")
						||zhc.contains("编后")||zhc.contains("编后")){
					ew.addZhang(zhcname);
				}else{
					ew.addJie(zhcname);
				}
				String zhcHtml="";
				try{
					zhcHtml = tools.getContentOld(jieUrl,"gbk");
				   }catch (Exception e){
					zhcHtml = tools.getContentOld(jieUrl,"gbk");
					System.out.println("第二次源码获取："+jieUrl);
					if (zhcHtml==null||zhcHtml.trim().equals("")){
						System.out.println("第二次源码获取失败！！！！"+jieUrl);
						breakTrue = true;
						Ztvalue = "章节缺下";
						break;
						}
				   }
				//根据获取到的章名的雷同程度，判断是否为同一章，并合并在一章节中
				if (zhcname.contains(zhcRex)){     
					if (zhcRex.equals("")) 	//加载每一章的章名并确保只加载一次且位置正确不漏下
						ew.addZhang(zhcname);
				String chapter=tools.getStrByREX(zhcHtml, "<p id=\"zoom\">.+?((<font color=\"#ffffff\">)|(</p>))");
				chapter = hsgTools.overtxt(chapter);
				chapter_info +="\n"+chapter;
				if(chapter.trim().equals(""))
					{
						System.err.println("获取到的章节为空:"+jieUrl);
						Ztvalue = "图片或章节内容为空终止";
						breakTrue = true;
						break;
					}
				zhcRex = zhc;			//下载完每小节并修改zhcRex的值
				if (j==al.size()-1) {    	//完成了每部的最后一张内容的添加
					ew.addText(chapter_info);
				}
				System.out.println("下载中...."+j);
			}else {
				//根据章名的匹配失败后可完成对每章节内容的加载并确保完整性，不包裹每一部的最后一章内容的加载
				if(!chapter_info.equals(""))
				ew.addText(chapter_info);	
				j--;
				zhcRex = "";
				chapter_info="";
			}
			}
			if (breakTrue)
				{
				ew.zt=Ztvalue;
				}else {
				ew.zt = "抢书部分";
				}
		System.out.println("id:"+booklist.get(t).getId()+"第"+t+"本：《"+ew.shuming+"》下载完成！！！");
		t++;
		ew.close();
	 }
		System.out.println("完成所有下载的任务！！！！O(∩_∩)O哈！");
  }
}
