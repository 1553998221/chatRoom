﻿package com.chat;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

public class TestSockServer {
	List<Client> clientList = new ArrayList<Client>();

	public void init() {
		ServerSocket server = null;
		Socket socket = null;
		try {
			// 打开端口，等待服务器连接。
			server = new ServerSocket(8888);
			System.out.println("服务器已开启！");
			// 服务端循环监听，若是有客户端连接，服务端接受并且创建一个线程，将连接通道socket放入该线程中，并启动线程。
			while (true) {
				//开通通道
				socket = server.accept();
				Client c = new Client(socket);
				clientList.add(c);// 将新连接的客户端存入list中，以便于后面遍历发送信息。
				c.start();
				//发送用户列表信息userlist
				updateUserList();
			}

		} catch (IOException e) {
			System.out.println("server over!");
		} finally {
			try {
				server.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	public static void main(String[] args) {
		TestSockServer ts = new TestSockServer();
		ts.init();
	}

	//按照规则遍历得到字符串字符串规则,aa,bb,cc,dd,
	public String getNameStr(){
		String str = ",";
		for (int i = 0; i < clientList.size(); i++) {
			str += clientList.get(i).name + ",";
		}
		return str;
	}
	//向所有客户端发送更新后的用户列表by getNameStr()
	public void updateUserList(){
		String usersStr = getNameStr();
		for (int i = 0; i < clientList.size(); i++) {
			try {
				new DataOutputStream(clientList.get(i).socket.getOutputStream()).writeUTF("");
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	// 因为需要启用多个线程来完成客户端的连接，所以这里使用内部类。
	class Client extends Thread {
		Socket socket;
		DataOutputStream dos;
		DataInputStream dis;
		String name;

		// 使用带参数的构造方法来将通道放入线程中。
		public Client(Socket socket) {
			this.socket = socket;
			name = "用户：" + socket.getPort();
		}

		@Override
		public void run() {
			String str = null;
			try {
				dos = new DataOutputStream(socket.getOutputStream());
				dis = new DataInputStream(socket.getInputStream());
				// 循环遍历用来接收某个客户端传来的信息，并且将这些信息遍历发送给各个客户端。
				while (true) {
					if ((str = dis.readUTF()) != null) {
						// 判断数据格式，如果#开始，更新自己的name属性，否则循环输出信息
						if (str.startsWith("#")) {
							name = str.substring(1);
						} else {
							// 循环遍历将信息发送给所有客户端。
							for (int i = 0; i < clientList.size(); i++) {
								// 判断客户是否有名字
									new DataOutputStream(clientList.get(i).socket.getOutputStream())
											.writeUTF(name + ":" + str);
							}
						}
					}
				}
			} catch (IOException e) {
				System.out.println("客户端" + socket.getPort() + "退出");
				clientList.remove(this);
				updateUserList();
				// e.printStackTrace();
			} finally {
				// 关闭各种流
				try {
					dis.close();
					dos.close();
					socket.close();
				} catch (IOException e) {
					e.printStackTrace();
				}

			}

		}
	}
}