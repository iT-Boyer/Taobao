package net.books.pojo;

import org.nutz.dao.entity.annotation.ColDefine;
import org.nutz.dao.entity.annotation.ColType;
import org.nutz.dao.entity.annotation.Column;
import org.nutz.dao.entity.annotation.Id;
import org.nutz.dao.entity.annotation.One;
import org.nutz.dao.entity.annotation.Table;

@Table("tbBooksZT")
public class BooksZT 
{
	@Column("author")
	@ColDefine(type=ColType.VARCHAR,width=50)
	private String author;
	@Column("BookName")
	@ColDefine(type=ColType.VARCHAR,width=500)
	private String BookName;
	@Column("BookURL")
	@ColDefine(type=ColType.VARCHAR,width=500)
	private String BookURL;
	@Column
	@Id
	private int id;
	@Column("ImgURL")
	@ColDefine(type=ColType.VARCHAR,width=500)
	private String ImgURL;
	@Column("Press")
	@ColDefine(type=ColType.VARCHAR,width=500)
	private String Press;
	@Column("State")
	@ColDefine(type=ColType.VARCHAR,width=500)
	private String state;
	public String getAuthor() {
		return author;
	}
	public String getBookName() {
		return BookName;
	}
	public String getBookURL()
	{
		return BookURL;
	}
	public int getId() {
		return id;
	}
	public String getImgURL() {
		return ImgURL;
	}
	public String getPress() {
		return Press;
	}
	public String getState()
	{
		return state;
	}
	public void setAuthor(String author) {
		this.author = author;
	}
	public void setBookName(String bookName) {
		BookName = bookName;
	}
	public void setBookURL(String BookURL)
	{
		this.BookURL = BookURL;
	}
	public void setId(int id) {
		this.id = id;
	}
	public void setImgURL(String imgURL) {
		ImgURL = imgURL;
	}
	public void setPress(String press) {
		Press = press;
	}
	public void setState(String state)
	{
		this.state = state;
	}
}
