package com.djdduty.ld30.game.entities;

import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;

import com.djdduty.ld30.core.Engine;
import com.djdduty.ld30.math.Vec2;
import com.djdduty.ld30.math.Vec3;
import com.djdduty.ld30.scene.entity.CollisionEvent;
import com.djdduty.ld30.scene.entity.EntityController;
import com.djdduty.ld30.scene.entity.Projectile;

public class PlayerController extends EntityController {
	int numProjectiles = 0;
	private float shootTimer = 0;
	public float shootDelay = 1;
	
	public int damage = 10;
	
	public PlayerController() {
	}
	
	public void onUpdate(float deltaTime) {
		float deltInSeconds = deltaTime*0.001f;
		shootTimer += deltInSeconds;
		Vec3 vel = new Vec3();
		
		vel.x(owner.getVelocity().x() - (owner.getVelocity().x()*(0.5f*deltInSeconds)));
		vel.y(owner.getVelocity().y() - (owner.getVelocity().y()*(0.5f*deltInSeconds)));
		if(Keyboard.isKeyDown(Keyboard.KEY_LEFT) || Keyboard.isKeyDown(Keyboard.KEY_A))
			vel.x(vel.x() + -(160.0f*deltInSeconds));
		
		if(Keyboard.isKeyDown(Keyboard.KEY_RIGHT) || Keyboard.isKeyDown(Keyboard.KEY_D))
			vel.x(vel.x() + (160.0f*deltInSeconds));
		
		if(Keyboard.isKeyDown(Keyboard.KEY_UP) || Keyboard.isKeyDown(Keyboard.KEY_W)) {
			vel.y(vel.y() + -(160.0f*deltInSeconds));
		}
		
		if(Keyboard.isKeyDown(Keyboard.KEY_DOWN) || Keyboard.isKeyDown(Keyboard.KEY_S)) {
			vel.y(vel.y() + (160.0f*deltInSeconds));
		}
		
		if(Mouse.isButtonDown(0) && shootTimer >= shootDelay) {
			Vec2 direction = new Vec2();
			direction.x(Mouse.getX() - (owner.getPosition().x()+owner.getBounds().getSize().x()/2));
			direction.y(((Mouse.getY()-Engine.get().getHeight())*-1) - (owner.getPosition().y()+owner.getBounds().getSize().y()/2));
			direction = direction.normalize();
			Vec3 projVel = new Vec3(direction.x()*500, direction.y()*500, 0);
			Projectile proj = new Projectile(new Vec3(-800,0,0), new Vec3(0,0,0), "PlayerProj"+numProjectiles, damage, new Vec2(16,16), null);
			owner.getScene().addProjectile(proj);
			proj.setOwner(owner);
			proj.getRenderable().getMaterial().setDiffuseTexture("cannonBall");
			proj.setVelocity(projVel);
			if(Mouse.getX() < owner.getPosition().x()+owner.getBounds().getSize().y()/2)
				proj.setPosition(new Vec3(
						owner.getPosition().x()-proj.getBounds().getSize().x(),
						owner.getPosition().y()+(owner.getBounds().getSize().y()/2),
						0));
			else
				proj.setPosition(new Vec3(
						owner.getPosition().x()+owner.getBounds().getSize().x(),
						owner.getPosition().y()+(owner.getBounds().getSize().y()/2),
						0));
			
			proj.mass = 1.0f;
			proj.collided = false;
			shootTimer = 0;
		}
		
		if(vel.x() > 150)
			vel.x(100);
		if(vel.y() > 150)
			vel.y(100);
		if(vel.x() < -150)
			vel.x(-100);
		if(vel.y() < -150)
			vel.y(-100);
		
		owner.setVelocity(vel);
	}
	
	public void onCollision(CollisionEvent event) {
		
	}
}
