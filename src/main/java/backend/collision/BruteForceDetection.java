package backend.collision;

import backend.actor.Actor;
import backend.main.GameEngine;
import java.util.List;

/**
 * Simple brute force collision detection.
 *
 * Checks each actor for collision with every other actor.
 *
 * @author pitmairen
 */
public class BruteForceDetection extends BaseDetection {

    private int collisionChecks = 0;

    public BruteForceDetection(GameEngine engine, CollisionListener listener) {
        super(engine, listener);
    }

    @Override
    public void detectCollisions() {
        collisionChecks = 0;
        List<Actor> actors = gameEngine.getCurrentLevel().getActors();
        int size = actors.size();
        for (int i = 0; i < size; i++) {
            checkWallCollision(actors.get(i));
            // start at i+1 to check each pair only once.
            for (int j = i + 1; j < size; j++) { 
                collisionChecks++;
                checkCollision(actors.get(i), actors.get(j));
            }
        }
    }

    @Override
    public int getCollisionCheckCount() {
        return collisionChecks;
    }
}
