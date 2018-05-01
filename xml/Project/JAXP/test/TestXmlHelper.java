import java.io.IOException;
import java.util.ArrayList;

import javax.xml.transform.TransformerException;

import org.junit.Test;
import org.w3c.dom.Attr;
import org.w3c.dom.DOMException;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.xml.sax.SAXException;

import com.ismyblue.entity.Book;
import com.ismyblue.util.XmlDomHelper;

public class TestXmlHelper {
	
	
	/**
	 * 测试能否获得一个Document对象
	 * @throws IOException 
	 * @throws SAXException 
	 */
	public void testGetDocument() throws SAXException, IOException {

		System.out.println(XmlDomHelper.getDocument("Book.xml"));
	}

	/**
	 * 获得某一个节点的text
	 * @throws IOException 
	 * @throws SAXException 
	 * @throws DOMException 
	 */
	public void testGetTextContentByTagName() throws DOMException, SAXException, IOException {

		System.out.println(XmlDomHelper.getDocument("Book.xml").getElementsByTagName("price").item(0).getTextContent());

	}

	// @Test
	public void testListAllElement() throws SAXException, IOException {
		XmlDomHelper.listAll(XmlDomHelper.getDocument("Book.xml"));
	}

	@Test
	public void testUpdateXml() throws TransformerException, SAXException, IOException {
		Document document = XmlDomHelper.getDocument("Book.xml");		
		Element rootElement = document.getDocumentElement();
		
		ArrayList<String> authors = new ArrayList<>();
		authors.add("huanghao");
		authors.add("Joy");
		authors.add("Lee"); 
		
		insertInNode(document, rootElement, new Book("一本好书", "en", authors, 35.5, "2018"));
		XmlDomHelper.updateXml(document, "Book.xml");
		
		XmlDomHelper.listAll(XmlDomHelper.getDocument("Book.xml"));
		
	}

	/**
	 * 在document中的node元素插入本书
	 * @param document
	 * @param node
	 * @param book
	 */
	public void insertInNode(Document document, Node node,Book book) {
		//Document document = XmlHelper.getDocument();
		Element newBook = document.createElement("book");
		
		Element bookTitle = document.createElement("title");		
		bookTitle.setTextContent(book.getTitle());
		Attr langAttr = document.createAttribute("lang");
		langAttr.setTextContent(book.getLang());
		bookTitle.setAttributeNode(langAttr);	
		newBook.appendChild(document.createTextNode("\n\t\t"));
		newBook.appendChild(bookTitle);		
		
		ArrayList<String> authors = book.getAuthors();
		for(int i = 0;i < authors.size();i++) {
			Element author = document.createElement("author");
			author.setTextContent(authors.get(i));
			newBook.appendChild(document.createTextNode("\n\t\t"));
			newBook.appendChild(author);
		}
		
		Element price = document.createElement("price");
		price.setTextContent(String.valueOf(book.getPrice()));
		newBook.appendChild(document.createTextNode("\n\t\t"));
		newBook.appendChild(price);
		
		Element year = document.createElement("year");
		year.setTextContent(String.valueOf(book.getYear()));
		newBook.appendChild(document.createTextNode("\n\t\t"));
		newBook.appendChild(year);
		newBook.appendChild(document.createTextNode("\n\t"));

		node.appendChild(document.createTextNode("\t"));
		node.appendChild(newBook);
		node.appendChild(document.createTextNode("\n"));
	}
}
