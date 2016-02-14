package backend.actor;

import backend.item.Parts;
import backend.main.GameEngine;
import backend.main.Vector;
import backend.shipmodule.LightCannon;
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
    private int[] healthBarRGBA = new int[]{20, 200, 20, 255};

    // Shape.
    private int healthBarWidth = 30;
    private int healthBarHeight = 7;

    //Image
    private final PImage enemyGraphics;

    // Modules.
    private LightCannon LightCannon = new LightCannon(this);

    /**
     * Constructor.
     */
    public Frigate(Vector position, GameEngine gameEngine) {

        super(position, gameEngine);

        name = "Frigate";
        engineThrust = 0.02f;
        frictionCoefficient = 0.04f;
        hitBoxRadius = 15;
        bounceModifier = 0.6f;
        maxHitPoints = 10;
        currentHitPoints = 10;
        mass = 25;
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
//        guiHandler.strokeWeight(0);
//        guiHandler.stroke(bodyRGBA[0], bodyRGBA[1], bodyRGBA[2]);
//        guiHandler.fill(bodyRGBA[0], bodyRGBA[1], bodyRGBA[2]);
//        guiHandler.ellipse((float) this.getPosition().getX(), (float) this.getPosition().getY(), (float) hitBoxRadius * 2, (float) hitBoxRadius * 2);
        guiHandler.tint(255);
        guiHandler.image(enemyGraphics, (float) this.getPosition().getX() - 15, (float) this.getPosition().getY() - 15);

        // Draw modules.
        if (currentOffensiveModule != null) {
            currentOffensiveModule.draw();
        }
        if (currentDefensiveModule != null) {
            currentDefensiveModule.draw();
        }

        // Health bar.
        guiHandler.pushMatrix();
        guiHandler.translate((float) this.getPosition().getX() - (healthBarWidth / 2), (float) this.getPosition().getY() + (float) hitBoxRadius + 7);

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
        gameEngine.getExplosionManager().explodeEnemy(this);
        gameEngine.getCurrentLevel().getPlayer().increaseScore(this.killValue);
        gameEngine.getCurrentLevel().getPlayer().increaseKillChain(1);
        gameEngine.getCurrentLevel().getEnemies().remove(this);

        // Spawn parts.
        Parts parts = new Parts(new Vector(this.getPosition().getX(), this.getPosition().getY(), 0), gameEngine);
        gameEngine.getCurrentLevel().getItems().add(parts);
        gameEngine.getCurrentLevel().getActors().add(parts);
    }
}
