package Basketballmanager.main.start;

import Basketballmanager.gui.main.Frame_Main;
import Basketballmanager.gui.start.Frame_Load;

/**
 * �������
 * ���� ���겨
 * ���ʱ�� 
 */

public class Main {
	public static String version = "alpha-17-12-27";
	//�Ƿ���ɼ���
	public static boolean isFinished;
	public static void main(String[] args) {
		new Thread(new Runnable() {
			@Override
			public void run() {				
				//��������
				new Frame_Load();
			}
		}).start();
		//������
		new Frame_Main();
	}
}