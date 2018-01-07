package Basketballmanager.gui.onGame;

import java.awt.BorderLayout;
import java.awt.Color;

import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

import Basketballmanager.constant.Const_UI;

public class chattingPanel extends JPanel{
	
	JTextArea Input = new JTextArea();
	JTextArea Output= new JTextArea();
    JScrollPane scroll;

	chattingPanel()
	{
		this.setSize(Const_UI.CHAT_WIDTH,Const_UI.CHAT_HEIGHT);
		this.setBackground(Color.BLUE);
		this.setLayout(null);
		Input.setLineWrap(true);
		Input.setLocation(Const_UI.CHATINPUT_X, Const_UI.CHATINPUT_Y);
		Input.setSize(Const_UI.CHATINPUT_WIDTH,Const_UI.CHATINPUT_HEIGHT);
		Output.setLineWrap(true);
		Output.setEditable(false);
		Output.setSize(Const_UI.CHATOUTPUT_WIDTH,Const_UI.CHATOUTPUT_HEIGHT);
		scroll=new JScrollPane(Output);
		scroll.setSize(Const_UI.CHATOUTPUT_WIDTH,Const_UI.CHATOUTPUT_HEIGHT);
		scroll.setLocation(0, 0);
		Output.setBackground(Color.GREEN);
        scroll.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED); 

		
		this.add(scroll);
		this.add(Input);
	}
}
