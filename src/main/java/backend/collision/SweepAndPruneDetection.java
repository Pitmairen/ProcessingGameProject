package backend.collision;

import backend.actor.Actor;
import backend.main.GameEngine;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Collision detection using the sweep and prune algorithm.
 *
 * See:
 * <a href="@https://en.wikipedia.org/wiki/Sweep_and_prune">@https://en.wikipedia.org/wiki/Sweep_and_prune</a>
 *
 * This simple implementation uses only the x-axis and sorts the whole array
 * from scratch on each check.
 *
 * @author pitmairen
 */
public class SweepAndPruneDetection extends BaseDetection {

    private int collisionChecks = 0;

    public SweepAndPruneDetection(GameEngine engine, CollisionListener listener) {
        super(engine, listener);
    }

    @Override
    public void detectCollisions() {
        collisionChecks = 0;
        List<Actor> actors = new ArrayList<>(gameEngine.getCurrentLevel().getActors());

        actors.sort((Actor a, Actor b)
                -> Double.compare(a.getPosition().getX() - a.getHitBoxRadius(),
                        b.getPosition().getX() - b.getHitBoxRadius()));

        List<Actor> activeList = new ArrayList<>();

        int size = actors.size();
        for (int i = 0; i < size; i++) {
            Actor current = actors.get(i);
            checkWallCollision(current);
            Iterator<Actor> it = activeList.iterator();
            while (it.hasNext()) {

                Actor active = it.next();

                if (active.getPosition().getX() + active.getHitBoxRadius() < current.getPosition().getX() - current.getHitBoxRadius()) {
                    it.remove();
                } else {
                    collisionChecks++;
                    checkCollision(active, current);
                }
            }
            activeList.add(current);
        }
    }

    @Override
    public int getCollisionCheckCount() {
        return collisionChecks;
    }

}
