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
	private int fireRateLevel = 1;
	private int fireRateCost = 100;
	
	private int damage = 10;
	private int damageLevel = 1;
	private int damageCost = 100;
	
	private int maxHealth = 100;
	private int healthLevel = 1;
	private int healthCost = 100;
	
	private float acceleration = 100;
	private int accelerationLevel = 1;
	private int accelerationCost = 100;
	
	public PlayerController() {
	}
	
	public void onUpdate(float deltaTime) {
		float deltInSeconds = deltaTime*0.001f;
		shootTimer += deltInSeconds;
		Vec3 vel = new Vec3();
		
		vel.x(owner.getVelocity().x() - (owner.getVelocity().x()*(0.5f*deltInSeconds)));
		vel.y(owner.getVelocity().y() - (owner.getVelocity().y()*(0.5f*deltInSeconds)));
		if(Keyboard.isKeyDown(Keyboard.KEY_LEFT) || Keyboard.isKeyDown(Keyboard.KEY_A))
			vel.x(vel.x() + -(acceleration*deltInSeconds));
		
		if(Keyboard.isKeyDown(Keyboard.KEY_RIGHT) || Keyboard.isKeyDown(Keyboard.KEY_D))
			vel.x(vel.x() + (acceleration*deltInSeconds));
		
		if(Keyboard.isKeyDown(Keyboard.KEY_UP) || Keyboard.isKeyDown(Keyboard.KEY_W)) {
			vel.y(vel.y() + -(acceleration*deltInSeconds));
		}
		
		if(Keyboard.isKeyDown(Keyboard.KEY_DOWN) || Keyboard.isKeyDown(Keyboard.KEY_S)) {
			vel.y(vel.y() + (acceleration*deltInSeconds));
		}
		
		if(Mouse.isButtonDown(0) && shootTimer >= shootDelay) {
			Vec2 direction = new Vec2();
			direction.x(Mouse.getX() - (owner.getPosition().x()+owner.getBounds().getSize().x()/2));
			direction.y(((Mouse.getY()-Engine.get().getHeight())*-1) - (owner.getPosition().y()+owner.getBounds().getSize().y()/2));
			direction = direction.normalize();
			Vec3 projVel = new Vec3(direction.x()*500, direction.y()*500, 0);
			Projectile proj = new Projectile(new Vec3(-800,0,0), new Vec3(0,0,0), "PlayerProj"+numProjectiles, damage, new Vec2(16,16), null);
			owner.getScene().addProjectile(proj);
			Engine.get().getSoundManager().getSound("shoot").playAsSoundEffect(1, 1, false);
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
	
	public void onDeath(CollisionEvent event) {
		Engine.get().getSoundManager().getSound("explosion").playAsSoundEffect(1, 1, false);
	}
	
	public void onHurt(CollisionEvent event) {
		Engine.get().getSoundManager().getSound("hurt").playAsSoundEffect(1, 1, false);
	}
	
	public int getHealCost() {
		return (int)(owner.getMaxHealth() * 0.1);
	}
	
	public int getHealthUpCost() {
		return healthCost;
	}
	
	public void heal() {
		if(owner.score >= getHealCost()) {
			Engine.get().getSoundManager().getSound("powerup").playAsSoundEffect(1, 1, false);
			owner.heal((int)(owner.getMaxHealth()*0.1));
			owner.score -= getHealCost();
		} else {
			Engine.get().getSoundManager().getSound("fail").playAsSoundEffect(1, 1, false);
		}
	}
	
	public void upHealth() {
		if(owner.score >= healthCost) {
			Engine.get().getSoundManager().getSound("powerup").playAsSoundEffect(1, 1, false);
			owner.setHealth((int)owner.getHealth(), (int)(owner.getMaxHealth()*1.5));
			owner.score -= getHealthUpCost();
			healthCost *= 1.5;
		} else {
			Engine.get().getSoundManager().getSound("fail").playAsSoundEffect(1, 1, false);
		}
	}

	public int getAccUpCost() {
		return accelerationCost;
	}
	
	public void upAcc() {
		if(owner.score >= accelerationCost) {
			Engine.get().getSoundManager().getSound("powerup").playAsSoundEffect(1, 1, false);
			acceleration += 20;
			owner.score -= getAccUpCost();
			accelerationCost *= 1.5;
		} else {
			Engine.get().getSoundManager().getSound("fail").playAsSoundEffect(1, 1, false);
		}
	}
	
	public int getFireRateUpCost() {
		return fireRateCost;
	}
	
	public void upFireRate() {
		if(owner.score >= fireRateCost) {
			Engine.get().getSoundManager().getSound("powerup").playAsSoundEffect(1, 1, false);
			shootDelay *= 0.9;
			owner.score -= getFireRateUpCost();
			fireRateCost *= 1.5;
		} else {
			Engine.get().getSoundManager().getSound("fail").playAsSoundEffect(1, 1, false);
		}
	}
	
	public int getDamageUpCost() {
		return damageCost;
	}
	
	public void upDamage() {
		if(owner.score >= damageCost) {
			Engine.get().getSoundManager().getSound("powerup").playAsSoundEffect(1, 1, false);
			damage += 10;
			owner.score -= getDamageUpCost();
			damageCost *= 1.5;
		} else {
			Engine.get().getSoundManager().getSound("fail").playAsSoundEffect(1, 1, false);
		}
	}
}
