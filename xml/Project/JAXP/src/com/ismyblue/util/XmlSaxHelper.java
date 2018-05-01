package com.ismyblue.util;

import java.io.IOException;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;


public class XmlSaxHelper {
	
	private SAXParserFactory saxParserFactory = null;
	private SAXParser saxParser = null;	

	
	public XmlSaxHelper() {
		saxParserFactory = SAXParserFactory.newInstance();
		try {
			saxParser = saxParserFactory.newSAXParser();
		} catch (ParserConfigurationException e) {
			e.printStackTrace();
		} catch (SAXException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * 获得一个saxHelper帮助类实例
	 * @return
	 */
	public static XmlSaxHelper newInstance() {
		return new XmlSaxHelper();
	}
	
	
	/**
	 * 通过自定义一个sax处理器，在sax解析器解析触发事件时，sax处理器会记录数据保存到那处理器内部，
	 * 再通过sax处理器自定义的getXXX()如getBookList()获取解析的实体列表
	 * @param uri
	 * @param defaultHandler
	 */
	public void parseToHandler(String uri, DefaultHandler defaultHandler) {	
		
		try {
			saxParser.parse(uri, defaultHandler);
		} catch (SAXException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}
	
	
	
	
}
