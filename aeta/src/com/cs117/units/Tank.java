package com.cs117.units;

public class Tank extends Unit {
	
	private static final int TANK_DEFAULT_HP = 20;
	
	public Tank() {
		super(TANK_DEFAULT_HP);
		this.name = "TANK";
	}
	
	public Tank(int hp) {
		super(hp);
		this.name = "TANK";
	}
	
	

}
