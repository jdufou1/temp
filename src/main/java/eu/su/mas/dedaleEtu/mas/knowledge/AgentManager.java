package eu.su.mas.dedaleEtu.mas.knowledge;

import java.util.ArrayList;

public class AgentManager {

	public static final String PROTOCOLE_COMMUNICATION = "SHARE-AGENTMANAGER";
	
	private Agent me;
	
	private ArrayList<Agent> agents = new ArrayList<>();
	
	public AgentManager(Agent me) {
		this.me = me;
	}
	
	public void set_agents(ArrayList<Agent> agents) {
		this.agents = agents;
	}
	
	public void add_agent(Agent agent) {
		this.agents.add(agent);
	}
	
	public void remove_agent(Agent agent) {
		this.agents.remove(agent);
	}
	
	public ArrayList<Agent> get_agents(){
		return this.agents;
	}
	
	public ArrayList<Agent> get_agents_type(Field.TYPE type){
		ArrayList<Agent> agents = new ArrayList<Agent>();
		for(Agent agent : this.agents) {
			if(agent.getType() == type) {
				agents.add(agent);
			}
		}
		return agents;
	}
	
	public Agent getMe() {
		return this.me;
	}
	
	public void set_me(Agent agent) {
		this.me = agent;
	}
	
	public Agent get_agent_with_name(String name) {
		for(Agent agent : agents) {
			if(agent.getName().equals(name)) {
				return agent;
			}
		}
		return null;
	}
	
	public String toJson() {
		StringBuilder str = new StringBuilder();
		str.append("[");
		str.append(me.toJson()+",");
		for(int i = 0; i < agents.size() ; i++) {
			str.append(agents.get(i).toJson());
			if(i+1 != agents.size()) {
				str.append(",");
			}
		}
		str.append("]");
		return str.toString();
	}
	
}
