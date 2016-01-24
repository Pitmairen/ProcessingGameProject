package backend.shipmodule;

import backend.main.Timer;
import backend.actor.Actor;
import backend.actor.Bullet;
import userinterface.Drawable;

/**
 * Automatic cannon that fires bullets. Rapid fire and small damage.
 *
 * @author Kristian Honningsvag.
 */
public class AutoCannon extends ShipModule implements Drawable {

    private double timeBetweenShots = 130;
    private Timer timer = new Timer();

    /**
     * Constructor.
     */
    public AutoCannon(Actor owner) {
        super("Auto Cannon", owner);

        launchVelocity = 1.8;
        projectileDamage = 1.6;
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
