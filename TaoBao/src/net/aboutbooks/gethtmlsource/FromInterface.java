package net.aboutbooks.gethtmlsource;

import java.util.ArrayList;

import net.aboutbooks.pojo.AboutBook;
import net.hsg.tools.Tools;

public interface FromInterface
{
	public Tools tools=new Tools();
	public ArrayList<AboutBook> getBybookName(String bookName,int maxNum);
}
