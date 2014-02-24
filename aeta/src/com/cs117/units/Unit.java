package com.cs117.units;

public class Unit {
	private int hp;
	protected String name;
	
	public Unit(int hp) {
		this.hp = hp;
	}
	
	public int getHp() {
		return hp;
	}
	
	
	public String getName() {
		return name;
	}
	
	public void setHp(int hp) {
		this.hp = hp;
	}
	
	public String toString() {
		return name;
	}
	
	
}
