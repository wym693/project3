package xml;

import java.io.File;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;


public class SaxReaderDemo {
	public static void main(String[] args) {
 
		String url="";
		SAXReader reader=new SAXReader();
		try {
			Document document=reader.read(new File(url));
			Element root=document.getRootElement();
			
			
			
		} catch (DocumentException e) {
			e.printStackTrace();
		}
		
		
		
		
		
		
	}

}
