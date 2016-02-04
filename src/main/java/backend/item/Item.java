package backend.item;

import backend.actor.Actor;
import backend.actor.Player;
import backend.main.GameEngine;
import java.util.ArrayList;
import userinterface.Drawable;

/**
 * Superclass for all items in the game.
 *
 * @author Kristian Honningsvag.
 */
public abstract class Item extends Actor implements Drawable {

    /**
     * Constructor.
     */
    public Item(double positionX, double positionY, GameEngine gameEngine) {

        super(positionX, positionY, gameEngine);

        currentHitPoints = 1;
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
     * @return The content this container is carrying.
     */
    public abstract Object pickup(Actor looter);

}
