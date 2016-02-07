
// Adapted from:
// https://www.shadertoy.com/view/ldlXRS
// and
// https://www.shadertoy.com/view/MslGWN

#define PROCESSING_TEXTURE_SHADER


uniform sampler2D texture;
uniform vec2 resolution;

uniform float currentTime;

#define time currentTime*0.015
#define starTime currentTime*0.25

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


float snoise(vec3 uv, float res)	// by trisomie21
{
    const vec3 s = vec3(1e0, 1e2, 1e4);

    uv *= res;

    vec3 uv0 = floor(mod(uv, res))*s;
    vec3 uv1 = floor(mod(uv+vec3(1.), res))*s;

    vec3 f = fract(uv); f = f*f*(3.0-2.0*f);

    vec4 v = vec4(uv0.x+uv0.y+uv0.z, uv1.x+uv0.y+uv0.z,
            uv0.x+uv1.y+uv0.z, uv1.x+uv1.y+uv0.z);

    vec4 r = fract(sin(v*1e-3)*1e5);
    float r0 = mix(mix(r.x, r.y, f.x), mix(r.z, r.w, f.x), f.y);

    r = fract(sin((v + uv1.z - uv0.z)*1e-3)*1e5);
    float r1 = mix(mix(r.x, r.y, f.x), mix(r.z, r.w, f.x), f.y);

    return mix(r0, r1, f.z)*2.-1.;
}


vec3 nrand3( vec2 co )
{
    vec3 a = fract( cos( co.x*8.3e-3 + co.y )*vec3(1.3e5, 4.7e5, 2.9e5) );
    vec3 b = fract( sin( co.x*0.3e-3 + co.y )*vec3(8.1e5, 1.0e5, 0.1e5) );
    vec3 c = mix(a, b, 0.5);
    return c;
}


void main()
{
    vec2 uv = gl_FragCoord.xy / resolution.xy;

    //setup system
    vec2 p = uv;
    p.x *= resolution.x/resolution.y;
    p*=4.;
    
    float rz = dualfbm(p);
    
    //final color
    vec3 col = vec3(0.15,0.1,0.2)/rz;
    col=pow(abs(col),vec3(1.0));


    // Stars
	vec2 uvs = uv * resolution.xy / max(resolution.x, resolution.y);
	vec3 p2 = vec3(uvs / 2., 0) + vec3(1., -2.3, 0.);
	p2 += .2 * vec3(sin(starTime / 16.0), sin(starTime / 12.0),  sin(starTime / 128.0));
	vec2 seed = p2.xy*2;	
	seed = floor(seed * resolution.x);
	vec3 rnd = nrand3( seed );
	vec4 starColor = vec4(pow(rnd.y,70.0));
	
	// Stars layer 2
	vec3 p3 = vec3(uvs / 3., 0) + vec3(1., -1.3, 0.);
	p3 += .2 * vec3(cos(starTime*2 / 16.0), cos(starTime*2 / 12.0),  cos(starTime*2 / 128.0));
	vec2 seed2 = p3.xy*5;	
	seed2 = floor(seed2 * resolution.y);
	vec3 rnd2 = nrand3( seed2 );
	starColor += vec4(pow(rnd2.y,80.0));


    gl_FragColor = vec4(col,1.) + starColor;
}
