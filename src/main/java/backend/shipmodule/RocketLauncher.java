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
    private final PImage rocketImg;

    /**
     * Constructor.
     */
    public RocketLauncher(Actor owner, RocketManager rocketManager) {

        super("Rocket Launcher", owner);

        this.rocketManager = rocketManager;
        rocketImg = owner.getGameEngine().getResourceManager().getImage(Image.ROCKET_LAUNCHER);
        launchVelocity = 0.6;
        projectileDamage = 22;
    }

    @Override
    public void draw() {
        PApplet gui = owner.getGuiHandler();
        gui.pushMatrix();
        gui.translate((float) owner.getPosition().getX(), (float) owner.getPosition().getY());
        gui.rotate((float)(owner.getHeading().getAngle2D()));
        gui.imageMode(PImage.CENTER);
        gui.image(rocketImg, 0, 0, 46.29f, 23.92f);
        gui.imageMode(PImage.CORNER);
        gui.popMatrix();
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
