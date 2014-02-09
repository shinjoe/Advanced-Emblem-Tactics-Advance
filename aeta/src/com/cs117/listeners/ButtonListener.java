package com.cs117.listeners;

import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;

public class ButtonListener extends InputListener {
	@Override 
	public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
		System.out.println("ButtonListener::touchDown");
		return true;
	}

}
