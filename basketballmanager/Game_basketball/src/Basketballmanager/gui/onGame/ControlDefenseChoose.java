package Basketballmanager.gui.onGame;

import java.awt.Color;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JPanel;

import Basketballmanager.constant.Const_UI;

public class ControlDefenseChoose extends JPanel implements ActionListener{
	JButton defensehandling=new JButton("防守重心");
	JButton strategy=new JButton("防守战术分配");

	
	ControlDefenseChoose()
	{
		this.setLayout(new GridLayout(1,2));
		this.setSize(Const_UI.ControlPanelChoose_WIDTH, Const_UI.ControlPanelChoose_HEIGHT);
		defensehandling.setFocusPainted(false);
		strategy.setFocusPainted(false);
		defensehandling.addActionListener(this);
		strategy.addActionListener(this);
		defensehandling.setBackground(Color.RED);
		strategy.setBackground(Color.GRAY);

		this.add(defensehandling);

		this.add(strategy);
	}


	@Override
	public void actionPerformed(ActionEvent e) {
		// TODO Auto-generated method stub
		if(e.getSource()==defensehandling)
		{
			defensehandling.setBackground(Color.RED);

			strategy.setBackground(Color.GRAY);
			controlPanelDefense.organize.setVisible(true);

			controlPanelDefense.defense.setVisible(false);

		}
		else if(e.getSource()==strategy)
		{
			defensehandling.setBackground(Color.GRAY);
			strategy.setBackground(Color.RED);
			controlPanelDefense.organize.setVisible(false);

			controlPanelDefense.defense.setVisible(true);

		}
	}
}
