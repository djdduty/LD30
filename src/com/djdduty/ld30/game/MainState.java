package com.djdduty.ld30.game;

import java.util.ArrayList;
import java.util.HashMap;

import org.lwjgl.input.Keyboard;

import com.djdduty.ld30.core.Engine;
import com.djdduty.ld30.core.State;
import com.djdduty.ld30.core.StateManager;
import com.djdduty.ld30.game.entities.BillboardEntity;
import com.djdduty.ld30.game.entities.GameEntity;
import com.djdduty.ld30.game.entities.PlayerController;
import com.djdduty.ld30.gui.Font;
import com.djdduty.ld30.gui.FontString;
import com.djdduty.ld30.math.Vec2;
import com.djdduty.ld30.math.Vec3;
import com.djdduty.ld30.scene.Scene;
import com.djdduty.ld30.scene.entity.Entity;

public class MainState implements State {
	private Font font;
	
	private FontString titleString;
	private FontString infoString;
	private ArrayList<FontString> labels = new ArrayList<>();
	
	private int selectedIndex = 0;
	private boolean keyDown = false;
	
	private StateManager manager;
	private GameEntity backgroundEntity;
	private boolean spaceDown = false;
	
	public MainState() {
		
	}
	
	public MainState(boolean sd) {
		spaceDown = sd;
	}
	
	public void init(StateManager manager) {
		//Load textures for the entire game
		Engine.get().getTextureManager().getTexture("test", "res/textures/checker.png");
		Engine.get().getTextureManager().getTexture("blimpLeft", "res/textures/blimpLeft.png");
		Engine.get().getTextureManager().getTexture("blimpRight", "res/textures/blimp.png");
		Engine.get().getTextureManager().getTexture("cannonBall", "res/textures/cannonBall.png");
		Engine.get().getTextureManager().getTexture("copter", "res/textures/Copter.png");
		Engine.get().getTextureManager().getTexture("bridge", "res/textures/bridge.png");
		Engine.get().getTextureManager().getTexture("bg", "res/textures/background.png");
		Engine.get().getTextureManager().getTexture("menubg", "res/textures/menubg.png");
		Engine.get().getTextureManager().getTexture("truck", "res/textures/brute.png");
		Engine.get().getTextureManager().getTexture("shop", "res/textures/house.png");
		Engine.get().getTextureManager().getTexture("introBG", "res/textures/introBG.png");
		//
				
		Engine.get().getTextureManager().getTexture("font-Big", "res/textures/font-Big.png");
		font = new Font(13, "font-Big", " !\"#$%&'()*+ ,-./01234567 89:;<=>?@ABC DEFGHIJKLMNO PQRSTUVWXYZ");
		
		titleString = new FontString("LD30", new Vec2(Engine.get().getWidth()/2,Engine.get().getHeight()/2-160), new Vec2(120,120), font, true);
		infoString = new FontString("Press SPACE to confirm", new Vec2(Engine.get().getWidth()/2,Engine.get().getHeight()-40), new Vec2(30,30), font, true);
		
		labels.add(new FontString("Play", new Vec2(Engine.get().getWidth()/2,Engine.get().getHeight()/2), new Vec2(60,60), font, true));
		labels.add(new FontString("About", new Vec2(Engine.get().getWidth()/2,Engine.get().getHeight()/2+80), new Vec2(40,40), font, true));
		labels.add(new FontString("Exit", new Vec2(Engine.get().getWidth()/2,Engine.get().getHeight()/2+160), new Vec2(40,40), font, true));
		
		backgroundEntity = new GameEntity(new Vec2(0, 0), "bgEntity", new Vec2(1024, 1024));
		backgroundEntity.init(null);
		backgroundEntity.getRenderable().getMaterial().setDiffuseTexture("bg");
		
		//Load audio for entire game
		try {
			Engine.get().getSoundManager().getSound("music", "res/sounds/music2.ogg").playAsMusic(1, 1, true);
			Engine.get().getSoundManager().getSound("menuSelect", "res/sounds/menuSelect.ogg");
			Engine.get().getSoundManager().getSound("shoot", "res/sounds/shoot.ogg");
			Engine.get().getSoundManager().getSound("hurt", "res/sounds/hurt.ogg");
			Engine.get().getSoundManager().getSound("explosion", "res/sounds/explosion.ogg");
			Engine.get().getSoundManager().getSound("powerup", "res/sounds/powerup.ogg");
			Engine.get().getSoundManager().getSound("fail", "res/sounds/fail.ogg");
		} catch (Throwable exc) {
			exc.printStackTrace();
		}
		//
		
		this.manager = manager;
	}

	public void update(double deltaTime) {
		backgroundEntity.queueForRender();
		boolean labelChanged = false;
		int oldIndex = 0;
		
		if((Keyboard.isKeyDown(Keyboard.KEY_DOWN) || Keyboard.isKeyDown(Keyboard.KEY_S)) && !keyDown) {
			oldIndex = selectedIndex;
			selectedIndex++;
			labelChanged = true;
			keyDown = true;
			Engine.get().getSoundManager().getSound("menuSelect").playAsSoundEffect(1, 1, false);
		}
		
		if(selectedIndex < 0)
			selectedIndex = labels.size()-1;
		
		if(selectedIndex > labels.size()-1) {
			selectedIndex = 0;
		}
		
		if(labelChanged) {
			labels.get(oldIndex).setSize(new Vec2(40,40));
			labels.get(selectedIndex).setSize(new Vec2(60,60));
			labelChanged = false;
		}
		
		if(!Keyboard.isKeyDown(Keyboard.KEY_DOWN) && !Keyboard.isKeyDown(Keyboard.KEY_S)) {
			keyDown = false;
		}
		
		for(FontString s : labels)
			s.queueForRender();
		
		titleString.queueForRender();
		infoString.queueForRender();
		
		if(Keyboard.isKeyDown(Keyboard.KEY_SPACE) && !spaceDown) {
			if(selectedIndex == 0) {
				manager.setState(new IntroState());
				Engine.get().getSoundManager().getSound("menuSelect").playAsSoundEffect(1, 1, false);
			} 
			if(selectedIndex == 2) {
				Engine.get().getSoundManager().getSound("menuSelect").playAsSoundEffect(1, 1, false);
				Engine.get().GetGameWindow().Stop();
			}
		}
		
		if(!Keyboard.isKeyDown(Keyboard.KEY_SPACE))
			spaceDown = false;
	}

	public void onInit() {
		
	}

	public void DeInit() {
		
	}

}
