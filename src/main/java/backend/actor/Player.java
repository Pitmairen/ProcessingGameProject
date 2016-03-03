package backend.actor;

import backend.item.Item;
import backend.main.GameEngine;
import backend.main.SimulationState;
import backend.main.Timer;
import backend.main.Vector;
import backend.resources.Sound;
import backend.shipmodule.AutoCannon;
import backend.shipmodule.Shield;
import backend.shipmodule.ShipModule;
import java.util.ArrayList;
import processing.core.PImage;
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
    private int[] energyBarRGBA = new int[]{20, 20, 200, 255};

    // Shape.
    private int healthBarWidth = 50;
    private int healthBarHeight = 10;
    private int energyBarWidth = 50;
    private int energyBarHeight = 10;
    private final int backgroundColor;  // Set in constructor.

    // Image.
    private final PImage playerGraphics;

    // Modules.
    private Timer offensiveModuleTimer = new Timer();
    private Timer defensiveModuleTimer = new Timer();
    private double offensiveModuleSwapDelay = 600;
    private double defensiveModuleSwapDelay = 600;

    /**
     * Constructor.
     */
    public Player(Vector position, GameEngine gameEngine) {

        super(position, gameEngine);

        name = "Player";
        engineThrust = 0.2f;
        frictionCoefficient = 0.4;
        hitBoxRadius = 20;
        bounceModifier = 0.8f;
        maxHitPoints = 60;
        currentHitPoints = 60;
        maxEnergy = 100;
        currentEnergy = 100;
        mass = 100;
        backgroundColor = guiHandler.color(bodyRGBA[0], bodyRGBA[1], bodyRGBA[2], 255);
        collisionDamageToOthers = 4;

        offensiveModules.add(new AutoCannon(this));  // Starting weapon.
        setCurrentOffensiveModule(offensiveModules.get(0));

        defensiveModules.add(new Shield(this));
        setCurrentDefensiveModule(defensiveModules.get(0));

        playerGraphics = guiHandler.loadImage("drone.png");

        healthBarWidth = (int) hitBoxRadius * 2;
        healthBarHeight = (int) (healthBarWidth * 0.2);
        energyBarWidth = (int) hitBoxRadius * 2;
        energyBarHeight = (int) (healthBarWidth * 0.2);
    }

    @Override
    public void draw() {

        // Set heading.
        heading.set(guiHandler.mouseX - this.getPosition().getX(), guiHandler.mouseY - this.getPosition().getY(), 0);

        // Draw main body.
//        guiHandler.strokeWeight(0);
//        guiHandler.stroke(bodyRGBA[0], bodyRGBA[1], bodyRGBA[2]);
//        guiHandler.fill(bodyRGBA[0], bodyRGBA[1], bodyRGBA[2]);
//        guiHandler.ellipse((float) this.getPosition().getX(), (float) this.getPosition().getY(), (float) hitBoxRadius * 2, (float) hitBoxRadius * 2);
        guiHandler.tint(255);
        guiHandler.image(playerGraphics, (float) this.getPosition().getX() - 20, (float) this.getPosition().getY() - 20);

        // Draw modules.
        if (currentOffensiveModule != null) {
            currentOffensiveModule.draw();
        }
        if (currentDefensiveModule != null) {
            currentDefensiveModule.draw();
        }
        if (tacticalModule != null) {
            tacticalModule.draw();
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

        // Energy bar.
        float energyPercentage = (float) currentEnergy / (float) maxEnergy;

        guiHandler.stroke(energyBarRGBA[0], energyBarRGBA[1], energyBarRGBA[2]);
        guiHandler.strokeWeight(1);
        guiHandler.noFill();
        guiHandler.rect((float) 0, healthBarHeight + 2, energyBarWidth, energyBarHeight, 6);
        guiHandler.fill(energyBarRGBA[0], energyBarRGBA[1], energyBarRGBA[2]);
        guiHandler.rect(0, healthBarHeight + 2, energyBarWidth * energyPercentage, energyBarHeight, 3);

        guiHandler.popMatrix();
    }

    @Override
    public void die() {
        gameEngine.getSoundManager().play(Sound.EXPLOSION, getPosition());
        gameEngine.getExplosionManager().explodePlayer(this);
        gameEngine.setSimulationState(SimulationState.DEATH_SCREEN);
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
                    } else {
                        // This actor crashed into an unfriendly projectile.
                        elasticColision(this, target, timePassed);
                        this.collision(target);
                        target.collision(this);
                        projectile.targetHit();
                    }
                } else if (target instanceof Item) {
                    ((Item) target).pickup(this);
                } else if (target instanceof Shield.ShieldActor && ((Shield.ShieldActor) target).getOwner() == this) {
                    // The player doesn't collide with its own shield
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
        if (currentOffensiveModule != null) {
            currentOffensiveModule.activate();
        }
    }

    /**
     * Activates the selected defensive ship module.
     */
    public void activateDefensiveModule() {
        if (currentDefensiveModule != null) {
            currentDefensiveModule.activate();
        }
    }

    /**
     * Activates the tactical ship module.
     */
    public void activateTacticalModule() {
        if (tacticalModule != null) {
            tacticalModule.activate();
        }
    }

    /**
     * Changes to the next offensive module.
     */
    public void swapOffensiveModule() {
        // Wait for timer for each swap.
        if (offensiveModuleTimer.timePassed() >= offensiveModuleSwapDelay) {
            ShipModule module = offensiveModules.get((offensiveModules.indexOf(currentOffensiveModule) + 1) % offensiveModules.size());
            setCurrentOffensiveModule(module);
            offensiveModuleTimer.restart();
        }
    }

    /**
     * Changes to the next defensive module.
     */
    public void swapDefensiveModule() {
        // Wait for timer for each swap.
        if (defensiveModuleTimer.timePassed() >= defensiveModuleSwapDelay) {
            ShipModule module = defensiveModules.get((defensiveModules.indexOf(currentDefensiveModule) + 1) % defensiveModules.size());
            setCurrentDefensiveModule(module);
            defensiveModuleTimer.restart();
        }
    }

    public int getBackgroundColor() {
        return backgroundColor;
    }
    
}
