package backend.actor;

import backend.item.Item;
import backend.main.GameEngine;
import backend.main.Vector;
import backend.resources.Sound;
import backend.shipmodule.LightCannon;
import java.util.ArrayList;
import processing.core.PImage;
import userinterface.Drawable;

/**
 * A simple drone. Attempts to crash into the player while simultaneously
 * shooting at it.
 *
 * @author Kristian Honningsvag.
 */
public class Drone extends Enemy implements Drawable {

    //Image
    private final PImage enemyGraphics;

    // Modules.
    private LightCannon LightCannon = new LightCannon(this);

    /**
     * Constructor.
     */
    public Drone(Vector position, GameEngine gameEngine) {

        super(position, gameEngine);

        name = "Drone";
        engineThrust = 0.02f;
        frictionCoefficient = 0.04f;
        hitBoxRadius = 15;
        bounceModifier = 0.6f;
        maxHitPoints = 3;
        currentHitPoints = 3;
        mass = 10;
        collisionDamageToOthers = 4;
        attackDelay = 1000;
        killValue = 1;

        offensiveModules.add(LightCannon);
        currentOffensiveModule = LightCannon;

        enemyGraphics = guiHandler.loadImage("multishotDrone.png");
    }

    @Override
    public void draw() {

        // Draw main body.
        guiHandler.tint(255);
        guiHandler.image(enemyGraphics, (float) this.getPosition().getX() - 15, (float) this.getPosition().getY() - 15);

        // Draw modules.
        if (currentOffensiveModule != null) {
            currentOffensiveModule.draw();
        }
        if (currentDefensiveModule != null) {
            currentDefensiveModule.draw();
        }
    }

    @Override
    public void die() {
        gameEngine.getSoundManager().play(Sound.EXPLOSION, getPosition());
        gameEngine.getExplosionManager().explodeEnemy(this);
        gameEngine.getCurrentLevel().getPlayer().increaseScore(this.killValue);
        gameEngine.getCurrentLevel().getPlayer().increaseKillChain(1);
        gameEngine.getCurrentLevel().getEnemies().remove(this);
    }

    @Override
    protected void checkActorCollisions(double timePassed) {

        ArrayList<Actor> collisions = collisionDetector.detectActorCollision(this);

        if (collisions.size() > 0) {
            for (Actor target : collisions) {

                if ((target instanceof Projectile)) {
                    Projectile projectile = (Projectile) target;

                    if (projectile.getShipModule().getOwner() == this) {
                        // No damage from your own projectiles.
                    } else if (projectile.getShipModule().getOwner() instanceof Enemy) {
                        // No damage from other enemy projectiles.
                    } else {
                        // Crashed into an unfriendly projectile.
                        elasticColision(this, target, timePassed);
                        this.collision(target);
                        target.collision(this);
                        projectile.targetHit();
                    }
                } else if (target instanceof Item) {
                    // No interaction with items.
                } else if (target instanceof Enemy) {
                    if (target instanceof Boss) {
                        // No interaction with boss enemies.    
                    } else {
                        // Crashed into some other enemy.
                        elasticColision(this, target, timePassed);
                    }
                } else {
                    // Crashed into some other actor.
                    elasticColision(this, target, timePassed);
                    this.collision(target);
                    target.collision(this);
                }
            }
        }
    }

}
