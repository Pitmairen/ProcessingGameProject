package backend.actor;

import backend.main.GameEngine;
import backend.main.Vector;
import java.util.Random;

/**
 * AI for the drone enemy type.
 *
 * @author Kristian Honningsvag.
 */
public class DroneAI implements AI {

    private GameEngine gameEngine;
    private Actor target;
    private Vector heading = new Vector();
    private Enemy enemy;

    private double lastTimeFired = 0;
    private double attackDelayFactor = 1;
    private double attackDelay = 2000;
    private Random random;

    public DroneAI(GameEngine gameEngine, Actor target, Enemy enemy) {
        this.gameEngine = gameEngine;
        this.target = target;
        this.enemy = enemy;
        random = new Random();
    }

    @Override
    public void updateBehaviour(double timePassed) {
        targetPlayerLocation();
        fireAtPlayer();
        approachTarget();
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

            if (enemy.getCurrentOffensiveModule() != null) {
                enemy.getCurrentOffensiveModule().activate();
                lastTimeFired = System.currentTimeMillis();
                attackDelayFactor = random.nextFloat() + 1;
            }
        }
    }

    /**
     * Accelerate towards the current heading.
     */
    private void approachTarget() {
        enemy.applyForce(heading.normalize().mult(enemy.getEngineThrust()));
    }

}
