package Backend;

/**
 * The player.
 *
 * @author Kristian Honningsvag.
 */
public class Player {

    private float positionX;
    private float positionY;
    private float speedX;
    private float speedY;

    private double speedT;
    private double direction;

    private float acceleration = 0.35f;
    private float friction = 0.06f;
    private float bounceDampening = 0.4f;

    /**
     * Constructor.
     *
     * @param positionX Players x position.
     * @param positionY Players y position.
     * @param speedX Players speed in x direction.
     * @param speedY Players speed in y direction.
     */
    public Player(int positionX, int positionY, int speedX, int speedY) {
        this.positionX = positionX;
        this.positionY = positionY;
        this.speedX = speedX;
        this.speedY = speedY;
        calculateDirection();
    }

    /**
     * Call this function for each turn in the simulation.
     */
    public void act() {
        updatePosition();
        friction();
        calculateDirection();
    }

    /**
     * Updates the player position.
     */
    private void updatePosition() {
        positionX = positionX + speedX;
        positionY = positionY + speedY;
    }

    /**
     * Accelerates the player in the given direction.
     *
     * @param direction The direction of the acceleration.
     */
    public void accelerate(String direction) {
        if (direction.equalsIgnoreCase("up")) {
            setSpeedY(getSpeedY() - acceleration);
        }
        // Accelerate downwards.
        if (direction.equalsIgnoreCase("down")) {
            setSpeedY(getSpeedY() + acceleration);
        }
        // Accelerate left.
        if (direction.equalsIgnoreCase("left")) {
            setSpeedX(getSpeedX() - acceleration);
        }
        // Accelerate right.
        if (direction.equalsIgnoreCase("right")) {
            setSpeedX(getSpeedX() + acceleration);
        }
    }

    /**
     * Makes the player gradually come to a halt if no acceleration is applied.
     */
    private void friction() {

        if (speedX > 0 && speedX > friction) {
            speedX = speedX - friction;
        }
        if (speedX < 0 && Math.abs(speedX) > friction) {
            speedX = speedX + friction;
        }
        if (speedY > 0 && speedY > friction) {
            speedY = speedY - friction;
        }
        if (speedY < 0 && Math.abs(speedY) > friction) {
            speedY = speedY + friction;
        }

        // Complete halt if speed is lower than friction value.
        if (Math.abs(speedX) > 0 && Math.abs(speedX) < friction) {
            speedX = 0;
        }
        if (Math.abs(speedY) > 0 && Math.abs(speedY) < friction) {
            speedY = 0;
        }
    }

    /**
     * Calculates the direction and speed of the players current movement. NB:
     * Processing operates with an inverse y-axis.
     */
    private void calculateDirection() {

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

    /**
     * Changes player speed and direction upon collision with the outer walls.
     *
     * @param wall The wall that was hit.
     */
    public void wallBounce(String wall) {

        switch (wall) {

            // Right wall was hit.
            case "right":
                speedX = speedX * (-bounceDampening);
                speedY = speedY * (bounceDampening);
                updatePosition();
                break;

            // Lower wall was hit.
            case "lower":
                speedX = speedX * (bounceDampening);
                speedY = speedY * (-bounceDampening);
                updatePosition();
                break;

            // Left wall was hit.
            case "left":
                speedX = speedX * (-bounceDampening);
                speedY = speedY * (bounceDampening);
                updatePosition();
                break;

            // Upper wall was hit.
            case "upper":
                speedX = speedX * (bounceDampening);
                speedY = speedY * (-bounceDampening);
                updatePosition();
                break;
        }
        calculateDirection();
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
