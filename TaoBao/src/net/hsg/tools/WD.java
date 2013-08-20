package net.hsg.tools;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.ie.InternetExplorerDriver;


public class WD
{
	private static WD singleClass=new WD();
	private  WebDriver driver;  
	public WD()
	{
//		driver = new InternetExplorerDriver();\
		driver=new FirefoxDriver();
	}
	public static WD getWD()
	{
		if(singleClass==null)
		{
			singleClass=new WD();
		}
		return singleClass;
	}

	public WebDriver getWebBrowser()
	{
		if(driver==null)
		{
			driver = new InternetExplorerDriver();
		}
		return driver;
	}

	public static void setSingleClass(WD singleClass)
	{
		WD.singleClass = singleClass;
	}
	public static String getContent(String url)
	{
		WD.getWD().driver.get(url);
		String html=WD.getWD().driver.getPageSource();
		if(html==null||html.trim().equals(""))
		{
			System.err.println("获取到的正文为空###################"+url);
			try {
				Thread.sleep(500);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			html=WD.getContent(html);
		}
		return html;
	}
}
