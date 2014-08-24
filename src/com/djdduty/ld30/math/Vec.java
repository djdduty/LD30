package com.djdduty.ld30.math;

import java.nio.FloatBuffer;

public interface Vec<V> {
	V add(V v);
	
	V sub(V v);
	
	V mult(V v);
	
	V mult(float f);
	
	V divide(V v);
	
	V divide(float f);
	
	V mod(float f);
	
	float length();
	
	V copy();
	
	FloatBuffer toBuffer();
	
	@Override
	String toString();
}
