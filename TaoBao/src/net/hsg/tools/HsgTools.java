package net.hsg.tools;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;

import net.books.hanzi2pinyin.hanzi2pinyin;
import net.books.words.ExportWord;

import com.books.urlstream.DetectEcoding;
import com.forfuture.tools.Tools;

public class HsgTools
{
	Tools tools = new Tools();
	/**
	 * 
	 * @return tuwenStr
	 * @throws IOException		
	 * @ //获取txt文档中的书本的基本信息
	 */
	public String BsUrl(String str) throws IOException{
		File f = new File("e:/豆瓣/");
		f.mkdirs();
		File ff = new File("e:/图书/"+str+".txt");
		FileReader fr = new FileReader(ff);
		char[] cs = new char[(int) ff.length()];
		fr.read(cs);
		String tuwenStr = new String(cs);
		return tuwenStr;
	}
	/*
	 * @从珠江三角洲数字图书馆获取图书的信息
	 */
	public boolean zhjsk(String bookname, String zz, ExportWord ew,FileWriter fw) throws IOException
	{
		String urlEcod = "";
		try
		{
			urlEcod = URLEncoder.encode(bookname, "gb2312");
		} catch (UnsupportedEncodingException e2)
		{
			System.err.println("将书名转换为Url编码时失败！！！" + bookname);
		}
		String zjStr = "http://books.gdlink.net.cn/search?sw=" + urlEcod + "&allsw=&bCon=&ecode=GBK&channel=search&Field=1";
		DetectEcoding de = new DetectEcoding();
		de.setUrl(zjStr);
		String ecoding = de.getEcoding();
		String zjht = "";
		boolean bl = false;
		try
		{
			zjht = tools.getContent(zjStr, ecoding);
		} catch (IOException e1)
		{
			System.err.println("获取珠江三角洲图书馆源码失败！！" + zjStr);
		}
		ArrayList<String> zjlist = tools.getByREX(zjht, "<tr width=\"100%\">.+?</tr>");
		for (int db = 0; db < zjlist.size(); db++)
		{
			String z1 = zjlist.get(db).replaceAll("\\s+", "");
			z1 = tools.Html2Text(z1);
			if (z1.equals("《"+bookname+"》")||z1.contains(bookname))
			{
				String zjurl = tools.getStrByREX(zjlist.get(db), "<a[^>]+>.+?</a>");
				zjurl = tools.getLinkTagFromLinkStr(zjurl, zjStr).getLink().replaceAll(".+\\?", "http://books.gdlink.net.cn/views/specific/2929/frame.jsp?").replace("&amp;", "&");
				String htmString = "";
				try
				{
					System.out.println("将要获取的书本信息的URL:"+zjurl);
					htmString = tools.getContent(zjurl);
				} catch (IOException e)
				{
						System.err.println("获取珠江三角洲图书馆网查询到的图书的详细信息源码失败！！" + zjurl);
						fw.write("珠江图书馆中获取失败："+bookname);
				}
				// 获取图书信息
				htmString = htmString.replace("&middot;", "・").replace("（", "[").replace("）", "]");
				String zuozhe = tools.getStrByREX(htmString, "【作\\s*者】[^\\\\]+?【");
				if (zuozhe == null)
					zuozhe = zz;
				ew.zuozhe = tools.Html2Text(zuozhe).replaceAll("【作\\s*者】", "").replace("【", "").trim();
				String chuban = tools.getStrByREX(htmString, "【出版项】[^\\\\]+?【");
				String isbn = tools.getStrByREX(htmString, "【ISBN号】.+?【");
				String yesh = tools.getStrByREX(htmString, "【形态项】.+?【");
				if(yesh!=null&&!yesh.trim().equals("")){
					yesh = tools.Html2Text(yesh).replace("【形态项】", "").replace("【", "").replaceAll(";.+", "").trim();
					ew.yeshu = yesh;
				}
				boolean tt=false;
				boolean tt1=false;
				if (chuban!=null && !chuban.trim().equals(""))
				{
					chuban = tools.Html2Text(chuban).replace("【出版项】", "").replace("【", "").trim();
					ew.chubanxiang = chuban;
					tt = true;
				}
				if (isbn!=null && !isbn.trim().equals(""))
				{
					isbn = tools.Html2Text(isbn).replace("【ISBN号】", "").replace("【", "").replace("-", "").trim();
					ew.isbn = isbn;
					tt1 = true;
				}
				dbYeshu(bookname,zz,ew,fw);  		//从豆瓣网中获取图书的页数和小说简介
				String neirong = tools.getStrByREX(htmString, "<div[^>]+((m_abname)|(tu_content))[^>]*>.+?((<br\\s*/>)|(<p>)).+?</div>");
				if (neirong!=null && !neirong.trim().equals(""))
				{
					neirong = tools.Html2Text(neirong).replace("内容提要:", "").replaceAll("(\\s*((<BR>)|(<br\\s*/>))\\s*){1,}", "\n").trim();
					if(neirong.length()>=200){
						ew.neirongtiyao = neirong.substring(0, 190)+"...";
					}else{
						ew.neirongtiyao = neirong.substring(0, neirong.length());
					}
				}
				System.out.println("书名："+bookname+"\n出版项："+chuban+"\nISBN号："+isbn+"\n页数："+yesh+"\n作者："+ew.zuozhe);
				if (tt1&&tt) //假如从珠江三角洲图书馆中无法获取到ISBN或者出版项就放弃以上操作开始从豆瓣网中获取信息
					bl = true;
				break;
			}
		}
		if (bl)
		{
			ew.zt = "通过！！";
			System.out.println("珠江三角洲数字图书馆获取成功！！！");
		} else
		{
			System.out.println("珠江三角洲数字图书馆获取失败！！！");
		}
		return bl;
	}	
	/*
	 * @从豆瓣读书网站中获取图书的页数信息
	 */
	public void dbYeshu(String bookname, String zz, ExportWord ew,FileWriter fw) throws IOException
	{
		String dbStr = "http://book.douban.com/subject_search?search_text=" + bookname + " " + zz + "&cat=1001";
		String dbht = "";
		boolean bl = false;
		try
		{
			dbht = tools.getContent(dbStr).replace("&middot;", "・").replace("（", "[").replace("）", "]");
		} catch (IOException e1)
		{
			System.err.println("获取豆瓣网查询结果列表的源码失败！！" + dbStr);
			e1.printStackTrace();
		}
		ArrayList<String> dblist = tools.getByREX(dbht, "<div class=\"pl2\">.+?<div\\s*[^>]+collect_form[^>]+>");
		for (int db = 0; db < dblist.size(); db++)
		{
			String z1 = dblist.get(db).replaceAll("\\s+", "");
			z1 = tools.Html2Text(z1);
			String z2 = zz.replaceAll("((（.+?）)|(\\[.+?\\]))", "").replaceAll("・.+", "").trim();
			String z3 = zz.replaceAll(".+・", "");
			hanzi2pinyin hp = new hanzi2pinyin();
			String z4 = hp.toPinyin(z3);
			String z5 = hp.toPinyin(z1);
			if (z1.contains(z2) && z1.contains(bookname) && z1.contains(z3) && z5.contains(z4))
			{
				String dburl = tools.getStrByREX(dblist.get(db), "<a[^>]+>.+?</a>");
				dburl = tools.getLinkTagFromLinkStr(dburl, dbStr).getLink();
				String htmString = "";
				try
				{
					htmString = tools.getContent(dburl).replace("&middot;", "・").replace("（", "[").replace("）", "]");
				} catch (IOException e)
				{
					System.err.println("获取豆瓣网查询结果的详细信息的源码失败！！" + dburl);
					e.printStackTrace();
				}
				// 获取图书信息
				String yeshu = "";
				String jjie = "";
				if(ew.yeshu==null){
					yeshu = tools.getStrByREX(htmString, "页数:.+?<br\\s*/>");
					if(yeshu == null)
						fw.write("@@@@@从豆瓣中获取到图书的页数失败："+bookname+"\r\n\r\n");
					ew.yeshu = yeshu;
				}
				if(ew.neirongtiyao==null){
					jjie = tools.getStrByREX(htmString, "<div class=\"related_info\">.+?<br/>\\s*</div>");
					if (jjie!=null && !jjie.trim().equals("")){
						jjie = tools.getStrByREX(jjie, "<div class=\"indent\">.+?</div>");
						jjie = tools.Html2Text(jjie).replaceAll("(\\s*((<BR>)|(<br\\s*/>))\\s*){1,}", "\n").trim();
						if(jjie.length()>=200){
							ew.neirongtiyao = jjie.substring(0, 190)+"...";
						}else{
							ew.neirongtiyao = jjie.substring(0, jjie.length())+"...";
						}
					}else {
						fw.write("%%%%%%从豆瓣中获取图书简介时失败："+bookname+"\r\n\r\n");
					}
				}
				if((yeshu!=null || jjie !=null)&&(!yeshu.trim().equals("") || !jjie.trim().equals("")) )
					bl = true;
				break;
			}
			
		}
		if (bl)
		{
			System.out.println("豆瓣网获取“图书的页数”成功！！！");
			fw.close();
		} else
		{
			fw.write("@@@@@从豆瓣中获取到图书的页数失败："+bookname+"\r\n\r\n");
		}
	}

	/*
	 * @从豆瓣读书网站中获取图书信息
	 */
	public boolean dbwang(String bookname, String zz, ExportWord ew,FileWriter fw) throws IOException 
	{
		String dbStr = "http://book.douban.com/subject_search?search_text=" + bookname + " " + zz + "&cat=1001";
		String dbht = "";
		boolean bl = false;
		try
		{
			dbht = tools.getContent(dbStr).replace("&middot;", "・").replace("（", "[").replace("）", "]");
		} catch (IOException e1)
		{
			System.err.println("获取豆瓣网查询结果列表的源码失败！！" + dbStr);
			e1.printStackTrace();
		}
		ArrayList<String> dblist = tools.getByREX(dbht, "<div class=\"pl2\">.+?<div\\s*[^>]+collect_form[^>]+>");
		for (int db = 0; db < dblist.size(); db++)
		{
			String z1 = dblist.get(db).replaceAll("\\s+", "");
			z1 = tools.Html2Text(z1);
			String z2 = zz.replaceAll("((（.+?）)|(\\[.+?\\]))", "").replaceAll("・.+", "").trim();
			String z3 = zz.replaceAll(".+・", "");
			hanzi2pinyin hp = new hanzi2pinyin();
			String z4 = hp.toPinyin(z3);
			String z5 = hp.toPinyin(z1);
			if (z1.contains(z2) && z1.contains(bookname) && z1.contains(z3) && z5.contains(z4))
			{
				String dburl = tools.getStrByREX(dblist.get(db), "<a[^>]+>.+?</a>");
				dburl = tools.getLinkTagFromLinkStr(dburl, dbStr).getLink();
				String htmString = "";
				try
				{
					htmString = tools.getContent(dburl).replace("&middot;", "・").replace("（", "[").replace("）", "]");
				} catch (IOException e)
				{
					System.err.println("获取豆瓣网查询结果的详细信息的源码失败！！" + dburl);
					e.printStackTrace();
				}
				// 获取图书信息
				String zuozhe = tools.getStrByREX(htmString, "作者</span>:.+?<br\\s*/>");
				String yizhe = tools.getStrByREX(htmString, "译者</span>:.+?<br\\s*/>");
				if(yizhe!=null)
					zuozhe += yizhe;
				if (zuozhe == null)
					zuozhe = zz;
				zuozhe = tools.Html2Text(zuozhe).replace("作者:", "").replace("\n", "").replace("\r", "").replaceAll("\\s+", "").trim();
				ew.zuozhe = zuozhe.trim();
				String chuban = tools.getStrByREX(htmString, "出版社:.+?<br\\s*/>.+?<br\\s*/>");
				if (chuban != null)
					ew.chubanxiang = tools.Html2Text(chuban).replaceAll("\\s*", "").replace("出版社:", "").replace("出版年:", "  ").trim();
				String isbn = tools.getStrByREX(htmString, "ISBN:.+?<br\\s*/>");
				if (isbn != null)
					ew.isbn = tools.Html2Text(isbn).replace("ISBN:", "").trim();
				String yeshu = tools.getStrByREX(htmString, "页数:.+?<br\\s*/>");
				if (yeshu != null)
					ew.yeshu = tools.getStrByREX(yeshu, "\\d{1,}");
				String jjie = tools.getStrByREX(htmString, "<div[^>]+related_info[^>]*>.+?<br\\s*/>\\s*</div>");
				if (jjie!=null && !jjie.trim().equals("")){
					jjie = tools.getStrByREX(jjie, "<div class=\"indent\">.+?</div>");
					jjie = tools.Html2Text(jjie).replaceAll("(\\s*((<BR>)|(<br\\s*/>))\\s*){1,}", "\n").trim();
					if(jjie.length()>=200){
						ew.neirongtiyao = jjie.substring(0, 190)+"...";
					}else{
						ew.neirongtiyao = jjie.substring(0, jjie.length());
					}
				}
				if (chuban!=null||isbn!=null||jjie!=null)
				bl = true;
				break;
			}
		}
		if (bl)
		{
			ew.zt = "通过！！";
			System.out.println("豆瓣网获取成功！！！");
			fw.close();
		} else
		{
			System.err.println("开始从豆瓣网做最后的获取！！！");
		}
		return bl;
	}
	/*
	 * @豆瓣网上第一次详细匹配抓取仍然失败后，在豆瓣网上做最后的图书信息的获取，，获取第一个查询到的信息并储存在txt文件中
	 */
	public void dbOne(String bookname, String zz,FileWriter fw,ExportWord ew) throws IOException
	{
		String dbStr = "http://book.douban.com/subject_search?search_text=" + bookname + " " + zz + "&cat=1001";
		String dbht = "";
		try
		{
			dbht = tools.getContent(dbStr).replace("&middot;", "・").replace("（", "[").replace("）", "]");
		} catch (IOException e1)
		{
			System.err.println("获取豆瓣网查询结果列表的源码失败！！" + dbStr);
			e1.printStackTrace();
		}
		String db = tools.getStrByREX(dbht, "<div class=\"pl2\">.+?<div class=\"star clearfix\">");
		String dburl = tools.getStrByREX(db, "<a[^>]+>.+?</a>");
		System.out.println(bookname + ":" + dburl);
		if (dburl != null)
		{
			dburl = tools.getLinkTagFromLinkStr(dburl, dbStr).getLink();
			String htmString = "";
			try
			{
				htmString = tools.getContent(dburl).replace("&middot;", "・").replace("（", "[").replace("）", "]");
			} catch (IOException e)
			{
				System.err.println("获取豆瓣网第一查询结果的详细信息的源码失败！！" + dburl);
				e.printStackTrace();
			}
			// 获取图书信息
			String zuozhe = tools.getStrByREX(htmString, "作者</span>:.+?<br\\s*/>");
			String yizhe = tools.getStrByREX(htmString, "译者</span>:.+?<br\\s*/>");
			if(yizhe!=null&&!yizhe.trim().equals(""))
			zuozhe += yizhe;
			if (zuozhe == null || zuozhe.trim().equals(""))
				zuozhe = zz;
			zuozhe = tools.Html2Text(zuozhe).replace("作者:", "").replace("\n", "").replace("\r", "").replaceAll("\\s+", "").trim();
			ew.zuozhe = zuozhe;
			String chuban = tools.getStrByREX(htmString, "出版社:.+?<br\\s*/>.+?<br\\s*/>");
			if (chuban != null&&!chuban.trim().equals(""))
				chuban = tools.Html2Text(chuban).replaceAll("\\s*", "").replace("出版社:", "").replace("出版年:", "  ").trim();
			ew.chubanxiang = chuban;
			String isbn = tools.getStrByREX(htmString, "ISBN:.+?<br\\s*/>");
			if (isbn != null&&!isbn.trim().equals(""))
				isbn = tools.Html2Text(isbn).replace("ISBN:", "").trim();
			ew.isbn = isbn;
			String yeshu = tools.getStrByREX(htmString, "页数:.+?<br\\s*/>");
			if (yeshu != null&&!yeshu.trim().equals(""))
				yeshu = tools.getStrByREX(yeshu, "\\d{1,}");
			ew.yeshu = yeshu;
			String jjie = tools.getStrByREX(htmString, "<div class=\"related_info\">.+?<br\\s*/>\\s*</div>");
			jjie = tools.getStrByREX(jjie, "<div class=\"indent\">.+?<br\\s*/>\\s*</div>");
			if (jjie!= null&&!jjie.trim().equals(""))
			{
				jjie = tools.Html2Text(jjie).replaceAll("(\\s*((<BR>)|(<br\\s*/>))\\s*){1,}", "\n").trim();
				if(jjie.length()>=200){
					ew.neirongtiyao = jjie.substring(0, 190)+"...";
				}else{
					ew.neirongtiyao = jjie.substring(0, jjie.length());
				}
			}
			ew.zt = "需认真检查！！";
			// 将信息写入txt文档
			fw.write("图书名：" + bookname + "\r\n图书网作者：" + zz + "\r\n豆瓣网作者：" + zuozhe + "\r\n图书出版项：" + chuban + "\r\n图书isbn:" + isbn + "\r\n图书页数:" + yeshu + "\r\n\r\n");
		}else
		{
			fw.write("####信息获取失败：" + bookname + "\r\n\r\n");
		}
		fw.close();
	}
	/*
	 * @从豆瓣读书网站中获取图书的页数信息
	 */
	public void dbmulu(String bookname, String zz, ExportWord ew,FileWriter fw) throws IOException
	{
		String dbStr = "http://book.douban.com/subject_search?search_text=" + bookname + " " + zz + "&cat=1001";
		String dbht = "";
		boolean bl = false;
		try
		{
			dbht = tools.getContent(dbStr).replace("&middot;", "・").replace("（", "[").replace("）", "]");
		} catch (IOException e1)
		{
			System.err.println("获取豆瓣网查询结果列表的源码失败！！" + dbStr);
			e1.printStackTrace();
		}
		ArrayList<String> dblist = tools.getByREX(dbht, "<div class=\"pl2\">.+?<div\\s*[^>]+collect_form[^>]+>");
		for (int db = 0; db < dblist.size(); db++)
		{
			String z1 = dblist.get(db).replaceAll("\\s+", "");
			z1 = tools.Html2Text(z1);
			String z2 = zz.replaceAll("((（.+?）)|(\\[.+?\\]))", "").replaceAll("・.+", "").trim();
			String z3 = zz.replaceAll(".+・", "");
			hanzi2pinyin hp = new hanzi2pinyin();
			String z4 = hp.toPinyin(z3);
			String z5 = hp.toPinyin(z1);
			if (z1.contains(z2) && z1.contains(bookname) && z1.contains(z3) && z5.contains(z4))
			{
				String dburl = tools.getStrByREX(dblist.get(db), "<a[^>]+>.+?</a>");
				dburl = tools.getLinkTagFromLinkStr(dburl, dbStr).getLink();
				String htmString = "";
				try
				{
					htmString = tools.getContent(dburl).replace("&middot;", "・").replace("（", "[").replace("）", "]");
				} catch (IOException e)
				{
					System.err.println("获取豆瓣网查询结果的详细信息的源码失败！！" + dburl);
					e.printStackTrace();
				}
				// 获取图书信息
				String yeshu = "";
				String jjie = "";
				if(ew.yeshu==null){
					yeshu = tools.getStrByREX(htmString, "页数:.+?<br\\s*/>");
					if(yeshu == null)
						fw.write("@@@@@从豆瓣中获取到图书的页数失败："+bookname+"\r\n\r\n");
					ew.yeshu = yeshu;
				}
				if(ew.neirongtiyao==null){
					jjie = tools.getStrByREX(htmString, "<div class=\"related_info\">.+?<br/>\\s*</div>");
					if (jjie!=null && !jjie.trim().equals("")){
						jjie = tools.getStrByREX(jjie, "<div class=\"indent\">.+?</div>");
						jjie = tools.Html2Text(jjie).replaceAll("(\\s*((<BR>)|(<br\\s*/>))\\s*){1,}", "\n").trim();
						if(jjie.length()>=200){
							ew.neirongtiyao = jjie.substring(0, 190)+"...";
						}else{
							ew.neirongtiyao = jjie.substring(0, jjie.length())+"...";
						}
					}else {
						fw.write("%%%%%%从豆瓣中获取图书简介时失败："+bookname+"\r\n\r\n");
					}
				}
				if((yeshu!=null || jjie !=null)&&(!yeshu.trim().equals("") || !jjie.trim().equals("")) )
					bl = true;
				break;
			}
			
		}
		if (bl)
		{
			System.out.println("豆瓣网获取“图书的页数”成功！！！");
			fw.close();
		} else
		{
			fw.write("@@@@@从豆瓣中获取到图书的页数失败："+bookname+"\r\n\r\n");
		}
	}

	public String jianjiechuli(String jjie){
		jjie = overtxt(jjie);
		if (jjie!=null&&!jjie.equals(""))
		jjie = jjie.replaceAll(".+?简\\s*介\\s*((：)|(:))\\s*", "")
				.replaceAll("作者\\s*((:)|(：))", "").trim();
		if (jjie!=null && !jjie.trim().equals("")){
			if(jjie.length()>=200){
				jjie = jjie.substring(0,190);
				if (jjie.endsWith("\n"))
				{
					jjie = jjie.substring(0,189)+"...";
				}else {
					jjie = jjie+"...";
				}
			}else{
				jjie = jjie.substring(0, jjie.length());
			}
		}
		return jjie;
	}
	public String overtxt (String txt){
		if (txt!=null && !txt.trim().equals("")){
			txt = txt.replaceAll("((◉)|(☉)|(⊙)|(◎)|(＊)|(￢)|(¬)|(◤)|(◥)|(◢)|(◣)|(░)|(▒)|(▓)|(☍)|(☋)|(☌)|(↓)" +
					"|(↑)|(↔)|(®)|(©)|(※)|(●)|(■)|(□)|(☆)|(△)|(▽)|(▼)|(★)|(\\*)|(◇)|(◆)|(>{2,}))", "")
					.replace("&nbsp;","").replace("&amp;","&")
					.replaceAll("((&#8198;)|(&#61482;)|(&#8202)|(&#9673;)|(&middot;))", "")
					.replaceAll("(&amp;#8226;)|(&#8226;)|(&#8231;)", "·")
					.replace("&#12316;", "~")
					.replace("&#8943;", "⋯").replace("&oslash;", "ø")
					.replaceAll("((<br\\s*/>)|(<BR>))\\s*…{1,}", "")
//					.trim().replaceAll("(()|( )", "")		//英文空格，汉字空格，和位置字符空格
					.replaceAll("(<br\\s*/\\s*>)(\\s*·\\s*)\\d{1,}(\\s*·\\s*)(<br\\s*/\\s*>)", "")
					.replaceAll("(<br\\s*/\\s*>)(\\s*-\\s*)\\d{1,}(\\s*-\\s*)(<br\\s*/\\s*>)", "")
				    .replaceAll("(<br\\s*/\\s*>)(\\s*·\\s*){1,}", "")
				    .replaceAll("(<br\\s*/\\s*>)(\\s*-\\s*){1,}", "")
				    .replaceAll("(<br\\s*/\\s*>)(\\s*-\\s*).+?<br\\s*/\\s*>.+?<br\\s*/\\s*>", "")
				    .replaceAll("(<br\\s*/\\s*>)\\s*青崖白鹿记.+?(<br\\s*/\\s*>)", "")
				    .replaceAll("[^\\d]\n", "")
					.replaceAll("<[^>]+?id=.+?>\\s*", "")
					.replaceAll("（[^（）]+?http[^（）]+?）", "").trim();
			String zhang="";
//			ArrayList<String> zhang1 = tools.getByREX(txt, "<br/>[^>]{1})<br/>");
//			ArrayList<String> zhang1 = tools.getByREX(txt, "<br/>第.{1,3}((章)|(卷)|(节)).{0,20}?<br/>");
//			ArrayList<String> zhang1 = tools.getByREX(txt, "<br/>\\[.+?\\].{0,20}?<br/>");
			ArrayList<String> zhang1 = tools.getByREX(txt, "<br />[^>]*\\d{1,2}．");
			System.out.println("章节个数："+zhang1.size());
			if(zhang1!=null&&zhang1.size()!=0){
				for (int j = 0; j < zhang1.size(); j++)
				{
					zhang = zhang1.get(j).trim();
					System.out.println(zhang);
					String jie = tools.getStrByREX(zhang, "第.{1,3}节");
					String juan = tools.getStrByREX(zhang, "第.{1,3}卷");
					String zhz = tools.getStrByREX(zhang, "第.{1,3}章");
					String jz = tools.getStrByREX(zhang, "\\[.+?\\]");
					String zz="";
					if(jie!=null&&!jie.trim().equals("")){
						zz = "【节】"+tools.Html2Text(zhang);
					}else if(juan!=null&&!juan.trim().equals("")){
						zz = "【章】"+tools.Html2Text(zhang);
					}else if(zhz!=null&&!zhz.trim().equals("")){
						zz = "【章】"+tools.Html2Text(zhang);
					}else if(jz!=null&&!jz.trim().equals("")){
						zz = "【章】"+tools.Html2Text(zhang);
					}else {
						zz = "【章】"+tools.Html2Text(zhang);
					}
//					txt = txt.replace(zhang,zz);
				}
			}
			txt = txt.replaceAll("(\\s*((<br>)|(<BR>)|(<br\\s*/>)|(</h4>))\\s*){1,}", "\n");
			txt = tools.Html2Text(txt)
					.replace("&lt;", "<").replace("&gt;", ">")
					.replace("%0%", "")
					.replace("　", "")
					.replace("○", "")
					.replace("查看原图", "").trim();
		}
		return txt;
	}
	public  boolean rexName (ArrayList<String> al,String bookUrl){
		boolean result=true;
		String []notName={"宣传","广告","专家","编辑","编者","介绍","名家","读者","本书","媒体","买点","推荐","目录","内容","插图","图）"}; 
		for(int n=0; n<al.size(); n++){
			String rexName = tools.getLinkTagFromLinkStr(al.get(n), bookUrl).getAttribute("linktxt");
			rexName = tools.Html2Text(rexName).replaceAll("\\s+", "");
			for (int t = 0; t < notName.length; t++)
			{
				if(rexName.contains(notName[t])){
					result = true;
					break;
				}else {
					result = false;
				}
			}
			if(result==false)
			{
				System.out.println(" 这本书需要加第一部分的信息");
				break;
			}
		}
		return result;
	}
	
	public boolean zhcnameRex(String zhcname){
		boolean rex=false;
		zhcname = zhcname.replaceAll("\\(.+", "").replaceAll("（.+", "").trim();
		if (zhcname.contains("宣传")||zhcname.contains("广告")||zhcname.contains("专家")||zhcname.contains("编辑")
				||zhcname.contains("名家")||zhcname.contains("读者")||zhcname.contains("本书")||zhcname.contains("媒体")
				||zhcname.contains("买点")||zhcname.contains("卖点")||zhcname.contains("推荐")
				||zhcname.contains("目录")||zhcname.contains("插图")
				||zhcname.contains("内容")||zhcname.contains("介绍")||zhcname.contains("编者"))
		{
				rex = true;
		}
		return rex;
	}
}
