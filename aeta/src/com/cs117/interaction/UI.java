package com.cs117.interaction;

import java.util.HashMap;


import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.math.Vector2;
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
	private TextButton atkBtn;
	private Skin skin;
	private Coordinate selectedTile;
	private HashMap<Coordinate, Unit> unitMap;
	private Vector2 moveOffset;
	private Vector2 atkOffset;
	
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
		atkBtn = new TextButton("Attack", buttonStyle);
		stage.addActor(moveBtn);
		stage.addActor(atkBtn);
		moveBtn.setVisible(false);
		moveBtn.setWidth(Game.BLOCK_WIDTH);
		moveBtn.setHeight(Game.BLOCK_HEIGHT);
		atkBtn.setVisible(false);
		atkBtn.setWidth(Game.BLOCK_WIDTH);
		atkBtn.setHeight(Game.BLOCK_HEIGHT);
		Gdx.input.setInputProcessor(stage);
		selectedTile = tilemap.getSelectedTile();
		unitMap = tilemap.getUnitMap();
		moveOffset = new Vector2();
		atkOffset = new Vector2();
	}

	public void draw() {
		stage.draw();
	}
	
	public void handleTilePress(int xCoord, int yCoord) {
		// show UI if a coordinate with a unit is pressed
		if (unitMap.containsKey(selectedTile)) {
			int quadrant = selectedTile.getQuadrant();
			
			calculateMoveBtnOffset(quadrant);
			moveBtn.setX(xCoord * Game.BLOCK_WIDTH  + moveOffset.x);
			moveBtn.setY(yCoord * Game.BLOCK_HEIGHT + moveOffset.y);
			moveBtn.setVisible(true);
			
			calculateAtkBtnOffset(quadrant);
			atkBtn.setX(xCoord * Game.BLOCK_WIDTH + atkOffset.x);
			atkBtn.setY(yCoord * Game.BLOCK_HEIGHT + atkOffset.y);
			atkBtn.setVisible(true);
			
		} else {
			moveBtn.setVisible(false);
			atkBtn.setVisible(false);
		}
	}
	
	private void calculateAtkBtnOffset(int quadrant) {
		atkOffset.set(moveOffset.x, 0);
		if (quadrant == 1 || quadrant == 2) {
			atkOffset.set(atkOffset.x, -1 * atkBtn.getHeight() - Game.TILE_OFFSET);
		} else if (quadrant == 3 || quadrant == 4) {
			atkOffset.set(atkOffset.x, -Game.TILE_OFFSET);
		} else {
			System.err.println("Unexpected Quadrant in calculateAtkBtnOffset");
		}
	}
	
	private void calculateMoveBtnOffset(int quadrant) {
		float x_offset = 0;
		float y_offset = 0;
		if (quadrant == 1) {
			x_offset = -1 * moveBtn.getWidth() - Game.TILE_OFFSET;
			y_offset = Game.TILE_OFFSET;
		} else if (quadrant == 2) {
			x_offset = moveBtn.getWidth() + Game.TILE_OFFSET;
			y_offset = Game.TILE_OFFSET;
		} else if (quadrant == 3) {
			x_offset = moveBtn.getWidth() + Game.TILE_OFFSET;
			y_offset = moveBtn.getHeight();
		} else if (quadrant == 4) {
			x_offset = -1 * moveBtn.getWidth();
			y_offset = moveBtn.getHeight();
		} else {
			System.err.println("Unexpected Quadrant received...");
		}
		moveOffset.set(x_offset, y_offset);
	}
	
	public boolean buttonPressed(int prevX, int prevY) {
		// don't change what tile we're examining if a button is being pressed
		if (moveBtn.isPressed()) {
			System.out.println("Game::Move pressed");
			selectedTile.setX(prevX);
			selectedTile.setY(prevY);
			// clear button so that movement tiles can be shown
			moveBtn.setVisible(false);
			atkBtn.setVisible(false);
			return true;
		} else if (atkBtn.isPressed()) {
			System.out.println("Game::Attack pressed");
			selectedTile.setX(prevX);
			selectedTile.setY(prevY);
			// clear button so that movement tiles can be shown
			moveBtn.setVisible(false);
			atkBtn.setVisible(false);
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
