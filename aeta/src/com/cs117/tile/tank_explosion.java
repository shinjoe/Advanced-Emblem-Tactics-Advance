package com.cs117.tile;

import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL10;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;


public class tank_explosion implements ApplicationListener{
	private static final int FRAME_COLS = 3;
	private static final int FRAME_ROWS = 2;
	
	Animation tankExpAnimation;
	Texture   expSheet;
	static TextureRegion[] expFrames;
	SpriteBatch     spriteBatch;
	TextureRegion   currentFrame;
	
	float stateTime;
	
	@Override
	public void create(){
		expSheet = new Texture(Gdx.files.internal("tankAttackExplosion.png"));
		TextureRegion[][] tmp = TextureRegion.split(expSheet,expSheet.getWidth(),expSheet.getHeight());
		expFrames = new TextureRegion[FRAME_COLS*FRAME_ROWS];
		int index = 0;
		for (int i = 0; i < FRAME_ROWS; i++) {
		     for (int j = 0; j < FRAME_COLS; j++) {
		    	 expFrames[index++] = tmp[i][j];
		     }
		}
		tankExpAnimation = new Animation(0.025f,expFrames);
		spriteBatch = new SpriteBatch();
		stateTime = 0f;
	
		
		
	}
	
	@Override
	public void render(){
		 Gdx.gl.glClear(GL10.GL_COLOR_BUFFER_BIT | GL10.GL_DEPTH_BUFFER_BIT);                        // #14
	        stateTime += Gdx.graphics.getDeltaTime();           // #15
	        currentFrame = tankExpAnimation.getKeyFrame(stateTime, true);  // #16
	        spriteBatch.begin();
	        spriteBatch.draw(currentFrame, 50, 50);             // #17
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
}
