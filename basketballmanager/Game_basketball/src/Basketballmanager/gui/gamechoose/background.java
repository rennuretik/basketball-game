package Basketballmanager.gui.gamechoose;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.Toolkit;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.SwingConstants;

import Basketballmanager.constant.Const_UI;
import Basketballmanager.gui.onGame.gamePanel;
public class background extends JPanel{
	private static final long serialVersionUID = -8505197231593797314L;
    public static Image image;  
    public static modeChoose ChooseMode=new modeChoose();
    public static modeChooseLayer2 ChooseMode2= new modeChooseLayer2();
    public static modeChooseLayer3 ChooseMode3=new modeChooseLayer3();
    public static gamePanel GameMain=new gamePanel();
    public static JProgressBar processBar = new JProgressBar();// 创建进度条  
    
    
    public static JLabel output= new JLabel();
	public background()
	{
		//最外层边框
		this.setSize(Const_UI.FRAME_WIDTH, Const_UI.FRAME_HEIGHT);
		this.setLocation(0, 0);
		this.setLayout(null);

		image = Toolkit.getDefaultToolkit().createImage(  
        		background.class  
                        .getResource("/img/NBA2.gif"));  
		

		//背景图案
        /** 
         *  初始化选项按钮输出面板
         */
        output.setLocation(Const_UI.TEXTLABEL_X,Const_UI.TEXTLABEL_Y);
        output.setSize(Const_UI.TEXTLABEL_WIDTH,Const_UI.TEXTLABEL_HEIGHT);
        output.setVerticalAlignment(SwingConstants.TOP);
        output.setVisible(false);
        
        /**
         *  初始化进度条
         */
        processBar.setString("Loading");
        processBar.setStringPainted(true);
        processBar.setBackground(Color.GREEN);  
        processBar.setSize(Const_UI.PROGRESS_WIDTH,Const_UI.PROGRESS_HEIGHT);
        processBar.setLocation(Const_UI.PROGRESS_X, Const_UI.PROGRESS_Y);
        processBar.setVisible(false);
        
        //主游戏面板
        GameMain.setLocation(0,0);
        GameMain.setVisible(false);
        
        this.add(output);
        this.add(processBar);
        //添加选择面板
        this.add(ChooseMode);
        this.add(ChooseMode2);
        this.add(ChooseMode3);
        this.add(GameMain);
		this.setVisible(true);
		
	}

	/**
	 * 绘制
	 */
	public void paintComponent(Graphics g) {
        super.paintComponent(g);  
        g.drawImage(image, 0, 0, this);  
	}
	
	
	
}
