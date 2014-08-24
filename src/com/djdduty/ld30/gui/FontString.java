package com.djdduty.ld30.gui;

import java.util.ArrayList;

import com.djdduty.ld30.core.Engine;
import com.djdduty.ld30.graphics.Renderable;
import com.djdduty.ld30.graphics.Vertex;
import com.djdduty.ld30.math.Vec2;
import com.djdduty.ld30.math.Vec3;
import com.djdduty.ld30.math.Vec4;

public class FontString {
	private String content;
	private Renderable renderable;
	private Vec2 position;
	private Vec2 letterSize;
	private Font font;
	private boolean centerFont;
	
	public FontString(String content, Vec2 position, Vec2 letterSize, Font font) {
		this(content, position, letterSize, font, false);
	}
	
	public FontString(String content, Vec2 position, Vec2 letterSize, Font font, boolean centerFont) {
		this.centerFont = centerFont;
		this.content = content;
		this.position = position;
		this.letterSize = letterSize;
		this.font = font;
		
		renderable = new Renderable("Font"+content+"-renderable");
		constructBuffer();
	}
	
	public void constructBuffer() {
		String c = content.toUpperCase();
		ArrayList<Vertex> vertices = new ArrayList<>();
		ArrayList<Integer> indices = new ArrayList<>();
		
		int lastIndex = 0;
		for(int x = 0; x < c.length(); x++) {
			float xoff;
			float yoff;
			if(centerFont) {
				xoff = (x*(letterSize.x()*0.75f) + position.x()) - ((c.length()*(letterSize.x()*0.75f))*0.5f);
				yoff = position.y() - letterSize.y()/2;
			} else {
				xoff = x*(letterSize.x()*0.75f) + position.x();
				yoff = position.y();
			}
			
			Vec2 uv = font.getStartUvOfLetter(c.charAt(x));
			
			Vertex v1 = new Vertex(new Vec3(xoff, yoff								,0),	new Vec3(0,0,1),new Vec2(uv.x()							,uv.y()));
			Vertex v2 = new Vertex(new Vec3(xoff+letterSize.x(), yoff				,0),	new Vec3(0,0,1),new Vec2(uv.x()+font.getLetterUvWidth()	,uv.y()));
			Vertex v3 = new Vertex(new Vec3(xoff+letterSize.x(), yoff+letterSize.y(),0), 	new Vec3(0,0,1),new Vec2(uv.x()+font.getLetterUvWidth()	,uv.y()+font.getLetterUvHeight()));
	        Vertex v4 = new Vertex(new Vec3(xoff, yoff+letterSize.y()				,0),	new Vec3(0,0,1),new Vec2(uv.x()  						,uv.y()+font.getLetterUvHeight()));
	        
			vertices.add(v1);
			vertices.add(v2);
			vertices.add(v3);
			vertices.add(v4);

			indices.add(lastIndex+0);
			indices.add(lastIndex+1);
			indices.add(lastIndex+2);
			
			indices.add(lastIndex+2);
			indices.add(lastIndex+3);
			indices.add(lastIndex+0);
			lastIndex += 4;
		}
		
		renderable.getMaterial().setDiffuseTexture(font.getTextureName());
		renderable.getMaterial().setIsFont(true);
		renderable.setData(vertices, indices);
		renderable.init();
	}
	
	public void queueForRender() {
		Engine.get().getRenderer().addRenderable(renderable);
	}
	
	public void setContent(String newContent) {
		content = newContent;
		constructBuffer();
	}
	
	public void setSize(Vec2 newSize) {
		letterSize = newSize;
		constructBuffer();
	}

	public Renderable getRenderable() {
		return this.renderable;
	}
}
