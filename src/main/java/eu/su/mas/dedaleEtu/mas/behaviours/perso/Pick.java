package eu.su.mas.dedaleEtu.mas.behaviours.perso;

import java.util.List;

import dataStructures.tuple.Couple;
import eu.su.mas.dedale.env.Observation;
import eu.su.mas.dedale.mas.AbstractDedaleAgent;
import eu.su.mas.dedaleEtu.mas.agents.dummies.perso.AgentProjet;
import eu.su.mas.dedaleEtu.mas.knowledge.Field;
import jade.core.behaviours.OneShotBehaviour;

public class Pick extends OneShotBehaviour{

	public static final int PICK = 0;
	
	public static final int NO_PICK = 1;
	
	private static final long serialVersionUID = 1L;
	
	private static final boolean verbose_mode = true;
	
	private AgentProjet myAgent;
	
	private int status = Pick.NO_PICK;
	
	public Pick(AgentProjet myAgent) {
		super(myAgent);
		this.myAgent = myAgent;
	}
	
	@Override
	public void action() {
		
		List<Couple<String,List<Couple<Observation,Integer>>>> lobs=((AbstractDedaleAgent)this.myAgent).observe();
		
		for(Couple<String,List<Couple<Observation,Integer>>> obs : lobs) {
			
			/* Si on se trouve sur la position actuelle alors on sauvegarde les infos dans le MapManager */
			
			if(myAgent.getCurrentPosition().equals(obs.getLeft())) {
				
				Field field = new Field(obs.getLeft());
				
				for(Couple<Observation, Integer> analyze : obs.getRight()) {
					
					if(analyze.getLeft().equals(Observation.DIAMOND)) {
						
						field.set_type(Field.TYPE.DIAMOND);
						
						field.set_diamond_quantity(analyze.getRight());
						
					}
					else if(analyze.getLeft().equals(Observation.GOLD)) {
						
						field.set_type(Field.TYPE.GOLD);
						
						field.set_gold_quantity(analyze.getRight());
					}
				}
				
				field.set_time_idleness(this.myAgent.get_clock());
				
				if(!this.myAgent.getMapManager().add_field(field)) {
					
					Field current_field = this.myAgent.getMapManager().get_field(field.get_position());
					
					current_field.set_diamond_quantity(field.get_diamond_quantity());
					
					current_field.set_gold_quantity(field.get_gold_quantity());
					
					current_field.set_time_idleness(field.get_time_idleness());
					
					current_field.set_type(field.get_type());
				}
			}
		}
		
		/* Faire le ramassage de la ressource */
		 
		if(myAgent.getCurrentPosition().equals(myAgent.getAgentManager().getMe().getDirection().get_position())
				 && !myAgent.getAgentManager().getMe().mean_reached() ) {
			
			/* Deverouillage du coffre */
			
			if(myAgent.getAgentManager().getMe().getDirection().get_type().equals(Field.TYPE.DIAMOND))
				((AbstractDedaleAgent) this.myAgent).openLock(Observation.DIAMOND);
			if(myAgent.getAgentManager().getMe().getDirection().get_type().equals(Field.TYPE.GOLD))
				((AbstractDedaleAgent) this.myAgent).openLock(Observation.GOLD);
			
			int value_pick = ((AbstractDedaleAgent) this.myAgent).pick();
			
			/* Mise a jour des valeurs de l'agent */
			
			if(value_pick != 0) {
				myAgent.getAgentManager().getMe().setType(myAgent.getAgentManager().getMe().getDirection().get_type());
				if(verbose_mode) {
					System.out.println("[Pick] : L'agent "+myAgent.getLocalName()+" a recupere "+value_pick+" de "+myAgent.getAgentManager().getMe().getDirection().get_type());
				}
			}
				
			
			/* Mise a jour du stock personnel d'or et de diamant*/
			
			if(myAgent.getAgentManager().getMe().getDirection().get_type().equals(Field.TYPE.GOLD))
				myAgent.getAgentManager().getMe().setGold_quantity(myAgent.getAgentManager().getMe().getGold_quantity() + value_pick);
			else
				myAgent.getAgentManager().getMe().setDiamond_quantity(myAgent.getAgentManager().getMe().getDiamond_quantity() + value_pick);
			
			/* Mise a jour des valeurs des fields */
			
			Field field = myAgent.getMapManager().get_field(myAgent.getAgentManager().getMe().getDirection().get_position());
			
			if(myAgent.getAgentManager().getMe().getDirection().get_type().equals(Field.TYPE.GOLD)) {
				
				field.set_gold_quantity(field.get_gold_quantity() - value_pick);
				
			}
			else {
				
				field.set_diamond_quantity(field.get_diamond_quantity() - value_pick);
				
			}
			if(field.get_diamond_quantity() <= 0 && field.get_gold_quantity() <= 0)
				field.set_type(Field.TYPE.NONE);
			
			this.status = Pick.PICK;
		}
		else {
			
			this.status = Pick.NO_PICK;
			
		}
	}

	@Override
	public int onEnd() {
		return status;
	}
}
