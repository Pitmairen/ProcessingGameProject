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

    // Shape and color.
    private int turretLength = 27;
    private int turretWidth = 4;
    private int[] turretRGBA = new int[]{30, 30, 200, 255};

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
        owner.getGuiHandler().strokeWeight(turretWidth);
        owner.getGuiHandler().stroke(turretRGBA[0], turretRGBA[1], turretRGBA[2]);
        owner.getGuiHandler().fill(turretRGBA[0], turretRGBA[1], turretRGBA[2]);
        owner.getGuiHandler().line((float) owner.getPositionX(), (float) owner.getPositionY(),
                (float) owner.getPositionX() + (float) (turretLength * Math.cos(owner.getHeading())),
                (float) owner.getPositionY() + (float) (turretLength * Math.sin(owner.getHeading())));
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
