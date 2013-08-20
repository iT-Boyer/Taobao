package net.books.ebookwebsite;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import com.forfuture.tools.Tools;

public class 铁血读书 
{
	Tools tools=new Tools();
	public void run()
	{
		String url="http://book.tiexue.net/Download.aspx?NovelID=17341";
		try 
		{
			String html=tools.getContent(url);
//			html=tools.getStrByREX(html,"<a name=#797454>.+?<DIV id=\"Div1\" class=\"sitebar\" style=\"BORDER-BOTTOM:0px\">");
			FileWriter fw=new FileWriter(new File("d:/dushu.txt"));
			fw.write(html);
			fw.flush();
			fw.close();
		} 
		catch (IOException e) 
		{
			e.printStackTrace();
		}
	}
}
