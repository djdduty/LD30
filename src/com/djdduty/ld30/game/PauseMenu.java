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

public class PauseMenu implements SubState {
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
	private boolean escDown = true;
	
	public PauseMenu(GameState s) {
		state = s;
	}
	
	public void init(StateManager manager) {
		this.manager = manager;
		
		//initialize font
		Engine.get().getTextureManager().getTexture("font-Big", "res/textures/font.png");
		font = new Font(13, "font-Big", " !\"#$%&'()*+ ,-./01234567 89:;<=>?@ABC DEFGHIJKLMNO PQRSTUVWXYZ");
		//
		
		//Add menu options
		labels.add(new FontString("Resume", new Vec2(Engine.get().getWidth()/2,Engine.get().getHeight()/2-35), new Vec2(30,30), font, true));
		labels.add(new FontString("Exit to menu", new Vec2(Engine.get().getWidth()/2,Engine.get().getHeight()/2), new Vec2(30,30), font, true));
		labels.add(new FontString("Quit", new Vec2(Engine.get().getWidth()/2,Engine.get().getHeight()/2+35), new Vec2(30,30), font, true));
		//
		
		//other
		mainLabel = new FontString("Paused!", new Vec2(Engine.get().getWidth()/2,Engine.get().getHeight()/2-100), new Vec2(60,60), font, true);
		backgroundEntity = new GameEntity(new Vec2(150, Engine.get().getHeight()/2-125), "popupBGEntity", new Vec2(500, 200));
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
		if(updateLabels) {
			labelChanged = true;
			updateLabels = false;
		}
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
			labelChanged = false;
		}
		
		if(!Keyboard.isKeyDown(Keyboard.KEY_DOWN) && !Keyboard.isKeyDown(Keyboard.KEY_S) && !Keyboard.isKeyDown(Keyboard.KEY_UP) && !Keyboard.isKeyDown(Keyboard.KEY_W)) {
			keyDown = false;
		}
		
		for(FontString s : labels)
			s.queueForRender();
		
		if(Keyboard.isKeyDown(Keyboard.KEY_ESCAPE) && !escDown) {
			state.menuOpen = false;
			state.escDown = true;
			manager.clearSubState();
		}
		
		if(!Keyboard.isKeyDown(Keyboard.KEY_ESCAPE))
			escDown = false;
		
		if(Keyboard.isKeyDown(Keyboard.KEY_SPACE) && !spaceDown) {
			spaceDown = true;
			if(selectedIndex == 0) {
				state.menuOpen = false;
				state.spaceDown = true;
				state.escDown = true;
				manager.clearSubState();
			}
			if(selectedIndex == 1) {
				manager.clearSubState();
				manager.setState(new MainState(true));
			}
			if(selectedIndex == 2)
				Engine.get().GetGameWindow().Stop();
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
