package backend.main;

import backend.actor.Frigate;
import backend.actor.Player;
import backend.actor.Rocket;
import processing.core.PGraphics;
import processing.core.PVector;

/**
 * The explosion manager manages all the explosions.
 *
 * It updates and draws all the particles used in the game.
 *
 * @author pitmairen
 */
public class ExplosionManager implements FadingCanvas.Drawable {

    private final ParticleEmitter particles; // Used for explosions

    /**
     * Constructs the explosions manager
     *
     * @param particles the particle that the explosion manager should use
     */
    public ExplosionManager(ParticleEmitter particles) {
        this.particles = particles;
    }

    /**
     * Updates the particles
     *
     * @param timePassed the time since the last update
     */
    public void update(double timePassed) {
        particles.update((float) timePassed / 25.0f);
    }

    /**
     * Draws the particles
     *
     * @param canvas
     */
    @Override
    public void draw(PGraphics canvas) {

        // ADD causes the colors of objects that are drawn on top of eachother
        // to be added together which creates a nice visual effect.
        canvas.blendMode(PGraphics.ADD);
        canvas.noStroke();

        // Draw the particles 
        particles.draw(canvas);

        // Reset blendMode
        canvas.blendMode(PGraphics.BLEND);

    }

    /**
     * Creates an explosion for a enemy frigate
     *
     * @param enemy the enemy that should explode
     */
    public void explodeEnemy(Frigate enemy) {
        this.particles.emitParticles(100,
                new PVector((float) enemy.getPosition().getX(), (float) enemy.getPosition().getY()), 0xffFF1511, 30.0f, 20.0f);
    }

    /**
     * Creates an explosion for a rocket.
     *
     * @param rocket the rocket that should explode
     */
    public void explodeRocket(Rocket rocket) {
        this.particles.emitParticles(50,
                new PVector((float) rocket.getPosition().getX(), (float) rocket.getPosition().getY()), rocket.getBackgroundColor(), 10f, 8f);
    }

    /**
     * Creates an explosion for the player if he dies.
     *
     * @param player the player that should explode.
     */
    public void explodePlayer(Player player) {
        this.particles.emitParticles(200,
                new PVector((float) player.getPosition().getX(), (float) player.getPosition().getY()), player.getBackgroundColor(), 30f, 20f, 2f, 2f);
    }

}
