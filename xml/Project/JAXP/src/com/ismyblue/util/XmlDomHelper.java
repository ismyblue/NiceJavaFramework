package com.ismyblue.util;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.Writer;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.Document;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

public class XmlDomHelper {
	
	//文档构造器工厂，文档构造器，写回器工厂，写回器
	private static DocumentBuilderFactory documentBuilderFactory;
	private static DocumentBuilder documentBuilder;
	private static TransformerFactory transformerFactory;
	private static Transformer transformer;
	
	//private Document document;
	
	/**
	 * 获得一个文档构造器工厂，用这个文档构造器工厂去创建一个文档构造器
	 */
	static {
		documentBuilderFactory = DocumentBuilderFactory.newInstance();
		try {
			documentBuilder = documentBuilderFactory.newDocumentBuilder();
		} catch (ParserConfigurationException e) {
			e.printStackTrace();
		}
		transformerFactory = TransformerFactory.newInstance();
		try {
			transformer = transformerFactory.newTransformer();
		} catch (TransformerConfigurationException e) {
			e.printStackTrace();
		}
	}

	/**
	 * 返回一个空的Document专门用来createElements等操作
	 * @return
	 */
	public static Document getDocument() {
		return documentBuilder.newDocument();
	}
	
	
	public static Document getDocument(String uri) throws SAXException, IOException {
			return documentBuilder.parse(uri);
	}

	public static Document getDocument(File file) throws SAXException, IOException {
			return documentBuilder.parse(file);
	}
	
	public static Document getDocument(InputStream inputStream) throws SAXException, IOException {
			return documentBuilder.parse(inputStream);		
	}

	
	/**
	 * 以友好的方式格式化列出某个节点下的所有节点
	 * @param node
	 */
	public static void listAll(Node node) {
		int i = 0;
		NodeList nodeList = node.getChildNodes();
		int len = nodeList.getLength();
		while(i < len) {
			Node childNode = nodeList.item(i++);
			switch (childNode.getNodeType()) {
			case Node.ELEMENT_NODE:				
				System.out.printf("<%s",childNode.getNodeName());
				listAttributes(childNode);
				System.out.print(">");
				listAll(childNode);
				System.out.printf("</%s>",childNode.getNodeName());
				break;
			case Node.TEXT_NODE:
				System.out.print(childNode.getTextContent());
				break;
			default:
				break;
			}				
		}			
	}

	/**
	 * 以友好的方式格式化列出某个节点下的所有属性
	 * @param node
	 */
	public static void listAttributes(Node node) {
		
		NamedNodeMap attributes = node.getAttributes();
		int i = 0;
		int len = attributes.getLength();
		while(i < len) {
			System.out.printf(" %s=\"%s\"",attributes.item(i).getNodeName(),attributes.item(i++).getTextContent());
		}
		
	}
	
	/**
	 * 更新Xml
	 * @param node 要更新的内容
	 * @param uri 更新到这里
	 * @throws TransformerException
	 */
	public static void updateXml(Node node, String uri) throws TransformerException {	
		transformer.transform(new DOMSource(node),new StreamResult(uri));		
	}

	/**
	 * 更新Xml
	 * @param node
	 * @param file
	 * @throws TransformerException
	 */
	public static void updateXml(Node node, File file) throws TransformerException {	
		transformer.transform(new DOMSource(node),new StreamResult(file));		
	}
	
	/**
	 * 更新Xml
	 * @param node
	 * @param outputStream
	 * @throws TransformerException
	 */
	public static void updateXml(Node node, OutputStream outputStream) throws TransformerException {	
		transformer.transform(new DOMSource(node),new StreamResult(outputStream));		
	}	

	/**
	 * 更新Xml
	 * @param node
	 * @param writer
	 * @throws TransformerException
	 */
	public static void updateXml(Node node,  Writer writer) throws TransformerException {	
		transformer.transform(new DOMSource(node),new StreamResult(writer));		
	}
	
	

	
	
	
}
