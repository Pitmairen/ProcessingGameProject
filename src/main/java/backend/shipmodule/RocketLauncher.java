package backend.shipmodule;

import backend.actor.Actor;
import backend.actor.Rocket;
import backend.main.FadingCanvas;
import backend.main.Timer;
import java.util.ArrayList;
import java.util.Iterator;
import processing.core.PGraphics;
import processing.core.PImage;
import userinterface.Drawable;

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
public class RocketLauncher extends ShipModule implements Drawable, FadingCanvas.Drawable {

    private final ArrayList<Rocket> rockets;
    private final PImage rocketImage;

    private double timeBetweenShots = 600;
    private Timer timer = new Timer();

    /**
     * Constructor.
     */
    public RocketLauncher(Actor owner) {

        super("Rocket Launcher", owner);

        this.rocketImage = owner.getGuiHandler().loadImage("particle.png");
        this.rockets = new ArrayList<>();

        launchVelocity = 0.8;
        projectileDamage = 9;
    }

    @Override
    public void draw() {
        // The balls are drawn to the fading canvas.
    }

    public void draw(PGraphics canvas) {

        // Add causes the colors of objects that are drawn on top of eachother
        // to be added together which creates a nice visual effect.
        // Draw the rockets.
        Iterator<Rocket> it = this.rockets.iterator();
        while (it.hasNext()) {

            Rocket rocket = it.next();

            rocket.draw(canvas);

            if (rocket.isHasExploded() || rocket.getHitPoints() <= 0) {
                it.remove(); // Remove the rockets after they have exploded.
            }
        }

        canvas.blendMode(PGraphics.BLEND); // Reset blendMode
    }

    /**
     * Fires a new rocket.
     */
    @Override
    public void activate() {

        // Wait for timer for each shot.
        if (timer.timePassed() >= timeBetweenShots) {
            Rocket rocket = new Rocket(owner.getPositionX(), owner.getPositionY(), this);

            owner.getGameEngine().getCurrentLevel().getProjectiles().add(rocket);
            owner.getGameEngine().getCurrentLevel().getActors().add(rocket);
            this.rockets.add(rocket);
            timer.restart();
        }
    }

    // Getters.
    public ArrayList<Rocket> getRockets() {
        return rockets;
    }

    public PImage getRocketImage() {
        return rocketImage;
    }

}
