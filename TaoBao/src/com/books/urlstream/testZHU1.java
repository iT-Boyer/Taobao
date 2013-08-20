package com.books.urlstream;

import com.forfuture.urlstream.testThread;

public class testZHU1 {

	/**
	 * @param args
	 */
	public static void main(String[] args)
	{
		// TODO Auto-generated method stub
		for(int i=0;i<100;i++)
		{
			System.out.println("---:"+i);
			testThread tt=new testThread();
			tt.setName(String.valueOf(i));
			tt.start();
		}
	}
}
