import java.util.LinkedList;

import org.junit.Test;
import org.xml.sax.helpers.DefaultHandler;

import com.ismyblue.entity.Book;
import com.ismyblue.util.BookSaxlHandler;
import com.ismyblue.util.XmlSaxHelper;

public class TestXmlSaxHelper {

	@Test
	public void testParseToHandler() {
		DefaultHandler df = new BookSaxlHandler();
		XmlSaxHelper.newInstance().parseToHandler("Book.xml", df);
		LinkedList<Book> list = ((BookSaxlHandler) df).getBookList();
		for(Book book : list) {
			System.out.println(book);
		}
	}
	
}
