package net.books.Souhu;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

import org.htmlparser.tags.LinkTag;
import org.nutz.dao.Chain;
import org.nutz.dao.Cnd;
import org.nutz.dao.Condition;
import org.nutz.dao.util.cri.Static;

import antlr.collections.List;
import bsh.This;

import com.forfuture.tools.Tools;

import net.aboutbooks.pipei.PipeiThrad;
import net.aboutbooks.pojo.AboutBook;
import net.books.pojo.Books;
import net.books.pojo.BooksZT;
import net.books.pojo.Books_Info;
import net.books.words.ExportWord;
import net.hsg.tools.*;
import net.rile.sql.DataBaseDao;

public class shubenword_Copy1 
{
	/**
	 * @param args
	 */
	private static ExportWord ew = new ExportWord();
	public static Tools tools = new Tools();
	public static void main(String[] args) throws Exception {
		DataBaseDao dbd=new DataBaseDao();
//		java.util.List<Books_Info> booklist= dbd.query(Books_Info.class, Cnd.where("进度","<>", "待整理").and("进度", "<>", "完成").and("进度",">", 0.5), null);  //无条件全部遍历一边
//		java.util.List<Books_Info> booklist= dbd.query(Books_Info.class, Cnd.where("id", ">", 1700).and("id", "<", 1928).and("进度","=","完成"), null);
		java.util.List<Books_Info> booklist= dbd.query(Books_Info.class, Cnd.where("id", "=", 1857), null);
		System.out.println("剩余未下的图书本数："+booklist.size());
		int t=0;
		String html=null;
		String jjie=null;
		while ( t<=booklist.size()) {
			ew.clean();
			ew.id = booklist.get(t).getId();
			System.out.println("&&&&&&&&&"+ew.id);
			ew.shuming = booklist.get(t).getBookname().replaceAll("((《)|(》)|(\\s+))", "");	//图书网作者
			ew.url = booklist.get(t).getUrl();			//图书网路径
			ew.fengmian = booklist.get(t).getFengmianUrl();	//图书网封面路径
			if(!booklist.get(t).getAuthor().contains("&#65533;"))
			{
				ew.zuozhe = booklist.get(t).getAuthor();		//图书网作者
			}else {
				ew.zuozhe = booklist.get(t).getYYauthor();		//图书网作者
			}
			
			if(booklist.get(t).getPagesum()!=0)
			ew.yeshu = ""+booklist.get(t).getPagesum();
			String press = booklist.get(t).getYYchubanxiang();	//已整理后的出版项为主，尽量将出版项的精确信息整理到数据表的"YY出版项"字段里
			if(press==null||press.trim().equals(""))
			{
				ew.chubanxiang = booklist.get(t).getChubanxiang().replace("·", "");
			}
			else if(press.replaceAll("\\d{1,}", "").length()<3)
			{
				ew.chubanxiang = booklist.get(t).getChubanxiang().replace("·", "")+press;
			}
			else
			{
				ew.chubanxiang = press.replace("·", "");
			}
			ew.isbn = booklist.get(t).getISBN();				//ISBN号
			ew.banbenshuoming =booklist.get(t).getImprint();	//版本说明
			String fileName = ew.shuming.replaceAll("((\\\\)|(/)|(:)|(\")|(<)|(>)|(\\*)|(\\?)|(\\|))", "");
			try {
				html =tools.getContentOld(ew.url,"gbk");
			} catch (Exception e) {
				System.out.println("获取不到的任何内容："+ew.url);
			}
			ew.fileroot = "e:/下载书_测试/"+fileName+".doc";
			//添加图书简介
			jjie=tools.getStrByREX(html, "<p[^>]+info1[^>]+>.+?</p>");
			if (jjie!=null && !jjie.trim().equals("")){
				jjie=jjie.replace("&nbsp;", "").replaceAll("<br>\\s*…{1,}", "");
				jjie=tools.Html2Text(jjie.replaceAll("(\\s*((<BR>)|(<br\\s*/>))\\s*){1,}", "\n")).replace("&#65533;", "").replace("&#8226;", "·").trim();
				jjie=jjie.replaceAll("((■)|(□)|(▲)|(△)|(▽)|(▼)|(●)|(○)|(☆)|(★)|(\\*)|(&#8226;)|(◇)|(◆)|(>{2,}))", "").trim();
				if(jjie.length()>=200){
					ew.neirongtiyao = jjie.substring(2,190)+"...";
				}else{
					ew.neirongtiyao = jjie.substring(2, jjie.length());
				}
			}
			 //*/
	 			ew.addHeader();//将上边定义的头部信息添加到word中
	        //  开始增加正文和章节
			ArrayList<String> parts = tools.getByREX(html, "<div class=\"boxI\">.+?<ul class=\"clear\">.+?</div>"); //匹配部得到parts集合
			boolean bookbreak = false;
			for (int i = 0; i < parts.size(); i++) {
				String partName = tools.getStrByREX(parts.get(i), "<h2>.+?</h2>").replaceAll("((&#8231;)|(&#8226;))", "·").replace("&nbsp;", "");
				partName = tools.Html2Text(partName).trim();
				if(partName.contains("章")&&partName.contains("第"))
				{
					bookbreak = true;
					dbd.update(Books_Info.class, Chain.make("进度", "章章章"), Cnd.where("id", "=", booklist.get(t).getId()));
					break;
				}
				ArrayList<String> al = tools.getByREX(parts.get(i), "<a\\s+target[^>]+>.+?</a>");//匹配得到zhc的集合
					if(rexName(al))
						continue;
					ew.addParse(partName);
				String zhcRex = "";
				String chapter_info="";
				boolean breakTrue = false;
				String Ztvalue = "";
				for(int j=0;j<al.size();j++)
				{
							LinkTag lt = tools.getLinkTagFromLinkStr(al.get(j), ew.url);
							String jieUrl = lt.getLink();
							String zhcname = "";
								String zhcHtml="";
								try
								{
									zhcHtml = tools.getContentOld(jieUrl,"gbk");
								}
								catch (Exception e)
								{
									zhcHtml = tools.getContentOld(jieUrl,"gbk");
									System.out.println("第二次源码获取："+jieUrl);
									if (zhcHtml==null||zhcHtml.trim().equals(""))
									{
										System.out.println("第二次源码获取失败！！！！"+jieUrl);
									}
								}
								zhcname = tools.getStrByREX(zhcHtml, "<h1>.+?</h1>");
								zhcname = tools.Html2Text(zhcname);
								if(zhcname==null){
									System.err.println("获取到的章节为空:"+jieUrl);
									Ztvalue = "章节名为空终止";
									breakTrue = true;
									break;
								}
								String zhcNam = zhcname;
								zhcname = zhcname.replaceAll("\\(.+", "").replaceAll("（.+", "").trim();
								if (zhcname.contains("宣传")||zhcname.contains("广告")||zhcname.contains("专家")||zhcname.contains("编辑")
										||zhcname.contains("名家")||zhcname.contains("读者")||zhcname.contains("本书")||zhcname.contains("媒体")
										||zhcname.contains("买点")||zhcname.contains("卖点")||zhcname.contains("作者")||zhcname.contains("推荐")
										||zhcname.contains("目录")||zhcname.contains("插图")||zhcNam.contains("图）")
										||zhcname.contains("内容")||zhcname.contains("简介")||zhcname.contains("介绍")||zhcname.contains("编者"))
								{
									if (j==al.size()-1) {    	//完成了每部的最后一张内容的添加
										ew.addText(chapter_info.substring(1));
									}
									continue;
								}
								//根据获取到的章名的雷同程度，判断是否为同一章，并合并在一章节中
								if (zhcname.contains(zhcRex)) {     
									if (zhcRex.equals("")) {	//加载每一章的章名并确保只加载一次且位置正确不漏下
										ew.addZhang(zhcname);
									}
								String chapter=tools.getStrByREX(zhcHtml, "</h4>.+?</div>");
								try {
									chapter = chapter.replace("&nbsp;"," ").replace("&amp;","&")
											.replaceAll("((&#8198;)|(&#61482;)|(&#8202)|(&#9673;))", "")
											.replaceAll("(&amp;#8226;)|(&#8226;)|(&#8231;)", "·").replace("&#12316;", "~")
											.replace("&#8943;", "⋯").replace("&oslash;", "ø").replace("版权声明", "")
											.replaceAll("((<br>)|(<BR>))\\s*…{1,}", "").replaceAll("（[^（）]+?http[^（）]+?）", "");
									chapter = chapter.replaceAll("(\\s*((<br>)|(BR)|(<br\\s*/>)|(</h4>))\\s*){1,}", "\n    ");
									chapter = tools.Html2Text(chapter).replace("&lt;", "<").replace("&gt;", ">").replace("\n    查看原图", "");
									chapter = chapter.replaceAll("((◉)|(☉)|(⊙)|(◎)|(＊)|(￢)|(¬)|(◤)|(◥)|(◢)|(◣)|(░)|(▒)|(▓)|(☍)|(☋)|(☌)|(↓)" +
											"|(↑)|(↔)|(®)|(©)|(※)|(●)|(■)|(□)|(☆)|(▽)|(▼)|(○)|(★)|(\\*)|(◇)|(◆)|(>{2,}))", "").trim();
									chapter_info += "\n    "+chapter;
									if (chapter_info.trim().equals(""))
									{
										System.err.println("获取到的章节为空:"+jieUrl);
										Ztvalue = "图片或章节内容为空终止";
										breakTrue = true;
										break;
									}
								} catch (Exception e) 
								{
									System.err.println("获取到的章节为空:"+jieUrl);
									breakTrue = true;
									break;
								}
								zhcRex = zhcname;			//下载完每小节并修改zhcRex的值
								if (j==al.size()-1) {    	//完成了每部的最后一张内容的添加
									if(!chapter_info.equals(""))
									ew.addText(chapter_info.substring(1));
								}
								System.out.println("下载中...."+j);
							}else {
								//根据章名的匹配失败后可完成对每章节内容的加载并确保完整性，不包裹每一部的最后一章内容的加载
								if(!chapter_info.equals(""))
								ew.addText(chapter_info.substring(1));	
								j--;
								zhcRex = "";
								chapter_info="";
							}
				}
				if (breakTrue)
				{
					ew.zt=Ztvalue;
					break;
				}else {
					ew.zt = "抢书部分";
				}
			}
			t++;
			if(bookbreak)
				continue;
			ew.close();
			System.out.println("id:"+booklist.get(t).getId()+"第"+t+"本：《"+ew.shuming+"》下载完成！！！");
		}
		System.out.println("完成所有下载的任务！！！！O(∩_∩)O哈！");
		}
	public static boolean rexName (ArrayList<String> al){
		boolean result=true;
		String []notName={"宣传","广告","专家","编辑","编者","介绍","名家","读者","本书","媒体","买点","作者","推荐","目录","内容","简介","插图","图）"}; 
		for(int n=0; n<al.size(); n++){
			String rexName = tools.getLinkTagFromLinkStr(al.get(n), ew.url).getAttribute("linktxt");
			rexName = tools.Html2Text(rexName).replaceAll("\\s+", "");
			for (int t = 0; t < notName.length; t++)
			{
				if(rexName.contains(notName[t])){
					result = true;
					break;
				}else {
					result = false;
				}
			}
			if(result==false)
			{
				System.out.println(" 这本书需要加第一部分的信息");
				break;
			}
		}
		return result;
	}
}
