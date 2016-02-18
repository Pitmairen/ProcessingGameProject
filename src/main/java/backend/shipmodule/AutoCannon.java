package backend.shipmodule;

import backend.main.Timer;
import backend.actor.Actor;
import backend.actor.Bullet;
import backend.resources.Sound;

/**
 * Automatic cannon that fires bullets. Rapid fire and small damage.
 *
 * @author Kristian Honningsvag.
 */
public class AutoCannon extends OffensiveModule {

    // Shape and color.
    private int turretLength = 27;
    private int turretWidth = 5;
    private int[] turretRGBA = new int[]{30, 30, 200, 255};

    private double timeBetweenShots = 130;
    private Timer timer = new Timer();

    /**
     * Constructor.
     */
    public AutoCannon(Actor owner) {
        super("Auto Cannon", owner);

        launchVelocity = 1.6;
        projectileDamage = 1.6;
    }

    @Override
    public void draw() {
        owner.getGuiHandler().strokeWeight(turretWidth);
        owner.getGuiHandler().stroke(turretRGBA[0], turretRGBA[1], turretRGBA[2]);
        owner.getGuiHandler().fill(turretRGBA[0], turretRGBA[1], turretRGBA[2]);
        owner.getGuiHandler().line((float) owner.getPosition().getX(), (float) owner.getPosition().getY(),
                (float) owner.getPosition().getX() + (float) (turretLength * Math.cos(owner.getHeading().getAngle2D())),
                (float) owner.getPosition().getY() + (float) (turretLength * Math.sin(owner.getHeading().getAngle2D())));
    }

    /**
     * Fires a bullet from the actor towards the current heading.
     */
    @Override
    public void activate() {

        if (timer.timePassed() >= timeBetweenShots) {   // Check fire rate.

//            owner.getGameEngine().getSoundManager().play(Sound.AUTO_CANNON, owner.getPosition());
            Bullet bullet = new Bullet(owner.getPosition().copy(), this);

            owner.getGameEngine().getCurrentLevel().getProjectiles().add(bullet);
            owner.getGameEngine().getCurrentLevel().getActors().add(bullet);

            timer.restart();
        }
    }

}
