package net.books.console;

public class ThreadControl 
{
	private static int MaxThread=5;
	public static void yanchi()
	{
		while (isStart())   //用一个while循环将主线程暂停掉
		{
		}
	}
	public static boolean isStart()
	{
		int n=Thread.currentThread().activeCount(); //获取当前总线程数量
		if(n<MaxThread)  //判断是否允许生成新县城
		{
			return false;
		}
		else  //不允许生成新县城
		{

			try 
			{
				Thread.currentThread().sleep(500);  //先休眠0.5秒钟，不休眠的话会导致cpu使用率过高
			} 
			catch (InterruptedException e) 
			{
				e.printStackTrace();
			}
			return true;
		}
	}
}
