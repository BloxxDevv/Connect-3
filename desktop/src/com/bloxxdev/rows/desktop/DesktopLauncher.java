package com.bloxxdev.rows.desktop;

import com.bloxxdev.rows.window.WindowConstants;
import org.mini2Dx.desktop.DesktopMini2DxConfig;

import com.badlogic.gdx.backends.lwjgl.DesktopMini2DxGame;

import com.bloxxdev.rows.Main;

public class DesktopLauncher {
	public static void main (String[] arg) {
		DesktopMini2DxConfig config = new DesktopMini2DxConfig(Main.GAME_IDENTIFIER);
		config.vSyncEnabled = true;
		config.width = WindowConstants.WIDTH;
		config.height = WindowConstants.HEIGHT;
		config.resizable = WindowConstants.RESIZABLE;
		new DesktopMini2DxGame(new Main(), config);
	}
}
