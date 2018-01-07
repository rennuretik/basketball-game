package Basketballmanager.gui.main;

import java.util.List;

import javax.swing.JFrame;

import Basketballmanager.main.start.Main;
import Basketballmanager.constant.Const_UI;
import Basketballmanager.deprecated.Information;
import Basketballmanager.gui.gamechoose.background;

/**
 * 游戏主窗体
 * 
 * @author user
 * 
 */
public class Frame_Main extends JFrame {
	
	private static final long serialVersionUID = -6905419069196737546L;
	public static Frame_Main me;
	private background Background;
	public Frame_Main() {
		me = this;
		//Information.loadAll();
		GameOn();
	}
	
	private void GameOn()
	{
		CreateUI();
	}
	
	void CreateUI()
	{
		System.out.println("开始创建窗体");
		this.setTitle("BaskballManager"+"--"+Main.version);
		this.setSize(Const_UI.FRAME_WIDTH, Const_UI.FRAME_HEIGHT);
		this.setDefaultCloseOperation(3);
		this.setResizable(false);
		this.setLocationRelativeTo(null);
		this.setLayout(null);
		Background=new background();
		this.setContentPane(Background);//add background panel
		System.out.println("准备显示窗体");
		Main.isFinished = true;
		
		this.setVisible(true);
		System.out.println("完成");
	}
}
