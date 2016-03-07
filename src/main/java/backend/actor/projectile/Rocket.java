package backend.actor.projectile;

import backend.actor.Player;
import backend.main.Vector;
import backend.resources.Image;
import backend.shipmodule.ShipModule;
import processing.core.PGraphics;
import processing.core.PConstants;
import processing.core.PImage;

/**
 * The rockets need to be drawn on a offscreen graphics object to create the
 * trail/fading effect.
 *
 * It does not implement the normal draw method, but instead it uses a special
 * draw method that is called from the RocketManager class. which passes in the
 * graphics object that the rockets are drawn onto.
 *
 */
public class Rocket extends Projectile {

    private final int backgroundColor;
    private final float radius = 8.0f;
    private boolean hasExploded = false;

    Player playerOwner;

    // Color of rocket.
    private int[] bodyRGBA = new int[]{255, 255, 255, 255};

    /**
     * Constructor.
     */
    public Rocket(Vector position, ShipModule shipModule) {

        super(position, shipModule);

        this.playerOwner = (Player) shipModule.getOwner();

        backgroundColor = guiHandler.color(230,200,30);

        name = "Rocket";
        hitBoxRadius = 8;
        currentHitPoints = 1;
        mass = 3;
        collisionDamageToOthers = shipModule.getProjectileDamage();

        setLaunchVelocity(shipModule.getOwner().getHeading().getAngle2D());
    }

    @Override
    public void draw() {
        // Draw the body of the rocket.
//        guiHandler.strokeWeight(0);
//        guiHandler.stroke(bodyRGBA[0], bodyRGBA[1], bodyRGBA[2], bodyRGBA[3]);
//        guiHandler.fill(bodyRGBA[0], bodyRGBA[1], bodyRGBA[2], bodyRGBA[3]);
//        guiHandler.ellipse((float) this.getPosition().getX(), (float) this.getPosition().getY(), (float) hitBoxRadius * 2, (float) hitBoxRadius * 2);
//        guiHandler.noFill();

        PImage image = this.getGameEngine().getResourceManager().getImage(Image.ROCKET);

        guiHandler.tint(bodyRGBA[0], bodyRGBA[1], bodyRGBA[2], bodyRGBA[3]);
        guiHandler.imageMode(PImage.CENTER);
        guiHandler.image(image,
                (float) this.getPosition().getX(), (float) this.getPosition().getY(),
                (float) this.radius * 2, (float) this.radius * 2);
       
        guiHandler.imageMode(PImage.CORNER);

        // Do nothing. The rockets are managed by the rocket manager and are
        // drawn to the fading canvas. 
    }

    @Override
    public void die() {
        gameEngine.getExplosionManager().explodeRocket(this);
        gameEngine.getCurrentLevel().getProjectiles().remove(this);
    }

    @Override
    public void targetHit() {
        setCurrentHitPoints(0);
        hasExploded = true;

    }

    /**
     * Draws the rocket
     *
     * @param canvas the graphics object to draw to
     * @param image the background image to use for the rocket
     */
    public void draw(PGraphics canvas, PImage image) {
        if (hasExploded) {
            return;
        }

        canvas.imageMode(PConstants.CENTER);
        canvas.ellipseMode(PConstants.CENTER);
        canvas.tint(this.backgroundColor, 155);
        canvas.fill(this.backgroundColor, 200);

        canvas.image(image,
                (float) this.getPosition().getX(), (float) this.getPosition().getY(),
                (float) this.radius * 2, (float) this.radius * 2);
        canvas.ellipse((float) this.getPosition().getX(), (float) this.getPosition().getY(),
                (float) this.radius / 2, (float) this.radius / 2);
    }

    /**
     *
     */
    public void explode() {
        hasExploded = true;
    }

    /**
     * Sets the projectiles speedT vectors.
     *
     * @param heading The direction the actor is aiming.
     */
    private void setLaunchVelocity(double heading) {
        this.getSpeedT().set(shipModule.getLaunchVelocity() * Math.cos(heading), shipModule.getLaunchVelocity() * Math.sin(heading), 0);
    }

    // Getters.
    public boolean isHasExploded() {
        return hasExploded;
    }

    public int getBackgroundColor() {
        return backgroundColor;
    }
}
