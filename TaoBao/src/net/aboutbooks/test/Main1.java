package net.aboutbooks.test;

import java.util.ArrayList;
import java.util.Iterator;

import net.aboutbooks.gethtmlsource.FromDangdang;
import net.aboutbooks.gethtmlsource.FromDouban;
import net.aboutbooks.gethtmlsource.FromZhusanjiao;
import net.aboutbooks.pipei.Pipei;
import net.aboutbooks.pipei.PipeiThrad;
import net.aboutbooks.pojo.AboutBook;

public class Main1 {

	/**
	 * @param args
	 */
	public static void main(String[] args) 
	{
		PipeiThrad pp=new PipeiThrad();
		pp.ZHUSANJIAO=false;  //�ر������ǲ�ѯ  �����ܹرյ��������꣬��ر��Ǹ��ͽ���Ӧ����Ϊfalse
//		pp.DOUBAN=false;
		pp.DANGDANG=false;
		AboutBook ab=new AboutBook();  //�ȶ���һ��aboutbook����߷�������ץȡ����Ϣ������Ǹ����֣��ͽ��Ǹ������ϡ�
		ab.setName("��ˮ�");  //������name����������Ҳ�ͻ�������������֡�
//		ab.setChubanshe("���ĳ�����");    //��仰ע�͵��ˣ�Ҳ����˵û�ж�ab���ó����磬�Ͳ���Գ��������ƽ���ˣ������Գ��������֡�
//		ab.setZuozhe("������");       //��������Ҳ��������
//		ab.setIsbn("20667638");
//		ArrayList<AboutBook> abbs=pp.comparByXSDList(ab);
		System.out.println("��ѽ��");
//		for (Iterator iterator = abbs.iterator(); iterator.hasNext();) {
//			AboutBook abb = (AboutBook) iterator.next();
//			if(abb!=null)
//				System.out.println(abb.fenshu+":"+abb.website+":"+abb.getName()+"\n"+abb.getJianjie());
//		}
		AboutBook abb = pp.comparByXSD(ab);
		System.out.println("���ݼ�飤����������������������������"+abb.getJianjie());
		
	}
	
}
