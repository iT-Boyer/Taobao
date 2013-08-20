package net.books.qq;

import java.io.IOException;
import java.util.ArrayList;

import net.hsg.tools.Tools;
import com.lowagie.text.DocumentException;

import net.books.hanzi2pinyin.hanzi2pinyin;
import net.books.words.ExportWord;

public class shubenword {

	/**
	 * @param args
	 */
	private static ExportWord ew = new ExportWord();
	
	public static void main(String[] args) throws Exception {
		//获取网页源码
		Tools tools = new Tools();
		String html="";
		String url = "http://book.qq.com/s/book/0/3/3968/index.shtml";//深浅
		try {
			html = tools.getContent(url);
		} catch (IOException e) {
			System.out.println("无法从该页面获取任何内容："+url);
			e.printStackTrace();
		}
		String bookname = tools.getStrByREX(html, "<h1>.+?</h1>");
		bookname = tools.Html2Text(bookname);
		String fileName =bookname.replace("・", "");
		ew.fileroot = "e:/下载书/"+fileName+".doc";
		ew.shuming=bookname;
		String author =tools.getStrByREX(html, "\\(splitAuthor.+?\\)");
		author =author.replaceAll("\\(\\s*splitAuthor\\(", "").replaceAll("・", "·").replaceAll("'", "").replaceAll("\\)", "");
		ew.zuozhe=author;
		String chubanStr = tools.getStrByREX(html, "出 版：.+?</a>");
		chubanStr = tools.Html2Text(chubanStr.replace("出 版：", ""));
		ew.chubanxiang = chubanStr;
		String summary = tools.getStrByREX(html, "<p>[^\\\\]+?</p>");
		summary = tools.Html2Text(summary);
		ew.neirongtiyao=summary;
		String img = tools.getStrByREX(html, "<div id=\"bookCover\">.+?</div>");
		img = tools.getStrByREX(img, "<img[^>]+>");
		ew.url=url;
		if(img!=null && !img.trim().equals(""))
			{
			String imgUrl = tools.getSrcFromImgtag(img, url);
			ew.fengmian=imgUrl;
			}
		ew.addHeader();//将上边定义的头部信息添加到word中
        //开始增加正文和章节
		ArrayList<String> al = tools.getByREX(html, "<div class=\"tit\">\\s*<span>.+?<div class=\"b\">");
		for(int i=0;i<al.size();i++)
		{
			String zc = "第"+(i+1)+"章";
				ew.addZhang(zc);
				ArrayList<String> jieList = tools.getByREX(al.get(i), "<a[^>]+javascript:opennew[^>]+>.+?</a>");
				for (int j = 0; j < jieList.size(); j++) {
					String pageStr = jieList.get(j).replace("javascript:opennew('", "").replace("')", "");
					String jieUrl = tools.getLinkTagFromLinkStr(pageStr, url).getLink();
					String jieNm = tools.getLinkTagFromLinkStr(pageStr, url).getAttribute("linktxt").trim();
					String jieHtml="";
					try {
						jieHtml = tools.getContent(jieUrl,"utf-8");
					} catch (IOException e1) {
						System.out.println("无法从该页面取到小说章节内容："+pageStr);
						e1.printStackTrace();
					}
					String chapter_info=tools.getStrByREX(jieHtml, "<div[^>]+content[^>]+>[^\\\\]+?</div>");
					try {
						chapter_info = tools.Html2Text(chapter_info.replaceAll("(\\s*<br />\\s*){1,}", "\n")).trim();
						if(chapter_info.startsWith("\n"))
							chapter_info=chapter_info.substring(1);
					} catch (Exception e) {
					e.printStackTrace();
					}
					ew.addJie(jieNm);
					ew.addText(chapter_info);
				}
		}
			ew.close();
			System.out.println("本书下载完成！！！");
		}
}
