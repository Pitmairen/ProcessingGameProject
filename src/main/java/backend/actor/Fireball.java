package backend.actor;

import backend.main.FadingCanvas;
import java.util.ArrayList;
import processing.core.PConstants;
import processing.core.PGraphics;

/**
 * The fireballs us implemented as a internal class because they need to be
 * drawn on a offscreen graphics object to create the trail/fading effect.
 *
 * It does not implement the normal draw method, but instead it uses a special
 * draw method that is called from the FireballCanon class. which passes in the
 * graphics object that the balls are drawn onto.
 *
 */
public class Fireball extends Projectile {

    private final int backgroundColor;
    private final float radius = 8.0f;
    private final float speed = 0.8f;
    private boolean hasExploded = false;
    Player playerOwner;

    public Fireball(double positionX, double positionY, double targetAngle, Actor owner) {

        super(positionX, positionY, owner);

        this.playerOwner = (Player) owner;

        accelerationX = 0;
        accelerationY = 0;
        hitBoxRadius = 8;
        drag = 0;
        hitPoints = 1;
        mass = 3;

        setSpeed(targetAngle);

        backgroundColor = guiHandler.color(guiHandler.random(10, 255),
                guiHandler.random(10, 255), guiHandler.random(10, 255), 255);
    }

    @Override
    public void draw() {
        // Do nothing. The fireballs are managed by the fireball cannon
        // and are drawn to the fading canvas. 
    }

    @Override
    protected void checkWallCollisions(double timePassed) {
        if (hasExploded) {
            return;
        }

        String wallCollision = gameEngine.getCollisionDetector().detectWallCollision(this);

        if (wallCollision != null) {
            setHitPoints(0);
            explode();
        }
    }

    @Override
    protected void checkActorCollisions(double timePassed) {

        if (hasExploded) {
            return;
        }

        ArrayList<Actor> collisions = collisionDetector.detectActorCollision(this);

        if (collisions.size() > 0) {

            for (Actor actorInList : collisions) {

                if (actorInList != owner) {

                    elasticColision(this, actorInList, timePassed);
                    this.modifyHitPoints(-actorInList.getCollisionDamageToOthers());
                    actorInList.modifyHitPoints(-collisionDamageToOthers);
                    explode();

                    if ((actorInList instanceof Frigate)) {
                        owner.increaseScore(1);
                    }
                }
            }
        }
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

        canvas.image(playerOwner.getFireballCannon().getBallImage(),
                (float) this.positionX, (float) this.positionY,
                (float) this.radius * 2, (float) this.radius * 2);
        canvas.ellipse((float) this.positionX, (float) this.positionY,
                (float) this.radius / 2, (float) this.radius / 2);
    }

    private void setSpeed(double targetAngle) {
        speedX = speed * Math.cos(targetAngle);
        speedY = speed * Math.sin(targetAngle);
    }

    public void explode() {
        hasExploded = true;
    }

    // Getters.
    public boolean isHasExploded() {
        return hasExploded;
    }
    
    public int getBackgroundColor() {
        return backgroundColor;
    }
    

}
