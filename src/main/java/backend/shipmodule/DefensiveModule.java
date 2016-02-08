package backend.shipmodule;

import backend.actor.Actor;

/**
 * Super class for all defensive ship modules.
 *
 * @author Kristian Honningsvag.
 */
public abstract class DefensiveModule extends ShipModule {

    public DefensiveModule(String name, Actor owner) {
        super(name, owner);
    }

    @Override
    public void activate() {
    }

    @Override
    public void draw() {
    }

}
