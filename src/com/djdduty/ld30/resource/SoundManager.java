package com.djdduty.ld30.resource;

import java.util.HashMap;

import org.newdawn.slick.openal.Audio;
import org.newdawn.slick.openal.AudioLoader;

public class SoundManager {
	private HashMap<String, Audio> sounds = new HashMap<>();
	
	public SoundManager() {
		
	}
	
	public Audio getSound(String Name) {
		if(sounds.containsKey(Name)) return sounds.get(Name);
		else System.out.println("Could not find the sound " + Name);
		return null;
	}

	public Audio getSound(String Name, String Path) {
		if(sounds.containsKey(Name)) return sounds.get(Name);

		Audio a = extract(Path);
		if(a != null)
			sounds.put(Name, a);
		return a;
	}
	
	public Audio extract(String path) {
		path = path.trim().replace('\\', '/');
		
		if(path.startsWith("/"))
			path = path.substring(1);
		
		try {
			return AudioLoader.getAudio(path.substring(path.lastIndexOf('.')+1).toUpperCase().trim(), SoundManager.class.getClassLoader().getResourceAsStream(path));
		} catch (Exception exc) {
			exc.printStackTrace();
			return null;
		}
	}
}
