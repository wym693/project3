package xml;

import java.io.FileWriter;
import java.io.IOException;
/**
 * 使用字符输出流输出字符串到指定文件中。
 *
 */
public class TestIO2 {
	public static void main(String[] args) {
		String str = "Hello World!欢迎您!";
		FileWriter fw = null;
		try {
			// 1、创建字符输出流对象，负责向c:\hello.txt写入数据
			fw = new FileWriter("c:\\hello.txt");
			// 2、把str的内容的写入到fw所指文件中
			fw.write(str);
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				// 3、一定要关闭输出流
				if (null != fw)
					fw.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
}
