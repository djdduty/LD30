package com.djdduty.ld30.core;

import org.lwjgl.input.Keyboard;
import org.lwjgl.opengl.ContextAttribs;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.DisplayMode;
import org.lwjgl.opengl.PixelFormat;

import static org.lwjgl.opengl.GL11.*;

public abstract class GLProgram {
	protected int fps;
	protected boolean printFps = false;
	protected boolean shouldStop = false;
	
	public GLProgram(String name, int width, int height, boolean resizable) {
		Display.setTitle(name);
		try {
			Display.setDisplayMode(new DisplayMode(width, height));
		} catch(Exception exc) {
			exc.printStackTrace();
		}
		
		Display.setResizable(resizable);
		fps = 60;
	}
	
	public void setFPS(int fps) {
		this.fps = fps;
	}
	
	public int getFPS() {
		return this.fps;
	}
	
	public final void run() {
		run(false);
	}
	
	public final void run(boolean core) {
		run(core, new PixelFormat());
	}
	
	public final void run(boolean core, PixelFormat format) {
		run(format, core ? new ContextAttribs(3, 3).withProfileCore(true) : null);
	}
	
	public final void run(int major, int minor) {
		run(major, minor, false);
	}
	
	public final void run(int major, int minor, boolean core) {
		run(major, minor, core, new PixelFormat());
	}
	
	public final void run(int major, int minor, boolean core, PixelFormat format) {
		run(format, core ? new ContextAttribs(major, minor).withProfileCore(core) : new ContextAttribs(major, minor));
	}
	
	public final void run(PixelFormat format) {
		run(format, null);
	}
	
	public final void run(ContextAttribs attribs) {
		run(new PixelFormat(), attribs);
	}
	
	public final void run(PixelFormat format, ContextAttribs attribs) {
		try {
			Display.create(format, attribs);
		} catch(Exception exc) {
			exc.printStackTrace();
			System.exit(1);
		}
		
		gameLoop();
	}
	
	private void gameLoop() {
		try {
			init();
			resized();
			long lastTime, lastFPS;
			lastTime = lastFPS = System.nanoTime();
			int frames = 0;
			
			while(!Display.isCloseRequested() && !shouldStop()) {
				long deltaTime = System.nanoTime() - lastTime;
				lastTime += deltaTime;
				
				if(Display.wasResized())
					resized();
				
				update(deltaTime);
				render();
				Display.update();
				
				frames++;
				if(System.nanoTime() - lastFPS >= 1e9) {
					if(printFps)
						System.out.println("FPS: ".concat(String.valueOf(frames)));
					lastFPS += 1e9;
					frames = 0;
				}
				
				if(fps > 0)
					Display.sync(fps);
			}
		} catch(Throwable exc) {
			exc.printStackTrace();
		} finally {
			destroy();
		}
	}
	
	public abstract void render();
	public void update(long deltaTime) {}
	
	private void destroy() {
		Display.destroy();
		System.exit(0);
	}
	
	public void resized() {
		glViewport(0,0,getWidth(), getHeight());
	}
	
	public int getWidth() {
		return Display.getWidth();
	}
	
	public int getHeight() {
		return Display.getHeight();
	}
	
	public boolean shouldStop() {
		return shouldStop;
	}
	
	public void Stop() {
		shouldStop = true;
	}
	
	public abstract void init();
}
