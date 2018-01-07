package server;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;

import server.constant.constant;
import server.game.gameOn;

public class ServerThread extends Thread {

	public Socket gamer1;
	public Socket gamer2;

	public InputStream ins1;
	public OutputStream ous1;
	public InputStream ins2;
	public OutputStream ous2;
	public gameOn GAME;
	public volatile boolean End = false;// Whether the game is ended
	public volatile boolean RotateAllowed = false;
	public volatile boolean Sending = false;// set to true after all below is OK
	// need to set OK
	public volatile String SendingWords;
	public volatile int Score[] = new int[2];// Score for team A and B
	public volatile int TimeRemainning;

	private int[][] Decision = new int[6][];// �洢������ս������

	public ServerThread(Socket socket, Socket socket2, int type) throws IOException {
		this.gamer1 = socket;
		this.gamer2 = socket2;
		ins1 = socket.getInputStream();
		ous1 = socket.getOutputStream();
		ins2 = socket2.getInputStream();
		ous2 = socket2.getOutputStream();
		GAME = new gameOn(type, this);
	}

	public void run() {

		// to store coach's decision
		for (int i = 0; i < 4; i++) {
			Decision[i] = new int[5];
		}
		for (int i = 0; i < 2; i++) {
			Decision[i + 4] = new int[1];
		}

		try {
			// ���ӳɹ��ź�
			sendMsg(ous1, "1");// �ͻ���1���ӳɹ�
			sendMsg(ous2, "1");// �ͻ���2���ӳɹ�

			// �ַ���ǩ���
			sendMsg(ous1, "0");// 1�ſͻ��˳ַ���ǩ���
			sendMsg(ous2, "1");// 2�ſͻ��˳ַ���ǩ���

			sendMsg(ous1,Myserver.TeamName[GAME.gameType][0]); //���Ͷ������κ�һ��������Ƚ��յ��ľ����Լ����飬����ܵ����ǶԷ�����
			sendMsg(ous1,Myserver.TeamName[GAME.gameType][1]); //���Ͷ������κ�һ��������Ƚ��յ��ľ����Լ����飬����ܵ����ǶԷ�����


			sendMsg(ous2,Myserver.TeamName[GAME.gameType][1]); //���Ͷ������κ�һ��������Ƚ��յ��ľ����Լ����飬����ܵ����ǶԷ�����
			sendMsg(ous2,Myserver.TeamName[GAME.gameType][0]); //���Ͷ������κ�һ��������Ƚ��յ��ľ����Լ����飬����ܵ����ǶԷ�����
			
			// ��Ա����+���
			for (int i = 0; i < constant.maxPlayer; i++)// ��1�ſͻ��˷�����Ա���������
			{
				sendMsg(ous1, GAME.Team[constant.TeamA][i].getNamePlayer());
				sendMsg(ous1, String.valueOf(GAME.Team[constant.TeamA][i].getIndexPlayer()));
			}
			for (int i = 0; i < constant.maxPlayer; i++)// ��2�ſͻ��˷�����Ա���������
			{
				sendMsg(ous2, GAME.Team[constant.TeamB][i].getNamePlayer());
				sendMsg(ous2, String.valueOf(GAME.Team[constant.TeamB][i].getIndexPlayer()));
			}
			//���ͻ��˷��ͶԷ���Ա���������
			for (int i = 0; i < 5; i++)// ��1�ſͻ��˷��ͶԷ���Ա���������
			{
				sendMsg(ous1, GAME.Team[constant.TeamB][i].getNamePlayer());
				sendMsg(ous1, String.valueOf(GAME.Team[constant.TeamB][i].getIndexPlayer()));
			}
			for (int i = 0; i < 5; i++)// ��1�ſͻ��˷��ͶԷ���Ա���������
			{
				sendMsg(ous2, GAME.Team[constant.TeamA][i].getNamePlayer());
				sendMsg(ous2, String.valueOf(GAME.Team[constant.TeamA][i].getIndexPlayer()));
			}
			// ��ʼ�趨
			System.out.println("��Ϸ��ʼǰ");

			refreshStrategy();// ��ȡ��������
			System.out.println("��Ϸ��ʼǰ1");

			GAME.start();//��Ϸ��ʼ
			System.out.println("��Ϸ��ʼ��!");
			while (true) {
				if (End == true) {// ��������
					sendMsg(ous1, "5555");// 1�ſͻ�����Ϸ���������ź�
					sendMsg(ous2, "5555");// 2�ſͻ�����Ϸ���������ź�
					sendMsg(ous1, "Game over");// 1�ſͻ�����Ϸ���������ź�
					sendMsg(ous2, "Game over");// 1�ſͻ�����Ϸ���������ź�
					sendMsg(ous1, SendingWords);
					sendMsg(ous2, SendingWords);// �������ս��
					System.out.println("��������");
					break;
				}

				if (Sending == true) {
						System.out.println("����������Ϣ!");
						sendMsg(ous1, "8888");// 1�ſͻ����������ο�ʼ�ź�
						sendMsg(ous2, "8888");// 2�ſͻ����������ο�ʼ�ź�
						System.out.println("�����������");
						sendMsg(ous1, SendingWords);// 1�ſͻ�����ʾ���
						sendMsg(ous2, SendingWords);// 2�ſͻ�����ʾ���
	
						sendMsg(ous1, String.valueOf(Score[constant.TeamA]));// 1�ſͻ��˱ȷ�
						sendMsg(ous1, String.valueOf(Score[constant.TeamB]));// 1�ſͻ��˱ȷ�
	
						sendMsg(ous2, String.valueOf(Score[constant.TeamA]));// 2�ſͻ��˱ȷ�
						sendMsg(ous2, String.valueOf(Score[constant.TeamB]));// 2�ſͻ��˱ȷ�
	
						sendMsg(ous1, String.valueOf(TimeRemainning));// 1�ſͻ���ʣ��ʱ��
						sendMsg(ous2, String.valueOf(TimeRemainning));// 2�ſͻ���ʣ��ʱ��
	
//						sendMsg(ous1, "9999");// 1�ſͻ����������ν����ź�
//						sendMsg(ous2, "9999");// 2�ſͻ����������ν����ź�
						
						Sending=false;
				}
				else{
					//����
					sendMsg(ous1, "9999");
					sendMsg(ous2, "9999");
				}
				String Read=readMsg(ins1);
				//��ȡ����or ��ͣ
				if (Read.equals("9999")==false) {
					// ����ͻ���1����Ҫ��
					if (Read.equals("4444"))// �ͻ���ǿ���˳�
					{
						sendMsg(ous2, "4444");// �쳣�˳��ź�
						break;

					} else if (Read.equals("6666")) {// �ͻ���1������ͣ
						sendMsg(ous1, "6666");// 1�ſͻ��˷�����ͣ�ź�
						sendMsg(ous2, "6666");// 2�ſͻ��˷�����ͣ�ź�
						sendMsg(ous1, Myserver.TeamName[GAME.gameType][0]+" calls for timeout");// 1�ſͻ��˷�����ͣ�ź�
						sendMsg(ous2, Myserver.TeamName[GAME.gameType][0]+" calls for timeout");// 2�ſͻ��˷�����ͣ�ź�
						Timeout();
						continue;
					}
					else if(Read.equals("5555"))//����
					{
						refreshChangePlayer(constant.TeamA);
					}
				}
				Read=readMsg(ins2);
				if (Read.equals("9999")==false) {
					// ����ͻ���2����Ҫ��
					if (Read.equals("4444"))// �ͻ���ǿ���˳�
					{
						sendMsg(ous1, "4444");// �쳣�˳��ź�
						break;

					} else if (Read.equals("6666")) {// �ͻ���2������ͣ
						sendMsg(ous1, "6666");// 1�ſͻ��˷�����ͣ�ź�
						sendMsg(ous2, "6666");// 2�ſͻ��˷�����ͣ�ź�
						sendMsg(ous1, Myserver.TeamName[GAME.gameType][1]+" calls for timeout");// 1�ſͻ��˷�����ͣ�ź�
						sendMsg(ous2, Myserver.TeamName[GAME.gameType][1]+" calls for timeout");// 2�ſͻ��˷�����ͣ�ź�
						Timeout();
					}
					else if(Read.equals("5555"))//����
					{
						refreshChangePlayer(constant.TeamB);
					}
				}

				try {
					Thread.sleep(3000);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}

		} catch (Exception e) {
			// TODO Auto-generated catch block
			try {
				ins1.close();
				ous1.close();
				ins2.close();
				ous2.close();
				gamer1.close();
				gamer2.close();
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}

		}
	}



	private void refreshStrategy() {
		// �ĸ�5
		try {
			for (int i = 0; i < 4; i++)// �ͻ���1
				for (int j = 0; j < 5; j++)
					Decision[i][j] = Integer.parseInt(readMsg(ins1));
			// ��������ս��
			for (int i = 0; i < 2; i++) {
				Decision[i + 4][0] = Integer.parseInt(readMsg(ins1));
			}

			GAME.ChangeStrategy(constant.TeamA, Decision);// ֱ�Ӱ����ݸ�GAME
			for (int i = 0; i < 4; i++)// �ͻ���2
				for (int j = 0; j < 5; j++)
					Decision[i][j] = Integer.parseInt(readMsg(ins2));

			// ��������ս��
			for (int i = 0; i < 2; i++) {
				Decision[i + 4][0] = Integer.parseInt(readMsg(ins2));
			}

			GAME.ChangeStrategy(constant.TeamB, Decision);
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	private void Timeout()
	{
		refreshStrategy();// ��ȡ���µ�ս������

		String timeoutEndA=null ; //����1�Ļ�Ӧ
		String timeoutEndB=null;//����2�Ļ�Ӧ
		try {
			timeoutEndA=readMsg(ins1);
			timeoutEndB = readMsg(ins2);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 

		if (timeoutEndA.equals("7777"))// �л�����ͣ
		{
			refreshChangePlayer(constant.TeamA);
		} 
		if (timeoutEndB.equals("7777"))// �ͻ���B�л�����ͣ
		{
			refreshChangePlayer(constant.TeamB);
		} 
		/*
		if(timeoutEndA!="7777"&&timeoutEndA!="8888"&& timeoutEndB!="7777"&& timeoutEndB!="8888")
		{
			System.out.println("��ͣ��������");
		}
		*/
		try {
			sendMsg(ous1,"9999");//��ͣ����
			sendMsg(ous2,"9999");

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		
	}
	
	
	
	private void refreshChangePlayer(int INDEX) {

		InputStream in=(INDEX==constant.TeamA)?ins1:ins2;
		String Name=Myserver.TeamName[GAME.gameType][INDEX];

			int[] index=new int[5];
		
		
			try {

				sendMsg(ous1,"7777");
				sendMsg(ous2,"7777");//���������ź�
				
				
				
				sendMsg(ous1,Name+" calls for player change");
				sendMsg(ous2,Name+" calls for player change");//�������

				//���ͻ��˺�
				sendMsg(ous1,String.valueOf(INDEX));
				sendMsg(ous2,String.valueOf(INDEX));//��һ������
				
				
				
				for(int i=0;i<5;i++)
				{
					index[i]=Integer.parseInt(readMsg(in));
				}
				
				GAME.ChangePlayer(INDEX, index);//��ֵ��Game
				
				for(int i=0;i<5;i++)
				{
					//��5��λ�õ�ֵ���������ͻ���
					//����+���
					sendMsg(ous1,GAME.Team[INDEX][index[i]].getNamePlayer());
					sendMsg(ous1,String.valueOf(index[i]));
					sendMsg(ous2,GAME.Team[INDEX][index[i]].getNamePlayer());
					sendMsg(ous2,String.valueOf(index[i]));//������������֮������
				}
				
				
				
			} catch (Exception e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			
			
			
			
			
		try {
			sendMsg(ous1,"Player change over");//���˽���
			sendMsg(ous2,"Player change over");

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	// ��ȡ�ͻ����������ݵĺ���
	public static String readMsg(InputStream ins) throws Exception {
		// ��ȡ�ͻ��˵���Ϣ
		int value = ins.read();

		// ��ȡ���� ��ȡ���س���13�����У�10��ʱֹͣ��
		String str = "";
		while (value != 10) {
			// ����رտͻ���ʱ�᷵��-1ֵ
			if (value == -1) {
				throw new Exception();
			}
			str = str + ((char) value);
			value = ins.read();
		}
		str = str.trim();
		return str;
	}
	
	// ������Ϣ�ĺ���
	public static void sendMsg(OutputStream os, String s) throws IOException {
		// ��ͻ��������Ϣ
		byte[] bytes = s.getBytes();
		os.write(bytes);
		os.write(10);
		os.flush();

	}

}