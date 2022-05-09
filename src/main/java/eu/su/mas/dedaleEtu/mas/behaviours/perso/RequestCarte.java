package eu.su.mas.dedaleEtu.mas.behaviours.perso;

import java.util.List;

import eu.su.mas.dedale.mas.AbstractDedaleAgent;
import eu.su.mas.dedaleEtu.mas.agents.dummies.perso.AgentProjet;
import jade.core.AID;
import jade.core.behaviours.OneShotBehaviour;
import jade.lang.acl.ACLMessage;

public class RequestCarte extends OneShotBehaviour{

	private List<String> receivers;
	
	private static final long serialVersionUID = -5871263774373078041L;
	
	public RequestCarte(AgentProjet ag, List<String> receivers) {
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
		
		msg.setContent(EcouteRequest.CARTE);
		
		((AbstractDedaleAgent)this.myAgent).sendMessage(msg);
	}
	
}
