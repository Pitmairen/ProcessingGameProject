package Backend;

/**
 * Calculates vectors, angles, and other values to be used in the simulation.
 * NB: Positive Y-axis is downwards, and radial angles have their positive
 * direction clockwise.
 *
 * @author Kristian Honningsvag.
 */
public abstract class NumberCruncher {

    /**
     * Calculates the angle from one point to another.
     *
     * @param xVector Horizontal distance between the two points.
     * @param yVector Vertical distance between the two points.
     * @return The angle in radians.
     */
    public static double calculateAngle(double xVector, double yVector) {

        double angle = Math.atan(yVector / xVector);
        double targetAngle = 0;

        // If vector lies in quadrant 1
        if (xVector > 0 && yVector > 0) {
            targetAngle = angle;
        }
        // If vector lies in quadrant 2
        if (xVector < 0 && yVector > 0) {
            targetAngle = angle + Math.PI;
        }
        // If vector lies in quadrant 3
        if (xVector < 0 && yVector < 0) {
            targetAngle = angle + Math.PI;
        }
        // If vector lies in quadrant 4
        if (xVector > 0 && yVector < 0) {
            targetAngle = angle + 2 * Math.PI;
        }

        // If vector is straight to the right.
        if (xVector > 0 && yVector == 0) {
            targetAngle = 0;
        }
        // If vector is straight down.
        if (xVector == 0 && yVector > 0) {
            targetAngle = Math.PI / 2;
        }
        // If vector is straight to the left.
        if (xVector < 0 && yVector == 0) {
            targetAngle = Math.PI;
        }
        // If vector is straight up.
        if (xVector == 0 && yVector < 0) {
            targetAngle = (2 * Math.PI) - (Math.PI / 2);
        }
        // If vector is zero.
        if (xVector == 0 && yVector == 0) {
            targetAngle = 0;
        }
        return targetAngle;
    }

}
