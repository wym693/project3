package demo;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.OutputFormat;
import org.dom4j.io.SAXReader;
import org.dom4j.io.XMLWriter;

public class Test {
	public static void main(String[] args) {
		
		File filefolder=new File(System.getProperty("user.dir"),"file");
		//读取User 的xml文件
		
		File usersxml=new File(filefolder,"users.xml");
		
		SAXReader reader=new SAXReader();
		Document document=null;
		try {
			document=reader.read(usersxml);
		} catch (DocumentException e) {
			e.printStackTrace();
			System.out.println("获取不到users.xml");
		}
		
	    Element root=document.getRootElement();
		
	    User user=new User("zhangsan","1234");
	    		
		
		
		Element adduser=root.addElement("user");
		adduser.addElement("username").setText(user.getUsername());
		adduser.addElement("password").setText(user.getPassword());
		
		
		
		//保存XML
		
		OutputFormat format=new OutputFormat("  ",true);
		
		FileOutputStream fos=null;
		try {
			fos = new FileOutputStream(usersxml);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		
		OutputStreamWriter out=new OutputStreamWriter(fos);
        
		XMLWriter writer=new XMLWriter(out, format);
		try {
			writer.write(document);
			writer.flush();
			writer.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		System.out.println("users.xml添加更新成功");
		
	}

}
