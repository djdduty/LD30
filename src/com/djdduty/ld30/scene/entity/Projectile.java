package com.djdduty.ld30.scene.entity;

import java.util.ArrayList;

import com.djdduty.ld30.graphics.Renderable;
import com.djdduty.ld30.graphics.Vertex;
import com.djdduty.ld30.math.Rectangle;
import com.djdduty.ld30.math.Vec2;
import com.djdduty.ld30.math.Vec3;

public class Projectile extends Entity {
	private int damage;
	private Vec2 size;
	private Entity owner;
	public boolean collided = false;
	private float deathTimer = 0;
	public boolean onGround = false;
	
	public Projectile(Vec3 startingPos, Vec3 velocity, String name, Entity owner) {
		this(startingPos, velocity, name, 10, owner);
	}
	
	public Projectile(Vec3 startingPos, Vec3 velocity, String name, int damage, Entity owner) {
		this(startingPos, velocity, name, damage, new Vec2(10,10), owner);
	}
	
	public Projectile(Vec3 startingPos, Vec3 velocity, String name, int damage, Vec2 size, Entity owner) {
		super(startingPos, name);
		setVelocity(velocity);
		this.damage = damage;
		this.size = size;
		this.owner = owner;
		isProjectile = true;
	}
	
	public void onUpdate(float deltaTime) {
		if(collided) {
			deathTimer += deltaTime * 0.001f;
			isCollidable = false;
		}
		
		if(onGround)
			disable();
		
		if(deathTimer >= 5.0)
			scene.removeProjectile(name);
	}
	
	public void onCollision(CollisionEvent event) {
		if(!collided && event.owner.getName() != name && event.owner.getName() != owner.getName()) {
			CollisionEvent  dmgevent = new CollisionEvent(owner, new Vec3(position));
			dmgevent.damage = damage;
			event.owner.hurt(dmgevent);
			collided = true;
			if(event.owner.name == "groundEntity") {
				onGround = true;
			}
		} else if(event.owner.getName() == name) {
			collided = true;
		}
	}
	
	public void onInit() {
		renderable = new Renderable(name + "-Renderer");

		//Mesh Information
		ArrayList<Vertex> vertices = new ArrayList<>();
		ArrayList<Integer> indices = new ArrayList<>();

		Vertex v1 = new Vertex(new Vec3(0,0,-1),				new Vec3(0,0,1),new Vec2(0   ,0));
		Vertex v2 = new Vertex(new Vec3(size.x(),0,-1),			new Vec3(0,0,1),new Vec2(1   ,0));
		Vertex v3 = new Vertex(new Vec3(size.x(),size.y(),-1),	new Vec3(0,0,1),new Vec2(1   ,1));
        Vertex v4 = new Vertex(new Vec3(0, size.y(),-1),		new Vec3(0,0,1),new Vec2(0  , 1));

		vertices.add(v1);
		vertices.add(v2);
		vertices.add(v3);
		vertices.add(v4);

		indices.add(0);
		indices.add(1);
		indices.add(2);
		
		indices.add(2);
		indices.add(3);
		indices.add(0);
		//

		renderable.setData(vertices, indices);
		bounds = new Rectangle(new Vec2(position.x(), position.y()), size);
	}
	
	public void setOwner(Entity e) {
		this.owner = e;
	}

	public Entity getOwner() {
		return owner;
	}
}
