package Backend;

/**
 * A missile.
 *
 * @author Kristian Honningsvag.
 */
public class Missile extends Actor {

    /**
     * Constructor.
     */
    public Missile(float positionX, float positionY, float speedX, float speedY) {
        super(positionX, positionY, speedX, speedY);

        this.speedLimit = 20;
        this.acceleration = 1.08f;
        this.friction = 0.06f;
        this.bounceDampening = 0.4f;
    }

    /**
     * Call this function for each turn in the simulation.
     */
    public void act() {
        updatePosition();
        calculateDirection();
        accelerate();
    }

    /**
     * Updates the missiles position.
     */
    private void updatePosition() {
        positionX = positionX + speedX;
        positionY = positionY + speedY;
    }

    /**
     * Accelerates the missile.
     */
    private void accelerate() {
        if (speedT < speedLimit) {

            this.speedX = this.speedX * this.acceleration;
            this.speedY = this.speedY * this.acceleration;
        }
    }
}
