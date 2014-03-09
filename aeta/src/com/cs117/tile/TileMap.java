package com.cs117.tile;

import java.util.ArrayList;
import java.util.concurrent.ConcurrentHashMap;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL10;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.cs117.aeta.Game;
import com.cs117.connection.ActionResolver;
import com.cs117.units.Infantry;
import com.cs117.units.Mech;
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
	private SpriteBatch spriteBatch2;
	
	private Coordinate selectedTile;
	private ConcurrentHashMap<Coordinate, Unit> unitMap;
    private ArrayList<Coordinate> walkable;
    private ArrayList<Coordinate> attackable;
    
    private BitmapFont font;
    private BitmapFont hpFont;
    
    private Texture grassTexture;
    private Texture blueOverlay;
    private Texture redOverlay;
    private Texture mtnTexture;
    private Texture blue_inf_right;
    private Texture blue_tank_right;
    private Texture red_inf_right;
    private Texture red_tank_right;
   // private Texture blue_mech_right;
    //private Texture red_mech_right;
    private Texture red_mech_right;
    private Texture red_mech_left;
    private Texture red_mech_front;
    private Texture red_mech_back;
    private Texture blue_mech_right;
    private Texture blue_mech_left;
    private Texture blue_mech_front;
    private Texture blue_mech_back;
    
    
    private ActionResolver AR;
	
    // Animation variables
    private mech_tank_death mechDeath;
    private soldier_death infDeath;
	public static long timer;
	public static boolean timerOn;
	private Coordinate currAtkCoord;
	private Texture tankDeathTexture1;
	private Texture tankDeathTexture2;
	private Texture tankDeathTexture3;
	private Texture tankDeathTexture4;
	private Texture tankDeathTexture5;
	private Texture tankDeathTexture6;
    	

	public TileMap(ShapeRenderer shapeRenderer, SpriteBatch spriteBatch, BitmapFont font, BitmapFont hpFont,
					ActionResolver ar) {
		
		AR = ar;
		
		terrain = new int[][] { 
				{0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
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
		//blue_mech_right = new Texture(Gdx.files.internal("gfx/blue_mech_right.png"));
		red_inf_right = new Texture(Gdx.files.internal("gfx/red_inf_right.png"));
		red_tank_right = new Texture(Gdx.files.internal("gfx/red_tank_right.png"));
		//red_mech_right = new Texture(Gdx.files.internal("gfx/red_mech_right.png"));
		red_mech_right = new Texture(Gdx.files.internal("gfx/eliteRight2.png"));
		red_mech_left = new Texture(Gdx.files.internal("gfx/eliteLeft2.png"));
		red_mech_front = new Texture(Gdx.files.internal("gfx/eliteFront2.png"));
		red_mech_back = new Texture(Gdx.files.internal("gfx/eliteBack2.png"));
		blue_mech_right = new Texture(Gdx.files.internal("gfx/eliteRight.png"));
		blue_mech_left = new Texture(Gdx.files.internal("gfx/eliteLeft.png"));
		blue_mech_front = new Texture(Gdx.files.internal("gfx/eliteFront.png"));
		blue_mech_back = new Texture(Gdx.files.internal("gfx/eliteBack.png"));

		tankDeathTexture1 = new Texture(Gdx.files.internal("gfx/tankdeath1.png"));
		tankDeathTexture2 = new Texture(Gdx.files.internal("gfx/tankdeath2.png"));
		tankDeathTexture3 = new Texture(Gdx.files.internal("gfx/tankdeath3.png"));
		tankDeathTexture4 = new Texture(Gdx.files.internal("gfx/tankdeath4.png"));
		tankDeathTexture5 = new Texture(Gdx.files.internal("gfx/tankdeath5.png"));
		tankDeathTexture6 = new Texture(Gdx.files.internal("gfx/tankdeath6.png"));

		mechDeath = new mech_tank_death();
		infDeath = new soldier_death();
		
		selectedTile = new Coordinate(-1, -1);
		
		unitMap = new ConcurrentHashMap<Coordinate, Unit>();
		// put an infantry unit at (0, 0)
		unitMap.put(new Coordinate(0, 0), new Infantry(RED_TEAM));
		// put a tank at (5, 1)
		unitMap.put(new Coordinate(5, 1), new Tank(RED_TEAM));
		unitMap.put(new Coordinate(1, 3), new Tank(BLUE_TEAM));
		unitMap.put(new Coordinate(5, 5), new Infantry(BLUE_TEAM));
		unitMap.put(new Coordinate(6, 2), new Mech(RED_TEAM));
		unitMap.put(new Coordinate(2, 6), new Mech(BLUE_TEAM));
		
		walkable = null;
		attackable = null;
		this.font = font;
		this.hpFont = hpFont;
		TileMap.timerOn = false;
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
			
			String hpString = String.valueOf(curUnit.getHp());
			hpFont.draw(spriteBatch, hpString, 
						(c.getX()+1) * Game.BLOCK_WIDTH - hpFont.getSpaceWidth()*3,
						c.getY() * Game.BLOCK_HEIGHT + hpFont.getLineHeight());
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
		} else if (curUnit.getName().equals("MECH")){
			if(curUnit.getTeam() == RED_TEAM)
				return red_mech_right;
			else
				return blue_mech_right;
		}
		else return null;
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
			int r = Math.abs(Math.abs(i-x)-atkRange);
			for (int j = y - r; j <= y + r; j++) {
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
					if(attacked.getHp() <= 0) {
						if (attacked.getName() == "MECH" || attacked.getName() == "TANK")
						{  
							System.out.println("Loading mech/tank attack animation");
							mechDeath.create(); 
							mechDeath.renderAt(xCoord,yCoord);
						}
						
						else if (attacked.getName() == "INF")
						{
							System.out.println("Loading infantry attack animation");
							infDeath.create();
							infDeath.renderAt(xCoord,yCoord);
						}
						unitMap.remove(c);
					}
			
					atkSynch(c.getX(), c.getY(), attacked.getHp());
					
					currAtkCoord = new Coordinate(xCoord, yCoord);
					TileMap.timer = System.currentTimeMillis();
					TileMap.timerOn = true;
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
	
	// Deprecated Function
	public void updateUnit(int atkedX, int atkedY, int newHP)
	{
		System.out.printf("Updating units at %d %d\n", atkedX, atkedY);
		Coordinate atkedC = new Coordinate(atkedX, atkedY);
		Unit atked = unitMap.get(atkedC);
		if(newHP <= 0)
		{	
			if (atked.getName() == "MECH" || atked.getName() == "TANK")
			{  
				
				mechDeath.create(); 
				mechDeath.renderAt(atkedX, atkedY);
			}
			
			else if (atked.getName() == "INF")
			{
				infDeath.create();
				infDeath.renderAt(atkedX,atkedY);
			}		
			unitMap.remove(atkedC);
		}
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
			int r = Math.abs(Math.abs(i-x)-moveRnge);
			for (int j = y - r; j <= y + r; j++) {
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
	
	
	
	public ConcurrentHashMap<Coordinate, Unit> getUnitMap() {
		return unitMap;
	}
	
	public Coordinate getSelectedTile() {
		return selectedTile;
	}
	
	public void drawExplosionTexture(long curTimeDelta) {
		System.out.println("Current Delta: " + curTimeDelta);
		Texture explosionTexture = null;
		if(curTimeDelta >= 0)
			explosionTexture = tankDeathTexture1;
		// NOTE: This part isn't working currently
		/*
		else if(curTimeDelta >= 200)
			explosionTexture = tankDeathTexture1;
		else if(curTimeDelta >= 400)
			explosionTexture = tankDeathTexture1;
		else if(curTimeDelta >= 600)
			explosionTexture = tankDeathTexture3;
		else if(curTimeDelta >= 800)
			explosionTexture = tankDeathTexture2;
		else if(curTimeDelta >= 1000)
			explosionTexture = tankDeathTexture1;
		*/
		else
			TileMap.timerOn = false;
		if(explosionTexture != null) {
			spriteBatch.draw(explosionTexture, currAtkCoord.getX() * Game.BLOCK_WIDTH + Game.TILE_OFFSET, 
			 currAtkCoord.getY() * Game.BLOCK_HEIGHT + Game.TILE_OFFSET, 
			 Game.BLOCK_WIDTH - Game.TILE_OFFSET, 
			 Game.BLOCK_HEIGHT - Game.TILE_OFFSET);
		}
	}

	
	

}
