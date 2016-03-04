package backend.shipmodule;

import backend.actor.Actor;
import backend.actor.Rocket;
import backend.main.RocketManager;
import backend.main.Timer;
import backend.resources.Image;
import processing.core.PApplet;
import processing.core.PImage;

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

    private double timeBetweenShots = 900;
    private Timer timer = new Timer();

    private RocketManager rocketManager;

    /**
     * Constructor.
     */
    public RocketLauncher(Actor owner, RocketManager rocketManager) {

        super("Rocket Launcher", owner);

        this.rocketManager = rocketManager;
        
        launchVelocity = 0.6;
        projectileDamage = 25;
        
        moduleImage = getImageFromResourceManager(Image.ROCKET_LAUNCHER);
    }

    @Override
    public void draw() {
        drawModule(moduleImage, 0, 0, defaultModuleWidth, defaultModuleHeight);
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
