package eu.su.mas.dedaleEtu.mas.knowledge;

import java.util.ArrayList;

public class Agent {
	
	public static final int LIMIT_MESSAGE = 2; 
	
	/*
	 * Modes disponibles
	 * */
	public static enum MODE { 
			EXPLORATION , 
			RECOLTE 
	}
	
	/*
	 * Mode de l'agent
	 * */
	private Agent.MODE mode = Agent.MODE.EXPLORATION; 
	
	/*
	 * Nom de l'agent
	 * */
	private String name = "";
	
	/*
	 * Zone d'apparition de l'agent
	 * */
	private Field first_spawn = null;
	
	/*
	 * Type de l'agent
	 * */
	private Field.TYPE type = Field.TYPE.NONE;
	
	/*
	 * Quantite d'or de l'agent
	 * */
	private int gold_quantity = 0;
	
	/*
	 * Quantite de diamant de l'agent
	 * */
	private int diamond_quantity = 0;
	
	/*
	 * Capacite d'or de l'agent
	 * */
	private int backpack_gold = 0;
	
	/*
	 * Capacite de diamant de l'agent
	 * */
	private int backpack_diamond = 0;
	
	/*
	 * Direction de l'agent
	 * */
	private Field direction = null;
	
	/*
	 * Derniere position de l'agent
	 * */
	private Field last_position = null;
	
	/*
	 * Zone(s) accessible(s) depuis la derniere intersection rencontree de l'agent
	 * */
	private ArrayList<Field> last_intersection_directions = new ArrayList<>();
	
	/*
	 * Noeud(s) ouverts bloquees par un wumpus de l'agent 
	 * */
	private ArrayList<Field> open_nodes_blocked = new ArrayList<>();
	
	/*
	 * Position courante de l'agent
	 * */
	private String current_position = null;
	
	/*
	 * Nombre de pas fait depuis la derniere intersection de l'agent
	 * */
	private int cpt_after_intersection = 0;
	
	/*
	 * Mode recul de l'agent
	 * */
	private boolean backward = false;
	
	/*
	 * Quota de ressource atteint
	 * */
	private boolean mean_reached = false;

	
	
	/*
	 * Compteur de message de l'agent (permet de controler le flux de message que l'on traite)
	 * */
	private int compteur_messages = 0;
	
	public Agent(String name, int backpack_gold,int backpack_diamond) {
		this.name = name;
		this.backpack_gold = backpack_gold;
		this.backpack_diamond = backpack_diamond;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Field.TYPE getType() {
		return type;
	}

	public void setType(Field.TYPE type) {
		this.type = type;
	}

	public int getGold_quantity() {
		return gold_quantity;
	}

	public void setGold_quantity(int gold_quantity) {
		this.gold_quantity = gold_quantity;
	}

	public int getDiamond_quantity() {
		return diamond_quantity;
	}

	public void setDiamond_quantity(int diamond_quantity) {
		this.diamond_quantity = diamond_quantity;
	}

	public int getBackPackGold() {
		return backpack_gold;
	}

	public void setBackPackGold(int backpack_gold) {
		this.backpack_gold = backpack_gold;
	}
	
	public int getBackPackDiamond() {
		return backpack_diamond;
	}
	
	public void setBackPackDiamond(int backpack_diamond) {
		this.backpack_diamond = backpack_diamond;
	}
	
	public Field getDirection() {
		return direction;
	}
	
	public void setDirection(Field direction) {
		this.direction = direction;
	}
	
	public Field getLastPosition() {
		return this.last_position;
	}
	
	public void setLastPosition(Field last_position) {
		this.last_position = last_position;
	}
	
	public void set_cpt_after_intersection(int cpt_after_intersection) {
		if(cpt_after_intersection < 0) {
			this.cpt_after_intersection = 0;
		}
		else {
			this.cpt_after_intersection = cpt_after_intersection;
		}
		
	}
	
	public int get_cpt_after_intersection() {
		return this.cpt_after_intersection;
	}
	
	public void increment_cpt_after_intersection() {
		this.cpt_after_intersection++;
	}
	
	public void reset_cpt_after_intersection() {
		this.cpt_after_intersection = 0;
	}
	
	public ArrayList<Field> getlast_intersection_directions(){
		return last_intersection_directions;
	}
	
	public void setlast_intersection_directions(ArrayList<Field> last_intersection_directions) {
		this.last_intersection_directions = last_intersection_directions;
	}
	
	public void set_mode(Agent.MODE mode){
		this.mode = mode;
	}
	
	public ArrayList<Field> get_open_nodes_blocked(){
		return this.open_nodes_blocked;
	}
	
	public void set_open_nodes_blocked(ArrayList<Field> open_nodes_blocked) {
		this.open_nodes_blocked = open_nodes_blocked;
	}
	
	public void add_open_node_blocked(Field field) {
		if(!open_nodes_blocked.contains(field)) {
			this.open_nodes_blocked.add(field);
		}
	}
	
	public void remove_open_node_blocked(Field field) {
		this.open_nodes_blocked.remove(field);
	}
	
	public Agent.MODE get_mode(){
		return this.mode;
	}
	
	public Field getRandomDirectionIntersection() {
		if(last_intersection_directions.size() == 0) {
			return null;
		}
		
		int min = 0;
		int max = last_intersection_directions.size() - 1;
		int range = max - min + 1;
		int index_rand =  (int)(Math.random() * range) + min;
		return last_intersection_directions.get(index_rand);
	}
	
	public boolean isBlocked(Field current_position) {
		if(last_position == null)
			return false;
		else
			return this.last_position.equals(current_position);
	}
	
	public boolean get_backward() {
		return this.backward;
	}
	
	public void set_backward(boolean backward) {
		this.backward = backward;
	}
	
	public String get_current_position(){
		return this.current_position;
	}
	
	public void set_current_position(String current_position) {
		this.current_position = current_position;
	}
	
	public Field get_first_spawn() {
		return this.first_spawn;
	}
	
	public void set_first_spawn(Field first_spawn) {
		this.first_spawn = first_spawn;
	}
	
	public int get_compteur() {
		return this.compteur_messages;
	}
	
	public void reset_compteur() {
		this.compteur_messages = 0;
	}
	
	public void increment_compteur(){
		this.compteur_messages++;
	}
	
	public boolean mean_reached() {
		return this.mean_reached;
	}
	
	public void set_mean_reached(boolean value) {
		this.mean_reached = value;
	}
	
	
	public String toJson() {
		
		StringBuilder str = new StringBuilder();
		
		str.append("{");
		
		str.append("\"name\" : \""+this.name+"\"");
		
		str.append(",");
		
		str.append("\"current_position\" : \""+this.current_position+"\"");
		
		str.append(",");
		
		str.append("\"type\" : \""+this.type+"\"");
		
		str.append(",");
		
		str.append("\"gold_quantity\" : "+this.gold_quantity);
		
		str.append(",");
		
		str.append("\"diamond_quantity\" : "+this.diamond_quantity);
		
		str.append(",");
		
		str.append("\"backpack_gold\" : "+this.backpack_gold);
		
		str.append(",");
		
		str.append("\"backpack_diamond\" : "+this.backpack_diamond);
		
		str.append(",");
		
		str.append("\"backward\" : "+this.backward);
		
		str.append(",");
		
		str.append("\"cpt_after_intersection\" : "+this.cpt_after_intersection);
		
		str.append(",");
		
		if(this.direction != null) {
			
			str.append("\"direction\" : "+this.direction.toJson());
			
		}
		else {
			
			str.append("\"direction\" : \"Null\"");
			
		}
		str.append(",");
		
		if(this.last_position != null) {
			
			str.append("\"last_position\" : "+this.last_position.toJson());
			
		}
		else {
			
			str.append("\"last_position\" : \"Null\"");
			
		}
		str.append("}");
		
		return str.toString();
	}
	
	@Override
	public boolean equals(Object obj) {
		if(obj instanceof Agent) {
			Agent agent = (Agent)obj;
			return this.name.equals(agent.getName());
		}
		return false;
	}
	
}
