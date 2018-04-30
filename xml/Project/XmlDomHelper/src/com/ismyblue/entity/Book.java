package com.ismyblue.entity;


public class Book {
	
	private String title = null;	  
	private String lang = null;
	private String[] authors = null;
	private double  price= 0;
	private String year = null;
	
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getLang() {
		return lang;
	}
	public void setLang(String lang) {
		this.lang = lang;
	}
	public String[] getAuthors() {
		return authors;
	}
	public void setAuthors(String[] authors) {
		this.authors = authors;
	}
	public double getPrice() {
		return price;
	}
	public void setPrice(double price) {
		this.price = price;
	}
	public String getYear() {
		return year;
	}
	public void setYear(String year) {
		this.year = year;
	}
	
	@Override
	public String toString() {		
		StringBuffer stringBuffer = new StringBuffer();
		stringBuffer.append("[title:"+ title);
		stringBuffer.append(", lang:"+ lang);
		stringBuffer.append(", authors:"+ authors.toString());
		stringBuffer.append(", price:"+ price);
		stringBuffer.append(", year:"+ year + "] ");
		return stringBuffer.toString();
		
	}
	public Book(String title, String lang, String[] authors, double price, String year) {
		super();
		this.title = title;
		this.lang = lang;
		this.authors = authors;
		this.price = price;
		this.year = year;
	}
}
