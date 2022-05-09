package eu.su.mas.dedaleEtu.mas.behaviours.perso;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import eu.su.mas.dedaleEtu.mas.agents.dummies.perso.AgentProjet;
import eu.su.mas.dedaleEtu.mas.knowledge.Agent;
import eu.su.mas.dedaleEtu.mas.knowledge.AgentManager;
import eu.su.mas.dedaleEtu.mas.knowledge.Field;
import jade.core.AID;
import jade.core.behaviours.OneShotBehaviour;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;

public class EcouteReceptionAgentManager extends OneShotBehaviour{

	public static final int NO_REPLY = 0;
	
	public static final int REPLY = 1;
	
	private int status = EcouteReceptionAgentManager.NO_REPLY; 
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 3763809333870707384L;

	private AgentProjet myAgent;
	
	public EcouteReceptionAgentManager(AgentProjet myAgent) {
		super(myAgent);
		this.myAgent = myAgent;
	}
	
	@Override
	public void action() {
		
		/* Definition du protocole de reception du message */
		MessageTemplate msgTemplate=MessageTemplate.and(
				MessageTemplate.MatchProtocol(AgentManager.PROTOCOLE_COMMUNICATION),
				MessageTemplate.MatchPerformative(ACLMessage.INFORM));
		
		/* Reception du message */
		ACLMessage msgReceived=this.myAgent.receive(msgTemplate);
		
		if (msgReceived!=null) {
			AID sender = msgReceived.getSender();
			
			String name_sender = sender.getLocalName();
			
			String message = msgReceived.getContent();
			
			JSONParser parser = new JSONParser();
			try {
				Object obj = parser.parse(message);
				
				JSONArray array = (JSONArray)obj;
				
				status = EcouteReceptionAgentManager.REPLY;
				
				/* Met a jour l'Agent Manager */
				updateAgentManager(array);
				
				/* Gere l'interblocage */
				interblocage(name_sender); 
			} catch (ParseException e) {
				e.printStackTrace();
				status = EcouteReceptionAgentManager.NO_REPLY;
			}
		}
		else {
			status = EcouteReceptionAgentManager.NO_REPLY;
		}
		
	}

	/*
	 * Cette méthode permet de mettre à jour l'AgentManager de l'agent avec la un message JSON
	 * @param array C'est le message JSON a traiter
	 * @return null
	 * */
	private void updateAgentManager(JSONArray array) {
		
		for(int i = 0 ; i < array.size() ; i++) {
			
			JSONObject obj = (JSONObject)array.get(i);
			
			/* diamond quantity*/
			String dq = obj.get("diamond_quantity").toString();
			int diamond_quantity = Integer.parseInt(dq);
			
			/* gold quantity*/
			String gq = obj.get("gold_quantity").toString();
			int gold_quantity = Integer.parseInt(gq);
			
			/* backpack gold */
			String bg = obj.get("backpack_gold").toString();
			int backpack_gold = Integer.parseInt(bg);
			
			/* backpack diamond */
			
			String bd = obj.get("backpack_diamond").toString();
			int backpack_diamond = Integer.parseInt(bd);
			
			/* name */
			
			String name = obj.get("name").toString();
			
			/* current position*/
			
			String current_position = obj.get("current_position").toString();
			
			/* cpt_after_intersection */
			
			String cai = obj.get("cpt_after_intersection").toString();
			int cpt_after_intersection = Integer.parseInt(cai);
			
			/* type */
			
			String t = obj.get("type").toString();
			
			Field.TYPE type = Field.TYPE.NONE;
			
			if(t.contains("GOLD")) {
				type = Field.TYPE.GOLD;
			}
			else if(t.contains("DIAMOND")) {
				type = Field.TYPE.DIAMOND;
			}
			
			/* backward */
			
			String bw = obj.get("backward").toString();
			boolean backward = Boolean.parseBoolean(bw);
			
			/* direction */
			
			/* L'extraction doit se faire dans un try/catch car si la direction est null, alors une exception sera levé*/
			Field direction = null;
			try {
				JSONObject direction_array = (JSONObject) obj.get("direction");
				
				/* diamond quantity direction*/
				String ddq = direction_array.get("diamond_quantity").toString();
				int direction_diamond_quantity = Integer.parseInt(ddq);
				
				/* gold quantity direction*/
				String dgq = direction_array.get("gold_quantity").toString();
				int direction_gold_quantity = Integer.parseInt(dgq);
				
				/* direction position */
				String direction_position = direction_array.get("position").toString();			
				
				/* time idleness direction */
				String dti = direction_array.get("time_idleness").toString();
				int direction_time_idleness = Integer.parseInt(dti);
				
				/* type direction */
				
				String td = direction_array.get("type").toString();
				
				Field.TYPE type_direction = Field.TYPE.NONE;
				
				if(td.contains("GOLD")) {
					type_direction = Field.TYPE.GOLD;
				}
				else if(t.contains("DIAMOND")) {
					type_direction = Field.TYPE.DIAMOND;
				}
				
				direction = new Field(direction_position);
				
				direction.set_diamond_quantity(direction_diamond_quantity);
				
				direction.set_gold_quantity(direction_gold_quantity);
				
				direction.set_time_idleness(direction_time_idleness);
				
				direction.set_type(type_direction);
			}
			catch(Exception e) {
				/* Rien a faire*/
			}
			
			/* L'extraction doit se faire dans un try/catch car si la derniere position est null, alors une exception sera levé*/
			Field last_position = null;
			try {
				JSONObject last_position_array = (JSONObject) obj.get("last_position");
				
				/* diamond quantity position*/
				String lpdq = last_position_array.get("diamond_quantity").toString();
				int last_position_diamond_quantity = Integer.parseInt(lpdq);
				
				/* gold quantity position*/
				String lpgq = last_position_array.get("gold_quantity").toString();
				int last_position_gold_quantity = Integer.parseInt(lpgq);
				
				//System.out.println("direction_gold_quantity : " + last_position_gold_quantity);
				
				/* last_position position */
				String last_position_position = last_position_array.get("position").toString();			
				
				/* time idleness direction */
				String lpti = last_position_array.get("time_idleness").toString();
				int last_position_time_idleness = Integer.parseInt(lpti);
				
				/* type last_position */
				
				String lpd = last_position_array.get("type").toString();
				
				Field.TYPE type_last_position = Field.TYPE.NONE;
				
				if(lpd.contains("GOLD")) {
					type_last_position = Field.TYPE.GOLD;
				}
				else if(t.contains("DIAMOND")) {
					type_last_position = Field.TYPE.DIAMOND;
				}
				
				last_position = new Field(last_position_position);
				
				last_position.set_diamond_quantity(last_position_diamond_quantity);
				
				last_position.set_gold_quantity(last_position_gold_quantity);
				
				last_position.set_time_idleness(last_position_time_idleness);
				
				last_position.set_type(type_last_position);
				
			}catch(Exception e) {
				/* Rien a faire */
			}
			
			/* Creation d'un agent avec les informations recus */
			Agent agent = new Agent(name,backpack_gold,backpack_diamond);
			
			agent.set_cpt_after_intersection(cpt_after_intersection);
			
			agent.setGold_quantity(gold_quantity);
			
			agent.setDiamond_quantity(diamond_quantity);
			
			agent.setType(type);
			
			agent.set_current_position(current_position);
			
			agent.set_backward(backward);
			
			agent.setDirection(direction);
			
			agent.setLastPosition(last_position);
			
			
			
			if(!myAgent.getAgentManager().get_agents().contains(agent) && !myAgent.getAgentManager().getMe().equals(agent)) {
				/* Si l'agent actuel ne connait pas l'agent créé */
				myAgent.getAgentManager().add_agent(agent);
			}
			else if(!myAgent.getAgentManager().getMe().equals(agent)){
				
				Agent current_agent = myAgent.getAgentManager().get_agent_with_name(name);
				
				if(current_agent.getDiamond_quantity() <= agent.getDiamond_quantity() ||
						current_agent.getGold_quantity() <= agent.getGold_quantity()) {
					
					/* Si l'agent créé est plus récent que la version que l'on connait alors on met a jour avec le critère de quantite de ressource */
					
					myAgent.getAgentManager().remove_agent(current_agent);
					
					myAgent.getAgentManager().add_agent(agent);
				}
				
				if(current_agent.getType().equals(Field.TYPE.NONE) && ( agent.getType().equals(Field.TYPE.DIAMOND) || agent.getType().equals(Field.TYPE.GOLD ))) {
					/* Si l'agent créé est plus récent que la version que l'on connait alors on met a jour avec le critère de type de ressource */
					
					myAgent.getAgentManager().remove_agent(current_agent);
					
					myAgent.getAgentManager().add_agent(agent);
				}
			}
		}
	}
	
	private void interblocage(String name_sender) {
		
		Agent agent_sender = myAgent.getAgentManager().get_agent_with_name(name_sender);
		
		if(agent_sender != null) {
			
			Field current_position = myAgent.getMapManager().get_field(myAgent.getCurrentPosition());
			
			/* Si l'agent actuel a une position = derniere position alors interblocage */
			if(myAgent.getAgentManager().getMe().isBlocked(current_position)) {
				
				/* Decision pour savoir si c'est l'agent créé ou l'agent actuel qui doit reculer */
				if(myAgent.getAgentManager().getMe().get_cpt_after_intersection() < agent_sender.get_cpt_after_intersection()) {
					
					/* Ici, c'est a l'agent actuel de reculer */
					myAgent.getAgentManager().getMe().set_backward(true);
					myAgent.getAgentManager().getMe().setDirection(myAgent.getAgentManager().getMe().getRandomDirectionIntersection());
					
				}
				else {
					agent_sender.set_backward(true);
				}
				
			}
		}
	}

	@Override
	public int onEnd() {
		return status;
	}
}
