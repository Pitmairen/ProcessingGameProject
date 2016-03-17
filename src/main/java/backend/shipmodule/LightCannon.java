package backend.shipmodule;

import backend.main.Timer;
import backend.actor.Actor;
import backend.actor.projectile.Bullet;
import backend.resources.Image;
import backend.resources.Sound;

/**
 * Cannon that fires single slow moving bullets.
 *
 * @author Kristian Honningsvag.
 */
public class LightCannon extends OffensiveModule {

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

        launchVelocity = 0.7;
        projectileDamage = 10;

        moduleImage = getImageFromResourceManager(Image.LIGHT_CANNON);
    }

    //@Override
    public void draw() {
        drawModule(moduleImage, 0, 0, defaultModuleWidth, defaultModuleHeight);
    }

    /**
     * Fires a bullet from the actor towards the current heading.
     */
    @Override
    public void activate() {

        if (timer.timePassed() >= timeBetweenShots) {   // Check fire rate.
            owner.getGameEngine().getSoundManager().play(Sound.AUTO_CANNON, owner.getPosition());
            Bullet bullet = new Bullet(owner.getPosition().copy(), this);

            owner.getGameEngine().getCurrentLevel().getProjectiles().add(bullet);
            owner.getGameEngine().getCurrentLevel().getActors().add(bullet);

            timer.reset();
        }
    }

}
