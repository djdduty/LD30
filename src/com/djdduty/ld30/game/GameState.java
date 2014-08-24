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
	private PlayerController playerCont;
	private GameEntity groundEntity;
	private GameEntity upgradeEntity;
	
	private int health = 100;
	private int maxHealth = 100;
	private int score = 0;
	private int strikes = 0;
	
	private Font font;
	private FontString scoreLabel;
	private FontString healthLabel;
	private FontString strikeLabel;
	private FontString lostLabel;
	private FontString lostHelpLabel;
	private FontString infoLabel;
	
	private ArrayList<GameEntity> mobs = new ArrayList<>();
	
	private int numBrutes = 0;
	private float bruteDelay = 10;
	private float lastBruteDelay = 10;
	
	private float missileDelay = 30;
	private float lastMissileDelay = 30;
	
	private int numCopters = 0;
	private float copterDelay = 45;
	private float lastCopterDelay = 45;
	
	private GameEntity backgroundEntity;
	public boolean menuOpen = false;
	
	private StateManager manager;
	public boolean spaceDown = false;
	
	
	public void init(StateManager manager) {
		this.manager = manager;
		scene = new Scene();
		
		//setup font/labels
		Engine.get().getTextureManager().getTexture("font-Big", "res/textures/font.png");
		font = new Font(13, "font-Big", " !\"#$%&'()*+ ,-./01234567 89:;<=>?@ABC DEFGHIJKLMNO PQRSTUVWXYZ");
		
		scoreLabel = new FontString("Points:0", new Vec2(0,0), new Vec2(30,30), font, false);
		healthLabel = new FontString("Health:100/100", new Vec2(0,30), new Vec2(30,30), font, false);
		strikeLabel = new FontString("Strikes:0", new Vec2(0,60), new Vec2(30,30), font, false);
		lostLabel = new FontString("You lost! Final score:", new Vec2(Engine.get().getWidth()/2, Engine.get().getHeight()/2), new Vec2(40,40), font, true);
		infoLabel = new FontString("Press space to acces shop.", new Vec2(Engine.get().getWidth()/2, Engine.get().getHeight()/2-150), new Vec2(36,36), font, true);
		lostHelpLabel = new FontString("Press space to try again.", new Vec2(Engine.get().getWidth()/2, Engine.get().getHeight()/2+30), new Vec2(24,24), font, true);
		//
		
		//setup entities
		playerCont = new PlayerController();
		playerEntity = new GameEntity(new Vec2(400-64, 300-64), "playerEntity", new Vec2(128, 128), playerCont);
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
		
		upgradeEntity = new GameEntity(new Vec2(200, 500-128), "upgradeEntity", new Vec2(128, 128));
		upgradeEntity.mass = 0.0f;
		upgradeEntity.setHealth(2000000000, 2000000000);
		upgradeEntity.init(scene);
		upgradeEntity.getRenderable().getMaterial().setDiffuseTexture("test");
		//
	}

	public void update(double deltaTime) {
		backgroundEntity.queueForRender();
		upgradeEntity.update((float)deltaTime);
		upgradeEntity.queueForRender();
		
		if(!menuOpen && strikes < 3 && health > 0) {
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
		}
		
		scene.queueForRender();
		
		if(health != playerEntity.getHealth() || maxHealth != playerEntity.getMaxHealth()) {
			health = (int) playerEntity.getHealth();
			maxHealth = (int) playerEntity.getMaxHealth();
			healthLabel.setContent("Health:"+health+"/"+maxHealth);
		}
		
		if(strikes != Engine.get().numStrikes) {
			strikes = Engine.get().numStrikes;
			strikeLabel.setContent("Strikes:"+strikes);
		}
		
		if(score != playerEntity.getScore()) {
			score = playerEntity.getScore();
			scoreLabel.setContent("Points:"+score);
			lostLabel.setContent("You lost! Final score:"+score);
		}
		
		scoreLabel.queueForRender();
		healthLabel.queueForRender();
		strikeLabel.queueForRender();
		if(strikes >= 3 || health <= 0) {
			lostLabel.queueForRender();
			lostHelpLabel.queueForRender();
		}
		
		for(int i = 0; i < mobs.size(); i++) {
			mobs.get(i).queueForRender();
		}
		
		if(Mouse.getX() > playerEntity.getPosition().x()+64)
			playerEntity.getRenderable().getMaterial().setDiffuseTexture("blimpRight");
		else 
			playerEntity.getRenderable().getMaterial().setDiffuseTexture("blimpLeft");
		
		if(upgradeEntity.collides(playerEntity) && Keyboard.isKeyDown(Keyboard.KEY_SPACE) && !spaceDown && !menuOpen) {
			showUpgradeMenu();
		}
		
		if(upgradeEntity.collides(playerEntity) && !menuOpen && strikes < 3 && health > 0)
			infoLabel.queueForRender();
		
		if((strikes >= 3 || health <= 0) && Keyboard.isKeyDown(Keyboard.KEY_SPACE)) {
			manager.setState(new GameState());
			Engine.get().numStrikes = 0;
		}
		
		if(!Keyboard.isKeyDown(Keyboard.KEY_SPACE))
			spaceDown = false;
	}
	
	private void showUpgradeMenu() {
		manager.setSubState(new UpgradeMenu(this, playerCont));
		menuOpen = true;
	}
	
	private void createBrute() {
		numBrutes++;
		GameEntity brute = new SpriteSheetEntity(new Vec2(Engine.get().getWidth()-128, 500-128), "Brute"+numBrutes, new Vec2(128, 128), new BruteController());
		brute.setHealth(20, 20);
		brute.mass = 0.0f;
		mobs.add(brute);
		brute.init(scene);
		brute.getRenderable().getMaterial().setDiffuseTexture("truck");
		
		bruteDelay = lastBruteDelay*0.95f;
		if(bruteDelay < 2)
			bruteDelay = 2;
		lastBruteDelay = bruteDelay;
	}
	
	private void createCopter() {
		numCopters++;
		GameEntity copter = new SpriteSheetEntity(new Vec2(Engine.get().getWidth()-40, (int)(Math.random()*300)+100-128), "Copter"+numCopters, new Vec2(128, 128), new CopterController(playerEntity));
		copter.setHealth(100, 100);
		copter.mass = 0.0f;
		mobs.add(copter);
		copter.init(scene);
		copter.getRenderable().getMaterial().setDiffuseTexture("copter");
		
		copterDelay = lastCopterDelay*0.95f;
		if(copterDelay < 10)
			copterDelay = 10;
		lastCopterDelay = copterDelay;
	}
	
	public void onInit() {
		
	}

	public void DeInit() {
		
	}
}
