package Basketballmanager.client;

import java.awt.Toolkit;

import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;

import javax.swing.ImageIcon;

import Basketballmanager.constant.Const_Game;
import Basketballmanager.gui.gamechoose.background;
import Basketballmanager.gui.onGame.controlPanelDefense;
import Basketballmanager.gui.onGame.controlPanelOffense;
import Basketballmanager.gui.onGame.gamePanel;
import Basketballmanager.util.ImgUtil;

public class Myclient extends Thread {
	private int GameType;
	private boolean ExceptionEnd = false;
	private volatile boolean ReadyChange = false;
	// private int[] Score =new int[2];
	// private int remainningTime=0;//ʣ��ʱ��
	private int TeamType;
	public static Socket client = null;
	public static int[] PlayerIndexMe = new int[Const_Game.maxPlayer];
	public static int[] PlayerIndexThem = new int[5];
	public static volatile boolean RotatePlayerThis = false;
	public static volatile boolean TimeoutThis = false;

	public static ImageIcon[][] PlayerPicture = new ImageIcon[2][Const_Game.maxPlayer];// 0
																						// ��ŵ����Լ����飬1
																						// ��ŵ��ǶԷ�����
    private int TimeoutNumber=5;//����ͣ��Ŀ

	public void setType(int type) {
		GameType = type;
	}

	public void setReadyChange(boolean Change) {
		ReadyChange = Change;
	}

	public void run() {

		// ������ʼ��
		BackToInitialization();

		try {
			// �����ͻ��˶���
			//client = new Socket("192.168.229.1", 9090);
			//client = new Socket("47.100.124.11", 9090);
			client = new Socket("192.168.229.1", 9090);
			System.out.println("������");
			// ��ȡ�ͻ��˵����������
			final InputStream ins = client.getInputStream();
			final OutputStream ous = client.getOutputStream();

			sendMsg(ous, String.valueOf(GameType)); // ���͵�ǰ��Ϸ���
			while (true) {
				String read = readMsg(ins);
				
				if (read .equals("9999") ) {
					// ������쳣
					ExceptionEnd = true;
					break;
				} else if (read.equals("1") )
					// ���ӳɹ�
					break;
				Thread.sleep(100);
			}
			if (ExceptionEnd == true) {// ��⵽�����˳�
				BackToInitialization();
				return;
			}
			// ֻ��������������ʱ�ſ��Ա��

			while (true) {
				if (ReadyChange == true)
					break;
				Thread.sleep(100);
			}

			// ��һЩ�Ա������Ĵ���
			background.processBar.setVisible(false);
			// ������Ҫ���ݵ�ǰ��GameType ����ͬ�Ĵ���
			// if .....
			// �Ա����ļ����Ĵ���
			background.image = Toolkit.getDefaultToolkit()
					.createImage(background.class.getResource("/img/Game2015.png"));
			background.GameMain.setVisible(true);

			// ������Ҫ���ݵ�ǰ��GameType ����ͬ�Ĵ���
			TeamType = Integer.parseInt(readMsg(ins));

			gamePanel.TeamNameA.setText(readMsg(ins));
			gamePanel.TeamNameB.setText(readMsg(ins));

			// �����ļ�
			if (TeamType == Const_Game.TeamAindex)// ����Լ��Ƕ���A
			{
				for (int t = 0; t < 2; t++) {// ��������
					for (int i = 0; i < Const_Game.maxPlayer; i++) {
						PlayerPicture[t][i] = ImgUtil.getImageIcon(GameType, t, i);
					}
				}
			} else {// ����Լ��Ƕ���B

				for (int i = 0; i < Const_Game.maxPlayer; i++) {
					PlayerPicture[0][i] = ImgUtil.getImageIcon(GameType, 1, i);// 0
																				// ����1,1����0
				}
				for (int i = 0; i < Const_Game.maxPlayer; i++) {
					PlayerPicture[1][i] = ImgUtil.getImageIcon(GameType, 0, i);// 0
																				// ����1,1����0
				}
			}

			// ���ܳ�ʼ��Ա���������
			// ***** 15���Լ���

			for (int i = 0; i < Const_Game.maxPlayer; i++) {
				if (i < 5) {
					background.GameMain.setName(i, readMsg(ins), 0);
					PlayerIndexMe[i] = Integer.parseInt(readMsg(ins));// ������Ա���
					background.GameMain.setImageByImageIcon(i, PlayerPicture[0][PlayerIndexMe[i]],
							Const_Game.TeamAindex);
				} else {
					gamePanel.ControlBar.PlayerDetail.rotation[i - 5].setName(readMsg(ins));
					PlayerIndexMe[i] = Integer.parseInt(readMsg(ins));// ������Ա���
					gamePanel.ControlBar.PlayerDetail.rotation[i - 5]
							.setImageByImageIcon(PlayerPicture[0][PlayerIndexMe[i]]);
				}

			}
			// ******
			// 5���Է���
			for (int i = 0; i < 5; i++) {
				background.GameMain.setName(i, readMsg(ins), 1);
				PlayerIndexThem[i] = Integer.parseInt(readMsg(ins));
				background.GameMain.setImageByImageIcon(i, PlayerPicture[1][PlayerIndexThem[i]], Const_Game.TeamBindex);// ���öԷ�i��λ���ϵ���Ա��Ƭ
			}
			SendStre(ous);
			while (true) {
				// ������Ϣѭ��
				String receiver = readMsg(ins);
				switch (receiver) {
				case "5555":
					background.GameMain.Center_boardcast.append(readMsg(ins) + "\n");// Game Over
					background.GameMain.Center_boardcast.append(readMsg(ins) + "\n");// Final result
					return;
				case "4444":
					System.out.println("�쳣�˳�");
					break;
				case "8888":// ��������
					background.GameMain.Center_boardcast.append(readMsg(ins) + "\n");// ��ʾ���
					if (TeamType == Const_Game.TeamAindex) {
						// ��������������1
						gamePanel.ScoreA.setText(readMsg(ins));
						gamePanel.ScoreB.setText(readMsg(ins));
					} else {
						gamePanel.ScoreB.setText(readMsg(ins));
						gamePanel.ScoreA.setText(readMsg(ins));
					}
					SetTimeRemaining(Integer.parseInt(readMsg(ins)));
					break;
				case "6666":
					// ��ͣ
					background.GameMain.Center_boardcast.append(readMsg(ins) + "\n");// ��ʾ���
																						// ������ͣ
					
					Thread.sleep(10000);
					background.GameMain.Center_boardcast.append("Time out over\n");

					// ���ͽ�������ս��
					SendStre(ous);
					if (RotatePlayerThis == true) {
						sendMsg(ous, "7777");// �л�����ͣ
						for (int i = 0; i < 5; i++)// ���廻�����
							sendMsg(ous, String.valueOf(PlayerIndexMe[i]));
						RotatePlayerThis = false;
					} else
						sendMsg(ous, "8888");// �޻�����ͣ

					while (readMsg(ins).equals( "9999")==false) {
						AnswerToChangePlayer(ins);
					}
					background.GameMain.Center_boardcast.append("The game is on\n");
					break;
				case "7777":
					// ���˽��˻���
					AnswerToChangePlayer(ins);
					break;
				}
				// ��������������Ϣ�������Լ�Ҫ����

				if (TimeoutThis == true) { // ��ͣ
					if(TimeoutNumber>0)
					{
					TimeoutThis = false;
					sendMsg(ous, "6666");
					TimeoutNumber--;
					gamePanel.ControlBar.BarChoose.TimeOutShort.setText("��ͣ"+"("+String.valueOf(TimeoutNumber)+")");//���������
					if(TimeoutNumber<=0)
						gamePanel.ControlBar.BarChoose.TimeOutShort.setEnabled(false);
					}
					else{
						System.out.println("��ͣ��������");
					}
				} else if (RotatePlayerThis == true) {
					// ����
					sendMsg(ous, "5555");
					for (int i = 0; i < 5; i++)
						sendMsg(ous, String.valueOf(PlayerIndexMe[i]));
					RotatePlayerThis = false;
				} else
					sendMsg(ous, "9999");

				Thread.sleep(100);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		// ������Ϣ�ü�����

		BackToInitialization();
	}

	public String readMsg(InputStream ins) throws Exception {
		int value = ins.read();
		String str = "";
		while (value != 10) {
			// �����ͻ��˲������ر�
			if (value == -1) {
				throw new Exception();
			}
			str = str + (char) value;
			value = ins.read();
		}
		str = str.trim();
		return str;
	}

	// ������Ϣ�ĺ���
	public void sendMsg(OutputStream ous, String str) throws Exception {
		byte[] bytes = str.getBytes();
		ous.write(bytes);
		ous.write(10);
		ous.flush();
	}

	private void BackToInitialization() {
		ExceptionEnd = false;
		ReadyChange = false;// ������λ
		gamePanel.ScoreB.setText("0");
		gamePanel.ScoreA.setText("0");
		SetTimeRemaining(Const_Game.MaxPlayTime);

	}

	/**
	 * ��ս����Ϣ����������,����20��ֵ��2��ս��ѡ��
	 */
	private void SendStre(OutputStream ous) {
		int[] a = controlPanelOffense.organize.getStre();
		for (int i = 0; i < 5; i++) {
			try {
				sendMsg(ous, String.valueOf(a[i]));
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		a = controlPanelOffense.scoreArr.getStre();
		for (int i = 0; i < 5; i++) {
			try {
				sendMsg(ous, String.valueOf(a[i]));
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		a = controlPanelOffense.attack.getStre();
		for (int i = 0; i < 5; i++) {
			try {
				sendMsg(ous, String.valueOf(a[i]));
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		a = controlPanelDefense.organize.getStre();
		for (int i = 0; i < 5; i++) {
			try {
				sendMsg(ous, String.valueOf(a[i]));
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		try {
			sendMsg(ous, String.valueOf(controlPanelOffense.Offense.getChoose()));// ���ͽ�������
			sendMsg(ous, String.valueOf(controlPanelDefense.defense.getChoose()));// ���ͷ��ز���

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	private void AnswerToChangePlayer(InputStream ins) {
		// ���˽��˻���
		try {
			background.GameMain.Center_boardcast.append(readMsg(ins) + "\n");// ���뻻��

			int teamCallingChange = Integer.parseInt(readMsg(ins));// ��ȡ���˺�
			if (teamCallingChange == TeamType)// �Լ��еĻ���
			{
				for (int i = 0; i < 10; i++)
					readMsg(ins);// ��10������Ϊ(name+index)
			} else {// �Է�����
				for (int i = 0; i < 5; i++) {
					background.GameMain.setName(i, readMsg(ins), 1);
					PlayerIndexThem[i] = Integer.parseInt(readMsg(ins));
					background.GameMain.setImageByImageIcon(i, PlayerPicture[1][PlayerIndexMe[i]],
							Const_Game.TeamBindex);// ���öԷ�i��λ���ϵ���Ա��Ƭ
				}
			}

			background.GameMain.Center_boardcast.append(readMsg(ins) + "\n");// ���˽���
		} catch (Exception e) {
			System.out.println("AnswerToChangePlayer ���ô���");
			e.printStackTrace();

		}
	}
	
	private void SetTimeRemaining(int time)
	{
		//����ڼ���
		//gamePanel.TimeRemainning.setText(readMsg(ins));
		if(time>Const_Game.MaxPlayTime)
		{
			//��ʱ��
			int extra=time-Const_Game.MaxPlayTime;
			int T1=extra/Const_Game.ExtraTime;//��ʱ����
			int T2=extra%Const_Game.ExtraTime;//��ʱ����
			int T_m=T2/60;
			int T_s=T2%60;
			String second=(T_s>=10)?String.valueOf(T_s):"0"+String.valueOf(T_s);
			gamePanel.TimeRemainning.setText("��ʱ��"+String.valueOf(T1+1)+" "+"0"+String.valueOf(T_m)+":"+second);
			return;
		}
		if(time==Const_Game.MaxPlayTime)
		{
			gamePanel.TimeRemainning.setText("��һ�� 12:00");
			return;
		}
		int past_time=Const_Game.MaxPlayTime-time;//�Ѿ���ȥ��ʱ��
		
			int a=past_time/720;
			int b=time%720;
			int minutes=b/60;
			int seconds=b%60;
			String Toset=null;
			switch(a){
			case 0://��һ��
				Toset="��һ�� ";
				break;
			case 1://�ڶ���
				Toset="�ڶ��� ";
				break;
			case 2://������
				Toset="������ ";
				break;
			case 3://���Ľ�
				Toset="���Ľ� ";
				break;
			}
			if(minutes>=10)
				Toset+=String.valueOf(minutes);
			else
				Toset+="0"+String.valueOf(minutes);//������
			Toset+=":";
			if(seconds>=10)
				Toset+=String.valueOf(seconds);
			else
				Toset+="0"+String.valueOf(seconds);//������
			gamePanel.TimeRemainning.setText(Toset);
		
	}
	
}