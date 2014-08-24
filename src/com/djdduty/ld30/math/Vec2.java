package com.djdduty.ld30.math;

import java.nio.FloatBuffer;

import org.lwjgl.BufferUtils;

public class Vec2 implements Vec<Vec2> {
	private float x, y;
	
	public static final Vec2 RIGHT = new Vec2(1, 0);
	public static final Vec2 LEFT = new Vec2(-1, 0);
	public static final Vec2 UP = new Vec2(0, 1);
	public static final Vec2 DOWN = new Vec2(0, -1);
	
	public Vec2() {
		this(0, 0);
	}
	
	public Vec2(float v) {
		this(v, v);
	}
	
	public Vec2(float x, float y) {
		set(x, y);
	}
	
	public Vec2(Vec2 vec) {
		set(vec);
	}
	
	public Vec2(Vec3 vec) {
		set(vec);
	}
	
	public Vec2(Vec4 vec) {
		set(vec);
	}
	
	@Override
	public Vec2 copy() {
		return new Vec2(this);
	}
	
	public float x() {
		return x;
	}
	
	public Vec2 x(float x) {
		this.x = x;
		return this;
	}
	
	public float y() {
		return y;
	}
	
	public Vec2 y(float y) {
		this.y = y;
		return this;
	}
	
	@Override
	public boolean equals(Object o) {
		if(o instanceof Vec2) {
			Vec2 v = (Vec2)o;
			return x == v.x && y == v.y;
		}
		
		return false;
	}
	
	public Vec2 set(float x, float y) {
		this.x = x;
		this.y = y;
		return this;
	}
	
	public Vec2 set(Vec2 vec) {
		return set(vec.x, vec.y);
	}
	
	public Vec2 set(Vec3 vec) {
		return set(vec.x(), vec.y());
	}
	
	public Vec2 set(Vec4 vec) {
		return set(vec.x(), vec.y());
	}
	
	public Vec2 reset() {
		x = y = 0;
		return this;
	}
	
	@Override
	public float length() {
		return (float)Math.sqrt(x * x + y * y);
	}
	
	public Vec2 normalize() {
		float length = length();
		x /= length;
		y /= length;
		return this;
	}
	
	public float dot(Vec2 vec) {
		return x * vec.x + y * vec.y;
	}
	
	public Vec2 add(float x, float y) {
		this.x += x;
		this.y += y;
		return this;
	}
	
	@Override
	public Vec2 add(Vec2 vec) {
		return add(vec.x, vec.y);
	}
	
	public Vec2 sub(float x, float y) {
		this.x -= x;
		this.y -= y;
		return this;
	}
	
	@Override
	public Vec2 sub(Vec2 vec) {
		return sub(vec.x, vec.y);
	}
	
	@Override
	public Vec2 mult(float f) {
		return mult(f, f);
	}
	
	public Vec2 mult(float x, float y) {
		this.x *= x;
		this.y *= y;
		return this;
	}
	
	@Override
	public Vec2 mult(Vec2 vec) {
		return mult(vec.x, vec.y);
	}
	
	@Override
	public Vec2 divide(float f) {
		return divide(f, f);
	}
	
	public Vec2 divide(float x, float y) {
		this.x /= x;
		this.y /= y;
		return this;
	}
	
	@Override
	public Vec2 divide(Vec2 vec) {
		return divide(vec.x, vec.y);
	}
	
	@Override
	public Vec2 mod(float f) {
		x %= f;
		y %= f;
		
		return this;
	}
	
	@Override
	public String toString() {
		return "(" + x + ", " + y + ")";
	}
	
	private final static FloatBuffer direct = BufferUtils.createFloatBuffer(2);
	
	@Override
	public FloatBuffer toBuffer() {
		direct.clear();
		direct.put(x).put(y);
		direct.flip();
		return direct;
	}
}
