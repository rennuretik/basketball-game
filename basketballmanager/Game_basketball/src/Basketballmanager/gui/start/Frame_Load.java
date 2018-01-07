package Basketballmanager.gui.start;

import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;

import javax.swing.JFrame;

import Basketballmanager.main.start.Main;
import Basketballmanager.util.ImgUtil;

/**
 * ���봰��
 * @author user
 *
 */
public class Frame_Load extends JFrame{
	/**
	 * 
	 */
	private static final long serialVersionUID = -5475941235030060504L;
	BufferedImage img ;
	public Frame_Load(){
		img = ImgUtil.getJpgImgByName("logo");
		setUI();
		Runnable run = new Runnable() {
			
			@Override
			public void run() {
				showIt();
			}
		};
		new Thread(run).start();
	}
	/**
	 * ����UI
	 */
	public void setUI(){
		setBounds(new Rectangle(550, 400));
		setLocationRelativeTo(null);
		setResizable(false);
		setUndecorated(true);
		setVisible(true);
	}
	/**
	 * ���Ʊ���
	 */
	public void paint(Graphics g){
		//super.paint(g);
		if(img!=null){
			g.drawImage(img, 0, 0, null);
		}
	}
	/**
	 * δ���������һֱ��ʾ
	 */
	public void showIt(){
		while(!Main.isFinished){}
		setVisible(false);
		dispose();
	}
	
}