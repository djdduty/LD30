package com.djdduty.ld30.core;

public class StateManager {
	private Game game;
	private State currentState;
	private SubState currentSubState;
	
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
	
	public void setSubState(SubState subState) {
		SubState oldState = currentSubState;
		currentSubState = subState;
		if(oldState != null)
			oldState.DeInit();
		currentSubState.init(this);
	}
	
	public void onInit() {
		currentState.onInit();
		if(currentSubState != null)
			currentSubState.onInit();
	}
	
	public void onDeinit() {
		currentState.DeInit();
		if(currentSubState != null)
			currentSubState.DeInit();
	}
	
	public void update(double deltaTime) {
		try {
			currentState.update(deltaTime);
			if(currentSubState != null)
				currentSubState.update(deltaTime);
		} catch(Exception exc) {
			exc.printStackTrace();
		}
	}
	
	public Game getGame() {
		return game;
	}

	public void clearSubState() {
		if(currentSubState != null)
			currentSubState.DeInit();
		currentSubState = null;
	}
}
