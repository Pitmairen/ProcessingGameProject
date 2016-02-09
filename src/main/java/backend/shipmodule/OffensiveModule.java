package backend.shipmodule;

import backend.actor.Actor;

/**
 * Super class for all offensive ship modules.
 *
 * @author Kristian Honningsvag.
 */
public abstract class OffensiveModule extends ShipModule {

    public OffensiveModule(String name, Actor owner) {
        super(name, owner);
    }

    @Override
    public void activate() {
    }

    @Override
    public void draw() {
    }

}
