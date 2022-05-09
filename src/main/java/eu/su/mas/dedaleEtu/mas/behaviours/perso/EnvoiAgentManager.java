package eu.su.mas.dedaleEtu.mas.behaviours.perso;

import java.util.List;

import eu.su.mas.dedale.mas.AbstractDedaleAgent;
import eu.su.mas.dedaleEtu.mas.agents.dummies.perso.AgentProjet;
import eu.su.mas.dedaleEtu.mas.knowledge.AgentManager;
import jade.core.AID;
import jade.core.behaviours.OneShotBehaviour;
import jade.lang.acl.ACLMessage;

public class EnvoiAgentManager extends OneShotBehaviour{

	/**
	 * 
	 */
	private static final long serialVersionUID = 523200356716279394L;
	
	private AgentProjet ag;

	private List<String> receivers;

	public EnvoiAgentManager(AgentProjet ag,List<String> receivers) {
		super(ag);
		this.receivers = receivers;
		this.ag = ag;
	}
	
	@Override
	public void action() {
		ACLMessage msg = new ACLMessage(ACLMessage.INFORM);
		
		msg.setSender(this.myAgent.getAID());
		
		msg.setProtocol(AgentManager.PROTOCOLE_COMMUNICATION);
		
		for (String agentName : receivers) {
			
			msg.addReceiver(new AID(agentName,AID.ISLOCALNAME));
			
		}
		
		msg.setContent(ag.getAgentManager().toJson());
		
		((AbstractDedaleAgent)this.myAgent).sendMessage(msg);
	}
}
