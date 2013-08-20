package net.books.UU8;

import java.io.File;

public class 遍历指定文件夹中的所有文件 {
	private static int size = 0;
 public static void main(String[] args) throws Exception {
  //递归显示C盘下所有文件夹及其中文件
  File root = new File("E:/UUs8/新建文件夹 (3)/新建文件夹/8");
  showAllFiles(root);
  System.out.println("共有"+size+"个文件");
 }
 
 final static void showAllFiles(File dir) throws Exception{
  File[] fs = dir.listFiles();
  size +=fs.length;
  for(int i=0; i<fs.length; i++){
	  System.out.println(fs[i].getAbsolutePath());//获取绝对路径：e:/uus8/已通过/中国历史的后门.doc
	  System.out.println(fs[i].getName()); 	//获取文件名：中国历史的后门.doc
   if(fs[i].isDirectory()){			//递归的方法获取子文件夹中的所有图书的相关信息
    try{
     showAllFiles(fs[i]);
    }catch(Exception e){}
   }
  }
 }
}
