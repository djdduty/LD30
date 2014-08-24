package com.djdduty.ld30.graphics;

import com.djdduty.ld30.math.Vec2;
import com.djdduty.ld30.math.Vec3;

public class Vertex {
	public Vec3 position = new Vec3(0, 0, 0);
	public Vec3 normal = new Vec3(0, 0, 0);
	public Vec2 uv = new Vec2(0, 0);
	public Vec3 tangent;
	
	public Vertex() {};
	
	public Vertex(Vec3 pos) {
		position = pos;
	}
	
	public Vertex(Vec3 pos, Vec3 norm) {
		position = pos;
		normal = norm;
	}
	
	public Vertex(Vec3 pos, Vec3 norm, Vec2 uv) {
		position = pos;
		normal = norm;
		this.uv = uv;
	}
	
	public Vertex(Vec3 pos, Vec3 norm, Vec2 uv, Vec3 tang) {
		position = pos;
		normal = norm;
		this.uv = uv;
		tangent = tang;
	}
}

