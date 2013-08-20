package net.books.Souhu;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

import org.htmlparser.tags.LinkTag;
import org.nutz.dao.Chain;
import org.nutz.dao.Cnd;

import com.forfuture.tools.Tools;

import net.books.pojo.Books_Info;
import net.books.words.ExportWord;
import net.rile.sql.DataBaseDao;

public class shubenword_Copy_NOChange 
{
	/**
	 * @param args
	 */
	private static ExportWord ew = new ExportWord();
	public static Tools tools = new Tools();
	public static  FileWriter fwtxt;
	public static void main(String[] args) throws Exception {
		DataBaseDao dbd=new DataBaseDao();
//		java.util.List<Books_Info> booklist= dbd.query(Books_Info.class, Cnd.where("进度","<>", "待整理").and("进度", "<>", "完成").and("进度",">", 0.5), null);  //无条件全部遍历一边
//		java.util.List<Books_Info> booklist= dbd.query(Books_Info.class, Cnd.where("id", ">", 1700).and("id", "<", 1928).and("进度","=","完成"), null);
		java.util.List<Books_Info> booklist= dbd.query(Books_Info.class, Cnd.where("id", "=", 1857), null);
		System.out.println("剩余未下的图书本数："+booklist.size());
		int t=0;
		String html=null;
		while ( t<=booklist.size()) {
			String bookName = booklist.get(t).getBookname();
			String bookUrl = booklist.get(t).getUrl();
			String fileName = bookName.replaceAll("((\\\\)|(/)|(:)|(\")|(<)|(>)|(\\*)|(\\?)|(\\|))", "");
			try {
				html =tools.getContentOld(bookUrl,"gbk");
			} catch (Exception e) {
				System.out.println("获取不到的任何内容："+bookUrl);
			}
			String fileroot = "e:/章次排查/";
			File f=new File(fileroot);
            f.mkdirs();
			fwtxt=new FileWriter("e:/章次排查/"+fileName+".txt"); 
	        //  开始增加正文和章节
			ArrayList<String> parts = tools.getByREX(html, "<div class=\"boxI\">.+?<ul class=\"clear\">.+?</div>"); //匹配部得到parts集合
			for (int i = 0; i < parts.size(); i++) {
				String partName = tools.getStrByREX(parts.get(i), "<h2>.+?</h2>").replaceAll("((&#8231;)|(&#8226;))", "·").replace("&nbsp;", "");
				partName = tools.Html2Text(partName).trim();
				fwtxt.write(partName+"\r\n");
				ArrayList<String> al = tools.getByREX(parts.get(i), "<a\\s+target[^>]+>.+?</a>");//匹配得到zhc的集合
				String zhcRex = "";
				for(int j=0;j<al.size();j++)
				{
							LinkTag lt = tools.getLinkTagFromLinkStr(al.get(j), bookUrl);
							String zhcname = lt.getAttribute("linktxt");
								zhcname = zhcname.replaceAll("\\(.+", "").replaceAll("（.+", "").trim();
								//根据获取到的章名的雷同程度，判断是否为同一章，并合并在一章节中
								if (zhcname.contains(zhcRex)) {     
									if (zhcRex.equals("")) {	//加载每一章的章名并确保只加载一次且位置正确不漏下
										fwtxt.write(zhcname+"\r\n");
									}
								zhcRex = zhcname;			//下载完每小节并修改zhcRex的值
								if (j==al.size()-1) {    	//完成了每部的最后一张内容的添加
									fwtxt.write(zhcname+"\r\n");
								}
								System.out.println("下载中...."+j);
							}else {
								//根据章名的匹配失败后可完成对每章节内容的加载并确保完整性，不包裹每一部的最后一章内容的加载
								j--;
								zhcRex = "";
							}
				}
			}
			t++;
			fwtxt.close();
			System.out.println("id:"+booklist.get(t).getId()+"第"+t+"本：《"+ew.shuming+"》下载完成！！！");
		}
		System.out.println("完成所有下载的任务！！！！O(∩_∩)O哈！");
		}
}
