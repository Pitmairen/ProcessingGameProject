package backend.actor;

import backend.actor.Actor;
import backend.actor.Player;
import backend.main.GameEngine;
import backend.main.Vector;
import java.util.ArrayList;
import userinterface.Drawable;

/**
 * Superclass for all items in the game.
 *
 * @author Kristian Honningsvag.
 */
public abstract class Item extends Actor implements Drawable {

    protected double pullDistance = 0;
    private boolean approachingPlayer = false;

    /**
     * Constructor.
     */
    public Item(Vector position, GameEngine gameEngine) {

        super(position, gameEngine);

        currentHitPoints = 1;
        mass = 1;
        engineThrust = 0.015;
        frictionCoefficient = 0.006;
    }

    @Override
    public void act(double timePassed) {
        pulledTowardsPlayer();
        super.act(timePassed);
    }

    @Override
    public void draw() {
    }

    @Override
    protected void checkActorCollisions(double timePassed) {
        ArrayList<Actor> collisions = collisionDetector.detectActorCollision(this);

        if (collisions.size() > 0) {

            for (Actor target : collisions) {

                if ((target instanceof Player)) {
                    // This item crashed into a player.
                    this.pickup(target);

                } else {
                    // Items does not interact with other actors than the player.
                }
            }
        }
    }

    /**
     * This method should be called when an actor interacts with this item in
     * order to pick up its contents. The contents get handed over to the
     * interacting actor, and the container gets marked for deletion.
     *
     * @param looter The actor that is interacting with the container.
     */
    public abstract void pickup(Actor looter);

    /**
     * If the player is within reach, the item gets drawn towards the player
     * until they collide.
     */
    private void pulledTowardsPlayer() {

        if (approachingPlayer) {
            if (gameEngine.getCurrentLevel().getPlayer().getCurrentHitPoints() > 0) {
                Vector force = Vector.sub(gameEngine.getCurrentLevel().getPlayer().getPosition(), getPosition());
                force.normalize().mult(engineThrust);
                applyForce(force);
            } else {
                approachingPlayer = false;
            }
        }

        if (!approachingPlayer) {
            if (this.position.dist(gameEngine.getCurrentLevel().getPlayer().getPosition()) <= pullDistance) {
                approachingPlayer = true;
            }
        }
    }

}
