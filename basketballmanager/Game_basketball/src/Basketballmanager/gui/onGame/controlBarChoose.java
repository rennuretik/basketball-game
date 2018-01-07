package Basketballmanager.gui.onGame;

import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JPanel;

import Basketballmanager.client.Myclient;
import Basketballmanager.constant.Const_UI;

public class controlBarChoose extends JPanel implements ActionListener{
	public JButton TimeOutShort=new JButton("��ͣ(5)");
	public JButton Offense=new JButton("��������");
	public JButton Defense=new JButton("���ز���");
	public JButton Player=new JButton("��Ա��Ϣ");
	controlBarChoose()
	{
		this.setLocation(0, 0);
		this.setSize(Const_UI.CONTROLSTATE_WIDTH, Const_UI.CONTROLSTATE_HEIGHT);
		this.setLayout(new GridLayout(4,1));
		TimeOutShort.addActionListener(this);
		Offense.addActionListener(this);
		Defense.addActionListener(this);
		Player.addActionListener(this);

		this.add(TimeOutShort);
		this.add(Offense);
		this.add(Defense);
		this.add(Player);
		
	}
	@Override
	public void actionPerformed(ActionEvent e) {
		// TODO Auto-generated method stub
		if(e.getSource()==TimeOutShort)
		{
			//������ͣ
			Myclient.TimeoutThis=true;
			
			
		}
		else if(e.getSource()==Offense)
		{
			gamePanel.ControlBar.OffenseDetail.setVisible(true);
			gamePanel.ControlBar.PlayerDetail.setVisible(false);
			gamePanel.ControlBar.DefenseDetail.setVisible(false);

		}
		else if(e.getSource()==Defense)
		{
			gamePanel.ControlBar.OffenseDetail.setVisible(false);
			gamePanel.ControlBar.PlayerDetail.setVisible(false);
			gamePanel.ControlBar.DefenseDetail.setVisible(true);

		}
		else if(e.getSource()==Player)
		{
			gamePanel.ControlBar.OffenseDetail.setVisible(false);
			gamePanel.ControlBar.PlayerDetail.setVisible(true);
			gamePanel.ControlBar.DefenseDetail.setVisible(false);

		}
	}
}
