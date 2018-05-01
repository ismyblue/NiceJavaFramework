
import java.io.FileWriter;
import java.io.IOException;
import java.util.Iterator;
import java.util.List;

import org.dom4j.Attribute;
import org.dom4j.Comment;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.dom4j.Node;
import org.dom4j.QName;
import org.dom4j.Text;
import org.dom4j.io.OutputFormat;
import org.dom4j.io.SAXReader;
import org.dom4j.io.XMLWriter;
import org.junit.Test;

public class Dom4j {	
	
	//@Test
	//查
	public void test1() throws DocumentException {
		//創建一個xml解析器
		SAXReader saxReader = new SAXReader();
		//解析器解析出一個dom文档，这个Document是org.dom4j.Document;而不是w3c.xml.dom.Document;
		Document document =saxReader.read("src/Book.xml");
		System.out.println(document);
		//得到dom文档的根元素org.dom4j.Element.
		Element root = document.getRootElement();		
		System.out.println(root);
		//获得root里的第一个book元素
		Element bookElement = root.element("book"); 
		System.out.printf("第一个%s的StringValue"
				+ ":%s\n",bookElement.getName(),bookElement.getStringValue());
		//获得root元素里的所有名为book的元素
		List<Element> bookList = root.elements("book");
		System.out.println(bookList);
		//获得root元素里的第二个book元素
		Element secondBookElement = bookList.get(1);
		System.out.println(secondBookElement);
		//输出第二个book元素的title元素的内容		
		System.out.println(secondBookElement.elementText("title"));
		System.out.println(secondBookElement.element("title").getText());
		//输出第二个book元素的title元素的属性	
		Element secondBookTitle = secondBookElement.element("title"); 
		System.out.println(secondBookTitle.attributes());
		System.out.println(secondBookTitle.attributeValue("lang"));
		
	}
	
	//遍历所有元素节点
	public void listAll(Node node) {
		if(node.getNodeType() == Node.DOCUMENT_NODE) {//如果是文档节点			
			listAll(((Document)node).getRootElement());
		}else if(node.getNodeType() == Node.ELEMENT_NODE) {//如果是元素节点
			Element ele = (Element)node;
			System.out.printf("<%s",ele.getName());
			int k = 0;
			//如果存在属性节点
			while(k < ele.attributeCount()) {				
				listAll(ele.attribute(k++));
			}
			System.out.print(">");
			for(int i = 0 ;i < ele.nodeCount();i++) {				
				listAll(ele.node(i));
			}
			System.out.printf("</%s>",ele.getName());
		}else if(node.getNodeType() == Node.ATTRIBUTE_NODE) {//如果是属性节点
			Attribute attr = (Attribute)node;
			System.out.printf(" %s=\"%s\"",attr.getName(),attr.getValue());
		}else if(node.getNodeType() == Node.TEXT_NODE) {//如果是文本节点
			System.out.printf("%s",((Text)node).getText());		
		}		
	}
	
	//@Test
	//测试列出所有的节点，Document节点，Element节点，Attribute节点,Text节点
	public void testListAll() throws DocumentException {
		SAXReader saxReader = new SAXReader();
		Document document = saxReader.read("src/Book.xml");
		listAll(document);
	}
	
	
	//@Test
	//测试迭代器的使用
	public void testIterator() throws DocumentException {
		SAXReader saxReader = new SAXReader();
		Document document = saxReader.read("src/Book.xml");
		System.out.println(document);
		Element root = document.getRootElement();
		//for(Iterator i = root.attributeIterator();i.hasNext();) {
		//for(Iterator i = root.elementIterator();i.hasNext();) {		
		for(Iterator<Node> i = root.nodeIterator();i.hasNext();) {
			Node node = (Node) i.next();
			System.out.println(node);
		}
	}
	
	/**
	 * 创建一个document文档对象
	 * @return
	 */
	public Document createElement() {
		Document document = DocumentHelper.createDocument();
		Element root = DocumentHelper.createElement("root");
		document.setRootElement(root);
		
	//	Element root = document.addElement("root");
		
		//root元素加入一个元素,名字是"author",这个元素在内存中的引用是Element author1
		Element author1 = root.addElement("author")
				.addAttribute("name", "ismyblue")
				.addAttribute("location","China");
		author1.addAttribute("home","hengyang");
		author1.addText("content");

		//root添加一条注释
		Comment comment = DocumentHelper.createComment("这是注释");
		root.add(comment);				
		root.addElement("author").addText("Content2");		
		Element secondAuthor = root.elements("author").get(1);
		//增加CDATA和属性
		secondAuthor.addCDATA("this is CDATA");		
		secondAuthor.addAttribute("name", "joy")
					.addAttribute("location", "hengyang")
					.addAttribute("home","hengNan");		
		//删除location属性
		secondAuthor.remove(secondAuthor.attribute("location"));		
		//修改文本内容
		String oldText = secondAuthor.getText();
		secondAuthor.setText(oldText + "   -content2 updated-");		
		//修改属性的值
		secondAuthor.attribute("home").setValue("HengNanXian");
		
		return document;
	}
	
	@Test
	//测试DocumentHelper类创建一个document对象，并写入一个文档对象到xml文件
	public void testWriter() throws IOException, DocumentException {
		
		SAXReader saxReader = new SAXReader();
		Document document = saxReader.read("src/Book.xml");	
		
		
		FileWriter fileWriter = new FileWriter("src/new.xml");
		document.write(fileWriter);
		//一定要记住关闭写入流动
		fileWriter.close();
		
		XMLWriter xmlWriter = new XMLWriter(new FileWriter("src/new2.xml"));		
//		xmlWriter.write(document);
//		//一定要记住关闭写入流动
//		xmlWriter.close();
//		
		//漂亮的输出格式化
		OutputFormat outputFormat = OutputFormat.createPrettyPrint();
		//紧凑的输出格式化
		//OutputFormat outputFormat = OutputFormat.createCompactFormat();
		xmlWriter = new XMLWriter(new FileWriter("src/new2.xml"), outputFormat);
		//漂亮的格式化输出到src/new2.xml
		xmlWriter.write(createElement());
		//一定要记住关闭写入流动
		xmlWriter.close();
	}
	
	
}
