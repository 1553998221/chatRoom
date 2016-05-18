package com.chat;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Scanner;

public class Client {

	public static void main(String[] args) {
		DataInputStream dis = null;
		DataOutputStream dos = null;
		Socket socket = null;
		String s = null;
		try {
			socket = new Socket("127.0.0.1", 8888);
			String str = null;
			Scanner input = new Scanner(System.in);
			do {
				dis = new DataInputStream(socket.getInputStream());
				dos = new DataOutputStream(socket.getOutputStream());
				str = input.nextLine();
				dos.writeUTF(str);
				if ((s = dis.readUTF()) != null) {
					System.out.println(s);
				}			
				} while (str != "88");
			System.out.println("用户端关闭！");

		} catch (IOException e) {
			System.out.println("用户端关闭！");
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
