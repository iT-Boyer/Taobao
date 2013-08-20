package net.books.console;

import java.util.ArrayList;

import com.forfuture.singlets.SingletThings;

public class main {

	/**
	 * @param args
	 */
	public static void main(String[] args) 
	{
		for(int i=0;i<1000;i++)
		{
			myThread mt=new myThread();
			mt.setI(i);
			//
			ThreadControl.yanchi();  //调用ThreadControl的延迟函数
			//
			mt.start();  //启动线程
		}
	}
}
