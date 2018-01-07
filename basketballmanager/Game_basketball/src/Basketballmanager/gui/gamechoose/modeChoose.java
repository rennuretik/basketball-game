package Basketballmanager.gui.gamechoose;

import java.awt.GridLayout;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;

import Basketballmanager.Listener.mouseListenerFormode;
import Basketballmanager.constant.Const_UI;

/**
 * 选择游戏模式
 * @author Fang Hongbo
 *
 */
public class modeChoose extends JPanel {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	public static JButton chooseDynasty=new JButton("Dynasty mode");
	public static JButton chooseCrazy=new JButton("Crazy mode");
	private mouseListenerFormode Listener=new mouseListenerFormode();
	modeChoose()
	{
		this.setSize(Const_UI.MODE_WIDTH,Const_UI.MODE_HEIGHT);
		this.setLocation(Const_UI.MODE_X, Const_UI.MODE_Y);
		this.setLayout(new GridLayout(11,1));
		chooseDynasty.addMouseListener(Listener);
		chooseCrazy.addMouseListener(Listener);
		this.add(chooseDynasty);
		this.add(chooseCrazy);
		this.setOpaque(false);
		this.setVisible(true);
	}
	
}
