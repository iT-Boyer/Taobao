package net.books.UU8;

import java.util.ArrayList;

import org.htmlparser.tags.LinkTag;
import org.nutz.dao.Chain;
import org.nutz.dao.Cnd;

import com.forfuture.tools.Tools;

import net.books.pojo.Books_Info;
import net.books.words.ExportWord;
import net.hsg.tools.*;
import net.rile.sql.DataBaseDao;

public class shubenword_Copy1 
{
	/**
	 * @param args
	 */
	private static ExportWord ew = new ExportWord();
	public static Tools tools = new Tools();
	public static HsgTools hsgTools = new HsgTools();
	public static void main(String[] args) throws Exception {
		DataBaseDao dbd=new DataBaseDao();
//		java.util.List<Books_Info> booklist= dbd.query(Books_Info.class, Cnd.where("进度", "=", "周四"), null);
		java.util.List<Books_Info> booklist= dbd.query(Books_Info.class, Cnd.where("id", "=",  6630), null);
//		java.util.List<Books_Info> booklist= dbd.query(Books_Info.class, Cnd.where("书名", "like", "%古今情海%"), null);
//		java.util.List<Books_Info> booklist= dbd.query(Books_Info.class, Cnd.where("进度", ">", 0.5).and("封面名字", "<>", "特别推荐").and("id", "<>", 3192), null);
		System.out.println("剩余未下的图书本数："+booklist.size());
		int t=0;
		String html=null;
		while ( t < booklist.size() ) {
	    boolean breakTrue = false;
		String Ztvalue = "";
			ew.clean();
			ew.id = booklist.get(t).getId();
			System.out.println("&&&&&&&&&"+ew.id);
			String docfile="tttttttttttttt";
//			if(booklist.get(t).getFengmian()!=null)
//				docfile = booklist.get(t).getFengmian().trim();
			ew.shuming = booklist.get(t).getBookname().replaceAll("((《)|(》)|(\\s+))", "");	//图书网作者
			ew.url = booklist.get(t).getUrl();			//图书网路径
			try {
				html =tools.getContentOld(ew.url,"gbk");
			} catch (Exception e) {
				System.out.println("获取不到的任何内容："+ew.url);
				t++;
				continue;
			}
			if(booklist.get(t).getAuthor()!=null)
			{
				ew.zuozhe = booklist.get(t).getAuthor();		//图书网作者
			}else {
				ew.zuozhe = booklist.get(t).getYYauthor();		//图书网作者
			}
			int yeshu = booklist.get(t).getPagesum();
			if(yeshu==0||yeshu==26){
				ew.yeshu="";
			}else {
				ew.yeshu = ""+yeshu;
			}
			ew.chubanxiang = booklist.get(t).getYYchubanxiang();	//已整理后的出版项为主，尽量将出版项的精确信息整理到数据表的"YY出版项"字段里
			ew.isbn = booklist.get(t).getISBN();				//ISBN号
			ew.banbenshuoming ="";					//版本说明
			String Summary=booklist.get(t).getSummary();
			if(Summary!=null)
			ew.neirongtiyao = Summary.replaceAll("\\s+", "");   //添加图书简介
			String fileName = ew.shuming.replaceAll("((\\\\)|(/)|(:)|(\")|(<)|(>)|(\\*)|(\\?)|(\\|))", "");
			ew.fileroot = "e:/UUs8/"+docfile+"/"+fileName+".doc";
			String fenmian = tools.getStrByREX(html, "<img[^>]+cover[^>]+>");
			if (fenmian!=null)
			fenmian = tools.getSrcFromImgtag(fenmian, ew.url);
			if(fenmian!=null&&!fenmian.trim().equals("")){
				ew.fengmian = ew.url+"/"+tools.getStrByREX(fenmian, "cover\\..+");	//图书网封面路径
			  }else{
				System.out.println("原封面图片加载失败："+fenmian);
				String tupurl = booklist.get(t).getFengmianUrl();
				if(tupurl!=null&&!tupurl.trim().equals("")){
					dbd.update(Books_Info.class, Chain.make("正文", "非本书封面图"), 
												 Cnd.where("id", "=", ew.id));
					ew.fengmian = tupurl;
				}else {
					System.err.println("数据库中封面图片加载失败:"+ew.shuming);
					Ztvalue = "封面图片不存在！";
					t++;
					continue;
					}
			  }
//			ew.fengmian = "http://cover.duxiu.com/cover/Cover.dll?iid=6768656D6B6965676866A29D3133303934363436";
			ew.banbenshuoming="全本";
 			ew.addHeader();//将上边定义的头部信息添加到word中
	        //  开始增加正文和章节
 			String htmlstr = tools.getStrByREX(html, "<div id=\"youli2\">.+?</div>");
			ArrayList<String> al = tools.getByREX(htmlstr, "<a\\s+[^>]+>.+?</a>");//匹配得到zhc的集合
			System.out.println(al.size());
			for(int j=0;j<al.size();j++)
			{
				LinkTag lt = tools.getLinkTagFromLinkStr(al.get(j), ew.url);
//				System.out.println(lt.getLink());
				String jieUrl = ew.url+tools.getStrByREX(lt.getLink().replace("\\", "/"), "/[^/]+((\\d{0,})|(\\w{0,}))\\s*\\.htm");
				System.out.println("章节下载url："+jieUrl);
				String zhcname = lt.getAttribute("linktxt").trim();
				String zhc = zhcname.replaceAll("\\s+", "");
			  if (hsgTools.zhcnameRex(zhc))
				{
					if(zhc.contains("简介")||zhc.contains("提要")) {
						ew.neirongtiyao = hsgTools.jianjiechuli(booklist.get(t).getSummary());
						ew.addHeader();
						}
				   continue;
				}
				if(zhc.startsWith("卷")&&!zhc.contains("卷结")){
					ew.addParse(zhcname);
				}else if(zhc.startsWith("第")&&zhc.contains("部"))
				{
					ew.addParse(zhcname);
				}else {
					ew.addZhang(zhcname);
				}
				String zhcHtml="";
				try{
					zhcHtml = tools.getContentOld(jieUrl,"gbk");
				   }catch (Exception e){
					zhcHtml = tools.getContentOld(jieUrl,"gbk");
					System.out.println("第二次源码获取："+jieUrl);
					if (zhcHtml==null||zhcHtml.trim().equals("")){
						System.out.println("第二次源码获取失败！！！！"+jieUrl);
						breakTrue = true;
						Ztvalue = "章节缺下";
						break;
						}
				   }
				String chapter=tools.getStrByREX(zhcHtml, "<p id=\"zoom\">.+?((<font color=\"#ffffff\">)|(</p>))");
				chapter = hsgTools.overtxt(chapter);
				if(chapter!=null&&!chapter.trim().equals("")){
					ew.addText(chapter);
					}else {
						System.err.println("获取到的章节为空:"+jieUrl);
						breakTrue = true;
						Ztvalue = "章节缺下";
						continue;
					 }
				System.out.println("下载中...."+j);
			}
			if (breakTrue)
				{
				ew.zt=Ztvalue;
				}else {
					if (booklist.get(t).getZt().equals("登记@@")){
						ew.zt = "登记@@";
					}else {
						ew.zt = "周六";
					}
				}
		System.out.println("id:"+booklist.get(t).getId()+"第"+t+"本：《"+ew.shuming+"》下载完成！！！");
		ew.close();
		t++;
	 }
		System.out.println("完成所有下载的任务！！！！O(∩_∩)O哈！");
  }
}
