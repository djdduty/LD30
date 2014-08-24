package com.djdduty.ld30.game.entities;

import java.util.ArrayList;

import com.djdduty.ld30.graphics.Renderable;
import com.djdduty.ld30.graphics.Vertex;
import com.djdduty.ld30.math.Rectangle;
import com.djdduty.ld30.math.Vec2;
import com.djdduty.ld30.math.Vec3;
import com.djdduty.ld30.scene.entity.EntityController;

public class SpriteSheetEntity extends GameEntity {
	public SpriteSheetEntity(Vec2 pos, String name, Vec2 size) {
		super(pos, name, size);
	}
	
	public SpriteSheetEntity(Vec2 vec2, String string, Vec2 vec22, EntityController copterController) {
		super(vec2, string, vec22, copterController);
	}

	public void onInit() {
		renderable = new Renderable(name + "-Renderer");

		//Mesh Information
		ArrayList<Vertex> vertices = new ArrayList<>();
		ArrayList<Integer> indices = new ArrayList<>();

		Vertex v1 = new Vertex(new Vec3(0,0,-1),					new Vec3(0,0,1),new Vec2(0   ,0));
		Vertex v2 = new Vertex(new Vec3(size.x(),0,-1),				new Vec3(0,0,1),new Vec2(0.5f   ,0));
		Vertex v3 = new Vertex(new Vec3(size.x(),size.y(),-1),		new Vec3(0,0,1),new Vec2(0.5f   ,0.5f));
        Vertex v4 = new Vertex(new Vec3(0, size.y(),-1),			new Vec3(0,0,1),new Vec2(0  , 0.5f));

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

		renderable.setData(vertices, indices);
		bounds = new Rectangle(new Vec2(position.x(), position.y()), size);
	}
}
