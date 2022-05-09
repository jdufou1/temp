package eu.su.mas.dedaleEtu.mas.behaviours.perso;

import java.util.List;

import eu.su.mas.dedale.mas.AbstractDedaleAgent;
import eu.su.mas.dedaleEtu.mas.agents.dummies.perso.AgentProjet;
import eu.su.mas.dedaleEtu.mas.knowledge.AgentManager;
import jade.core.AID;
import jade.core.behaviours.OneShotBehaviour;
import jade.lang.acl.ACLMessage;

public class ReplyAgentManager extends OneShotBehaviour{

	
	private AgentProjet ag;
	
	private List<String> receivers;
	
	private static final long serialVersionUID = 7575257548967820615L;

	public ReplyAgentManager(AgentProjet ag, List<String> receivers) {
		super(ag);
		this.ag = ag;
		this.receivers = receivers;
	}
	
	@Override
	public void action() {
		
		ACLMessage msg = new ACLMessage(ACLMessage.INFORM);
		
		msg.setProtocol(AgentManager.PROTOCOLE_COMMUNICATION);
		
		msg.setSender(this.myAgent.getAID());
		
		for (String agentName : receivers) {
			
			msg.addReceiver(new AID(agentName,AID.ISLOCALNAME));
			
		}
		
		msg.setContent(ag.getAgentManager().toJson());
		
		((AbstractDedaleAgent)this.myAgent).sendMessage(msg);
	}

}
