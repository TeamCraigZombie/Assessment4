package com.geeselightning.zepr.desktop;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.geeselightning.zepr.Zepr;

public class DesktopLauncher {
	public static void main (String[] arg) {
		LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
		config.resizable = true;
		config.width = 1920;
		config.height = 1080;
		config.title = "Geese Lightning";
		new LwjglApplication(new Zepr(), config);
	}
}
