package com.snakegame.desktop;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.snakegame.Application;

public class DesktopLauncher {
	public static void main (String[] arg) {
		LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
		config.title = Application.APP_TITLE;
		config.resizable = false;
		config.width = Application.WIDTH;
		config.height = Application.HEIGHT;
		config.backgroundFPS = Application.APP_FPS;
		config.foregroundFPS = Application.APP_FPS;
		new LwjglApplication(new Application(), config);
	}
}
