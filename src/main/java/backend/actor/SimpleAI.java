package backend.actor;

import backend.main.GameEngine;
import backend.main.Vector;
import java.util.Random;

/**
 *
 * @author dogsh
 */
public class SimpleAI implements AI {


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
        heading = target.getPosition().copy().sub(enemy.getPosition());
        enemy.getHeading().set(heading);
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
    protected void approachTarget() {
        setSpeeds();
    }

    /**
     * Accelerate near the target, but not into the target.
     *
     * @param timePassed
     */
    private void circleAroundTarget(int minDistance) {

        double distance = heading.mag();
        if (distance <= minDistance) {
            heading.rotate(Math.PI/2.5);
        }
        approachTarget();
    }

    private void setSpeeds() {
        enemy.applyForce(heading.normalize().mult(enemy.getEngineThrust()));
    }

    protected void createMovement(double timePassed) {
        circleAroundTarget(600);
    }
}
