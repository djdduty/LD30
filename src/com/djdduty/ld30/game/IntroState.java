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
import com.djdduty.ld30.math.Vec4;
import com.djdduty.ld30.scene.Scene;
import com.djdduty.ld30.scene.entity.Entity;

public class IntroState implements State {
	private Font font;
	private ArrayList<FontString> labels = new ArrayList<>();
	
	private StateManager manager;
	private GameEntity backgroundEntity;
	
	private boolean keyDown = true;
	
	private float timeElapsed = 0;
	
	public void init(StateManager manager) {		
		Engine.get().getTextureManager().getTexture("font-Big", "res/textures/font-Big.png");
		font = new Font(13, "font-Big", " !\"#$%&'()*+ ,-./01234567 89:;<=>?@ABC DEFGHIJKLMNO PQRSTUVWXYZ");
		
		ArrayList<String> strings = new ArrayList<>();
		strings.add("They weren't supposed to come this far...");
		strings.add("I am the watcher between two worlds,");
		strings.add("doomed to watch the bridge from one world");
		strings.add("to the other. I have lived peacefully here");
		strings.add("until now, they want to go across the bridge.");
		strings.add("I can't let them...");
		strings.add(" ");
		strings.add("I knew this day would come, and I have");
		strings.add("everything I could need right here in my");
		strings.add("shack in case it gets a little heavy.");
		strings.add(" ");
		strings.add("Controls:");
		strings.add("Movement: WASD / Arrow keys");
		strings.add("Aim & Shoot: Mouse & left mouse button");
		strings.add(" ");
		strings.add(" ");
		strings.add(" ");
		strings.add(" ");
		strings.add(" ");
		strings.add(" ");
		strings.add("Press space to continue...");
		
		for(int i = 0; i < strings.size(); i++) {
			FontString t = new FontString(strings.get(i), new Vec2(Engine.get().getWidth()/2,50+(i*24)), new Vec2(24,24), font, true); 
			labels.add(t);
			t.getRenderable().getMaterial().setDiffuseColor(new Vec4(1,1,1,0));
		}
		
		backgroundEntity = new GameEntity(new Vec2(150, -150), "bgEntity", new Vec2(1024, 1024));
		backgroundEntity.init(null);
		backgroundEntity.getRenderable().getMaterial().setDiffuseColor(new Vec4(1,1,1,0));
		backgroundEntity.getRenderable().getMaterial().setDiffuseTexture("introBG");
		
		this.manager = manager;
	}

	public void update(double deltaTime) {
		timeElapsed += deltaTime*0.001f;
		backgroundEntity.update((float)deltaTime);
		backgroundEntity.queueForRender();
		
		if(timeElapsed < 2.0f) {
			for(FontString s : labels)
				s.getRenderable().getMaterial().setDiffuseColor(new Vec4(1, 1, 1, timeElapsed*0.5f));
			
			backgroundEntity.getRenderable().getMaterial().setDiffuseColor(new Vec4(1,1,1,timeElapsed*0.5f));
		}
		
		for(FontString s : labels)
			s.queueForRender();
		
		if(Keyboard.isKeyDown(Keyboard.KEY_SPACE) && !keyDown) {
			manager.setState(new GameState());
			keyDown = true;
		} 
		
		if (!Keyboard.isKeyDown(Keyboard.KEY_SPACE)) {
			keyDown = false;
		}
	}

	public void onInit() {
		
	}

	public void DeInit() {
		
	}

}
