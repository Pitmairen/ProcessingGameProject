package Backend;

import UserInterface.GUIHandler;

/**
 * The player.
 *
 * @author Kristian Honningsvag.
 */
public class Player extends Actor implements Drawable {

    // Shape.
    private int turretLength = 20;
    private int turretWidth = 3;

    // Color.
    private int[] bodyRGBA = new int[]{0, 70, 200, 255};
    private int[] turretRGBA = new int[]{30, 30, 200, 255};

    /**
     * Constructor.
     */
    public Player(double positionX, double positionY, GameEngine gameEngine) {

        super(positionX, positionY, gameEngine);

        speedLimit = 0.5f;
        accelerationX = 0.001f;
        accelerationY = 0.001f;
        hitBoxRadius = 30;
        drag = 0.0005f;
        bounceModifier = 1.2f;
        hitPoints = 30;
    }

    @Override
    public void draw(GUIHandler guiHandler) {

        // Draw main body.
        guiHandler.strokeWeight(0);
        guiHandler.stroke(bodyRGBA[0], bodyRGBA[1], bodyRGBA[2]);
        guiHandler.fill(bodyRGBA[0], bodyRGBA[1], bodyRGBA[2]);
        guiHandler.ellipse((float) this.getPositionX(), (float) this.getPositionY(), (float) hitBoxRadius, (float) hitBoxRadius);

        // Draw turret.
        double xVector = guiHandler.mouseX - this.getPositionX();
        double yVector = guiHandler.mouseY - this.getPositionY();
        double targetAngle = NumberCruncher.calculateAngle(xVector, yVector);

        guiHandler.strokeWeight(turretWidth);
        guiHandler.stroke(turretRGBA[0], turretRGBA[1], turretRGBA[2]);
        guiHandler.fill(turretRGBA[0], turretRGBA[1], turretRGBA[2]);
        guiHandler.line((float) this.getPositionX(), (float) this.getPositionY(),
                (float) this.getPositionX() + (float) (turretLength * Math.cos(targetAngle)),
                (float) this.getPositionY() + (float) (turretLength * Math.sin(targetAngle)));
    }

    /**
     * Accelerates the player in the given direction.
     *
     * @param direction The direction of the acceleration.
     */
    public void accelerate(String direction) {
        if (direction.equalsIgnoreCase("up")) {
            if (speedY > (-speedLimit)) {
                speedY = speedY - accelerationY;
            }
        }
        // Accelerate downwards.
        if (direction.equalsIgnoreCase("down")) {
            if (speedY < (speedLimit)) {
                speedY = speedY + accelerationY;
            }
        }
        // Accelerate left.
        if (direction.equalsIgnoreCase("left")) {
            if (speedX > (-speedLimit)) {
                speedX = speedX - accelerationX;
            }
        }
        // Accelerate right.
        if (direction.equalsIgnoreCase("right")) {
            if (speedX < (speedLimit)) {
                speedX = speedX + accelerationX;
            }
        }
    }

}
