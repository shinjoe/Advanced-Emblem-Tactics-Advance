package com.cs117.aeta;

import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL10;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.math.Vector3;


public class Game implements ApplicationListener {
	
	public static int WIDTH;
	public static int HEIGHT;
	
	public static int[][] TileMap;
	
	public static int BLOCK_WIDTH;
	public static int BLOCK_HEIGHT;
	
	public static final int NUM_ROWS = 8;
	public static final int NUM_COLS = 10;
	private ShapeRenderer shapeRenderer;
	
	private OrthographicCamera cam;
	
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
	}
	
	public void render() {
		// clear the screen to black
		Gdx.gl.glClearColor(0, 0, 0, 1);
		Gdx.gl.glClear(GL10.GL_COLOR_BUFFER_BIT);
		
		shapeRenderer.setProjectionMatrix(cam.combined);
		// render the tilemap
		shapeRenderer.begin(ShapeType.Filled);
		for (int i = TileMap.length - 1; i > -1; i--) {
			for (int j = 0; j < TileMap[0].length; j++) {
				Color curColor = Color.BLACK;
				if (TileMap[i][j] == 1)
					curColor = Color.WHITE;
				else if (TileMap[i][j] == 2)
					curColor = Color.GREEN;
				shapeRenderer.setColor(curColor);
				shapeRenderer.rect(j * BLOCK_WIDTH,  BLOCK_HEIGHT * (TileMap.length - 1) - i * BLOCK_HEIGHT, BLOCK_WIDTH, BLOCK_HEIGHT);
			}
		}
		shapeRenderer.end();
		
		if (Gdx.input.isTouched()) {
			Vector3 touchPos = new Vector3();
			touchPos.set(Gdx.input.getX(), Gdx.input.getY(), 0);
			System.out.println("Touch/Click detected");
			System.out.println("X " + touchPos.x);
			System.out.println("Y " + touchPos.y);
			// make tile green when touched
			TileMap[(int) (touchPos.y / BLOCK_HEIGHT)][(int) (touchPos.x / BLOCK_WIDTH)] = 2;
		}
	}
	
	public void resize(int width, int height) {}
	public void pause() {}
	public void resume() {}
	public void dispose() {}
	
}
