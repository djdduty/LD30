package com.djdduty.ld30.core;

public class StateManager {
	private Game game;
	private State currentState;
	
	public StateManager(State state) {
		currentState = state;
	}
	
	public void init(Game game) {
		this.game = game;
		currentState.init(this);
	}
	
	public void setState(State newState) {
		State oldState = currentState;
		currentState = newState;
		oldState.DeInit();
		currentState.init(this);
	}
	
	public void onInit() {
		currentState.onInit();
	}
	
	public void onDeinit() {
		currentState.DeInit();
	}
	
	public void update(double deltaTime) {
		try {
			currentState.update(deltaTime);
		} catch(Exception exc) {
			exc.printStackTrace();
		}
	}
	
	public Game getGame() {
		return game;
	}
}
