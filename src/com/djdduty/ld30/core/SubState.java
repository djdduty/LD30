package com.djdduty.ld30.core;

public interface SubState {
	public void init(StateManager manager);
	public void update(double deltaTime);
	public void onInit();
	public void DeInit();
}
