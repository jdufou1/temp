package eu.su.mas.dedaleEtu.mas.behaviours.perso;

import java.util.ArrayList;
import eu.su.mas.dedaleEtu.mas.agents.dummies.perso.AgentProjet;
import eu.su.mas.dedaleEtu.mas.knowledge.Field;
import jade.core.behaviours.OneShotBehaviour;

public class CalculBestNode extends OneShotBehaviour{

	public static final int TIME = 0;
	
	public static final int NB_AGENT = 6; /* A MODIFIER SELON CONFIGURATION */
	
	public static final int NO_DESTINATION = 0;
	public static final int DESTINATION = 1;
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private AgentProjet myAgent;

	private int status = CalculBestNode.NO_DESTINATION;
	
	public CalculBestNode(AgentProjet myAgent) {
		super(myAgent);
		this.myAgent = myAgent;
	}
	
	@Override
	public void action() {
		
		/// ETAPE 1 : CHOISIR LE TYPE DE RESSOURCE A COLLECTER
		
		Field.TYPE type_ressource_target = myAgent.getAgentManager().getMe().getType();
		
		if(type_ressource_target.equals(Field.TYPE.NONE)) {
			type_ressource_target = choose_affectation();
		}
		
		/// ETAPE 2 : CHOISIR QUEL DEPOT DE RESSOURCE A COLLECTER
		
		Field target = choose_destination(type_ressource_target);
		
		if(target == null) {
			myAgent.getAgentManager().getMe().set_open_nodes_blocked(new ArrayList<Field>());
			target = myAgent.getMapManager().getFieldAfterRecolte();
		}
		
		myAgent.getAgentManager().getMe().setDirection(target);
		
		
		status = (target == null) ? CalculBestNode.NO_DESTINATION : CalculBestNode.DESTINATION;
		
		try {
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	private Field.TYPE choose_affectation() {
		
		int nb_agent = CalculBestNode.NB_AGENT; // TODO A fixé plus tard
		
		/// ETAPE 1 : CALCUL DES MOYENNES OR ET DIAMAND
		
		/* On recupere les fields d'or et de diamands */
		
		ArrayList<Field> gold_fields = myAgent.getMapManager().get_type_field(Field.TYPE.GOLD);
		ArrayList<Field> diamond_fields = myAgent.getMapManager().get_type_field(Field.TYPE.DIAMOND);
		
		/* calcul des sommes de diamants et d'or disponible d'apres la connaissance de l'agent */
		
		float sum_gold = 0;
		for(Field field : gold_fields)
			sum_gold += field.get_gold_quantity();
		float sum_diamond = 0;
		for(Field field : diamond_fields)
			sum_diamond += field.get_diamond_quantity();
		
		/* calcul des moyennes d'or et de diamand par agent */
		
		float mean_gold = sum_gold / nb_agent;
		float mean_diamond = sum_diamond / nb_agent;
		
		float ratio_gold;
		if(mean_diamond == 0) {
			ratio_gold = 1; 
		}
		else if(mean_gold == 0) {
			ratio_gold = 0;
		}
		else {
			ratio_gold = mean_gold / (mean_gold + mean_diamond);
		}
		float ratio_diamond = 1-ratio_gold;
		
		/// ETAPE 2 : NORMALISATION DES CAPACITE DE STOCKAGE DE L'AGENT
		
		/* Calcul des ratio */
		
		float ratio_backpack_gold = (float) myAgent.getAgentManager().getMe().getBackPackGold() / (float)(myAgent.getAgentManager().getMe().getBackPackGold() + myAgent.getAgentManager().getMe().getBackPackDiamond());
		float ratio_backpack_diamond = 1 - ratio_backpack_gold;
		
		/// ETAPE 3 : CALCUL DES RATIO DES MOYENNES * RATIO DES CAPACITES
		
		float raw_gold_power = ratio_backpack_gold * ratio_gold;
		float raw_diamond_power = ratio_backpack_diamond * ratio_diamond;
		
		
		float gold_power = raw_gold_power / (raw_gold_power + raw_diamond_power);
		float diamond_power = 1 - gold_power;
		
		/* Si les puissances ne sont pas comprise entre 1-alpha <= puissance <= alpha : alors on arrondit les ratios à 0 et 1*/
		
		double alpha = 0.5; /* si on fixe a 0.5 alors version deterministe (classe majoritaire) sinon version probabiliste */
		
		if(gold_power > alpha) {
			gold_power = 1;
			diamond_power = 0;
		}
		if(diamond_power >= alpha) {
			gold_power = 0;
			diamond_power = 1;
		}
	
		/// ETAPE 4 : CHOIX DE L'AFFECTATION : OR OU DIAMAND
		
		double rand = Math.random();
		
		Field.TYPE type = myAgent.getAgentManager().getMe().getType();
		
		if(type.equals(Field.TYPE.NONE)) {
			if(rand < gold_power) {
				type = Field.TYPE.GOLD;
			}
			else {
				type = Field.TYPE.DIAMOND;
			}
		}
		return type;
	}
	
	/*
	 * Cette fonction renvoit la destination d'un agent en fonction du type Or ou Diamant
	 * @param type_ressource_target C'est le type de ressource que l'on souhaite atteindre
	 * @return Field C'est la destination
	 * */
	private Field choose_destination(Field.TYPE type_ressource_target) {
		
		int nb_agent = CalculBestNode.NB_AGENT; // TODO A fixer plus tard
		
		/// ETAPE 1 : VERIFIER SI ON A ATTEINT LA MOYENNE DE LA QUANTITE DE RESSOURCE QUE L'ON PEUT PRENDRE
		
		float sum_type_ressource = myAgent.getMapManager().get_sum_from_type_field(type_ressource_target);
		float mean_type_ressource = sum_type_ressource / nb_agent;
		
		if(mean_reached(mean_type_ressource,type_ressource_target) ||  capa_reached(type_ressource_target) ) {
			myAgent.getAgentManager().getMe().set_mean_reached(true);
			return null;
		}
		
		
		if(capa_reached(type_ressource_target)) {
			System.out.println(myAgent.getLocalName() + " : capacité atteinte");
		}
		
		
		
		
		/// ETAPE 2 : PARCOURIR LES FIELDS DU TYPE ET ALLER SUR LE DEPOT MINIMAL
		
		ArrayList<Field> fields_type = myAgent.getMapManager().get_type_field(type_ressource_target);
		
		/* si aucun depot n'est disponible on renvoit null */
		
		if(fields_type.size() == 0) {
			return null;
		}
		
		Field bestField = fields_type.get(0);
		
		int min = type_ressource_target.equals(Field.TYPE.GOLD) ? bestField.get_gold_quantity() : bestField.get_diamond_quantity();
		
		for(Field field : fields_type) {
			if(type_ressource_target.equals(Field.TYPE.GOLD) && field.get_gold_quantity() < min) {
				bestField = field;
				min = bestField.get_gold_quantity();
			}
			if(type_ressource_target.equals(Field.TYPE.DIAMOND) && field.get_diamond_quantity() < min) {
				bestField = field;
				min = bestField.get_diamond_quantity();
			}
		}
		return bestField;
	}
	
	/*
	 * Cette fonction est utilisée pour savoir si la moyenne est atteinte
	 * @param mean C'est la moyenne que l'on souhaite tester 
	 * @param type C'est le type de ressource de la moyenne a tester
	 * @return boolean Retourne vrai si la moyenne est atteinte
	 * */
	private boolean mean_reached(float mean,Field.TYPE type) {
		if(type.equals(Field.TYPE.DIAMOND)) {
			return myAgent.getAgentManager().getMe().getDiamond_quantity() >= mean;
		}
		else if(type.equals(Field.TYPE.GOLD)){
			return myAgent.getAgentManager().getMe().getGold_quantity() >= mean;
		}	
		return false;
	}
	
	
	
	public boolean capa_reached(Field.TYPE type) {
		
		if(type.equals(Field.TYPE.DIAMOND)) {
			return myAgent.getAgentManager().getMe().getDiamond_quantity() == myAgent.getAgentManager().getMe().getBackPackDiamond();
		}
		else if(type.equals(Field.TYPE.GOLD)){
			return myAgent.getAgentManager().getMe().getGold_quantity() == myAgent.getAgentManager().getMe().getBackPackGold();
		}	
		return true;
		
	}

	@Override
	public int onEnd() {
		return status;
	}
}
