package backend.actor;

import backend.shipmodule.ShipModule;
import processing.core.PGraphics;
import processing.core.PVector;
import userinterface.Drawable;
import backend.main.FadingCanvas;
import processing.core.PConstants;

/**
 * The rockets need to be drawn on a offscreen graphics object to create the
 * trail/fading effect.
 *
 * It does not implement the normal draw method, but instead it uses a special
 * draw method that is called from the RocketLauncher class. which passes in the
 * graphics object that the rockets are drawn onto.
 *
 */
public class Rocket extends Projectile implements Drawable {

    private final int backgroundColor;
    private final float radius = 8.0f;
    private boolean hasExploded = false;

    Player playerOwner;

    /**
     * Constructor.
     */
    public Rocket(double positionX, double positionY, ShipModule shipModule) {

        super(positionX, positionY, shipModule);

        this.playerOwner = (Player) shipModule.getOwner();

        backgroundColor = guiHandler.color(guiHandler.random(10, 255),
                guiHandler.random(10, 255), guiHandler.random(10, 255), 255);

        hitBoxRadius = 8;
        hitPoints = 1;
        mass = 3;
        collisionDamageToOthers = shipModule.getProjectileDamage();

        setLaunchVelocity(shipModule.getOwner().getHeading());
    }

    @Override
    public void act(double timePassed) {
        super.addFriction(timePassed);
        super.updatePosition(timePassed);
        checkWallCollisions(timePassed);
        checkActorCollisions(timePassed);
    }

    @Override
    public void draw() {
        // Do nothing. The rockets are managed by the rocket launcher
        // and are drawn to the fading canvas. 
    }

    @Override
    public void targetHit() {
        setHitPoints(0);
        hasExploded = true;

    }

    /**
     * Draws the ball
     *
     * @param canvas the graphics object to draw to
     */
    public void draw(PGraphics canvas) {
        if (hasExploded) {
            return;
        }

        canvas.imageMode(PConstants.CENTER);
        canvas.ellipseMode(PConstants.CENTER);
        canvas.tint(this.backgroundColor, 255);
        canvas.fill(this.backgroundColor, 200);

        canvas.image(playerOwner.getRocketLauncher().getRocketImage(),
                (float) this.positionX, (float) this.positionY,
                (float) this.radius * 2, (float) this.radius * 2);
        canvas.ellipse((float) this.positionX, (float) this.positionY,
                (float) this.radius / 2, (float) this.radius / 2);
    }

    public void explode() {
        hasExploded = true;
    }

    /**
     * Sets the projectiles speed vectors.
     *
     * @param heading The direction the actor is aiming.
     */
    private void setLaunchVelocity(double heading) {
        speedX = shipModule.getLaunchVelocity() * Math.cos(heading);
        speedY = shipModule.getLaunchVelocity() * Math.sin(heading);
    }

    // Getters.
    public boolean isHasExploded() {
        return hasExploded;
    }

    public int getBackgroundColor() {
        return backgroundColor;
    }
}
