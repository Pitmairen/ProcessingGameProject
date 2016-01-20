package backend.actor;

import backend.GameEngine;
import java.util.ArrayList;
import userinterface.Drawable;

/**
 * A simple bullet.
 *
 * @author Kristian Honningsvag.
 */
public class Bullet extends Actor implements Drawable {

    // Color.
    private int[] bulletRGBA = new int[]{80, 200, 0, 255};
    
    double launchVelocity = 1.2f;

    /**
     * Constructor.
     */
    public Bullet(double positionX, double positionY, GameEngine gameEngine, double targetAngle) {
        
        super(positionX, positionY, gameEngine);
        
        speedLimit = 0.6f;
        accelerationX = 0;
        accelerationY = 0;
        drag = 0;
        hitBoxRadius = 5;
        bounceModifier = 0.6f;
        hitPoints = 1;
        
        setLaunchVelocity(targetAngle);
    }

    /**
     * Check for wall collisions and react to them.
     */
    protected void checkWallCollisions(double timePassed) {
        
        String wallCollision = gameEngine.getCollisionDetector().detectWallCollision(this);
        
        if (wallCollision != null) {
            setHitPoints(0);
        }
    }
    
    @Override
    protected void checkActorCollisions() {
        
        ArrayList<Actor> collisions = collisionDetector.detectActorCollision(this);
        
        if (collisions.size() > 0) {
            for (Actor actorInList : collisions) {
                if (!(actorInList instanceof Player)) {
                    setHitPoints(0);
                }
                if ((actorInList instanceof Frigate)) {
                    gameEngine.getCurrentLevel().getPlayer().increaseScore(1);
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
