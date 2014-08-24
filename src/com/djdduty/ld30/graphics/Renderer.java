package com.djdduty.ld30.graphics;

import java.util.ArrayList;

import com.djdduty.ld30.core.Engine;
import com.djdduty.ld30.math.Vec2;
import com.djdduty.ld30.math.Vec3;
import com.djdduty.ld30.resource.ShaderManager;
import com.djdduty.ld30.resource.ShaderProgram;

import static org.lwjgl.opengl.GL11.*;

public class Renderer {
	private ArrayList<Renderable> renderables;
	private Vec2 screenOffset;
	private Camera activeCamera;
	private int renderScale = 0;
	
	public Renderer() {
		renderables = new ArrayList<Renderable>();
		screenOffset = new Vec2(0,0);
		activeCamera = new Camera();
	}
	
	public void init() {
		ShaderManager manager = Engine.get().getShaderManager();
		ShaderProgram defaultShader = manager.getShader("DefaultShader", "/res/shaders/default.vert", "/res/shaders/default.frag");
		defaultShader.bindAttribute(0, "Position");
		defaultShader.bindAttribute(1, "Normal");
		defaultShader.bindAttribute(2, "Texcoord");
		defaultShader.bindAttribute(3, "Tangent");
		defaultShader.linkProgram();
		defaultShader.printErrorLog();
	}
	
	public void addRenderable(Renderable r) {
		renderables.add(r);
		r.enable();
	}
	
	public void setScreenOffset(Vec2 Off) {
		screenOffset = Off;
		Off.x(Off.x() + (Engine.get().getWidth()/renderScale)/2);
		Off.y(Off.y() + (Engine.get().getHeight()/renderScale)/2);
		//TODO: translate the camera
		activeCamera.setPosition(new Vec3(-Off.x(),-Off.y(),0));
	}
	
	public Vec2 getScreenOffset() {
		return screenOffset;
	}
	
	public void setRenderScale(int scale) {
		renderScale = scale;
	}
	
	public int getRenderScale() {
		return renderScale;
	}
	
	public void render() {
		glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
		glViewport(0,0, Engine.get().getWidth(),Engine.get().getHeight());

		glEnable(GL_DEPTH_TEST);
		//glEnable(GL_CULL_FACE);
		glEnable(GL_BLEND);
		glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);
		
		for(int i = renderables.size(); i > 0; i--) {
			Renderable r = renderables.get(i-1);
		 	if(r.getEnabled()) {
				r.preRender();
				r.render();
				r.postRender();
		 	}
		}
		renderables.clear();

		glDisable(GL_DEPTH_TEST);
	    glDisable(GL_CULL_FACE);
		glDisable(GL_BLEND);
	}
	
	public Camera getActiveCamera() {
		return activeCamera;
	}
	
	public void update(float deltaTime) {
		if(activeCamera != null)
			activeCamera.update(deltaTime);
	}
	
	public void setActiveCamera(Camera cam) {
		activeCamera = cam;
	}
}

