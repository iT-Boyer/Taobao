package net.books.Souhu;

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
import net.rile.sql.DataBaseDao;

import com.forfuture.tools.Tools;

public class Txt_Copy2_处理0_5 {

	/**
	 * @param args
	 * @throws IOException 
	 */
	public static void main(String[] args) throws IOException {
		Tools tools = new Tools();
		DataBaseDao dbd=new DataBaseDao();
		List<Books_Info> als = dbd.query(Books_Info.class, Cnd.where("进度", "<=", 0.5).and("进度", "=", "待整理"), null);
		System.out.println("待处理的图书个数："+als.size());	
		for (int a = 0; a < als.size(); a++) {
				String bookName = als.get(a).getBookname(); //图书名
				String author = als.get(a).getAuthor(); //作者
				String press = als.get(a).getChubanxiang();
				Books_Info books_Info = new Books_Info(); //初始化图书信息表的类
				//----------------------搜索引擎的返回信息的采集-----------------------
					PipeiThrad pp=new PipeiThrad();
//					pp.DOUBAN=false;  					//关闭豆瓣搜索
//					pp.ZHUSANJIAO=false;
					AboutBook ab=new AboutBook(); 		//提供原始数据的pojo
					ab.setName(bookName);
					ab.setChubanshe(press);
					ab.setZuozhe(author);
					AboutBook abb=pp.comparByXSD(ab);   //通过匹配引擎返回结果集
					Panduan pd=new Panduan();			//出版地的判断方法
					if(abb!=null){
					books_Info.setYYbookname(abb.getName());
					books_Info.setYYauthor(abb.getZuozhe());
					String pressold = abb.getChubanshe();
					if(pressold.replaceAll("\\d{1,}", "").trim().length()<4)
					{
						pressold = press+pressold;
//						System.out.println(pressold);
					}
					if(!pressold.contains("市:")&&!pressold.contains("市："))
					{
						pressold = pd.panduan(pressold).replaceAll(":.+", ":")+pressold;
					}
					books_Info.setYYchubanxiang(pressold);
					books_Info.setYYurl(abb.website);
					books_Info.setISBN(abb.getIsbn());			//获取到的ISBN号
					String yy = tools.getStrByREX(abb.getYeshu(), "\\d{1,}");  //获取到的页数
					if(yy!=null&&!yy.equals(""))
					books_Info.setPagesum(Integer.valueOf(yy));
					books_Info.setZt(""+abb.fenshu);			//给获取到的信息打分
//					books_Info.setSummary(abb.getJianjie());		//小说简介----待添加---当图书网站没有简介时，需启动搜索引擎反馈该信息
//					books_Info.setFengmianUrl(abb.getImgSrc());		//小说简介----待添加---当图书网站没有封面时，需启动搜索引擎反馈该信息
					}else {
						books_Info.setZt("待整理");
					}
//----------------------所有资源信息采集结束，存储到数据库中---------------------------------
					dbd.update(Books_Info.class,Chain.make("进度", books_Info.getZt()),Cnd.where("书名","=",bookName));
					dbd.update(Books_Info.class,Chain.make("YY书名", books_Info.getYYbookname()),Cnd.where("书名","=",bookName));
					dbd.update(Books_Info.class,Chain.make("YY作者", books_Info.getYYauthor()),Cnd.where("书名","=",bookName));
					dbd.update(Books_Info.class,Chain.make("YY源网站url", books_Info.getYYurl()),Cnd.where("书名","=",bookName));
					dbd.update(Books_Info.class,Chain.make("YY出版项", books_Info.getYYchubanxiang()),Cnd.where("书名","=",bookName));
					dbd.update(Books_Info.class,Chain.make("ISBN", books_Info.getISBN()),Cnd.where("书名","=",bookName));
					dbd.update(Books_Info.class,Chain.make("页数", books_Info.getPagesum()),Cnd.where("书名","=",bookName));
			}
		System.out.println("操作成功！！！");
	}

}
