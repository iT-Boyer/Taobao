package net.ztoolss.single;

import java.io.IOException;
import java.net.URLEncoder;
import org.apache.james.mime4j.message.SingleBody;
import com.forfuture.tools.Tools;

public class Main
{
	/**
	 * @param args
	 * @throws IOException 
	 */
	public static void main(String[] args) throws IOException
	{
		Tools tools=new Tools();
		String sw="好妈妈胜过好老师";
		String url="http://books.gdlink.net.cn/search?sw={sw}&allsw=&bCon=&ecode=GBK&channel=search&Field=1";
		sw=URLEncoder.encode(sw,"gb2312");
		url=url.replace("{sw}",sw);
		url=tools.getContent(url);
		url=tools.getStrByREX(url,"<input[^<>]*url0[^<>]*>").replace("amp;","").replace("input","a").replace("value=","href=")+"</a>"; //将里边的amp字符去掉，将将input换掉，将这个<input>标签换成一个<a>标签，然后将value换成href，就能通过函数取出里边的连接了。
		url=tools.getLinkTagFromLinkStr(url,"").getLink();
		System.out.println(url);
		System.out.println(tools.getContent(url));
	}
}
