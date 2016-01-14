package backend;

import userinterface.Drawable;
import userinterface.GUIHandler;

/**
 * A simple bullet.
 *
 * @author Kristian Honningsvag.
 */
public class Bullet extends Actor implements Drawable {

    // Color.
    private int[] missileRGBA = new int[]{80, 200, 0, 255};

    double speed = 1.2f;

    /**
     * Constructor.
     */
    public Bullet(double positionX, double positionY, GameEngine gameEngine, GUIHandler guiHandler, double targetAngle) {

        super(positionX, positionY, gameEngine, guiHandler);

        accelerationX = 0;
        accelerationY = 0;
        hitBoxRadius = 5;
        drag = 0;
        hitPoints = 1;

        setSpeed(targetAngle);
    }

    /**
     * Sets the bullets speed vectors.
     *
     * @param targetAngle
     */
    private void setSpeed(double targetAngle) {
        speedX = speed * Math.cos(targetAngle);
        speedY = speed * Math.sin(targetAngle);
    }

    @Override
    public void draw() {
        guiHandler.strokeWeight(0);
        guiHandler.stroke(missileRGBA[0], missileRGBA[1], missileRGBA[2]);
        guiHandler.fill(missileRGBA[0], missileRGBA[1], missileRGBA[2]);
        guiHandler.ellipse((float) this.getPositionX(), (float) this.getPositionY(), (float) hitBoxRadius * 2, (float) hitBoxRadius * 2);
    }

}
