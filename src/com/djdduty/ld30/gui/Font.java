package com.djdduty.ld30.gui;

import com.djdduty.ld30.core.Engine;
import com.djdduty.ld30.math.Vec2;
import com.djdduty.ld30.resource.Texture;

public class Font {
	private int lettersPerLine;
	private String textureName;
	private Texture texture;
	private Vec2 letterUvSize;
	private String mappings;
	
	public Font(int lettersPerLine, String textureName, String mappings) {
		this.lettersPerLine = lettersPerLine;
		this.textureName = textureName;
		this.mappings = mappings.toUpperCase();
		texture = Engine.get().getTextureManager().getTexture(textureName);
		float uSize = 1.0f / 13.0f;
		float vSize = 1.0f / 13.0f;
		this.letterUvSize = new Vec2(uSize, vSize);
	}
	
	public Vec2 getStartUvOfLetter(char c) {
		int xind = mappings.indexOf(c) % lettersPerLine;
		int yind = (int) Math.floor(mappings.indexOf(c) / lettersPerLine);
			
		float u = xind * letterUvSize.x();
		float v = yind * letterUvSize.y();
		return new Vec2(u,v);
	}
	
	public String getTextureName() {
		return textureName;
	}
	
	public float getLetterUvWidth() {
		return letterUvSize.x();
	}
	
	public float getLetterUvHeight() {
		return letterUvSize.y();
	}
}
