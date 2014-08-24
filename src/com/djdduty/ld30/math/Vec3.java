package com.djdduty.ld30.math;

import java.nio.FloatBuffer;

import org.lwjgl.BufferUtils;

public class Vec3 implements Vec<Vec3> {
	private float x, y, z;
	
	public static final Vec3 RIGHT = new Vec3(1, 0, 0);
	public static final Vec3 LEFT = new Vec3(-1, 0, 0);
	public static final Vec3 UP = new Vec3(0, 1, 0);
	public static final Vec3 DOWN = new Vec3(0, -1, 0);
	public static final Vec3 FORWARD = new Vec3(0, 0, -1);
	public static final Vec3 BACK = new Vec3(0, 0, 1);
	
	public Vec3() {
		set(0, 0, 0);
	}
	
	public Vec3(float v) {
		this(v, v, v);
	}
	
	public Vec3(float x, float y, float z) {
		set(x, y, z);
	}
	
	public Vec3(Vec2 vec) {
		set(vec);
	}
	
	public Vec3(Vec2 vec, float z) {
		set(vec, z);
	}
	
	public Vec3(Vec3 vec) {
		set(vec);
	}
	
	public Vec3(Vec4 vec) {
		set(vec);
	}
	
	@Override
	public Vec3 copy() {
		return new Vec3(this);
	}
	
	public float x() {
		return x;
	}
	
	public Vec3 x(float x) {
		this.x = x;
		return this;
	}
	
	public float y() {
		return y;
	}
	
	public Vec3 y(float y) {
		this.y = y;
		return this;
	}
	
	public float z() {
		return z;
	}
	
	public Vec3 z(float z) {
		this.z = z;
		return this;
	}
	
	@Override
	public boolean equals(Object o) {
		if(o instanceof Vec3) {
			Vec3 v = (Vec3)o;
			return x == v.x && y == v.y && z == v.z;
		}
		
		return false;
	}
	
	public Vec3 set(float x, float y, float z) {
		this.x = x;
		this.y = y;
		this.z = z;
		return this;
	}
	
	public Vec3 set(Vec2 vec) {
		return set(vec.x(), vec.y(), 0);
	}
	
	public Vec3 set(Vec2 vec, float z) {
		return set(vec.x(), vec.y(), z);
	}
	
	public Vec3 set(Vec3 vec) {
		return set(vec.x, vec.y, vec.z);
	}
	
	public Vec3 set(Vec4 vec) {
		return set(vec.x(), vec.y(), vec.z());
	}
	
	public Vec3 reset() {
		x = y = z = 0;
		return this;
	}
	
	@Override
	public float length() {
		return (float)Math.sqrt(x * x + y * y + z * z);
	}
	
	public Vec3 normalize() {
		float length = length();
		float x = this.x/length;
		float y = this.y/length;
		float z = this.z/length;
		return new Vec3(x,y,z);
	}
	
	public float dot(Vec3 vec) {
		return x * vec.x + y * vec.y + z * vec.z;
	}
	
	public Vec3 cross(Vec3 vec) {
		return new Vec3(y * vec.z - vec.y * z, z * vec.x - vec.z * x, x * vec.y - vec.x * y);
	}
	
	public Vec3 add(float x, float y, float z) {
		this.x += x;
		this.y += y;
		this.z += z;
		return this;
	}
	
	public Vec3 addNew(Vec3 vec) {
		return new Vec3(
				this.x + vec.x(),
				this.y + vec.y(),
				this.z + vec.z());
	}
	
	@Override
	public Vec3 add(Vec3 vec) {
		return add(vec.x, vec.y, vec.z);
	}
	
	public Vec3 sub(float x, float y, float z) {
		this.x -= x;
		this.y -= y;
		this.z -= z;
		return this;
	}
	
	@Override
	public Vec3 sub(Vec3 vec) {
		return sub(vec.x, vec.y, vec.z);
	}
	
	@Override
	public Vec3 mult(float f) {
		return mult(f, f, f);
	}
	
	public Vec3 mult(float x, float y, float z) {
		this.x *= x;
		this.y *= y;
		this.z *= z;
		return this;
	}
	
	@Override
	public Vec3 mult(Vec3 vec) {
		return mult(vec.x, vec.y, vec.z);
	}
	
	@Override
	public Vec3 divide(float f) {
		return divide(f, f, f);
	}
	
	public Vec3 divide(float x, float y, float z) {
		this.x /= x;
		this.y /= y;
		this.z /= z;
		return this;
	}
	
	@Override
	public Vec3 divide(Vec3 vec) {
		return divide(vec.x, vec.y, vec.z);
	}
	
	@Override
	public Vec3 mod(float f) {
		x %= f;
		y %= f;
		z %= f;
		
		return this;
	}
	
	@Override
	public String toString() {
		return "(" + x + ", " + y + ", " + z + ")";
	}
	
	private final static FloatBuffer direct = BufferUtils.createFloatBuffer(3);
	
	@Override
	public FloatBuffer toBuffer() {
		direct.clear();
		direct.put(x).put(y).put(z);
		direct.flip();
		return direct;
	}
}

