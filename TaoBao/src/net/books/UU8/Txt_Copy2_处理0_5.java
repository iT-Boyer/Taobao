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
				String bookUrl = als.get(a).getUrl();
				String author = ""; //作者
				String press = "";
				String major = "";
				Books_Info books_Info = new Books_Info(); //初始化图书信息表的类
				//从UUs8网站中获取作者，出版社信息
				String xxhtm = tools.getContentOld(bookUrl, "gbk");
				xxhtm = tools.getStrByREX(xxhtm, "<div id=\"youli2\">\\s*<ul>.+?</a>");
				String txtlink = tools.getStrByREX(xxhtm, "<a\\s+[^>]+>.+?</a>");
				if(txtlink!=null&&!txtlink.trim().equals(""))
				{
					String orjj = tools.Html2Text(txtlink).replaceAll("\\s+", "");
					if (orjj.contains("简介")||orjj.contains("内容"))
					{
						books_Info.setImprint(orjj);
						LinkTag ltxx = tools.getLinkTagFromLinkStr(txtlink, bookUrl);
						String txturl =bookUrl+"/"+tools.getStrByREX(ltxx.getLink(), "\\d{1,}\\.htm");
						String xx2htm = tools.getContentOld(txturl, "gbk");
						String neirong =tools.getStrByREX(xx2htm, "<p id=\"zoom\">.+?((<font color=\"#ffffff\">)|(</p>))");
						if (neirong!=null && !neirong.trim().equals("")){
							author = tools.getStrByREX(neirong, "作者\\s*((:)|(：)).+?((出版)|(<br\\s*/>))");
							press = tools.getStrByREX(neirong, "出版.+?<br\\s*/>");
						if (author!=null&&!author.trim().equals(""))
						{
							author = tools.Html2Text(author).replaceAll("\\s+", "");
							author = author.replaceAll("((作者\\s*:)|(作者\\s*：))", "").replace("出版", "");
							System.out.println("本书作者："+author);
							books_Info.setAuthor(author);
						}
						if (press!=null&&press.trim().equals(""))
						{
							press = tools.Html2Text(press).replaceAll("((出版\\s*：)|(出版\\s*:))", "");
							System.out.println("本书出版项："+press);
							books_Info.setChubanxiang(press);
						}
							neirong = neirong.replaceAll("作者\\s*((:)|(：)).+?<\\s*br\\s*/\\s*>", "");
							neirong=neirong.replace("&nbsp;", "").replaceAll("<br>\\s*…{1,}", "");
							neirong=tools.Html2Text(neirong.replaceAll("(\\s*((<BR>)|(<br\\s*/>))\\s*){1,}", "\n")).replace("&#65533;", "").replace("&#8226;", "·").trim();
							neirong=neirong.replaceAll("((■)|(□)|(▲)|(△)|(▽)|(▼)|(●)|(○)|(☆)|(★)|(\\*)|(&#8226;)|(◇)|(◆)|(>{2,}))", "").trim();
							if(neirong.length()>=200){
								neirong = neirong.substring(2,190)+"...";
							}else{
								neirong = neirong.substring(2, neirong.length());
							}
							major = neirong;
						}
					}
				}
				//从UUs8网站跳出。。。。。。。。。。进入引擎搜索
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
					String pressold = abb.getChubanshe();
					if(abb!=null){
						books_Info.setYYbookname(abb.getName());
						books_Info.setYYauthor(abb.getZuozhe());
						if (pressold!=null&&!pressold.trim().equals(""))
						{
							if(!pressold.contains("市:")&&!pressold.contains("市："))
							{
								if(pressold.replaceAll("\\d{1,}", "").trim().length()<4){
									if(books_Info.getChubanxiang()!=null&&!books_Info.getChubanxiang().trim().equals(""))
										{
										pressold = books_Info.getChubanxiang()+pressold;
										}else {
											pressold = "";
										}
								  }
						  if (!pressold.trim().equals(""))
							pressold = pd.panduan(pressold).replaceAll(":.+", "：")+pressold;
							}
						 books_Info.setYYchubanxiang(pressold);
						}
						books_Info.setYYurl(abb.website);
						books_Info.setISBN(abb.getIsbn());			//获取到的ISBN号
						String yy = tools.getStrByREX(abb.getYeshu(), "\\d{1,}");  //获取到的页数
						if(yy!=null&&!yy.equals(""))
							books_Info.setPagesum(Integer.valueOf(yy));
						books_Info.setZt(""+abb.fenshu);			//给获取到的信息打分
						//小说简介----待添加---当图书网站没有简介时，需启动搜索引擎反馈该信息
						if (major==null||major.trim().equals(""))
							if (abb.getJianjie()!=null&&!abb.getJianjie().trim().equals(""))
								books_Info.setSummary(abb.getJianjie());	//小说简介----待添加---当图书网站没有简介时，需启动搜索引擎反馈该信息
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
