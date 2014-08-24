package com.djdduty.ld30.resource;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL20.*;

public class ShaderProgram {
	private int program;
	private String name;
	
	public ShaderProgram(String vertShader, String fragShader, String name) {
		int vs = compileShader(vertShader, GL_VERTEX_SHADER);
		int fs = compileShader(fragShader, GL_FRAGMENT_SHADER);

		program = glCreateProgram();
		glAttachShader(program, vs);
		glAttachShader(program, fs);
		
		this.name = name;
	}

	public void bindAttribute(int Index, String Name) {
		glBindAttribLocation(program, Index, Name);
	}
	
	public void linkProgram() {
		glLinkProgram(program);
	}
	
	public void printErrorLog() {
		String infoLog = glGetProgramInfoLog(program, glGetProgrami(program, GL_INFO_LOG_LENGTH));
		
		if(glGetProgrami(program, GL_LINK_STATUS) == GL_FALSE)
			throw new RuntimeException("Failure in linking program. ERROR:\n" + infoLog);
		else {
			System.out.print("Linking program successful.");
			if(infoLog != null && !(infoLog = infoLog.trim()).isEmpty())
				System.out.println(" Log:\n" + infoLog);
			else
				System.out.println();
		}
	}
	
	public int compileShader(String source, int type) {
		int shader = glCreateShader(type);
		glShaderSource(shader, source);
		glCompileShader(shader);

		String infoLog = glGetShaderInfoLog(shader, glGetShaderi(shader, GL_INFO_LOG_LENGTH));
		if(glGetShaderi(shader, GL_COMPILE_STATUS) == GL_FALSE)
			throw new RuntimeException("Failure compiling " + getShaderTypeName(type) + ". ERROR:\n" + infoLog);
		else {
			System.out.print("Compiling " + getShaderTypeName(type) + " successful.");
			if(infoLog != null && !(infoLog = infoLog.trim()).isEmpty())
				System.out.println(" Log:\n" + infoLog);
		}
		
		return shader;
	}
	
	public String getShaderTypeName(int type) {
		if(type == GL_VERTEX_SHADER)
			return "Vertex Shader";
		if(type == GL_FRAGMENT_SHADER)
			return "Fragment Shader";
		
		return "";
	}
	
	public int GetUniformLocation(String name) {
		return glGetUniformLocation(program, name);
	}
	
	public int getProgram() {
		return program;
	}
	
	public void enable() {
		glUseProgram(program);
		//System.out.println("Wut? "+name);
	}
	
	public void disable() {
		glUseProgram(0);
	}
	
	public void destroy() {
		glDeleteProgram(program);
	}
	
	public String getName() {
		return name;
	}
}

