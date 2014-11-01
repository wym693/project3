package demo;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;


//服务器监听类 负责创建多多线程类
public class ServletLister {
	
	public static void main(String[] args) {
		
		ServerSocket sc=null;
		try {
			sc = new ServerSocket(8080);
			System.out.println("服务器启动成功");
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		
		while(true){
			
			try {
				
				Socket socket=sc.accept();
				System.out.println("获得用户连接");
				
				ServerThread st=new ServerThread(socket);
				st.start();
				
			} catch (IOException e) {
				System.out.println("服务器启动失败");
				e.printStackTrace();
			}
			
			
		}
		
		
		
	}

}
