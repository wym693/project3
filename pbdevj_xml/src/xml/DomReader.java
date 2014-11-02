package xml;

import java.io.IOException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

public class DomReader {

	public static void main(String[] args) {
		
		DocumentBuilderFactory dbf=DocumentBuilderFactory.newInstance();
		try {
			DocumentBuilder db=dbf.newDocumentBuilder();
			try {
				Document document=db.parse("pets.xml");
				NodeList list=document.getElementsByTagName("dog");
				System.out.println("拥有狗狗："+list.getLength());
				for (int i = 0; i < list.getLength(); i++) {
					Node node=list.item(i);
					//有了Element才能去id值。
					Element element=(Element)node;
					String id=element.getAttribute("id");
					for(Node child=node.getFirstChild();child!=null;child=child.getNextSibling()){
						System.out.println(child.getNodeName()+"  类型"+child.getNodeType()+child.getNodeValue());
						if(child.getNodeType()==Node.ELEMENT_NODE){
						String nodeName=child.getNodeName();
						System.out.println("="+child.getFirstChild().getNodeType());
						String value=child.getFirstChild().getNodeValue();
						System.out.println("\t\t"+nodeName+"\t\t"+value);
						}
					}
					
				}
				
				
				
				
			} catch (SAXException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
			
		} catch (ParserConfigurationException e) {
			
			e.printStackTrace();
		}
		
		
		
		
		

	}

}
