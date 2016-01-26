package backend.shipmodule;

import backend.main.Timer;
import backend.actor.Actor;
import backend.actor.Bullet;
import userinterface.Drawable;

/**
 * Cannon that fires single slow moving bullets.
 *
 * @author Kristian Honningsvag.
 */
public class LightCannon extends ShipModule implements Drawable {

    private double timeBetweenShots = 100;
    private Timer timer = new Timer();

    /**
     * Constructor.
     */
    public LightCannon(Actor owner) {
        super("Auto Cannon", owner);

        launchVelocity = 0.8;
        projectileDamage = 3;
    }

    @Override
    public void draw() {
    }

    /**
     * Fires a bullet from the actor towards the current heading.
     */
    @Override
    public void activate() {

        if (timer.timePassed() >= timeBetweenShots) {   // Check fire rate.

            Actor bullet = new Bullet(owner.getPositionX(), owner.getPositionY(), this);

            owner.getGameEngine().getCurrentLevel().getProjectiles().add(bullet);
            owner.getGameEngine().getCurrentLevel().getActors().add(bullet);

            timer.restart();
        }
    }

}
