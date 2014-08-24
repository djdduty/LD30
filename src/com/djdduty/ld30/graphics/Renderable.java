package com.djdduty.ld30.graphics;

import org.lwjgl.BufferUtils;

import com.djdduty.ld30.math.Matrix4;
import com.djdduty.ld30.math.Vec3;

import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import java.util.ArrayList;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL15.*;
import static org.lwjgl.opengl.GL20.*;
import static org.lwjgl.opengl.GL30.*;

public class Renderable {
	protected Material material;
	protected String name;

	private boolean enabled = false;
	private boolean initialized = false;

	protected int vao;
	protected int vbo;
	protected int ibo;

	protected int indexCount = 0;

	protected Vec3 position;
	protected Matrix4 transform;
	
	public Renderable(String name) {
		this.name = name;
		material = new Material(this);
		transform = new Matrix4().clearToIdentity();
	}
	
	public final void init() {
		if(!initialized) {
			onInit();
			initialized = true;
		}
	}

	public void setData(ArrayList<Vertex> vertices, ArrayList<Integer> indices) {
		if(vertices.get(0).tangent == null) {
			//calculateTangents(vertices, indices); TODO: BROKEN
		}

		int vertCount = vertices.size();
		indexCount = indices.size();

		FloatBuffer data = BufferUtils.createFloatBuffer(11 * vertCount);
		for(int i = 0; i < vertCount; i++) {
			Vertex v = vertices.get(i);
			data.put(v.position.x()).put(v.position.y()).put(v.position.z());
			//System.out.println(v.position.x() + " " + v.position.y() + " " + v.position.z());

			data.put(v.normal.x());
			data.put(v.normal.y());
			data.put(v.normal.z());

			data.put(v.uv.x());
			data.put(v.uv.y());

			data.put(0);//v.tangent.x());
			data.put(0);//v.tangent.y());
			data.put(0);//v.tangent.z());
		}

		data.flip();

		IntBuffer indexData = BufferUtils.createIntBuffer(indexCount);
		for(int i = 0; i < indexCount; i++) {
			indexData.put(indices.get(i));
		}
		indexData.flip();

		vao = glGenVertexArrays();
		glBindVertexArray(vao);

		glEnableVertexAttribArray(0);
		glEnableVertexAttribArray(1);
		glEnableVertexAttribArray(2);
		glEnableVertexAttribArray(3);

		vbo = glGenBuffers();
		glBindBuffer(GL_ARRAY_BUFFER, vbo);
		glBufferData(GL_ARRAY_BUFFER, data, GL_STATIC_DRAW);

		glVertexAttribPointer(0, 3, GL_FLOAT, false, 44, 0);
		glVertexAttribPointer(1, 3, GL_FLOAT, false, 44, 12);
		glVertexAttribPointer(2, 2, GL_FLOAT, false, 44, 24);
		glVertexAttribPointer(3, 2, GL_FLOAT, false, 44, 32);

		ibo = glGenBuffers();
		glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, ibo);
		glBufferData(GL_ELEMENT_ARRAY_BUFFER, indexData, GL_STATIC_DRAW);

		glBindVertexArray(0);
		glBindBuffer(GL_ARRAY_BUFFER, 0);
		glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, 0);

		glDisableVertexAttribArray(0);
		glDisableVertexAttribArray(1);
		glDisableVertexAttribArray(2);
		glDisableVertexAttribArray(3);
	}

	public final void deInit() {
		if(initialized) {
			onDeInit();
			initialized = false;
		}
	}

	public final void preRender() {
		material.bind();
		material.prepareUniforms();
	}
	
	public void render() {
		if(indexCount > 0) {
			glBindVertexArray(vao);
			glDrawElements(GL_TRIANGLES, indexCount, GL_UNSIGNED_INT, 0);
			glBindVertexArray(0);
			//System.out.println("Rendering");
		}
	}
	
	public final void postRender() {
		material.unBind();
	}
	
	public Material getMaterial() {
		return material;
	}
	
	public void setMaterial(Material m) {
		material = m;
	}

	public void translate(Vec3 trans) {
		position.add(trans);
		transform.translate(trans);
	}
	
	public void setPosition(Vec3 pos) {
		position = pos;
		transform = new Matrix4().clearToIdentity().translate(position);
	}
	
	public void setTransform(Matrix4 trans) {
		this.transform = trans;
		position = new Vec3(trans.matrix[12], trans.matrix[13], trans.matrix[14]);
	}

	public final void enable() {
		if(!enabled) {
			onEnable();
			enabled = true;
		}
	}

	public final void disable() {
		if(enabled) {
			onDisable();
			enabled = false;
		}
	}

	public ArrayList<Vertex> calculateTangents(ArrayList<Vertex> vertices, ArrayList<Integer> indices) {
		for (int i = 0 ; i < indices.size(); i += 3) {
			Vertex v0 = vertices.get(indices.get(i+0));
			Vertex v1 = vertices.get(indices.get(i+1));
			Vertex v2 = vertices.get(indices.get(i+2));

			Vec3 Edge1 = v1.position.sub(v0.position);
			Vec3 Edge2 = v2.position.sub(v0.position);

			float deltaU1 = v1.uv.x() - v0.uv.x();
			float deltaV1 = v1.uv.y() - v0.uv.y();
			float deltaU2 = v2.uv.x() - v0.uv.x();
			float deltaV2 = v2.uv.y() - v0.uv.y();

			float f = 1.0f / (deltaU1 * deltaV2 - deltaU2 * deltaV1);

			Vec3 tangent = new Vec3();

			tangent.x(f * (deltaV2 * Edge1.x() - deltaV1 * Edge2.x()));
			tangent.y(f * (deltaV2 * Edge1.y() - deltaV1 * Edge2.y()));
			tangent.z(f * (deltaV2 * Edge1.z() - deltaV1 * Edge2.z()));

			tangent = tangent.normalize();

			v0.tangent.add(tangent);
			v1.tangent.add(tangent);
			v2.tangent.add(tangent);
		}

		return vertices;
	}

	protected void onInit() {}
	protected void onEnable() {}
	protected void onDisable() {}
	protected void onDeInit() {}

	public boolean getEnabled() {
		return enabled;
	}
	
	public boolean getInitialized() {
		return initialized;
	}
	
	public Matrix4 getTransform() {
		return transform;
	}
	
	public String getName() {
		return name;
	}
	
	public Vec3 getPosition() {
		return position;
	}
}
