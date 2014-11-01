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
		System.out.println("��ӭʹ����������TXTС˵������");
		System.out.println("----------------------");
		System.out.println("1.��¼");
		System.out.println("2.ע��");
		System.out.println("3.�˳�");
		System.out.println("----------------------");
		System.out.println("��ѡ��");
		int choice = input.nextInt();
		switch (choice) {
		case 1:
			login();
			break;
		case 2:
			regist();
			break;
		case 3:
			System.out.println("ϵͳ�����˳�");
			try {
				this.os.close();
				this.is.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			System.out.println("�ر�Session");
//			System.exit(0);
			break;

		default:
			break;
		}
	}

	// ע�Ṧ�� ��֤�Ƿ��и��û� ע��ɹ� ��ת����¼ ע��ʧ������ע��

	private void regist() {
		System.out.println("~~~~~~~~~~~~");
		System.out.println("��ǰ�������û�ע��");
		System.out.println("~~~~~~~~~~~~");

		while (true) {
			System.out.println("�������¼��");
			String username = input.next();
			System.out.println("����������");
			String password = input.next();
			System.out.println("���ٴ���������");
			String repassword = input.next();
			if (!password.equals(repassword)) {
				System.out.println("�����������벻ƥ��");
				System.out.println("����������");
				continue;
			}
			// ������󣬷��͸�������
			User user = new User(username, password);

			Data request = new Data(false, "REGIST", user);

			Data response = send(request);

			if (response.isSuccess()) {
				// �����¼
				login();
			} else {
				// ��¼ʧ����ʾ������Ϣ������ע��
				System.out.println(response.getMsg());
				regist();
			}

		}

	}

	private void login() {

		while (true) {
			System.out.println("~~~~~~~~~~~~");
			System.out.println("��ǰ�������û���¼");
			System.out.println("~~~~~~~~~~~~");
			System.out.println("�������¼��");
			String username = input.next();
			System.out.println("����������");
			String password = input.next();

			// �����û����󣬷��͸��������Σ����ܷ��ؽ��
			User user = new User(username, password);

			// ������Ϣ�Ĺ����� �����һ����������
			Data data = new Data(false, "login", user, "");

			Data result = send(data);

			// ��¼�ɹ�
			if (result.isLogin()) {
				// ��ȡС˵����
				getNovel();

			} else {
				System.out.println("��¼ʧ�ܣ������µ�¼");
				continue;

			}

		}

	}

	private void getNovel() {
		// ��ӡ��¼�˵�(���Ǵӷ�����������)
		System.out.println("0.�����ϼ��˵�");
		System.out.println("1.����");
		System.out.println("2.����");
		System.out.println("��ѡ��");
		int choice = input.nextInt();
		switch (choice) {
		case 0:
			menu();// �������˵�
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
		
		// �������ݣ� ��ȡ����С˵���� ���ص���С˵�ļ���

		Data sendData = new Data(true, "readRomantic");

		Data result = null;
		result = send(sendData);

		List<Novel> novels = (List<Novel>) result.getList();
		System.out.println("����С˵�б�");
		System.out.println("���\t����\t����\t���");
		for (Novel novel : novels) {
			System.out.println(novel.getId() + "\t" + novel.getName() + "\t"
					+ novel.getAuthor() + "\t" + novel.getDetail());
		}
		System.out.println("С˵�б����");
		System.out.println("�Ķ���������ѡ���ţ��ϴ�TXT������-1,����������0:");
		
		int choice = input.nextInt();

		switch (choice) {
		case 0:
			getNovel();
			break;
		case -1:
			upload();
			break;
		default:
			//�ҵ��ƶ���С˵ ����С˵�ı������Ҫ�޸� ͨ�����峣���޸�
			Novel novel = novels.get(choice - 1);
			read(novel);
			break;
		}
		
		
	}

	// ��ȡ����С˵
	private void readKongfu() {

		// �������ݣ� ��ȡ����С˵���� ���ص���С˵�ļ���

		Data sendData = new Data(true, "readKongFu");

		Data result = null;
		result = send(sendData);

		List<Novel> novels = (List<Novel>) result.getList();
		System.out.println("����С˵�б�");
		System.out.println("���\t����\t����\t���");
		for (Novel novel : novels) {
			System.out.println(novel.getId() + "\t" + novel.getName() + "\t"
					+ novel.getAuthor() + "\t" + novel.getDetail());
		}
		System.out.println("С˵�б����");
		System.out.println("�Ķ���������ѡ���ţ��ϴ�TXT������-1,����������0:");
		
		int choice = input.nextInt();

		switch (choice) {
		case 0:
			getNovel();
			break;
		case -1:
			upload();
			break;
		default:
			//�ҵ��ƶ���С˵ ����С˵�ı������Ҫ�޸� ͨ�����峣���޸�
			Novel novel = novels.get(choice - 1);
			read(novel);
			break;
		}
		
	}

	// �ϴ�
	private void upload() {
		System.out.println("������С˵��");
		String novleName=input.next();
		System.out.println("����������");
		String author=input.next();
		System.out.println("��������:");
		String detail=input.next();
		System.out.println("�������ϴ���·��");
		String path=input.next();
		
		//�ϴ��ɹ�������·��
		saveUploadFile(path,novleName);
		
		Novel novel=new Novel(novleName,author,detail,"","����");
		
		System.out.println("�ϴ��ɹ���");
		
		
	}

	// �����ϴ��ļ�
	private void saveUploadFile(String path, String name) {
		File filefolder = new File(System.getProperty("user.dir"), "novels");
		if (!filefolder.exists()) {
			filefolder.mkdir();
		}
		// С˵�ļ�

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
		// ��ȡ���صĵ�ַ�����浽�����ļ���ʹ��IO
		System.out.println("���سɹ�");
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
			System.out.println("�ļ�д���쳣");
		}

		// ������ɺ� �����б�ҳ��
		getNovel();

	}

	private void read(Novel novel) {
		System.out.println(novel.getContent());
		System.out.println("������ʾ�б�������1,����TXT������2,������һ��0");
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
				System.out.println("�����µ�����");
			}
			// System.out.println("���ӳɹ�");
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
			System.out.println("���ؽ��");

			// �ر�socket;
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
