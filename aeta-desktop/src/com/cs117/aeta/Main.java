package com.cs117.aeta;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;

public class Main {
	public static void main(String[] args) {
		LwjglApplicationConfiguration cfg = new LwjglApplicationConfiguration();
		cfg.title = "aeta";
		cfg.useGL20 = true;
		cfg.width = 600;
		cfg.height = 320;
		
		new LwjglApplication(new Game(), cfg);
	}
}
