package com.cs117.aeta;

import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Gdx;
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
	
	public static int WIDTH;
	public static int HEIGHT;
	
	public static TileMap tilemap;
	public static UI ui;
	
	public static int BLOCK_WIDTH;
	public static int BLOCK_HEIGHT;
	public static final int TILE_OFFSET = 2;
	public static int UNIT_TEXT_X_OFFSET;
	public static int UNIT_TEXT_Y_OFFSET;
	public static final int NUM_ROWS = 8;
	public static final int NUM_COLS = 10;
	
	public static int pid = 0;
	private ShapeRenderer shapeRenderer;
	private SpriteBatch spriteBatch;
	private OrthographicCamera cam;
	
	private Vector3 touchPos;
	
	private BitmapFont font;
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
		cam = new OrthographicCamera(WIDTH, HEIGHT);
		cam.translate(WIDTH/2, HEIGHT/2);
		cam.update();		
		
		tilemap = new TileMap(shapeRenderer, spriteBatch, font, mActionResolver);
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
			
			int xCoord = (int) touchPos.x / BLOCK_WIDTH;
			int yCoord = (int) (NUM_ROWS - touchPos.y / BLOCK_HEIGHT);		
			
			int prevX = tilemap.getSelectedTile().getX();
			int prevY = tilemap.getSelectedTile().getY();
			
			tilemap.updateSelectedTile(xCoord, yCoord);
			ui.handleTilePress(xCoord, yCoord);
			tilemap.moveSelectedUnit(xCoord, yCoord, prevX, prevY);
			tilemap.attackWithSelectedUnit(xCoord, yCoord, prevX, prevY);
			if (ui.moveButtonPressed(prevX, prevY)) {
				tilemap.getWalkableTerrain();
			} else if (ui.atkButtonPressed(prevX, prevY)) {
				tilemap.getAttackableTerrain();
			}
			
			System.out.println("Touch/Click detected");
			System.out.println("X " + touchPos.x);
			System.out.println("Y " + touchPos.y);
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
	
}
