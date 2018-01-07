package Basketballmanager.gui.onGame;

import java.awt.Color;

import javax.swing.JPanel;

import Basketballmanager.constant.Const_UI;

public class controlPanelDefense extends JPanel{

	public static ControlDefenseChoose chooseBar=new ControlDefenseChoose();
	public static OrganizePanel organize=new OrganizePanel();

	public static defenseSt defense=new defenseSt();

	
	
	controlPanelDefense()
	{
		this.setSize(Const_UI.CONTROLDETAIL_WIDTH, Const_UI.CONTROLDETAIL_HEIGHT);
		this.setLayout(null);
		
		chooseBar.setLocation(0,0);
		organize.setLocation(Const_UI.Meddling_X, Const_UI.Meddling_Y);

		defense.setLocation(Const_UI.Meddling_X, Const_UI.Meddling_Y);
		

		this.setBackground(Color.BLUE);
		this.add(chooseBar);
		this.add(organize);

		this.add(defense);

		this.setVisible(false);
	}
}