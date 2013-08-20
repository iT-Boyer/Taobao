package net.books.Souhu;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.htmlparser.tags.LinkTag;
import org.nutz.dao.Cnd;
import org.nutz.el.opt.custom.Trim;
import org.omg.CORBA.PUBLIC_MEMBER;

import net.aboutbooks.chubanshe.Panduan;
import net.aboutbooks.pipei.PipeiThrad;
import net.aboutbooks.pojo.AboutBook;
import net.books.pojo.Books;
import net.books.pojo.BooksZT;
import net.books.pojo.Books_Info;
import net.rile.sql.DataBaseDao;

import com.forfuture.tools.Tools;

public class Txt_Copy1 {

	/**
	 * @param args
	 * @throws IOException 
	 */
	public static void main(String[] args) throws IOException {
		Tools tools = new Tools();
		DataBaseDao dbd=new DataBaseDao();
		dbd.create(Books_Info.class, true);   //生成图书的信息表
		for (int i = 0; i <= 1; i++) {
//			String url1="http://lz.book.sohu.com/lz_search.php?searchtype=0&searchvalue=&ordertype=1&author=&PB_page="+(i+1); //搜狐图书首地址
			String url1="http://lz.book.sohu.com/lz_list.php?ps=40&px=20&page="+(i+1); //搜狐图书首地址
			System.out.println("当前抓取的搜狐读书的页数："+url1);
			String htmls = "";
			try {
				htmls = tools.getContent(url1);
			} catch (IOException e) {
				System.out.println("获取搜狐读书书籍路径失败："+url1);
				e.printStackTrace();
			}
			ArrayList<String> als = tools.getByREX(htmls, "<div class=\"pt\">.+?出版：.+?</span>");
			for (int a = 0; a < als.size(); a++) {
				String bookName = tools.getStrByREX(als.get(a), "<h3>.+?</h3>"); //图书名
				bookName = tools.Html2Text(bookName).replace("&#8226;", "·").replaceAll("((《)|(》)|(\\s+))", "");
				if (nameRex(bookName)){
					System.out.println(bookName+":数据库中已存在，跳过！！！");
					continue;
				}
				String BookUrl = tools.getStrByREX(als.get(a), "<a[^>]+>.+?</a>");//图书的url
				BookUrl = tools.getLinkTagFromLinkStr(BookUrl,url1).getLink();
				String keyhtml= "";
//				String key = "";
//				try {
//					keyhtml =tools.getContentOld(BookUrl,"gbk");
//				} catch (Exception e) {
//					System.out.println("获取不到的任何内容："+BookUrl);
//				}
//				key = tools.getStrByREX(keyhtml, "<em>\\s*连\\s*载.+?</em>");
//				key = tools.Html2Text(key).replaceAll("\\s+", "").trim();
//				if (!key.equals("连载完成")){
//					System.err.println(bookName+"本书未连载完成！！！跳过！！！");
//					continue;
//				}
				String ImgUrl = tools.getStrByREX(als.get(a), "<img\\s+[^>]+>"); //封面的url
				if (ImgUrl!=null&&!ImgUrl.trim().equals(""))
				ImgUrl = tools.getSrcFromImgtag(ImgUrl,url1);
				String author = tools.getStrByREX(als.get(a), "<a\\s+[^>]+author[^>]+>.+?</a>"); //作者
				if (author!=null&&!author.trim().equals(""))
				{
					String authorHtml = tools.getContent(tools.getLinkTagFromLinkStr(author, url1).getLink());
					author = tools.getStrByREX(authorHtml, "<!--author页面显示-->.+?</h2>");
					if (author!=null&&!author.trim().equals(""))
					author = tools.Html2Text(author).replace("&#8226;", "·").trim();
				}
				String press = tools.getStrByREX(als.get(a), "出\\s*版\\s*：.+?</span>");
				if (press!=null&&!press.trim().equals(""))
				press = tools.Html2Text(press).replaceAll("((\\s+)|(:)|(：))", "").replace("出版", "").trim();
				Books_Info books_Info = new Books_Info(); //初始化图书信息表的类
				//---------------------本网站的信息采集------------------------------
					books_Info.setBookname(bookName);
					books_Info.setUrl(BookUrl);
					books_Info.setAuthor(author);
					books_Info.setFengmianUrl(ImgUrl);
					books_Info.setChubanxiang(press);	//该网站出版信息不完善，同样需要保留，以便在启动搜索引擎时提供匹配信息参量以便达到更精确的匹配效果
//					books_Info.setSummary(""); 		    //小说简介----待添加---该网站在下载正文时获取相关简介
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
						pressold = pd.panduan(pressold).replaceAll(":.+", "：")+pressold;
					}
					books_Info.setYYchubanxiang(pressold.replace(",", "，"));
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
					dbd.insert(books_Info);
			}
			System.out.println("图书个数："+als.size()*(i+1));
		}
		System.out.println("操作成功！！！");
	}
	public static boolean nameRex(String bookName) throws IOException{
		DataBaseDao dbd=new DataBaseDao();
		List<Books_Info> nameList = dbd.query(Books_Info.class, null, null);
		boolean rex = false;
		for (int m = 0; m < nameList.size(); m++)
		{
			if (nameList.get(m).getBookname().equals(bookName)){
				rex = true;
				break;
			}
		}
		return rex;
	}
}
