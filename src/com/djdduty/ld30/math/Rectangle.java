package com.djdduty.ld30.math;

public class Rectangle {
	private Vec2 pos, size;
	
	public Rectangle() {
		this(new Vec2(0,0), new Vec2(0,0));
	}
	
	public Rectangle(Vec2 pos, Vec2 size) {
		this.pos = pos;
		this.size = size;
	}
	
	public Vec2 getPosition() {
		return pos;
	}
	
	public Vec2 getSize() {
		return size;
	}
	
	public void setPosition(float x, float y) {
		pos.x(x);
		pos.y(y);
	}
	
	public void setPosition(Vec2 pos) {
		setPosition(pos.x(), pos.y());
	}
	
	public void setSize(Vec2 size) {
		this.size.x(size.x());
		this.size.y(size.y());
	}
	
	public void setBounds(Vec2 pos, Vec2 size) {
		setPosition(pos.x(), pos.y());
		setSize(size);
	}
	
	public boolean interescts(Rectangle other) {
		return !(pos.x()+size.x() <= other.pos.x() ||
				 pos.x() >= other.pos.x()+other.size.x() ||
				 pos.y() + size.y() <= other.pos.y() ||
				 pos.y() >= other.pos.y() + other.size.y());
	}
	
	public boolean contains(Vec2 point) {
		return point.x() >= pos.x() && point.x() < pos.x() + size.x() && point.y() >= pos.y() && point.y() < pos.y() + size.y();
	}
}
