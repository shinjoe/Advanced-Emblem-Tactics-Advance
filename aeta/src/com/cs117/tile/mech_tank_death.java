package com.cs117.tile;

import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL10;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.cs117.aeta.Game;


public class mech_tank_death implements ApplicationListener{
	private static final int FRAME_COLS = 3;
	private static final int FRAME_ROWS = 2;
	
	Animation mech_tank_death_anime;
	Texture   expSheet;
	static TextureRegion[] expFrames;
	SpriteBatch     spriteBatch;
	TextureRegion   currentFrame;
	
	float stateTime;
	
	@Override
	public void create(){
		expSheet = new Texture(Gdx.files.internal("gfx/mech_tank_death_explosion.png"));	
		TextureRegion[][] tmp = TextureRegion.split(expSheet,expSheet.getWidth()/FRAME_COLS,expSheet.getHeight()/FRAME_ROWS);
		expFrames = new TextureRegion[FRAME_COLS * FRAME_ROWS];

		int index = 0;
		for (int i = 0; i < FRAME_ROWS; i++) {
		     for (int j = 0; j < FRAME_COLS; j++) {
		    	 expFrames[index++] = tmp[i][j];
		     }
		}
		mech_tank_death_anime = new Animation(0.15f,expFrames);
		spriteBatch = new SpriteBatch();
		stateTime = 0f;
	}
	
	public void renderAt(int X,int Y){
			Gdx.gl.glClear(GL10.GL_COLOR_BUFFER_BIT | GL10.GL_DEPTH_BUFFER_BIT);                        // #14
	        stateTime += Gdx.graphics.getDeltaTime();           // #15
	        currentFrame = mech_tank_death_anime.getKeyFrame(stateTime, true);  // #16
	        spriteBatch.begin();
	        spriteBatch.draw(currentFrame, X*Game.BLOCK_WIDTH+Game.TILE_OFFSET, 
	        							   Y*Game.BLOCK_HEIGHT+Game.TILE_OFFSET, 
	        							   Game.BLOCK_WIDTH - Game.TILE_OFFSET,Game.BLOCK_HEIGHT - Game.TILE_OFFSET);             // #17
	        spriteBatch.end();	
	}


	@Override
	public void resize(int width, int height) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void pause() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void resume() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void dispose() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void render() {
		 Gdx.gl.glClear(GL10.GL_COLOR_BUFFER_BIT | GL10.GL_DEPTH_BUFFER_BIT);                        // #14
	        stateTime += Gdx.graphics.getDeltaTime();           // #15
	        currentFrame = mech_tank_death_anime.getKeyFrame(stateTime, true);  // #16
	        spriteBatch.begin();
	        spriteBatch.draw(currentFrame,10,10);             // #17
	        spriteBatch.end();	
	}
}
