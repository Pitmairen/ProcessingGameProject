package backend.actor;

import java.util.ArrayList;
import userinterface.Drawable;

/**
 * A simple bullet.
 *
 * @author Kristian Honningsvag.
 */
public class Bullet extends Projectile implements Drawable {

    // Color.
    private int[] bulletRGBA = new int[]{80, 200, 0, 255};

    /**
     * Constructor.
     */
    public Bullet(double positionX, double positionY, double targetAngle, Actor owner) {

        super(positionX, positionY, owner);

        speedLimit = 0.6f;
        accelerationX = 0;
        accelerationY = 0;
        drag = 0;
        hitBoxRadius = 5;
        bounceModifier = 0.6f;
        hitPoints = 1;
        mass = 3;
        collisionDamageToOthers = 3;

        setLaunchVelocity(targetAngle);
    }

    @Override
    public void act(double timePassed) {
        super.addFriction(timePassed);
        super.updatePosition(timePassed);
        checkWallCollisions(timePassed);
        checkActorCollisions(timePassed);
    }

    @Override
    protected void checkWallCollisions(double timePassed) {

        String wallCollision = gameEngine.getCollisionDetector().detectWallCollision(this);

        if (wallCollision != null) {
            setHitPoints(0);
        }
    }

    @Override
    protected void checkActorCollisions(double timePassed) {

        ArrayList<Actor> collisions = collisionDetector.detectActorCollision(this);

        if (collisions.size() > 0) {

            for (Actor actorInList : collisions) {
                if (actorInList != owner) {

                    elasticColision(this, actorInList, timePassed);
                    this.modifyHitPoints(-actorInList.getCollisionDamageToOthers());
                    actorInList.modifyHitPoints(-collisionDamageToOthers);

                    if ((actorInList instanceof Frigate)) {
                        owner.increaseScore(1);
                    }
                }
            }
        }
    }

    /**
     * Sets the bullets speed vectors.
     *
     * @param targetAngle
     */
    private void setLaunchVelocity(double targetAngle) {
        speedX = launchVelocity * Math.cos(targetAngle);
        speedY = launchVelocity * Math.sin(targetAngle);
    }

    @Override
    public void draw() {
        guiHandler.strokeWeight(0);
        guiHandler.stroke(bulletRGBA[0], bulletRGBA[1], bulletRGBA[2]);
        guiHandler.fill(bulletRGBA[0], bulletRGBA[1], bulletRGBA[2]);
        guiHandler.ellipse((float) this.getPositionX(), (float) this.getPositionY(), (float) hitBoxRadius * 2, (float) hitBoxRadius * 2);
    }

}
