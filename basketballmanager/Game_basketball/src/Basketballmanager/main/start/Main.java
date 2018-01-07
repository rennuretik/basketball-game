package Basketballmanager.main.start;

import Basketballmanager.gui.main.Frame_Main;
import Basketballmanager.gui.start.Frame_Load;

/**
 * 程序入口
 * 作者 方宏波
 * 完成时间 
 */

public class Main {
	public static String version = "alpha-17-12-27";
	//是否完成加载
	public static boolean isFinished;
	public static void main(String[] args) {
		new Thread(new Runnable() {
			@Override
			public void run() {				
				//启动界面
				new Frame_Load();
			}
		}).start();
		//主界面
		new Frame_Main();
	}
}