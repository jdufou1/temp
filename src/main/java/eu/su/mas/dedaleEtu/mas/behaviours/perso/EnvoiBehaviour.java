package eu.su.mas.dedaleEtu.mas.behaviours.perso;

import java.util.List;

import eu.su.mas.dedale.mas.AbstractDedaleAgent;
import jade.core.AID;
import jade.core.Agent;
import jade.core.behaviours.OneShotBehaviour;
import jade.lang.acl.ACLMessage;

/*
 * 
 * */
public class EnvoiBehaviour extends OneShotBehaviour{

	private static final long serialVersionUID = 1L;
	
	public static final String START_COM = "START";
	
	private List<String> receivers;
	
	public EnvoiBehaviour(Agent a, List<String> receivers) {
		super(a);
		this.receivers = receivers;
	}

	@Override
	public void action() {
		ACLMessage msg = new ACLMessage(ACLMessage.REQUEST);
		
		msg.setSender(this.myAgent.getAID());
		
		for (String agentName : receivers) {
			
			msg.addReceiver(new AID(agentName,AID.ISLOCALNAME));
			
		}
		
		msg.setContent(START_COM);
		
		((AbstractDedaleAgent)this.myAgent).sendMessage(msg);
		
		this.myAgent.doWait(1000);
	}

}
