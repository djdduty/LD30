package com.djdduty.ld30.game;

import java.util.ArrayList;

import org.lwjgl.input.Keyboard;

import com.djdduty.ld30.core.Engine;
import com.djdduty.ld30.core.StateManager;
import com.djdduty.ld30.core.SubState;
import com.djdduty.ld30.game.entities.GameEntity;
import com.djdduty.ld30.game.entities.PlayerController;
import com.djdduty.ld30.gui.Font;
import com.djdduty.ld30.gui.FontString;
import com.djdduty.ld30.math.Vec2;

public class UpgradeMenu implements SubState {
	private Font font;
	private FontString mainLabel;
	private StateManager manager;
	private GameState state;
	private GameEntity backgroundEntity;
	
	private ArrayList<FontString> labels = new ArrayList<>();
	
	private int selectedIndex = 0;
	private boolean updateLabels = true;
	private boolean keyDown = false;
	private boolean spaceDown = true;
	
	private PlayerController player;
	
	public UpgradeMenu(GameState s, PlayerController player) {
		state = s;
		this.player = player;
	}
	
	public UpgradeMenu(GameState s, PlayerController player, int selectedIndex) {
		this(s, player);
		this.selectedIndex = selectedIndex;
	}
	
	public void init(StateManager manager) {
		this.manager = manager;
		
		//initialize font
		Engine.get().getTextureManager().getTexture("font-Big", "res/textures/font.png");
		font = new Font(13, "font-Big", " !\"#$%&'()*+ ,-./01234567 89:;<=>?@ABC DEFGHIJKLMNO PQRSTUVWXYZ");
		//
		
		//Add menu options
		labels.add(new FontString("Heal(Cost:"+player.getHealCost()+")", new Vec2(Engine.get().getWidth()/2,Engine.get().getHeight()/2-35), new Vec2(30,30), font, true));
		labels.add(new FontString("Max Health++(Cost:"+player.getHealthUpCost()+")", new Vec2(Engine.get().getWidth()/2,Engine.get().getHeight()/2), new Vec2(30,30), font, true));
		labels.add(new FontString("Acceleration++(Cost:"+player.getAccUpCost()+")", new Vec2(Engine.get().getWidth()/2,Engine.get().getHeight()/2+35), new Vec2(30,30), font, true));
		labels.add(new FontString("Fire Rate++(Cost:"+player.getFireRateUpCost()+")", new Vec2(Engine.get().getWidth()/2,Engine.get().getHeight()/2+70), new Vec2(30,30), font, true));
		labels.add(new FontString("Damage++(Cost:"+player.getDamageUpCost()+")", new Vec2(Engine.get().getWidth()/2,Engine.get().getHeight()/2+105), new Vec2(30,30), font, true));
		labels.add(new FontString("Go Back", new Vec2(Engine.get().getWidth()/2,Engine.get().getHeight()/2+145), new Vec2(30,30), font, true));
		//
		
		//other
		mainLabel = new FontString("Upgrades!", new Vec2(Engine.get().getWidth()/2,Engine.get().getHeight()/2-100), new Vec2(60,60), font, true);
		backgroundEntity = new GameEntity(new Vec2(40, Engine.get().getHeight()/2-125), "popupBGEntity", new Vec2(720, 300));
		backgroundEntity.init(null);
		backgroundEntity.mass = 0.0f;
		backgroundEntity.getRenderable().getMaterial().setDiffuseTexture("menubg");
		//
	}

	public void update(double deltaTime) {
		mainLabel.queueForRender();
		backgroundEntity.update((float)deltaTime);
		backgroundEntity.queueForRender();
		
		boolean labelChanged = false;
		if(updateLabels)
			labelChanged = true;
		int oldIndex = 0;
		
		if((Keyboard.isKeyDown(Keyboard.KEY_DOWN) || Keyboard.isKeyDown(Keyboard.KEY_S)) && !keyDown) {
			oldIndex = selectedIndex;
			selectedIndex++;
			labelChanged = true;
			keyDown = true;
			Engine.get().getSoundManager().getSound("menuSelect").playAsSoundEffect(1, 1, false);
		}
		
		if((Keyboard.isKeyDown(Keyboard.KEY_UP) || Keyboard.isKeyDown(Keyboard.KEY_W)) && !keyDown) {
			oldIndex = selectedIndex;
			selectedIndex--;
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
			labels.get(oldIndex).setSize(new Vec2(30,30));
			labels.get(selectedIndex).setSize(new Vec2(40,40));
		}
		
		if(!Keyboard.isKeyDown(Keyboard.KEY_DOWN) && !Keyboard.isKeyDown(Keyboard.KEY_S) && !Keyboard.isKeyDown(Keyboard.KEY_UP) && !Keyboard.isKeyDown(Keyboard.KEY_W)) {
			keyDown = false;
		}
		
		for(FontString s : labels)
			s.queueForRender();
		
		if(Keyboard.isKeyDown(Keyboard.KEY_ESCAPE)) {
			state.menuOpen = false;
			manager.clearSubState();
		}
		
		if(Keyboard.isKeyDown(Keyboard.KEY_SPACE) && !spaceDown) {
			spaceDown = true;
			if(selectedIndex == 0)
				player.heal();
			if(selectedIndex == 1)
				player.upHealth();
			if(selectedIndex == 2)
				player.upAcc();
			if(selectedIndex == 3)
				player.upFireRate();
			if(selectedIndex == 4)
				player.upDamage();
			if(selectedIndex == 5) {
				state.menuOpen = false;
				state.spaceDown = true;
				manager.clearSubState();
			} else {
				manager.setSubState(new UpgradeMenu(state, player, selectedIndex));
			}
		}
		
		if(!Keyboard.isKeyDown(Keyboard.KEY_SPACE)) {
			spaceDown = false;
		}
	}

	public void onInit() {
		
	}

	public void DeInit() {
		
	}

}
