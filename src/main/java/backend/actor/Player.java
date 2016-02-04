package backend.actor;

import backend.item.Item;
import backend.item.ModuleContainer;
import backend.item.Parts;
import backend.main.GameEngine;
import backend.main.NumberCruncher;
import backend.main.Timer;
import backend.shipmodule.AutoCannon;
import backend.shipmodule.ShipModule;
import java.util.ArrayList;
import userinterface.Drawable;

/**
 * The player.
 *
 * @author Kristian Honningsvag.
 */
public class Player extends Actor implements Drawable {

    // Color.
    private int[] bodyRGBA = new int[]{0, 70, 200, 255};
    private int[] healthBarRGBA = new int[]{20, 200, 20, 255};
    private int healthBarWidth = 50;
    private int healthBarHeight = 10;
    private final int backgroundColor;  // Set in constructor.

    // Modules.
    private Timer offensiveModuleTimer = new Timer();
    private Timer defensiveModuleTimer = new Timer();
    private double offensiveModuleSwapDelay = 600;
    private double defensiveModuleSwapDelay = 600;
    
    private int parts = 0;

    /**
     * Constructor.
     */
    public Player(double positionX, double positionY, GameEngine gameEngine) {

        super(positionX, positionY, gameEngine);

        name = "Player";
        speedLimit = 0.6f;
        acceleration = 0.002f;
        drag = 0.001f;
        hitBoxRadius = 20;
        bounceModifier = 0.6f;
        maxHitPoints = 30;
        currentHitPoints = 30;
        mass = 100;
        backgroundColor = guiHandler.color(bodyRGBA[0], bodyRGBA[1], bodyRGBA[2], 255);
        collisionDamageToOthers = 4;

        offensiveModules.add(new AutoCannon(this));  // Starting weapon.
        currentOffensiveModule = offensiveModules.get(0);
    }

    @Override
    public void draw() {

        // Set heading.
        double xVector = guiHandler.mouseX - positionX;
        double yVector = guiHandler.mouseY - positionY;
        heading = NumberCruncher.calculateAngle(xVector, yVector);

        // Draw main body.
        guiHandler.strokeWeight(0);
        guiHandler.stroke(bodyRGBA[0], bodyRGBA[1], bodyRGBA[2]);
        guiHandler.fill(bodyRGBA[0], bodyRGBA[1], bodyRGBA[2]);
        guiHandler.ellipse((float) this.getPositionX(), (float) this.getPositionY(), (float) hitBoxRadius * 2, (float) hitBoxRadius * 2);

        // Draw modules.
        if (currentOffensiveModule != null) {
            currentOffensiveModule.draw();
        }
        if (currentDefensiveModule != null) {
            currentDefensiveModule.draw();
        }

        // Draw health bar.
        float healthPercentage = (float) currentHitPoints / (float) maxHitPoints;
        if (healthPercentage >= 0.66) {
            healthBarRGBA = new int[]{20, 200, 20, 255};
        } else if (healthPercentage < 0.66 && healthPercentage > 0.33) {
            healthBarRGBA = new int[]{200, 200, 20, 255};
        } else {
            healthBarRGBA = new int[]{200, 20, 20, 255};
        }
        guiHandler.strokeWeight(0);
        guiHandler.stroke(healthBarRGBA[0], healthBarRGBA[1], healthBarRGBA[2]);
        guiHandler.fill(healthBarRGBA[0], healthBarRGBA[1], healthBarRGBA[2]);
        guiHandler.rect((float) positionX - (healthBarWidth / 2), (float) positionY + (float) hitBoxRadius + 5, healthBarWidth * healthPercentage, healthBarHeight, 6);
    }

    @Override
    public void die() {
        gameEngine.getExplosionManager().explodePlayer(this);
        gameEngine.setSimulationState("deathScreen");
    }

    @Override
    protected void checkActorCollisions(double timePassed) {

        ArrayList<Actor> collisions = collisionDetector.detectActorCollision(this);

        if (collisions.size() > 0) {

            for (Actor target : collisions) {

                if ((target instanceof Projectile)) {
                    Projectile projectile = (Projectile) target;

                    if (projectile.getShipModule().getOwner() == this) {
                        // This player crashed into a projectile fired by itself.
                        // No damage. Players can't collide with their own projectiles.
                    }
                    else {
                        // This actor crashed into an unfriendly projectile.
                        elasticColision(this, target, timePassed);
                        this.collision(target);
                        target.collision(this);
                        projectile.targetHit();
                    }
                }
                
                else if (target instanceof Item) {
                    if (target instanceof ModuleContainer) {
                        ModuleContainer modulePickup = (ModuleContainer) target;
                        ShipModule shipModule = (ShipModule) modulePickup.pickup(this);
                        offensiveModules.add(shipModule);
                    }
                    else if (target instanceof Parts) {
                        Parts pickedUpParts = (Parts) target;
                        this.parts++;
                    }
                } else {
                    // This player crashed into an actor.
                    elasticColision(this, target, timePassed);
                    this.collision(target);
                    target.collision(this);
                }
            }
        }
    }

    /**
     * Activates the selected offensive ship module.
     */
    public void activateOffensiveModule() {
        currentOffensiveModule.activate();
    }

    /**
     * Activates the selected defensive ship module.
     */
    public void activateDefensiveModule() {
        currentDefensiveModule.activate();
    }

    /**
     * Changes to the next offensive module.
     */
    public void swapOffensiveModule() {
        // Wait for timer for each swap.
        if (offensiveModuleTimer.timePassed() >= offensiveModuleSwapDelay) {
            currentOffensiveModule = offensiveModules.get((offensiveModules.indexOf(currentOffensiveModule) + 1) % offensiveModules.size());
            offensiveModuleTimer.restart();
        }
    }

    /**
     * Changes to the next defensive module.
     */
    public void swapDefensiveModule() {
        // Wait for timer for each swap.
        if (defensiveModuleTimer.timePassed() >= defensiveModuleSwapDelay) {
            currentDefensiveModule = defensiveModules.get((defensiveModules.indexOf(currentDefensiveModule) + 1) % defensiveModules.size());
            defensiveModuleTimer.restart();
        }
    }

    /**
     * Increments the parts counter.
     */
    public void addParts(int numberOfParts) {
        parts = parts + numberOfParts;
    }

    public int getBackgroundColor() {
        return backgroundColor;
    }

}
