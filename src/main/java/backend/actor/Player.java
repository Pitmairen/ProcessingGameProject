package backend.actor;

import backend.GameEngine;
import backend.NumberCruncher;
import backend.Timer;
import java.util.ArrayList;
import userinterface.Drawable;

/**
 * The player.
 *
 * @author Kristian Honningsvag.
 */
public class Player extends Actor implements Drawable {

    // Shape.
    private int turretLength = 23;
    private int turretWidth = 3;

    // Color.
    private int[] bodyRGBA = new int[]{0, 70, 200, 255};
    private int[] turretRGBA = new int[]{30, 30, 200, 255};

    // Environment variable.
    private boolean laserActive = false;

    private int score = 0;
    private int fireRate = 200;
    private Timer timer;

    private FireballCanon canon;
    /**
     * Constructor.
     */
    public Player(double positionX, double positionY, GameEngine gameEngine, FireballCanon canon) {

        super(positionX, positionY, gameEngine);

        this.canon = canon;

        speedLimit = 0.6f;
        accelerationX = 0.002f;
        accelerationY = 0.002f;
        drag = 0.001f;
        hitBoxRadius = 20;

        bounceModifier = 0.6f;

        hitPoints = 30;
        mass = 40;

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
            laserActive = false;
        }
    }

    @Override
    protected void checkActorCollisions() {

        ArrayList<Actor> collisions = collisionDetector.detectActorCollision(this);

        if (collisions.size() > 0) {
            for (Actor actorInList : collisions) {
                if ((actorInList instanceof Frigate)) {
                    setHitPoints(0);
                }
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
    }

    /**
     * Fires a bullet from the actor to the mouse cursor.
     */
    public void fireBullet() {

        // Wait for timer for each shot.
        if (timer.timePassed() >= fireRate) {
            double xVector = guiHandler.mouseX - positionX;
            double yVector = guiHandler.mouseY - positionY;
            double targetAngle = NumberCruncher.calculateAngle(xVector, yVector);

            Actor bullet = new Bullet(positionX, positionY, gameEngine, targetAngle);

            gameEngine.getCurrentLevel().getProjectiles().add(bullet);
            gameEngine.getCurrentLevel().getActors().add(bullet);

            timer.restart();
        }
    }

    /**
     * Fires a new fireball
     */
    public void fireFireball() {

        // Wait for timer for each shot.
        if (timer.timePassed() >= fireRate) {
            double xVector = guiHandler.mouseX - positionX;
            double yVector = guiHandler.mouseY - positionY;
            double targetAngle = NumberCruncher.calculateAngle(xVector, yVector);

            this.canon.fire(positionX, positionY, targetAngle);

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
