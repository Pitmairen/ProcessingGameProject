package backend.actor.enemy;

import backend.actor.Actor;
import backend.actor.projectile.Projectile;
import backend.item.Item;
import backend.item.Parts;
import backend.main.GameEngine;
import backend.main.Vector;
import backend.resources.Sound;
import backend.shipmodule.DroneLauncher;
import java.util.ArrayList;
import processing.core.PImage;
import userinterface.Drawable;

/**
 * A boss NPC. Big and dangerous. Has several attack patterns and a a lot of hit
 * points.
 *
 * @author Kristian Honningsvag.
 */
public class Boss extends Enemy implements Drawable {

    // Color.
    private int[] bodyRGBA = new int[]{200, 30, 30, 255};
    private int[] healthBarRGBA = new int[]{20, 200, 20, 255};

    // Shape.
    private int healthBarWidth = 0;
    private int healthBarHeight = 0;

    //Image
    private final PImage enemyGraphics;

    // Modules.
    private DroneLauncher DroneLauncher = new DroneLauncher(this);

    /**
     * Constructor.
     */
    public Boss(Vector position, GameEngine gameEngine) {

        super(position, gameEngine);

        name = "Boss";
        engineThrust = 0.2f;
        frictionCoefficient = 0.29f;
        hitBoxRadius = 75;
        bounceModifier = 0.4f;
        maxHitPoints = 300;
        currentHitPoints = 300;
        mass = 300;
        collisionDamageToOthers = 20;
        attackDelay = 200;
        killValue = 1;

        offensiveModules.add(DroneLauncher);
        currentOffensiveModule = DroneLauncher;

        enemyGraphics = guiHandler.loadImage("Actors/DroneV2Lrg.png");

        healthBarWidth = (int) hitBoxRadius * 2;
        healthBarHeight = (int) (healthBarWidth * 0.1);
    }

    @Override
    public void draw() {

        // Draw main body.
//        guiHandler.strokeWeight(0);
//        guiHandler.stroke(bodyRGBA[0], bodyRGBA[1], bodyRGBA[2]);
//        guiHandler.fill(bodyRGBA[0], bodyRGBA[1], bodyRGBA[2]);
//        guiHandler.ellipse((float) this.getPosition().getX(), (float) this.getPosition().getY(), (float) hitBoxRadius * 2, (float) hitBoxRadius * 2);
        guiHandler.tint(255);
        guiHandler.image(enemyGraphics, (float) this.getPosition().getX() - 75, (float) this.getPosition().getY() - 75);

        // Draw modules.
        if (currentOffensiveModule != null) {
            currentOffensiveModule.draw();
        }
        if (currentDefensiveModule != null) {
            currentDefensiveModule.draw();
        }

        // Draw small hud below actor.
        guiHandler.pushMatrix();
        guiHandler.translate((float) this.getPosition().getX() - (healthBarWidth / 2), (float) this.getPosition().getY() + (float) hitBoxRadius + healthBarHeight / 2);

        // Health bar.
        float healthPercentage = (float) currentHitPoints / (float) maxHitPoints;
        if (healthPercentage >= 0.66) {
            healthBarRGBA = new int[]{20, 200, 20, 255};
        } else if (healthPercentage < 0.66 && healthPercentage >= 0.33) {
            healthBarRGBA = new int[]{200, 200, 20, 255};
        } else {
            healthBarRGBA = new int[]{200, 20, 20, 255};
        }
        guiHandler.stroke(healthBarRGBA[0], healthBarRGBA[1], healthBarRGBA[2]);
        guiHandler.strokeWeight(1);
        guiHandler.noFill();
        guiHandler.rect((float) 0, 0, healthBarWidth, healthBarHeight, 6);
        guiHandler.fill(healthBarRGBA[0], healthBarRGBA[1], healthBarRGBA[2]);
        guiHandler.rect(0, 0, healthBarWidth * healthPercentage, healthBarHeight, 3);

        guiHandler.popMatrix();
    }

    @Override
    public void die() {
        gameEngine.getSoundManager().play(Sound.EXPLOSION, getPosition());
        gameEngine.getExplosionManager().explodeEnemy(this);
        gameEngine.getCurrentLevel().getPlayer().increaseScore(this.killValue);
        gameEngine.getCurrentLevel().getPlayer().increaseKillChain(1);
        gameEngine.getCurrentLevel().getEnemies().remove(this);

        // Spawn parts.
        Parts parts = new Parts(new Vector(this.getPosition().getX(), this.getPosition().getY(), 0), gameEngine);
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
                    if (target instanceof KamikazeDrone) {
                        // No interaction with drones.    
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
