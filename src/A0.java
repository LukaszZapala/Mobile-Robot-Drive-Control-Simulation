import jade.core.AID;
import jade.core.Agent;
import jade.core.behaviours.Behaviour;
import jade.core.behaviours.CyclicBehaviour;
import jade.core.behaviours.OneShotBehaviour;
import jade.core.behaviours.TickerBehaviour;
import jade.lang.acl.ACLMessage;

public class A0 extends Agent 
{
	private static Boolean readyToWork = false;
	
	private static Boolean readyToMove = false;
	
	private Boolean standby = false;
	
	private String[] agentTable = { "Agent1", "Agent2", "Agent3", "Agent4" };
	
	private int[] receiveTurnOnTable = {0, 0, 0, 0};
		
	private int[] receiveAgentTable = {0, 0, 0, 0};
	
	public static Boolean[] moveLogicTable = { false, false, false, false };
	
	public static Animation animation = new Animation();
	
	public void setup()
	{
		super.setup();
		
		// TODO Uruchamiamy animacje jako zachowanie
		Behaviour runAnimation = new OneShotBehaviour(this) 
		{
			@Override
			public void action() 
			{
				animation.Run();
			}			
		};
		
		// TODO Wysy³amy po³¹czenie w³¹czenia agentów.
		Behaviour turnOnAllAgents = new OneShotBehaviour()
		{
			@Override
			public void action() 
			{
				ACLMessage msg = new ACLMessage(ACLMessage.INFORM);
				
				for (int it = 0; it < agentTable.length; it++)
				{
					msg.addReceiver(new AID(agentTable[it], AID.ISLOCALNAME));
				}
				
				msg.setContent("ON");
				send(msg);
				
				System.out.println("A0: Wys³ano do " + agentTable[0]);
			}			
		};
		
		// TODO Testujemy po³¹czenie
		Behaviour testConnection = new OneShotBehaviour()
		{
			@Override
			public void action() 
			{
				ACLMessage msg;
				do
				{
					msg = receive();
					
					if (msg != null)
					{
						for (int it = 0; it < agentTable.length; it++)
						{
							if (agentTable[it].equals(msg.getSender().getLocalName()) && "ON".equals(msg.getContent()) 
									&& receiveTurnOnTable[it] == 0)
							{
								receiveTurnOnTable[it] = 1;
								System.out.println("A0: Potwierdzenie od " + msg.getSender().getLocalName() + " polecenia " + msg.getContent());
							}
						}
					}
					
					if (receiveTurnOnTable[0] == 1  && receiveTurnOnTable[1] == 1 && receiveTurnOnTable[2] == 1 
							&& receiveTurnOnTable[3] == 1)
					{
						readyToWork = true;
					}
					
				}while(readyToWork == false);
				
				System.out.println("A0: Wszyscy agenici siê zg³osili!");
			}
			
		};
		
		// TODO Wysy³amy informacje o ruchu
		Behaviour sendInfoAboutMove = new CyclicBehaviour(this)
		{
			@Override
			public void action() 
			{
				if (moveLogicTable[0] || moveLogicTable[1] || moveLogicTable[2] || moveLogicTable[3])
				{
					ACLMessage msg = new ACLMessage(ACLMessage.INFORM);	
					
					for (int it = 0; it < agentTable.length; it++)
					{
						System.out.println("A0: Wys³ano do " + agentTable[it]);
						msg.addReceiver(new AID(agentTable[it], AID.ISLOCALNAME));
					}
					
					if (moveLogicTable[0])
					{
						msg.setContent("MOVE UP");
						//moveLogicTable[0] = false;
					}
					if (moveLogicTable[1])
					{
						msg.setContent("MOVE DOWN");
						//moveLogicTable[1] = false;
					}
					if (moveLogicTable[2])
					{
						msg.setContent("TURN RIGHT");
						//moveLogicTable[2] = false;
					}
					if (moveLogicTable[3])
					{
						msg.setContent("TURN RIGHT");
						//moveLogicTable[3] = false;
					}
					
					send(msg);					
					msg = null;
				}
				
			}
			
		};
		
		// TODO Odbieramy potwierdzenia
		Behaviour receivingConfirmation = new CyclicBehaviour(this)
		{
			@Override
			public void action() 
			{
				ACLMessage msg = receive();
				
				if (msg != null)
				{
					standby = true;
					
					for (int it = 0; it < agentTable.length; it++)
					{
						if (agentTable[it].equals(msg.getSender().getLocalName()) && receiveAgentTable[it] == 0)
						{
							if ("MOVE UP".equals(msg.getContent()) || "MOVE DOWN".equals(msg.getContent()) 
								|| "TURN RIGHT".equals(msg.getContent()) || "TURN LEFT".equals(msg.getContent()))
							{
								System.out.println("A0: Potwierdzenie od " + msg.getSender().getLocalName() + " polecenia " + msg.getContent());
								receiveAgentTable[it] = 1;
							}
							else System.out.println("A0: Nic nie robie!");
						}
					}
					
					msg = null;
				}
				else
				{
					if (standby == false) 
					{
						standby = true;
						System.out.println("A0: Czekam na sygna³!");
					}					
				}
				
				if (receiveAgentTable[0] == 1 && receiveAgentTable[1] == 1 && receiveAgentTable[2] == 1 && receiveAgentTable[3] == 1)
				{
					System.out.println("A0: Wszyscy agenci potwierdzili polecenie");
					readyToMove = true;
					
					receiveAgentTable[0] = 0;
					receiveAgentTable[1] = 0;
					receiveAgentTable[2] = 0;
					receiveAgentTable[3] = 0;
				}
			}
			
		};
		
		// TODO Uruchomiamy zachowania.
		addBehaviour(runAnimation);
		addBehaviour(turnOnAllAgents);
		addBehaviour(testConnection);
		addBehaviour(sendInfoAboutMove);
		addBehaviour(receivingConfirmation);
	}

	public void SetValueInMoveLogoicTable(boolean a, boolean b, boolean c, boolean d) 
	{
		moveLogicTable[0] = a; 
		moveLogicTable[1] = b;
		moveLogicTable[2] = c;
		moveLogicTable[3] = d;
	}
	
	public Boolean GetReadyToWork()
	{
		return readyToWork;
	}
	
	public Boolean GetReadyToMove()
	{
		return readyToMove;
	}
}