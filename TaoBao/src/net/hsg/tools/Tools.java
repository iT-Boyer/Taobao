/**
 * 
 */
package net.hsg.tools;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.zip.DataFormatException;

import org.htmlparser.Parser;
import org.htmlparser.filters.NodeClassFilter;
import org.htmlparser.lexer.Page;
import org.htmlparser.tags.ImageTag;
import org.htmlparser.tags.LinkTag;
import org.htmlparser.util.NodeList;
import org.htmlparser.util.ParserException;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import com.brooksandrus.utils.swf.SWFDecompressor;
import com.forfuture.autoconfig.dto.myNode;
import com.forfuture.autoconfig.dto.newspaperdecr;
import net.hsg.tools.HTMLDecoder;
import com.forfuture.urlstream.CharTools;
import com.forfuture.urlstream.DetectEcoding;
import com.forfuture.urlstream.urlconnector;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.HtmlPage;

/**
 * @author ZHAO
 * 一些小工具集合
 */
public class Tools
{	 
//	private  WebClient wc=new WebClient();
	public Tools()
	{
//		wc.setJavaScriptEnabled(false);
	}
	public Document getDocument(String url) throws IOException
	{
		Document doc=null;
		try 
		{
			doc = Jsoup.connect(url).get();
		} catch (IOException e) 
		{
			doc = Jsoup.connect(url).get();
		}
		return doc;
	}
	/**
	 * 获取img标签中的Src属性的值，并且放回该图片的绝对路径
	 * @param tagstr
	 * @param baseurl
	 * @return
	 */
	public String getSrcFromImgtag(String tagstr,String baseurl)
	{
		ImageTag lt=new ImageTag();
		lt.setText(tagstr);
		lt.getPage().setBaseUrl(baseurl);
		try
		{
			return new String(lt.getImageURL());
		}
		catch (Exception e)
		{
			
			return null;
		}
		
	}
	/**
	 * 获取img标签中的Src属性的值，并且放回该图片的绝对路径
	 * @param tagstr
	 * @param baseurl
	 * @return
	 */
	public String getSrcFromImgtag2(String tagstr,String baseurl)
	{
		ImageTag lt=new ImageTag();
		tagstr=getStrByREX(tagstr,"<img.+?>");
		lt.setText(tagstr);
		lt.getPage().setBaseUrl(baseurl);
		try
		{
			return new String(lt.getImageURL());
		}
		catch (Exception e)
		{
			
			return null;
		}
	}
	/**
	 * 获取img标签中的Src属性的值，并且放回该图片的绝对路径
	 * @param tagstr
	 * @param baseurl
	 * @return
	 */
	public LinkedList<String> getImgSrcFromHtml(String html,String rex,String baseurl)
	{
		ArrayList<String> arrayList=getByREX(html,rex);
		LinkedList<String> linkedList=new LinkedList<String>();
		for (Iterator iterator = arrayList.iterator(); iterator.hasNext();) 
		{
			String string = (String) iterator.next();
			linkedList.add(getSrcFromImgtag2(string,baseurl));
		}
		return linkedList;
	}
	public ImageTag getImgtag(String tagstr,String baseurl)
	{
		ImageTag lt=new ImageTag();
		lt.setText(tagstr);
		lt.getPage().setBaseUrl(baseurl);
		return lt;
		
	}
	/**
	 * 获取a标签中的href属性的值，并返回该超链接的绝对路径
	 * @param linkStr
	 * @param baseurl
	 * @return
	 */
	public LinkTag getLinkTagFromLinkStr(String linkStr,String baseurl)
	{
		LinkTag lt=new LinkTag();
		lt.setText(linkStr);
		lt.getPage().setBaseUrl(baseurl);
		lt.setAttribute("linktxt",Html2Text(linkStr));
		return lt;
	}
	/**
	 * 获取a标签中的href属性的值，并返回该超链接的绝对路径
	 * @param linkStr
	 * @param baseurl
	 * @return
	 */
	public LinkTag getLinkTagFromLinkStr2(String linkStr,String baseurl)
	{
		LinkTag lt=new LinkTag();
		linkStr=this.getStrByREX(linkStr,"<a.+?</a\\s*>");
		lt.setText(linkStr);
		lt.getPage().setBaseUrl(baseurl);
		lt.setAttribute("linktxt",Html2Text(linkStr));
		return lt;
	}
	/**
	 * 获取a标签中的href属性的值，并返回该超链接的绝对路径
	 * @param html 要过滤的网页
	 * @param rex 链接的正则
	 * @param baseurl 网页的网址
	 * @return
	 */
	public LinkedList<LinkTag> getLinkTagFromHtml(String html,String rex,String baseurl)
	{
		ArrayList<String> arrayList=getByREX(html,rex);
		LinkedList<LinkTag> linkedList=new LinkedList<LinkTag>();
		for (Iterator iterator = arrayList.iterator(); iterator.hasNext();) 
		{
			String string = (String) iterator.next();
			linkedList.add(getLinkTagFromLinkStr2(string,baseurl));
		}
		return linkedList;
	}
	/**
	 * 由相对路径获取绝对路径
	 * @param xiangduiPath 需要处理的相对路径
	 * @param sourceUrl 此相对路径所在的页面的链接
	 * */
	public String getJueduiPath(String xiangduiPath,String sourceUrl)
	{
		String arg0="<a href=\"{link}\" id=\"234324\"></a>";
		LinkTag tag=new LinkTag();
		tag.setText(arg0.replace("{link}", xiangduiPath));
		tag.getPage().setBaseUrl(sourceUrl);
		try
		{
			return new String(tag.getLink());
		} 
		catch (Exception e)
		{
			return null;
		}
	}
	/**
	 * 由一个含有相对路径的<a></a>获取绝对路径
	 * @param xiangduiPath 需要处理的相对路径
	 * @param sourceUrl 此相对路径所在的页面的链接
	 * */
	public String getJueduiPathByTag(String xiangduiPath,String sourceUrl)
	{
		LinkTag tag=new LinkTag();
		tag.setText(xiangduiPath);
		tag.getPage().setBaseUrl(sourceUrl);
		try
		{
			return new String(tag.getLink());
		} 
		catch (Exception e)
		{
			return null;
		}
	}
	/**
	 * 获取一个页面上所有的链接
	 * @throws ParserException 如果不能获取网页时要抛出这个异常
	 * @param url 要获取页面的网址
	 * @param ecoding 页面的编码方式
	 * */
	public NodeList getLinkTags(String url,String ecoding) throws ParserException
	{
		Parser parser=new Parser();
		parser.setURL(url);
		parser.setEncoding(ecoding);
		NodeClassFilter nodefilter=new NodeClassFilter(LinkTag.class);    //先建立一个过滤<a></a>标签的过滤器
		NodeList nodeList = parser.extractAllNodesThatMatch(nodefilter);
		return nodeList;
	}
	/**
	 * 是用urlconnector获取一个页面上所有的链接
	 * @throws ParserException 如果不能获取网页时要抛出这个异常
	 * @param url 要获取页面的网址
	 * @param ecoding 页面的编码方式
	 * */
	public NodeList getLinkTagsByUrlconnector(String url,String ecoding) throws ParserException
	{
		urlconnector uc=new urlconnector();
		uc.setEcoding(ecoding);
		System.out.println(uc.getContent(url));
		Page page=new Page(uc.getContent(url));
		page.setBaseUrl(url);
		Parser parser=new Parser();
		parser.getLexer().setPage(page);
		parser.setEncoding(ecoding);
		NodeClassFilter nodefilter=new NodeClassFilter(LinkTag.class);    //先建立一个过滤<a></a>标签的过滤器
		NodeList nodeList = parser.extractAllNodesThatMatch(nodefilter);
		return nodeList;
	}
	/**
	 * 是用urlconnector获取一个页面上所有的链接
	 * @throws ParserException 如果不能获取网页时要抛出这个异常
	 * @param url 要获取页面的网址
	 * @param ecoding 页面的编码方式
	 * */
	public NodeList getLinkTagsByHtmlSource(String html,String baseUrl,String ecoding) throws ParserException
	{
//		urlconnector uc=new urlconnector();
//		uc.setEcoding(ecoding);
//		System.out.println(uc.getContent(url));
		Page page=new Page(html);
		page.setBaseUrl(baseUrl);
		Parser parser=new Parser();
		parser.getLexer().setPage(page);
		parser.setEncoding(ecoding);
		NodeClassFilter nodefilter=new NodeClassFilter(LinkTag.class);    //先建立一个过滤<a></a>标签的过滤器
		NodeList nodeList = parser.extractAllNodesThatMatch(nodefilter);
		return nodeList;
	}
	/**
	 * 有一个使用过的Parser获取一个页面上所有的链接
	 * @throws ParserException 如果不能获取网页时要抛出这个异常
	 * @param url 要获取页面的网址
	 * */
	public NodeList getLinkTagsByParser(Parser oldParser) throws ParserException
	{
		Parser parser=new Parser();
		parser.getLexer().setPage(oldParser.getLexer().getPage());
		NodeClassFilter nodefilter=new NodeClassFilter(LinkTag.class);    //先建立一个过滤<a></a>标签的过滤器
		NodeList nodeList = parser.extractAllNodesThatMatch(nodefilter);
		return nodeList;
	}
	/**
	 * 获取一个页面上所有图片的链接
	 * @throws ParserException 如果不能获取网页时要抛出这个异常
	 * @param url 要获取页面的网址
	 * @param ecoding 页面的编码方式
	 * */
	public NodeList getImageTags(String url,String ecoding) throws ParserException
	{
		Parser parser=new Parser();
		parser.setURL(url);
		parser.setEncoding(ecoding);
		NodeClassFilter nodefilter=new NodeClassFilter(ImageTag.class);    //先建立一个过滤<a></a>标签的过滤器
		NodeList nodeList = parser.extractAllNodesThatMatch(nodefilter);
		return nodeList;
	}
	/**
	 * 获取一个页面上所有图片的链接
	 * @throws ParserException 如果不能获取网页时要抛出这个异常
	 * @param url 要获取页面的网址
	 * */
	public NodeList getImageTagByParser(Parser oldParser) throws ParserException
	{
		Parser parser=new Parser();
		parser.getLexer().setPage(oldParser.getLexer().getPage());
		NodeClassFilter nodefilter=new NodeClassFilter(ImageTag.class);    //先建立一个过滤<a></a>标签的过滤器
		NodeList nodeList = parser.extractAllNodesThatMatch(nodefilter);
		return nodeList;
	}
	/** 下载文件
	 * @param strUrl 要下载的文件的地址
	 * @param fileRoute要存放在硬盘上的地址
	 * @throws IOException 
	 * */
	public int saveFile(String strUrl,String Folder, String fileName) throws IOException 
	{
		try 
		{
		saveFile_old(strUrl,Folder, fileName);
		} 
		catch (IOException e) 
		{
			saveFile_old(strUrl,Folder, fileName);
		}
		return 2;
	}
//	public int saveFile_old(String strUrl,String Folder, String fileName) throws IOException
//	{
//		if(strUrl!=null)
//		{
//			strUrl=strUrl.replace(" ","%20");
//		}
//		CharTools charTools = new CharTools();
//		URL url = null;
//		InputStream is = null;
//		OutputStream os = null;
//		String FolderName = Folder;
//		strUrl=charTools.Utf8URLencode(strUrl);
//		url = new URL(strUrl);
//		is = url.openStream();
//		File f = new File(FolderName);
//		f.mkdirs();
//		os = new FileOutputStream(FolderName.trim() + "/" + fileName.trim());
//		java.io.ByteArrayOutputStream baos =new java.io.ByteArrayOutputStream();  //内存缓存字节流
//		int bytesRead = 0;
//		byte[] buffer = new byte[8192];
//		while ((bytesRead = is.read(buffer, 0, 8192)) != -1)
//		{
//			baos.write(buffer, 0, bytesRead);
//		}
//		os.write(baos.toByteArray(),0,baos.toByteArray().length);
//		return 2;
//	}
	/**
	 *修改部分文件下载时防止盗链的方法 
	 */
	public int saveFile_old(String strUrl,String Folder, String fileName) throws IOException
	{
		if(strUrl!=null)
		{
			strUrl=strUrl.replace(" ","%20");
		}
		CharTools charTools = new CharTools();
		URL url = null;
		InputStream is = null;
		OutputStream os = null;
		String FolderName = Folder;
		strUrl=charTools.Utf8URLencode(strUrl);
		url = new URL(strUrl);
		HttpURLConnection urlcon=(HttpURLConnection) url.openConnection();
		urlcon.addRequestProperty("referer", strUrl);
//		is = url.openStream();
		is=urlcon.getInputStream();
		File f = new File(FolderName);
		f.mkdirs();
		os = new FileOutputStream(FolderName.trim() + "/" + fileName.trim());
		java.io.ByteArrayOutputStream baos =new java.io.ByteArrayOutputStream();  //内存缓存字节流
		int bytesRead = 0;
		byte[] buffer = new byte[8192];
		while ((bytesRead = is.read(buffer, 0, 8192)) != -1)
		{
			baos.write(buffer, 0, bytesRead);
		}
		os.write(baos.toByteArray(),0,baos.toByteArray().length);
		return 2;
	}

	/**
	 * 从一个标志子符截取字符串，这个是截取标志字符之前的字符串
	 * @param string 要操作的字符串
	 * @param flag 标志字符
	 * */
	public String  getFrontStr(String string,String flag)throws Exception
	{
		if(!string.contains(flag))
			throw new Exception("IsNotContainsThisFlag");
		int x=string.indexOf(flag);
		try
		{
			return new String(string.substring(0,x+flag.length()-1));
		} 
		catch (Exception e)
		{
			return null;
		}
	}
	/**
	 * 从一个标志子符截取字符串，这个是截取标志字符之后的字符串
	 * @param string 要操作的字符串
	 * @param flag 标志字符
	 * */
	public String  getAftertStr(String string,String flag)throws Exception
	{
		if(!string.contains(flag))
			throw new Exception("IsNotContainsThisFlag");
		int x=string.indexOf(flag);
		try
		{
			return new String(string.substring(x+flag.length()));
		}
		catch (Exception e)
		{
			return null;
		}
	}
	/**
	 * 找出两个字符之间的字符串
	 * @param string 要查询的字符串
	 * @param frontFlag 前一个字符串
	 * @param afterFlag 后一个字符串
	 * */
	public String getMiddleStr(String string,String frontFlag,String afterFlag)throws Exception
	{
		int x=string.indexOf(frontFlag);
		int y=string.indexOf(afterFlag,x);
		if(x<0||y<0||x>y)
			throw new Exception("IsError");
		try
		{
			return new String(string.substring(x+frontFlag.length(),y));
		} 
		catch (Exception e)
		{
			return null;
		}
	}
	/**
	 * 截取一个字符串中的字符的集合
	 * @param string 要处理的字符
	 * @param frontFlag 前一个字符串
	 * @param afterFlag 后一个字符串
	 * */
	public ArrayList<myNode> getListOfSplit(String string,String frontFlag,String afterFlag)
	{
		int x=0,y=0;
		ArrayList<myNode> al=new ArrayList<myNode>();
		while(true)
		{
			x=string.indexOf(frontFlag,x+1);
			y=string.indexOf(afterFlag,x);
			try
			{
				if(x>0&&y>0)
				{
					myNode mn=new myNode();
					mn.setTagStart(x);
					mn.setTagEnd(y);
					mn.setSourceText(string);
					mn.setText(string.substring(x+frontFlag.length(),y));
					al.add(mn);
				}
				else 
					return al;
			} 
			catch (Exception e)
			{
				return al;
			}
		}
	}
	/**
	 * 获取指定页面的html源码
	 * @param strUrl 指定的链接
	 * @param ecoding 指定的编码方式
	 * @throws IOException 
	 * */
	public  String getContent(String strUrl,String ecoding) throws IOException
	{
		if(strUrl!=null)
		{
			strUrl=strUrl.trim().replace(" ","%20");
		}
//		try
//		{
//			Document doc=Jsoup.connect(strUrl).get();
//			return doc.outerHtml();
//		} catch (Exception e)
//		{
			return getContentOld(strUrl,ecoding);
//		}
	}
	public  String getContent(String strUrl) throws IOException
	{

		if(strUrl!=null)
		{
			strUrl=strUrl.trim().replace(" ","%20");
		}
//		try
//		{
//			Document doc=Jsoup.connect(strUrl).get();
//			return doc.outerHtml();
//		} catch (Exception e)
//		{
			return getContentOld(strUrl,new DetectEcoding().getEcoding(strUrl));
//		}
	}
	public  String getContentByWebClient(String strUrl,String ecoding) throws IOException
	{
		if(strUrl!=null)
		{
			strUrl=strUrl.trim().replace(" ","%20");
		}
		try
		{
			WebClient wc=new WebClient();
			wc.setJavaScriptEnabled(true);
			HtmlPage hp=wc.getPage(strUrl);
			return new String(hp.getWebResponse().getContentAsString());
		} catch (Exception e)
		{
			return getContentOld(strUrl,ecoding);
		}
	}
	public  String getContentByWebClient(String strUrl) throws IOException
	{
		if(strUrl!=null)
		{
			strUrl=strUrl.trim().replace(" ","%20");
		}
		try
		{
			WebClient wc=new WebClient();
			wc.setJavaScriptEnabled(true);
			HtmlPage hp=wc.getPage(strUrl);
			return new String(hp.getWebResponse().getContentAsString());
		} catch (Exception e)
		{
			return getContentOld(strUrl,new DetectEcoding().getEcoding(strUrl));
		}
	}
	//
	public String getContentNew(String strUrl)
	{
		DetectEcoding de=new DetectEcoding();
		String ecoding=de.getEcoding(strUrl);
		System.err.println(ecoding);
		try {
			return getContentOld(strUrl,ecoding);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}
	}
	public  String getContentOld(String strUrl,String ecoding) throws IOException
	{
		if(strUrl!=null)
		{
			strUrl=strUrl.replace(" ","%20");
		}
			URL url = new URL(strUrl);
			URLConnection uc = url.openConnection();
			HttpURLConnection urlcon=(HttpURLConnection) url.openConnection();
			urlcon.addRequestProperty("referer", strUrl);
//			is = url.openStream();
			BufferedReader br = new BufferedReader(new InputStreamReader(urlcon.getInputStream(), ecoding));
			String s = "";
			StringBuffer sb = new StringBuffer("");
			while ((s = br.readLine()) != null)
			{
				sb.append(s + "\r\n");
			}
			br.close();
			try
			{
				return new String(sb.toString());
			} 
			catch (Exception e)
			{
				return null;
			}
	}
	/**
	 * 解决了繁体字的乱码的问题
	 * @param strUrl
	 * @param ecoding
	 * @return
	 * @throws IOException
	 */
	public  String getContent2Html(String strUrl) throws IOException
	{

		if(strUrl!=null)
		{
			strUrl=strUrl.trim().replace(" ","%20");
		}
		try
		{
			String html=getContentOld(strUrl, "utf-8");
			HTMLDecoder htmlDec = new HTMLDecoder();
			html = htmlDec.decode(html);
			return html;
		} catch (Exception e)
		{
			return getContentOld(strUrl,new DetectEcoding().getEcoding(strUrl));
		}
	}
	/**
	 * 获取指定链接的编码方式
	 * @param url 指定 的链接
	 * */
	public String getEcoding(String url)
	{
		DetectEcoding de=new DetectEcoding();
		de.setUrl(url);
		try
		{
			return new String(de.getEcoding());
		}
		catch (Exception e)
		{
			return null;
		}
	}
	/**
	 * 通过一个图片链接获取图片的名称
	 * */
	public  String getImgName(String url)
	{
		url=url.replace("\\", "/");
		if(url.contains("?"))
		{
			int i=url.indexOf("?");
			url=url.substring(0,i);
		}
		if(url.contains(".")||url.contains("/"))
		{
			int x=url.lastIndexOf("/");
			if(x<0)
				return "pageImage.jpg";
			return url.substring(x+1);
		}
		else
			return "pageImage.jpg";
	}
	/**
	 * 通过一个图片链接获取图片的扩展名
	 * */
	public  String getImgExtenName(String url)
	{
		if(url.contains("."))
		{
			int x=url.lastIndexOf(".");
			if(x<0)
				return ".jpg";
			return url.substring(x, url.length());
		}
		else
			return ".jpg";
	}
	/**
	 * 通过图片连接判断两个图片是否相同
	 * */
	public boolean isSame(String url1,String url2)
	{
		if(url1.contains("/")||url2.contains("/"))
		{
			url1=url1.substring(url1.lastIndexOf("/")+1, url1.length());
			url2=url2.substring(url2.lastIndexOf("/")+1, url2.length());
			if(url1.equals(url2))
				return true;
			else
				return false;
		}
		else
			return false;
	}
	/**
	 * 判断一个字符串是否不为空
	 * */
	private boolean isNull(String str)
	{
		if(str==null)
			return true;
		str=str.trim();
		if(str.equals("")||str==null)
			return true;
		else
			return false;
	}
	/**
	 * 祛除尖括号
	 */
	public String Html2Text(String inputString)
	{
		// TODO Auto-generated method stub
		if(inputString==null)
			return inputString;
		String htmlStr = inputString;//.substring(z, webStr.indexOf("<!--/enpcontent-->")+18);
		htmlStr = htmlStr.trim();
		htmlStr=new String(strReplaceREX(htmlStr,"&nbsp;",""));
		htmlStr=new String(strReplaceREX(htmlStr,"<\\s*/\\s*P\\s*>\\s*<\\s*P\\s*>",""));
		htmlStr=new String(strReplaceREX(htmlStr,"<\\s*P\\s*>\\s*<\\s*/\\s*P\\s*>",""));
		htmlStr=new String(strReplaceREX(htmlStr,"<\\s*script.+?<\\s*/\\s*script\\s*>",""));
		htmlStr=new String(strReplaceREX(htmlStr,"<\\s*!\\s*-\\s*-.+?-\\s*-\\s*>",""));
		htmlStr=new String(strReplaceREX(htmlStr,"<\\s*style.+?<\\s*/\\s*style\\s*>",""));
		htmlStr=new String(strReplaceREX(htmlStr,"<[^<>]*?>",""));
		try
		{
			return new String(htmlStr);
		} 
		catch (Exception e)
		{
			return null;
		}
	}
	//新添加的20101225
	//获取字符串中的图片
	public NodeList getImageTagList(String html,String baseUrl,String ecoding) throws ParserException
	{
		Page page=new Page(html);
		page.setBaseUrl(baseUrl);
		Parser parser=new Parser();
		parser.getLexer().setPage(page);
		parser.setEncoding(ecoding);
		NodeClassFilter nodefilter=new NodeClassFilter(ImageTag.class);    //先建立一个过滤<a></a>标签的过滤器
		NodeList nodeList = parser.extractAllNodesThatMatch(nodefilter);
		return nodeList;
	}
	/**
	 * 获取链接中的文件名
	 * */
	public String getFileNameOfUrl(String link)
	{
		link=link.replace(" ","");
		int i=link.lastIndexOf("/");
		if(i<0)
		{
			return new String(Double.toString(Math.random()));
		}
		else
		{
			link=new String(link.substring(i+1));
			if(link.contains("?"))
			{
				int j=link.indexOf("?");
				return new String(link.substring(0,j));
			}
			else 
			{
				return new String(link);
			}
		}
	}
	/**
	 * 使用正则表达式替换字符串
	 * */
	public String strReplaceREX(String baseCode, String rex, String replaceCode) 
	{
		if (baseCode == null||baseCode.trim().equals("")) 
		{
			return baseCode;
		}
		Pattern pat = Pattern.compile(rex, Pattern.CASE_INSENSITIVE+ Pattern.DOTALL);
		Matcher mat = pat.matcher(baseCode);
		String outcome = mat.replaceAll("");
		try
		{
			return new String(mat.replaceAll(replaceCode));
		} catch (Exception e)
		{
			return null;
		}
		}
	/**
	 * 使用正则表达式查找字符串
	 * */
	public ArrayList<String> getByREX(String baseCode, String rex)
	{
		ArrayList<String> outcome = new ArrayList<String>();
		if(baseCode==null||baseCode.trim().equals(""))
		{
			return outcome;
		}
		Pattern f1p = Pattern.compile(rex, Pattern.CASE_INSENSITIVE+ Pattern.DOTALL);
		Matcher f1m = f1p.matcher(baseCode);
		while (f1m.find())
		{
			outcome.add(f1m.group().trim());
		}
		return outcome;
	}
	/**
	 * 使用正则表达式查找字符串,返回一个找到的列表中的第一个结果
	 * */
	public String getStrByREX(String baseCode, String rex)
	{
		ArrayList<String> outcome = new ArrayList<String>();
		if(baseCode==null||baseCode.trim().equals(""))
		{
			return baseCode;
		}
		Pattern f1p = Pattern.compile(rex, Pattern.CASE_INSENSITIVE+ Pattern.DOTALL);
		Matcher f1m = f1p.matcher(baseCode);
		while (f1m.find())
		{
			outcome.add(f1m.group().trim());
		}
		try
		{
			return new String(outcome.get(0));
		}
		catch (Exception e)
		{
			return null;
		}
	}
	/**
	 * 格式化日期的
	 * */
	public String DateFormat(String dateSte,String InFormat,String OutFormat)
	{
		SimpleDateFormat simpleFormat = new SimpleDateFormat(InFormat);  
		SimpleDateFormat simpleFormatOut = new SimpleDateFormat(OutFormat);  
		Date date=null;
		try
		{
			date=simpleFormat.parse(dateSte);
		} catch (ParseException e)
		{
			e.printStackTrace();
			return dateSte;
		}
		return new String(simpleFormatOut.format(date));
	}
	/**
	 * 生成md5值
	 * @param str
	 * @return
	 */
	public static String md5(String str)
	{
		try
		{
			MessageDigest md = MessageDigest.getInstance("MD5");
		    md.update(str.getBytes());
		    byte b[] = md.digest();
		    int i;
		    StringBuffer buf = new StringBuffer("");
		    for (int offset = 0; offset < b.length; offset++) 
		    {
		    	i = b[offset];
		    	if (i < 0)
		    		i += 256;
		    	if (i < 16)
		    		buf.append("0");
		    	buf.append(Integer.toHexString(i));
		    }
		    str = buf.toString();
		    return new String(buf);
		}
		catch (NoSuchAlgorithmException e) 
		{
			return null;
		}
	}
	 public String  escape (String src)
	 {
	  int i;
	  char j;
	  StringBuffer tmp = new StringBuffer();
	  tmp.ensureCapacity(src.length()*6);

	  for (i=0;i<src.length() ;i++ )
	  {

	   j = src.charAt(i);

	   if (Character.isDigit(j) || Character.isLowerCase(j) || Character.isUpperCase(j))
	    tmp.append(j);
	   else
	    if (j<256)
	    {
	    tmp.append( "%" );
	    if (j<16)
	     tmp.append( "0" );
	    tmp.append( Integer.toString(j,16) );
	    }
	    else
	    {
	    tmp.append( "%u" );
	    tmp.append( Integer.toString(j,16) );
	    }
	  }
	  return new String(tmp.toString());
	 }

	 public String  descape (String src)
	 {
	  StringBuffer tmp = new StringBuffer();
	  tmp.ensureCapacity(src.length());
	  int  lastPos=0,pos=0;
	  char ch;
	  while (lastPos<src.length())
	  {
	   pos = src.indexOf("%",lastPos);
	   if (pos == lastPos)
	    {
	    if (src.charAt(pos+1)=='u')
	     {
	     ch = (char)Integer.parseInt(src.substring(pos+2,pos+6),16);
	     tmp.append(ch);
	     lastPos = pos+6;
	     }
	    else
	     {
	     ch = (char)Integer.parseInt(src.substring(pos+1,pos+3),16);
	     tmp.append(ch);
	     lastPos = pos+3;
	     }
	    }
	   else
	    {
	    if (pos == -1)
	     {
	     tmp.append(src.substring(lastPos));
	     lastPos=src.length();
	     }
	    else
	     {
	     tmp.append(src.substring(lastPos,pos));
	     lastPos=pos;
	     }
	    }
	  }
	  return new String(tmp.toString());
	 }
	 public String banmcl (String banm){
		 String [] ts={"・","&middot;"};
		 String [] zc={"·","·",};
		 for(int i=0;i<ts.length;i++){
			 banm.replace(ts[i], zc[i]);
		 }
		 return banm;
	 }
	 /**
		 *图片再swf文件中的
	 * @throws DataFormatException 
		 */
		public int saveFile_Swf(String strUrl,String Folder, String fileName) throws IOException, DataFormatException
		{
			if(strUrl!=null)
			{
				strUrl=strUrl.replace(" ","%20");
			}
			if(!strUrl.endsWith("swf"))
			{
				return saveFile(strUrl, Folder, fileName);
			}
			CharTools charTools = new CharTools();
			URL url = null;
			InputStream is = null;
			OutputStream os = null;
			String FolderName = Folder;
			strUrl=charTools.Utf8URLencode(strUrl);
			url = new URL(strUrl);
			HttpURLConnection urlcon=(HttpURLConnection) url.openConnection();
			urlcon.addRequestProperty("referer", strUrl);
//			is = url.openStream();
			is=urlcon.getInputStream();
			File f = new File(FolderName);
			f.mkdirs();
			os = new FileOutputStream(FolderName.trim() + "/" + fileName.trim().replace("swf", "jpg"));
			java.io.ByteArrayOutputStream baos =new java.io.ByteArrayOutputStream();  //内存缓存字节流
			int bytesRead = 0;
			byte[] buffer = new byte[8192];
			while ((bytesRead = is.read(buffer, 0, 8192)) != -1)
			{
				baos.write(buffer, 0, bytesRead);
			}
			byte[] picbyte=getPicFromSwf(baos.toByteArray());
			os.write(picbyte,0,picbyte.length);
			return 2;
		}
	 public byte[] getPicFromSwf(byte[] baos) throws DataFormatException
	 {
		byte[] aaas = new SWFDecompressor().uncompress(baos);
		ByteArrayOutputStream baos2=new ByteArrayOutputStream();
	    int start=0;
		int stop=0;
		for (int i=0;i<aaas.length-6;i++)
		{
			if(aaas[i]==(-1)&&aaas[i+1]==(-40))
			{
				start=i;
			}
			if(aaas[i]==(-1)&&aaas[i+1]==(-39))
			{
				stop=i+1;
			}
		}
		for(int i=start;i<=stop;i++)
		{
			baos2.write(aaas[i]);
		}
		return baos2.toByteArray();
	 }
}
