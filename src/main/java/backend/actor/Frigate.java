package backend.actor;

import backend.main.GameEngine;
import backend.main.NumberCruncher;
import userinterface.Drawable;

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
    public Frigate(double positionX, double positionY, GameEngine gameEngine) {

        super(positionX, positionY, gameEngine);

        speedLimit = 0.4f;
        accelerationX = 0.0015f;
        accelerationY = 0.0015f;
        drag = 0.001f;
        hitBoxRadius = 15;
        bounceModifier = 0.6f;
        hitPoints = 10;
        mass = 30;
        collisionDamageToOthers = 2;
    }

    @Override
    public void act(double timePassed) {
        approachTarget(timePassed);
        super.act(timePassed);
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
     * Accelerate towards the target.
     */
    private void approachTarget(double timePassed) {

        double xVector = gameEngine.getCurrentLevel().getPlayer().getPositionX() - this.positionX;
        double yVector = gameEngine.getCurrentLevel().getPlayer().getPositionY() - this.positionY;
        double targetAngle = NumberCruncher.calculateAngle(xVector, yVector);

        if (speedT < speedLimit) {
            speedX = speedX + (accelerationX * Math.cos(targetAngle) * timePassed);
            speedY = speedY + (accelerationY * Math.sin(targetAngle) * timePassed);
        }
    }

}
