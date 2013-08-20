package net.books.UU8;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.htmlparser.tags.LinkTag;

import net.aboutbooks.chubanshe.Panduan;
import net.aboutbooks.pipei.PipeiThrad;
import net.aboutbooks.pojo.AboutBook;
import net.books.pojo.Books_Info;
import net.hsg.tools.HsgTools;
import net.hsg.tools.Tools;
import net.rile.sql.DataBaseDao;

import com.forfuture.autoconfig.dto.newspaperdecr;

public class Txt_Copy1 {
	/**
	 * @param args
	 * @throws IOException 
 */
public static void main(String[] args) throws IOException {
	Tools tools = new Tools();
	DataBaseDao dbd=new DataBaseDao();
	HsgTools hsgTools = new HsgTools();
//		dbd.create(Books_Info.class, true);   //生成图书的信息表
for (int n = 34; n < 36; n++)
{
	String leibie ="";
	int t =16;
	for (int i = 0; i <= t; i++) {
		String url1="http://www.uus8.com/du8/types.asp?typeid="+n+"&page="+i; //搜狐图书首地址
		System.out.println("当前抓取的搜狐读书的页数："+url1);
		String htmls = "";
		try {
			htmls = tools.getContent(url1);
		} catch (IOException e) {
			System.out.println("获取UUS8书籍路径失败："+url1);
			e.printStackTrace();
			break;
		}
		String yt=tools.getStrByREX(htmls, "共有\\s*\\d{1,}\\s*页");
		yt = tools.getStrByREX(yt, "\\d{1,}");
		t = Integer.parseInt(yt);
		leibie = tools.getStrByREX(htmls, "<h2>.+?</h2>");
		leibie = tools.Html2Text(leibie).trim();
		String hh = tools.getStrByREX(htmls, "<!-- left -->.+?</div>");
		ArrayList<String> als = tools.getByREX(hh, "<a\\s+[^>]+>.+?</a>");
		for (int a = 0; a < als.size(); a++) {
			Books_Info books_Info = new Books_Info(); //初始化图书信息表的类
			if (leibie!=null&&!leibie.equals(""))
				books_Info.setFengmian(leibie);
			LinkTag lt =tools.getLinkTagFromLinkStr(als.get(a),url1);
			String bookName = tools.getStrByREX(als.get(a), "title=\".+?\""); //图书名
			bookName = bookName.replace("title=\"", "").replace("&#8226;", "·").replaceAll("((\")|(《)|(》)|(\\s+))", "");
			if (nameRex(bookName)){
				System.out.println(bookName+":数据库中已存在，跳过！！！");
				continue;
			}
			String major = "";
			//从UUs8网站中获取作者，出版社信息
			if (lt.getLink()==null||lt.getLink().trim().equals(""))
				continue;
			String xxhtm="";
			try
			{
				xxhtm = tools.getContentOld(lt.getLink(), "gbk");
			} catch (Exception e)
			{
				System.out.println("uus8源码获取失败！！！"+lt.getLink());
				continue;
			}
			xxhtm = tools.getStrByREX(xxhtm, "<div id=\"youli2\">\\s*<ul>.+?</a>");
			String txtlink = tools.getStrByREX(xxhtm, "<a\\s+[^>]+>.+?</a>");
			if(txtlink!=null&&!txtlink.trim().equals(""))
			{
				String orjj = tools.Html2Text(txtlink).replaceAll("\\s+", "");
				if (orjj.contains("简介")||orjj.contains("内容"))
				{
					books_Info.setImprint(orjj);
					LinkTag ltxx = tools.getLinkTagFromLinkStr(txtlink, lt.getLink());
					String txturl =lt.getLink()+"/"+tools.getStrByREX(ltxx.getLink(), "\\d{1,}\\.htm");
					String xx2htm = tools.getContentOld(txturl, "gbk");
					String neirong =tools.getStrByREX(xx2htm, "<p id=\"zoom\">.+?((<font color=\"#ffffff\">)|(</p>))");
					if (neirong!=null && !neirong.trim().equals(""))
						{
						String author = tools.getStrByREX(neirong, "作者\\s*((:)|(：)).{0,50}?((出版)|(<br\\s*/>))");
						String chuban = tools.getStrByREX(neirong, "出版.+?<br\\s*/>");
						if (author!=null&&!author.trim().equals(""))
						{
							author = tools.Html2Text(author).replaceAll("\\s+", "");
							author = author.replaceAll("((作者\\s*:)|(作者\\s*：))", "").replace("出版", "");
							System.out.println("本书作者："+author);
							books_Info.setAuthor(author);
						}
						if (chuban!=null&&chuban.trim().equals(""))
						{
							chuban = tools.Html2Text(chuban).replaceAll("((出版\\s*：)|(出版\\s*:))", "");
							System.out.println("本书出版项："+chuban);
							books_Info.setChubanxiang(chuban);
						}
						if(neirong.length()>200){
							neirong = neirong.substring(100,neirong.length());
							major = hsgTools.jianjiechuli(neirong);
						}else {
							major = hsgTools.jianjiechuli(neirong);
						}
						books_Info.setSummary(major);
					}
				}
			}
				//从UUs8网站跳出。。。。。。。。。。进入引擎搜索
				//---------------------本网站的信息采集------------------------------
					books_Info.setBookname(bookName);
					if(leibie.equals("卧龙生武侠全集"))
						books_Info.setAuthor("卧龙生");
					if(leibie.equals("诸葛青云武侠全集"))
						books_Info.setAuthor("诸葛青云");
					if(leibie.equals("温瑞安武侠全集"))
						books_Info.setAuthor("温瑞安");
					if(leibie.equals("卧龙生武侠全集"))
						books_Info.setAuthor("卧龙生");
					if(leibie.equals("黄易武侠全集"))
						books_Info.setAuthor("黄易");
					if(leibie.equals("还珠楼主武侠全集"))
						books_Info.setAuthor("还珠楼主");
					if(leibie.equals("沧月作品全集"))
						books_Info.setAuthor("沧月");
					if(leibie.equals("鲁迅全集"))
						books_Info.setAuthor("鲁迅");
					books_Info.setUrl(lt.getLink());
				//----------------------搜索引擎的返回信息的采集-----------------------
					PipeiThrad pp=new PipeiThrad();
//					pp.DOUBAN=false;  					//关闭豆瓣搜索
//					pp.ZHUSANJIAO=false;
//					pp.DANGDANG = false;
					AboutBook ab=new AboutBook(); 		//提供原始数据的pojo
					ab.setName(bookName);
					if (books_Info.getAuthor()!=null&&!books_Info.getAuthor().trim().equals(""))
						ab.setZuozhe(books_Info.getAuthor());
					if(books_Info.getChubanxiang()!=null&&!books_Info.getChubanxiang().trim().equals(""))
						ab.setChubanshe(books_Info.getChubanxiang());
					AboutBook abb=pp.comparByXSD(ab);   //通过匹配引擎返回结果集
					Panduan pd=new Panduan();			//出版地的判断方法
					if(abb!=null){
					books_Info.setYYbookname(abb.getName());
					books_Info.setYYauthor(abb.getZuozhe());
					books_Info.setFengmianUrl(abb.getImgSrc());
					String pressold = abb.getChubanshe();
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
						if(!pressold.trim().equals(""))
						  if(!pd.panduan(pressold).replaceAll("：.+", "").trim().equals(""))
							  pressold = pd.panduan(pressold).replaceAll("：.+", "：").trim()+pressold;
						}
					 books_Info.setYYchubanxiang(pressold);
					}
					books_Info.setYYurl(abb.website);
					books_Info.setISBN(abb.getIsbn());			//获取到的ISBN号
					String yy = tools.getStrByREX(abb.getYeshu(), "\\d{1,}");  //获取到的页数
					if(yy!=null&&!yy.equals(""))
						books_Info.setPagesum(Integer.parseInt(yy));
					books_Info.setZt(""+abb.fenshu);			//给获取到的信息打分
					//小说简介----待添加---当图书网站没有简介时，需启动搜索引擎反馈该信息
					if (major==null||major.trim().equals(""))
						if (abb.getJianjie()!=null&&!abb.getJianjie().trim().equals("")){
							books_Info.setImprint("网络资源");
							books_Info.setSummary(abb.getJianjie());
						}
//					books_Info.setFengmianUrl(abb.getImgSrc());		//小说简介----待添加---当图书网站没有封面时，需启动搜索引擎反馈该信息
					}else {
						books_Info.setZt("待整理");
					}
//----------------------所有资源信息采集结束，存储到数据库中---------------------------------
					dbd.insert(books_Info);
			}
			System.out.println("图书个数："+als.size()*(i+1));
		}
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
