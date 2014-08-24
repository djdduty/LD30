package com.djdduty.ld30.game;

import java.util.ArrayList;

import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;

import com.djdduty.ld30.core.Engine;
import com.djdduty.ld30.core.State;
import com.djdduty.ld30.core.StateManager;
import com.djdduty.ld30.game.entities.BruteController;
import com.djdduty.ld30.game.entities.CopterController;
import com.djdduty.ld30.game.entities.GameEntity;
import com.djdduty.ld30.game.entities.PlayerController;
import com.djdduty.ld30.game.entities.SpriteSheetEntity;
import com.djdduty.ld30.gui.Font;
import com.djdduty.ld30.gui.FontString;
import com.djdduty.ld30.math.Vec2;
import com.djdduty.ld30.math.Vec3;
import com.djdduty.ld30.scene.Scene;

public class GameState implements State {
	private Scene scene;
	private GameEntity playerEntity;
	private GameEntity groundEntity;
	private int score = 0;
	private Font font;
	private FontString scoreLabel;
	
	private ArrayList<GameEntity> mobs = new ArrayList<>();
	
	private int numBrutes = 0;
	private float bruteDelay = 10;
	private float lastBruteDelay = 10;
	
	private float missileDelay = 30;
	private float lastMissileDelay = 30;
	
	private int numCopters = 0;
	private float copterDelay = 5;
	private float lastCopterDelay = 5;
	
	private GameEntity backgroundEntity;
	
	
	public void init(StateManager manager) {
		scene = new Scene();
		
		//setup font/label
		Engine.get().getTextureManager().getTexture("font-Big", "res/textures/font.png");
		font = new Font(13, "font-Big", " !\"#$%&'()*+ ,-./01234567 89:;<=>?@ABC DEFGHIJKLMNO PQRSTUVWXYZ");
		
		scoreLabel = new FontString("Score:0", new Vec2(0,0), new Vec2(30,30), font, false);
		//
		
		//setup entities
		playerEntity = new GameEntity(new Vec2(400, 0), "playerEntity", new Vec2(128, 128), new PlayerController());
		playerEntity.mass = 0.0f;
		scene.addEntity(playerEntity);
		playerEntity.getRenderable().getMaterial().setDiffuseTexture("blimpRight");
		
		groundEntity = new GameEntity(new Vec2(0, 500), "groundEntity", new Vec2(Engine.get().getWidth(), 64));
		groundEntity.mass = 0.0f;
		groundEntity.setHealth(2000000000, 2000000000);
		scene.addEntity(groundEntity);
		groundEntity.getRenderable().getMaterial().setDiffuseTexture("bridge");
		
		backgroundEntity = new GameEntity(new Vec2(0, 0), "bgEntity", new Vec2(1024,1024));
		backgroundEntity.init(null);
		backgroundEntity.getRenderable().getMaterial().setDiffuseTexture("bg");
		//
	}

	public void update(double deltaTime) {
		//backgroundEntity.queueForRender();
		
		bruteDelay -= deltaTime*0.001f;
		missileDelay -= deltaTime*0.001f;
		copterDelay -= deltaTime*0.001f;
		
		if(bruteDelay <= 0) {
			createBrute();
		}
		
		if(copterDelay <= 0) {
			createCopter();
		}
		
		scene.update((float)deltaTime);
		
		for(int i = 0; i < mobs.size(); i++) {
			mobs.get(i).update((float)deltaTime);
		}
		
		scene.queueForRender();
		
		if(score != playerEntity.getScore()) {
			score = playerEntity.getScore();
			scoreLabel.setContent("Score:"+score);
		}
		
		scoreLabel.queueForRender();
		
		for(int i = 0; i < mobs.size(); i++) {
			mobs.get(i).queueForRender();
		}
		
		if(Mouse.getX() > playerEntity.getPosition().x()+64)
			playerEntity.getRenderable().getMaterial().setDiffuseTexture("blimpRight");
		else 
			playerEntity.getRenderable().getMaterial().setDiffuseTexture("blimpLeft");
	}
	
	public void createBrute() {
		numBrutes++;
		GameEntity brute = new SpriteSheetEntity(new Vec2(Engine.get().getWidth()-128, 500-128), "Brute"+numBrutes, new Vec2(128, 128), new BruteController());
		brute.setHealth(20, 20);
		brute.mass = 0.0f;
		mobs.add(brute);
		brute.init(scene);
		brute.getRenderable().getMaterial().setDiffuseTexture("truck");
		
		bruteDelay = lastBruteDelay*0.99f;
		if(bruteDelay < 2)
			bruteDelay = 2;
		lastBruteDelay = bruteDelay;
	}
	
	public void createCopter() {
		numCopters++;
		GameEntity copter = new SpriteSheetEntity(new Vec2(Engine.get().getWidth()-40, (int)(Math.random()*300)+100), "Copter"+numCopters, new Vec2(128, 128), new CopterController(playerEntity));
		copter.setHealth(100, 100);
		copter.mass = 0.0f;
		mobs.add(copter);
		copter.init(scene);
		copter.getRenderable().getMaterial().setDiffuseTexture("copter");
		
		copterDelay = lastCopterDelay*0.99f;
		if(copterDelay < 10)
			copterDelay = 10;
		lastCopterDelay = copterDelay;
	}
	
	public void onInit() {
		
	}

	public void DeInit() {
		
	}
}
