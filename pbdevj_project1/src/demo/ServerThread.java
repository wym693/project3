package demo;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.DocumentBuilderFactory;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.DocumentFactory;
import org.dom4j.Element;
import org.dom4j.io.OutputFormat;
import org.dom4j.io.SAXReader;
import org.dom4j.io.XMLWriter;

//�������߳���  ֧�ֶ��û���¼
public class ServerThread extends Thread {

	// ˽������
	private Socket socket = null;
	private InputStream is = null;
	private OutputStream os = null;

	private ObjectOutputStream oos = null;
	// ��ǰ�ͻ���û�е�¼
	private boolean isLogin = false;

	// �Ƿ��˳�
	private boolean exit = false;

	private List<Novel> novels = new ArrayList<Novel>();

	// ��ȡ��¼�û�����
	private List<User> users = new ArrayList<User>();

	public void initNovel() {

		Novel novel = new Novel(1, "����˫��", "����", "��������С˵��", "����˫�����ݡ�����������",
				"����");
		Novel nove2 = new Novel(2, "¹����", "����", "��������С˵��", "¹�������ݡ�����������", "����");
		Novel nove3 = new Novel(3, "������", "����", "̨����������С˵��", "���������ݡ�����������",
				"����");
		Novel nove4 = new Novel(4, "�»�����", "����", "̨����������С˵��", "�»��������ݡ�����������",
				"����");
		novels.add(novel);
		novels.add(nove2);
		novels.add(nove3);
		novels.add(nove4);
	}

	public ServerThread() {
	}

	public ServerThread(Socket socket) {
		this.socket = socket;
	}

	public void run() {
		// ��ʼ��С˵
		initNovel();
		// ��ʼ��xml
		initXML();
		// ��ʼ��users�ļ���
		loadXml();

		ObjectInputStream ois = null;
		try {
			this.os = this.socket.getOutputStream();
			this.is = this.socket.getInputStream();
			ois = new ObjectInputStream(is);

		} catch (IOException e) {
			e.printStackTrace();
		}

		while (!this.socket.isClosed()) {
			
			
			System.out.println("ѭ��");
			Data request = null;
			try {
				request = (Data) ois.readObject();
			} catch (ClassNotFoundException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
			Data response = null;
			if ("login".equals(request.getOperation())) {
				response = valid(request);
				send(response);
			}
			if ("readKongFu".equals(request.getOperation())) {
				response = getNovels(request);
				send(response);
			}
			//readRomantic
			if ("readRomantic".equals(request.getOperation())) {
				response = getNovels(request);
				send(response);
			}
			
			// ע��
			if ("REGIST".equals(request.getOperation())) {
				response = regist(request);
				send(response);
			}

		}

	}

	private Data readRomantic(Data request) {
		
		return null;
	}

	private void initXML() {

		File filefolder = new File(System.getProperty("user.dir"), "file");
		if (!filefolder.exists()) {
			filefolder.mkdir();
		}
		// ��ȡUser ��xml�ļ�

		File usersxml = new File(filefolder, "users.xml");
		if (!usersxml.exists()) {
			try {
				usersxml.createNewFile();
			} catch (IOException e) {
				e.printStackTrace();
				System.out.println("�����ļ�ʧ��");
			}
			System.out.println("users.xml�����ɹ�");
			// ��ʼ����Ŀ¼
			initUserXML(usersxml);
		}

	}

	private Data regist(Data request) {
		// �ж��Ƿ�������� �����ھʹ����µ��ĵ�
		// ���ھͶ�ȡ
		/*
		 * Users user username password
		 */
		Data response = new Data();
		//��ȡ�ļ����ǳ���
		File filefolder = new File(System.getProperty("user.dir"), "file");
		if (!filefolder.exists()) {
			filefolder.mkdir();
		}
		// ��ȡUser ��xml�ļ�

		File usersxml = new File(filefolder, "users.xml");
		
		// �ж��Ƿ���ڸ��û�
		boolean usernameExist = validUsername(request.getUser().getUsername());
		System.out.println("�û��������ڣ�׼����ӽ���");
		if (!usernameExist) {
			// �����û���xml��
			addUserToXml(usersxml, request.getUser());
			// ���¼��ؼ���
			loadXml();
			// ��ӵ�ע�����
			response.setSuccess(true);
			response.setMsg("ע��ɹ�,����û�����" + request.getUser().getUsername());
		} else {
			response.setSuccess(false);
			response.setMsg("ע��ʧ��,����û����Ѿ�����");
		}
		return response;
	}

	// ��ʼ��XML
	private void initUserXML(File file) {
		// ʹ��Dom4jд����ڵ�
		DocumentFactory df = DocumentFactory.getInstance();
		Document document = df.createDocument();
		Element root = document.addElement("Users");

		document.setRootElement(root);

		// ���ø�ʽ(��������)
		OutputFormat format = new OutputFormat("  ", true);
		format.setEncoding("UTF-8");

		FileOutputStream fos = null;
		try {
			fos = new FileOutputStream(file);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}

		OutputStreamWriter out = new OutputStreamWriter(fos);

		XMLWriter writer = new XMLWriter(out, format);
		try {
			writer.write(document);
			writer.flush();
			writer.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		System.out.println("users.xml��ʼ���ɹ�");
	}

	private void loadXml() {
		// ���µ�ǰ�ļ���
		this.users.clear();

		SAXReader reader = new SAXReader();
		File file = new File(System.getProperty("user.dir"), "file");
		Document document = null;
		try {
			document = reader.read(new File(file, "users.xml"));
		} catch (DocumentException e) {
			e.printStackTrace();
		}
		Element root = document.getRootElement();
		List<Element> users = root.elements();

		for (Element user : users) {
			// ��һ��ӵ����ϵ���
			User u = new User(user.elementText("username"),
					user.elementText("password"));
			this.users.add(u);
		}
		System.out.println("��ȡXML�ɹ�");

	}

	// ���User��xml
	private void addUserToXml(File usersxml, User user) {

		SAXReader reader = new SAXReader();
		Document document = null;
		try {
			document = reader.read(usersxml);
		} catch (DocumentException e) {
			e.printStackTrace();
			System.out.println("��ȡ����users.xml");
		}

		Element root = document.getRootElement();

		Element adduser = root.addElement("user");
		adduser.addElement("username").setText(user.getUsername());
		adduser.addElement("password").setText(user.getPassword());

		// ����XML

		OutputFormat format = OutputFormat.createPrettyPrint();

		FileOutputStream fos = null;
		try {
			fos = new FileOutputStream(usersxml);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}

		OutputStreamWriter out = new OutputStreamWriter(fos);

		XMLWriter writer = new XMLWriter(out, format);
		try {
			writer.write(document);
			writer.flush();
			writer.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		System.out.println("users.xml��Ӹ��³ɹ�");

	}

	private boolean validUsername(String username) {

		// �Ƚ��û���������
		for (User user : this.users) {
			// ���ж��Ƿ���ڸ��û�

			if (user.getUsername().equals(username)) {
				// �ҵ�ͬ��
				return true;
			}
		}

		return false;
	}

	private Data getNovels(Data request) {
		Data response = new Data();
		List<Novel> resultnovels=new ArrayList<Novel>();
		
		if("readKongFu".equals(request.getOperation())){
			for (Novel novel : this.novels) {
				if("����".equals(novel.getType())){
					resultnovels.add(novel);
				}
			}
		}
		//readRomantic
		if("readRomantic".equals(request.getOperation())){
			for (Novel novel : this.novels) {
				if("����".equals(novel.getType())){
					resultnovels.add(novel);
				}
			}
		}
		
		
		response.setList(resultnovels);

		return response;
	}

	// ��֤�û���������
	private Data valid(Data request) {
		Data response = new Data();
		// ��ȡUser ƥ��XML �û�������
		if (null == request.getUser()) {
			this.isLogin = false;
			request.setLogin(false);
			response.setMsg("�û�����Ϊ��");

		}
		// ����XML ���û�д��û� �򷵻ز����ڴ��û�
		for (User dbu : this.users) {
			String u = dbu.getUsername().trim();
			String p = dbu.getPassword().trim();
			if (request.getUser().getUsername().trim().equals(u)
					&& request.getUser().getPassword().trim().equals(p)) {
				this.isLogin = true;
				response.setLogin(true);
				response.setMsg("��¼�ɹ�");
				break;
			}

		}

		return response;

	}

	public void send(Data object) {
		try {
			if (null == oos) {
				oos = new ObjectOutputStream(this.os);
			}
			oos.writeObject(object);
			oos.flush();
			// oos.close();

			// �ر�socket;
			// this.socket.close();

		} catch (UnknownHostException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

}
