package backend.shipmodule;

import backend.actor.Actor;
import backend.actor.Rocket;
import backend.main.RocketManager;
import backend.main.Timer;

/**
 * Fires rockets. Slow rate of fire and large damage.
 *
 * The rocket launcher manages all the active fire balls. It uses a offscreen
 * PGraphics objects for the drawing to be able to create a fading effect for
 * the rockets and the particles when the rocket explodes.
 *
 * It uses the particle emitter object to draw the particles when a rocket is
 * exploding.
 *
 * @author pitmairen
 */
public class RocketLauncher extends OffensiveModule {

    // Shape and color.
    private int turretLength = 22;
    private int turretWidth = 7;
    private int[] turretRGBA = new int[]{100, 100, 100, 255};

    private double timeBetweenShots = 600;
    private Timer timer = new Timer();
    
    private RocketManager rocketManager;

    /**
     * Constructor.
     */
    public RocketLauncher(Actor owner, RocketManager rocketManager) {

        super("Rocket Launcher", owner);

        this.rocketManager = rocketManager;

        launchVelocity = 0.8;
        projectileDamage = 12;
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
     * Fires a new rocket.
     */
    @Override
    public void activate() {

        // Wait for timer for each shot.
        if (timer.timePassed() >= timeBetweenShots) {
            Rocket rocket = new Rocket(owner.getPosition().copy(), this);

            owner.getGameEngine().getCurrentLevel().getProjectiles().add(rocket);
            owner.getGameEngine().getCurrentLevel().getActors().add(rocket);
            rocketManager.addRocket(rocket);

            timer.restart();
        }
    }

}
