package com.djdduty.ld30.game.entities;

import java.util.ArrayList;

import com.djdduty.ld30.graphics.Renderable;
import com.djdduty.ld30.graphics.Vertex;
import com.djdduty.ld30.math.Vec2;
import com.djdduty.ld30.math.Vec3;
import com.djdduty.ld30.scene.entity.Entity;

public class BillboardEntity extends Entity {
	private Vec2 size;
	
	public BillboardEntity(Vec3 pos, Vec2 size, String name) {
		super(pos, name);
		this.size = size;
	}
	
	public void onInit() {
		renderable = new Renderable(name + "-Renderer");

		//Mesh Information
		ArrayList<Vertex> vertices = new ArrayList<>();
		ArrayList<Integer> indices = new ArrayList<>();

		Vertex v1 = new Vertex(new Vec3(-size.x()/2,0,0),			new Vec3(0,0,1),new Vec2(0   ,1));
		Vertex v2 = new Vertex(new Vec3(size.x()/2,0,0),			new Vec3(0,0,1),new Vec2(1   ,1));
		Vertex v3 = new Vertex(new Vec3(size.x()/2,size.y(),0),		new Vec3(0,0,1),new Vec2(1   ,0));
        Vertex v4 = new Vertex(new Vec3(-size.x()/2, size.y(),0),	new Vec3(0,0,1),new Vec2(0  , 0));

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

		renderable.getMaterial().setBillboarded(true);
		renderable.setData(vertices, indices);
	}
}