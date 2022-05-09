package eu.su.mas.dedaleEtu.mas.knowledge;

public class Field {

	/*
	 * Types disponibles
	 * */
	public static enum TYPE {
		NONE,
		DIAMOND,
		GOLD
	}
	
	/*
	 * Type de ressource de la case
	 * */
	private TYPE type = TYPE.NONE;
	
	/*
	 * Quantité d'or sur la case
	 * */
	private int gold_quantity = 0;
	
	/*
	 * Quantité de diamant sur la case
	 * */
	private int diamond_quantity = 0;
	
	/*
	 * Temps d'innocupation de la case
	 * */
	private int time_idleness = 0; 
	
	/*
	 * Position de la case
	 * */
	private String position;
	
	/*
	 * présence de l'odeur du wumpus sur la case
	 * */
	private boolean stench = false;
	
	public Field(String position) {
		this.position = position;
	}
	
	public Field(String position,TYPE type,int quantity) {
		this.position = position;
		this.type = type;
		if(type == TYPE.GOLD) {
			this.gold_quantity = quantity;
		}
		else if(type == TYPE.DIAMOND) {
			this.diamond_quantity = quantity;
		}
	}
	
	public int get_gold_quantity() {
		return this.gold_quantity;
	}
	
	public int get_diamond_quantity() {
		return this.diamond_quantity;
	}
	
	public String get_position() {
		return this.position;
	}
	
	public TYPE get_type() {
		return this.type;
	}
	
	public void set_type(TYPE type) {
		this.type = type;
	}
	
	public void set_time_idleness(int time_idleness) {
		this.time_idleness = time_idleness;
	}
	
	public void up_time_idleness() {
		this.time_idleness++;
	}
	
	public void set_diamond_quantity(int diamond_quantity) {
		this.diamond_quantity = diamond_quantity;
	}
	
	public void set_gold_quantity(int gold_quantity) {
		this.gold_quantity = gold_quantity;
	}
	
	public int get_time_idleness() {
		return this.time_idleness;
	}
	
	public boolean get_stench() {
		return this.stench;
	}
	
	public void set_stench(boolean stench) {
		this.stench = stench;
	}
	
	@Override
	public boolean equals(Object obj) {
		if(obj instanceof Field) {
			Field field = (Field) obj;
			return field.get_position().equals(this.position);
		}
		return false;
	}
	
	public String toJson() {
		StringBuilder str = new StringBuilder();
		str.append("{");
		str.append("\"position\" : \""+this.position+"\"");
		str.append(",");
		str.append("\"type\" : \""+this.type+"\"");
		str.append(",");
		str.append("\"gold_quantity\" : "+this.gold_quantity);
		str.append(",");
		str.append("\"diamond_quantity\" : "+this.diamond_quantity);
		str.append(",");
		str.append("\"time_idleness\" : "+this.time_idleness);
		str.append("}");
		return str.toString();
	}
	
	@Override
	public String toString() {
		return this.toJson();
	}
}
