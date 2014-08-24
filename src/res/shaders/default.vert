#version 130

in vec3 Position;
in vec3 Normal;
in vec2 Texcoord;
in vec3 Tangent;

uniform mat4 ModelViewMatrix;
uniform mat4 ProjectionMatrix;
uniform mat3 NormalMatrix;
uniform int IsStatic;

//Normals not working right now

out vec2 UV;
out vec3 Norm;
out vec3 Tang;

void main()
{
    if(IsStatic == 1) {
        gl_Position = ProjectionMatrix * vec4(Position, 1.0);
    } else {
        gl_Position = ProjectionMatrix * ModelViewMatrix * vec4(Position, 1.0);
    }
    UV = Texcoord;
    Norm = NormalMatrix * Normal;
    Tang = NormalMatrix * Tangent;
}
