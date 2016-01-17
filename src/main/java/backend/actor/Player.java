package backend.actor;

import backend.GameEngine;
import backend.NumberCruncher;
import backend.Timer;
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
    private int fireRate = 200;
    private Timer timer;

    /**
     * Constructor.
     */
    public Player(double positionX, double positionY, GameEngine gameEngine, GUIHandler guiHandler) {

        super(positionX, positionY, gameEngine, guiHandler);

        speedLimit = 0.45f;
        accelerationX = 0.0013f;
        accelerationY = 0.0013f;
        hitBoxRadius = 15;
        drag = 0.0005f;
        bounceModifier = 1.2f;
        hitPoints = 30;

        timer = new Timer();
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
            double screenDiagonalLength = Math.sqrt(Math.pow(gameEngine.getGuiHandler().getWidth(), 2) + Math.pow(gameEngine.getGuiHandler().getHeight(), 2));
            xVector = guiHandler.mouseX - positionX;
            yVector = guiHandler.mouseY - positionY;
            targetAngle = NumberCruncher.calculateAngle(xVector, yVector);

            guiHandler.strokeWeight(2);
            guiHandler.stroke(255, 0, 0);
            guiHandler.line((float) positionX, (float) positionY,
                    (float) positionX + (float) (screenDiagonalLength * Math.cos(targetAngle)),
                    (float) positionY + (float) (screenDiagonalLength * Math.sin(targetAngle)));
        }
    }

    /**
     * Fires the laser from the player to the mouse cursor.
     *
     * @param laserActive State of the laser.
     */
    public void fireLaser(boolean laserActive) {
        this.laserActive = laserActive;
        double yVector = guiHandler.mouseY - positionY;
    }

    /**
     * Fires a bullet from the player to the mouse cursor.
     */
    public void fireBullet() {

        // Wait for timer for each shot.
        if (timer.timePassed() >= fireRate) {
            double xVector = guiHandler.mouseX - positionX;
            double yVector = guiHandler.mouseY - positionY;
            double targetAngle = NumberCruncher.calculateAngle(xVector, yVector);

            Actor bullet = new Bullet(positionX, positionY, gameEngine, guiHandler, targetAngle);

            gameEngine.getCurrentLevel().getProjectiles().add(bullet);
            gameEngine.getCurrentLevel().getActors().add(bullet);

            timer.restart();
        }
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
