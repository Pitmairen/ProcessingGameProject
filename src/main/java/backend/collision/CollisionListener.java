package backend.collision;

import backend.actor.Actor;

/**
 * Listener that will be called when a collision occur.
 *
 * @author pitmairen
 */
public interface CollisionListener {

    /**
     * A collision between has been detected between actor a and b
     *
     * @param a colliding actor a
     * @param b colliding actor b
     */
    public void collisionDetected(Actor a, Actor b);

    /**
     * An actor has collided with the wall.
     *
     * @param actor the colliding actor
     * @param wall the wall that the actor collided with
     */
    public void wallCollisionDetected(Actor actor, Wall wall);
}
