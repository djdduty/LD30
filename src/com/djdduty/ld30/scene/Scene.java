package com.djdduty.ld30.scene;

import java.util.ArrayList;
import java.util.HashMap;

import com.djdduty.ld30.scene.entity.Entity;
import com.djdduty.ld30.scene.entity.Projectile;

public class Scene {
	private HashMap<String, SceneObject> objects = new HashMap<>();
	private HashMap<String, Entity> entities = new HashMap<>();
	private ArrayList<Projectile> projectiles = new ArrayList<>();
	
	public Scene() {
		
	}
	
	public void addObject(SceneObject object) {
		objects.put(object.getName(), object);
		object.init(this);
	}
	
	public void removeObject(String name) {
		objects.remove(name);
	}
	
	public void addProjectile(Projectile p) {
		projectiles.add(p);
		p.init(this);
	}
	
	public void addEntity(Entity entity) {
		entities.put(entity.getName(), entity);
		entity.init(this);
	}
	
	public void removeEntity(String name) {
		entities.remove(name);
	}
	
	public void update(float deltaTime) {
		for(Entity e : entities.values())
			e.update(deltaTime);
		
		for(int i = 0; i < projectiles.size(); i++)
			projectiles.get(i).update(deltaTime);
	}
	
	public void queueForRender() {
		for(Entity e : entities.values())
			e.queueForRender();
		
		for(SceneObject o : objects.values())
			o.queueForRender();
		
		for(int i = 0; i < projectiles.size(); i++)
			projectiles.get(i).queueForRender();
	}
	
	public Entity checkCollision(Entity entity) {
		for(int i = 0; i < projectiles.size(); i++)
			if(projectiles.get(i).isCollidable && projectiles.get(i).getEnabled() && !entity.isProjectile && projectiles.get(i).getOwner().getName() != entity.getName() && projectiles.get(i).getName() != entity.getName() && entity.collides(projectiles.get(i))) {
				return projectiles.get(i);
			}
		
		for(Entity e : entities.values())
			if(e.isCollidable && e.getName() != "playerEntity" && e.getInitialized() && entity.getName() != e.getName() && entity.collides(e)) {
				return e;
			}
		
		return null;
	}

	public void removeProjectile(String name) {
		for(int i = 0; i < projectiles.size(); i++) {
			if(projectiles.get(i).getName() == name) {
				projectiles.remove(i);
				return;
			}
		}
	}
}
