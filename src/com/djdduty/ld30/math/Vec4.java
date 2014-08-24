package com.djdduty.ld30.math;

import java.nio.FloatBuffer;

import org.lwjgl.BufferUtils;

public class Vec4 implements Vec<Vec4> {
	private float x, y, z, w;
	
	public static final Vec4 RIGHT = new Vec4(1, 0, 0, 1);
	public static final Vec4 LEFT = new Vec4(-1, 0, 0, 1);
	public static final Vec4 UP = new Vec4(0, 1, 0, 1);
	public static final Vec4 DOWN = new Vec4(0, -1, 0, 1);
	public static final Vec4 FORWARD = new Vec4(0, 0, -1, 1);
	public static final Vec4 BACK = new Vec4(0, 0, 1, 1);
	
	public Vec4() {
		set(0, 0, 0, 0);
	}
	
	public Vec4(float v) {
		this(v, v, v, v);
	}
	
	public Vec4(float x, float y, float z, float w) {
		set(x, y, z, w);
	}
	
	public Vec4(Vec2 vec) {
		set(vec);
	}
	
	public Vec4(Vec2 vec, float z, float w) {
		set(vec, z, w);
	}
	
	public Vec4(Vec3 vec) {
		set(vec);
	}
	
	public Vec4(Vec3 vec, float w) {
		set(vec, w);
	}
	
	public Vec4(Vec4 vec) {
		set(vec);
	}
	
	@Override
	public Vec4 copy() {
		return new Vec4(this);
	}
	
	public float x() {
		return x;
	}
	
	public Vec4 x(float x) {
		this.x = x;
		return this;
	}
	
	public float y() {
		return y;
	}
	
	public Vec4 y(float y) {
		this.y = y;
		return this;
	}
	
	public float z() {
		return z;
	}
	
	public Vec4 z(float z) {
		this.z = z;
		return this;
	}
	
	public float w() {
		return w;
	}
	
	public Vec4 w(float w) {
		this.w = w;
		return this;
	}
	
	@Override
	public boolean equals(Object o) {
		if(o instanceof Vec4) {
			Vec4 v = (Vec4)o;
			return x == v.x && y == v.y && z == v.z && w == v.w;
		}
		
		return false;
	}
	
	public Vec4 set(float x, float y, float z, float w) {
		this.x = x;
		this.y = y;
		this.z = z;
		this.w = w;
		return this;
	}
	
	public Vec4 set(Vec2 vec) {
		return set(vec, 0, 0);
	}
	
	public Vec4 set(Vec2 vec, float z, float w) {
		return set(vec.x(), vec.y(), z, w);
	}
	
	public Vec4 set(Vec3 vec) {
		return set(vec, 0);
	}
	
	public Vec4 set(Vec3 vec, float w) {
		return set(vec.x(), vec.y(), vec.z(), w);
	}
	
	public Vec4 set(Vec4 vec) {
		return set(vec.x, vec.y, vec.z, vec.w);
	}
	
	public Vec4 reset() {
		x = y = z = w = 0;
		return this;
	}
	
	@Override
	public float length() {
		return (float)Math.sqrt(x * x + y * y + z * z + w * w);
	}
	
	public Vec4 normalize() {
		float length = length();
		x /= length;
		y /= length;
		z /= length;
		w /= length;
		return this;
	}
	
	public float dot(Vec4 vec) {
		return x * vec.x + y * vec.y + z * vec.z + w * vec.w;
	}
	
	public Vec4 add(float x, float y, float z, float w) {
		this.x += x;
		this.y += y;
		this.z += z;
		this.w += w;
		return this;
	}
	
	@Override
	public Vec4 add(Vec4 vec) {
		return add(vec.x, vec.y, vec.z, vec.w);
	}
	
	public Vec4 sub(float x, float y, float z, float w) {
		this.x -= x;
		this.y -= y;
		this.z -= z;
		this.w -= w;
		return this;
	}
	
	@Override
	public Vec4 sub(Vec4 vec) {
		return sub(vec.x, vec.y, vec.z, vec.w);
	}
	
	@Override
	public Vec4 mult(float f) {
		return mult(f, f, f, f);
	}
	
	public Vec4 mult(float x, float y, float z, float w) {
		this.x *= x;
		this.y *= y;
		this.z *= z;
		this.w *= w;
		return this;
	}
	
	@Override
	public Vec4 mult(Vec4 vec) {
		return mult(vec.x, vec.y, vec.z, vec.w);
	}
	
	@Override
	public Vec4 divide(float f) {
		return divide(f, f, f, f);
	}
	
	public Vec4 divide(float x, float y, float z, float w) {
		this.x /= x;
		this.y /= y;
		this.z /= z;
		this.w /= w;
		return this;
	}
	
	@Override
	public Vec4 divide(Vec4 vec) {
		return divide(vec.x, vec.y, vec.z, vec.w);
	}
	
	@Override
	public Vec4 mod(float f) {
		x %= f;
		y %= f;
		z %= f;
		w %= f;
		
		return this;
	}
	
	@Override
	public String toString() {
		return "(" + x + ", " + y + ", " + z + ", " + w + ")";
	}
	
	private final static FloatBuffer direct = BufferUtils.createFloatBuffer(4);
	
	@Override
	public FloatBuffer toBuffer() {
		direct.clear();
		direct.put(x).put(y).put(z).put(w);
		direct.flip();
		return direct;
	}
}
