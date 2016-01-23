package backend.shipmodule;

import backend.actor.Actor;
import backend.actor.Player;
import backend.main.ParticleEmitter;
import backend.actor.Fireball;
import backend.main.Timer;
import java.util.ArrayList;
import java.util.Iterator;
import processing.core.PGraphics;
import processing.core.PImage;
import userinterface.Drawable;

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
public class FireballCanon extends ShipModule implements Drawable {

    private final ArrayList<Fireball> balls;
    private final PGraphics canvas;
    private final PImage ballImage;
    private final ParticleEmitter particles; // Used for explosions

    private double timeBetweenShots = 400;
    private int damage = 10;
    private Timer timer = new Timer();
    private Actor actor;

    public FireballCanon(Player actor) {
        this.actor = actor;

        this.canvas = actor.getGuiHandler().createGraphics(actor.getGuiHandler().getWidth(),
                actor.getGuiHandler().getHeight(), PGraphics.P2D);

        this.ballImage = actor.getGuiHandler().loadImage("particle.png");
        this.balls = new ArrayList<>();
        this.canvas.ellipseMode(PGraphics.CENTER);
        this.canvas.imageMode(PGraphics.CENTER);
        this.particles = new ParticleEmitter(actor.getGuiHandler());
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

    /**
     * Animates the particles.
     */
    public void act(double timePassed) {
        particles.update((float) timePassed / 25.0f);
    }

    @Override
    public void draw() {

        this.canvas.beginDraw();

        // Creates the fading tail effect. The second number (40) creates the 
        // tail effect, a lower number means a longer tail. This affects both
        // particles and the fireballs.
        this.canvas.fill(0, 40);
        this.canvas.rect(0, 0,
                actor.getGuiHandler().getWidth(), actor.getGuiHandler().getHeight());

        // Add causes the colors of objects that are drawn on top of eachother
        // to be added together which creates a nice visual effect.
        this.canvas.blendMode(PGraphics.ADD);
        this.canvas.noStroke();

        // Draw the particles 
        particles.draw(this.canvas);

        // Draw the balls
        Iterator<Fireball> it = this.balls.iterator();
        while (it.hasNext()) {

            Fireball ball = it.next();

            ball.draw(this.canvas);

            if (ball.isHasExploded() || ball.getHitPoints() <= 0) {
                it.remove(); // Remove the balls after it has exploded
            }
        }

        this.canvas.blendMode(PGraphics.BLEND);
        this.canvas.endDraw();
        actor.getGuiHandler().image(this.canvas, 0, 0);
    }

    // Getters.
    public ArrayList<Fireball> getBalls() {
        return balls;
    }

    public PGraphics getCanvas() {
        return canvas;
    }

    public PImage getBallImage() {
        return ballImage;
    }

    public ParticleEmitter getParticles() {
        return particles;
    }

    public Actor getActor() {
        return actor;
    }

}
