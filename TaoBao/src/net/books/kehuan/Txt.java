package net.books.kehuan;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

import net.books.pojo.BooksZT;
import net.rile.sql.DataBaseDao;

import com.forfuture.tools.Tools;

public class Txt {

	/**
	 * @param args
	 * @throws IOException 
	 */
	public static void main(String[] args) throws IOException {
		// TODO Auto-generated method stub
		Tools tools = new Tools();
		DataBaseDao dbd=new DataBaseDao();
		BooksZT booksZT = new BooksZT();
		String url1="http://www.eywedu.com/kehuanxiaoshuo/index.htm"; //经典科幻
//		String url1="http://www.eywedu.com/suewenxue/index.htm"; //苏俄文学
		String htmls = "";
		try {
			htmls = tools.getContent(url1);
		} catch (IOException e) {
			System.out.println("获取kehuan书籍路径失败："+url1);
			e.printStackTrace();
		}
		ArrayList<String> als = tools.getByREX(htmls, "<td align=\"center\">.+?</tr>.+?</tr>");
            File f=new File("e:/图书/");
            f.mkdirs();
		FileWriter fw=new FileWriter("e:/图书/苏俄.txt",false);
		System.out.println("图书个数："+als.size());
		for (int a = 0; a < als.size(); a++) {
			ArrayList<String> uuu = tools.getByREX(als.get(a), "<a[^>]+>.+?</a>");
			ArrayList<String> hhh = tools.getByREX(als.get(a), "<font[^>]+>.+?</font>");
			for (int b = 0; b < uuu.size(); b++) {
				String lujing = tools.getLinkTagFromLinkStr(uuu.get(b), url1).getLink();
				String img = tools.getStrByREX(uuu.get(b), "<img[^>]+>");
				String Imgurl = tools.getSrcFromImgtag(img, url1);
				String bookName = tools.Html2Text(hhh.get(b));
//				booksZT.setTitle(bookName);
//				booksZT.setUrl(lujing);
				booksZT.setState("未完成");
		        dbd.insert(booksZT);
				fw.write("图书Url:"+lujing+"\r\n图书版图："+Imgurl+"\r\n图书名："+bookName+"\r\n\r\n");
			}
		}
		fw.close();
		System.out.println("操作成功！！！");
	}

}
