/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package backend.main;

import backend.actor.Fireball;
import backend.actor.Frigate;
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
    public ExplosionManager(ParticleEmitter particles){
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
    public void explodeEnemy(Frigate enemy){
        this.particles.emitParticles(100,
                new PVector((float)enemy.getPositionX(), (float)enemy.getPositionY()), 0xffFF1511, 40.0f, 50.0f);
    }
    
    /**
     * Creates an explosion for a fireball
     * 
     * @param ball the fireball that should explode
     */
    public void explodeFireball(Fireball ball){
        this.particles.emitParticles(50,
                new PVector((float)ball.getPositionX(), (float)ball.getPositionY()), ball.getBackgroundColor());
    }


    
}
