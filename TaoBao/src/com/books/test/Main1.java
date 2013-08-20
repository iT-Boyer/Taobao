package com.books.test;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;

import org.nutz.dao.Cnd;

import com.forfuture.tools.Tools;

import net.aboutbooks.gethtmlsource.FromDangdang;
import net.aboutbooks.gethtmlsource.FromDouban;
import net.aboutbooks.gethtmlsource.FromZhusanjiao;
import net.aboutbooks.pipei.Pipei;
import net.aboutbooks.pipei.PipeiThrad;
import net.aboutbooks.pojo.AboutBook;
import net.books.pojo.Books_Info;
import net.rile.sql.DataBaseDao;

public class Main1 {

	/**
	 * @param args
	 * @throws IOException 
	 */
	public static void main(String[] args) throws IOException 
	{
		PipeiThrad pp=new PipeiThrad();
//		pp.ZHUSANJIAO=false;  //�ر������ǲ�ѯ  �����ܹرյ��������꣬��ر��Ǹ��ͽ���Ӧ����Ϊfalse
		AboutBook ab=new AboutBook();  //�ȶ���һ��aboutbook����߷�������ץȡ����Ϣ������Ǹ����֣��ͽ��Ǹ������ϡ�
		ab.setName("�ղ�");  //������name����������Ҳ�ͻ�������������֡�
		ab.setChubanshe("���ĳ�����");    //��仰ע�͵��ˣ�Ҳ����˵û�ж�ab���ó����磬�Ͳ���Գ��������ƽ���ˣ������Գ��������֡�
		ab.setZuozhe("������");       //��������Ҳ��������
		ab.setIsbn("20667638");
		ArrayList<AboutBook> abbs=pp.comparByXSDList(ab);
		System.out.println("��ѽ��");
		for (Iterator iterator = abbs.iterator(); iterator.hasNext();) {
			AboutBook abb = (AboutBook) iterator.next();
			if(abb!=null)
				System.out.println(abb.fenshu+":"+abb.website+":"+abb.getName());
		}
	}
	
}
