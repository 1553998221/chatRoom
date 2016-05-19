package com.chat;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Scanner;

public class Client {

	static DataInputStream dis = null;

	public static void main(String[] args) {
		DataOutputStream dos = null;
		Socket socket = null;
		String str = null;
		Scanner input = new Scanner(System.in);
		try {
			socket = new Socket("127.0.0.1", 8888);
			do {
				dis = new DataInputStream(socket.getInputStream());
				dos = new DataOutputStream(socket.getOutputStream());
				//匿名内部类
				new Thread() {
					@Override
					public void run() {
						String s = null;
						try {
							if ((s = dis.readUTF()) != null) {
								System.out.println(s);
							}
						} catch (IOException e) {
							e.printStackTrace();
						}
					}
				}.start();

				str = input.nextLine();
				dos.writeUTF(str);
			} while (!str.equals("88"));
			System.out.println("用户端关闭！");

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
