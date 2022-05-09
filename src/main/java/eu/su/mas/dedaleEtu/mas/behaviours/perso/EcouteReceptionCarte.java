package eu.su.mas.dedaleEtu.mas.behaviours.perso;

import dataStructures.serializableGraph.SerializableSimpleGraph;
import eu.su.mas.dedaleEtu.mas.agents.dummies.perso.AgentProjet;
import eu.su.mas.dedaleEtu.mas.knowledge.MapRepresentation;
import eu.su.mas.dedaleEtu.mas.knowledge.MapRepresentation.MapAttribute;
import jade.core.behaviours.OneShotBehaviour;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;
import jade.lang.acl.UnreadableException;

public class EcouteReceptionCarte extends OneShotBehaviour{

	private AgentProjet ag;
	
	private int status;
	
	public static final int NO_REPLY = 0;
	
	public static final int REPLY = 1;
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -4927490953850204808L;

	public EcouteReceptionCarte(AgentProjet ag) {
		super(ag);
		this.ag = ag;
	}
	
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
				
				status = EcouteReceptionCarte.NO_REPLY;
				
			}
			
			/* Fusion de la carte en mémoire et de la carte recu*/
			ag.getMap().mergeMap(sgreceived);
			
			status = EcouteReceptionCarte.REPLY;
		}
		else {
			status = EcouteReceptionCarte.NO_REPLY;
		}
	}
	
	@Override
	public int onEnd() {
		return status;
	}
	
}
