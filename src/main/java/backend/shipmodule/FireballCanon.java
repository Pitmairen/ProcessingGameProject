package backend.shipmodule;

import backend.actor.Actor;
import backend.actor.Player;
import backend.actor.Fireball;
import backend.main.FadingCanvas;
import backend.main.Timer;
import java.util.ArrayList;
import java.util.Iterator;
import processing.core.PGraphics;
import processing.core.PImage;

/**
 * The fire ball canon manages all the active fire balls.
 *
 * It uses a offscreen PGraphics objects for the drawing to be able to create a
 * fading effect for the balls and the particles when the ball explodes.
 *
 * It uses the particle emitter object to draw the particles when a ball is
 * exploding.
 *
 * @author pitmairen
 */
public class FireballCanon extends ShipModule implements FadingCanvas.Drawable {

    private final ArrayList<Fireball> balls;
    private final PImage ballImage;

    private double timeBetweenShots = 400;
    private int damage = 10;
    private Timer timer = new Timer();
    private Actor actor;

    public FireballCanon(Player actor) {
        this.actor = actor;
        this.ballImage = actor.getGuiHandler().loadImage("particle.png");
        this.balls = new ArrayList<>();
    }

    /**
     * Fires a new fireball
     *
     * @param posX the start x position
     * @param posY the start y position
     * @param targetAngle the direction
     */
    public void activate(double posX, double posY, double targetAngle, Actor owner) {

        Fireball ball = new Fireball(posX, posY, targetAngle, owner);

        actor.getGameEngine().getCurrentLevel().getProjectiles().add(ball);
        actor.getGameEngine().getCurrentLevel().getActors().add(ball);
        this.balls.add(ball);
    }
    
    @Override
    public void draw() {
        // The balls are drawn to the fading canvas.
    }

    
    @Override
    public void draw(PGraphics canvas) {

        // ADD causes the colors of objects that are drawn on top of eachother
        // to be added together which creates a nice visual effect.
        canvas.blendMode(PGraphics.ADD);
        canvas.noStroke();

        // Draw the balls
        Iterator<Fireball> it = this.balls.iterator();
        while (it.hasNext()) {

            Fireball ball = it.next();

            ball.draw(canvas);

            if (ball.isHasExploded() || ball.getHitPoints() <= 0) {
                it.remove(); // Remove the balls after it has exploded
            }
        }

        canvas.blendMode(PGraphics.BLEND); // Reset blendMode

    }

    // Getters.
    public ArrayList<Fireball> getBalls() {
        return balls;
    }


    public PImage getBallImage() {
        return ballImage;
    }

}
