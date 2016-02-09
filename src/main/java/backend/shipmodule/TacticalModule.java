package backend.shipmodule;

import backend.actor.Actor;

/**
 * Super class for all tactical ship modules. These are very powerful modules
 * with limited use.
 *
 * @author Kristian Honningsvag.
 */
public abstract class TacticalModule extends ShipModule {

    public TacticalModule(String name, Actor owner) {
        super(name, owner);
    }

    @Override
    public void activate() {
    }

    @Override
    public void draw() {
    }

}
