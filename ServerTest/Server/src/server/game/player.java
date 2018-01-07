package server.game;

import server.constant.constant;

public class player {
	//attribute measure the condition of player
	public final String name;
	public int[] attribute=new int[constant.attributeNumber];//including index
	public int[] state=new int [constant.stateNumber]; //state number
	
	//dynamic information
	public volatile boolean onCourt=false;//false for on court, true for on court
	public volatile int currentPosition=0; //no position
	player(String Name, int a[])
	{
		//initial player information
		name=Name;
		for(int i=0;i<constant.attributeNumber;i++)
			attribute[i]=a[i];
		state[constant.energy]=attribute[constant.maxEnergy];
	}

	player(player source)//copy constructor
	{
		//initial player information
		name=source.name;
		for(int i=0;i<constant.attributeNumber;i++)
			attribute[i]=source.attribute[i];
		state[constant.energy]=attribute[constant.maxEnergy];
	}
	public void SetPosition(int position)
	{
		if(position<=0||position>5)
			return;
		currentPosition=position;
	}
	
	public String getNamePlayer()
	{
		return name;
	}
	
	public int getIndexPlayer()
	{
		return attribute[constant.indexPlayer];
	}
	
	public void SetCourt(boolean CourtCondition)
	{
		onCourt=CourtCondition;
	}
	
	public boolean getCourt()
	{
		return onCourt;
	}
	public int getPosition()
	{
		return currentPosition;
	}
	public void ChangeAttribute(int attributeIndex,int number)
	{
		attribute[attributeIndex]=(attribute[attributeIndex]+number>=0)?attribute[attributeIndex]+number:0;
	}
	
}
