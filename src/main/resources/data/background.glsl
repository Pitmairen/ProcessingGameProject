
// Adapted from:
// https://www.shadertoy.com/view/ldlXRS
//

#define PROCESSING_TEXTURE_SHADER


uniform sampler2D texture;
uniform vec2 resolution;

uniform float currentTime;

#define time currentTime*0.015


mat2 makem2(in float theta){
    float c = cos(theta);
    float s = sin(theta);
    return mat2(c,-s,s,c);
}

float noise( in vec2 x ){
    return texture2D(texture, x*.015).x;
}

float fbm(in vec2 p)
{	
    float z=1.;
    float rz = 0.;
    vec2 bp = p;
    for (float i= 1.;i < 6.;i++)
    {
        rz+= abs((noise(p)-0.1)*2.3)/z;
        z = z*2.;
        p = p*2.;
    }
    return rz;
}

float dualfbm(in vec2 p)
{
    //get two rotated fbm calls and displace the domain
    vec2 p2 = p*.7;
    vec2 basis = vec2(fbm(p2-time*1.6),fbm(p2+time*1.7));
    basis = (basis-.5)*.2;
    p += basis;
    
    //coloring
    return fbm(p*makem2(time*0.2));
}


void main()
{
    //setup system
    vec2 p = gl_FragCoord.xy / resolution.xy;
    p.x *= resolution.x/resolution.y;
    p*=4.;
    
    float rz = dualfbm(p);
    
    //final color
    vec3 col = vec3(0.15,0.1,0.2)/rz;
    col=pow(abs(col),vec3(1.0));
    gl_FragColor = vec4(col,1.);
}
