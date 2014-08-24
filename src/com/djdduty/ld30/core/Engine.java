package com.djdduty.ld30.core;

import com.djdduty.ld30.graphics.Renderer;
import com.djdduty.ld30.math.Vec2;
import com.djdduty.ld30.resource.ShaderManager;
import com.djdduty.ld30.resource.SoundManager;
import com.djdduty.ld30.resource.TextureManager;

public class Engine {
	private static Engine singleton = new Engine();
	private Game game;
	private TextureManager textureManager;
	private ShaderManager shaderManager;
	private SoundManager soundManager;
	private Renderer renderer;
	
	//Hacky hacky!
	public int numStrikes = 0;
	
	private Engine() {}
	
	public static Engine get() {
		return singleton;
	}
	
	public void init(Game g) {
		game = g;
		shaderManager = new ShaderManager();
		textureManager = new TextureManager();
		soundManager = new SoundManager();
		renderer = new Renderer();
		renderer.init();
	}
	
	public void update(double deltaTime) {
		//TODO: Update managers
		renderer.update((float)deltaTime);
	}
	
	public void render() {
		renderer.render();
	}
	
	public Game GetGameWindow() {
		return game;
	}
	
	public Renderer getRenderer() {
		return renderer;
	}
	
	public ShaderManager getShaderManager() {
		return shaderManager;
	}
	
	public TextureManager getTextureManager() {
		return textureManager;
	}
	
	public SoundManager getSoundManager() {
		return soundManager;
	}
	
	public int getWidth() {
		return game.getWidth();
	}
	
	public int getHeight() {
		return game.getHeight();
	}
}
