package backend;

import userinterface.Drawable;
import userinterface.GUIHandler;

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

    // Environment variable.
    private volatile boolean laserActive = false;

    private int score = 0;

    /**
     * Constructor.
     */
    public Player(double positionX, double positionY, GameEngine gameEngine, GUIHandler guiHandler) {

        super(positionX, positionY, gameEngine, guiHandler);

        speedLimit = 0.5f;
        accelerationX = 0.001f;
        accelerationY = 0.001f;
        hitBoxRadius = 15;
        drag = 0.0005f;
        bounceModifier = 1.2f;
        hitPoints = 30;
    }

    @Override
    public void draw() {

        // Draw main body.
        guiHandler.strokeWeight(0);
        guiHandler.stroke(bodyRGBA[0], bodyRGBA[1], bodyRGBA[2]);
        guiHandler.fill(bodyRGBA[0], bodyRGBA[1], bodyRGBA[2]);
        guiHandler.ellipse((float) this.getPositionX(), (float) this.getPositionY(), (float) hitBoxRadius * 2, (float) hitBoxRadius * 2);

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

        // If the laser is being fired.
        if (laserActive) {
            xVector = 100 * (guiHandler.mouseX - positionX);
            yVector = 100 * (guiHandler.mouseY - positionY);

            guiHandler.strokeWeight(2);
            guiHandler.stroke(255, 0, 0);
            guiHandler.line((float) positionX, (float) positionY,
                    guiHandler.mouseX + (float) xVector, guiHandler.mouseY + (float) yVector);
        }
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

    /**
     * Fires the laser from the player to the mouse cursor.
     *
     * @param laserActive State of the laser.
     */
    public void fireLaser(boolean laserActive) {
        this.laserActive = laserActive;
//        double xVector = 100 * (guiHandler.mouseX - positionX);
//        double yVector = 100 * (guiHandler.mouseY - positionY);
    }

    /**
     * Fires a missile from the player to the mouse cursor.
     */
    public void fireBullet() {

        double xVector = guiHandler.mouseX - positionX;
        double yVector = guiHandler.mouseY - positionY;
        double targetAngle = NumberCruncher.calculateAngle(xVector, yVector);

        Actor bullet = new Bullet(positionX, positionY, gameEngine, guiHandler, targetAngle);

        gameEngine.getProjectiles().add(bullet);
        gameEngine.getAllEntities().add(bullet);
    }

    /**
     * Increases the players score by an given amount.
     *
     * @param points How much to increase the score by.
     */
    public void increaseScore(int points) {
        this.score = score + points;
    }

    // Getters.
    public int getScore() {
        return score;
    }

    // Setters.
    public void setLaserActive(boolean laserActive) {
        this.laserActive = laserActive;
    }

}
