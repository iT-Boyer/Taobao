package com.books.test;

import java.util.HashMap;
import java.util.Iterator;

import net.aboutbooks.xiangsidu.LD;

import org.dyno.visual.swing.parser.listener.ThisClassModel;

public class Panduan 
{
	private Chubanshe chu;
	private LD ld;
	public Panduan()
	{
		chu=new Chubanshe();
		ld=new LD();
	}
	/**
	 * @param args
	 */
	public  String panduan(String name)
	{
		String rename=name;
		name=name.replace("出版社","");
		String re=chu.hm.get(name);
		if(re!=null)
			return re+rename;
		double fen=0;
		String maxFen=null;
		Iterator iterator = chu.hm.keySet().iterator();
		while (iterator.hasNext()) 
		{
			String key=(String) iterator.next();
			double temp_fen=ld.compare(name,key);
			if(temp_fen>fen)
			{
				fen=temp_fen;
				maxFen=key;
			}
		}
		return chu.hm.get(maxFen)+rename;
	}
}
