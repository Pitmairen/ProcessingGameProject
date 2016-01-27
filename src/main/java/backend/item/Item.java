package backend.item;

import backend.actor.Actor;
import backend.main.GameEngine;
import backend.shipmodule.ShipModule;
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

        hitPoints = 1;
    }

    @Override
    public void draw() {
    }

    /**
     * Creates the ship module, hands it to the actor that is picking it up, and
     * finally destroys the container.
     *
     * @param looter The actor that is picking up the object.
     * @return The ship module it is carrying.
     */
    public abstract ShipModule take(Actor looter);

}
