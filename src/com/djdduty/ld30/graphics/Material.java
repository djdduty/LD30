package com.djdduty.ld30.graphics;

import static org.lwjgl.opengl.GL11.glGetError;
import static org.lwjgl.opengl.GL20.*;
import static org.lwjgl.util.glu.GLU.gluErrorString;

import com.djdduty.ld30.core.Engine;
import com.djdduty.ld30.math.Matrix3;
import com.djdduty.ld30.math.Matrix4;
import com.djdduty.ld30.math.Vec2;
import com.djdduty.ld30.math.Vec3;
import com.djdduty.ld30.math.Vec4;
import com.djdduty.ld30.resource.ShaderProgram;
import com.djdduty.ld30.resource.Texture;

public class Material {
	// diffuse
	private boolean hasDiffuseTexture = false;
	private String diffuseTextureName = "";
	private Vec4 diffuseColor = new Vec4(1, 1, 1, 1);
	private int useDiffuseMapLocation;
	private int diffuseColorLocation;
	private Texture diffuseTexture;
	private int diffuseMapLocation;
	private Vec2 uvOffset = new Vec2(0,0);
	private int uvOffsetLocation;
	
	// normal
	private boolean hasNormalTexture = false;
	private String normalTextureName = "";
	private int useNormalMapLocation;
	private Texture normalTexture;
	private int normalMapLocation;
	
	// specular
	private boolean hasSpecular = false;
	private float specular = -1.0f;
	private int useSpecularLocation;
	private int specularLocation;
	
	// custom shader
	private boolean useCustomShader = false;
	private String vertexShader = "";
	private String fragmentShader = "";
	
	// misc
	private boolean flagsNeedUpdate = true;
	private Renderable owner; // Needed to get the model matrix
	private int modelViewMatrixLocation;
	private int projectionMatrixLocation;
	private int normalMatrixLocation;
	private boolean isStatic = false;
	private int isStaticLocation;
	private boolean isBillboarded = false;
	private boolean isFont = false;
	
	ShaderProgram program;
	
	public Material(Renderable renderable) {
		owner = renderable;
	}
	
	public void bind() {
		if(flagsNeedUpdate)
			updateFlags();
		
		program.enable();
	}
	
	public void unBind() {
		program.disable();
	}
	
	protected void updateFlags() {
		if(!useCustomShader) program = Engine.get().getShaderManager().getShader("DefaultShader");
		else program = Engine.get().getShaderManager().getShader(owner.getName()+"Shader", vertexShader, fragmentShader);

		modelViewMatrixLocation = program.GetUniformLocation("ModelViewMatrix");
		projectionMatrixLocation = program.GetUniformLocation("ProjectionMatrix");
		normalMatrixLocation = program.GetUniformLocation("NormalMatrix");

		useDiffuseMapLocation = program.GetUniformLocation("UseDiffuseMap");
		diffuseColorLocation = program.GetUniformLocation("DiffuseColor");
		diffuseMapLocation = program.GetUniformLocation("DiffuseMap");
		uvOffsetLocation = program.GetUniformLocation("uvOffset");

		useNormalMapLocation = program.GetUniformLocation("NormalMap");
		normalMapLocation = program.GetUniformLocation("UseNormalMap");

		useSpecularLocation = program.GetUniformLocation("UseSpecular");
		specularLocation = program.GetUniformLocation("SpecularPower");
		
		isStaticLocation = program.GetUniformLocation("IsStatic");

		if(hasDiffuseTexture)
			diffuseTexture = Engine.get().getTextureManager().getTexture(diffuseTextureName);

		if(hasNormalTexture)
			normalTexture = Engine.get().getTextureManager().getTexture(normalTextureName);

		flagsNeedUpdate = false;
	}

	public void prepareUniforms() {
		Matrix4 modelView;
		Matrix4 projection;
		
		if(!isFont) {
			Matrix4 model = owner.getTransform();
			Matrix4 view = Engine.get().getRenderer().getActiveCamera().getTransform();
			modelView = model;//view.mult(model);
			projection = Engine.get().getRenderer().getActiveCamera().getOrtho();
			
			if(isBillboarded) {
				Vec3 pos = owner.getPosition();
				Vec3 camPos = Engine.get().getRenderer().getActiveCamera().getPosition();
				Vec3 look = pos.addNew(camPos).normalize();
				Vec3 up = new Vec3(0,1,0);
				Vec3 right = look.cross(up);
				Matrix4 m2 = new Matrix4().clearToIdentity();
				m2.matrix[0] = right.x(); m2.matrix[4] = up.x(); m2.matrix[8] = look.x();  m2.matrix[12] = pos.x();
				m2.matrix[1] = right.y(); m2.matrix[5] = up.y(); m2.matrix[9] = look.y();  m2.matrix[13] = pos.y();
				m2.matrix[2] = right.z(); m2.matrix[6] = up.z(); m2.matrix[10] = look.z(); m2.matrix[14] = pos.z();
				modelView = view.mult(m2);
			}
		} else {
			modelView = new Matrix4().clearToIdentity();
			projection = Engine.get().getRenderer().getActiveCamera().getOrtho();
		}
		
		Matrix3 normalMatrix = new Matrix3(modelView).inverse().transpose();
		
		glUniformMatrix4(modelViewMatrixLocation, false, modelView.toBuffer());
		glUniformMatrix4(projectionMatrixLocation, false, projection.toBuffer());
		glUniformMatrix3(normalMatrixLocation, false, normalMatrix.toBuffer());

		if(hasDiffuseTexture) {
			diffuseTexture.bind(0);
			glUniform1i(useDiffuseMapLocation, 1);
			glUniform1i(diffuseMapLocation, 0);
		} else {
			glUniform1i(useDiffuseMapLocation, 0);
		}


		if(hasNormalTexture) {
			normalTexture.bind(1);
			glUniform1i(useNormalMapLocation, 1);
			glUniform1i(normalMapLocation, 1);
		} else {
			glUniform1i(useNormalMapLocation, 0);
		}
		
		if(hasSpecular) {
			glUniform1i(useSpecularLocation, 1);
			glUniform1f(specularLocation, specular);
		} else {
			glUniform1i(useSpecularLocation, 0);
		}
		
		if(isStatic) {
			glUniform1i(isStaticLocation, 1);
		} else {
			glUniform1i(isStaticLocation, 0);
		}
		
		glUniform4f(diffuseColorLocation, diffuseColor.x(), diffuseColor.y(), diffuseColor.z(), diffuseColor.w());
		glUniform2f(uvOffsetLocation, uvOffset.x(), uvOffset.y());
	}
	
	public void save(String path) {} // TODO:
	
	public void load(String path) {} // TODO:
	
	public void setDiffuseTexture(String name) {
		diffuseTextureName = name;
		
		hasDiffuseTexture = false;
		if(diffuseTextureName != "")
			hasDiffuseTexture = true;
		
		flagsNeedUpdate = true;
	}
	
	public void setNormalTexture(String name) {
		normalTextureName = name;
		
		hasNormalTexture = false;
		if(normalTextureName != "")
			hasNormalTexture = true;
		
		flagsNeedUpdate = true;
	}
	
	public void setDiffuseColor(Vec4 color) {
		diffuseColor = color;
	}
	
	public void setUvOffset(Vec2 offset) {
		uvOffset = offset;
	}
	
	public Vec2 getUvOffset() {
		return uvOffset;
	}
	
	public void setSpecular(float specular) {
		this.specular = specular;
		
		hasSpecular = false;
		if(this.specular > 0f)
			hasSpecular = true;
		
		flagsNeedUpdate = true;
	}
	
	public void setShaders(String vertShader, String fragShader) {
		vertexShader = vertShader;
		fragmentShader = fragShader;
		
		if(vertexShader != "" && fragmentShader != "") {
			useCustomShader = true;
			program = Engine.get().getShaderManager().getShader(owner.getName()+"Shader", vertexShader, fragmentShader);
		}
		
		flagsNeedUpdate = true;
	}
	
	public ShaderProgram getProgram() {
		return program;
	}
	
	public void setStatic(boolean s) {
		isStatic = s;
	}
	
	public void setBillboarded(boolean billboarded) {
		isBillboarded = billboarded;
	}

	public void setIsFont(boolean f) {
		this.isFont = f;
	}
}


