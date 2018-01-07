package Basketballmanager.gui.onGame;

import java.awt.Color;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JPanel;

import Basketballmanager.constant.Const_UI;

public class ControlOffenseChoose extends JPanel implements ActionListener{
	JButton ballhandling=new JButton("��Ȩ����");
	JButton scorehandling=new JButton("����Ȩ����");
	JButton createOpportunity=new JButton("�����������");
	JButton strategy=new JButton("����ս������");

	
	ControlOffenseChoose()
	{
		this.setLayout(new GridLayout(1,4));
		this.setSize(Const_UI.ControlPanelChoose_WIDTH, Const_UI.ControlPanelChoose_HEIGHT);
		ballhandling.setFocusPainted(false);
		scorehandling.setFocusPainted(false);
		createOpportunity.setFocusPainted(false);
		strategy.setFocusPainted(false);

		ballhandling.addActionListener(this);
		scorehandling.addActionListener(this);
		createOpportunity.addActionListener(this);
		strategy.addActionListener(this);
		ballhandling.setBackground(Color.RED);
		scorehandling.setBackground(Color.GRAY);
		createOpportunity.setBackground(Color.GRAY);
		strategy.setBackground(Color.GRAY);

		this.add(ballhandling);
		this.add(scorehandling);
		this.add(createOpportunity);
		this.add(strategy);
	}


	@Override
	public void actionPerformed(ActionEvent e) {
		// TODO Auto-generated method stub
		if(e.getSource()==ballhandling)
		{
			ballhandling.setBackground(Color.RED);
			scorehandling.setBackground(Color.GRAY);
			createOpportunity.setBackground(Color.GRAY);
			strategy.setBackground(Color.GRAY);
			controlPanelOffense.organize.setVisible(true);
			controlPanelOffense.scoreArr.setVisible(false);
			controlPanelOffense.attack.setVisible(false);
			controlPanelOffense.Offense.setVisible(false);

		}
		else if(e.getSource()==scorehandling)
		{
			ballhandling.setBackground(Color.GRAY);
			scorehandling.setBackground(Color.RED);
			createOpportunity.setBackground(Color.GRAY);
			strategy.setBackground(Color.GRAY);
			controlPanelOffense.organize.setVisible(false);
			controlPanelOffense.scoreArr.setVisible(true);
			controlPanelOffense.attack.setVisible(false);
			controlPanelOffense.Offense.setVisible(false);

		}
		else if(e.getSource()==createOpportunity)
		{
			ballhandling.setBackground(Color.GRAY);
			scorehandling.setBackground(Color.GRAY);
			createOpportunity.setBackground(Color.RED);
			strategy.setBackground(Color.GRAY);
			controlPanelOffense.organize.setVisible(false);
			controlPanelOffense.scoreArr.setVisible(false);
			controlPanelOffense.attack.setVisible(true);
			controlPanelOffense.Offense.setVisible(false);

		}
		else if(e.getSource()==strategy)
		{
			ballhandling.setBackground(Color.GRAY);
			scorehandling.setBackground(Color.GRAY);
			createOpportunity.setBackground(Color.GRAY);
			strategy.setBackground(Color.RED);
			controlPanelOffense.organize.setVisible(false);
			controlPanelOffense.scoreArr.setVisible(false);
			controlPanelOffense.attack.setVisible(false);
			controlPanelOffense.Offense.setVisible(true);

		}
	}
}
