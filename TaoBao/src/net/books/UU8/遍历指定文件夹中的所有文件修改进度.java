package net.books.UU8;

import java.io.File;
import java.util.List;

import org.nutz.dao.Chain;
import org.nutz.dao.Cnd;

import net.books.pojo.Books_Info;
import net.rile.sql.DataBaseDao;
public class 遍历指定文件夹中的所有文件修改进度 {
 public static void main(String[] args) throws Exception {
  //递归显示C盘下所有文件夹及其中文件
  File root = new File("E:/UUs8/tttttttttttttt/15/30");
  showAllFiles(root);
 }
 private static int t = 0;
 private static int tt = 0;
 final static void showAllFiles(File dir) throws Exception{
	 DataBaseDao dbd=new DataBaseDao();
	 List<Books_Info> books_Infos = dbd.query(Books_Info.class, null, null);
	 System.out.println("当前图书的个数："+books_Infos.size());
  File[] fs = dir.listFiles();
  tt+=fs.length;
  for(int i=0; i<fs.length; i++){
//	  System.out.println(fs[i].getAbsolutePath());//获取绝对路径：e:/uus8/已通过/中国历史的后门.doc
//	  System.out.println(fs[i].getName()); 	//获取文件名：中国历史的后门.doc
	  for (int j = 0; j < books_Infos.size(); j++)
	{
		  String fileName = fs[i].getName().replace(".doc", "").trim();
		  String bookName = books_Infos.get(j).getBookname().trim();
		  int bookId = books_Infos.get(j).getId();
		if (fileName.equals(bookName))
		{
			System.out.println("处理图书："+fileName);
			dbd.update(Books_Info.class, Chain.make("进度", "30"), Cnd.where("id", "=", bookId));
			t++;
			break;
		}
	}
   if(fs[i].isDirectory()){			//递归的方法获取子文件夹中的所有图书的相关信息
    try{
     showAllFiles(fs[i]);
    }catch(Exception e){}
   }
  }
  System.out.println("文件夹中图书的个数："+tt+"  %%%%%匹配到的数据条数："+t);
 }
}
