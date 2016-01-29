import jade.core.AID;
import jade.core.Agent;
import jade.core.behaviours.Behaviour;
import jade.core.behaviours.CyclicBehaviour;
import jade.core.behaviours.OneShotBehaviour;
import jade.lang.acl.ACLMessage;

public class A4 extends Agent 
{
	private Boolean readyToWork = false;

	private int state = 0;
	
	private Boolean standby = false;
	
	public void setup()
	{
		super.setup();
		
		// TODO Uruchamiamy agenta
		Behaviour turnOnAgent = new OneShotBehaviour()
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
						if ("Agent0".equals(msg.getSender().getLocalName()) && "ON".equals(msg.getContent()))
						{
							System.out.println("A4: Odebrano komunikat " + msg.getContent());
							
							ACLMessage reply = msg.createReply();
							reply.addReceiver(new AID("Agent0", AID.ISLOCALNAME));
							reply.setContent(msg.getContent());
							myAgent.send(reply);
							
							readyToWork = true;
						}
					}
					
				}while(readyToWork == false);
			}
			
		};
		
		// TODO Odbieramy sygna³ o ruchu
		Behaviour receiveMessageAboutMove = new CyclicBehaviour()
		{
			@Override
			public void action() 
			{
				ACLMessage msg = receive();
				
				if (msg != null)
				{
					standby = false;
					
					if ("Agent0".equals(msg.getSender().getLocalName()))
					{
						System.out.println("A4: Odebrano komunikat " + msg.getContent());
						
						ACLMessage reply = msg.createReply();
						reply.addReceiver(new AID("Agent0", AID.ISLOCALNAME));
						reply.setContent(msg.getContent());
						myAgent.send(reply);
						
						if ("MOVE UP".equals(msg.getContent()) || "TURN RIGHT".equals(msg.getContent())) state = 1;
						else if ("MOVE DOWN".equals(msg.getContent()) || "TURN LEFT".equals(msg.getContent())) state = -1;
						else state = 0; 
					}
					
					msg = null;
				}
				else
				{
					if (standby == false) 
					{
						standby = true;
						System.out.println("A4: Czekam na polecenie!");
					}
				}
			}
					
				};
				
				addBehaviour(turnOnAgent);
				addBehaviour(receiveMessageAboutMove);
	}
}
