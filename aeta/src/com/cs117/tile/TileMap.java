package com.cs117.tile;

import java.util.ArrayList;
import java.util.Hashtable;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
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
	private static final int IMMEDIATE_ATTACKABLE = 24;
	
	public static final int RED_TEAM  = 0;
	public static final int BLUE_TEAM = 1; 
	
	private int[][] terrain = null;
	private ShapeRenderer shapeRenderer;
	private SpriteBatch spriteBatch;
	
	private Coordinate selectedTile;
	private Hashtable<Coordinate, Unit> unitMap;
    private ArrayList<Coordinate> walkable;
    private ArrayList<Coordinate> attackable;
    
    private BitmapFont font;
    private Texture grassTexture;
    private Texture blueOverlay;
    private Texture redOverlay;
    private Texture mtnTexture;
    private Texture blue_inf_right;
    private Texture blue_tank_right;
    private Texture red_inf_right;
    private Texture red_tank_right;
    
    private ActionResolver AR;
    	

	public TileMap(ShapeRenderer shapeRenderer, SpriteBatch spriteBatch, BitmapFont font,
					ActionResolver ar) {
		
		AR = ar;
		
		terrain = new int[][] { 
				{0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
				{0, 1, 1, 1, 1, 1, 0, 0, 0, 0},
				{0, 0, 1, 1, 1, 1, 0, 1, 0, 0},
				{1, 0, 0, 1, 1, 1, 1, 1, 0, 0},
				{1, 1, 1, 1, 1, 1, 1, 1, 0, 0},
				{0, 1, 1, 1, 1, 1, 1, 1, 1, 0},
				{0, 1, 1, 0, 0, 1, 1, 1, 1, 0},
				{0, 0, 0, 0, 0, 0, 0, 0, 0, 0}
		};

		this.shapeRenderer = shapeRenderer;
		this.spriteBatch = spriteBatch;
		
		grassTexture = new Texture(Gdx.files.internal("gfx/grass.png"));
		blueOverlay = new Texture(Gdx.files.internal("gfx/blueOverlay.png"));
		redOverlay = new Texture(Gdx.files.internal("gfx/redOverlay.png"));
		mtnTexture = new Texture(Gdx.files.internal("gfx/mtn.png"));
		blue_inf_right = new Texture(Gdx.files.internal("gfx/blue_inf_right.png"));
		blue_tank_right = new Texture(Gdx.files.internal("gfx/blue_tank_right.png"));
		red_inf_right = new Texture(Gdx.files.internal("gfx/red_inf_right.png"));
		red_tank_right = new Texture(Gdx.files.internal("gfx/red_tank_right.png"));
		
		selectedTile = new Coordinate(-1, -1);
		
		unitMap = new Hashtable<Coordinate, Unit>();
		// put an infantry unit at (0, 0)
		unitMap.put(new Coordinate(0, 0), new Infantry(RED_TEAM));
		// put a tank at (5, 1)
		unitMap.put(new Coordinate(5, 1), new Tank(RED_TEAM));
		unitMap.put(new Coordinate(1, 3), new Tank(BLUE_TEAM));
		unitMap.put(new Coordinate(5, 5), new Infantry(BLUE_TEAM));
		
		walkable = null;
		attackable = null;
		this.font = font;
	}
	
	/** === DRAW FUNCTIONS === **/
	public void drawTerrainTexture() {
		Texture curTexture = null;
		for (int i = terrain.length - 1; i > -1; i--) {
			for (int j = 0; j < terrain[0].length; j++) {
				if (terrain[i][j] == 1) 
					curTexture = grassTexture;
				else if (terrain[i][j] == 0) 
					curTexture = mtnTexture;
						
				spriteBatch.draw(curTexture, j * Game.BLOCK_WIDTH + Game.TILE_OFFSET,  
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
	
	public void __DEBUG_drawUnitString() {
		for (Coordinate c : unitMap.keySet()) {
			Unit curUnit = unitMap.get(c);
			font.draw(spriteBatch, 
					 curUnit.getName(), 
					 c.getX() * Game.BLOCK_WIDTH + Game.UNIT_TEXT_X_OFFSET, 
					 (c.getY() + 1) * Game.BLOCK_HEIGHT - Game.UNIT_TEXT_Y_OFFSET);
		}
	}
	
	public void drawUnits() {
		Texture curTexture = null;
		for (Coordinate c : unitMap.keySet()) {
			Unit curUnit = unitMap.get(c);
			curTexture = resolveTexture(curUnit);
			
			spriteBatch.draw(curTexture, c.getX() * Game.BLOCK_WIDTH + Game.TILE_OFFSET, 
					 c.getY() * Game.BLOCK_HEIGHT + Game.TILE_OFFSET, 
					 Game.BLOCK_WIDTH - Game.TILE_OFFSET, 
					 Game.BLOCK_HEIGHT - Game.TILE_OFFSET);
		}
	}
	
	private Texture resolveTexture(Unit curUnit) {
		if (curUnit.getName().equals("INF")) {
			if (curUnit.getTeam() == RED_TEAM)
				return red_inf_right;
			else
				return blue_inf_right;
		} else if (curUnit.getName().equals("TANK")) {
			if (curUnit.getTeam() == RED_TEAM)
				return red_tank_right;
			else 
				return blue_tank_right;
		} else return null;
	}

	public void updateSelectedTile(int xCoord, int yCoord) {
		// only toggle if selected coordinates are different from previous
		selectedTile.setX(xCoord);
		selectedTile.setY(yCoord);
	}
	
	/** === ATTACK FUNCTIONS === **/
	public void drawAttackable() {
		if (attackable != null) {
			for (Coordinate c : attackable) {
				spriteBatch.draw(redOverlay, c.getX() * Game.BLOCK_WIDTH + Game.TILE_OFFSET, 
								 c.getY() * Game.BLOCK_HEIGHT + Game.TILE_OFFSET, 
								 Game.BLOCK_WIDTH - Game.TILE_OFFSET, 
								 Game.BLOCK_HEIGHT - Game.TILE_OFFSET);
			}
		}
	}
	
	/*
	 * Returns all nearby terrain that is attackable. 
	 */
	public void getAttackableTerrain() {
		attackable = new ArrayList<Coordinate>(IMMEDIATE_ATTACKABLE);
		Unit atkUnit = unitMap.get(selectedTile);
		int atkRange = atkUnit.getAtkRange();
		
		int x = selectedTile.getX();
		int y = terrain.length - selectedTile.getY() - 1;
		
		for (int i = x - atkRange; i <= x + atkRange; i++) {
			for (int j = y - atkRange; j <= y + atkRange; j++) {
				// skip over self
				if (i == x && j == y) continue;
				// make sure we don't go out of bounds
				if (i >= 0 && i < terrain[0].length && j >= 0 && j < terrain.length){ 
						attackable.add(new Coordinate(i, terrain.length - j - 1));
				}
			}
		}
	}
	
	public void attackWithSelectedUnit(int xCoord, int yCoord, int prevX, int prevY) {
		if (attackable != null) {
			for (Coordinate c : attackable) {
				if (c.equals(selectedTile) && unitMap.containsKey(c)) {	
					Unit attacked = unitMap.get(c);
					Coordinate currUnit = new Coordinate(prevX, prevY);
					Unit attacking = unitMap.get(currUnit);
					
					attacked.getAttacked(attacking);
					if(attacked.getHp() <= 0)
						unitMap.remove(c);
					
					atkSynch(c.getX(), c.getY(), attacked.getHp());
					
					attackable = null;
					break;
				}
			}
			attackable = null;
		}
	}
	
	public void atkSynch(int atkedX, int atkedY, int newHP)
	{
		AR.sendAtkRes(atkedX, atkedY, newHP);
	}
	
	public void updateUnit(int atkedX, int atkedY, int newHP)
	{
		Coordinate atkedC = new Coordinate(atkedX, atkedY);
		Unit atked = unitMap.get(atkedC);
		if(newHP <= 0)
			unitMap.remove(atkedC);
		else
			atked.setHp(newHP);
	}
	
	/** === MOVEMENT FUNCTIONS === **/
	public void drawWalkable() {
		if (walkable != null) {
			for (Coordinate c : walkable) {
				spriteBatch.draw(blueOverlay, c.getX() * Game.BLOCK_WIDTH + Game.TILE_OFFSET,
								   c.getY() * Game.BLOCK_HEIGHT + Game.TILE_OFFSET,
								   Game.BLOCK_WIDTH - Game.TILE_OFFSET,
								   Game.BLOCK_HEIGHT - Game.TILE_OFFSET);
			}
		}
	}
	
	/*
	 * Returns all nearby terrain that is walkable. 
	 * Used for calculating what nearby squares a unit can go to. 
	 */
	public void getWalkableTerrain() {
		walkable = new ArrayList<Coordinate>(IMMEDIATE_WALKABLE);
		Unit selUnit = unitMap.get(selectedTile);
		int moveRnge = selUnit.getMoveRange();
		int x = selectedTile.getX();
		// transform coords to tile coords, which is flipped about the x axis (y invert)
		int y = terrain.length - selectedTile.getY() - 1;
		
		Coordinate unitCoord = new Coordinate(-1, -1);
		// look at all 8 adjacent squares
		for (int i = x - moveRnge; i <= x + moveRnge; i++) {
			for (int j = y - moveRnge; j <= y + moveRnge; j++) {
				// skip over self
				if (i == x && j == y) continue;
				// make sure we don't go out of bounds
				if (i >= 0 && i < terrain[0].length && j >= 0 && j < terrain.length) {
					unitCoord.setX(i);
					unitCoord.setY(terrain.length - j - 1);
					// 1 is passable, 0 impassable terrain for now
					// also check to see if a unit isn't there -- stacking not allowed
					if ((terrain[j][i] == 1 && !unitMap.containsKey(unitCoord)) || 
						(terrain[j][i] == 0 && selUnit.isMntnClimber() && !unitMap.containsKey(unitCoord))) {
						// transform coordinate back
						walkable.add(new Coordinate(i, terrain.length - j - 1));		
					}
				}
				
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
					moveSynchronize(xCoord, yCoord, prevX, prevY);
					walkable = null;
					break;
				}
			}
			// no tile selected, therefore effectively a cancel
			walkable = null;	
		}
	}
	
	public void moveSynchronize(int xCoord, int yCoord, int prevX, int prevY) {
		AR.sendCoordinates(prevX, prevY, xCoord, yCoord);
	}
	
	public void updateUnit(int xCoord, int yCoord, int prevX, int prevY) {
		Coordinate prevCoord = new Coordinate(prevX, prevY);
		Unit curUnit = unitMap.get(prevCoord);
		unitMap.remove(prevCoord);
		unitMap.put(new Coordinate(xCoord, yCoord), curUnit);
	}
	
	
	
	public Hashtable<Coordinate, Unit> getUnitMap() {
		return unitMap;
	}
	
	public Coordinate getSelectedTile() {
		return selectedTile;
	}
	
	
	
	

}
