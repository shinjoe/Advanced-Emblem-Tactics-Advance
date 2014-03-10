package com.cs117.animation;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.cs117.aeta.Game;
import com.cs117.tile.TileMap;

public class ExplosionAnimation {

	private static final int    FRAME_COLS = 4;     
	private static final int    FRAME_ROWS = 4;     

	Animation           explAnimation;     
	Texture             explSheet;      
	TextureRegion[]     explFrames;    
	TextureRegion       currentFrame;      
	
	public static float stateTime;                

	public ExplosionAnimation() {
		explSheet = new Texture(Gdx.files.internal("spritesheets/explosion.png"));
		TextureRegion[][] tmp = TextureRegion.split(explSheet, explSheet.getWidth()/FRAME_COLS, explSheet.getHeight()/FRAME_ROWS);              // #10
		explFrames = new TextureRegion[FRAME_COLS * FRAME_ROWS];
		int index = 0;
		for (int i = 0; i < FRAME_ROWS; i++) {
			for (int j = 0; j < FRAME_COLS; j++) {
				explFrames[index++] = tmp[i][j];
			}
		}
		explAnimation = new Animation(0.025f, explFrames);      
		stateTime = 0f;                         
	}

	public void draw(SpriteBatch spriteBatch, int x, int y) {
	
		stateTime += Gdx.graphics.getDeltaTime();  
		if (explAnimation.isAnimationFinished(stateTime)) {
			TileMap.animOver = true;
			stateTime = 0f;
			return;
		}
		currentFrame = explAnimation.getKeyFrame(stateTime, true);  
		spriteBatch.draw(currentFrame, 
						 x * Game.BLOCK_WIDTH + Game.TILE_OFFSET, 
					 	 y * Game.BLOCK_HEIGHT + Game.TILE_OFFSET, 
				 		 Game.BLOCK_WIDTH - Game.TILE_OFFSET,
					     Game.BLOCK_HEIGHT - Game.TILE_OFFSET);  
	}
}

