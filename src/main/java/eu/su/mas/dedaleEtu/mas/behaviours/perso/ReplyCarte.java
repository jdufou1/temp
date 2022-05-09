package eu.su.mas.dedaleEtu.mas.behaviours.perso;

import java.io.IOException;
import java.util.List;

import dataStructures.serializableGraph.SerializableSimpleGraph;
import eu.su.mas.dedale.mas.AbstractDedaleAgent;
import eu.su.mas.dedaleEtu.mas.agents.dummies.perso.AgentProjet;
import eu.su.mas.dedaleEtu.mas.knowledge.MapRepresentation;
import eu.su.mas.dedaleEtu.mas.knowledge.MapRepresentation.MapAttribute;
import jade.core.AID;
import jade.core.behaviours.OneShotBehaviour;
import jade.lang.acl.ACLMessage;

public class ReplyCarte extends OneShotBehaviour{

	private AgentProjet ag;
	
	private List<String> receivers;
	
	private static final long serialVersionUID = -7690147021150732204L;

	public ReplyCarte(AgentProjet ag, List<String> receivers) {
		super(ag);
		this.ag = ag;
		this.receivers = receivers;
	}
	
	@Override
	public void action() {
		
		ACLMessage msg = new ACLMessage(ACLMessage.INFORM);
		
		msg.setProtocol(MapRepresentation.PROTOCOLE_COMMUNICATION);
		
		msg.setSender(this.myAgent.getAID());
		
		for (String agentName : receivers) {
			
			msg.addReceiver(new AID(agentName,AID.ISLOCALNAME));
			
		}
		
		SerializableSimpleGraph<String, MapAttribute> sg=this.ag.getMap().getSerializableGraph();
		
		try {	
			
			msg.setContentObject(sg);
			
		} catch (IOException e) {
			
			e.printStackTrace();
			
		}
		
		((AbstractDedaleAgent)this.myAgent).sendMessage(msg);
		
	}

}
