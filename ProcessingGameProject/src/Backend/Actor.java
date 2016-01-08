package Backend;

/**
 * Super class for all actors.
 *
 * @author Kristian Honningsvag.
 */
public abstract class Actor {

    // Position.
    protected float positionX;
    protected float positionY;

    // Speed.
    protected float speedX;
    protected float speedY;
    protected double speedT;

    // Direction.
    protected double direction;

    // Characteristics.
    protected double speedLimit;
    protected float acceleration;
    protected float friction;
    protected float bounceDampening;

    /**
     * Constructor.
     */
    protected Actor(float positionX, float positionY, float speedX, float speedY) {

        this.positionX = positionX;
        this.positionY = positionY;
        this.speedX = speedX;
        this.speedY = speedY;
    }

    /**
     * Calculates the direction and speed of the actors current movement.
     * NB: Processing operates with an inverse y-axis.
     */
    protected void calculateDirection() {

        double angle = Math.atan(speedY / speedX);
        this.speedT = Math.sqrt(Math.pow(speedX, 2) + Math.pow(speedY, 2));

        // If speed vector lies in processing quadrant 1
        if (speedX > 0 && speedY > 0) {
            this.direction = angle;
        }
        // If speed vector lies in processing quadrant 2
        if (speedX < 0 && speedY > 0) {
            this.direction = angle + Math.PI;
        }
        // If speed vector lies in processing quadrant 3
        if (speedX < 0 && speedY < 0) {
            this.direction = angle + Math.PI;
        }
        // If speed vector lies in processing quadrant 4
        if (speedX > 0 && speedY < 0) {
            this.direction = angle + 2 * Math.PI;
        }

        // If speed vector is straight to the right.
        if (speedX > 0 && speedY == 0) {
            this.direction = 0;
        }
        // If speed vector is straight down.
        if (speedX == 0 && speedY > 0) {
            this.direction = Math.PI / 2;
        }
        // If speed vector is straight to the left.
        if (speedX < 0 && speedY == 0) {
            this.direction = Math.PI;
        }
        // If speed vector is straight up.
        if (speedX == 0 && speedY < 0) {
            this.direction = (2 * Math.PI) - (Math.PI / 2);
        }
        // If standing still.
        if (speedX == 0 && speedY == 0) {
            this.direction = 0;
        }
    }

    // Setters.
    public void setPositionX(float playerPositionX) {
        this.positionX = playerPositionX;
    }

    public void setPositionY(float playerPositionY) {
        this.positionY = playerPositionY;
    }

    public void setSpeedX(float playerSpeedX) {
        this.speedX = playerSpeedX;
    }

    public void setSpeedY(float playerSpeedY) {
        this.speedY = playerSpeedY;
    }

    public void setSpeedT(double speedT) {
        this.speedT = speedT;
    }

    public void setDirection(double direction) {
        this.direction = direction;
    }

    public void setAcceleration(float acceleration) {
        this.acceleration = acceleration;
    }

    public void setFriction(float friction) {
        this.friction = friction;
    }

    public void setBounceDampening(float bounceDampening) {
        this.bounceDampening = bounceDampening;
    }

    // Getters.
    public float getPositionX() {
        return positionX;
    }

    public float getPositionY() {
        return positionY;
    }

    public float getSpeedX() {
        return speedX;
    }

    public float getSpeedY() {
        return speedY;
    }

    public double getSpeedT() {
        return speedT;
    }

    public double getDirection() {
        return direction;
    }

    public float getAcceleration() {
        return acceleration;
    }

    public float getFriction() {
        return friction;
    }

    public float getBounceDampening() {
        return bounceDampening;
    }
}
