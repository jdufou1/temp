package eu.su.mas.dedaleEtu.mas.behaviours.perso;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import eu.su.mas.dedaleEtu.mas.agents.dummies.perso.AgentProjet;
import eu.su.mas.dedaleEtu.mas.knowledge.Field;
import eu.su.mas.dedaleEtu.mas.knowledge.MapManager;
import jade.core.behaviours.OneShotBehaviour;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;

public class EcouteFusionMapManager extends OneShotBehaviour{

	/**
	 * 
	 */
	private static final long serialVersionUID = -7551212233156649188L;

	AgentProjet ag;
	
	private int status;
	
	public static final int NO_RECEIVE = 0;
	public static final int RECEIVE = 1;
	
	public EcouteFusionMapManager(AgentProjet ag) {
		super(ag);
		this.ag = ag;
	}
	
	@Override
	public void action() {
		
		/* Definition du protocole de reception du message */
		MessageTemplate msgTemplate=MessageTemplate.and(
				MessageTemplate.MatchProtocol(MapManager.PROTOCOLE_COMMUNICATION),
				MessageTemplate.MatchPerformative(ACLMessage.INFORM));
		
		/* Reception du message */
		
		ACLMessage msgReceived=this.myAgent.receive(msgTemplate);
		
		if (msgReceived!=null) {
			String message = msgReceived.getContent();
			
			JSONParser parser = new JSONParser();
			try {
				Object obj = parser.parse(message);
				
				JSONArray array = (JSONArray)obj;
				
				/* Mise a jour du MapManager de l'agent avec les informations recus*/
				updateMapManager(array);
				
				status = EcouteReceptionMapManager.REPLY;
			} catch (ParseException e) {
				e.printStackTrace();
				
				status = EcouteReceptionMapManager.NO_REPLY;
			}
		}
		else {
			status = EcouteReceptionMapManager.NO_REPLY;
		}
	}
	
	private void updateMapManager(JSONArray array) {
		for(int i = 0 ; i < array.size() ; i++) {
			
			JSONObject obj = (JSONObject)array.get(i);
			String position = obj.get("position").toString();
			
			/* gold quantity */
			String qg = obj.get("gold_quantity").toString();
			int gold_quantity = Integer.parseInt(qg);
			
			/* time idleness */
			String ti = obj.get("time_idleness").toString();
			int time_idleness = Integer.parseInt(ti);
			
			String dq = obj.get("diamond_quantity").toString();
			int diamond_quantity = Integer.parseInt(dq);
			
			String t = obj.get("type").toString();
			Field.TYPE type = Field.TYPE.NONE;
			
			if(t.contains("GOLD")) {
				type = Field.TYPE.GOLD;
			}
			else if(t.contains("DIAMOND")) {
				type = Field.TYPE.DIAMOND;
			}
			
			Field field = new Field(position);
			
			field.set_type(type);
			
			field.set_diamond_quantity(diamond_quantity);
			
			field.set_gold_quantity(gold_quantity);
			
			field.set_time_idleness(time_idleness);
			
			if(!this.ag.getMapManager().get_fields().contains(field)) {
				
				this.ag.getMapManager().add_field(field);
				
			}
			else {
				Field current_field = this.ag.getMapManager().getFieldWithPosition(position);
				
				if(current_field.get_type().equals(Field.TYPE.NONE) && !field.get_type().equals(Field.TYPE.NONE)){
					
					this.ag.getMapManager().remove_field(current_field);
					
					this.ag.getMapManager().add_field(field);
				}
				else if(current_field.get_type() != Field.TYPE.NONE && !field.get_type().equals(Field.TYPE.NONE)) {
					
					if(field.get_gold_quantity() < current_field.get_gold_quantity() || 
							field.get_diamond_quantity() < current_field.get_diamond_quantity()){
						
						this.ag.getMapManager().remove_field(current_field);
						
						this.ag.getMapManager().add_field(field);
					}
				}
			}
		}
	}
	
	@Override
	public int onEnd() {
		return status;
	}
}
