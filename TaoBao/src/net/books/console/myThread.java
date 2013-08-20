package net.books.console;

public class myThread extends Thread 
{
	private int i;
	
	public int getI() {
		return i;
	}

	public void setI(int i) {
		this.i = i;
	}

	public void run()
	{
		System.out.println(i+"我来了");
		try {
			sleep(2000+i);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		System.out.println(i+"我走了");
	}
}
