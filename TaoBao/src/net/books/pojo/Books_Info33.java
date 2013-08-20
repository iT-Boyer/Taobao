package net.books.pojo;

import org.nutz.dao.entity.annotation.ColDefine;
import org.nutz.dao.entity.annotation.ColType;
import org.nutz.dao.entity.annotation.Column;
import org.nutz.dao.entity.annotation.Id;
import org.nutz.dao.entity.annotation.Table;

@Table("tbBooks_Info33")
public class Books_Info33
{
	@Column("进度")
	@ColDefine(type=ColType.VARCHAR,width=20)
	private String zt;
	@Column
	@Id
	private int id;
	@Column("书名")
	@ColDefine(type = ColType.VARCHAR,width=200)
	private String bookname;
	@Column("YY书名")
	@ColDefine(type = ColType.VARCHAR,width=200)
	private String YYbookname;
	@Column("作者")
	@ColDefine(type = ColType.VARCHAR,width=150)
	private String author;
	@Column("YY作者")
	@ColDefine(type = ColType.VARCHAR,width=150)
	private String YYauthor;
	@Column("源网站url")
	@ColDefine(type=ColType.VARCHAR,width=300)
	private String url;
	@Column("YY源网站url")
	@ColDefine(type=ColType.VARCHAR,width=300)
	private String YYurl;
	@Column("出版项")
	@ColDefine(type=ColType.VARCHAR,width=300)
	private String chubanxiang;
	@Column("YY出版项")
	@ColDefine(type=ColType.VARCHAR,width=300)
	private String YYchubanxiang;
	@Column("ISBN")
	@ColDefine(type=ColType.VARCHAR,width=100)
	private String ISBN;
	@Column("页数")
	@ColDefine(type=ColType.INT)
	private int pagesum;
	@Column("版本说明")
	@ColDefine(type=ColType.VARCHAR,width=100)
	private String imprint;
	@Column("内容提要")
	@ColDefine(type=ColType.TEXT)
	private String Summary;
	@Column("封面名字")  //封面图片的本地存储路径
	@ColDefine(type =ColType.VARCHAR,width=500)
	private String fengmian;
	@Column("封面url")  //封面图片的网络地址
	@ColDefine(type =ColType.VARCHAR,width=500)
	private String fengmianUrl;
	@Column("正文")
	@ColDefine(type=ColType.TEXT)
	private String Text;
	public String getZt()
	{
		return zt;
	}
	public void setZt(String zt)
	{
		this.zt = zt;
	}
	public String getText() {
		return Text;
	}
	public void setText(String text) {
		Text = text;
	}
	public String getFengmianUrl() {
		return fengmianUrl;
	}

	public void setFengmianUrl(String fengmianUrl) {
		this.fengmianUrl = fengmianUrl;
	}
	public String getImprint() {
		return imprint;
	}
	public void setImprint(String imprint) {
		this.imprint = imprint;
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
	public String getYYbookname() {
		return YYbookname;
	}
	public void setYYbookname(String yYbookname) {
		YYbookname = yYbookname;
	}
	public String getYYauthor() {
		return YYauthor;
	}
	public void setYYauthor(String yYauthor) {
		YYauthor = yYauthor;
	}
	public String getYYurl() {
		return YYurl;
	}
	public void setYYurl(String yYurl) {
		YYurl = yYurl;
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
	public String getYYchubanxiang() {
		return YYchubanxiang;
	}
	public void setYYchubanxiang(String yYchubanxiang) {
		YYchubanxiang = yYchubanxiang;
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
