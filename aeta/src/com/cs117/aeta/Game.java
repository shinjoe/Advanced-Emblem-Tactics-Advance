package com.cs117.aeta;

import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.math.Vector3;
import com.cs117.tile.TileMap;
import com.cs117.connection.ActionResolver;
import com.cs117.interaction.UI;


public class Game implements ApplicationListener {
	public static int BLOCK_WIDTH;
	public static int BLOCK_HEIGHT;
	public static final int TILE_OFFSET = 2;
	public static int UNIT_TEXT_X_OFFSET;
	public static int UNIT_TEXT_Y_OFFSET;
	public static final int NUM_ROWS = 8;
	public static final int NUM_COLS = 10;
	public static int CAM_X_OFFSET = 0;
	public static int CAM_Y_OFFSET = 0;
	
	public static int WIDTH;
	public static int HEIGHT;
	
	public static TileMap tilemap;
	public static UI ui;
	
	public static int pid = 1;
	public static int curTurn = 1;
	private ShapeRenderer shapeRenderer;
	private SpriteBatch spriteBatch;
	private OrthographicCamera cam;
	
	private Vector3 touchPos;
	
	private BitmapFont font;
	private BitmapFont hpFont;
	private ActionResolver mActionResolver;
	
	public Game(ActionResolver actionResolver) {
		this.mActionResolver = actionResolver;
	}
	
	public void create() {
		WIDTH = Gdx.graphics.getWidth();
		HEIGHT = Gdx.graphics.getHeight();
		
		touchPos = new Vector3();
		
		BLOCK_WIDTH = WIDTH / NUM_COLS;
		BLOCK_HEIGHT = HEIGHT / NUM_ROWS;
		UNIT_TEXT_X_OFFSET = BLOCK_WIDTH / 2;
		UNIT_TEXT_Y_OFFSET = BLOCK_HEIGHT / 2;
		
		shapeRenderer = new ShapeRenderer();
		spriteBatch = new SpriteBatch();
		
		font = new BitmapFont(Gdx.files.internal("fonts/font.fnt"));
		font.setColor(Color.ORANGE);
		hpFont = new BitmapFont(Gdx.files.internal("fonts/hpFont.fnt"));
		hpFont.setColor(Color.WHITE);
		
		cam = new OrthographicCamera(WIDTH, HEIGHT);
		cam.translate(WIDTH/2, HEIGHT/2);
		cam.update();		
		//glViewport = new Rectangle(0, 0, WIDTH, HEIGHT);
		
		tilemap = new TileMap(shapeRenderer, spriteBatch, font, hpFont, mActionResolver);
		ui = new UI(WIDTH, HEIGHT, font, tilemap);
	}
	
	
	public void render() {
		// clear the screen to light green, this forms the tile outline
		Gdx.gl.glClearColor(0.145f, 0.776f, 0.0f, 1.0f);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		
		//stage.act();
		
		shapeRenderer.setProjectionMatrix(cam.combined);
		shapeRenderer.begin(ShapeType.Filled);
		// draw objects on tilemap
		tilemap.drawSelectedTile();
		shapeRenderer.end();
		
		spriteBatch.setProjectionMatrix(cam.combined);
		spriteBatch.begin();
		tilemap.drawTerrainTexture();
		tilemap.drawWalkable();
		tilemap.drawAttackable();
		//tilemap.__DEBUG_drawUnitString();
		tilemap.drawUnits();
		spriteBatch.end();
		
		ui.draw();
		
		// handle unit being pressed
		if (Gdx.input.justTouched()) {
			touchPos.set(Gdx.input.getX(), Gdx.input.getY(), 0);
			
			int xCoord = (int) touchPos.x / BLOCK_WIDTH + CAM_X_OFFSET;
			int yCoord = (int) (NUM_ROWS - touchPos.y / BLOCK_HEIGHT) + CAM_Y_OFFSET;		
			
			int prevX = tilemap.getSelectedTile().getX();
			int prevY = tilemap.getSelectedTile().getY();
			
		    if (ui.endTurnPressed(prevX, prevY)) {
		    	this.curTurn = 1-pid;
				mActionResolver.sendEndTurn(1-pid);
		    	return;
			} else if (ui.cameraPressed(prevX, prevY)) {
				ui.showArrows();
				return;
			} else if (ui.upArrowPressed(prevX, prevY)) {
				cam.translate(0, BLOCK_HEIGHT);
			    cam.update();
			    CAM_Y_OFFSET++;
				return;
			} else if (ui.rightArrowPressed(prevX, prevY)) {
				cam.translate(BLOCK_WIDTH, 0);
			    cam.update();
			    CAM_X_OFFSET++;
				return;
			} else if (ui.downArrowPressed(prevX, prevY)) {
				cam.translate(0, -BLOCK_HEIGHT);
				cam.update();
				CAM_Y_OFFSET--;
				return;
			} else if (ui.leftArrowPressed(prevX, prevY)) {
				cam.translate(-BLOCK_WIDTH, 0);
				cam.update();
				CAM_X_OFFSET--;
				return;
			}
			
		    
			
			tilemap.updateSelectedTile(xCoord, yCoord);
			ui.handleTilePress(xCoord, yCoord);
			
			// Only if unit is pressed
			tilemap.moveSelectedUnit(xCoord, yCoord, prevX, prevY);
			tilemap.attackWithSelectedUnit(xCoord, yCoord, prevX, prevY);
			
			// Only if atk/move button pressed
			if (ui.moveButtonPressed(prevX, prevY)) {
				tilemap.getWalkableTerrain();
			} else if (ui.atkButtonPressed(prevX, prevY)) {
				tilemap.getAttackableTerrain();
			} 
			
			System.out.println("Touch/Click detected");
			System.out.println("X " + touchPos.x);
			System.out.println("Y " + touchPos.y);
		} else if (Game.curTurn == Game.pid && Gdx.input.isKeyPressed(Keys.MENU)) {
			ui.showMenu();
		}
	}
	
	public void resize(int width, int height) {}
	public void pause() {}
	public void resume() {}
	public void dispose() {
		ui.dispose();		
		spriteBatch.dispose();
		font.dispose();
		shapeRenderer.dispose();
	}
	
	public TileMap getTileMap() {
		return tilemap;
	}
	
	public void displayTurnReady() {
		System.out.println("TURN RDYYYY");
	}
	
}
