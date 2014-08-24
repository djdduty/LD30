package com.djdduty.ld30.resource;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.HashMap;

public class ShaderManager {
	private HashMap<String, ShaderProgram> shaders = new HashMap<>();

	public ShaderManager() {

	}

	public ShaderProgram getShader(String name) {
		if(shaders.containsKey(name)) return shaders.get(name);
		else System.out.println("Could not find the shader " + name);
		return null;
	}

	public ShaderProgram getShader(String name, String vertShaderPath, String fragShaderPath) {
		if(shaders.containsKey(name)) return shaders.get(name);

		ShaderProgram s = new ShaderProgram(readFromFile(vertShaderPath), readFromFile(fragShaderPath), name);
		shaders.put(name, s);
		return s;
	}

	public void unLoad(String Name) {
		//TODO: For smarter memory management
	}
	
	public String readFromFile(String file) {
		try(BufferedReader reader = new BufferedReader(new InputStreamReader(getClass().getResourceAsStream(file), "UTF-8"))) {
			StringBuilder s = new StringBuilder();
			String l;

			while((l = reader.readLine()) != null)
				s.append(l).append('\n');

			return s.toString();
		} catch(Exception exc) {
			throw new RuntimeException("Failure reading file: " + file, exc);
		}
	}
}
