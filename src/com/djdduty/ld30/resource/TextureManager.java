package com.djdduty.ld30.resource;

import java.util.HashMap;

public class TextureManager {
	private HashMap<String, Texture> textures = new HashMap<>();

	public TextureManager() {

	}

	public Texture getTexture(String Name) {
		if(textures.containsKey(Name)) return textures.get(Name);
		else System.out.println("Could not find the texture " + Name);
		return null;
	}

	public Texture getTexture(String Name, String Path) {
		if(textures.containsKey(Name)) return textures.get(Name);

		Texture t = new Texture(Path);
		t.load();
		textures.put(Name, t);
		return t;
	}

	public void unLoad(String Name) {
		//TODO: For smarter memory management
	}
}

