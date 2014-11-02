package xml;

import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Writer;

import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.dom4j.io.OutputFormat;
import org.dom4j.io.XMLWriter;

public class MyXmlCreate {

	
	public static void main(String[] args) {
	//查询Dom4japi文档，通过工具类	DocumentHelper创建document
	Document document=	DocumentHelper.createDocument();
	//通过工具类	DocumentHelper创建Element
	
	Element root=DocumentHelper.createElement("学生宿舍");
	//设置根节点
	document.setRootElement(root);
	//添加根节点的孩子节点。
	Element one=root.addElement("一班");
	one.addAttribute("人数", "5");
	Element student1=one.addElement("学生");
	student1.setText("李彪");
	Element student2=one.addElement("学生");
	student2.setText("符伟振");
	Element student3=one.addElement("学生");
	student3.setText("谢伟亮");
	Element student4=one.addElement("学生");
	student4.setText("陈晓强");
	Element student5=one.addElement("学生");
	student5.setText("张龙风");
	
	OutputFormat format=OutputFormat.createPrettyPrint();
	//只有OutputStreamWriter
	OutputStreamWriter osw=null;
	
	try {
		
		FileOutputStream fos=new FileOutputStream("dome.xml");
		
		osw=new OutputStreamWriter(fos,"UTF-8");
		System.out.println(osw.getEncoding());
		
	} catch (IOException e1) {
		e1.printStackTrace();
	}
	//向控制台输出
	//XMLWriter writer=new XMLWriter(new OutputStreamWriter
//(System.out), format);
	//向目标文件输出。
	XMLWriter writer=new XMLWriter(osw,format);
	try {
		writer.write(document);
		writer.flush();
		writer.close();
	} catch (IOException e) {
		e.printStackTrace();
	}
	
	
	
	
	
	
	
	
	
	
		
		
		
		
		
		
		
		
		
		
		
		
		

	}

}
