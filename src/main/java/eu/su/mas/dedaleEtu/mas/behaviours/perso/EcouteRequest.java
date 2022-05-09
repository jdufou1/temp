package eu.su.mas.dedaleEtu.mas.behaviours.perso;

import java.util.Iterator;
import java.util.List;

import dataStructures.tuple.Couple;
import eu.su.mas.dedale.env.Observation;
import eu.su.mas.dedale.mas.AbstractDedaleAgent;
import eu.su.mas.dedaleEtu.mas.agents.dummies.perso.AgentProjet;
import eu.su.mas.dedaleEtu.mas.knowledge.Agent;
import eu.su.mas.dedaleEtu.mas.knowledge.Field;
import eu.su.mas.dedaleEtu.mas.knowledge.MapRepresentation.MapAttribute;
import jade.core.behaviours.OneShotBehaviour;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;

public class EcouteRequest extends OneShotBehaviour{

	
	public static final String CARTE = "CARTE";
	
	public static final String MAPMANAGER = "MAPMANAGER";
	
	public static final String AGENTMANAGER = "AGENTMANAGER";
	
	public static final int NO_REQUEST_NODECLOSED = 0;
	
	public static final int NO_REQUEST_NODEOPENED = 1;
	
	public static final int REQUEST_MAP = 2;
	
	public static final int REQUEST_MAPMANAGER = 3;
	
	public static final int REQUEST_AGENTMANAGER = 4;
	
	public static final int NO_REQUEST_AGENTMANAGER = 5;
	
	private AgentProjet ag;
	
	private int status;
	
	public EcouteRequest(AgentProjet ag) {
		super(ag);
		this.ag = ag;
	}
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Override
	public void action() {
		
		
		/* ROUTINE 1 : Mise a jour des noeuds ouvert */
		checkOpenedNode();
		
		
		/* ROUTINE 2 : Incrémentation de l'horloge de l'agent actuel */
		this.ag.up_clock();
		
		
		/* Reception d'une Request */
		MessageTemplate msgTemplate = MessageTemplate.MatchPerformative(ACLMessage.REQUEST);
		
		ACLMessage msgReceived = this.myAgent.receive(msgTemplate);
		
		if (msgReceived!=null && ag.getAgentManager().getMe().get_compteur() < Agent.LIMIT_MESSAGE) {
			
			/* Traitement de la request */
			
			String content = msgReceived.getContent();
			if(content.contains(EcouteRequest.CARTE)) {
				
				/* Demande de partage de carte */
				status = EcouteRequest.REQUEST_MAP;
			}
			else if(content.contains(EcouteRequest.MAPMANAGER)) {
				
				/* Demande de partage de MapManager */
				status = EcouteRequest.REQUEST_MAPMANAGER;
			}
			else if(content.contains(EcouteRequest.AGENTMANAGER)){
				
				/* Demande de partage d'AgentManager */
				status = EcouteRequest.REQUEST_AGENTMANAGER;
			}
			else {
				
				/* Sinon si l'exploration est fini l'agent passe a la phase recolte sinon il fait une demande de carte */
				status = /*ag.getMap().hasOpenNode()*/ !exploration_end() ? EcouteRequest.NO_REQUEST_NODEOPENED : EcouteRequest.NO_REQUEST_NODECLOSED;
			}
			
		}
		else {
			
			
			if(ag.getAgentManager().getMe().getLastPosition() != null  &&  ag.getAgentManager().getMe().getDirection() != null &&
					ag.getCurrentPosition().equals(ag.getAgentManager().getMe().getLastPosition().get_position())) {
				
					status = EcouteRequest.NO_REQUEST_NODEOPENED;
					
					if(agent_presence()) {
						/* interblocage avec agent */
						status = EcouteRequest.NO_REQUEST_AGENTMANAGER;
					}
					else if(stench_presence()) {
						ag.getAgentManager().getMe().set_backward(true);
						ag.getAgentManager().getMe().setDirection(ag.getAgentManager().getMe().getRandomDirectionIntersection());
					}
					else {
						status = EcouteRequest.NO_REQUEST_NODECLOSED;
					}
					
				}
			
			else {
				
				status =  /* ag.getMap().hasOpenNode() */  !exploration_end()  ? EcouteRequest.NO_REQUEST_NODEOPENED : EcouteRequest.NO_REQUEST_NODECLOSED;
			}
		}
		ag.getAgentManager().getMe().increment_compteur();
		if(ag.getAgentManager().getMe().get_compteur() > Agent.LIMIT_MESSAGE) {
			ag.getAgentManager().getMe().reset_compteur();
		}
	}
	
	
	/*
	 * Méthode qui va vérifier si il existe des noeuds ouverts dans les cases adjacentes à l'agent
	 * @param null
	 * @return null
	 * */
	private void checkOpenedNode() {
		List<Couple<String,List<Couple<Observation,Integer>>>> lobs=((AbstractDedaleAgent)this.myAgent).observe();
		String myPosition=((AbstractDedaleAgent)this.myAgent).getCurrentPosition();
		if (myPosition!=null){
			ag.getMap().addNode(myPosition, MapAttribute.closed);
			String nextNode=null;
			Iterator<Couple<String, List<Couple<Observation, Integer>>>> iter=lobs.iterator();
			while(iter.hasNext()){
				String nodeId=iter.next().getLeft();
				boolean isNewNode=ag.getMap().addNewNode(nodeId);
				if (myPosition!=nodeId) {
					ag.getMap().addEdge(myPosition, nodeId);
					if (nextNode==null && isNewNode) nextNode=nodeId;
				}
			}
		}
	}
	
	
	/*
	 * Fonction qui renvoit vrai si il n'y a plus de noeud ouvert ou si les seuls noeud ouvert on ete bloque par le wumpus
	 * @param null
	 * @return boolean 
	 * */
	private boolean exploration_end() {
		List<String> open_nodes = ag.getMap().getOpenNodes();
		for(String node : open_nodes) {
			Field field = new Field(node);
			if(!ag.getAgentManager().getMe().get_open_nodes_blocked().contains(field)) {
				return false;
			}
		}
		return true;
	}
	
	private boolean stench_presence() {
		
		List<Couple<String,List<Couple<Observation,Integer>>>> lobs=((AbstractDedaleAgent)this.myAgent).observe();
		
		for(Couple<String,List<Couple<Observation,Integer>>> obs : lobs) {
			/* L'agent actuel est sur la position */
				for(Couple<Observation,Integer> item : obs.getRight()) {
					if(item.getLeft().equals(Observation.STENCH)) {
						return true;
					}
				}
		}
		return false;
	}
	
	private boolean agent_presence() {
		
		List<String> path = ag.getMap().getShortestPath(ag.getCurrentPosition(),ag.getAgentManager().getMe().getDirection().get_position());
		
		if(path.size() == 0) {
			return false;
		}
		
		Field nextMove = new Field(path.get(0));
		
		List<Couple<String,List<Couple<Observation,Integer>>>> lobs=((AbstractDedaleAgent)this.myAgent).observe();
		
		for(Couple<String,List<Couple<Observation,Integer>>> obs : lobs) {
			/* L'agent actuel est sur la position */
			if(nextMove.get_position().equals(obs.getLeft())) {
				for(Couple<Observation,Integer> item : obs.getRight()) {
					if(item.getLeft().equals(Observation.STENCH)) {
						return false;
					}
				}
			}
		}
		return true;
	}
	
	
	@Override
	public int onEnd() {
		return status;
	}

}
