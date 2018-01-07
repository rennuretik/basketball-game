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
	// private int remainningTime=0;//剩余时间
	private int TeamType;
	public static Socket client = null;
	public static int[] PlayerIndexMe = new int[Const_Game.maxPlayer];
	public static int[] PlayerIndexThem = new int[5];
	public static volatile boolean RotatePlayerThis = false;
	public static volatile boolean TimeoutThis = false;

	public static ImageIcon[][] PlayerPicture = new ImageIcon[2][Const_Game.maxPlayer];// 0
																						// 存放的是自己队伍，1
																						// 存放的是对方队伍
    private int TimeoutNumber=5;//总暂停数目

	public void setType(int type) {
		GameType = type;
	}

	public void setReadyChange(boolean Change) {
		ReadyChange = Change;
	}

	public void run() {

		// 基本初始化
		BackToInitialization();

		try {
			// 创建客户端对象
			//client = new Socket("192.168.229.1", 9090);
			//client = new Socket("47.100.124.11", 9090);
			client = new Socket("192.168.229.1", 9090);
			System.out.println("已连接");
			// 获取客户端的输入输出流
			final InputStream ins = client.getInputStream();
			final OutputStream ous = client.getOutputStream();

			sendMsg(ous, String.valueOf(GameType)); // 发送当前游戏编号
			while (true) {
				String read = readMsg(ins);
				
				if (read .equals("9999") ) {
					// 编号有异常
					ExceptionEnd = true;
					break;
				} else if (read.equals("1") )
					// 连接成功
					break;
				Thread.sleep(100);
			}
			if (ExceptionEnd == true) {// 检测到错误，退出
				BackToInitialization();
				return;
			}
			// 只有在允许变更面板时才可以变更

			while (true) {
				if (ReadyChange == true)
					break;
				Thread.sleep(100);
			}

			// 做一些对本地面板的处理
			background.processBar.setVisible(false);
			// 这里需要根据当前的GameType 做不同的处理
			// if .....
			// 对本地文件做的处理
			background.image = Toolkit.getDefaultToolkit()
					.createImage(background.class.getResource("/img/Game2015.png"));
			background.GameMain.setVisible(true);

			// 这里需要根据当前的GameType 做不同的处理
			TeamType = Integer.parseInt(readMsg(ins));

			gamePanel.TeamNameA.setText(readMsg(ins));
			gamePanel.TeamNameB.setText(readMsg(ins));

			// 导入文件
			if (TeamType == Const_Game.TeamAindex)// 如果自己是队伍A
			{
				for (int t = 0; t < 2; t++) {// 两个队伍
					for (int i = 0; i < Const_Game.maxPlayer; i++) {
						PlayerPicture[t][i] = ImgUtil.getImageIcon(GameType, t, i);
					}
				}
			} else {// 如果自己是队伍B

				for (int i = 0; i < Const_Game.maxPlayer; i++) {
					PlayerPicture[0][i] = ImgUtil.getImageIcon(GameType, 1, i);// 0
																				// 载入1,1载入0
				}
				for (int i = 0; i < Const_Game.maxPlayer; i++) {
					PlayerPicture[1][i] = ImgUtil.getImageIcon(GameType, 0, i);// 0
																				// 载入1,1载入0
				}
			}

			// 接受初始球员姓名和序号
			// ***** 15个自己的

			for (int i = 0; i < Const_Game.maxPlayer; i++) {
				if (i < 5) {
					background.GameMain.setName(i, readMsg(ins), 0);
					PlayerIndexMe[i] = Integer.parseInt(readMsg(ins));// 接受球员序号
					background.GameMain.setImageByImageIcon(i, PlayerPicture[0][PlayerIndexMe[i]],
							Const_Game.TeamAindex);
				} else {
					gamePanel.ControlBar.PlayerDetail.rotation[i - 5].setName(readMsg(ins));
					PlayerIndexMe[i] = Integer.parseInt(readMsg(ins));// 接受球员序号
					gamePanel.ControlBar.PlayerDetail.rotation[i - 5]
							.setImageByImageIcon(PlayerPicture[0][PlayerIndexMe[i]]);
				}

			}
			// ******
			// 5个对方的
			for (int i = 0; i < 5; i++) {
				background.GameMain.setName(i, readMsg(ins), 1);
				PlayerIndexThem[i] = Integer.parseInt(readMsg(ins));
				background.GameMain.setImageByImageIcon(i, PlayerPicture[1][PlayerIndexThem[i]], Const_Game.TeamBindex);// 设置对方i号位置上的球员照片
			}
			SendStre(ous);
			while (true) {
				// 接受消息循环
				String receiver = readMsg(ins);
				switch (receiver) {
				case "5555":
					background.GameMain.Center_boardcast.append(readMsg(ins) + "\n");// Game Over
					background.GameMain.Center_boardcast.append(readMsg(ins) + "\n");// Final result
					return;
				case "4444":
					System.out.println("异常退出");
					break;
				case "8888":// 正常语句段
					background.GameMain.Center_boardcast.append(readMsg(ins) + "\n");// 显示语句
					if (TeamType == Const_Game.TeamAindex) {
						// 如果这个面板是玩家1
						gamePanel.ScoreA.setText(readMsg(ins));
						gamePanel.ScoreB.setText(readMsg(ins));
					} else {
						gamePanel.ScoreB.setText(readMsg(ins));
						gamePanel.ScoreA.setText(readMsg(ins));
					}
					SetTimeRemaining(Integer.parseInt(readMsg(ins)));
					break;
				case "6666":
					// 暂停
					background.GameMain.Center_boardcast.append(readMsg(ins) + "\n");// 显示语句
																						// 申请暂停
					
					Thread.sleep(10000);
					background.GameMain.Center_boardcast.append("Time out over\n");

					// 发送进攻防守战术
					SendStre(ous);
					if (RotatePlayerThis == true) {
						sendMsg(ous, "7777");// 有换人暂停
						for (int i = 0; i < 5; i++)// 具体换人情况
							sendMsg(ous, String.valueOf(PlayerIndexMe[i]));
						RotatePlayerThis = false;
					} else
						sendMsg(ous, "8888");// 无换人暂停

					while (readMsg(ins).equals( "9999")==false) {
						AnswerToChangePlayer(ins);
					}
					background.GameMain.Center_boardcast.append("The game is on\n");
					break;
				case "7777":
					// 有人叫了换人
					AnswerToChangePlayer(ins);
					break;
				}
				// 给服务器发送信息告诉他自己要干嘛

				if (TimeoutThis == true) { // 暂停
					if(TimeoutNumber>0)
					{
					TimeoutThis = false;
					sendMsg(ous, "6666");
					TimeoutNumber--;
					gamePanel.ControlBar.BarChoose.TimeOutShort.setText("暂停"+"("+String.valueOf(TimeoutNumber)+")");//给面板设置
					if(TimeoutNumber<=0)
						gamePanel.ControlBar.BarChoose.TimeOutShort.setEnabled(false);
					}
					else{
						System.out.println("暂停次数用完");
					}
				} else if (RotatePlayerThis == true) {
					// 换人
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

		// 发送消息用监听器

		BackToInitialization();
	}

	public String readMsg(InputStream ins) throws Exception {
		int value = ins.read();
		String str = "";
		while (value != 10) {
			// 代表客户端不正常关闭
			if (value == -1) {
				throw new Exception();
			}
			str = str + (char) value;
			value = ins.read();
		}
		str = str.trim();
		return str;
	}

	// 发送消息的函数
	public void sendMsg(OutputStream ous, String str) throws Exception {
		byte[] bytes = str.getBytes();
		ous.write(bytes);
		ous.write(10);
		ous.flush();
	}

	private void BackToInitialization() {
		ExceptionEnd = false;
		ReadyChange = false;// 重新置位
		gamePanel.ScoreB.setText("0");
		gamePanel.ScoreA.setText("0");
		SetTimeRemaining(Const_Game.MaxPlayTime);

	}

	/**
	 * 把战术信息发给服务器,包括20个值和2个战术选项
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
			sendMsg(ous, String.valueOf(controlPanelOffense.Offense.getChoose()));// 发送进攻策略
			sendMsg(ous, String.valueOf(controlPanelDefense.defense.getChoose()));// 发送防守策略

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	private void AnswerToChangePlayer(InputStream ins) {
		// 有人叫了换人
		try {
			background.GameMain.Center_boardcast.append(readMsg(ins) + "\n");// 申请换人

			int teamCallingChange = Integer.parseInt(readMsg(ins));// 获取换人号
			if (teamCallingChange == TeamType)// 自己叫的换人
			{
				for (int i = 0; i < 10; i++)
					readMsg(ins);// 读10个不作为(name+index)
			} else {// 对方换人
				for (int i = 0; i < 5; i++) {
					background.GameMain.setName(i, readMsg(ins), 1);
					PlayerIndexThem[i] = Integer.parseInt(readMsg(ins));
					background.GameMain.setImageByImageIcon(i, PlayerPicture[1][PlayerIndexMe[i]],
							Const_Game.TeamBindex);// 设置对方i号位置上的球员照片
				}
			}

			background.GameMain.Center_boardcast.append(readMsg(ins) + "\n");// 换人结束
		} catch (Exception e) {
			System.out.println("AnswerToChangePlayer 调用错误");
			e.printStackTrace();

		}
	}
	
	private void SetTimeRemaining(int time)
	{
		//计算第几节
		//gamePanel.TimeRemainning.setText(readMsg(ins));
		if(time>Const_Game.MaxPlayTime)
		{
			//加时赛
			int extra=time-Const_Game.MaxPlayTime;
			int T1=extra/Const_Game.ExtraTime;//加时赛几
			int T2=extra%Const_Game.ExtraTime;//加时赛几
			int T_m=T2/60;
			int T_s=T2%60;
			String second=(T_s>=10)?String.valueOf(T_s):"0"+String.valueOf(T_s);
			gamePanel.TimeRemainning.setText("加时赛"+String.valueOf(T1+1)+" "+"0"+String.valueOf(T_m)+":"+second);
			return;
		}
		if(time==Const_Game.MaxPlayTime)
		{
			gamePanel.TimeRemainning.setText("第一节 12:00");
			return;
		}
		int past_time=Const_Game.MaxPlayTime-time;//已经过去的时间
		
			int a=past_time/720;
			int b=time%720;
			int minutes=b/60;
			int seconds=b%60;
			String Toset=null;
			switch(a){
			case 0://第一节
				Toset="第一节 ";
				break;
			case 1://第二节
				Toset="第二节 ";
				break;
			case 2://第三节
				Toset="第三节 ";
				break;
			case 3://第四节
				Toset="第四节 ";
				break;
			}
			if(minutes>=10)
				Toset+=String.valueOf(minutes);
			else
				Toset+="0"+String.valueOf(minutes);//分钟数
			Toset+=":";
			if(seconds>=10)
				Toset+=String.valueOf(seconds);
			else
				Toset+="0"+String.valueOf(seconds);//秒钟数
			gamePanel.TimeRemainning.setText(Toset);
		
	}
	
}