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

    // Shape.
    private int turretLength = 10;
    private int turretWidth = 3;
    private int[] turretRGBA = new int[]{70, 100, 100, 255};

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
        // Draw turret.
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
