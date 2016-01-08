package Backend;

/**
 * The player.
 *
 * @author Kristian Honningsvag.
 */
public class Player extends Actor {

    /**
     * Constructor.
     */
    public Player(float positionX, float positionY, float speedX, float speedY) {
        super(positionX, positionY, speedX, speedY);

        this.speedLimit = 8;
        this.acceleration = 0.35f;
        this.friction = 0.06f;
        this.bounceDampening = 0.4f;

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
            if (speedY > (-speedLimit)) {
                this.speedY = speedY - acceleration;
            }
        }
        // Accelerate downwards.
        if (direction.equalsIgnoreCase("down")) {
            if (speedY < (speedLimit)) {
                this.speedY = speedY + acceleration;
            }
        }
        // Accelerate left.
        if (direction.equalsIgnoreCase("left")) {
            if (speedX > (-speedLimit)) {
                this.speedX = speedX - acceleration;
            }
        }
        // Accelerate right.
        if (direction.equalsIgnoreCase("right")) {
            if (speedX < (speedLimit)) {
                this.speedX = speedX + acceleration;
            }
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
}
