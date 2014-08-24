package com.djdduty.ld30.scene.entity;

import com.djdduty.ld30.math.Vec3;

public class CollisionEvent {
	public int damage = 0;
	public Vec3 location;
	public Entity owner;
	public boolean onYAxis = false;
	public Vec3 otherVelocity = new Vec3();
	
	public CollisionEvent(Entity owner, Vec3 location) {
		this.owner = owner;
		this.location = location;
	}
}
