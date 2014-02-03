package com.cs117.tile_placeable;

public class Unit {
	private int hp;
	private UNIT_TYPE type;
	
	public Unit(int hp, UNIT_TYPE type) {
		this.hp = hp;
		this.type = type;
	}
	
	public int getHp() {
		return hp;
	}
	
	public UNIT_TYPE getUnitType() {
		return type;
	}
	
	public void setHp(int hp) {
		this.hp = hp;
	}
	
}
