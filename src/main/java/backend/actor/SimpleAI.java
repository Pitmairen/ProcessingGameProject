package backend.actor;

import backend.main.GameEngine;
import backend.main.Vector;
import java.util.Random;

/**
 *
 * @author dogsh
 */
public class SimpleAI implements AI {

    private double xVector = 0;
    private double yVector = 0;
    private double timeReference = 0;
    private GameEngine gameEngine;
    private Actor target;
    private Vector heading = new Vector();
    private Enemy enemy;

    private double lastTimeFired = 0;
    private double attackDelayFactor = 1;
    private double attackDelay = 2000;
    private Random random;

    public SimpleAI(GameEngine gameEngine, Actor target, Enemy enemy) {
        this.gameEngine = gameEngine;
        this.target = target;
        this.enemy = enemy;
        random = new Random();
    }

    @Override
    public void updateBehaviour(double timePassed) {
        targetPlayerLocation();
        fireAtPlayer();
        createMovement(timePassed);
    }

    /**
     * Sets heading towards the players location.
     */
    private void targetPlayerLocation() {
        heading.set(target.getPosition().getX() - enemy.getPosition().getX(), target.getPosition().getY() - enemy.getPosition().getY(), 0);
        enemy.getHeading().set(target.getPosition().getX() - enemy.getPosition().getX(), target.getPosition().getY() - enemy.getPosition().getY(), 0);
    }

    /**
     * Fires a bullet towards the player.
     */
    private void fireAtPlayer() {

        if (System.currentTimeMillis() - lastTimeFired > attackDelay * attackDelayFactor) {

            enemy.getCurrentOffensiveModule().activate();
            lastTimeFired = System.currentTimeMillis();
            attackDelayFactor = random.nextFloat() + 1;
        }
    }

    /**
     * Accelerate into the target.
     */
    protected void approachTarget(double timePassed) {
        setSpeeds(timePassed);
    }

    /**
     * Accelerate near the target, but not into the target.
     *
     * @param timePassed
     */
    private void circleAroundTarget(double timePassed, int minDistance) {

        double distance = Math.sqrt(Math.pow(xVector, 2) + Math.pow(yVector, 2)); //Hypotenus calculated

        if (distance > minDistance) {
            timeReference = timePassed;
            approachTarget(timePassed);
        } else if (distance <= minDistance) {
            enemy.getHeading().rotate(Math.PI / 4);
            setSpeeds(timePassed);
        }
    }

    private void setSpeeds(double timePassed) {
//        double speedX = enemy.getSpeed().getX() + (enemy.getAccelerationTemp() * Math.cos(heading) * timePassed);
//        enemy.getSpeed().setX(speedX);
//        double speedY = enemy.getSpeed().getY() + (enemy.getAccelerationTemp() * Math.sin(heading) * timePassed);
//        enemy.getSpeed().setY(speedY);
    }

    protected void createMovement(double timePassed) {
        circleAroundTarget(timePassed, 600);
    }
}
