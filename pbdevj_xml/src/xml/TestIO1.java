package xml;

import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
/**
 * 使用字符输入流读取文件内容并输出，文件内容是"hello world!"。
 *
 */
public class TestIO1 {
	public static void main(String[] args) {
		Reader fr = null;
		int length = 0;
		char ch[] = null;
		try {
			// 1、创建字符输入流对象，负责读取c:\hello.txt文件
			fr = new FileReader("c:/hello.txt");
			// 2、创建中转站数组，存放读取的内容
			ch = new char[1024];
			// 3、读取文件内容到ch数组
			length = fr.read(ch);
			// 4、输出保存在ch数组中的文件内容
			System.out.println(new String(ch, 0, length));
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			// 5、一定要关闭输入流
			try {
				if (null != fr)
					fr.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
}
