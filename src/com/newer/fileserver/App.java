package com.newer.fileserver;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.math.BigInteger;
import java.net.ServerSocket;
import java.net.Socket;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * 服务端
 * 
 * @author 汤振
 *
 */
public class App {

	//服务端套接字
	ServerSocket serverSocket;
	
	//定义服务端端口
	int port = 9000;
	
	//定义线程池
	ExecutorService pool;
	
	//定义文件保存地址
	String filePath = "E://files";
	public void start() {
		try {
			serverSocket = new ServerSocket(port);
			pool = Executors.newCachedThreadPool();

			while(true) {
				Socket socket = serverSocket.accept();
				pool.execute(new Runnable() {
					
					@Override
					public void run() {
						ByteArrayOutputStream data = new ByteArrayOutputStream();
						try(InputStream in = socket.getInputStream()) {
							byte[] buf = new byte[1024*4];
							int size;
							while(-1!=(size = in.read(buf))) {
								data.write(buf, 0, size);
							}
						} catch (Exception e) {
							// TODO: handle exception
						}
						//接收到的数据
						byte[] info = data.toByteArray();
						
						//利用SHA-256生产的16进制散列值作为文件名
						String fileName = "";
						
						byte[] hash = null;
						
						//获得文件的散列值,定义文件名
						try {
							hash = MessageDigest.getInstance("SHA-256").digest(info);
							fileName = new BigInteger(1,hash).toString(16);
							
						} catch (NoSuchAlgorithmException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
				
						HashMap<byte[], String> map = new HashMap<byte[], String>();
						if(!map.containsKey(hash)) {
							map.put(hash, fileName);
							try(FileOutputStream out = new FileOutputStream(new File(filePath, fileName))) {
								out.write(info);
								out.flush();
								System.out.println("上传完成");
							} catch (Exception e) {
								System.out.println("上传失败");
							}
						}else {
							System.out.println("文件已经存在，请不要重复上传");
							return;
						}
						
						//接收文件
//						try(FileOutputStream out = new FileOutputStream(new File(filePath, fileName))) {
//							out.write(info);
//							out.flush();
//							System.out.println("上传完成");
//						} catch (Exception e) {
//							System.out.println("上传失败");
//						}
					}
				});
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	public static void main(String[] args) {
		App serverApp = new App();
		serverApp.start();
	}
}
