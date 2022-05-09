package eu.su.mas.dedaleEtu.mas.behaviours.perso;

import java.util.List;

import dataStructures.tuple.Couple;
import eu.su.mas.dedale.env.Observation;
import eu.su.mas.dedale.mas.AbstractDedaleAgent;
import eu.su.mas.dedaleEtu.mas.agents.dummies.perso.AgentProjet;
import eu.su.mas.dedaleEtu.mas.knowledge.Agent;
import eu.su.mas.dedaleEtu.mas.knowledge.AgentManager;
import eu.su.mas.dedaleEtu.mas.knowledge.Field;
import eu.su.mas.dedaleEtu.mas.knowledge.MapManager;
import eu.su.mas.dedaleEtu.mas.knowledge.MapRepresentation;
import jade.core.behaviours.OneShotBehaviour;

/*
 * Classe qui permet d'initialiser les connaissances de l'agent
 * */
public class InitialisationState extends OneShotBehaviour{

	private static final long serialVersionUID = 7294615798991702768L;
	
	private static final boolean verbose_mode = true;
	
	private AgentProjet ag;
	
	public InitialisationState(AgentProjet ag) {
		super(ag);
		this.ag = ag;
	}
	
	@Override
	public void action() {
		
		
		System.out.println("capacity : "+((AbstractDedaleAgent) this.ag).getBackPackFreeSpace());
		
		
		
		
		
		
		
		/* Initialisation des connaissances pour l'agent */
		
		ag.setMap(new MapRepresentation());
		
		ag.setMapManager(new MapManager());
		
		List<Couple<Observation,Integer>> result = ((AbstractDedaleAgent) this.myAgent).getBackPackFreeSpace();
		
		int backpack_gold = 0, backpack_diamond = 0;
		
		for(Couple<Observation,Integer> couple : result) {
			
			if(couple.getLeft().equals(Observation.GOLD)) {
				backpack_gold = couple.getRight();
			}
			
			else if(couple.getLeft().equals(Observation.DIAMOND)){
				backpack_diamond = couple.getRight();
			}
		}
		
		Agent me = new Agent(ag.getLocalName(),backpack_gold,backpack_diamond);
		
		ag.setAgentManager(new AgentManager(me));
		
		Field first_position = new Field(ag.getCurrentPosition());
		ag.getAgentManager().getMe().set_first_spawn(first_position);
		
		
		List<Couple<Observation, Integer>> list_capa = ((AbstractDedaleAgent) this.ag).getBackPackFreeSpace();
		
		for(Couple<Observation, Integer> capa : list_capa) {
			if(capa.getLeft() == Observation.GOLD) {
				ag.getAgentManager().getMe().setBackPackGold(capa.getRight());
			}
			else if(capa.getLeft() == Observation.DIAMOND) {
				ag.getAgentManager().getMe().setBackPackDiamond(capa.getRight());
			}
		}
		if(verbose_mode) {
			System.out.println("[InitialisationState] : Initialisation des connaissances de l'agent "+ag.getLocalName());
		}
		
		
		
		
		
	}

}
