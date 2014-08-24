package com.djdduty.ld30.game.entities;

import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;

import com.djdduty.ld30.core.Engine;
import com.djdduty.ld30.math.Vec2;
import com.djdduty.ld30.math.Vec3;
import com.djdduty.ld30.scene.entity.CollisionEvent;
import com.djdduty.ld30.scene.entity.EntityController;
import com.djdduty.ld30.scene.entity.Projectile;

public class CopterController extends EntityController {
	private GameEntity player;
	private int damage = 10;
	private int numProjectiles = 0;
	private float shootTimer = 0;
	public float shootDelay = 2;
	private float animationTimer = 0;
	private int frame = 0;
	
	public CopterController(GameEntity player) {
		this.player = player;
	}
	
	public void onInit() {
		owner.setHealth(20, 20);
	}
	
	public void onUpdate(float deltaTime) {
		Vec3 vel = new Vec3(0,0,0);
		float deltInSeconds = deltaTime * 0.001f;
		shootTimer += deltInSeconds;
		animationTimer += deltInSeconds;
		
		if(animationTimer >= 0.5) {
			frame++;
			animationTimer = 0;
			if(frame > 1)
				frame = 0;
			owner.getRenderable().getMaterial().setUvOffset(new Vec2(frame*0.5f,0));
		}
		
		
		
		if(!owner.isDead) {
			vel.x(owner.getVelocity().x() - (owner.getVelocity().x()*(0.5f*deltInSeconds)));
			vel.y(owner.getVelocity().y() - (owner.getVelocity().y()*(0.5f*deltInSeconds)));
				vel.x(vel.x() + -(80.0f*deltInSeconds));
				
			if(shootTimer >= shootDelay) {
				Vec2 direction = new Vec2();
				float distance = player.getPosition().x() - owner.getPosition().x();
				direction.x((player.getPosition().x()+player.getBounds().getSize().x()/2) - (owner.getPosition().x()+owner.getBounds().getSize().x()/2));
				direction.y((player.getPosition().y()+player.getBounds().getSize().y()/2+(distance*0.5f)) - (owner.getPosition().y()+owner.getBounds().getSize().y()/2));
				direction = direction.normalize();
				Vec3 projVel = new Vec3(direction.x()*200, direction.y()*200, 0);
				Projectile proj = new Projectile(new Vec3(-800,0,0), new Vec3(0,0,0), "PlayerProj"+numProjectiles, damage, new Vec2(16,16), null);
				owner.getScene().addProjectile(proj);
				Engine.get().getSoundManager().getSound("shoot").playAsSoundEffect(1, 1, false);
				proj.setOwner(owner);
				proj.getRenderable().getMaterial().setDiffuseTexture("cannonBall");
				proj.setVelocity(projVel);
				if((player.getPosition().x()+player.getBounds().getSize().x()/2) < owner.getPosition().x()+owner.getBounds().getSize().y()/2)
					proj.setPosition(new Vec3(
							owner.getPosition().x()-proj.getBounds().getSize().x(),
							owner.getPosition().y()+(owner.getBounds().getSize().y()/2),
							0));
				else
					proj.setPosition(new Vec3(
							owner.getPosition().x()+owner.getBounds().getSize().x(),
							owner.getPosition().y()+(owner.getBounds().getSize().y()/2),
							0));
				
				proj.mass = 0.1f;
				proj.collided = false;
				shootTimer = 0;
			}
		} else
			owner.deInit();
		
		if(owner.getPosition().x() <= 0) {
			Engine.get().numStrikes++;
			owner.deInit();
		}
		
		if(vel.x() <= -100)
			vel.x(-100);
		
		owner.setVelocity(vel);
	}
	
	public void onCollision(CollisionEvent event) {
		
	}
	
	public void onDeath(CollisionEvent event) {
		event.owner.addScore(150);
		Engine.get().getSoundManager().getSound("explosion").playAsSoundEffect(1, 1, false);
	}
	
	public void onHurt(CollisionEvent event) {
		animationTimer = 0;
		owner.getRenderable().getMaterial().setUvOffset(new Vec2(0.0f,0.5f));
		Engine.get().getSoundManager().getSound("hurt").playAsSoundEffect(1, 1, false);
	}
}