package backend.main;

import backend.resources.Image;
import backend.resources.ResourceManager;
import java.util.Random;
import processing.core.PGraphics;
import processing.core.PImage;
import processing.core.PVector;

/**
 * The particle emitter manages all the particles in the explosions.
 *
 * Because there can be a lot of particles in an explosion the emitter keeps a a
 * pool of particles that are reused when the particles die. This should make
 * the code faster because it causes less memory allocations and creates less
 * work for the garbage collector.
 *
 * The update method should be called once every frame with a time delta that
 * has passed since the last update.
 *
 * The particles are drawn onto a PGraphics object with the draw method. The
 * PGrapahics is used to be able to create some fading effects. It also seems to
 * be faster.
 *
 * @author pitmairen
 */
public class ParticleEmitter {

    private final int PARTICLE_LIMIT = 1000;

    private final Particle[] particles;

    // The current number of live particles
    private int particleCount = 0;

    private final Random random = new Random();

    // Background image used for the particles
    private final PImage particleImage;

    /**
     * Constructor.
     * @param resources the resource manager
     */
    public ParticleEmitter(ResourceManager resources) {

        particleImage = resources.getImage(Image.PARTICLE);

        this.particles = new Particle[PARTICLE_LIMIT];

        // Create the particle pool
        for (int i = 0; i < PARTICLE_LIMIT; i++) {
            this.particles[i] = new Particle();
        }
    }

    /**
     * Emits a burst of particles that explodes out from @position in random
     * directions.
     *
     * @param count the number of particles
     * @param position the position to emit the particles from
     * @param particleColor the color of the particles
     */
    public void emitParticles(int count, PVector position, int particleColor) {
        emitParticles(count, position, particleColor, 15.0f, 10.0f, 8f, 6f);
    }

    
    /**
     * Emits a burst of particles that explodes out from @position in random
     * directions.
     *
     * @param count the number of particles
     * @param position the position to emit the particles from
     * @param particleColor the color of the particles
     * @param sizeLimit the maximum size of the particles
     * @param speedLimit the maximum speed of the particles
     * 
     */
    public void emitParticles(int count, PVector position, int particleColor, float sizeLimit, float speedLimit) {
        emitParticles(count, position, particleColor, sizeLimit, speedLimit, 8f, 6f);
    }
    
    /**
     * Emits a burst of particles that explodes out from @position in random
     * directions.
     *
     * @param count the number of particles
     * @param position the position to emit the particles from
     * @param particleColor the color of the particles
     * @param sizeLimit the maximum size of the particles
     * @param speedLimit the maximum speed of the particles
     * @param opacityStep how much the particles opacity is reduces on each update
     * @param lifeStep how much the particles life is reduced on each update
     * 
     */
    public void emitParticles(int count, PVector position, int particleColor, float sizeLimit, float speedLimit, float opacityStep, float lifeStep) {

        for (int i=0; i < count; i++) {
            this.particles[(particleCount + i) % PARTICLE_LIMIT].reset(
                    position.copy(),
                    PVector.random2D().mult(this.randomRange(0.2f, speedLimit)), // Velocity
                    particleColor,
                    this.randomRange(2.0f, sizeLimit), // Particle size
                    opacityStep,
                    lifeStep
            );
        }
        this.particleCount = Math.min(PARTICLE_LIMIT, this.particleCount + count);
    }

    /**
     * Updates the position of the particles
     *
     * @param timeDelta the time since the last call
     */
    public void update(float timeDelta) {

        for (int i = 0; i < this.particleCount; i++) {
            this.particles[i].update(timeDelta);
            if (!this.particles[i].isAlive()) {   
                recycle(i);
                i--; // We have to update the particle that was swapped by the recycle
            }
        }
    }
    
    /**
     * Kills all the live particles.
     */
    public void killAllParticles(){
        for (int i = 0; i < this.particleCount; i++) {
            this.particles[i].kill();
        }
        this.particleCount = 0;
    }

    /**
     * Draws the particles onto the canvas
     *
     * @param canvas the canvas to draw to
     */
    public void draw(PGraphics canvas) {
        for (int i = 0; i < this.particleCount; i++) {
            this.particles[i].draw(canvas);
        }
    }

    /**
     *
     */
    private float randomRange(float min, float max) {
        return min + this.random.nextFloat() * ((max - min) + 1);
    }
    
    /**
     * Recycles a dead particle.
     * 
     * This is done by swapping the dead particles with the last known 
     * live particles. This will keep all the live particles in the from 
     * of the array and all the dead one at the end.
     * 
     */
    private void recycle(int i){
        Particle p = particles[i];
        particles[i] = particles[--particleCount];
        particles[particleCount] = p;
    }

    /**
     * Represents a particle
     */
    private class Particle {

        private PVector position = new PVector();
        private PVector velocity = new PVector();

        // Its alive util life is 0
        private float life = 255;
        private int particleColor;
        private float size;
        private float opacity = 255; // 0-255;
        private float opacityStep = 8.0f;
        private float lifeStep = 6.0f;
        
        /**
         * Constructor.
         */
        public Particle() {
            this.size = ParticleEmitter.this.randomRange(4, 20);
        }

        /**
         *
         */
        public void update(float timeDelta) {
            this.position.add(this.velocity.copy().mult(timeDelta));
            this.life -= lifeStep; //(timeDelta * lifeStep);
            this.opacity -= opacityStep; //(opacityStep + this.velocity.mag());
        }

        /**
         *
         */
        public void draw(PGraphics canvas) {
            canvas.fill(this.particleColor, this.opacity);
            canvas.ellipse(this.position.x, this.position.y, 3, 3);
            canvas.tint(this.particleColor, this.opacity);
            canvas.image(ParticleEmitter.this.particleImage, this.position.x, this.position.y, this.size, this.size);
        }

        /**
         * Resets the particle
         */
        public void reset(PVector position, PVector velocity, int pColor, float size, float opacityStep, float lifeStep) {
            this.position = position;
            this.velocity = velocity;
            this.particleColor = pColor;
            this.life = 255;
            this.size = size;
            this.opacity = 255;
            this.opacityStep = opacityStep + velocity.mag()/2.0f; // Make faster particles die quicker
            this.lifeStep = lifeStep + velocity.mag()/2.0f;
        }

        /**
         *
         */
        public boolean isAlive() {
            return this.life > 0;
        }
        
        /**
         * Kills the particle
         */
        public void kill(){
            this.life = 0;
        }

    }
    
}
