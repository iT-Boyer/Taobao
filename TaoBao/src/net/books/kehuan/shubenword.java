package net.books.kehuan;

import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

import org.htmlparser.tags.LinkTag;

import com.forfuture.tools.Tools;
import net.books.words.ExportWord;
import net.hsg.tools.*;

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
		HsgTools hsgTools = new HsgTools();
		String []list = hsgTools.BsUrl("kehuan书本地址").split("图书Url:");
		for(int i=1;i<list.length;i++){
			FileWriter fw = new FileWriter("e:/豆瓣/科幻Url.txt", true); //实例化写入txt文档的方法；
			ew.clean();				//清空ew
			String html = "";
			String[]luj= list[i].split("图书版图：");
			String url = luj[0].trim();
			System.out.println("#############"+url);
			try {
				html =tools.getContentOld(url,"gbk");
			} catch (Exception e) {
				System.out.println("获取不到的任何内容："+url);
			}
			String[] mimg = luj[1].split("图书名：");
			String bookname = mimg[1].trim();
			String fileName = bookname.replace("・", "");
			ew.fileroot = "e:/下载书/"+fileName+".doc";
			ew.shuming=bookname;
			String zz = tools.Html2Text(tools.getStrByREX(html, "<title>.+?</title>").replace("作者：", "").replaceAll("《.+?》", ""));
			if(!hsgTools.zhjsk( bookname, zz,ew,fw))
				if (!hsgTools.dbwang(bookname,zz,ew,fw)) 
					hsgTools.dbOne(bookname,zz,fw,ew);
			ew.url=url;
			ew.fengmian=mimg[0].trim();
			ew.addHeader();//将上边定义的头部信息添加到word中
	        //开始增加正文和章节
			String htStr = tools.getStrByREX(html, "((div[^>]+TitleLinks[^>]*>)|(<!--HTMLBUILERPART0-->)).+?((<!--/HTMLBUILERPART0-->)|(<!--HTMLBuilderInsertPoint-->))");
			ArrayList<String> al = tools.getByREX(htStr, "<a[^>]+>.+?</a>");
			for(int j=0;j<al.size();j++)
			{
						LinkTag lt = tools.getLinkTagFromLinkStr(al.get(j), url);
						String jieUrl = lt.getLink();
						String zcname = lt.getAttribute("linktxt").trim();
						ew.addZhang(zcname);
						String jieHtml="";
						try
						{
							jieHtml = tools.getContentOld(jieUrl,"gbk");
						}
						catch (Exception e)
						{
							System.out.println("源码获取失败："+jieUrl);
						}
						String chapter_info=tools.getStrByREX(jieHtml, "<!--HTMLBUILERPART0-->.+?<!--/HTMLBUILERPART0-->");
						System.out.println(chapter_info);
						try {
							chapter_info = tools.Html2Text(chapter_info.replaceAll("(\\s*((<BR>)|(<br\\s*/>))\\s*){1,}", "\n")).trim();
							if(chapter_info.startsWith("\n"))
								chapter_info=chapter_info.substring(1);
						} catch (Exception e) 
						{
							System.err.println("获取到的章节为空:"+jieUrl);
						}
						ew.addText(chapter_info);
			}
				ew.close();
				System.out.println("《"+bookname+"》本书下载完成！！！");
			}
		System.out.println("完成所有下载的任务！！！！O(∩_∩)O哈！");
		}
}
