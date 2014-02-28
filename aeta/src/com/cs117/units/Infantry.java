package com.cs117.units;

public class Infantry extends Unit {
	
	private static final int INFANTRY_DEFAULT_HP = 10;
	
	public Infantry() {
		super(INFANTRY_DEFAULT_HP);
		this.name = "INF";
	}
	
	public Infantry(int hp) {
		super(hp);
		this.name = "INF";
	}

}
