package com.djdduty.ld30.game;

import com.djdduty.ld30.core.Game;
import com.djdduty.ld30.core.GameSettings;
import com.djdduty.ld30.math.Vec2;

public class Main {
	public static void main(String[] args) throws Exception {
		GameSettings gameSettings = new GameSettings();
		gameSettings.resolution = new Vec2(800, 600);
		gameSettings.resizable = false;
		gameSettings.targetFps = 0;
		gameSettings.printFps = true;
		gameSettings.title = "LD30 - YE ARE A SPACE PIRATE, HARRY (Connected Worlds)";
		
		Game game = new Game(new MainState(), gameSettings);
		game.run();
	}
}
