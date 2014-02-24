package com.cs117.tile;

import java.util.ArrayList;
import java.util.HashMap;


import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.cs117.aeta.Game;
import com.cs117.connection.ActionResolver;
import com.cs117.units.Infantry;
import com.cs117.units.Tank;
import com.cs117.units.Unit;


public class TileMap {
	private static final int IMMEDIATE_WALKABLE = 8;
	
	private int[][] terrain = null;
	private ShapeRenderer shapeRenderer;
	private SpriteBatch spriteBatch;
	private Coordinate selectedTile;
	private HashMap<Coordinate, Unit> unitMap;
    private ArrayList<Coordinate> walkable;
    private BitmapFont font;
    
    private ActionResolver AR;
    	

	public TileMap(ShapeRenderer shapeRenderer, SpriteBatch spriteBatch, BitmapFont font,
					ActionResolver ar) {
		
		AR = ar;
		
		terrain = new int[][] { 
				{0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
				{0, 1, 1, 1, 1, 1, 0, 0, 0, 0},
				{0, 0, 1, 1, 1, 1, 0, 1, 0, 0},
				{0, 0, 0, 1, 1, 1, 1, 1, 0, 0},
				{0, 1, 1, 1, 1, 1, 1, 1, 0, 0},
				{0, 1, 1, 1, 1, 1, 1, 1, 1, 0},
				{0, 1, 1, 0, 0, 1, 1, 1, 1, 0},
				{0, 0, 0, 0, 0, 0, 0, 0, 0, 0}
		};

		this.shapeRenderer = shapeRenderer;
		this.spriteBatch = spriteBatch;
		selectedTile = new Coordinate(-1, -1);
		
		unitMap = new HashMap<Coordinate, Unit>();
		// put an infantry unit at (0, 0)
		unitMap.put(new Coordinate(0, 0), new Infantry(10));
		// put a tank at (5, 1)
		unitMap.put(new Coordinate(5, 1), new Tank(10));
		unitMap.put(new Coordinate(0, 7), new Tank(10));
		unitMap.put(new Coordinate(9, 7), new Infantry(10));
		unitMap.put(new Coordinate(9, 0), new Infantry(10));
		
		walkable = null;
		this.font = font;
	}

	public void drawTerrain() {
		for (int i = terrain.length - 1; i > -1; i--) {
			for (int j = 0; j < terrain[0].length; j++) {
				Color curColor = Color.BLACK;
				if (terrain[i][j] == 1)
					curColor = Color.WHITE;
				else if (terrain[i][j] == 2)
					curColor = Color.GRAY;
				shapeRenderer.setColor(curColor);
				shapeRenderer.rect(j * Game.BLOCK_WIDTH + Game.TILE_OFFSET,  
						           Game.BLOCK_HEIGHT * (terrain.length - 1) - i * Game.BLOCK_HEIGHT + Game.TILE_OFFSET, 
						           Game.BLOCK_WIDTH  - Game.TILE_OFFSET, 
						           Game.BLOCK_HEIGHT - Game.TILE_OFFSET);
			}
		}
	}

	public void drawSelectedTile() {
		if (selectedTile.getX() != -1) {
			shapeRenderer.setColor(Color.RED);
			shapeRenderer.rect(selectedTile.getX() * Game.BLOCK_WIDTH, 
					           selectedTile.getY() * Game.BLOCK_HEIGHT, 
					           Game.BLOCK_WIDTH  + Game.TILE_OFFSET, 
					           Game.BLOCK_HEIGHT + Game.TILE_OFFSET );
		}
	}

	public void updateSelectedTile(int xCoord, int yCoord) {
		// only toggle if selected coordinates are different from previous
		selectedTile.setX(xCoord);
		selectedTile.setY(yCoord);
	}
	
	public void drawUnits() {
		for (Coordinate c : unitMap.keySet()) {
			shapeRenderer.setColor(Color.CYAN);
			shapeRenderer.rect(c.getX() * Game.BLOCK_WIDTH + Game.TILE_OFFSET, 
					           c.getY() * Game.BLOCK_HEIGHT + Game.TILE_OFFSET, 
					           Game.BLOCK_WIDTH - Game.TILE_OFFSET, 
					           Game.BLOCK_HEIGHT - Game.TILE_OFFSET);
		}
	}
	
	public void drawWalkable() {
		if (walkable != null) {
			for (Coordinate c : walkable) {
				shapeRenderer.setColor(Color.BLUE);
				shapeRenderer.rect(c.getX() * Game.BLOCK_WIDTH + Game.TILE_OFFSET,
								   c.getY() * Game.BLOCK_HEIGHT + Game.TILE_OFFSET,
								   Game.BLOCK_WIDTH - Game.TILE_OFFSET,
								   Game.BLOCK_HEIGHT - Game.TILE_OFFSET);
			}
		}
	}
	
	public void moveSelectedUnit(int xCoord, int yCoord, int prevX, int prevY) {
		// move unit if blue tile selected
		if (walkable != null) {
			for (Coordinate c : walkable) {
				if (c.equals(selectedTile)) {
					updateUnit(xCoord, yCoord, prevX, prevY);
					System.out.println("removing : ");
					System.out.println(c);
					// sync with other phone for now
					synchronize(xCoord, yCoord, prevX, prevY);
					walkable = null;
					break;
				}
			}
			
			// no tile selected, therefore effectively a cancel
			walkable = null;
			
		}
	}
	
	public void synchronize(int xCoord, int yCoord, int prevX, int prevY){
		AR.sendCoordinates(prevX, prevY, xCoord, yCoord);
	}
	
	public void updateUnit(int xCoord, int yCoord, int prevX, int prevY){
		Coordinate prevCoord = new Coordinate(prevX, prevY);
		Unit curUnit = unitMap.get(prevCoord);
		unitMap.remove(prevCoord);
		unitMap.put(new Coordinate(xCoord, yCoord), curUnit);
	}
	
	public void __DEBUG_drawUnitString() {
		for (Coordinate c : unitMap.keySet()) {
			Unit curUnit = unitMap.get(c);
			font.draw(spriteBatch, 
					 curUnit.getName(), 
					 c.getX() * Game.BLOCK_WIDTH + Game.UNIT_TEXT_X_OFFSET, 
					 (c.getY() + 1) * Game.BLOCK_HEIGHT - Game.UNIT_TEXT_Y_OFFSET);
		}
	}
	
	public HashMap<Coordinate, Unit> getUnitMap() {
		return unitMap;
	}
	
	public Coordinate getSelectedTile() {
		return selectedTile;
	}
	
	/*
	 * Returns all nearby terrain that is walkable. 
	 * Used for calculating what nearby squares a unit can go to. 
	 */
	public void getWalkableTerrain() {
		walkable = new ArrayList<Coordinate>(IMMEDIATE_WALKABLE);
	
		int x = selectedTile.getX();
		// transform coords to tile coords, which is flipped about the x axis (y invert)
		int y = terrain.length - selectedTile.getY() - 1;
		
		Coordinate unitCoord = new Coordinate(-1, -1);
		// look at all 8 adjacent squares
		for (int i = x - 1; i <= x + 1; i++) {
			for (int j = y - 1; j <= y + 1; j++) {
				// skip over self
				if (i == x && j == y) continue;
				// make sure we don't go out of bounds
				if (i > 0 && i < terrain[0].length && j > 0 && j < terrain.length) {
					unitCoord.setX(i);
					unitCoord.setY(terrain.length - j - 1);
					// 1 is passable, 0 impassable terrain for now
					// also check to see if a unit isn't there -- stacking not allowed
					if (terrain[j][i] == 1 && !unitMap.containsKey(unitCoord)) {
						// transform coordinate back
						walkable.add(new Coordinate(i, terrain.length - j - 1));		
					}
				}
				
			}
		}
	
	}

}
