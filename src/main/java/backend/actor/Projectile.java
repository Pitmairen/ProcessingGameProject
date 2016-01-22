package backend.actor;

import userinterface.Drawable;

/**
 * Super class for all projectiles.
 *
 * @author Kristian Honningsvag.
 */
abstract class Projectile extends Actor implements Drawable {

    protected double launchVelocity = 1.2f;
    protected Actor owner; // From constructor.

    /**
     * Constructor.
     */
    public Projectile(double positionX, double positionY, Actor owner) {

        super(positionX, positionY, owner.getGameEngine());

        this.owner = owner;
    }

    @Override
    public abstract void draw();

    // Getters.
    public Actor getOwner() {
        return owner;
    }

}
