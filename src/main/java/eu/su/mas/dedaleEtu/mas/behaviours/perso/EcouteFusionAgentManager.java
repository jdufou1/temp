package eu.su.mas.dedaleEtu.mas.behaviours.perso;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import eu.su.mas.dedaleEtu.mas.agents.dummies.perso.AgentProjet;
import eu.su.mas.dedaleEtu.mas.knowledge.Agent;
import eu.su.mas.dedaleEtu.mas.knowledge.AgentManager;
import eu.su.mas.dedaleEtu.mas.knowledge.Field;
import jade.core.behaviours.OneShotBehaviour;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;

public class EcouteFusionAgentManager extends OneShotBehaviour{

	/**
	 * 
	 */
	private static final long serialVersionUID = -7809910938075577664L;

	AgentProjet myAgent;
	
	private int status;
	
	public static final int NO_RECEIVE = 0;
	public static final int RECEIVE = 1;
	
	public EcouteFusionAgentManager(AgentProjet myAgent) {
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

			String message = msgReceived.getContent();

			JSONParser parser = new JSONParser();

			try {
				Object obj = parser.parse(message);
				
				JSONArray array = (JSONArray)obj;
				
				status = EcouteFusionAgentManager.RECEIVE;
				
				updateAgentManager(array); /* Met a jour l'Agent Manager */
				
			} catch (ParseException e) {
				e.printStackTrace();
				status = EcouteFusionAgentManager.NO_RECEIVE;
			}
		}
		else {
			status = EcouteFusionAgentManager.NO_RECEIVE;
		}
	}
	
	/*
	 * Cette méthode permet de mettre à jour l'AgentManager de l'agent avec la un message JSON
	 * @param array C'est le message JSON a traiter
	 * @return null
	 * */
	private void updateAgentManager(JSONArray array) {
		/*
		 * Recoit les modifications des informations de l'autre agent et met a jour les connaissances du AgentManager
		 * */
				
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
				/* Rien a faire ici */
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
				/* Rien a faire ici */
			}
			
			/* creation d'un agent avec les informations recus */
			Agent agent = new Agent(name,backpack_gold,backpack_diamond);
			
			agent.set_cpt_after_intersection(cpt_after_intersection);
			
			agent.setGold_quantity(gold_quantity);
			
			agent.setDiamond_quantity(diamond_quantity);
			
			agent.setType(type);
			
			agent.set_current_position(current_position);
			
			agent.set_backward(backward);
	
			agent.setDirection(direction);
			
			agent.setLastPosition(last_position);
			
			if(myAgent.getAgentManager().getMe().equals(agent)) {
				/* Si l'agent créé correspond à l'agent actuel*/
				if(backward) {
					
					/* Si on a pour consigne de reculer */
					myAgent.getAgentManager().getMe().setDirection(myAgent.getAgentManager().getMe().getRandomDirectionIntersection());
				}
				
			}
			else if(!myAgent.getAgentManager().get_agents().contains(agent)) {
				/* Si l'agent actuel ne connait pas l'agent créé */
				myAgent.getAgentManager().add_agent(agent);
			}
			else{
				/* Si l'agent actuel connait l'agent créé */
				Agent current_agent = myAgent.getAgentManager().get_agent_with_name(name);
				
				myAgent.getAgentManager().remove_agent(current_agent);
				
				myAgent.getAgentManager().add_agent(agent);
			}
		}
	}
	
	@Override
	public int onEnd() {
		return status;
	}
	

}
