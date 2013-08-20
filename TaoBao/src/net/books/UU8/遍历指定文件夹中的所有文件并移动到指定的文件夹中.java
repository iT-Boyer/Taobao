package net.books.UU8;

import java.io.File;
import java.util.List;

import org.nutz.dao.Chain;
import org.nutz.dao.Cnd;

import net.books.pojo.Books_Info;
import net.rile.sql.DataBaseDao;
public class 遍历指定文件夹中的所有文件并移动到指定的文件夹中 {
	
	 private static String fileUrl;
	 private static int t = 0;
	 private static int tt = 0;
	 private static DataBaseDao dbd;
	 private static  List<Books_Info> books_Infos;
	 private static String zt = "章节缺下";  //根据状态标志，指定需要新建的文件夹名
 public static void main(String[] args) throws Exception {
  //递归显示C盘下所有文件夹及其中文件
	 dbd = new DataBaseDao();
	 books_Infos = dbd.query(Books_Info.class, Cnd.where("进度", "like", "%"+zt+"%")
			 										.and("封面名字", "like", "%世界作家作%"), null);
	 for (int i = 0; i < books_Infos.size(); i++)
	{
		 fileUrl = "E:/UUs8/"+books_Infos.get(i).getFengmian();
	}
	 System.out.println(fileUrl);
  File root = new File(fileUrl);
  showAllFiles(root);
 }

 final static void showAllFiles(File dir) throws Exception{
	 System.out.println("当前图书的个数："+books_Infos.size());
  File[] fs = dir.listFiles();
  tt+=fs.length;
  for(int i=0; i<fs.length; i++){
//	  System.out.println(fs[i].getAbsolutePath());//获取绝对路径：e:/uus8/已通过/中国历史的后门.doc
//	  System.out.println(fs[i].getName()); 	//获取文件名：中国历史的后门.doc
	  for (int j = 0; j < books_Infos.size(); j++)
	{
		  String fileName = fs[i].getName().replaceAll("((\\.txt)|(\\.doc))", "").trim();
		  String bookName = books_Infos.get(j).getBookname().trim();
		if (fileName.equals(bookName))
		{
			File fold = new File(fs[i].getAbsolutePath());//某路径下的文件
			   String strNewPath = fileUrl+"/"+zt+"/";//新路径
			   File fnewpath = new File(strNewPath);
			   if(!fnewpath.exists())
			     fnewpath.mkdirs();
			   File fnew = new File(strNewPath+fold.getName());
			   fold.renameTo(fnew);
			System.out.println("处理图书："+fileName);
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
