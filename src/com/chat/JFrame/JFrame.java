package com.chat.JFrame;

import java.awt.EventQueue;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;

import javax.swing.DefaultListModel;
import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.LayoutStyle.ComponentPlacement;
import javax.swing.ListSelectionModel;
import javax.swing.border.EmptyBorder;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

public class JFrame extends javax.swing.JFrame {

	private JPanel contentPane;
	private JTextField sendText;
	private JTextArea showText;
	private JTextField nameField;
	private JLabel userLabel;
	private JList userList;

	private DataInputStream dis;
	private DataOutputStream dos;
	private Socket socket;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					JFrame frame = new JFrame();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	public void init() {
		// 客户端连接服务器
		try {
			socket = new Socket("127.0.0.1", 8888);
			System.out.println("客户端已连接！");
			dos = new DataOutputStream(socket.getOutputStream());
			dis = new DataInputStream(socket.getInputStream());

			new Thread() {
				@Override
				public void run() {
					String s = null;
					try {
						while (true) {
							if ((s = dis.readUTF()) != null)
								if(s.startsWith(",")){
									addList(s);
								}else{
								showText.append(s + "\n");
								}
						}
					} catch (IOException e) {
						// e.printStackTrace();
					}
				}
			}.start();
		} catch (UnknownHostException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Create the frame.
	 */
	public void Enter() {
		try {
			dos.writeUTF(sendText.getText());
			sendText.setText("");
		} catch (IOException e1) {
			e1.printStackTrace();
		}
	}

	public void EnterName() {
		try {
			dos.writeUTF("#" + nameField.getText());
			nameField.setText("");
		} catch (IOException e1) {
			e1.printStackTrace();
		}
	}

	public JFrame() {
		addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent arg0) {
				try {
					dis.close();
					dos.close();
					socket.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		});
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 517, 375);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);

		JButton sendButton = new JButton("\u53D1\u9001");
		sendButton.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				Enter();
			}
		});

		sendText = new JTextField();
		sendText.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				Enter();
			}
		});
		sendText.setColumns(10);

		showText = new JTextArea();
		showText.setEditable(false);

		JLabel nameLabel = new JLabel("\u6635\u79F0");

		nameField = new JTextField();
		nameField.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				EnterName();
			}
		});
		nameField.setColumns(10);

		JButton nameButton = new JButton("\u63D0\u4EA4");
		nameButton.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				// 向服务器提交昵称：#昵称格式
				EnterName();
			}
		});
		nameButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
			}
		});

		userLabel = new JLabel("\u5728\u7EBF\u7528\u6237\uFF1A");

		userList = new JList();

		userList.addListSelectionListener(new ListSelectionListener() {
			public void valueChanged(ListSelectionEvent arg0) {
				Object[] values = userList.getSelectedValues();
			}
		});
		GroupLayout gl_contentPane = new GroupLayout(contentPane);
		gl_contentPane.setHorizontalGroup(gl_contentPane.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_contentPane.createSequentialGroup()
						.addGroup(gl_contentPane.createParallelGroup(Alignment.LEADING)
								.addGroup(gl_contentPane.createSequentialGroup().addGap(23).addComponent(nameLabel)
										.addPreferredGap(ComponentPlacement.UNRELATED)
										.addComponent(nameField, GroupLayout.PREFERRED_SIZE, 176,
												GroupLayout.PREFERRED_SIZE)
										.addGap(36).addComponent(nameButton))
						.addGroup(gl_contentPane.createSequentialGroup().addContainerGap()
								.addGroup(gl_contentPane.createParallelGroup(Alignment.LEADING)
										.addGroup(gl_contentPane.createSequentialGroup()
												.addComponent(sendText, GroupLayout.PREFERRED_SIZE, 248,
														GroupLayout.PREFERRED_SIZE)
												.addGap(18).addComponent(sendButton))
								.addComponent(showText, GroupLayout.PREFERRED_SIZE, 324, GroupLayout.PREFERRED_SIZE))))
				.addGroup(gl_contentPane.createParallelGroup(Alignment.TRAILING)
						.addGroup(gl_contentPane.createSequentialGroup().addGap(32).addComponent(userLabel)
								.addContainerGap(GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
						.addGroup(gl_contentPane.createSequentialGroup().addPreferredGap(ComponentPlacement.RELATED)
								.addComponent(userList, GroupLayout.PREFERRED_SIZE, 114, GroupLayout.PREFERRED_SIZE)
								.addContainerGap()))));
		gl_contentPane.setVerticalGroup(gl_contentPane.createParallelGroup(Alignment.TRAILING)
				.addGroup(gl_contentPane.createSequentialGroup().addGap(19)
						.addGroup(gl_contentPane.createParallelGroup(Alignment.BASELINE).addComponent(nameLabel)
								.addComponent(nameField, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE,
										GroupLayout.PREFERRED_SIZE)
								.addComponent(nameButton).addComponent(userLabel))
				.addPreferredGap(ComponentPlacement.RELATED)
				.addGroup(gl_contentPane.createParallelGroup(Alignment.TRAILING)
						.addComponent(showText, GroupLayout.PREFERRED_SIZE, 207, GroupLayout.PREFERRED_SIZE)
						.addComponent(userList, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
				.addGap(26)
				.addGroup(gl_contentPane.createParallelGroup(Alignment.BASELINE).addComponent(sendText,
						GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
						.addComponent(sendButton)).addContainerGap()));
		contentPane.setLayout(gl_contentPane);
		init();
	}

	public void addList(String str) {
		String strs[] = str.split(",");
		DefaultListModel<String> dlm = new DefaultListModel<String>();
		dlm.addElement("aa");
		dlm.addElement("bb");
		userList.setModel(dlm);

	}
}
