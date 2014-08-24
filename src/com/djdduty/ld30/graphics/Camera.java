package com.djdduty.ld30.graphics;

import com.djdduty.ld30.core.Engine;
import com.djdduty.ld30.math.Matrix4;
import com.djdduty.ld30.math.Vec3;
import com.djdduty.ld30.scene.entity.Entity;

public class Camera {
	private Vec3 position = new Vec3(0,0,0);
	private Matrix4 projection;
	private Matrix4 transform;
	private Entity attached;
	private Matrix4 orthoProjection;
	
	public Camera() {
		transform = new Matrix4().clearToIdentity();
		projection = new Matrix4().clearToPerspective(45.0f, Engine.get().getWidth(), Engine.get().getHeight(), 0.1f, 1000.0f);
		orthoProjection = new Matrix4().clearToOrtho(0, Engine.get().getWidth(), Engine.get().getHeight(), 0, -1, 1000);
	}
	
	public Matrix4 getTransform() {
		return transform;
	}
	
	public void setPosition(Vec3 pos) {
		position = pos;
		transform = new Matrix4().clearToIdentity().translate(position);
	}
	
	public Matrix4 getProjection() {
		return projection;
	}
	
	public Matrix4 getOrtho() {
		return orthoProjection;
	}
	
	public Vec3 getPosition() {
		return new Vec3(transform.matrix[12], transform.matrix[13], transform.matrix[14]);
	}
	
	public void attachToEntity(Entity e) {
		attached = e;
	}
	
	public void update(float deltaTime) {
		if(attached != null) {
			Vec3 ownerPos = attached.getPosition();
			setPosition(new Vec3(-ownerPos.x(), -ownerPos.y(), -ownerPos.z()));
		} else {
			setPosition(position);
		}
	}
}
