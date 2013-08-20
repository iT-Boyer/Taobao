package com.books.test;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;

import com.forfuture.autoconfig.dto.newspaperdecr;

import jxl.read.biff.BiffException;
import net.exexcel.readexcels.ReadExcel;
import net.rile.sql.DataBaseDao;


public class main2 {

	/**
	 * @param args
	 * @throws IOException 
	 * @throws BiffException 
	 * @throws ParseException 
	 */
	public static void main(String[] args) throws BiffException, IOException 
	{
		ReadExcel re=new ReadExcel("E:/tbbooks_info.xls");
		for (int i = 1; i < 1800; i++) 
		{
			//re.read(X,Y); x是第几列，从0开始。y是第几行，也是从0开始；
			String str=re.read(1, i);
			System.out.println(str);
		}
	}
}
