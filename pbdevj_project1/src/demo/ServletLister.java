package demo;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;


//������������ ���𴴽�����߳���
public class ServletLister {
	
	public static void main(String[] args) {
		
		ServerSocket sc=null;
		try {
			sc = new ServerSocket(8080);
			System.out.println("�����������ɹ�");
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		
		while(true){
			
			try {
				
				Socket socket=sc.accept();
				System.out.println("����û�����");
				
				ServerThread st=new ServerThread(socket);
				st.start();
				
			} catch (IOException e) {
				System.out.println("����������ʧ��");
				e.printStackTrace();
			}
			
			
		}
		
		
		
	}

}
