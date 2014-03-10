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
    private Texture red_mech_right;
    private Texture red_mech_left;
    private Texture blue_mech_right;
    private Texture blue_mech_left;
    
    private ActionResolver AR;
	
    // Animation variables
    private mech_tank_death mechDeath;
    private soldier_death infDeath;
	public static long timer;
	public static boolean timerOn;
	private Coordinate currAtkCoord;
	
	//death animation frame; tank and mechanic units have the same death animation
	private Texture tankDeathTexture1;
	private Texture tankDeathTexture2;
	private Texture tankDeathTexture3;
	private Texture tankDeathTexture4;
	private Texture tankDeathTexture5;
	private Texture tankDeathTexture6;
	
	private Texture infDeathTexture1;
	private Texture infDeathTexture2;
	private Texture infDeathTexture3;
	private Texture infDeathTexture4;
	private Texture infDeathTexture5;
	private Texture infDeathTexture6;
    	

	//attack animation frame for infantry/mech/tank
	private Texture infAttackTexture1;
	private Texture infAttackTexture2;
	private Texture infAttackTexture3;
	private Texture infAttackTexture4;
	private Texture infAttackTexture5;
	private Texture infAttackTexture6;
	
	private Texture tankAttackTexture1;
	private Texture tankAttackTexture2;
	private Texture tankAttackTexture3;
	private Texture tankAttackTexture4;
	private Texture tankAttackTexture5;
	private Texture tankAttackTexture6;
	
	private Texture mechAttackTexture1;
	private Texture mechAttackTexture2;
	private Texture mechAttackTexture3;
	private Texture mechAttackTexture4;
	private Texture mechAttackTexture5;
	private Texture mechAttackTexture6;
	
	
	
	
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
		blue_mech_right = new Texture(Gdx.files.internal("gfx/eliteRight.png"));
		blue_mech_left = new Texture(Gdx.files.internal("gfx/eliteLeft.png"));

		tankDeathTexture1 = new Texture(Gdx.files.internal("gfx/tankdeath1.png"));
		tankDeathTexture2 = new Texture(Gdx.files.internal("gfx/tankdeath2.png"));
		tankDeathTexture3 = new Texture(Gdx.files.internal("gfx/tankdeath3.png"));
		tankDeathTexture4 = new Texture(Gdx.files.internal("gfx/tankdeath4.png"));
		tankDeathTexture5 = new Texture(Gdx.files.internal("gfx/tankdeath5.png"));
		tankDeathTexture6 = new Texture(Gdx.files.internal("gfx/tankdeath6.png"));
		
		infDeathTexture1 = new Texture(Gdx.files.internal("gfx/infdeath1.png"));
		infDeathTexture2 = new Texture(Gdx.files.internal("gfx/infdeath2.png"));
		infDeathTexture3 = new Texture(Gdx.files.internal("gfx/infdeath3.png"));
		infDeathTexture4 = new Texture(Gdx.files.internal("gfx/infdeath4.png"));
		infDeathTexture5 = new Texture(Gdx.files.internal("gfx/infdeath5.png"));
		infDeathTexture6 = new Texture(Gdx.files.internal("gfx/infdeath6.png"));
		
		infAttackTexture1 = new Texture(Gdx.files.internal("gfx/infAttack1.png"));
		infAttackTexture2 = new Texture(Gdx.files.internal("gfx/infAttack2.png"));
		infAttackTexture3 = new Texture(Gdx.files.internal("gfx/infAttack3.png"));
		infAttackTexture4 = new Texture(Gdx.files.internal("gfx/infAttack4.png"));
		infAttackTexture5 = new Texture(Gdx.files.internal("gfx/infAttack5.png"));
		infAttackTexture6 = new Texture(Gdx.files.internal("gfx/infAttack6.png"));
		
		tankAttackTexture1 = new Texture(Gdx.files.internal("gfx/tankAttack1.png"));
		tankAttackTexture2 = new Texture(Gdx.files.internal("gfx/tankAttack2.png"));
		tankAttackTexture3 = new Texture(Gdx.files.internal("gfx/tankAttack3.png"));
		tankAttackTexture4 = new Texture(Gdx.files.internal("gfx/tankAttack4.png"));
		tankAttackTexture5 = new Texture(Gdx.files.internal("gfx/tankAttack5.png"));
		tankAttackTexture6 = new Texture(Gdx.files.internal("gfx/tankAttack6.png"));
		
		mechAttackTexture1 = new Texture(Gdx.files.internal("gfx/mechAttack1.png"));
		mechAttackTexture2 = new Texture(Gdx.files.internal("gfx/mechAttack2.png"));
		mechAttackTexture3 = new Texture(Gdx.files.internal("gfx/mechAttack3.png"));
		mechAttackTexture4 = new Texture(Gdx.files.internal("gfx/mechAttack4.png"));
		mechAttackTexture5 = new Texture(Gdx.files.internal("gfx/mechAttack5.png"));
		mechAttackTexture6 = new Texture(Gdx.files.internal("gfx/mechAttack6.png"));
				
		
		mechDeath = new mech_tank_death();
		infDeath = new soldier_death();
		
		selectedTile = new Coordinate(-1, -1);
		
		unitMap = new ConcurrentHashMap<Coordinate, Unit>();
		// put an infantry unit at (0, 0)
		unitMap.put(new Coordinate(0, 6), new Infantry(RED_TEAM));
		// put a tank at (5, 1)
		unitMap.put(new Coordinate(5, 1), new Tank(RED_TEAM));
		unitMap.put(new Coordinate(1, 3), new Tank(BLUE_TEAM));
		unitMap.put(new Coordinate(5, 5), new Infantry(BLUE_TEAM));
		unitMap.put(new Coordinate(6, 2), new Mech(RED_TEAM));
		unitMap.put(new Coordinate(7, 3), new Mech(BLUE_TEAM));
	//	unitMap.put(new Coordinate(9, 3), new Tank(RED_TEAM));
	//	unitMap.put(new Coordinate(5, 8), new Infantry(RED_TEAM));
		
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
							System.out.println("Loading mech/tank death animation");
							Game.mechDeath = true;
							Game.infDeath = false;
							Game.infAttack = false;
							Game.mechAttack = false;
							Game.tankAttack = false;
							//mechDeath.create(); 
							//mechDeath.renderAt(xCoord,yCoord);
						}
						
						else if (attacked.getName() == "INF")
						{
							System.out.println("Loading infantry death animation");
							Game.infDeath = true;
							Game.mechDeath = false;
							Game.infAttack = false;
							Game.mechAttack = false;
							Game.tankAttack = false;
							//infDeath.create();
							//infDeath.renderAt(xCoord,yCoord);
						}
						unitMap.remove(c);
						TileMap.timer = System.currentTimeMillis();
						TileMap.timerOn = true;
					}
			
					else
					{
						if (attacking.getName() == "MECH")
						{
							System.out.println("Loading mech attack animation");
							Game.mechDeath = false;
							Game.infDeath = false;
							Game.infAttack = false;
							Game.mechAttack = true;
							Game.tankAttack = false;				
							
						}
						
						else if (attacking.getName() == "TANK")
						{
							System.out.println("Loading tank attack animation");
							Game.mechDeath = false;
							Game.infDeath = false;
							Game.infAttack = false;
							Game.mechAttack = false;
							Game.tankAttack = true;					
							
						}
						
						else if (attacking.getName() == "INF")
						{
							System.out.println("Loading inf attack animation");
							Game.mechDeath = false;
							Game.infDeath = false;
							Game.infAttack = true;
							Game.mechAttack = false;
							Game.tankAttack = false;					
							
						}
						TileMap.timer = System.currentTimeMillis();
						TileMap.timerOn = true;
						
						
					}
					
					atkSynch(c.getX(), c.getY(), attacked.getHp());
					
					currAtkCoord = new Coordinate(xCoord, yCoord);
					//TileMap.timer = System.currentTimeMillis();
					//TileMap.timerOn = true;
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
		if(curTimeDelta >= 0 && curTimeDelta <= 31)
		{
			if (Game.mechDeath == true)
			{
				System.out.println("tank death animation deployed");
				explosionTexture = tankDeathTexture1;
			}
			else if (Game.infDeath == true)
			{
				System.out.println("infantry death animation deployed");
				explosionTexture = infDeathTexture1;
			}
			
			else if (Game.infAttack == true)
			{
				System.out.println("infantry death animation deployed");
				explosionTexture = infAttackTexture1;
			}
			
			else if (Game.mechAttack == true)
			{
				System.out.println("infantry death animation deployed");
				explosionTexture = mechAttackTexture1;
			}
			
			else if (Game.tankAttack == true)
			{
				System.out.println("infantry death animation deployed");
				explosionTexture = tankAttackTexture1;
			}
		}
		else if(curTimeDelta >= 31 && curTimeDelta <= 65)
		{
			if (Game.mechDeath == true)
			{
				System.out.println("tank death animation deployed");
				explosionTexture = tankDeathTexture2;
			}
			else if (Game.infDeath == true)
			{
				System.out.println("infantry death animation deployed");
				explosionTexture = infDeathTexture2;
			}
			
			else if (Game.infAttack == true)
			{
				System.out.println("infantry death animation deployed");
				explosionTexture = infAttackTexture2;
			}
			
			else if (Game.mechAttack == true)
			{
				System.out.println("infantry death animation deployed");
				explosionTexture = mechAttackTexture2;
			}
			
			else if (Game.tankAttack == true)
			{
				System.out.println("infantry death animation deployed");
				explosionTexture = tankAttackTexture2;
			}
			
		}
		else if(curTimeDelta >= 65 && curTimeDelta <= 97)
		{	
			if (Game.mechDeath == true)
			{
				System.out.println("tank death animation deployed");
				explosionTexture = tankDeathTexture3;
			}
			else if (Game.infDeath == true)
			{
				System.out.println("infantry death animation deployed");
				explosionTexture = infDeathTexture3;
			}
			
			else if (Game.infAttack == true)
			{
				System.out.println("infantry death animation deployed");
				explosionTexture = infAttackTexture3;
			}
			
			else if (Game.mechAttack == true)
			{
				System.out.println("infantry death animation deployed");
				explosionTexture = mechAttackTexture3;
			}
			
			else if (Game.tankAttack == true)
			{
				System.out.println("infantry death animation deployed");
				explosionTexture = tankAttackTexture3;
			}
		}
		else if(curTimeDelta >= 97 && curTimeDelta <= 115)
		{	
			if (Game.mechDeath == true)
			{
				System.out.println("tank death animation deployed");
				explosionTexture = tankDeathTexture4;
			}
			else if (Game.infDeath == true)
			{
				System.out.println("infantry death animation deployed");
				explosionTexture = infDeathTexture4;
			}
			
			else if (Game.infAttack == true)
			{
				System.out.println("infantry death animation deployed");
				explosionTexture = infAttackTexture4;
			}
			
			else if (Game.mechAttack == true)
			{
				System.out.println("infantry death animation deployed");
				explosionTexture = mechAttackTexture4;
			}
			
			else if (Game.tankAttack == true)
			{
				System.out.println("infantry death animation deployed");
				explosionTexture = tankAttackTexture4;
			}
		}
		else if(curTimeDelta >= 115 && curTimeDelta <= 132)
		{	
			if (Game.mechDeath == true)
			{
				System.out.println("tank death animation deployed");
				explosionTexture = tankDeathTexture5;
			}
			else if (Game.infDeath == true)
			{
				System.out.println("infantry death animation deployed");
				explosionTexture = infDeathTexture5;
			}
			
			else if (Game.infAttack == true)
			{
				System.out.println("infantry death animation deployed");
				explosionTexture = infAttackTexture5;
			}
			
			else if (Game.mechAttack == true)
			{
				System.out.println("infantry death animation deployed");
				explosionTexture = mechAttackTexture5;
			}
			
			else if (Game.tankAttack == true)
			{
				System.out.println("infantry death animation deployed");
				explosionTexture = tankAttackTexture5;
			}
		}
		else if(curTimeDelta >= 132 && curTimeDelta <= 200)
		{	
			if (Game.mechDeath == true)
			{
				System.out.println("tank death animation deployed");
				explosionTexture = tankDeathTexture6;
			}
			else if (Game.infDeath == true)
			{
				System.out.println("infantry death animation deployed");
				explosionTexture = infDeathTexture6;
			}
			
			else if (Game.infAttack == true)
			{
				System.out.println("infantry death animation deployed");
				explosionTexture = infAttackTexture6;
			}
			
			else if (Game.mechAttack == true)
			{
				System.out.println("infantry death animation deployed");
				explosionTexture = mechAttackTexture6;
			}
			
			else if (Game.tankAttack == true)
			{
				System.out.println("infantry death animation deployed");
				explosionTexture = tankAttackTexture6;
			}
			TileMap.timerOn = false;
		}
		
		else
		{TileMap.timerOn = false;}
		
		if(explosionTexture != null) {
			spriteBatch.draw(explosionTexture, currAtkCoord.getX() * Game.BLOCK_WIDTH + Game.TILE_OFFSET, 
			 currAtkCoord.getY() * Game.BLOCK_HEIGHT + Game.TILE_OFFSET, 
			 Game.BLOCK_WIDTH - Game.TILE_OFFSET, 
			 Game.BLOCK_HEIGHT - Game.TILE_OFFSET);
		}
	}

	
	

}
