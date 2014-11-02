package xml;

import java.io.FileNotFoundException;
import java.io.IOException;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

/**
 * 使用DOM解析xml文档。
 * @author 北大青鸟
 */
public class TestDOM {
	public static void main(String[] args) {
		// 1、得到DOM解析器的工厂实例
		DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
		try {
			// 2、从DOM工厂获得DOM解析器
			DocumentBuilder db = dbf.newDocumentBuilder();
			// 3、解析XML文档，得到一个Document，即DOM树
			Document doc = db.parse("pets.xml");
			// 4、得到所有<DOG>节点列表信息
			NodeList dogList = doc.getElementsByTagName("dog");
			System.out.println("xml文档中共有" + 
					  dogList.getLength() + "条狗狗信息");
			// 5、轮循狗狗信息
			for (int i = 0; i < dogList.getLength(); i++) {
				// 5.1、获取第i个狗狗元素信息
				Node dog = dogList.item(i);
				// 5.2、获取第i个狗狗元素的id属性的值并输出
				Element element = (Element) dog;
				String attrValue = element.getAttribute("id");
				System.out.println("id:" + attrValue);
				// 5.3、获取第i个狗狗元素的所有子元素的名称和值并输出
				for (Node node = dog.getFirstChild(); node != null; 
											node = node.getNextSibling()) {
					if (node.getNodeType() == Node.ELEMENT_NODE) {
						String name = node.getNodeName();
						String value = node.getFirstChild().getNodeValue();
						System.out.print(name + ":" + value + "\t");
					}
				}
				System.out.println();
			}
		} catch (ParserConfigurationException e) {
			e.printStackTrace();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (SAXException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
