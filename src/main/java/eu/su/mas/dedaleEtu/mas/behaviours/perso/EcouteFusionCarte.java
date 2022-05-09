package eu.su.mas.dedaleEtu.mas.behaviours.perso;

import dataStructures.serializableGraph.SerializableSimpleGraph;
import eu.su.mas.dedaleEtu.mas.agents.dummies.perso.AgentProjet;
import eu.su.mas.dedaleEtu.mas.knowledge.MapRepresentation;
import eu.su.mas.dedaleEtu.mas.knowledge.MapRepresentation.MapAttribute;
import jade.core.behaviours.OneShotBehaviour;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;
import jade.lang.acl.UnreadableException;

public class EcouteFusionCarte extends OneShotBehaviour{

	
	AgentProjet ag;
	
	private int status;
	
	public static final int NO_RECEIVE = 0;
	public static final int RECEIVE = 1;
	
	/**
	 * 
	 */
	
	public EcouteFusionCarte(AgentProjet ag) {
		super(ag);
		this.ag = ag;
	}
	
	private static final long serialVersionUID = -3149319441266478010L;

	@SuppressWarnings("unchecked")
	@Override
	public void action() {
		/* Definition du protocole de reception du message */
		
		MessageTemplate msgTemplate=MessageTemplate.and(
				MessageTemplate.MatchProtocol(MapRepresentation.PROTOCOLE_COMMUNICATION),
				MessageTemplate.MatchPerformative(ACLMessage.INFORM));
		
		/* Reception du message */
		
		ACLMessage msgReceived=this.myAgent.receive(msgTemplate);
		
		if (msgReceived!=null) {
			SerializableSimpleGraph<String, MapAttribute> sgreceived=null;
			try {
				sgreceived = (SerializableSimpleGraph<String, MapAttribute>)msgReceived.getContentObject();
			} catch (UnreadableException e) {
				status = EcouteFusionCarte.NO_RECEIVE;
			}
			/* Fusion de la carte en mémoire et de la carte recu*/
			ag.getMap().mergeMap(sgreceived);
			
			status = EcouteFusionCarte.RECEIVE;
		}
		else {
			status = EcouteFusionCarte.NO_RECEIVE;
		}
	}
	
	@Override
	public int onEnd() {
		return status;
	}

}
