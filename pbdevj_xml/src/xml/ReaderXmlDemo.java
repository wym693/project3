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

public class ReaderXmlDemo {

	
	public static void main(String[] args) {
		
		//构造DocumentBuilderFactroy 抽象工厂
		DocumentBuilderFactory dbf=DocumentBuilderFactory.newInstance();
		
		//得到DocumentBuilder类，这可以通过工厂类来得到
		try {
			DocumentBuilder builder =dbf.newDocumentBuilder();
			
	        //构造Document对象，相当一个xml
			try {
				Document xml=builder.parse("School.xml");
				
			    NodeList banjis=xml.getElementsByTagName("班级");
			    
			    System.out.println("班级有："+banjis.getLength()+"个");
				
				//把班级里面学生读取出来。把学生的节点取出来
			    
			    NodeList students=xml.getElementsByTagName("学生");
			    System.out.println(students.getLength());
				//从集合里面遍历出学生的信息
			    for (int i = 0; i < students.getLength(); i++) {
			    	    Element student=(Element)students.item(i);
			    	    String studentNo=student.getAttribute("id");
			    	   // System.out.println(studentNo);
			    	   //取出学生节点的其他子元素节点。如姓名：张三
			    	    System.out.println("学号为："+studentNo+"信息：");
			    	    for(Node child=student.getFirstChild();
			    	    	child!=null;
			    	    	child=child.getNextSibling()	
			    	    		){
			    	    //空白不写当成text类型
			    	    // System.out.println(child.getNodeType());
			    	    	if(child.getNodeType()==Node.ELEMENT_NODE){
			    	    		Element e=(Element)child;
			    	    		String elementName=e.getTagName();
			    	    		String value=e.getFirstChild().getNodeValue();
			    	    		
			    	    		System.out.println(elementName+"\t\t"+value);
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
