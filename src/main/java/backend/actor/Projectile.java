package backend.actor;

import backend.main.Vector;
import backend.shipmodule.ShipModule;
import userinterface.Drawable;

/**
 * Super class for all projectiles.
 *
 * @author Kristian Honningsvag.
 */
public abstract class Projectile extends Actor implements Drawable {

    protected ShipModule shipModule;  // From constructor.

    /**
     * Constructor.
     */
    public Projectile(Vector position, ShipModule shipModule) {

        super(position, shipModule.getOwner().getGameEngine());

        this.shipModule = shipModule;
    }

    @Override
    public abstract void draw();

    /**
     * Call this function when the projectile have hit something.
     */
    public void targetHit() {
        this.currentHitPoints = 0;
    }

    // Getters.
    public ShipModule getShipModule() {
        return shipModule;
    }

}
