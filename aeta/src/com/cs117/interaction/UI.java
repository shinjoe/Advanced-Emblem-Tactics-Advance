package com.cs117.interaction;

import java.util.concurrent.ConcurrentHashMap;
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
	
	private static int MENU_WIDGET_HEIGHT = Game.BLOCK_HEIGHT * 2;
	private static int MENU_WIDGET_WIDTH = Game.BLOCK_WIDTH * 4;
	private static int END_TURN_X = Game.NUM_COLS * Game.BLOCK_WIDTH / 4 + Game.BLOCK_WIDTH / 2;
	private static int END_TURN_Y = Game.NUM_ROWS * Game.BLOCK_HEIGHT / 2;
	private static int CAM_BTN_X = END_TURN_X;
	private static int CAM_BTN_Y = END_TURN_Y - 3 * Game.BLOCK_HEIGHT;
	private static int UP_WIDTH = 2 * Game.BLOCK_WIDTH;
	private static int UP_HEIGHT = Game.BLOCK_HEIGHT;
	private static float UP_X = END_TURN_X + Game.BLOCK_WIDTH;
	private static float UP_Y = END_TURN_Y + 2 * Game.BLOCK_HEIGHT;
	private static int RIGHT_WIDTH = UP_HEIGHT;
	private static int RIGHT_HEIGHT = UP_WIDTH;
	private static float RIGHT_X = 9 * Game.BLOCK_WIDTH;
	private static float RIGHT_Y = 2 * Game.BLOCK_HEIGHT;
	
	
	private Stage stage;
	
	private TextureAtlas buttonAtlas;
	private TextButtonStyle buttonStyle;
	private TextButton moveBtn;
	private TextButton atkBtn;
	private TextButton endTurnBtn;
	private TextButton camBtn;
	private TextButton upBtn;
	private TextButton rightBtn;
	private TextButton downBtn;
	private TextButton leftBtn;
	private Skin skin;
	
	private Coordinate selectedTile;
	private ConcurrentHashMap<Coordinate, Unit> unitMap;
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
		endTurnBtn = new TextButton("End Turn", buttonStyle);
		camBtn = new TextButton("Move Camera", buttonStyle);
		upBtn = new TextButton("up", buttonStyle);
		rightBtn = new TextButton("right", buttonStyle);
		downBtn = new TextButton("down", buttonStyle);
		leftBtn = new TextButton("left", buttonStyle);
		stage.addActor(moveBtn);
		stage.addActor(atkBtn);
		stage.addActor(endTurnBtn);
		stage.addActor(camBtn);
		stage.addActor(upBtn);
		stage.addActor(rightBtn);
		stage.addActor(downBtn);
		stage.addActor(leftBtn);
		moveBtn.setVisible(false);
		moveBtn.setWidth(Game.BLOCK_WIDTH);
		moveBtn.setHeight(Game.BLOCK_HEIGHT);
		atkBtn.setVisible(false);
		atkBtn.setWidth(Game.BLOCK_WIDTH);
		atkBtn.setHeight(Game.BLOCK_HEIGHT);
		endTurnBtn.setVisible(false);
		endTurnBtn.setWidth(MENU_WIDGET_WIDTH);
		endTurnBtn.setHeight(MENU_WIDGET_HEIGHT);
		camBtn.setVisible(false);
		camBtn.setWidth(MENU_WIDGET_WIDTH);
		camBtn.setHeight(MENU_WIDGET_HEIGHT);
		camBtn.setX(CAM_BTN_X);
		camBtn.setY(CAM_BTN_Y);
		upBtn.setVisible(false);
		upBtn.setWidth(UP_WIDTH);
		upBtn.setHeight(UP_HEIGHT);
		upBtn.setX(UP_X);
		upBtn.setY(UP_Y);
		rightBtn.setVisible(false);
		rightBtn.setWidth(RIGHT_WIDTH);
		rightBtn.setHeight(RIGHT_HEIGHT);
		rightBtn.setX(RIGHT_X);
		rightBtn.setY(RIGHT_Y);
		downBtn.setVisible(false);
		downBtn.setWidth(UP_WIDTH);
		downBtn.setHeight(UP_HEIGHT);
		downBtn.setX(UP_X);
		downBtn.setY(Game.BLOCK_HEIGHT);
		leftBtn.setVisible(false);
		leftBtn.setWidth(RIGHT_WIDTH);
		leftBtn.setHeight(RIGHT_HEIGHT);
		leftBtn.setX(Game.BLOCK_WIDTH);
		leftBtn.setY(RIGHT_Y);
		
		
		Gdx.input.setInputProcessor(stage);
		selectedTile = tilemap.getSelectedTile();
		unitMap = tilemap.getUnitMap();
		moveOffset = new Vector2();
		atkOffset = new Vector2();
		endTurnBtn.setX(END_TURN_X);
		endTurnBtn.setY(END_TURN_Y);
	}

	public void draw() {
		stage.draw();
	}
	
	public void handleTilePress(int xCoord, int yCoord) {
		// show UI if a coordinate with a unit is pressed
		if (unitMap.containsKey(selectedTile) && 
			unitMap.get(selectedTile).getTeam() == Game.pid &&
			Game.pid == Game.curTurn) {
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
		endTurnBtn.setVisible(false);
		camBtn.setVisible(false);
		upBtn.setVisible(false);
		rightBtn.setVisible(false);
		downBtn.setVisible(false);
		leftBtn.setVisible(false);
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
	
	public void showMenu() {
		endTurnBtn.setVisible(true);
		camBtn.setVisible(true);
	}
	
	public void showArrows() {
		upBtn.setVisible(true);
		rightBtn.setVisible(true);
		downBtn.setVisible(true);
		leftBtn.setVisible(true);
	}
	
	public boolean upArrowPressed(int prevX, int prevY) {
		if (upBtn.isPressed()) {
			System.out.println("UI::up pressed");
			selectedTile.setX(prevX);
			selectedTile.setY(prevY);
			return true;
		}
		return false;
	}
	
	public boolean rightArrowPressed(int prevX, int prevY) {
		if (rightBtn.isPressed()) {
			System.out.println("UI::right pressed");
			selectedTile.setX(prevX);
			selectedTile.setY(prevY);
			return true;
		}
		return false;
	}
	
	public boolean downArrowPressed(int prevX, int prevY) {
		if (downBtn.isPressed()) {
			System.out.println("UI::down pressed");
			selectedTile.setX(prevX);
			selectedTile.setY(prevY);
			return true;
		}
		return false;
	}
	
	public boolean leftArrowPressed(int prevX, int prevY) {
		if (leftBtn.isPressed()) {
			System.out.println("UI::left pressed");
			selectedTile.setX(prevX);
			selectedTile.setY(prevY);
			return true;
		}
		return false;
	}
	
	public boolean cameraPressed(int prevX, int prevY) {
		if (camBtn.isPressed()) {
			System.out.println("UI::camera pressed");
			selectedTile.setX(prevX);
			selectedTile.setY(prevY);
			camBtn.setVisible(false);
			endTurnBtn.setVisible(false);
			return true;
		}
		return false;
	}
	
	public boolean endTurnPressed(int prevX, int prevY) {
		if (endTurnBtn.isPressed()) {
			System.out.println("UI::end turn pressed");
			selectedTile.setX(prevX);
			selectedTile.setY(prevY);
			endTurnBtn.setVisible(false);
			camBtn.setVisible(false);
			return true;
		}
		return false;
	}
	
	public boolean moveButtonPressed(int prevX, int prevY) {
		// don't change what tile we're examining if a button is being pressed
		if (moveBtn.isPressed()) {
			System.out.println("UI::Move pressed");
			selectedTile.setX(prevX);
			selectedTile.setY(prevY);
			// clear button so that movement tiles can be shown
			moveBtn.setVisible(false);
			atkBtn.setVisible(false);
			return true;
		} 
		return false;
	}
	
	public boolean atkButtonPressed(int prevX, int prevY) {
		if (atkBtn.isPressed()) {
			System.out.println("UI::Attack pressed");
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
