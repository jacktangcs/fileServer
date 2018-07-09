package com.newer.client;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Scanner;

/**
 * 客户端
 * 
 * @author 汤振
 *
 */
public class App {

	//定义客户端套接字
	Socket socket;
	
	//定义端口
	int port = 9000;
	
	//定义服务地址IP(默认本地地址)
	String serverAddress = "";
	
	//定义文件输入流
	FileInputStream fileIn;
	
	//定义输出流
	OutputStream out;
	
	//定义客户端需要实现的任务
	public void start() {
		//获得要上传的文件
		Scanner scanner = new Scanner(System.in);
		System.out.print("请输入文件绝对路径:");
		String file = scanner.next();
		scanner.close();
		
		 try {
			 //通过服务地址和端口建立链接
			socket =  new Socket(serverAddress, port);
			out = socket.getOutputStream();
			byte[] buf = new byte[1024*4];
			int size;
			fileIn = new FileInputStream(file);
			while(-1!=(size = fileIn.read(buf))) {
				out.write(buf, 0, size);
				out.flush();
			}
			System.out.println("文件发送完毕");
		} catch (UnknownHostException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}finally {
			try {
				fileIn.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			try {
				socket.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	public static void main(String[] args) {
		App clientApp = new App();
		clientApp.start();
	}
}
