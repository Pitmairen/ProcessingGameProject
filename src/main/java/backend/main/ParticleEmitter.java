package backend.main;

import java.util.Random;
import processing.core.PGraphics;
import processing.core.PImage;
import processing.core.PVector;
import userinterface.GUIHandler;

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
    // The current position of the first live particle 
    private int firstPosition = 0;

    private final Random random = new Random();

    // Background image used for the particles
    private final PImage particleImage;

    public ParticleEmitter(GUIHandler gui) {

        particleImage = gui.loadImage("particle.png");

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

        int start = (this.firstPosition + this.particleCount) % PARTICLE_LIMIT;

        for (int i = 0; i < count; i++) {
            this.particles[(start + i) % PARTICLE_LIMIT].reset(
                    position.copy(),
                    PVector.random2D().mult(this.randomRange(0.2f, 10.0f)), // Velocity
                    particleColor,
                    this.randomRange(2.0f, 15.0f) // Particle size
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

        int start = this.firstPosition;

        for (int i = 0; i < this.particleCount; i++) {
            int pos = (start + i) % PARTICLE_LIMIT;

            if (this.particles[pos].isAlive()) {
                this.particles[pos].update(timeDelta);
            } else {
                this.particleCount--;
                this.firstPosition = pos;
            }
        }
    }

    /**
     * Draws the particles onto the canvas
     *
     * @param canvas the canvas to draw to
     */
    public void draw(PGraphics canvas) {

        int start = this.firstPosition;

        for (int i = 0; i < this.particleCount; i++) {
            int pos = (start + i) % PARTICLE_LIMIT;

            if (this.particles[pos].isAlive()) {
                this.particles[pos].draw(canvas);

            }
        }
    }

    private float randomRange(float min, float max) {
        return min + this.random.nextFloat() * ((max - min) + 1);
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

        public Particle() {
            this.size = ParticleEmitter.this.randomRange(4, 20);
        }

        public void update(float timeDelta) {
            this.position.add(this.velocity.copy().mult(timeDelta));
            this.life -= (timeDelta * 5);
            this.opacity -= (5 + this.velocity.mag());
        }

        public void draw(PGraphics canvas) {

            canvas.fill(this.particleColor, this.opacity);
            canvas.ellipse(this.position.x, this.position.y, 3, 3);
            canvas.tint(this.particleColor, this.opacity);
            canvas.image(ParticleEmitter.this.particleImage, this.position.x, this.position.y, this.size, this.size);
        }

        /**
         * Resets the particle
         */
        public void reset(PVector position, PVector velocity, int pColor, float size) {
            this.position = position;
            this.velocity = velocity;
            this.particleColor = pColor;
            this.life = 255;
            this.size = size;
            this.opacity = 255;
        }

        public boolean isAlive() {
            return this.life > 0;
        }

    }
}
