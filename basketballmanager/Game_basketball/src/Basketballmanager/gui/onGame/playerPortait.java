package Basketballmanager.gui.onGame;

import java.awt.Color;
import java.awt.Font;
import java.awt.Image;

import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;

import Basketballmanager.constant.Const_UI;

public class playerPortait extends JPanel{
	private JLabel Name=new JLabel();
	private JLabel Picture=new JLabel();

	playerPortait()
	{
		this.setSize(Const_UI.PLAYER_WIDTH,Const_UI.PLAYER_HEIGHT);
		this.setLayout(null);
		Name.setSize(Const_UI.PLAYER_WIDTH,Const_UI.PLAYER_HEIGHT-Const_UI.PORTAIT_HEIGHT);
		Name.setFont(new Font("����", Font.PLAIN, 14));
		Name.setHorizontalAlignment(JLabel.CENTER);
		Name.setLocation(0,Const_UI.PORTAIT_HEIGHT);
		Picture.setSize(Const_UI.PORTAIT_WIDTH,Const_UI.PORTAIT_HEIGHT);
		Picture.setLocation(0, 0);
		Picture.setBackground(Color.BLACK);
		
		Name.setOpaque(true);
		Picture.setOpaque(true);
		
		this.add(Picture);
		this.add(Name);
	}
	
	public void setName(String name)
	{
		Name.setText(name);
	}
	
	public void setImageByIcon(Icon picture)
	{
		Picture.setIcon(picture);
	}
	
	public void setImageByImageIcon(ImageIcon picture)
	{
		picture.setImage(picture.getImage().  
                getScaledInstance(picture.getIconWidth(),picture.getIconHeight(), Image.SCALE_DEFAULT));
		Picture.setIcon(picture);
	}
}
