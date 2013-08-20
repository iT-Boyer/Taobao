package net.books.pojo;

import org.nutz.dao.entity.annotation.ColDefine;
import org.nutz.dao.entity.annotation.ColType;
import org.nutz.dao.entity.annotation.Column;
import org.nutz.dao.entity.annotation.Id;
import org.nutz.dao.entity.annotation.Table;

@Table("tbBooks")
public class Books
{
	@Column
	@Id
	private int id;
	@Column("书名")
	@ColDefine(type = ColType.VARCHAR,width=500)
	private String bookname;
	@Column("封面名字")  //封面图片的本地存储路径
	@ColDefine(type =ColType.VARCHAR,width=500)
	private String fengmian;
	@Column("封面url")  //封面图片的网络地址
	@ColDefine(type =ColType.VARCHAR,width=500)
	private String fengmianUrl;
	@Column("作者")
	@ColDefine(type = ColType.VARCHAR,width=150)
	private String author;
	@Column("出版项")
	@ColDefine(type=ColType.VARCHAR,width=500)
	private String chubanxiang;
	@Column("ISBN")
	@ColDefine(type=ColType.VARCHAR,width=500)
	private String ISBN;
	@Column("页数")
	@ColDefine(type=ColType.INT)
	private int pagesum;
	@Column("内容提要")
	@ColDefine(type=ColType.TEXT)
	private String Summary;
	public String getZt()
	{
		return zt;
	}
	public void setZt(String zt)
	{
		this.zt = zt;
	}
	@Column("源网站url")
	@ColDefine(type=ColType.VARCHAR,width=500)
	private String url;
	@Column("正确率")
	@ColDefine(type=ColType.VARCHAR,width=20)
	private String zt;
	
	public String getFengmianUrl() {
		return fengmianUrl;
	}

	public void setFengmianUrl(String fengmianUrl) {
		this.fengmianUrl = fengmianUrl;
	}

	public int getId()
	{
		return id;
	}
	
	public void setId(int id)
	{
		this.id = id;
	}
	public String getBookname()
	{
		return bookname;
	}
	public void setBookname(String bookname)
	{
		this.bookname = bookname;
	}
	public String getFengmian()
	{
		return fengmian;
	}
	public void setFengmian(String fengmian)
	{
		this.fengmian = fengmian;
	}
	public String getAuthor()
	{
		return author;
	}
	public void setAuthor(String author)
	{
		this.author = author;
	}
	public String getChubanxiang()
	{
		return chubanxiang;
	}
	public void setChubanxiang(String chubanxiang)
	{
		this.chubanxiang = chubanxiang;
	}
	public String getISBN()
	{
		return ISBN;
	}
	public void setISBN(String iSBN)
	{
		ISBN = iSBN;
	}
	public int getPagesum()
	{
		return pagesum;
	}
	public void setPagesum(int pagesum)
	{
		this.pagesum = pagesum;
	}
	public String getSummary()
	{
		return Summary;
	}
	public void setSummary(String summary)
	{
		Summary = summary;
	}
	public String getUrl()
	{
		return url;
	}
	public void setUrl(String url)
	{
		this.url = url;
	}
	
}
