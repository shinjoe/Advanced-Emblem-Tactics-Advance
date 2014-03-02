package com.cs117.units;

public abstract class Unit {
	private int hp;
	protected String name;
	protected int attackStr;
	
	public Unit(int hp) {
		this.hp = hp;
	}
	
	public int getHp() {
		return hp;
	}
	
	public String getName() {
		return name;
	}
	
	public int getAtkStr()
	{
		return attackStr;
	}
	
	public void setHp(int hp) {
		this.hp = hp;
	}
	
	public String toString() {
		return name;
	}
	
	public abstract void getAttacked(Unit attackingUnit);
	
	
}
