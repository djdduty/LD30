package com.djdduty.ld30.resource;

import java.io.InputStream;
import java.nio.ByteBuffer;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL13.*;

import org.lwjgl.BufferUtils;

import com.djdduty.ld30.math.PNGDecoder;

public class Texture {
	private int width = 0;
	private int height = 0;
	private String path;
	public int texPointer;

	public Texture(String path) {
		this.path = path;
	}

	public int getWidth() {
		return width;
	}

	public int getHeight() {
		return height;
	}

	public void setPath(String path) {
		this.path = path;
	}

	public String getPath() {
		return path;
	}

	public void load() {
		ByteBuffer data;
		InputStream inputStream = getClass().getClassLoader().getResourceAsStream(path);
		try {
			PNGDecoder decoder = new PNGDecoder(inputStream);
			int n = 4;
			data = BufferUtils.createByteBuffer(decoder.getWidth() * decoder.getHeight() * n * 4);
			decoder.decode(data, decoder.getWidth() * 4, PNGDecoder.Format.RGBA);
			data.flip();

			width = decoder.getHeight();
			height = decoder.getHeight();

			texPointer = glGenTextures();
			glBindTexture(GL_TEXTURE_2D, texPointer);
			glTexParameterf(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_NEAREST);
			glTexParameterf(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_NEAREST);
			glTexImage2D(GL_TEXTURE_2D, 0, GL_RGBA, width, height, 0, GL_RGBA, GL_UNSIGNED_BYTE, data);

		} catch(Exception exc) {
			exc.printStackTrace();
		} finally {
			try {
				inputStream.close();
			} catch(Exception exc) {}
		}
	}

	public void bind(int textureUnit) {
		glActiveTexture(GL_TEXTURE0 + textureUnit);
		glBindTexture(GL_TEXTURE_2D, texPointer);
	}
}