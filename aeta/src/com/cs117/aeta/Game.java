package com.cs117.aeta;

import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL10;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.math.Vector3;

import java.util.HashMap;

import com.cs117.tile_placeable.*;


public class Game implements ApplicationListener {
	
	public static int WIDTH;
	public static int HEIGHT;
	
	public static int[][] TileMap;
	
	public static int BLOCK_WIDTH;
	public static int BLOCK_HEIGHT;
	
	public static final int TILE_OFFSET = 2;
	
	public static final int NUM_ROWS = 8;
	public static final int NUM_COLS = 10;
	private ShapeRenderer shapeRenderer;
	
	private OrthographicCamera cam;
	
	private HashMap<Coordinate, Unit> unitMap;
	
	private Coordinate selectedTile;
	
	public void create() {
		WIDTH = Gdx.graphics.getWidth();
		HEIGHT = Gdx.graphics.getHeight();
		
		BLOCK_WIDTH = WIDTH / NUM_COLS;
		BLOCK_HEIGHT = HEIGHT / NUM_ROWS;
		
		TileMap = new int[][] { 
				{0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
			    {0, 1, 1, 1, 1, 1, 0, 0, 0, 0},
			    {0, 0, 1, 1, 1, 1, 0, 1, 0, 0},
				{0, 0, 0, 1, 1, 1, 1, 1, 0, 0},
				{0, 1, 1, 1, 1, 1, 1, 1, 0, 0},
				{0, 1, 1, 1, 1, 1, 1, 1, 1, 0},
				{0, 1, 1, 0, 0, 1, 1, 1, 1, 0},
				{0, 0, 0, 0, 0, 0, 0, 0, 0, 0}
		};
				
		shapeRenderer = new ShapeRenderer();
		cam = new OrthographicCamera(WIDTH, HEIGHT);
		cam.translate(WIDTH/2, HEIGHT/2);
		cam.update();		
		
		unitMap = new HashMap<Coordinate, Unit>();
		// put a unit at (0, 0)
		unitMap.put(new Coordinate(0, 0), new Unit(10, UNIT_TYPE.INFANTRY));
		// put a unit at (5, 1)
		unitMap.put(new Coordinate(5, 1), new Unit(10, UNIT_TYPE.INFANTRY));
		
		selectedTile = new Coordinate(-1, -1);
	}
	
	public void render() {
		// clear the screen to white
		Gdx.gl.glClearColor(1, 1, 1, 1);
		Gdx.gl.glClear(GL10.GL_COLOR_BUFFER_BIT);
		
		shapeRenderer.setProjectionMatrix(cam.combined);
		shapeRenderer.begin(ShapeType.Filled);
		// render the selection tile 
		if (selectedTile.getX() != -1) {
			shapeRenderer.setColor(Color.RED);
			shapeRenderer.rect(selectedTile.getX() * BLOCK_WIDTH, 
					           selectedTile.getY() * BLOCK_HEIGHT, 
					           BLOCK_WIDTH  + TILE_OFFSET, 
					           BLOCK_HEIGHT + TILE_OFFSET );
		}
		
		// render the tilemap
		for (int i = TileMap.length - 1; i > -1; i--) {
			for (int j = 0; j < TileMap[0].length; j++) {
				Color curColor = Color.BLACK;
				if (TileMap[i][j] == 1)
					curColor = Color.WHITE;
				else if (TileMap[i][j] == 2)
					curColor = Color.GRAY;
				shapeRenderer.setColor(curColor);
				shapeRenderer.rect(j * BLOCK_WIDTH + TILE_OFFSET,  
						           BLOCK_HEIGHT * (TileMap.length - 1) - i * BLOCK_HEIGHT + TILE_OFFSET, 
						           BLOCK_WIDTH  - TILE_OFFSET, 
						           BLOCK_HEIGHT - TILE_OFFSET);
			}
		}
		
		// render the units
		for (Coordinate c : unitMap.keySet()) {
			shapeRenderer.setColor(Color.CYAN);
			shapeRenderer.rect(c.getX() * BLOCK_WIDTH + TILE_OFFSET, 
					           c.getY() * BLOCK_HEIGHT + TILE_OFFSET, 
					           BLOCK_WIDTH - TILE_OFFSET, 
					           BLOCK_HEIGHT - TILE_OFFSET);
		}
		shapeRenderer.end();
		
		if (Gdx.input.isTouched()) {
			Vector3 touchPos = new Vector3();
			touchPos.set(Gdx.input.getX(), Gdx.input.getY(), 0);
			System.out.println("Touch/Click detected");
			System.out.println("X " + touchPos.x);
			System.out.println("Y " + touchPos.y);
			int xCoord = (int) touchPos.x / BLOCK_WIDTH;
			int yCoord = (int) touchPos.y / BLOCK_HEIGHT;
			// make tile green when touched
			//TileMap[yCoord][xCoord] = 2;
			
			selectedTile.setX(xCoord);
			selectedTile.setY(NUM_ROWS - yCoord - 1);
			
		}
	}
	
	public void resize(int width, int height) {}
	public void pause() {}
	public void resume() {}
	public void dispose() {}
	
}
