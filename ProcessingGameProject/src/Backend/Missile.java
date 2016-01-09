package Backend;

/**
 * A missile.
 *
 * @author Kristian Honningsvag.
 */
public class Missile extends Actor {

    private double targetAngle;

    /**
     * Constructor.
     */
    public Missile(double positionX, double positionY, double speedX, double speedY, double targetAngle) {
        super(positionX, positionY, speedX, speedY);

        speedLimit = 9;
        accelerationX = 1.06f;
        accelerationY = 1.06f;
        airResistance = 0.06f;
        bounceAmplifier = 1.2f;
        hitPoints = 1;

        this.targetAngle = targetAngle;
    }

    /**
     * Call this function for each turn in the simulation.
     */
    public void act() {
        accelerate();
        super.act();
    }

    /**
     * Accelerates the missile.
     */
    private void accelerate() {
        if (speedT < speedLimit) {
            speedX = speedX + (accelerationX * Math.cos(targetAngle));
            speedY = speedY + (accelerationY * Math.sin(targetAngle));
        }
    }
}
