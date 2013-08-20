package net.aboutbooks.pojo;

public class AboutBook 
{
	private String name="";
	private String zuozhe="";
	private String isbn="";
	private String chubanshe="";
	private String yeshu="";
	private String chubannian="";
	private String yizhe="";
	private String jianjie;
	private String imgSrc;
	public double  fenshu;
	public String website;
	public String getImgSrc() {
		return imgSrc;
	}
	public void setImgSrc(String imgSrc) {
		this.imgSrc = imgSrc;
	}
	public String getJianjie() {
		return jianjie;
	}
	public void setJianjie(String jianjie) {
		this.jianjie = jianjie;
	}
	public String getYizhe() {
		return yizhe;
	}
	public void setYizhe(String yizhe) {
		this.yizhe = yizhe;
	}
	public String getChubannian() {
		return chubannian;
	}
	public void setChubannian(String chubannian) {
		this.chubannian = chubannian;
	}
	public String getYeshu() {
		return yeshu;
	}
	public void setYeshu(String yeshu) {
		this.yeshu = yeshu;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) 
	{
		if(name!=null)
			name=name.replace("&middot;",".");
		this.name = name;
	}
	public String getZuozhe() {
		return zuozhe;
	}
	public void setZuozhe(String zuozhe) 
	{
		if(zuozhe!=null&&!zuozhe.trim().equals(""))
			zuozhe=zuozhe.replace("&middot;",".");
		this.zuozhe = zuozhe;
	}
	public String getIsbn() {
		return isbn;
	}
	public void setIsbn(String isbn) {
		this.isbn = isbn;
	}
	public String getChubanshe() {
		return chubanshe;
	}
	public void setChubanshe(String chubanshe) {
		this.chubanshe = chubanshe;
	}
	
}
