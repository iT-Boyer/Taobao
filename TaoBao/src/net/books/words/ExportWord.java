package net.books.words;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;

import net.books.pojo.Books;
import net.books.pojo.BooksZT;
import net.books.pojo.Books_Info;
import net.hsg.tools.HsgTools;
import net.rile.sql.DataBaseDao;

import org.nutz.dao.Chain;
import org.nutz.dao.Cnd;

import bsh.This;

import com.forfuture.tools.Tools;
import com.lowagie.text.Document;
import com.lowagie.text.DocumentException;
import com.lowagie.text.Element;
import com.lowagie.text.Font;
import com.lowagie.text.FontFactory;
import com.lowagie.text.Image;
import com.lowagie.text.PageSize;
import com.lowagie.text.Paragraph;
import com.lowagie.text.pdf.BaseFont;
import com.lowagie.text.rtf.RtfWriter2;
import com.lowagie.text.rtf.style.RtfFont;

public class ExportWord 
{
	public float zuoyou=63.5f;
	public float shangxia=79.25f;
	public String fileroot;
	//
	public int id ;
	public String shuming;
	public String fengmian;
	public String zuozhe;
	public String chubanxiang;
	public String isbn;
	public String yeshu;
	public String zt;
	public String neirongtiyao;
	public String banbenshuoming;
	public String url;
	public Document document;
	public  FileWriter fwtxt;
	public Font key;
	public Font text;
	public Tools tools=new Tools();
    public void addHeader()throws DocumentException, IOException
    {
        //设置纸张大小
        document=new Document(PageSize.A4, zuoyou, zuoyou, shangxia, shangxia);
        //建立一个书写器，与document对象关联
        if(fileroot!=null)
        {
        	String folder=fileroot.substring(0,fileroot.lastIndexOf("/"));
            File f=new File(folder);
            f.mkdirs();
        }
        fwtxt=new FileWriter(fileroot.replace("doc","txt"));
        RtfWriter2.getInstance(document, new FileOutputStream(fileroot));
        document.open();
        //设置关键字的字体
        BaseFont bfkey = BaseFont.createFont("STSongStd-Light","UniGB-UCS2-H",BaseFont.NOT_EMBEDDED);
        key=new Font(bfkey,11,Font.NORMAL);
        //设置正文的字体
        BaseFont bftext = BaseFont.createFont("STSongStd-Light","UniGB-UCS2-H",BaseFont.NOT_EMBEDDED);
        text=new Font(bftext,11,Font.NORMAL);
//        this.text=FontFactory.getFont("宋体","UniGB-UCS2-H",BaseFont.NOT_EMBEDDED);
//        this.text.setSize(10.5f);
//        this.key=this.text;
        //设置书名字体和书名
        Paragraph context = new Paragraph();
        context.setAlignment(Element.ALIGN_LEFT);
        context.setFont(key);
        context.add("【书名】");
        context.setFont(text);
        context.add(shuming);
        document.add(context);
        fwtxt.write(shuming+"\r\n");
        //
        Paragraph context2 = new Paragraph();
        context2.setAlignment(Element.ALIGN_LEFT);
        context2.setFont(key);
        context2.add("【封面】\n");
        Image png=Image.getInstance(fengmian);
        png.scaleAbsolute(85,140);   		//设置图片长宽的绝对大小
//      png.scaleToFit(85, 140);			//根据原图片长宽比例缩放
        context2.add(png);
        document.add(context2);
        fwtxt.write(fengmian+"\r\n");
        //
        Paragraph context3 = new Paragraph();
        context3.setAlignment(Element.ALIGN_LEFT);
        context3.setFont(key);
        context3.add("【作者】");
        context3.setFont(text);
        context3.add(zuozhe);
        document.add(context3);
        fwtxt.write(zuozhe+"\r\n");
        //
        Paragraph context4 = new Paragraph();
        context4.setAlignment(Element.ALIGN_LEFT);
        context4.setFont(key);
        context4.add("【出版项】");
        context4.setFont(text);
        context4.add(chubanxiang);
        document.add(context4);
        fwtxt.write(chubanxiang+"\r\n");
        //
        Paragraph context5 = new Paragraph();
        context5.setAlignment(Element.ALIGN_LEFT);
        context5.setFont(key);
        context5.add("【ISBN】");
        context5.setFont(text);
        context5.add(isbn);
        document.add(context5);
        fwtxt.write(isbn+"\r\n");
        //
        Paragraph context6 = new Paragraph();
        context6.setAlignment(Element.ALIGN_LEFT);
        context6.setFont(key);
        context6.add("【页数】");
        context6.setFont(text);
        context6.add(yeshu);
        document.add(context6);
        fwtxt.write(yeshu+"\r\n");
        //
        Paragraph context7 = new Paragraph();
        context7.setAlignment(Element.ALIGN_LEFT);
        context7.setFont(key);
        context7.add("【内容提要】");
        context7.setFont(text);
        context7.add(neirongtiyao);
        document.add(context7);
        fwtxt.write(neirongtiyao+"\r\n");
        
        //       
        Paragraph context10 = new Paragraph();
        context10.setAlignment(Element.ALIGN_LEFT);
        context10.setFont(key);
        context10.add("【版本说明】");
        context10.setFont(text);
        context10.add(banbenshuoming);
        document.add(context10);
        fwtxt.write(banbenshuoming+"\r\n");
        //
        Paragraph context8 = new Paragraph();
        context8.setAlignment(Element.ALIGN_LEFT);
        context8.setFont(key);
        context8.add("【源网站URL】");
        context8.setFont(text);
        context8.add(url);
        document.add(context8);
        fwtxt.write(url+"\r\n");
        //
        Paragraph context11 = new Paragraph();
        context11.setAlignment(Element.ALIGN_LEFT);
        context11.setFont(key);
        context11.add("【BEGIN】");
        document.add(context11);
    	fwtxt.write("[begin]"+"\r\n");
        
    }
    public void close() throws Exception
    {
    	Paragraph context9 = new Paragraph();
        context9.setAlignment(Element.ALIGN_LEFT);
        context9.setFont(key);
        context9.add("【END】");
        document.add(context9);
        document.close();
        DataBaseDao dbd=new DataBaseDao();
        dbd.update(Books_Info.class,Chain.make("进度", zt),Cnd.where("id","=",id));//更新图书信息表"进度"字段的更新的操作状态
        dbd.update(Books_Info.class,Chain.make("YY出版项", chubanxiang),Cnd.where("id","=",id));
        dbd.update(Books_Info.class,Chain.make("内容提要", neirongtiyao),Cnd.where("id","=",id));//更新图书信息表"内容提要"字段的更新的操作状态
      	fwtxt.write("[end]"+"\r\n");
    	fwtxt.close();
    }
    public void addZhang(String title) throws Exception
    {
      Paragraph context10 = new Paragraph();
      context10.setAlignment(Element.ALIGN_LEFT);
      context10.setFont(key);
      context10.add("【章】");
      context10.setFont(text);
      context10.add(title);
      document.add(context10);
      fwtxt.write("【章】"+title+"\r\n");
    }
    public void addJie(String title) throws Exception
    {
//      Paragraph context11 = new Paragraph();
//      context11.setAlignment(Element.ALIGN_LEFT);
//      context11.setFont(key);
//      context11.add("【节】");
//      context11.setFont(text);
//      context11.add(title);
//      document.add(context11);
//      fwtxt.write("【节】"+title+"\r\n");
    }
    public void addParse(String parse) throws Exception
    {
      Paragraph context12 = new Paragraph();
      context12.setAlignment(Element.ALIGN_LEFT);
      context12.setFont(key);
      context12.add("【部】");
      context12.setFont(text);
      context12.add(parse);
      document.add(context12);
      fwtxt.write("【部】"+parse+"\r\n");
    }
    public void addText(String txt) throws Exception
    {
    	Paragraph context11 = new Paragraph();
        context11.setAlignment(Element.ALIGN_LEFT);
        context11.setFont(text);
//        context11.setFirstLineIndent(10);//缩进一个汉字
        context11.setFirstLineIndent(20);//缩进两个汉字
        context11.add(txt);
        document.add(context11);
    }
    public void clean()
    {
    	this.shuming=null;
    	this.fengmian=null;
    	this.zuozhe=null;
    	this.chubanxiang=null;
    	this.isbn=null;
    	this.yeshu=null;
    	this.neirongtiyao="";
    	this.banbenshuoming="";
    	this.zt="";
    	this.url=null;
    }
    
    public static void main(String[] args) throws Exception {
        ExportWord word = new ExportWord();
        word.fileroot = "e:/aaa/test.doc";
        word.shuming="民国才女风景";
        word.zuozhe="陈学勇编著";
        word.chubanxiang="人民教育出版社";
        word.fengmian="http://cover.duxiu.com/cover/Cover.dll?iid=6566636B6769636A6665A09B3136313134363534";  //封面图片可以是url，也可以是本地图片路径
        word.isbn="978-7-80706-905-8";
        word.yeshu="254";
        word.neirongtiyao="这些民国女性作家，皆不是以文学为武器的左翼斗士，但远远望去，她们对文学的执著，下笔的严肃，则与以文学战士自命的革命文学家无异。不论是被五四震上文坛的前辈冰心，还是退居书斋一隅的新秀杨绛，即如好像不闻世事的另类女作家张爱玲，她们无不热切地关注民生，真切地体验人世，关注和体验都反映在她们的作品里。这种创作态度的严肃和执著，或许为今日标榜“身体写作”或“游戏人生”的年轻写家们所不屑，我却有敬意而热爱。 ";
        word.url="http://www.yzsc.com.cn/read.aspx?bookid=1411238&rpid=10";
        word.addHeader(); //将上边定义的头部信息添加到word中
        //开始增加正文和章节
        Tools tools = new Tools();
        HsgTools hsgTools = new HsgTools();
        String url = "http://www.uus8.com/book/html/heji/43/13/09.htm";
        String html = tools.getContentOld(url, "gbk");
        html = tools.getStrByREX(html, "<p id=\"zoom\">.+?((<font color=\"#ffffff\">)|(</p>))");
//        html =html.replaceAll("(\\s*((<br>)|(BR)|(<br\\s*/>)|(</h4>))\\s*){1,}", "\n");
//        html = html.trim().replace("　", "").replaceAll("(\\s*((<br>)|(BR)|(<br\\s*/>)|(</h4>))\\s*){1,}", "\n    ").replaceAll("<p id=.+?>\\s*", "    ");
//        html = html.trim().replaceAll("(\\s*((<br>)|(BR)|(<br\\s*/>)|(</h4>))\\s*)|(<p id=.+?>){1,}", "\n    ");
        html = hsgTools.overtxt(html);
            System.out.println(html);
//        String partname = tools.getStrByREX(html, "<h1>.+?</h1>");
//        System.out.println(partname);
//        partname = tools.Html2Text(partname);
//        for(int i=0;i<2;i++)
//        {
//        	word.addZhang("第"+i+"章");
//        	for(int j=0;j<1;j++)
//        	{
//        		word.addJie("第"+j+"节");
//        		word.addText("这些民国女性作家，皆不是以文学为武器的左翼斗士，但远远望去，她们对文学的执著，下笔的严肃，则与以文学战士自命的革命文学家无异。不论是被五四震上文坛的前辈冰心，还是退居书斋一隅的新秀杨绛，即如好像不闻世事的另类女作家张爱玲，她们无不热切地关注民生，真切地体验人世，关注和体验都反映在她们的作品里。这种创作态度的严肃和执著，或许为今日标榜“身体写作”或“游戏人生”的年轻写家们所不屑，我却有敬意而热爱。 ");
//        	}
//        }
        word.addText(html);
        word.close();
    }
}