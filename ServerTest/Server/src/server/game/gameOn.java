package server.game;

import java.util.Random;

import server.Myserver;
import server.ServerThread;
import server.constant.constant;

public class gameOn extends Thread{
    
	public  final int  gameType;
	public volatile player[][] Team= new player[2][constant.maxPlayer];//0 for team A, 1 for teamB
	public volatile int[][][] Coach=new int[2][6][];//战术设置
	ServerThread serverForthis=null;
	private int BallRight=0;
	public int [][][] statistics=new int[2][constant.maxPlayer][constant.statisticNumber];
	private String[] TeamName=new String[2];
	
	private int OverTimeCounter=0;
	
	public int remainingTime=constant.MaxPlayTime;
	public int[] Score=new int[2];//team A,teamB
	
	public gameOn(int type,ServerThread This)
	{
		gameType=type;
		for(int i=0;i<2;i++)
		{
			TeamName[i]=Myserver.TeamName[gameType][i];
			for(int j=0;j<constant.maxPlayer;j++)
			{
				Team[i][j]=new player(Myserver.Plot[gameType][i][j]);//copy constructor
			}
		}
		
		for(int i=0;i<2;i++)
			for(int j=0;j<5;j++)
			{
				Team[i][j].SetCourt(true);
				Team[i][j].SetPosition(j);
			}
		//initialize Coach
		
		
		for(int i=0;i<2;i++)
		{//two teams
			Coach[i][constant.organizeArrange]=new int[5];
			Coach[i][constant.scoreArrange]=new int[5];
			Coach[i][constant.scoreCreation]=new int[5];
			Coach[i][constant.defenseKey]=new int[5];
			Coach[i][constant.offenseStrategy]=new int[1];
			Coach[i][constant.defenseStrategy]=new int[1];
		}
		serverForthis=This;
		
		
		//随机改变球员的状态
		Random change=new Random();
		for(int i=0;i<2;i++)
		{
			for(int j=0;j<constant.maxPlayer;j++)
			{//player index
				for(int t=0;t<constant.attributeNumber;t++)
				{
					if(t==constant.indexPlayer || t==constant.strength ||t==constant.statusInTeam)//如果是这几种，跳过
						continue;
					int state=(change.nextInt(2)==1)?-1:1;
					int value=change.nextInt(9);
					Team[i][j].ChangeAttribute(t, value*state);
				}

			}
		}
	}
	public void run() {  

		
		
    	while(true)
    	{
    		String backwords=Caculate();//计算
    		while(true)
    		{
    			if(serverForthis.Sending==false)
    				break;
    			//break
    			try {
    				Thread.sleep(100);
    			} catch (InterruptedException e) {
    				// TODO Auto-generated catch block
    				e.printStackTrace();
    			}
    		}
    		serverForthis.SendingWords=backwords;
    		serverForthis.TimeRemainning=remainingTime;
    		serverForthis.Score[0]=Score[0];
    		serverForthis.Score[1]=Score[1];
    		serverForthis.Sending=true;
    		System.out.println(serverForthis.Sending);
    		
    	}
    	
    }

	public synchronized void ChangeStrategy(int team,int decision[][])
	{
		
		for(int i=0;i<4;i++)
		{
			// 4 five lines
			for(int j=0;j<5;j++)
			{
				Coach[team][i][j]=decision[i][j];
			}

		}
		for(int i=0;i<2;i++)
		{
			// 4 five lines
				Coach[team][i+4][0]=decision[i+4][0];
		}
	}
	
	
	
	/**
	 * 执行教练的换人指令
	 * @param team
	 * @param index
	 * @param changeTo
	 */
	public synchronized void ChangePlayer(int team,int index[])
	{
//		//首发五人位置
		
		for(int i=0;i<constant.maxPlayer;i++)
		{//set all to not on court
			Team[team][i].SetCourt(false);
		}
		for(int i=0;i<5;i++)
		{
			Team[team][index[i]].SetCourt(true);
			Team[team][index[i]].SetPosition(i);
		}
		
	}
	public synchronized String Caculate()
	{
		String OutputLanguage=null;
		//calculate this and updata the state
		//改变球权
		int currentTime=0;
		
		int Position[][][]=new int[2][constant.maxPlayer][constant.PositionType];//维护各个人的位置
		//automatically set to zero
		int ballManIndex=-1;//当前持球人
		
		int[] chooseStart={0,0,0,0,0};
		
		int[][] TeamOncourttt=new int[2][5];
		TeamOncourttt[0]=ReturnOncourt(0);//A队伍的场上五人，根据对位排的0-4
		TeamOncourttt[1]=ReturnOncourt(1);//A队伍的场上五人

		
		for(int i=0;i<5;i++)
		{
			//要球倾向+球队地位+运球倾向
			chooseStart[i]=Team[BallRight][TeamOncourttt[BallRight][i]].attribute[constant.desireForBall]
					+Team[BallRight][TeamOncourttt[BallRight][i]].attribute[constant.statusInTeam]
							+Team[BallRight][TeamOncourttt[BallRight][i]].attribute[constant.tendencyToDribble];
		}
		ballManIndex=maxInFive(chooseStart);
		
		
		Random any=new Random();
		
		
		while(true)
		{//每次进入必然都是从发球开始
			//开始进入判断状态
			//得分倾向+篮下位置+篮下能力
			//得分倾向+2分位置+2分能力
			//得分倾向+3分位置+3分能力
			//传球倾向+剩余时间<<4
			//运球倾向+剩余时间<<4
			int[] next={Team[BallRight][TeamOncourttt[BallRight][ballManIndex]].attribute[constant.tendencyToScore]
					+Position[BallRight][TeamOncourttt[BallRight][ballManIndex]][constant.RimPositionIndex]
					+Team[BallRight][TeamOncourttt[BallRight][ballManIndex]].attribute[constant.rimFinish],
					
					Team[BallRight][TeamOncourttt[BallRight][ballManIndex]].attribute[constant.tendencyToScore]
					+Position[BallRight][TeamOncourttt[BallRight][ballManIndex]][constant.TwoPositionIndex]
					+Team[BallRight][TeamOncourttt[BallRight][ballManIndex]].attribute[constant.twoPointerFinish],
					
					Team[BallRight][TeamOncourttt[BallRight][ballManIndex]].attribute[constant.tendencyToScore]
					+Position[BallRight][TeamOncourttt[BallRight][ballManIndex]][constant.ThreePositionIndex]
					+Team[BallRight][TeamOncourttt[BallRight][ballManIndex]].attribute[constant.threePointerFinish],
					
				Team[BallRight][TeamOncourttt[BallRight][ballManIndex]].attribute[constant.tendencyToPass]
						+(constant.maxTimeForOnePossession-currentTime)<<4,
				
				Team[BallRight][TeamOncourttt[BallRight][ballManIndex]].attribute[constant.tendencyToDribble]
						+(constant.maxTimeForOnePossession-currentTime)<<4
			};
			int nextDescision=maxInFive(next);

			if(nextDescision==0)
			{
				//内线强攻
				if(Team[BallRight][TeamOncourttt[BallRight][ballManIndex]].attribute[constant.rimFinish]
						+Position[BallRight][TeamOncourttt[BallRight][ballManIndex]][constant.RimPositionIndex]>any.nextInt(200))
				{
					//强攻得手
					Score[BallRight]+=2;
					OutputLanguage=HeadString()+" "+TeamName[BallRight]+" "+
							Team[BallRight][TeamOncourttt[BallRight][ballManIndex]].getNamePlayer()+" attacks the rim and score";
					remainingTime-=currentTime;
					BallRight=(BallRight==0)?1:0;

					break;
				}
				else{
					//强攻失败

					int other=(BallRight==0)?1:0;
					remainingTime-=currentTime;
					OutputLanguage=HeadString()+" "+TeamName[BallRight]+" "
							+Team[BallRight][TeamOncourttt[BallRight][ballManIndex]].getNamePlayer()+" fails to score around the rim\t"
							+TeamName[other]+" Team rebound";
					BallRight=other;
					break;
				}
			}
			
			else if(nextDescision==1)
			{
				//两分投射
				if(Team[BallRight][TeamOncourttt[BallRight][ballManIndex]].attribute[constant.twoPointerFinish]
						+Position[BallRight][TeamOncourttt[BallRight][ballManIndex]][constant.TwoPositionIndex]>any.nextInt(200))
				{
					//跳投得手
					Score[BallRight]+=2;
					OutputLanguage=HeadString()+" "+TeamName[BallRight]+" "+
							Team[BallRight][TeamOncourttt[BallRight][ballManIndex]].getNamePlayer()+" scores two points with a jumper";
					remainingTime-=currentTime;
					BallRight=(BallRight==0)?1:0;

					break;
				}
				else{
					//跳投失败

					int other=(BallRight==0)?1:0;
					remainingTime-=currentTime;
					OutputLanguage=HeadString()+" "+TeamName[BallRight]+" "
							+Team[BallRight][TeamOncourttt[BallRight][ballManIndex]].getNamePlayer()+" shoots the ball and misses\t"
							+TeamName[other]+" Team rebound";
					BallRight=other;
					break;
				}
				
				
			}
			else if(nextDescision==2)//三分投射
			{
				if(Team[BallRight][TeamOncourttt[BallRight][ballManIndex]].attribute[constant.threePointerFinish]
						+Position[BallRight][TeamOncourttt[BallRight][ballManIndex]][constant.ThreePositionIndex]>any.nextInt(200))
				{
					//三分得手
					Score[BallRight]+=3;
					OutputLanguage=HeadString()+" "+TeamName[BallRight]+" "+
							Team[BallRight][TeamOncourttt[BallRight][ballManIndex]].getNamePlayer()+" hits a three pointer";
					remainingTime-=currentTime;
					BallRight=(BallRight==0)?1:0;

					break;
				}
				else{
					//三分失败

					int other=(BallRight==0)?1:0;
					remainingTime-=currentTime;
					OutputLanguage=HeadString()+" "+TeamName[BallRight]+" "
							+Team[BallRight][TeamOncourttt[BallRight][ballManIndex]].getNamePlayer()+" misses a three pointer\t"
							+TeamName[other]+" Team rebound";
					BallRight=other;
					break;
				}
				
			}
			
			else if(nextDescision==3)
			{//传球
				int temp;
				while(true)
				{
					temp=maxInFive(chooseStart);
					if(temp!=ballManIndex)//没有传给自己
					{
						ballManIndex=temp;
						break;
					}
				}
			}
//			if(true)//投篮不中+篮板
//			{
//				
//			}
//			
//			
//			if(true)//投篮命中
//			{
//				
//			}
//			
//			
//			if(true)//犯规
//			{
//				
//			}
//			
//			if(true)//犯规+投篮命中
//			{
//				
//			}
			
			
			currentTime++;
			
			//比赛时间到
			if(currentTime>=remainingTime)
			{
				if(Score[0]>Score[1])
				{
				OutputLanguage=HeadString()+" "+"Game Over\t"+TeamName[0]+" wins";
				serverForthis.End=true;
				break;
				}
				else if(Score[0]<Score[1])
				{
					OutputLanguage=HeadString()+" "+"Game Over\t"+TeamName[1]+" wins";
					serverForthis.End=true;
					break;

				}
				else{
					if(OverTimeCounter==0)
						OutputLanguage=HeadString()+" "+"Regular time passed, step into Over Time";
					
					else
						OutputLanguage=HeadString()+" "+"Next overtime";
					
					OverTimeCounter++;
					//overtime
					remainingTime=constant.MaxPlayTime+OverTimeCounter*constant.ExtraTime;
					
				}
			}
			
			if(currentTime>=constant.maxTimeForOnePossession)
			{
				
				OutputLanguage=HeadString()+" "+TeamName[BallRight]+" 24 Seconds Violation!";
				remainingTime-=currentTime;
				BallRight=(BallRight==0)?1:0;
				break;
			}
		}
		
		
		
		//发球球给谁
		
		
		
		
		
		
		
		
		return OutputLanguage;
	}
	private synchronized String HeadString()
	{//加时赛没做
		System.out.println("加时赛没做");
		int a=remainingTime/720;
		switch(a)
		{
		case 0:
			return "Fourth Quarter: ";
		case 1:
			return "Third Quarter: ";
		case 2:
			return "Second Quarter: ";
		case 3:
			return "First Quarter: ";
		}
		a=(remainingTime-constant.MaxPlayTime)/constant.ExtraTime;//第几个加时
		return "Over Time "+String.valueOf(a+1)+" : ";
	}
	
	private synchronized int maxInFive(int[] attributeNumber)
	{
		int MaxIndex=0;
		int sum=0;
		for(int i=0;i<attributeNumber.length;i++)
			sum+=attributeNumber[i];

		int currentPointer=0;//0-sum-1
		int nextPointer=0;
		Random PointerTobeChoose=new Random();
		int toBechoosen=PointerTobeChoose.nextInt(sum);

		for(int i=0;i<attributeNumber.length;i++)
		{
			nextPointer+=attributeNumber[i];
			if(toBechoosen>=currentPointer&& toBechoosen<nextPointer)
			{
				return i;
			}
			currentPointer+=attributeNumber[i];
		}
		System.out.println("Error maxFive");
		return 4;
	}
	/**
	 * 返回当前场上五人的index
	 * @param teamIndex
	 * @return
	 */
	private synchronized int[] ReturnOncourt(int teamIndex){
		int[] result=new int[5];
		for(int i=0;i<constant.maxPlayer;i++)
		{
			if(Team[teamIndex][i].getCourt()==true)
				result[Team[teamIndex][i].getPosition()]=i;//index of player
		}
		return result;
	}
}
