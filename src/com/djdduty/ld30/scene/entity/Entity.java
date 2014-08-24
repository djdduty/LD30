package com.djdduty.ld30.scene.entity;

import com.djdduty.ld30.core.Engine;
import com.djdduty.ld30.graphics.Renderable;
import com.djdduty.ld30.math.Matrix4;
import com.djdduty.ld30.math.Rectangle;
import com.djdduty.ld30.math.Vec2;
import com.djdduty.ld30.math.Vec3;
import com.djdduty.ld30.scene.Scene;

public class Entity {
	protected String name;

    protected Vec3 position = new Vec3(0,0,0);
    private Vec3 velocity = new Vec3(0,0,0);

    protected float health = 100.0f;
    protected float maxHealth = 100.0f;

    protected Scene scene;
    protected Renderable renderable;
    
    public float mass = 1.0f;

    private boolean enabled = false;
    private boolean initialized = false;
    
    protected Rectangle bounds;
    
    private EntityController controller;
    
    public boolean isProjectile = false;
    public boolean isDead = false;
    public int score = 0;
    
    public boolean isCollidable = true;

    public Entity(Vec3 pos, String name) {
        position = pos;
        this.name = name;
    }
    
    public Entity(Vec3 pos, String name, EntityController controller) {
    	this(pos, name);
    	this.controller = controller;
    }

    public final void init(Scene scene) {
        if(!initialized) {
            this.scene = scene;
            onInit();
            if(renderable != null)
                renderable.init();
            if(controller != null)
            	controller.init(this);
            enable();
            initialized = true;
        }
    }

    public void setVelocity(Vec3 vel) {
        velocity = vel;
    }

    public final void update(float deltaTime) {  
    	if(initialized)
    		onUpdate(deltaTime);
    	if(initialized && enabled) {
    	    if(controller != null)
    	    	controller.update(deltaTime);
	  
	    	if(velocity.x() != 0 && scene != null) {
	    		float oldX = position.x();
		    	position.x(position.x() + (velocity.x()*(deltaTime*0.001f)));
		    	Entity e = scene.checkCollision(this);
		    	if(e != null) {
		    		CollisionEvent event = new CollisionEvent(e, position);
		    		event.otherVelocity = velocity;
		    		onCollision(event);
		    		position.x(oldX-velocity.normalize().x());
		    		velocity.x(0);
		    	}
	    	}
	    	
	    	if(velocity.y() != 0 && scene != null) {
	    		float oldY = position.y();
		    	position.y(position.y() + (velocity.y()*(deltaTime*0.001f)));
		    	Entity e = scene.checkCollision(this);
		    	if(e != null) {
		    		CollisionEvent event = new CollisionEvent(e, position);
		    		event.onYAxis = true;
		    		event.otherVelocity = velocity;
		    		onCollision(event);
		    		position.y(oldY-velocity.normalize().y());
		    		velocity.y(0);
		    	}
	    	}
	    	velocity.y(velocity.y() + ((500.0f * mass)*(deltaTime*0.001f)));
	
	        if(renderable != null)
	        	renderable.setPosition(position);
	        
	        if(bounds != null) {
	        	bounds.setPosition(new Vec2(position.x(), position.y()));
	        	//System.out.println(bounds.getPosition().x()+", "+bounds.getPosition().y());
	        }
    	}
    }

    public void hurt(CollisionEvent event) {
        health -= event.damage;
        
        if(controller != null && health > 0)
        	controller.onHurt(event);
        
        if(health <= 0 && !isDead)
            die(event);
        if(health >= maxHealth)
            health = maxHealth;
    }

    private final void die(CollisionEvent event) {
    	onDeath(event);
    	if(controller != null)
    		controller.onDeath(event);
    	isDead = true;
    	System.out.println(name+" died!");
    }

    public final void deInit() {
        if(initialized) {
            onDeInit();
            if(renderable != null)
            	renderable.deInit();
            if(controller != null)
            	controller.deInit();
            initialized = false;
        }
    }

    public final void enable() {
        if(!enabled) {
            onEnable();
            if(renderable != null)
            	renderable.enable();
            if(controller != null) 
            	controller.enable();
            enabled = true;
        }
    }

    public final void disable() {
        if(enabled) {
            onDisable();
            if(renderable != null)
            	renderable.disable();
            if(controller != null)
            	controller.disable();
            enabled = false;
        }
    }
    
    public final void collide(CollisionEvent event) {
    	onCollision(event);
    	if(controller != null)
    		controller.onCollision(event);
    }

    protected void onUpdate(float deltaTime) {}
    
    protected void onInit() {}
    
    protected void onEnable() {}
    
    protected void onDisable() {}
    
    protected void onDeInit() {}
    
    protected void onCollision(CollisionEvent event) {}
    
    protected void onDeath(CollisionEvent killingEvent) {}

    public String getName() {
    	return name;
    }
    
    public Scene getScene() {
    	return scene;
    }
    
    public float getHealth() {
    	return health;
    }
    
    public boolean getEnabled() {
    	return enabled;
    }
    
    public Vec3 getPosition() {
    	return position;
    }
    
    public void setPosition(Vec3 pos) {
    	this.position = pos;
    }
    
    public Vec3 getVelocity() {
    	return velocity;
    }
    
    public boolean getInitialized() {
    	return initialized;
    }
    
    public Renderable getRenderable() {
    	return renderable;
    }
    
    public EntityController getController() {
    	return controller;
    }
    
	public void queueForRender() {
		if(renderable != null && initialized)
			Engine.get().getRenderer().addRenderable(renderable);
	}
	
	public Rectangle getBounds() {
		if(bounds != null)
			return bounds;
		else
			return null;
	}
	
	public boolean collides(Entity other) {
		if(bounds == null)
			return false;
		
		if(other.getBounds().interescts(bounds)) {
			other.onCollision(new CollisionEvent(this, position));
			return true;
		}
		
		return false;
	}
	
	public void addScore(int num) {
		score += num;
	}
	
	public int getScore() {
		return score;
	}

	public void setHealth(int health, int max) {
		this.health = health;
		this.maxHealth = max;
	}

	public double getMaxHealth() {
		return this.maxHealth;
	}

	public void heal(int i) {
		this.health += i;
		if(health > maxHealth)
			health = maxHealth;
	}
}
