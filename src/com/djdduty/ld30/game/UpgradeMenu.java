package com.djdduty.ld30.game;

import org.lwjgl.input.Keyboard;

import com.djdduty.ld30.core.Engine;
import com.djdduty.ld30.core.StateManager;
import com.djdduty.ld30.core.SubState;
import com.djdduty.ld30.gui.Font;
import com.djdduty.ld30.gui.FontString;
import com.djdduty.ld30.math.Vec2;

public class UpgradeMenu implements SubState {
	private Font font;
	private FontString mainLabel;
	private StateManager manager;
	private GameState state;
	
	public UpgradeMenu(GameState s) {
		state = s;
	}
	
	public void init(StateManager manager) {
		this.manager = manager;
		
		//initialize font
		Engine.get().getTextureManager().getTexture("font-Big", "res/textures/font.png");
		font = new Font(13, "font-Big", " !\"#$%&'()*+ ,-./01234567 89:;<=>?@ABC DEFGHIJKLMNO PQRSTUVWXYZ");
		//
		
		mainLabel = new FontString("Upgrades!", new Vec2(Engine.get().getWidth()/2,Engine.get().getHeight()/2-160), new Vec2(60,60), font, true);
	}

	public void update(double deltaTime) {
		mainLabel.queueForRender();
		
		if(Keyboard.isKeyDown(Keyboard.KEY_ESCAPE)) {
			state.menuOpen = false;
			manager.clearSubState();
		}
	}

	public void onInit() {
		
	}

	public void DeInit() {
		
	}

}
