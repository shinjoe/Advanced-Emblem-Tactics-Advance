package com.cs117.interaction;

import java.util.HashMap;


import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton.TextButtonStyle;
import com.cs117.aeta.Game;
import com.cs117.tile.Coordinate;
import com.cs117.tile.TileMap;
import com.cs117.units.Unit;

public class UI {
	
	private Stage stage;
	private TextureAtlas buttonAtlas;
	private TextButtonStyle buttonStyle;
	private TextButton moveBtn;
	private Skin skin;
	private Coordinate selectedTile;
	private HashMap<Coordinate, Unit> unitMap;
	
	public UI(int WIDTH, int HEIGHT, BitmapFont font, TileMap tilemap) {
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
		moveBtn.setWidth(Game.BLOCK_WIDTH);
		moveBtn.setHeight(Game.BLOCK_HEIGHT/2);
		Gdx.input.setInputProcessor(stage);
		selectedTile = tilemap.getSelectedTile();
		unitMap = tilemap.getUnitMap();
	}

	public void draw() {
		stage.draw();
	}
	
	public void handleTilePress(int xCoord, int yCoord) {
		// show UI if a coordinate with a unit is pressed
		if (unitMap.containsKey(selectedTile)) {
			int quadrant = selectedTile.getQuadrant();
			float quad_x_offset = 0;
			float quad_y_offset = 0;
			if (quadrant == 1) {
				quad_x_offset = -1 * moveBtn.getWidth() - Game.TILE_OFFSET;
				quad_y_offset = moveBtn.getHeight() + Game.TILE_OFFSET;
			} else if (quadrant == 2) {
				quad_x_offset = moveBtn.getWidth() + Game.TILE_OFFSET;
				quad_y_offset = moveBtn.getHeight() + Game.TILE_OFFSET;
			} else if (quadrant == 3) {
				quad_x_offset = moveBtn.getWidth() + Game.TILE_OFFSET;
				quad_y_offset = moveBtn.getHeight();
			} else if (quadrant == 4) {
				quad_x_offset = -1 * moveBtn.getWidth();
				quad_y_offset = moveBtn.getHeight();
			} else {
				System.err.println("Unexpected Quadrant received...");
			}
			moveBtn.setX(xCoord * Game.BLOCK_WIDTH + quad_x_offset);
			moveBtn.setY(yCoord * Game.BLOCK_HEIGHT + quad_y_offset);
			moveBtn.setVisible(true);
		} else {
			moveBtn.setVisible(false);
		}
	}
	
	public boolean buttonPressed(int prevX, int prevY) {
		// don't change what tile we're examining if a button is being pressed
		if (moveBtn.isPressed()) {
			System.out.println("Game::Move pressed");
			selectedTile.setX(prevX);
			selectedTile.setY(prevY);
			// clear button so that movement tiles can be shown
			moveBtn.setVisible(false);
			return true;
		}
		return false;
	}
	
	public void dispose() {
		buttonAtlas.dispose();
		skin.dispose();
		stage.dispose();
	}
	
}
