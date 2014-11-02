package xml;

import java.io.IOException;
import java.io.OutputStreamWriter;

import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.dom4j.io.OutputFormat;
import org.dom4j.io.XMLWriter;

public class CreateXmlDemo {

	
	public static void main(String[] args) {
		Document document=DocumentHelper.createDocument();
		Element root=DocumentHelper.createElement("学校");
		document.setRootElement(root);
		Element room=root.addElement("班级");
		room.addAttribute("id", "1班");
		Element room_name=room.addElement("名称");
		room_name.setText("ACCP");
		Element room_level=room.addElement("年级");
		room_level.setText("S2");
		
		Element room_number=room.addElement("人事");
		room_number.setText("25");
		OutputFormat format=OutputFormat.createPrettyPrint();
		XMLWriter writer=new XMLWriter(new OutputStreamWriter(System.out),format);
		try {
			writer.write(document);
			writer.flush();
			writer.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		
		
		
		
		
		
		

	}

}
