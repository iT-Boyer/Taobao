package net.books.Souhu;

import java.io.File;
public class 遍历指定文件夹中的所有文件 {
 public static void main(String[] args) throws Exception {
  //递归显示C盘下所有文件夹及其中文件
  File root = new File("E:/UUs8/已通过");
  showAllFiles(root);
 }
 
 final static void showAllFiles(File dir) throws Exception{
  File[] fs = dir.listFiles();
  for(int i=0; i<fs.length; i++){
   System.out.println(fs[i].getAbsolutePath());
   if(fs[i].isDirectory()){
    try{
     showAllFiles(fs[i]);
    }catch(Exception e){}
   }
  }
 }
}
