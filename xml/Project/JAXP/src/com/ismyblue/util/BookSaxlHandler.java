package com.ismyblue.util;
import java.util.LinkedList;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import com.ismyblue.entity.Book;

public class BookSaxlHandler extends DefaultHandler {

	private LinkedList<Book> bookList = null;
	private Book book = null;
	private String tagName = null;
	
	@Override
	public void startDocument() throws SAXException {
		super.startDocument();
		bookList = new LinkedList<Book>();
		
	}

	@Override
	public void endDocument() throws SAXException {
		
	}

	@Override
	public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {		
		 
		tagName  = qName;
		if(tagName.equals("book")) {
			book = new Book();
			book.setLang(attributes.getValue("lang"));			
		}
	}

	@Override
	public void endElement(String uri, String localName, String qName) throws SAXException {
		
		tagName = "";		
		if(qName.equals("book")) {
			bookList.add(book);
		}
		
	}

	@Override
	public void characters(char[] ch, int start, int length) throws SAXException {
		
		/*ch[start] ------ ch[length] 才是读出来的内容，我以为ch就是读出来的内容，其实ch是整篇xml的内容
		 * */
		
		switch (tagName) {
		case "title":
			book.setTitle(String.copyValueOf(ch,start,length));			
			break;
		case "author":
			book.setAuthor(String.copyValueOf(ch,start,length));
			break;
		case "price":
			book.setPrice(Double.parseDouble(String.copyValueOf(ch, start, length)));
			break;
		case "year":
			book.setYear(String.copyValueOf(ch,start,length));
			break;
		default:
			break;
		}

	}

	public LinkedList<Book> getBookList() {
		return bookList;
	}

	public void setBookList(LinkedList<Book> bookList) {
		this.bookList = bookList;
	}
	
	
	
	
	
}
