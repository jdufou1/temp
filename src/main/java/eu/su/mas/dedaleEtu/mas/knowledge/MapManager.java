package eu.su.mas.dedaleEtu.mas.knowledge;

import java.util.ArrayList;

public class MapManager {
	
	public static final String PROTOCOLE_COMMUNICATION = "SHARE-MAPMANAGER";
	
	private ArrayList<Field> fields = new ArrayList<>(); /* Liste des zones de la carte */
	
	private static final boolean interest_position_only = true; 
	
	public boolean add_field(Field field) {
		if(!this.fields.contains(field)) {
			
			this.fields.add(field);
			
			return true;
		}
		return false;
	}
	
	public void remove_field(Field field) {
		
		this.fields.remove(field);
	}
	
	public ArrayList<Field> get_type_field(Field.TYPE type) {
		ArrayList<Field> fields = new ArrayList<Field>();
		
		for(Field field : this.fields) {
			
			if(field.get_type() == type) {
				
				fields.add(field);
			}
		}
		return fields;
	}
	
	public ArrayList<Field> get_fields(){
		return this.fields;
	}
	
	public Field getFieldWithPosition(String position) {
		for(Field field : this.fields) {
			
			if(field.get_position().equals(position))
				return field;
		}
		return null;
	}
	
	public Field get_field(String position) {
		for(Field field : fields) {
			
			if(field.get_position().equals(position))
				return field;
		}
		return null;
	}
	
	public float get_sum_from_type_field(Field.TYPE type) {
		ArrayList<Field> fields = get_type_field(type);
		
		float quantity_gold = 0;
		
		float quantity_diamond = 0;
		
		for(Field field : fields) {
			
			quantity_gold += field.get_gold_quantity();
			
			quantity_diamond += field.get_diamond_quantity();
		}
		return type == Field.TYPE.GOLD ? quantity_gold : quantity_diamond;
	}
	
	
	public Field getFieldAfterRecolte() {
		if(this.fields.size() == 0) {
			return null;
		}
		else {
			Field bestField = this.fields.get(0);
			int bestValue = bestField.get_time_idleness();
			for(Field field : this.fields) {
				if(bestValue > field.get_time_idleness()) {
					bestValue = field.get_time_idleness();
					bestField = field;
				}
			}
			return bestField;
		}
		
	}
	
	
	/*
	 * @param null
	 * @return String Export du MapManager en JSON
	 * */
	public String toJson() {
		
		StringBuilder str = new StringBuilder();
		
		str.append("[");
		
		for(int i = 0; i < fields.size() ; i++) {
			
			if(interest_position_only && fields.get(i).get_type() != Field.TYPE.NONE) {
				
				str.append(fields.get(i).toJson());
				
				if(i+1 != fields.size()) {
					
					str.append(",");
					
				}
			}
			else if(!interest_position_only) {
				
				str.append(fields.get(i).toJson());
				
				if(i+1 != fields.size()) {
					
					str.append(",");
				}
			}
			
		}
		str.append("]");
		
		return str.toString();
	}
}
