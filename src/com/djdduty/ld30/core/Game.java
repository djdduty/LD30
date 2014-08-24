package com.djdduty.ld30.core;

public class Game extends GLProgram {
	private StateManager manager;
	private GameSettings settings;
	
	public Game(State startState, GameSettings settings) {
		super(settings.title, (int)settings.resolution.x(), (int)settings.resolution.y(), settings.resizable);
		setFPS(settings.targetFps);
		printFps = settings.printFps;
		
		manager = new StateManager(startState);
		this.settings = settings;
	}
	
	public void init() {
		Engine.get().init(this);
		manager.init(this);
	}

	public void update(long deltaTime) {
		double ms = deltaTime * 0.000001;
		manager.update(ms);
		Engine.get().update(deltaTime);
	}
	
	public void render() {
		Engine.get().render();
	}
}
