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

//服务器线程类  支持多用户登录
public class ServerThread extends Thread {

	// 私有属性
	private Socket socket = null;
	private InputStream is = null;
	private OutputStream os = null;

	private ObjectOutputStream oos = null;
	// 当前客户有没有登录
	private boolean isLogin = false;

	// 是否退出
	private boolean exit = false;

	private List<Novel> novels = new ArrayList<Novel>();

	// 获取登录用户集合
	private List<User> users = new ArrayList<User>();

	public void initNovel() {

		Novel novel = new Novel(1, "绝代双骄", "古龙", "著名武侠小说家", "绝代双骄内容………………",
				"武侠");
		Novel nove2 = new Novel(2, "鹿鼎记", "古龙", "著名武侠小说家", "鹿鼎记内容………………", "武侠");
		Novel nove3 = new Novel(3, "还珠格格", "琼瑶", "台湾著名言情小说家", "还珠格格内容………………",
				"言情");
		Novel nove4 = new Novel(4, "新还珠格格", "古龙", "台湾著名言情小说家", "新还珠格格内容………………",
				"言情");
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
		// 初始化小说
		initNovel();
		// 初始化xml
		initXML();
		// 初始化users的集合
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
			
			
			System.out.println("循环");
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
			
			// 注册
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
		// 读取User 的xml文件

		File usersxml = new File(filefolder, "users.xml");
		if (!usersxml.exists()) {
			try {
				usersxml.createNewFile();
			} catch (IOException e) {
				e.printStackTrace();
				System.out.println("创建文件失败");
			}
			System.out.println("users.xml创建成功");
			// 初始化根目录
			initUserXML(usersxml);
		}

	}

	private Data regist(Data request) {
		// 判断是否存在数据 不存在就创建新的文档
		// 存在就读取
		/*
		 * Users user username password
		 */
		Data response = new Data();
		//读取文件考虑常量
		File filefolder = new File(System.getProperty("user.dir"), "file");
		if (!filefolder.exists()) {
			filefolder.mkdir();
		}
		// 读取User 的xml文件

		File usersxml = new File(filefolder, "users.xml");
		
		// 判断是否存在该用户
		boolean usernameExist = validUsername(request.getUser().getUsername());
		System.out.println("用户名不存在，准备添加进入");
		if (!usernameExist) {
			// 插入用户到xml中
			addUserToXml(usersxml, request.getUser());
			// 重新加载集合
			loadXml();
			// 添加到注册表当中
			response.setSuccess(true);
			response.setMsg("注册成功,你的用户名是" + request.getUser().getUsername());
		} else {
			response.setSuccess(false);
			response.setMsg("注册失败,你的用户名已经存在");
		}
		return response;
	}

	// 初始化XML
	private void initUserXML(File file) {
		// 使用Dom4j写入根节点
		DocumentFactory df = DocumentFactory.getInstance();
		Document document = df.createDocument();
		Element root = document.addElement("Users");

		document.setRootElement(root);

		// 设置格式(换行缩进)
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
		System.out.println("users.xml初始化成功");
	}

	private void loadXml() {
		// 更新当前的集合
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
			// 逐一添加到集合当中
			User u = new User(user.elementText("username"),
					user.elementText("password"));
			this.users.add(u);
		}
		System.out.println("读取XML成功");

	}

	// 添加User到xml
	private void addUserToXml(File usersxml, User user) {

		SAXReader reader = new SAXReader();
		Document document = null;
		try {
			document = reader.read(usersxml);
		} catch (DocumentException e) {
			e.printStackTrace();
			System.out.println("获取不到users.xml");
		}

		Element root = document.getRootElement();

		Element adduser = root.addElement("user");
		adduser.addElement("username").setText(user.getUsername());
		adduser.addElement("password").setText(user.getPassword());

		// 保存XML

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
		System.out.println("users.xml添加更新成功");

	}

	private boolean validUsername(String username) {

		// 比较用户名和密码
		for (User user : this.users) {
			// 先判断是否存在该用户

			if (user.getUsername().equals(username)) {
				// 找到同名
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
				if("武侠".equals(novel.getType())){
					resultnovels.add(novel);
				}
			}
		}
		//readRomantic
		if("readRomantic".equals(request.getOperation())){
			for (Novel novel : this.novels) {
				if("言情".equals(novel.getType())){
					resultnovels.add(novel);
				}
			}
		}
		
		
		response.setList(resultnovels);

		return response;
	}

	// 验证用户名和密码
	private Data valid(Data request) {
		Data response = new Data();
		// 获取User 匹配XML 用户和密码
		if (null == request.getUser()) {
			this.isLogin = false;
			request.setLogin(false);
			response.setMsg("用户对象为空");

		}
		// 查找XML 如果没有此用户 则返回不存在此用户
		for (User dbu : this.users) {
			String u = dbu.getUsername().trim();
			String p = dbu.getPassword().trim();
			if (request.getUser().getUsername().trim().equals(u)
					&& request.getUser().getPassword().trim().equals(p)) {
				this.isLogin = true;
				response.setLogin(true);
				response.setMsg("登录成功");
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

			// 关闭socket;
			// this.socket.close();

		} catch (UnknownHostException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

}
