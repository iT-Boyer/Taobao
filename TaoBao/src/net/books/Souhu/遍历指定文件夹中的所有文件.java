package net.books.Souhu;

import java.io.File;
public class ����ָ���ļ����е������ļ� {
 public static void main(String[] args) throws Exception {
  //�ݹ���ʾC���������ļ��м������ļ�
  File root = new File("E:/UUs8/��ͨ��");
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
