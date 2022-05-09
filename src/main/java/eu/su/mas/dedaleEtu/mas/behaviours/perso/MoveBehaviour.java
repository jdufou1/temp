package eu.su.mas.dedaleEtu.mas.behaviours.perso;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import dataStructures.tuple.Couple;
import eu.su.mas.dedale.env.Observation;
import eu.su.mas.dedale.mas.AbstractDedaleAgent;
import eu.su.mas.dedaleEtu.mas.agents.dummies.perso.AgentProjet;
import eu.su.mas.dedaleEtu.mas.knowledge.Field;
import eu.su.mas.dedaleEtu.mas.knowledge.MapRepresentation.MapAttribute;
import jade.core.behaviours.OneShotBehaviour;

public class MoveBehaviour extends OneShotBehaviour{

	public static final int TIME = 100; /* Temps d'attente pour eviter les bugs de synchronisation avec l'API */
	
	private static final long serialVersionUID = -2937732320362543855L;

	private AgentProjet myAgent;
	
	public MoveBehaviour(AgentProjet ag) {
		super(ag);
		myAgent = ag;
	}
	
	@Override
	public void action() {
		
		String myPosition=((AbstractDedaleAgent)this.myAgent).getCurrentPosition();
		
		myAgent.getAgentManager().getMe().setLastPosition(myAgent.getMapManager().get_field(myAgent.getAgentManager().getMe().get_current_position()));
		
		if (myPosition!=null){
			
			List<Couple<String,List<Couple<Observation,Integer>>>> lobs=((AbstractDedaleAgent)this.myAgent).observe();
			
			
			/* ETAPE 1 : On enregistre les infos dans le MapManager */
			
			for(Couple<String,List<Couple<Observation,Integer>>> obs : lobs) {
				
				/* Si on se trouve sur la position actuelle alors on sauvegarde les infos dans le MapManager */
				
				if(myPosition.equals(obs.getLeft())) {
					
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
						if(analyze.getLeft().equals(Observation.STENCH)) {
							
							field.set_stench(true);
							
						}
						else {
							
							field.set_stench(false);
							
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
			try {
				
				this.myAgent.doWait(MoveBehaviour.TIME);
				
			} catch (Exception e) {
				e.printStackTrace();
			}
			
			/* ETAPE 2 : On met a jour la MapRepresentation */
			
			myAgent.getMap().addNode(myPosition, MapAttribute.closed);
			
			Iterator<Couple<String, List<Couple<Observation, Integer>>>> iter=lobs.iterator();
			
			while(iter.hasNext()){
				
				String nodeId=iter.next().getLeft();
				
				if (myPosition!=nodeId) {
					
					myAgent.getMap().addEdge(myPosition, nodeId);
					
				}
			}
			
			
			if(myAgent.getCurrentPosition().equals(myAgent.getAgentManager().getMe().get_first_spawn().get_position())) {
				this.myAgent.doWait( MoveBehaviour.TIME);
			}
			
			/* ETAPE 3 : Choix de la direction a prendre */
			
			if(myAgent.getAgentManager().getMe().getDirection() != null && 
					myAgent.getAgentManager().getMe().getDirection().get_position().equals(myPosition)) {
				
				/* Si l'agent actuel est arrive a destination */
				
				myAgent.getAgentManager().getMe().setDirection(null);
				
				myAgent.getAgentManager().getMe().set_backward(false);
				
			}
			
			if (exploration_end() /* !myAgent.getMap().hasOpenNode() */){
				
				/* Si l'exploration est fini on suit alors la destination courante si il en existe */
				
				if(myAgent.getAgentManager().getMe().getDirection() != null) {
					
					/* On effectue le mouvement */
					
					List<String> path = myAgent.getMap().getShortestPath(myAgent.getCurrentPosition(),myAgent.getAgentManager().getMe().getDirection().get_position());
					
					if(path.size() > 0) {
						
						Field last_position = myAgent.getMapManager().get_field(myAgent.getCurrentPosition());
						
						((AbstractDedaleAgent)this.myAgent).moveTo(path.get(0));
						
						if(!last_position.get_position().equals(myAgent.getCurrentPosition())) {
							
							myAgent.getAgentManager().getMe().setLastPosition(last_position);
							
						}
						else {
							myAgent.getAgentManager().getMe().add_open_node_blocked(new Field(path.get(0)));
						}
						
					}
						
				}
				else {
					/* On ne fait rien */
				}
			}
			else{
				/* Si l'agent actuel connait une direction et que ce n'est pas notre position actuelle : il la suit*/
				
				if(myAgent.getAgentManager().getMe().getDirection() != null
						&& !(myAgent.getAgentManager().getMe().getDirection().get_position().equals(myPosition))) {
					
					/* On effectue le mouvement */
					
					List<String> path = myAgent.getMap().getShortestPath(myAgent.getCurrentPosition(),myAgent.getAgentManager().getMe().getDirection().get_position());
					
					if(path.size() > 0)
						((AbstractDedaleAgent)this.myAgent).moveTo(path.get(0));
					
					if(path.size() > 0 && myAgent.getCurrentPosition().equals(path.get(0))) {
						
						/* si le deplacement est un succes on enregistre notre derniere position */
						
						Field last_position = myAgent.getMapManager().get_field(myAgent.getAgentManager().getMe().get_current_position());
						
						myAgent.getAgentManager().getMe().setLastPosition(last_position);
					}
					else{
						
						/* si le deplacement est un echec  */
						
						myAgent.getAgentManager().getMe().add_open_node_blocked(new Field(path.get(0)));
						
						/* On redefinit une nouvelle destination sur un autre noeud ouvert */

						List<String> list_open_nodes = myAgent.getMap().getOpenNodes();
						
						for(String open_node : list_open_nodes) {
							
							Field field = new Field(open_node);
							
							if(!myAgent.getAgentManager().getMe().get_open_nodes_blocked().contains(field)) {
								
								myAgent.getAgentManager().getMe().setDirection(field);
								
								break;
							}
						}
						
						
					}
					
				}
				
				/* sinon on calcul une nouvelle direction d'exploration */
				
				else {
					
					String nextNode=myAgent.getMap().getShortestPathToClosestOpenNode(myPosition).get(0);
					
					Field last_position = myAgent.getMapManager().get_field(myAgent.getCurrentPosition());
					
					myAgent.getAgentManager().getMe().setLastPosition(last_position);
					
					/* On effectue le mouvement */
					((AbstractDedaleAgent)this.myAgent).moveTo(nextNode);

					Field direction = new Field(nextNode);
					
					if(!nextNode.equals(myAgent.getCurrentPosition())) {
						/* deplacement echec : */
						myAgent.getAgentManager().getMe().add_open_node_blocked(direction);
					}
					
					myAgent.getAgentManager().getMe().setDirection(direction);
				}
			}
			
			myAgent.getAgentManager().getMe().set_current_position(myAgent.getCurrentPosition());
			
			boolean deplacement = !(myPosition.equals(myAgent.getCurrentPosition()));
			
			lobs=((AbstractDedaleAgent)this.myAgent).observe();
			
			/* On verifie si il est dans une intersection */

			if(lobs.size()-1 > 2) {
				
				myAgent.getAgentManager().getMe().setlast_intersection_directions(null);
				
				/* On est dans une intersection */
				
				myAgent.getAgentManager().getMe().reset_cpt_after_intersection();
				
				ArrayList<Field> last_intersection_directions = new ArrayList<>();
				
				/* sauvegarde de l'intersection */
				
				for(Couple<String,List<Couple<Observation,Integer>>> obs : lobs) {
					
					/* Si on se trouve sur la position actuelle alors on sauvegarde les infos dans le MapManager */
					
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
					
					if(!(field.get_position().equals(myAgent.getCurrentPosition())))
						last_intersection_directions.add(field);
				}
				
				myAgent.getAgentManager().getMe().setlast_intersection_directions(last_intersection_directions);
			}
			else if(myAgent.getAgentManager().getMe().get_backward() && deplacement) {
				myAgent.getAgentManager().getMe().set_cpt_after_intersection(myAgent.getAgentManager().getMe().get_cpt_after_intersection()-1);
			}
			else if (deplacement){
				/* On n'est pas dans une intersection */
				myAgent.getAgentManager().getMe().increment_cpt_after_intersection();
			}
			
			
			myAgent.getAgentManager().getMe().set_current_position(myAgent.getCurrentPosition());
			
		}
	}
	
	/*
	 * Fonction qui renvoit vrai si il n'y a plus de noeud ouvert ou si les seuls noeud ouvert on ete bloque par le wumpus
	 * @param null
	 * @return boolean 
	 * */
	private boolean exploration_end() {
		List<String> open_nodes = myAgent.getMap().getOpenNodes();
		for(String node : open_nodes) {
			Field field = new Field(node);
			if(!myAgent.getAgentManager().getMe().get_open_nodes_blocked().contains(field)) {
				return false;
			}
		}
		return true;
	}

}
