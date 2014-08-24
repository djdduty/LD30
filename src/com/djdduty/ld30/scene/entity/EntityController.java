package com.djdduty.ld30.scene.entity;

public class EntityController {
	protected Entity owner;
	private boolean initialized = false;
	private boolean enabled = false;
	
	public EntityController() {}
	
	public final void init(Entity owner) {
		if(!initialized) {
			this.owner = owner;
			onInit();
			initialized = true;
		}
	}
	
	public final void deInit() {
		if(initialized) {
			onDeInit();
			initialized = false;
		}
	}
	
	public final void enable() {
		if(!enabled) {
			onEnable();
			enabled = true;
		}
	}
	
	public final void disable() {
		if(enabled) {
			onDisable();
			enabled = false;
		}
	}
	
	public final void update(float deltaTime) {
		if(enabled && initialized)
			onUpdate(deltaTime);
	}
	
	public void onCollision(CollisionEvent collision) {}
	
	protected void onInit() {}
	
	protected void onDeInit() {}
	
	protected void onEnable() {}
	
	protected void onDisable() {}
	
	protected void onUpdate(float deltaTime) {}
	
	public void onDeath(CollisionEvent event) {}
	
	public void onHurt(CollisionEvent event) {}
	
	public Entity getOwner() {
		return owner;
	}
}

