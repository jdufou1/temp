package eu.su.mas.dedaleEtu.mas.behaviours.perso;

import java.util.List;

import eu.su.mas.dedale.mas.AbstractDedaleAgent;
import eu.su.mas.dedaleEtu.mas.agents.dummies.perso.AgentProjet;
import jade.core.AID;
import jade.core.behaviours.OneShotBehaviour;
import jade.lang.acl.ACLMessage;

public class RequestMapManager extends OneShotBehaviour{

	private static final long serialVersionUID = -7608338699224464261L;

	private List<String> receivers;
	
	public RequestMapManager(AgentProjet ag, List<String> receivers) {
		super(ag);
		this.receivers = receivers;
	}
	
	@Override
	public void action() {
		
		ACLMessage msg = new ACLMessage(ACLMessage.REQUEST);
		
		msg.setSender(this.myAgent.getAID());
		
		for (String agentName : receivers) {
			
			msg.addReceiver(new AID(agentName,AID.ISLOCALNAME));
			
		}
		
		msg.setContent(EcouteRequest.MAPMANAGER);
		
		((AbstractDedaleAgent)this.myAgent).sendMessage(msg);
		
	}
}
