package net.books.UU8;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

import org.nutz.el.opt.custom.Trim;

import net.books.pojo.Books;
import net.books.pojo.BooksZT;
import net.rile.sql.DataBaseDao;

import com.forfuture.tools.Tools;

public class Txt {

	/**
	 * @param args
	 * @throws IOException 
	 */
	public static void main(String[] args) throws IOException {
		Tools tools = new Tools();
		DataBaseDao dbd=new DataBaseDao();
		dbd.create(Books.class, true);   //生成图书的信息表
		dbd.create(BooksZT.class,true);  //生成图书的状态表
		BooksZT booksZT = new BooksZT();
//		String url1="http://lz.book.sohu.com/lz_search.php?searchtype=0&searchvalue=&ordertype=1&author=&PB_page=1"; //搜狐图书首地址
//		 File f=new File("e:/图书/");
//         f.mkdirs();
//		FileWriter fw=new FileWriter("e:/图书/搜狐书本地址.txt",false);
		for (int i = 0; i <= 1522; i++) {
			String url1="http://lz.book.sohu.com/lz_search.php?searchtype=0&searchvalue=&ordertype=1&author=&PB_page="+(i+1); //搜狐图书首地址
			String htmls = "";
			try {
				htmls = tools.getContent(url1);
			} catch (IOException e) {
				System.out.println("获取搜狐读书书籍路径失败："+url1);
				e.printStackTrace();
			}
			ArrayList<String> als = tools.getByREX(htmls, "<div class=\"pt\">.+?出版：.+?</span>");
			for (int a = 0; a < als.size(); a++) {
//				System.out.println(als.get(a));
				String BookUrl = tools.getStrByREX(als.get(a), "<a[^>]+>.+?</a>");//图书的url
				BookUrl = tools.getLinkTagFromLinkStr(BookUrl,url1).getLink();
				String ImgUrl = tools.getStrByREX(als.get(a), "<img\\s+[^>]+>"); //封面的url
				ImgUrl = tools.getSrcFromImgtag(ImgUrl,url1);
				String bookName = tools.getStrByREX(als.get(a), "<h3>.+?</h3>"); //图书名
				bookName = tools.Html2Text(bookName).replace("&#8226;", "·");
				String author = tools.getStrByREX(als.get(a), "作\\s*者.+?</span>");   		//作者
//				System.out.println(author);
				author = tools.Html2Text(author).replace("作者", "").replace(":", "").replace("：", "").replace(" ", "").replace("&#8226;", "·").trim();
				String press = tools.getStrByREX(als.get(a), "出\\s*版\\s*：.+?</span>").replaceAll("\\s+", "");
				press = tools.Html2Text(press).replace("出版", "").replace(":", "").replace("：", "").replace(" ", "").replace("&#8226;", "·").trim();
					booksZT.setBookName(bookName);
					booksZT.setBookURL(BookUrl);
					booksZT.setAuthor(author);
					booksZT.setImgURL(ImgUrl);
					booksZT.setPress(press);
					booksZT.setState("No!!!");
			        dbd.insert(booksZT);
//					fw.write("图书Url:"+BookUrl+"\r\n图书版图："+ImgUrl+"\r\n图书名："+bookName+"\r\n\r\n");
			}
			System.out.println("图书个数："+als.size()*(i+1));
		}
//		fw.close();
		System.out.println("操作成功！！！");
	}

}
