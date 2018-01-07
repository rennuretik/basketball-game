package server;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

import server.constant.constant;
import server.game.Information;
import server.game.player;

public class Myserver {

	public static player[][][] Plot = new player[constant.type_number][2][constant.maxPlayer];
	public static String[][] TeamName = new String[constant.type_number][2];
	public static Socket[] Gamer = new Socket[constant.type_number]; // 容纳等待列表
	public static volatile int all_player = 0;
	public boolean[] Waiting = new boolean[constant.type_number];

	public static void main(String[] args) {
		Myserver Server = new Myserver();
		Server.initServer();
	}

	Myserver() {
		try {
			Information.loadAll(Plot, TeamName);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			System.out.println("Myserver");
		}
	}

	private void initServer() {
		while (true) {
			for(int i=0;i<constant.type_number;i++)
			{
				Waiting[i]=false;
			}
			
			
			try {
				// 创建服务器对象,并指定端口号
				ServerSocket server = new ServerSocket(9090);
				System.out.println("服务器已经建立......");
				// 不断获取客户端的连接
				while (true) {
					Socket temp = server.accept();
					System.out.println("连接建立......");
					InputStream ins = temp.getInputStream(); // 获取输入流
					OutputStream ous = temp.getOutputStream(); // 获取输出流
					int type = Integer.parseInt(ServerThread.readMsg(ins)); // 得到type
																			// 编号
					System.out.println(type);
					if (type < 0 || type >= constant.type_number) {
						ServerThread.sendMsg(ous, "9999");// Error!
					} else if (Waiting[type] == false)// put to wait
					{
						ServerThread.sendMsg(ous, "0");// Wait
						Gamer[type] = temp;
						Waiting[type] = true;
					} else {
						Waiting[type] = false;
						all_player += 2;
						ServerThread st = new ServerThread(Gamer[type], temp, type);
						st.start();
					}
					// 添加该客户端到容器中
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

}