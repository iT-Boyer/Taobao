package net.books.UU8;

import java.io.File;

public class ����ָ���ļ����е������ļ� {
	private static int size = 0;
 public static void main(String[] args) throws Exception {
  //�ݹ���ʾC���������ļ��м������ļ�
  File root = new File("E:/UUs8/�½��ļ��� (3)/�½��ļ���/8");
  showAllFiles(root);
  System.out.println("����"+size+"���ļ�");
 }
 
 final static void showAllFiles(File dir) throws Exception{
  File[] fs = dir.listFiles();
  size +=fs.length;
  for(int i=0; i<fs.length; i++){
	  System.out.println(fs[i].getAbsolutePath());//��ȡ����·����e:/uus8/��ͨ��/�й���ʷ�ĺ���.doc
	  System.out.println(fs[i].getName()); 	//��ȡ�ļ������й���ʷ�ĺ���.doc
   if(fs[i].isDirectory()){			//�ݹ�ķ�����ȡ���ļ����е�����ͼ��������Ϣ
    try{
     showAllFiles(fs[i]);
    }catch(Exception e){}
   }
  }
 }
}
