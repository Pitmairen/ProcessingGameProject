package backend.actor;

import backend.GameEngine;
import backend.NumberCruncher;
import userinterface.Drawable;
import userinterface.GUIHandler;

/**
 * A frigate. Small and fast.
 *
 * @author Kristian Honningsvag.
 */
public class Frigate extends Actor implements Drawable {

    // Shape.
    private int turretLength = 10;
    private int turretWidth = 3;

    // Color.
    private int[] bodyRGBA = new int[]{200, 30, 30, 255};
    private int[] turretRGBA = new int[]{70, 100, 100, 255};

    /**
     * Constructor.
     */
    public Frigate(double positionX, double positionY, GameEngine gameEngine, GUIHandler guiHandler) {

        super(positionX, positionY, gameEngine, guiHandler);

        speedLimit = 0.7f;
        accelerationX = 0.0017f;
        accelerationY = 0.0017f;
        hitBoxRadius = 10;
        drag = 0.0005f;
        bounceModifier = 1.2f;
        hitPoints = 5;
    }

    @Override
    public void act() {
        approachPlayer();
        super.act();
    }

    @Override
    public void draw() {

        // Draw main body.
        guiHandler.strokeWeight(0);
        guiHandler.stroke(bodyRGBA[0], bodyRGBA[1], bodyRGBA[2]);
        guiHandler.fill(bodyRGBA[0], bodyRGBA[1], bodyRGBA[2]);
        guiHandler.ellipse((float) this.getPositionX(), (float) this.getPositionY(), (float) hitBoxRadius * 2, (float) hitBoxRadius * 2);

        // Draw turret.
        guiHandler.strokeWeight(turretWidth);
        guiHandler.stroke(turretRGBA[0], turretRGBA[1], turretRGBA[2]);
        guiHandler.fill(turretRGBA[0], turretRGBA[1], turretRGBA[2]);
        guiHandler.line((float) this.getPositionX(), (float) this.getPositionY(),
                (float) this.getPositionX() + (float) (turretLength * Math.cos(course)),
                (float) this.getPositionY() + (float) (turretLength * Math.sin(course)));
    }

    /**
     * Accelerate towards the player.
     */
    private void approachPlayer() {

        double xVector = gameEngine.getCurrentLevel().getPlayer().getPositionX() - this.positionX;
        double yVector = gameEngine.getCurrentLevel().getPlayer().getPositionY() - this.positionY;
        double targetAngle = NumberCruncher.calculateAngle(xVector, yVector);

        if (speedT < speedLimit) {
            speedX = speedX + (accelerationX * Math.cos(targetAngle));
            speedY = speedY + (accelerationY * Math.sin(targetAngle));
        }
    }

}
