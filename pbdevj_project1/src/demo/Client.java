package demo;

import java.io.BufferedWriter;
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
import java.util.List;
import java.util.Scanner;

public class Client {

	private Scanner input = new Scanner(System.in);

	private Socket socket;

	private InputStream is = null;
	private OutputStream os = null;
	private ObjectInputStream ois = null;
	private ObjectOutputStream oos = null;

	public void menu() {
		System.out.println("欢迎使用在线迷你TXT小说管理器");
		System.out.println("----------------------");
		System.out.println("1.登录");
		System.out.println("2.注册");
		System.out.println("3.退出");
		System.out.println("----------------------");
		System.out.println("请选择");
		int choice = input.nextInt();
		switch (choice) {
		case 1:
			login();
			break;
		case 2:
			regist();
			break;
		case 3:
			System.out.println("系统正常退出");
			try {
				this.os.close();
				this.is.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			System.out.println("关闭Session");
//			System.exit(0);
			break;

		default:
			break;
		}
	}

	// 注册功能 验证是否有该用户 注册成功 跳转到登录 注册失败重新注册

	private void regist() {
		System.out.println("~~~~~~~~~~~~");
		System.out.println("当前操作：用户注册");
		System.out.println("~~~~~~~~~~~~");

		while (true) {
			System.out.println("请输入登录名");
			String username = input.next();
			System.out.println("请输入密码");
			String password = input.next();
			System.out.println("请再次输入密码");
			String repassword = input.next();
			if (!password.equals(repassword)) {
				System.out.println("两次输入密码不匹配");
				System.out.println("请重新输入");
				continue;
			}
			// 构造对象，发送给服务器
			User user = new User(username, password);

			Data request = new Data(false, "REGIST", user);

			Data response = send(request);

			if (response.isSuccess()) {
				// 进入登录
				login();
			} else {
				// 登录失败显示错误信息，重新注册
				System.out.println(response.getMsg());
				regist();
			}

		}

	}

	private void login() {

		while (true) {
			System.out.println("~~~~~~~~~~~~");
			System.out.println("当前操作：用户登录");
			System.out.println("~~~~~~~~~~~~");
			System.out.println("请输入登录名");
			String username = input.next();
			System.out.println("请输入密码");
			String password = input.next();

			// 构造用户对象，发送给服务器段，接受返回结果
			User user = new User(username, password);

			// 发送信息的工具类 打包成一个数据类型
			Data data = new Data(false, "login", user, "");

			Data result = send(data);

			// 登录成功
			if (result.isLogin()) {
				// 获取小说类型
				getNovel();

			} else {
				System.out.println("登录失败，请重新登录");
				continue;

			}

		}

	}

	private void getNovel() {
		// 打印登录菜单(考虑从服务器发回来)
		System.out.println("0.返回上级菜单");
		System.out.println("1.武侠");
		System.out.println("2.言情");
		System.out.println("请选择");
		int choice = input.nextInt();
		switch (choice) {
		case 0:
			menu();// 返回主菜单
			break;
		case 1:
			readKongfu();
			break;
		case 2:
			readRomantic();
			break;

		default:
			break;
		}

	}

	private void readRomantic() {
		
		// 发送数据， 获取武侠小说内容 返回的是小说的集合

		Data sendData = new Data(true, "readRomantic");

		Data result = null;
		result = send(sendData);

		List<Novel> novels = (List<Novel>) result.getList();
		System.out.println("言情小说列表");
		System.out.println("序号\t名称\t作者\t简介");
		for (Novel novel : novels) {
			System.out.println(novel.getId() + "\t" + novel.getName() + "\t"
					+ novel.getAuthor() + "\t" + novel.getDetail());
		}
		System.out.println("小说列表结束");
		System.out.println("阅读和下载请选择编号，上传TXT请输入-1,返回请输入0:");
		
		int choice = input.nextInt();

		switch (choice) {
		case 0:
			getNovel();
			break;
		case -1:
			upload();
			break;
		default:
			//找到制定的小说 关于小说的编号问题要修复 通过定义常量修复
			Novel novel = novels.get(choice - 1);
			read(novel);
			break;
		}
		
		
	}

	// 读取武侠小说
	private void readKongfu() {

		// 发送数据， 获取武侠小说内容 返回的是小说的集合

		Data sendData = new Data(true, "readKongFu");

		Data result = null;
		result = send(sendData);

		List<Novel> novels = (List<Novel>) result.getList();
		System.out.println("武侠小说列表");
		System.out.println("序号\t名称\t作者\t简介");
		for (Novel novel : novels) {
			System.out.println(novel.getId() + "\t" + novel.getName() + "\t"
					+ novel.getAuthor() + "\t" + novel.getDetail());
		}
		System.out.println("小说列表结束");
		System.out.println("阅读和下载请选择编号，上传TXT请输入-1,返回请输入0:");
		
		int choice = input.nextInt();

		switch (choice) {
		case 0:
			getNovel();
			break;
		case -1:
			upload();
			break;
		default:
			//找到制定的小说 关于小说的编号问题要修复 通过定义常量修复
			Novel novel = novels.get(choice - 1);
			read(novel);
			break;
		}
		
	}

	// 上传
	private void upload() {
		System.out.println("请输入小说名");
		String novleName=input.next();
		System.out.println("请输入作者");
		String author=input.next();
		System.out.println("请输入简介:");
		String detail=input.next();
		System.out.println("请输入上传的路径");
		String path=input.next();
		
		//上传成功后设置路径
		saveUploadFile(path,novleName);
		
		Novel novel=new Novel(novleName,author,detail,"","武侠");
		
		System.out.println("上传成功！");
		
		
	}

	// 保存上传文件
	private void saveUploadFile(String path, String name) {
		File filefolder = new File(System.getProperty("user.dir"), "novels");
		if (!filefolder.exists()) {
			filefolder.mkdir();
		}
		// 小说文件

		File novel = new File(filefolder, name + ".txt");

		FileInputStream fis=null;
		FileOutputStream fos=null;
		try {
			fis = new FileInputStream(path);
			fos = new FileOutputStream(novel);
			byte[] bs = new byte[1024];
			int len = -1;
			while ((len = fis.read(bs)) != -1) {
				fos.write(bs, 0, len);
			}
			fos.close();
			fis.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}


	}

	private void download(Novel novel) {
		// 获取下载的地址，保存到本地文件，使用IO
		System.out.println("下载成功");
		// System.out.println(System.getProperty("user.dir"));

		File download = new File(System.getProperty("user.dir"), "download");

		if (!download.exists()) {
			download.mkdir();
		}

		try {
			FileOutputStream fos = new FileOutputStream(new File(download,
					novel.getName() + ".txt"));
			BufferedWriter br = new BufferedWriter(new OutputStreamWriter(fos));
			br.write(novel.getContent());
			br.flush();
			br.close();
		} catch (IOException e) {
			e.printStackTrace();
			System.out.println("文件写入异常");
		}

		// 下载完成后 返回列表页面
		getNovel();

	}

	private void read(Novel novel) {
		System.out.println(novel.getContent());
		System.out.println("继续显示列表请输入1,下载TXT请输入2,返回上一级0");
		int choice = input.nextInt();

		switch (choice) {

		case 1:
			read(novel);
			break;
		case 2:
			download(novel);
			break;
		case 0:
			readKongfu();
			break;	

		default:
			break;
		}

	}

	public Data send(Object object) {
		// try {
		// if(null!=this.socket){
		// this.socket.close();
		// }
		// } catch (IOException e1) {
		// // TODO Auto-generated catch block
		// e1.printStackTrace();
		// }

		Data response = null;
		try {
			if (null == this.socket) {
				// this.socket=null;
				this.socket = new Socket("127.0.0.1", 8080);
				System.out.println("创建新的连接");
			}
			// System.out.println("连接成功");
			this.os = this.socket.getOutputStream();
			this.is = this.socket.getInputStream();

			if (null == oos) {
				oos = new ObjectOutputStream(this.os);
			}

			oos.writeObject(object);
			oos.flush();
			// oos.close();
			this.is = this.socket.getInputStream();

			if (null == this.ois) {
				this.ois = new ObjectInputStream(is);
			}

			response = (Data) ois.readObject();
			// ois.close();
			System.out.println("返回结果");

			// 关闭socket;
			// this.socket.close();

		} catch (UnknownHostException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}

		return response;

	}

	public static void main(String[] args) {

		Client client = new Client();
		client.menu();
		// client.download(null);

	}

}
