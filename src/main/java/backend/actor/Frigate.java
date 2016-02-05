package backend.actor;

import backend.item.Item;
import backend.item.Parts;
import backend.main.GameEngine;
import backend.shipmodule.LightCannon;
import java.util.ArrayList;
import processing.core.PImage;
import userinterface.Drawable;

/**
 * A frigate. Small and fast.
 *
 * @author Kristian Honningsvag.
 */
public class Frigate extends Enemy implements Drawable {

    // Color.
    private int[] bodyRGBA = new int[]{200, 30, 30, 255};

    // Modules.
    private LightCannon LightCannon = new LightCannon(this);
    
    //Image
    private final PImage enemyGraphics; 
    /**
     * Constructor.
     */
    public Frigate(double positionX, double positionY, GameEngine gameEngine) {

        super(positionX, positionY, gameEngine);

        name = "Frigate";
        speedLimit = 0.4f;
        acceleration = 0.0015f;
        drag = 0.001f;
        hitBoxRadius = 15;
        bounceModifier = 0.6f;
        currentHitPoints = 10;
        mass = 30;
        collisionDamageToOthers = 2;
        attackDelay = 2000;
        killValue = 1;

        offensiveModules.add(LightCannon);
        currentOffensiveModule = LightCannon;
        
        enemyGraphics = guiHandler.loadImage("multishotDrone.png");
    }

    @Override
    public void draw() {

        // Draw main body.
        //guiHandler.strokeWeight(0);
        //guiHandler.stroke(bodyRGBA[0], bodyRGBA[1], bodyRGBA[2]);
        //guiHandler.fill(bodyRGBA[0], bodyRGBA[1], bodyRGBA[2]);
        //guiHandler.ellipse((float) this.getPositionX(), (float) this.getPositionY(), (float) hitBoxRadius * 2, (float) hitBoxRadius * 2);
        guiHandler.image(enemyGraphics,(float) this.getPositionX()-15, (float) this.getPositionY()-15);
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
        gameEngine.getExplosionManager().explodeEnemy(this);
        gameEngine.getCurrentLevel().getPlayer().increaseScore(this.killValue);
        gameEngine.getCurrentLevel().getPlayer().increaseKillChain(1);
        gameEngine.getCurrentLevel().getEnemies().remove(this);

        // Spawn parts.
        Parts parts = new Parts(positionX, positionY, gameEngine);
        gameEngine.getCurrentLevel().getItems().add(parts);
        gameEngine.getCurrentLevel().getActors().add(parts);
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
                    }
                    else if (projectile.getShipModule().getOwner() instanceof Enemy) {
                        // No damage from other enemy projectiles.
                    }
                    else {
                        // Crashed into an unfriendly projectile.
                        elasticColision(this, target, timePassed);
                        this.collision(target);
                        target.collision(this);
                        projectile.targetHit();
                    }
                }
                else if (target instanceof Item) {
                    // No interaction with items.
                }
                else if (target instanceof Enemy) {
                    elasticColision(this, target, timePassed);
                }
                else {
                    // Crashed into some other actor.
                    elasticColision(this, target, timePassed);
                    this.collision(target);
                    target.collision(this);
                }
            }
        }
    }

}
