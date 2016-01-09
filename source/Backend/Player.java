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
    public Player(double positionX, double positionY, double speedX, double speedY) {
        super(positionX, positionY, speedX, speedY);

        speedLimit = 6;
        accelerationX = 0.2f;
        accelerationY = 0.2f;
        airResistance = 0.06f;
        bounceAmplifier = 1.2f;
        hitPoints = 30;
    }

    /**
     * Call this function for each turn in the simulation.
     */
    @Override
    public void act() {
        super.act();
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
