package com.cs117.units;

public abstract class Unit {
	private int hp;
	protected String name;
	protected int attackStr;
	protected int atkRange;
	protected int moveRange;
	protected int maxHP;
	protected char orientation;
	
	// 0 -> red; 1 -> blue
	protected int team;

	
	public Unit(int hp, int team) {
		this.hp = hp;
		this.team = team;
		this.orientation = 'r';
	}
	
	public Unit(int hp, int team, char orientation) {
		this.hp = hp;
		this.team = team;
		this.orientation = orientation;
	}
	
	public int getTeam() {
		return team;
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
	
	public int getAtkRange()
	{
		return atkRange;
	}
	
	public int getMoveRange()
	{
		return moveRange;
	}
	
	public int getMaxHP()
	{
		return maxHP;
	}
	
	public void setHp(int hp) {
		this.hp = hp;
	}
	
	public char getOrientation() {
		return orientation;
	}
	
	public void setOrientation(char orientation) {
		this.orientation = orientation;
	}
	
	public String toString() {
		return name;
	}
		
	public abstract void getAttacked(Unit attackingUnit);
	
	public abstract boolean isMntnClimber();
	
	
}
