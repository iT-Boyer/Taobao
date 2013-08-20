package net.books.UU8;

import java.io.File;
import java.util.List;

import org.nutz.dao.Chain;
import org.nutz.dao.Cnd;

import net.books.pojo.Books_Info;
import net.rile.sql.DataBaseDao;
public class ����ָ���ļ����е������ļ����ƶ���ָ�����ļ����� {
	
	 private static String fileUrl;
	 private static int t = 0;
	 private static int tt = 0;
	 private static DataBaseDao dbd;
	 private static  List<Books_Info> books_Infos;
	 private static String zt = "�½�ȱ��";  //����״̬��־��ָ����Ҫ�½����ļ�����
 public static void main(String[] args) throws Exception {
  //�ݹ���ʾC���������ļ��м������ļ�
	 dbd = new DataBaseDao();
	 books_Infos = dbd.query(Books_Info.class, Cnd.where("����", "like", "%"+zt+"%")
			 										.and("��������", "like", "%����������%"), null);
	 for (int i = 0; i < books_Infos.size(); i++)
	{
		 fileUrl = "E:/UUs8/"+books_Infos.get(i).getFengmian();
	}
	 System.out.println(fileUrl);
  File root = new File(fileUrl);
  showAllFiles(root);
 }

 final static void showAllFiles(File dir) throws Exception{
	 System.out.println("��ǰͼ��ĸ�����"+books_Infos.size());
  File[] fs = dir.listFiles();
  tt+=fs.length;
  for(int i=0; i<fs.length; i++){
//	  System.out.println(fs[i].getAbsolutePath());//��ȡ����·����e:/uus8/��ͨ��/�й���ʷ�ĺ���.doc
//	  System.out.println(fs[i].getName()); 	//��ȡ�ļ������й���ʷ�ĺ���.doc
	  for (int j = 0; j < books_Infos.size(); j++)
	{
		  String fileName = fs[i].getName().replaceAll("((\\.txt)|(\\.doc))", "").trim();
		  String bookName = books_Infos.get(j).getBookname().trim();
		if (fileName.equals(bookName))
		{
			File fold = new File(fs[i].getAbsolutePath());//ĳ·���µ��ļ�
			   String strNewPath = fileUrl+"/"+zt+"/";//��·��
			   File fnewpath = new File(strNewPath);
			   if(!fnewpath.exists())
			     fnewpath.mkdirs();
			   File fnew = new File(strNewPath+fold.getName());
			   fold.renameTo(fnew);
			System.out.println("����ͼ�飺"+fileName);
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
