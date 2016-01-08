package Backend;

/**
 * An enemy frigate. Small and fast.
 *
 * @author Kristian Honningsvag.
 */
public class EnemyFrigate extends Actor {

    Player player;

    /**
     * Constructor.
     */
    public EnemyFrigate(double positionX, double positionY, double speedX, double speedY, Player player) {
        super(positionX, positionY, speedX, speedY);

        speedLimit = 8;
        accelerationX = 0.45f;
        accelerationY = 0.45f;
        airResistance = 0.06f;
        bounceAmplifier = 1.2f;

        this.player = player;
    }

    /**
     * Call this function for each turn in the simulation.
     */
    @Override
    public void act() {
        approachPlayer();
        super.act();
    }

    /**
     * Accelerate towards the player.
     */
    public void approachPlayer() {

        double xVector = player.getPositionX() - this.positionX;
        double yVector = player.getPositionY() - this.positionY;

        double angle = Math.atan(yVector / xVector);
        double targetAngle = 0;

        // If speed vector lies in processing quadrant 1
        if (xVector > 0 && yVector > 0) {
            targetAngle = angle;
        }
        // If speed vector lies in processing quadrant 2
        if (xVector < 0 && yVector > 0) {
            targetAngle = angle + Math.PI;
        }
        // If speed vector lies in processing quadrant 3
        if (xVector < 0 && yVector < 0) {
            targetAngle = angle + Math.PI;
        }
        // If speed vector lies in processing quadrant 4
        if (xVector > 0 && yVector < 0) {
            targetAngle = angle + 2 * Math.PI;
        }

        // If speed vector is straight to the right.
        if (xVector > 0 && yVector == 0) {
            targetAngle = 0;
        }
        // If speed vector is straight down.
        if (xVector == 0 && yVector > 0) {
            targetAngle = Math.PI / 2;
        }
        // If speed vector is straight to the left.
        if (xVector < 0 && yVector == 0) {
            targetAngle = Math.PI;
        }
        // If speed vector is straight up.
        if (xVector == 0 && yVector < 0) {
            targetAngle = (2 * Math.PI) - (Math.PI / 2);
        }
        // If standing still.
        if (xVector == 0 && yVector == 0) {
            targetAngle = 0;
        }

        if (speedT < speedLimit) {
            speedX = speedX + (accelerationX * Math.cos(targetAngle));
            speedY = speedY + (accelerationY * Math.sin(targetAngle));
        }
    }
}
