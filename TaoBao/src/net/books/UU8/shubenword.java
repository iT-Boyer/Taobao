package net.books.UU8;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

import org.htmlparser.tags.LinkTag;
import org.nutz.dao.Cnd;

import antlr.collections.List;

import com.forfuture.tools.Tools;

import net.aboutbooks.pipei.PipeiThrad;
import net.aboutbooks.pojo.AboutBook;
import net.books.pojo.Books;
import net.books.pojo.BooksZT;
import net.books.words.ExportWord;
import net.hsg.tools.*;
import net.rile.sql.DataBaseDao;

public class shubenword 
{
	/**
	 * @param args
	 */
	private static ExportWord ew = new ExportWord();
	public static void main(String[] args) throws Exception {
//		System.setProperty("webdriver.firefox.bin","D:/Program Files/Mozilla Firefox/firefox.exe");
		//获取网页源码
		Tools tools = new Tools();
//		HsgTools hsgTools = new HsgTools();
		DataBaseDao dbd=new DataBaseDao();
		java.util.List<BooksZT> booklist= dbd.query(BooksZT.class, null, null);  //无条件全部遍历一边
		dbd.create(Books.class, true);
//		java.util.List<BooksZT> booklist= dbd.query(BooksZT.class, Cnd.where("State","=", "No!!!"), null);//根据State字段条件限制下载未下载的图书
//		java.util.List<BooksZT> booklist= dbd.query(BooksZT.class, Cnd.where("id",">", 775), null);//根据id指定下载条件限制下载未下载的图书
		System.out.println("剩余未下的图书本数："+booklist.size());
		int t=0;
		String bookName=null;
		String bookUrl=null;
		String imgUrl=null;
		String author=null;
		String press=null;
		String html=null;
		String jjie=null;
		String key=null;
		while ( t<=booklist.size()) {
			ew.clean();
			bookName = booklist.get(t).getBookName();
			bookUrl = booklist.get(t).getBookURL();
			imgUrl = booklist.get(t).getImgURL();
			author = booklist.get(t).getAuthor();
			press = booklist.get(t).getPress();
			try {
				System.out.println("当前的图书名："+bookName);
				html =tools.getContentOld(bookUrl,"gbk");
			} catch (Exception e) {
				System.out.println("获取不到的任何内容："+bookUrl);
			}
			key = tools.getStrByREX(html, "<em>\\s*连\\s*载.+?</em>");
			key = tools.Html2Text(key).replaceAll("\\s+", "").trim();
			if (!key.equals("连载完成")){
				System.err.println(bookName+"本书未连载完成！！！跳过！！！");
				t++;
				continue;
			}
			 String fileName = bookName.replaceAll("((\\\\)|(/)|(:)|(\")|(<)|(>)|(\\*)|(\\?)|(\\|))", "");
			ew.fileroot = "e:/下载书/"+fileName+".doc";
			ew.banbenshuoming = "全书";
			//添加图书名
			ew.shuming=bookName;
//			//添加作者
//			ew.zuozhe=author;
//			//添加出版项
//			ew.chubanxiang=press;  //注释后从其他网站上获取出版项的详细信息
			//添加图书简介
			jjie=tools.getStrByREX(html, "<p[^>]+info1[^>]+>.+?</p>");
			if (jjie!=null && !jjie.trim().equals("")){
				jjie=tools.Html2Text(jjie.replaceAll("(\\s*((<BR>)|(<br\\s*/>))\\s*){1,}", "\n")).replace("&#8226;", "·").trim();
				if(jjie.length()>=200){
					ew.neirongtiyao = jjie.substring(2,190)+"...";
				}else{
					ew.neirongtiyao = jjie.substring(2, jjie.length());
				}
			}
			/** 霍曙光的搜索执行中
			String zz = author;
			if(!hsgTools.zhjsk( bookName, zz,ew,fw))
				if (!hsgTools.dbwang(bookName,zz,ew,fw)) 
					hsgTools.dbOne(bookName,zz,fw,ew);**/
			//	赵培的搜索搁置了
				PipeiThrad pp=new PipeiThrad();
				pp.DOUBAN=false;  //关闭豆瓣搜索
//				pp.ZHUSANJIAO=false;
				AboutBook ab=new AboutBook(); //提供原始数据
				ab.setName(bookName);
				ab.setChubanshe(press);
				ab.setZuozhe(author);
				AboutBook abb=pp.comparByXSD(ab);  //通过匹配引擎返回结果集
//				ArrayList<AboutBook> abb=pp.comparByXSDList(ab);
				if(abb!=null){
					if (abb.getIsbn()!=null) 
						ew.isbn = abb.getIsbn();	
					ew.zt = ""+abb.fenshu;
		 			ew.yeshu=tools.getStrByREX(abb.getYeshu(), "\\d{1,}");
		 			if (abb.getChubanshe()!= null){
		 				ew.chubanxiang = abb.getChubanshe();
		 			}else if(abb.getChubanshe().replaceAll("\\d{1,}", "").length()<3){
						ew.chubanxiang = press + abb.getChubanshe();
					}else {
						ew.chubanxiang = press;
					}
		 			if (abb.getZuozhe()!=null) {
						ew.zuozhe = abb.getZuozhe();
					}else {
						ew.zuozhe = author;
					}
				}else {
					ew.chubanxiang = press;
					ew.zuozhe = author;
					ew.zt = "无结果！！！";
				}
			 //*/
	 			ew.fengmian=imgUrl;//当所抓的图书网中有图书封面
	 			ew.url=bookUrl;		
	 			ew.addHeader();//将上边定义的头部信息添加到word中
	        //  开始增加正文和章节
			ArrayList<String> parts = tools.getByREX(html, "<div class=\"boxI\">.+?</div>"); //匹配部得到parts集合
			for (int i = 0; i < parts.size(); i++) {
				String partName = tools.getStrByREX(parts.get(i), "<h2>.+?</h2>").replace("&nbsp;", "");
				partName = tools.Html2Text(partName).replace("章", "部").trim();
				ew.addParse(partName);
				ArrayList<String> al = tools.getByREX(parts.get(i), "<a\\s+target[^>]+>.+?</a>");//匹配得到zhc的集合
				String zhcRex = "";
				String chapter_info="";
				for(int j=0;j<al.size();j++)
				{
							LinkTag lt = tools.getLinkTagFromLinkStr(al.get(j), bookUrl);
							String jieUrl = lt.getLink();
							String zhcname = "";
								String zhcHtml="";
								try
								{
									zhcHtml = tools.getContentOld(jieUrl,"gbk");
								}
								catch (Exception e)
								{
									System.out.println("源码获取失败："+jieUrl);
								}
								zhcname = tools.getStrByREX(zhcHtml, "<h1>.+?</h1>");
								zhcname = zhcname.replaceAll("\\(.+?\\)", "").replaceAll("（.+?）", "").trim();
								if (zhcname.contains(zhcRex)) {     //根据获取到的章名的雷同程度，判断是否为同一章，并合并在一章节中
									if (zhcRex.equals("")) {	//加载每一章的章名并确保只加载一次且位置正确不漏下
										ew.addZhang(zhcname);
									}
								String chapter=tools.getStrByREX(zhcHtml, "</h4>.+?</div>");
								try {
									chapter = chapter.replaceAll("(\\s*((<br>)|(BR)|(<br\\s*/>)|(</h4>))\\s*){1,}", "\n");
									chapter = tools.Html2Text(chapter).replaceAll("((\\*)|(&nbsp;)|(&#8226;)|(◇)|(◆))", "").trim();
										chapter_info += "\n"+chapter;
								} catch (Exception e) 
								{
									ew.zt="终止下载";
									System.err.println("获取到的章节为空:"+jieUrl);
									break;
								}
								zhcRex = zhcname;			//下载完每小节并修改zhcRex的值
								if (j==al.size()-1) {    	//完成了每部的最后一张内容的添加
									ew.addText(chapter_info.substring(1));
								}
							}else {
								ew.addText(chapter_info.substring(1));	//根据章名的匹配失败后可完成对每章节内容的加载并确保完整性，不包裹每一部的最后一章内容的加载
								j--;
								zhcRex = "";
								chapter_info="";
							}
				}
			}
			ew.close();
			t++;
			System.out.println("第"+t+"本：《"+bookName+"》下载完成！！！");
		}
		System.out.println("完成所有下载的任务！！！！O(∩_∩)O哈！");
		}
}
