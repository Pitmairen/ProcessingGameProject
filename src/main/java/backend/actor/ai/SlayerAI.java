package backend.actor.ai;

import backend.actor.Actor;
import backend.main.Timer;
import backend.main.Vector;
import java.util.Random;

/**
 * The slayer is very smart.
 *
 * The slayer approaches the target until it reaches a suitable distance to
 * begin firing. During the attack it moves sideways to evade enemy fire. It
 * tries to keep a safe distance to the target. If all hope is lost, it 
 * will resort to a final kamikaze attack.
 *
 * Implemented as a finite state machine.
 *
 * @author pitmairen
 */
public class SlayerAI implements AI {

    private State currentState;

    private final Actor puppet;
    private final Actor target;

    private final double attackDelay = 1500;
    private final Timer fireTimer;
    private int fireVariance; // Vary the time between shots.
    
    private final Random random;
    
    private Vector attackVector;
    
    /**
     * Create a new AI
     *
     * @param puppet the actor to be controlled
     * @param target the target actor
     */
    public SlayerAI(Actor puppet, Actor target) {
        this.target = target;
        this.puppet = puppet;
        random = new Random();
        fireTimer = new Timer();
        setState(new ApproachTarget());
        attackVector = new Vector(0, 0, 0);
    }

    @Override
    public void updateBehaviour(double timePassed) {
        attackVector = Vector.sub(target.getPosition(), puppet.getPosition());
        puppet.getHeading().set(attackVector);
        currentState.baseUpdate(timePassed);
    }

    private void setState(State state) {
        currentState = state;
    }

    private double getTargetDistance() {
        return attackVector.mag();
    }

    private void accelerateTowardsTarget() {
        acceleratePuppet(attackVector.copy());
    }

    private void acceleratePuppet(Vector direction) {
        puppet.applyForce(direction.normalize().mult(puppet.getEngineThrust()));
    }

    private void fireAtTarget() {
        if (fireTimer.timePassed() > (attackDelay + fireVariance)) {
            puppet.getCurrentOffensiveModule().activate();
            fireTimer.restart();
            fireVariance = getRandomNumber(0, 1000);
        }
    }

    private int getRandomNumber(int min, int max) {
        return min + random.nextInt((max - min) + 1);
    }

    /**
     * Represents the various states the AI can be in.
     */
    private abstract class State {

        public void baseUpdate(double timePassed) {
            if (puppet.getCurrentHitPoints() < 5) {
                setState(new Kamikaze());
            }
            update(timePassed);
        }

        public abstract void update(double timePassed);
    }

    /**
     * Move towards the target
     */
    private class ApproachTarget extends State {

        // When to start firing
        private final int distanceThreshold;

        public ApproachTarget() {
            this.distanceThreshold = getRandomNumber(400, 600);
        }

        @Override
        public void update(double timePassed) {

            if (getTargetDistance() < distanceThreshold) {
                setState(new DistanceAttack(distanceThreshold + 200));
            } else {
                accelerateTowardsTarget();
            }
        }
    }

    /**
     * Shoot at the target and move sideways
     */
    private class DistanceAttack extends State {

        // If the distance goes above the threashold we approach the player again.
        private final int distanceThreshold;

        // Make sideways movements
        private final Timer moveTimer;
        private final int moveTimeout; // how long to move in each direction
        private int direction = 1; // 1 or -1

        private DistanceAttack(int distanceThreshold) {
            this.distanceThreshold = distanceThreshold;
            moveTimer = new Timer();
            moveTimeout = getRandomNumber(1000, 1500);
        }

        @Override
        public void update(double timePassed) {

            // Switch direction
            if (moveTimer.timePassed() > moveTimeout) {
                direction *= -1;
                moveTimer.restart();
            }

            // Move sideways
            Vector dir = attackVector.copy().rotate(Math.PI / 2); // normal vector
            acceleratePuppet(dir.mult(direction));

            fireAtTarget();

            double distance = getTargetDistance();
            if (distance > distanceThreshold) {
                setState(new ApproachTarget());
            } else if (distance < 200) {
                setState(new Flee());
            }
        }
    }

    /**
     * Move away from the target
     */
    private class Flee extends State {

        @Override
        public void update(double timePassed) {

            acceleratePuppet(attackVector.copy().mult(-1));

            if (getTargetDistance() >= 300) {
                setState(new DistanceAttack(getRandomNumber(400, 800)));
            }
        }
    }

    /**
     * Nothing left to lose
     */
    private class Kamikaze extends State {

        @Override
        public void update(double timePassed) {
            accelerateTowardsTarget();
            fireAtTarget();
        }
    }
}
