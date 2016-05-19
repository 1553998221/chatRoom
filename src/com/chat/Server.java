package com.chat;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

public class Server {
	static List<Client> clientList = new ArrayList<Client>();

	public static void main(String[] args) {
		Socket socket = null;
		try {
			ServerSocket s = new ServerSocket(8888);
			System.out.println("服务器已启动！");
			while (true) {
				socket = s.accept();
				Client c = new Server().new Client(socket);
				clientList.add(c);
				c.start();
			}
		} catch (IOException e) {
			System.out.println("服务器端关闭！");
		}
	}

	class Client extends Thread {
		DataInputStream dis = null;
		DataOutputStream dos = null;
		Socket socket = null;

		public Client(Socket socket) {
			this.socket = socket;
		}

		@Override
		public void run() {

			try {
				dis = new DataInputStream(socket.getInputStream());
				dos = new DataOutputStream(socket.getOutputStream());
				String str = null;
				if ((str = dis.readUTF()) != null) {
					System.out.println("客户端说：" + str);
				}
				// dos.writeUTF(socket.getPort() + "说：" + str);
				for (int i = 0; i < clientList.size(); i++) {
					new DataOutputStream(clientList.get(i).socket.getOutputStream())
							.writeUTF(socket.getPort() + ":" + str);
				}
			} catch (IOException e) {
				e.printStackTrace();
			} finally {
				try {
					dis.close();
					dos.close();
					socket.close();
				} catch (Exception e2) {
					e2.printStackTrace();
				}
			}
		}
	}
}
