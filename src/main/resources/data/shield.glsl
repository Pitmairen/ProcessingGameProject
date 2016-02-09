#ifdef GL_ES
precision mediump float;
precision mediump int;
#endif


#define PROCESSING_TEXTURE_SHADER

uniform vec4 position;
uniform sampler2D texture;
uniform float time;
uniform vec2 resolution;

varying vec4 vertColor;
varying vec4 vertCoord;

#define tau 6.2831853


float noise( in vec2 x ){
    return texture2D(texture, x*.01).x;
}

mat2 makem2(in float theta){
    float c = cos(theta);
    float s = sin(theta);
    return mat2(c,-s,s,c);
}

float fbm(in vec2 p)
{	
    float z=1.;
    float rz = 0.;
    vec2 bp = p;
    for (float i= 1.;i < 6.;i++)
    {
        rz+= abs((noise(p)-0.7)*2.3)/z;
        z = z*2.;
        p = p*2.;
    }
    return rz;
}

float circ(vec2 p) 
{
    float r = length(p);
    r = log(sqrt(r));
    return abs(mod(r*8.,tau)-3.14)*8.+.2; // 8 adjusts affects circle thikness

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

void main() {

    vec2 uv = gl_FragCoord.xy / resolution.xy;

    //setup system
    vec2 p = uv - vec2(position.x/resolution.x, position.y/resolution.y);
    p.x *= resolution.x/resolution.y;
    p*=4.;

    float rz = dualfbm(p);


    //p /= exp(mod(time*30.,3.14159)); // makes rings pulsate.
    rz *= pow(abs((0.1-circ(p))),0.9); // 0.1 can split the circle


    // Distance from center of ring to the fragment
    float d =  distance(gl_FragCoord.xy, position.xy);

    // The color of the ring
    vec3 col = vec3(.0,0.8,0.2)/rz;
    vec4 finalColor;

    // Draw shield if the radius is less than 80
    if(d < 80 && d > 70){ 
        finalColor = vec4(col, (80-d)/10.0); // Gradually fade the outside of the ring
    }
    else if(d <= 70){
        finalColor = vec4(col, 1.0);
    }
    else{
        // Else nothing, set alpha to 0
        finalColor = vec4(col, 0.0);
    }

    gl_FragColor = finalColor;

}
