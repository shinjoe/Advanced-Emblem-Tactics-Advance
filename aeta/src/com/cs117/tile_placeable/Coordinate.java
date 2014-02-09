package com.cs117.tile_placeable;

import com.cs117.aeta.Game;

public class Coordinate {
	private int x;
	private int y;
	
	public Coordinate(int x, int y) {
		this.x = x;
		this.y = y;
	}
	
	public void setX(int x) {
		this.x = x;
	}
	
	public void setY(int y) {
		this.y = y;
	}
	
	public int getX() {
		return x;
	}
	
	public int getY() {
		return y;
	}
	
	/*
	 * Returns the quadrant that the given coordinate is in. 
	 * Order : 
	 * ---------------+----------------+
	 * |              |                |
	 * |     II       |        I       |
	 * |              |                |
	 * ---------------+----------------+
	 * |              |                |
	 * |     III      |       IV       |
	 * |              |                |
	 * ---------------+----------------+
	 */
	public int getQuadrant() {
		if (x >= Game.NUM_COLS/2 && y >= Game.NUM_ROWS/2)     return 1;
		else if (x < Game.NUM_COLS/2 && y >= Game.NUM_ROWS/2) return 2;
		else if (x < Game.NUM_COLS/2 && y < Game.NUM_ROWS/2)  return 3;
		else if (x >= Game.NUM_COLS/2 && y < Game.NUM_ROWS/2) return 4;
		else {
			System.err.println("Bad coordinate. Failed to map to quadrant.");
			return -1;
		}
	}
	
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;
		
		Coordinate c = (Coordinate) o;
		return this.x == c.x && this.y == c.y;
	}
	
	public int hashCode() {
		int hash = 7;
		hash = 71 * hash + this.x;
	    hash = 71 * hash + this.y;
	    return hash;
	}
	
	public String toString() {
		return "[X : " + x + " | Y : " + y + "]";
				
	}
}
