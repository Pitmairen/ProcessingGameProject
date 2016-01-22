package backend.shipmodule;

import backend.main.Timer;
import backend.actor.Actor;
import backend.actor.Bullet;
import userinterface.Drawable;

/**
 * Automatic cannon that fires bullets. Medium fire rate and medium damage.
 *
 * @author Kristian Honningsvag.
 */
public class AutoCannon extends ShipModule implements Drawable {

    private double timeBetweenShots = 300;
    private int damage = 5;
    private Timer timer = new Timer();
    private Actor actor;

    /**
     * Constructor.
     *
     * @param actor The actor who owns this weapon.
     */
    public AutoCannon(Actor actor) {
        this.actor = actor;
    }

    @Override
    public void draw() {
    }

    /**
     * Fires a bullet from the actor towards the current heading.
     */
    public void activate() {

        if (timer.timePassed() >= timeBetweenShots) {   // Check fire rate.

            double targetAngle = actor.getHeading();

            Actor bullet = new Bullet(actor.getPositionX(), actor.getPositionY(), targetAngle, actor);

            actor.getGameEngine().getCurrentLevel().getProjectiles().add(bullet);
            actor.getGameEngine().getCurrentLevel().getActors().add(bullet);

            timer.restart();
        }
    }

}
