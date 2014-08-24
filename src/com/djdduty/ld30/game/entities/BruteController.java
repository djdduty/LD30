package com.djdduty.ld30.game.entities;

import com.djdduty.ld30.core.Engine;
import com.djdduty.ld30.math.Vec2;
import com.djdduty.ld30.math.Vec3;
import com.djdduty.ld30.scene.entity.CollisionEvent;
import com.djdduty.ld30.scene.entity.EntityController;

public class BruteController extends EntityController {
	private float animationTimer = 0;
	private int frame = 0;
	private int numBrutes = 0;
	private float speed = 100.0f;
	
	public BruteController(int nc) {
		numBrutes = nc;
	}
	
	public void onInit() {
		int add = numBrutes*2;
		owner.setHealth(10+(add*2), 10+(add*2));
		speed += add;
	}
	
	public void onUpdate(float deltaTime) {
		Vec3 vel = new Vec3(0,0,0);
		
		animationTimer += (deltaTime*0.001f);
		
		if(animationTimer >= 0.5) {
			frame++;
			animationTimer = 0;
			if(frame > 1)
				frame = 0;
			owner.getRenderable().getMaterial().setUvOffset(new Vec2(frame*0.5f,0));
		}
		
		
		if(!owner.isDead)
			vel.x(-speed);
		else
			owner.deInit();
		
		if(owner.getPosition().x() <= 0) {
			Engine.get().numStrikes++;
			owner.deInit();
		}
		
		owner.setVelocity(vel);
	}
	
	public void onCollision(CollisionEvent event) {
		
	}
	
	public void onDeath(CollisionEvent event) {
		event.owner.addScore(50);
		Engine.get().getSoundManager().getSound("explosion").playAsSoundEffect(1, 1, false);
	}
	
	public void onHurt(CollisionEvent event) {
		animationTimer = 0;
		owner.getRenderable().getMaterial().setUvOffset(new Vec2(0.0f,0.5f));
		Engine.get().getSoundManager().getSound("hurt").playAsSoundEffect(1, 1, false);
	}
}