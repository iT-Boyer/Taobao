package net.books.UU8;

import java.io.File;
import java.util.List;

import org.nutz.dao.Chain;
import org.nutz.dao.Cnd;

import net.books.pojo.Books_Info;
import net.rile.sql.DataBaseDao;
public class ����ָ���ļ����е������ļ��޸Ľ��� {
 public static void main(String[] args) throws Exception {
  //�ݹ���ʾC���������ļ��м������ļ�
  File root = new File("E:/UUs8/tttttttttttttt/15/30");
  showAllFiles(root);
 }
 private static int t = 0;
 private static int tt = 0;
 final static void showAllFiles(File dir) throws Exception{
	 DataBaseDao dbd=new DataBaseDao();
	 List<Books_Info> books_Infos = dbd.query(Books_Info.class, null, null);
	 System.out.println("��ǰͼ��ĸ�����"+books_Infos.size());
  File[] fs = dir.listFiles();
  tt+=fs.length;
  for(int i=0; i<fs.length; i++){
//	  System.out.println(fs[i].getAbsolutePath());//��ȡ����·����e:/uus8/��ͨ��/�й���ʷ�ĺ���.doc
//	  System.out.println(fs[i].getName()); 	//��ȡ�ļ������й���ʷ�ĺ���.doc
	  for (int j = 0; j < books_Infos.size(); j++)
	{
		  String fileName = fs[i].getName().replace(".doc", "").trim();
		  String bookName = books_Infos.get(j).getBookname().trim();
		  int bookId = books_Infos.get(j).getId();
		if (fileName.equals(bookName))
		{
			System.out.println("����ͼ�飺"+fileName);
			dbd.update(Books_Info.class, Chain.make("����", "30"), Cnd.where("id", "=", bookId));
			t++;
			break;
		}
	}
   if(fs[i].isDirectory()){			//�ݹ�ķ�����ȡ���ļ����е�����ͼ��������Ϣ
    try{
     showAllFiles(fs[i]);
    }catch(Exception e){}
   }
  }
  System.out.println("�ļ�����ͼ��ĸ�����"+tt+"  %%%%%ƥ�䵽������������"+t);
 }
}
