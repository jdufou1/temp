package eu.su.mas.dedaleEtu.mas.behaviours.perso;

import eu.su.mas.dedaleEtu.mas.agents.dummies.perso.AgentProjet;
import jade.core.behaviours.Behaviour;
import jade.core.behaviours.FSMBehaviour;


/*
 * Classe comportant les états de la FSM pour décrire les comportements de nos agents
 * */
public class BehavioursFSM extends FSMBehaviour{

	private enum State {
		/* Exploration */
		InitialisationState,
		EcouteRequest,
		Move,
		
		/* Carte */
		RequestCarte,
		EcouteReceptionCarte,
		EnvoiCarte,
		EcouteFusionCarte,
		ReplyCarte,
		
		/* MapManager */
		EnvoiMapManager,
		RequestMapManager,
		EcouteReceptionMapManager,
		EcouteFusionMapManager,
		ReplyMapManager,
		
		/* AgentManager */
		EnvoiAgentManager,
		RequestAgentManager,
		EcouteReceptionAgentManager,
		EcouteFusionAgentManager,
		ReplyAgentManager,
		
		/* Récolte */
		CalculBestNode,
		Pick
		;
		
		public String toString() {
			return this.name();
		}
	}
	
	private static final long serialVersionUID = -4040431509336162365L;

	private AgentProjet ag;
	
	public BehavioursFSM(AgentProjet a) {
		super(a);
		this.ag = a;
		Behaviour b;
		
		/*
		 *  PREMIER ETAT
		 * */
		
		b = new InitialisationState(a);
		b.setDataStore(this.getDataStore());
		this.registerFirstState(b, State.InitialisationState.name());
		/*
		 * DECLARATION DES ETATS DE L'AUTOMATE
		 * */
		b = new EcouteRequest(a);
		b.setDataStore(this.getDataStore());
		this.registerState(b, State.EcouteRequest.name());
		
		/* EXPLORATION */
		
		b = new MoveBehaviour(a);
		b.setDataStore(this.getDataStore());
		this.registerState(b, State.Move.name());
		
		/* CARTE */
		
		b = new RequestCarte(a,a.getListAgentNames());
		b.setDataStore(this.getDataStore());
		this.registerState(b, State.RequestCarte.name());
		
		b = new EcouteFusionCarte(a);
		b.setDataStore(this.getDataStore());
		this.registerState(b, State.EcouteFusionCarte.name());
		
		b = new EnvoiCarte(a,a.getListAgentNames());
		b.setDataStore(this.getDataStore());
		this.registerState(b, State.EnvoiCarte.name());
		
		b = new EcouteReceptionCarte(a);
		b.setDataStore(this.getDataStore());
		this.registerState(b, State.EcouteReceptionCarte.name());
		
		b = new ReplyCarte(a,a.getListAgentNames());
		b.setDataStore(this.getDataStore());
		this.registerState(b, State.ReplyCarte.name());
		
		/* MAPMANAGER */
		
		b = new EnvoiMapManager(a,a.getListAgentNames());
		b.setDataStore(this.getDataStore());
		this.registerState(b, State.EnvoiMapManager.name());
		
		b = new ReplyMapManager(a,a.getListAgentNames());
		b.setDataStore(this.getDataStore());
		this.registerState(b, State.ReplyMapManager.name());
		
		b = new EcouteReceptionMapManager(a);
		b.setDataStore(this.getDataStore());
		this.registerState(b, State.EcouteReceptionMapManager.name());
		
		b = new EcouteFusionMapManager(a);
		b.setDataStore(this.getDataStore());
		this.registerState(b, State.EcouteFusionMapManager.name());
		
		b = new RequestMapManager(a,a.getListAgentNames());
		b.setDataStore(this.getDataStore());
		this.registerState(b, State.RequestMapManager.name());
		
		/* AGENTMANAGER */
		
		b = new EnvoiAgentManager(a,a.getListAgentNames());
		b.setDataStore(this.getDataStore());
		this.registerState(b, State.EnvoiAgentManager.name());
		
		b = new ReplyAgentManager(a,a.getListAgentNames());
		b.setDataStore(this.getDataStore());
		this.registerState(b, State.ReplyAgentManager.name());
		
		b = new EcouteReceptionAgentManager(a);
		b.setDataStore(this.getDataStore());
		this.registerState(b, State.EcouteReceptionAgentManager.name());
		
		b = new EcouteFusionAgentManager(a);
		b.setDataStore(this.getDataStore());
		this.registerState(b, State.EcouteFusionAgentManager.name());
		
		b = new RequestAgentManager(a,a.getListAgentNames());
		b.setDataStore(this.getDataStore());
		this.registerState(b, State.RequestAgentManager.name());
		
		/* EXPLORATION */
		
		b = new CalculBestNode(a);
		b.setDataStore(this.getDataStore());
		this.registerState(b, State.CalculBestNode.name());
		
		
		b = new Pick(a);
		b.setDataStore(this.getDataStore());
		this.registerState(b, State.Pick.name());
		
		/*
		 * DECLARATION DES TRANSITIONS
		 * */
		
		/* CalculBestNode*/
		
		this.registerTransition(State.CalculBestNode.name(), State.EcouteRequest.name(),CalculBestNode.NO_DESTINATION);
		this.registerTransition(State.CalculBestNode.name(), State.Pick.name(),CalculBestNode.DESTINATION);
		
		/* Reply */
		
		this.registerDefaultTransition(State.ReplyCarte.name(),State.EcouteFusionCarte.name());
		this.registerDefaultTransition(State.ReplyMapManager.name(),State.EcouteFusionMapManager.name());
		this.registerDefaultTransition(State.ReplyAgentManager.name(),State.EcouteFusionAgentManager.name());
		
		/* Request */

		this.registerDefaultTransition(State.RequestMapManager.name(),State.EcouteReceptionMapManager.name());
		this.registerDefaultTransition(State.RequestCarte.name(),State.EcouteReceptionCarte.name());
		this.registerDefaultTransition(State.RequestAgentManager.name(),State.EcouteReceptionAgentManager.name());
		
		/* EcouteRequest */
		
		this.registerTransition(State.EcouteRequest.name(),State.ReplyCarte.name(), EcouteRequest.REQUEST_MAP);
		this.registerTransition(State.EcouteRequest.name(),State.ReplyMapManager.name(), EcouteRequest.REQUEST_MAPMANAGER);
		this.registerTransition(State.EcouteRequest.name(),State.ReplyAgentManager.name(), EcouteRequest.REQUEST_AGENTMANAGER);
		this.registerTransition(State.EcouteRequest.name(),State.RequestCarte.name(), EcouteRequest.NO_REQUEST_NODEOPENED);
		this.registerTransition(State.EcouteRequest.name(), State.CalculBestNode.name(),EcouteRequest.NO_REQUEST_NODECLOSED);
		this.registerTransition(State.EcouteRequest.name(), State.RequestAgentManager.name(),EcouteRequest.NO_REQUEST_AGENTMANAGER);
		
		/* EcouteReception */
		
		this.registerTransition(State.EcouteReceptionCarte.name(),State.EnvoiCarte.name(), EcouteReceptionCarte.REPLY);
		this.registerTransition(State.EcouteReceptionCarte.name(),State.Move.name(), EcouteReceptionCarte.NO_REPLY);
		
		this.registerTransition(State.EcouteReceptionMapManager.name(), State.EnvoiMapManager.name(),EcouteReceptionMapManager.REPLY);
		this.registerTransition(State.EcouteReceptionMapManager.name(), State.Move.name(),EcouteReceptionMapManager.NO_REPLY);
		
		this.registerTransition(State.EcouteReceptionAgentManager.name(), State.EnvoiAgentManager.name(),EcouteReceptionAgentManager.REPLY);
		this.registerTransition(State.EcouteReceptionAgentManager.name(), State.Move.name(),EcouteReceptionAgentManager.NO_REPLY);
		
		/* EcouteFusion */
		
		this.registerDefaultTransition(State.EcouteFusionCarte.name(),State.Move.name());
		this.registerDefaultTransition(State.EcouteFusionMapManager.name(),State.Move.name());
		this.registerTransition(State.EcouteFusionAgentManager.name(), State.Move.name(),EcouteFusionAgentManager.RECEIVE);
		this.registerTransition(State.EcouteFusionAgentManager.name(), State.Move.name(),EcouteFusionAgentManager.NO_RECEIVE);
		
		/* Envoi */
		
		this.registerDefaultTransition(State.EnvoiCarte.name(),State.RequestMapManager.name());
		this.registerDefaultTransition(State.EnvoiMapManager.name(),State.Move.name());
		this.registerDefaultTransition(State.EnvoiAgentManager.name(),State.Move.name());
		
		/* Move */
		
		this.registerDefaultTransition(State.Move.name(), State.EcouteRequest.name());
		
		/* Pick */
		
		this.registerTransition(State.Pick.name(), State.EcouteRequest.name(),Pick.PICK);
		this.registerTransition(State.Pick.name(), State.Move.name(),Pick.NO_PICK);
		
		/* Initialisation */
		
		this.registerDefaultTransition(State.InitialisationState.name(),State.EcouteRequest.name());
		
		/* Autres */
		
	} 
	
	public AgentProjet getAgent() {
		return this.ag;
	}
	
}
