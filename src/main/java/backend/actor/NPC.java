package backend.actor;

import backend.main.GameEngine;
import backend.main.NumberCruncher;
import java.util.Random;
import userinterface.Drawable;

/**
 * Super class for all NPC's.
 *
 * @author Kristian Honningsvag.
 */
public abstract class NPC extends Actor implements Drawable {

    // AI configuration.
    protected Random random = new Random();
    protected float attackDelay = 0;
    protected float attackDelayFactor = random.nextFloat() + 1;
    protected double lastTimeFired = 0;
    protected boolean isHostile = true;

    /**
     * Constructor.
     */
    protected NPC(double positionX, double positionY, GameEngine gameEngine) {

        super(positionX, positionY, gameEngine);
    }

    /**
     * Sets heading towards the players location.
     */
    protected void targetPlayerLocation() {
        double xVector = gameEngine.getCurrentLevel().getPlayer().getPositionX() - this.positionX;
        double yVector = gameEngine.getCurrentLevel().getPlayer().getPositionY() - this.positionY;
        heading = NumberCruncher.calculateAngle(xVector, yVector);
    }

    /**
     * Fires a bullet towards the player.
     */
    protected void fireAtPlayer() {

        if (System.currentTimeMillis() - lastTimeFired > attackDelay * attackDelayFactor) {

            currentOffensiveModule.activate();
            lastTimeFired = System.currentTimeMillis();
            attackDelayFactor = random.nextFloat() + 1;
        }
    }

    /**
     * Accelerate towards the target.
     */
    protected void approachTarget(double timePassed) {

        if (speedT < speedLimit) {
            speedX = speedX + (acceleration * Math.cos(heading) * timePassed);
            speedY = speedY + (acceleration * Math.sin(heading) * timePassed);
        }
    }

}
