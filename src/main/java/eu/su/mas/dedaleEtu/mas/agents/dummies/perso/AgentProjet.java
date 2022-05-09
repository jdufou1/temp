package eu.su.mas.dedaleEtu.mas.agents.dummies.perso;

import java.util.ArrayList;
import java.util.List;

import eu.su.mas.dedale.mas.AbstractDedaleAgent;
import eu.su.mas.dedale.mas.agent.behaviours.startMyBehaviours;
import eu.su.mas.dedaleEtu.mas.behaviours.ExploCoopBehaviour;
import eu.su.mas.dedaleEtu.mas.behaviours.perso.BehavioursFSM;
import eu.su.mas.dedaleEtu.mas.behaviours.perso.EnvoiBehaviour;
import eu.su.mas.dedaleEtu.mas.behaviours.perso.ReplyBehaviour;
import eu.su.mas.dedaleEtu.mas.knowledge.AgentManager;
import eu.su.mas.dedaleEtu.mas.knowledge.Field;
import eu.su.mas.dedaleEtu.mas.knowledge.MapManager;
import eu.su.mas.dedaleEtu.mas.knowledge.MapRepresentation;
import jade.core.behaviours.Behaviour;

public class AgentProjet extends AbstractDedaleAgent{

	public enum TYPE {
		GOLD,
		DIAMOND,
		NONE
	}
	
	private TYPE type = TYPE.NONE;
	
	private static final long serialVersionUID = 1L;
	
	private MapRepresentation myMap;
	
	private List<String> list_agentNames;
	
	private MapManager mm;
	
	private AgentManager am;
	
	private int clock = 0;

	protected void setup(){

		super.setup();
		
		final Object[] args = getArguments();
		
		list_agentNames=new ArrayList<String>();
		
		if(args.length==0){
			System.err.println("Error while creating the agent, names of agent to contact expected");
			System.exit(-1);
		}else{
			int i=2;
			while (i<args.length) {
				list_agentNames.add((String)args[i]);
				i++;
			}
		}

		List<Behaviour> lb=new ArrayList<Behaviour>();
		
		// lb.add(new ExploCoopBehaviour(this,this.myMap,list_agentNames));
		
		lb.add(new BehavioursFSM(this));
		
		addBehaviour(new startMyBehaviours(this,lb));
		
		System.out.println("the  agent "+this.getLocalName()+ " is started");
		
	}
	
	public List<String> getListAgentNames() {
		return list_agentNames;
	}
	
	public MapRepresentation getMap() {
		return myMap;
	}
	
	public void setMap(MapRepresentation myMap) {
		this.myMap = myMap;
	}
	
	public MapManager getMapManager() {
		return this.mm;
	}
	
	public void setMapManager(MapManager mm) {
		this.mm = mm;
	}

	public AgentManager getAgentManager() {
		return this.am;
	}
	
	public void setAgentManager(AgentManager am) {
		this.am = am;
	}
	
	public int get_clock() {
		return this.clock;
	}
	
	public void up_clock() {
		this.clock++;
	}

	public void set_type(AgentProjet.TYPE type) {
		this.type = type;
	}
	
	
}
