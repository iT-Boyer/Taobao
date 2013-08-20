package net.books.pojo;

import org.nutz.dao.entity.annotation.ColDefine;
import org.nutz.dao.entity.annotation.ColType;
import org.nutz.dao.entity.annotation.Column;
import org.nutz.dao.entity.annotation.Id;
import org.nutz.dao.entity.annotation.Name;
import org.nutz.dao.entity.annotation.Table;

@Table("tbPress")
public class Press {
	@Column
	@Id
	private int id;
	@Column("省市")
	@ColDefine(type=ColType.VARCHAR,width=100)
	private String province;
	@Column("版社")
//	@Name			//指定字段的唯一性:unique
	@ColDefine(type=ColType.VARCHAR,width=100)
	private String press;
	public int getId() {
		return id;
	}
	public String getPress() {
		return press;
	}
	public String getProvince() {
		return province;
	}
	public void setId(int id) {
		this.id = id;
	}
	public void setPress(String press) {
		this.press = press;
	}
	public void setProvince(String province) {
		this.province = province;
	}


}
