package com.cs117.aeta;

import java.util.ArrayList;
import java.util.HashMap;

import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton.TextButtonStyle;
import com.cs117.tile_placeable.Coordinate;
import com.cs117.tile_placeable.UNIT_TYPE;
import com.cs117.tile_placeable.Unit;

public class Game implements ApplicationListener {
	
	public static int WIDTH;
	public static int HEIGHT;
	
	public static int[][] TileMap;
	
	public static int BLOCK_WIDTH;
	public static int BLOCK_HEIGHT;
	public static final int TILE_OFFSET = 2;
	public static int UNIT_TEXT_X_OFFSET;
	public static int UNIT_TEXT_Y_OFFSET;
	public static final int NUM_ROWS = 8;
	public static final int NUM_COLS = 10;
	
	private ShapeRenderer shapeRenderer;
	private SpriteBatch spriteBatch;
	private OrthographicCamera cam;
	
	private Vector3 touchPos;
	private HashMap<Coordinate, Unit> unitMap;
	private Coordinate selectedTile;
	
	ArrayList<Coordinate> walkable;
	
	private BitmapFont font;
	
	private Stage stage;
	
	private TextureAtlas buttonAtlas;
	private TextButtonStyle buttonStyle;
	private TextButton moveBtn;
	private Skin skin;
	
	public void create() {
		WIDTH = Gdx.graphics.getWidth();
		HEIGHT = Gdx.graphics.getHeight();
		
		touchPos = new Vector3();
		
		BLOCK_WIDTH = WIDTH / NUM_COLS;
		BLOCK_HEIGHT = HEIGHT / NUM_ROWS;
		UNIT_TEXT_X_OFFSET = BLOCK_WIDTH / 2;
		UNIT_TEXT_Y_OFFSET = BLOCK_HEIGHT / 2;
		
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
		spriteBatch = new SpriteBatch();
		font = new BitmapFont(Gdx.files.internal("fonts/font.fnt"));
		font.setColor(Color.ORANGE);
		cam = new OrthographicCamera(WIDTH, HEIGHT);
		cam.translate(WIDTH/2, HEIGHT/2);
		cam.update();		
		
		unitMap = new HashMap<Coordinate, Unit>();
		// put a unit at (0, 0)
		unitMap.put(new Coordinate(0, 0), new Unit(10, UNIT_TYPE.INFANTRY));
		// put a unit at (5, 1)
		unitMap.put(new Coordinate(5, 1), new Unit(10, UNIT_TYPE.INFANTRY));
		unitMap.put(new Coordinate(0, 7), new Unit(10, UNIT_TYPE.INFANTRY));
		unitMap.put(new Coordinate(9, 7), new Unit(10, UNIT_TYPE.INFANTRY));
		unitMap.put(new Coordinate(9, 0), new Unit(10, UNIT_TYPE.INFANTRY));

		walkable = null;
		
		selectedTile = new Coordinate(-1, -1);
		
		stage = new Stage(WIDTH, HEIGHT, true);
		skin = new Skin();
		buttonAtlas = new TextureAtlas("buttons/button.pack");
		skin.addRegions(buttonAtlas);
		buttonStyle = new TextButtonStyle();
		buttonStyle.up = skin.getDrawable("button");
		buttonStyle.down = skin.getDrawable("buttonpressed");
		buttonStyle.font = font;	
		moveBtn = new TextButton("Move", buttonStyle);
		stage.addActor(moveBtn);
		moveBtn.setVisible(false);
		moveBtn.setWidth(BLOCK_WIDTH);
		moveBtn.setHeight(BLOCK_HEIGHT/2);
		Gdx.input.setInputProcessor(stage);
		//moveBtn.addListener(new ButtonListener());
	}
	
	
	public void render() {
		// clear the screen to white
		Gdx.gl.glClearColor(1, 1, 1, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		
		//stage.act();
		
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
		
		// render the terrain of the units
		for (Coordinate c : unitMap.keySet()) {
			shapeRenderer.setColor(Color.CYAN);
			shapeRenderer.rect(c.getX() * BLOCK_WIDTH + TILE_OFFSET, 
					           c.getY() * BLOCK_HEIGHT + TILE_OFFSET, 
					           BLOCK_WIDTH - TILE_OFFSET, 
					           BLOCK_HEIGHT - TILE_OFFSET);
		}
		
		// render walkable terrain moves
		if (walkable != null) {
			for (Coordinate c : walkable) {
				shapeRenderer.setColor(Color.BLUE);
				shapeRenderer.rect(c.getX() * BLOCK_WIDTH + TILE_OFFSET,
								   c.getY() * BLOCK_HEIGHT + TILE_OFFSET,
								   BLOCK_WIDTH - TILE_OFFSET,
								   BLOCK_HEIGHT - TILE_OFFSET);
			}
		}
		
		shapeRenderer.end();
		
		spriteBatch.begin();
		// render the letter of the units
		for (Coordinate c : unitMap.keySet()) {
			font.draw(spriteBatch, 
					 "Inf", 
					 c.getX() * BLOCK_WIDTH + UNIT_TEXT_X_OFFSET, 
					 (c.getY() + 1) * BLOCK_HEIGHT - UNIT_TEXT_Y_OFFSET);
		}
		spriteBatch.end();
		stage.draw();
		
		if (Gdx.input.justTouched()) {
			touchPos.set(Gdx.input.getX(), Gdx.input.getY(), 0);
			
			int xCoord = (int) touchPos.x / BLOCK_WIDTH;
			int yCoord = (int) (NUM_ROWS - touchPos.y / BLOCK_HEIGHT);		

			
			// only toggle if selected coordinates are different from previous
			int prevX = selectedTile.getX();
			int prevY = selectedTile.getY();
			selectedTile.setX(xCoord);
			selectedTile.setY(yCoord);
			
			// show UI if a coordinate with a unit is pressed
			if (unitMap.containsKey(selectedTile)) {
				int quadrant = selectedTile.getQuadrant();
				float quad_x_offset = 0;
				float quad_y_offset = 0;
				if (quadrant == 1) {
					quad_x_offset = -1 * moveBtn.getWidth() - TILE_OFFSET;
					quad_y_offset = moveBtn.getHeight() + TILE_OFFSET;
				} else if (quadrant == 2) {
					quad_x_offset = moveBtn.getWidth() + TILE_OFFSET;
					quad_y_offset = moveBtn.getHeight() + TILE_OFFSET;
				} else if (quadrant == 3) {
					quad_x_offset = moveBtn.getWidth() + TILE_OFFSET;
					quad_y_offset = moveBtn.getHeight();
				} else if (quadrant == 4) {
					quad_x_offset = -1 * moveBtn.getWidth();
					quad_y_offset = moveBtn.getHeight();
				} else {
					System.err.println("Unexpected Quadrant received...");
				}
				moveBtn.setX(xCoord * BLOCK_WIDTH + quad_x_offset);
				moveBtn.setY(yCoord * BLOCK_HEIGHT + quad_y_offset);
				moveBtn.setVisible(true);
			} else {
				moveBtn.setVisible(false);
			}
			
			// move unit if blue tile selected
			if (walkable != null) {
				for (Coordinate c : walkable) {
					if (c.equals(selectedTile)) {
						Unit curUnit = unitMap.get(c);
						System.out.println("removing : ");
						System.out.println(c);
						unitMap.remove(new Coordinate(prevX, prevY));
						unitMap.put(new Coordinate(xCoord, yCoord), curUnit);
						walkable = null;
						break;
					}
				}
				// no tile selected, therefore effectively a cancel
				walkable = null;
				
			}
			
			// don't change what tile we're examining if a button is being pressed
			if (moveBtn.isPressed()) {
				System.out.println("Game::Move pressed");
				
				selectedTile.setX(prevX);
				selectedTile.setY(prevY);
				// clear button so that movement tiles can be shown
				moveBtn.setVisible(false);
				walkable = Unit.getWalkableTerrain(TileMap, selectedTile, unitMap);
				for (Coordinate c : walkable) {
					System.out.println(c);
				}
			}		
			
		}
	}
	
	public void resize(int width, int height) {}
	public void pause() {}
	public void resume() {}
	public void dispose() {
		buttonAtlas.dispose();
		skin.dispose();
		spriteBatch.dispose();
		font.dispose();
		shapeRenderer.dispose();
		stage.dispose();
		
	}
	
}
