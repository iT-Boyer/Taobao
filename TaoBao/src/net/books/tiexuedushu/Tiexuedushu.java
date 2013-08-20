package net.books.tiexuedushu;

import java.io.IOException;
import com.forfuture.tools.Tools;

public class Tiexuedushu 
{
	Tools tools=new Tools();
	public void start(String url) throws IOException
	{
		String html=tools.getContent(url);
		String bookName=tools.Html2Text(tools.getStrByREX(html,"NovelTitle.+?;")).replace("NovelTitle = '","").replace(";","");
		String sumber=tools.Html2Text(tools.getStrByREX(html,"<p class=\"bookp\">.+?</div>")).trim();
		String picUrl=tools.getSrcFromImgtag(tools.getStrByREX(html,"<img[^<>]*BookCoverPic[^<>]*>"), url);
		System.out.println(bookName);
		System.out.println(sumber);
		System.out.println(picUrl);
	}
}
