package com.cs117.tile_placeable;

import java.util.ArrayList;
import java.util.HashMap;

public class Unit {
	private int hp;
	private UNIT_TYPE type;
	private static final int IMMEDIATE_WALKABLE = 8;
	
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
	
	/*
	 * Returns all nearby terrain that is walkable. 
	 * Used for calculating what nearby squares a unit can go to. 
	 * TODO: REFACTOR, place into Tilemap class. 
	 */
	public static ArrayList<Coordinate> getWalkableTerrain(int[][] tileMap, Coordinate c, HashMap<Coordinate, Unit> unitMap) {
		ArrayList<Coordinate> walkable = new ArrayList<Coordinate>(IMMEDIATE_WALKABLE);
	
		int x = c.getX();
		// transform coords to tile coords, which is flipped about the x axis (y invert)
		int y = tileMap.length - c.getY() - 1;
		
		Coordinate unitCoord = new Coordinate(-1, -1);
		// look at all 8 adjacent squares
		for (int i = x - 1; i <= x + 1; i++) {
			for (int j = y - 1; j <= y + 1; j++) {
				// skip over self
				if (i == x && j == y) continue;
				// make sure we don't go out of bounds
				if (i > 0 && i < tileMap[0].length && j > 0 && j < tileMap.length) {
					unitCoord.setX(i);
					unitCoord.setY(tileMap.length - j - 1);
					// 1 is passable, 0 impassable terrain for now
					// also check to see if a unit isn't there -- stacking not allowed
					if (tileMap[j][i] == 1 && !unitMap.containsKey(unitCoord)) {
						// transform coordinate back
						walkable.add(new Coordinate(i, tileMap.length - j - 1));		
					}
				}
				
			}
		}
		
		return walkable;
	}
	
}
