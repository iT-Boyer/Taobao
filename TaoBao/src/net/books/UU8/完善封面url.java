package net.books.UU8;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.htmlparser.tags.LinkTag;
import org.nutz.dao.Chain;
import org.nutz.dao.Cnd;
import org.nutz.el.opt.custom.Trim;

import net.aboutbooks.chubanshe.Panduan;
import net.aboutbooks.pipei.PipeiThrad;
import net.aboutbooks.pojo.AboutBook;
import net.books.pojo.Books;
import net.books.pojo.BooksZT;
import net.books.pojo.Books_Info;
import net.hsg.tools.HsgTools;
import net.hsg.tools.Tools;
import net.rile.sql.DataBaseDao;


public class 完善封面url {

	/**
	 * @param args
	 * @throws IOException 
	 */
	public static void main(String[] args) throws IOException {
		DataBaseDao dbd=new DataBaseDao();
		List<Books_Info> als = dbd.query(Books_Info.class, Cnd.where("版本说明 ","like", "网络资%").and("进度", ">", 0.5).and("id","<",3101), null);
		System.out.println("待处理的图书个数："+als.size());	
		for (int a = 0; a < als.size(); a++) {
				Tools tools = new Tools();
				if(als.get(a).getFengmianUrl()!=null&&!als.get(a).getFengmianUrl().trim().equals(""))
					continue;
				String url = als.get(a).getYYurl().replace("bookDetail", "frame").replaceAll("&fenlei.+", "");
				System.out.println(url);
				String html = tools.getContentOld(url, "gbk");
				String fengmian = tools.getStrByREX(html, "((<div class=\"pic\">)|(<div class=\"tubookimg\">)|(<div id=\"mainpic\">)).+?</div>");
				fengmian = tools.getStrByREX(fengmian, "<img\\s+[^>]+>");
				if(fengmian==null)
					continue;
				fengmian = tools.getSrcFromImgtag(fengmian, url);
				if(fengmian!=null){
							dbd.update(Books_Info.class,Chain.make("封面url", fengmian),Cnd.where("id","=",als.get(a).getId()));
							System.out.println("更新成功"+als.get(a).getId());
				}
					}
//----------------------所有资源信息采集结束，存储到数据库中---------------------------------
//					dbd.update(Books_Info.class,Chain.make("进度", books_Info.getZt()),Cnd.where("书名","=",bookName));
//					dbd.update(Books_Info.class,Chain.make("YY书名", books_Info.getYYbookname()),Cnd.where("书名","=",bookName));
//					dbd.update(Books_Info.class,Chain.make("YY作者", books_Info.getYYauthor()),Cnd.where("书名","=",bookName));
//					dbd.update(Books_Info.class,Chain.make("YY源网站url", books_Info.getYYurl()),Cnd.where("书名","=",bookName));
//					dbd.update(Books_Info.class,Chain.make("YY出版项", books_Info.getYYchubanxiang()),Cnd.where("书名","=",bookName));
//					dbd.update(Books_Info.class,Chain.make("ISBN", books_Info.getISBN()),Cnd.where("书名","=",bookName));
//					dbd.update(Books_Info.class,Chain.make("页数", books_Info.getPagesum()),Cnd.where("书名","=",bookName));
		}
	}

