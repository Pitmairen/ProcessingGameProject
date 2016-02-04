package backend.actor;

import backend.main.GameEngine;
import backend.main.NumberCruncher;
import java.util.Random;
import userinterface.Drawable;

/**
 * Super class for all enemies in the game.
 *
 * @author Kristian Honningsvag.
 */
public abstract class Enemy extends Actor implements Drawable {

    // AI configuration.
    protected Random random = new Random();
    protected float attackDelay = 0;
    protected float attackDelayFactor = random.nextFloat() + 1;
    protected double lastTimeFired = 0;
    protected boolean isHostile = true;
    protected double xVector = 0;
    protected double yVector = 0;
    protected double timeReference = 0;

    /**
     * Constructor.
     */
    protected Enemy(double positionX, double positionY, GameEngine gameEngine) {

        super(positionX, positionY, gameEngine);
    }

    /**
     * Sets heading towards the players location.
     */
    protected void targetPlayerLocation() {

        xVector = gameEngine.getCurrentLevel().getPlayer().getPositionX() - this.positionX;
        yVector = gameEngine.getCurrentLevel().getPlayer().getPositionY() - this.positionY;
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
     * Accelerate into the target.
     */
    protected void approachTarget(double timePassed) {
        if (speedT < speedLimit) {
            setSpeeds(timePassed);
        }
    }

    /**
     * Accelerate near the target, but not into the target.
     *
     * @param timePassed
     */
    protected void positionNearTarget(double timePassed, int minDistance) {

        double distance = Math.sqrt(Math.pow(xVector, 2) + Math.pow(yVector, 2)); //Hypotenus calculated

        if (speedT < speedLimit && distance > minDistance) {
            timeReference = timePassed;
            setSpeeds(timePassed);
        }else if(distance <= minDistance ){
            if(timePassed - timeReference < 500){
                heading -= Math.PI/4;
                setSpeeds(timePassed);
            }else if(timePassed - timeReference < 1000){
                heading -= 2*Math.PI/4;
                setSpeeds(timePassed);
            }else{
                timeReference = timePassed;
            }
        }
    }

    private void setSpeeds(double timePassed){
        speedX = speedX + (acceleration * Math.cos(heading) * timePassed);
        speedY = speedY + (acceleration * Math.sin(heading) * timePassed);
    }
}
