package com.djdduty.ld30.scene;

import com.djdduty.ld30.core.Engine;
import com.djdduty.ld30.graphics.Renderable;
import com.djdduty.ld30.math.Matrix4;
import com.djdduty.ld30.math.Vec3;

public class SceneObject {
	protected Vec3 position;
	
	protected String name;
	
	protected Scene scene;
	protected Renderable renderable;
	
	private boolean enabled = false;
	private boolean initialized = false;
	
	public SceneObject(Vec3 position, String name) {
		this.position = position;
		this.name = name;
	}
	
	public final void init(Scene scene) {
		if(!initialized) {
			this.scene = scene;
			onInit();
			if(renderable != null)
				renderable.init();
			enable();
			initialized = true;
		}
	}
	
	public final void deInit() {
		if(initialized) {
			onDeInit();
			if(renderable != null)
				renderable.deInit();
			initialized = false;
			disable();
		}
	}
	
	public final void enable() {
		if(!enabled) {
			onEnable();
			if(renderable != null)
				renderable.enable();
			enabled = true;
		}
	}
	
	public final void disable() {
		if(enabled) {
			onDisable();
			if(renderable != null)
				renderable.disable();
			enabled = false;
		}
	}
	
	protected void onInit() {}
	
	protected void onDeInit() {}
	
	protected void onEnable() {}
	
	protected void onDisable() {}
	
	public String getName() {
		return name;
	}
	
	public Scene getScene() {
		return scene;
	}
	
	public boolean getEnabled() {
		return enabled;
	}
	
	public boolean getInitialized() {
		return initialized;
	}
	
	public void setPosition(Vec3 position) {
		
	}
	
	public Vec3 getPosition() {
		return position;
	}
	
	public Renderable getRenderable() {
		return renderable;
	}

	public Matrix4 getTransform() {
		return new Matrix4().clearToIdentity().translate(position);
	}
	
	public void queueForRender() {
		if(renderable != null)
			Engine.get().getRenderer().addRenderable(renderable);
	}
}
