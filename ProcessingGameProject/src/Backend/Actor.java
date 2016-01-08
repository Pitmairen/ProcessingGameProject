package Backend;

/**
 * Super class for all actors.
 *
 * @author Kristian Honningsvag.
 */
public abstract class Actor {

    // Position.
    protected double positionX;
    protected double positionY;

    // Speed.
    protected double speedX;
    protected double speedY;
    protected double speedT;
    protected double speedLimit;

    // Direction.
    protected double direction;

    // Acceleration.
    protected float accelerationX;
    protected float accelerationY;

    // Characteristics.
    protected float airResistance;
    protected float bounceAmplifier;

    /**
     * Constructor.
     */
    protected Actor(double positionX, double positionY, double speedX, double speedY) {

        this.positionX = positionX;
        this.positionY = positionY;
        this.speedX = speedX;
        this.speedY = speedY;

        calculateSpeed();
        calculateDirection();

        speedLimit = 20;
        accelerationX = 0;
        accelerationY = 0;
        airResistance = 0;
        bounceAmplifier = 1;
    }

    /**
     * Calculates the actors total speed.
     */
    protected void calculateSpeed() {
        speedT = Math.sqrt(Math.pow(speedX, 2) + Math.pow(speedY, 2));
    }

    /**
     * Calculates the direction of the actors current movement.
     * NB: Processing operates with an inverse y-axis.
     */
    protected void calculateDirection() {

        double angle = Math.atan(speedY / speedX);

        // If speed vector lies in processing quadrant 1
        if (speedX > 0 && speedY > 0) {
            direction = angle;
        }
        // If speed vector lies in processing quadrant 2
        if (speedX < 0 && speedY > 0) {
            direction = angle + Math.PI;
        }
        // If speed vector lies in processing quadrant 3
        if (speedX < 0 && speedY < 0) {
            direction = angle + Math.PI;
        }
        // If speed vector lies in processing quadrant 4
        if (speedX > 0 && speedY < 0) {
            direction = angle + 2 * Math.PI;
        }

        // If speed vector is straight to the right.
        if (speedX > 0 && speedY == 0) {
            direction = 0;
        }
        // If speed vector is straight down.
        if (speedX == 0 && speedY > 0) {
            direction = Math.PI / 2;
        }
        // If speed vector is straight to the left.
        if (speedX < 0 && speedY == 0) {
            direction = Math.PI;
        }
        // If speed vector is straight up.
        if (speedX == 0 && speedY < 0) {
            direction = (2 * Math.PI) - (Math.PI / 2);
        }
        // If standing still.
        if (speedX == 0 && speedY == 0) {
            direction = 0;
        }
    }

    /**
     * Call this function for each turn in the simulation.
     */
    protected void act() {
        positionX = positionX + speedX;
        positionY = positionY + speedY;
        friction();
        calculateSpeed();
        calculateDirection();
    }

    /**
     * Makes the actor gradually come to a halt if no acceleration is applied.
     */
    private void friction() {

        if (speedX > 0 && speedX > airResistance) {
            speedX = speedX - airResistance;
        }
        if (speedX < 0 && Math.abs(speedX) > airResistance) {
            speedX = speedX + airResistance;
        }
        if (speedY > 0 && speedY > airResistance) {
            speedY = speedY - airResistance;
        }
        if (speedY < 0 && Math.abs(speedY) > airResistance) {
            speedY = speedY + airResistance;
        }

        // Complete halt if speed is lower than friction value.
        if (Math.abs(speedX) > 0 && Math.abs(speedX) < airResistance) {
            speedX = 0;
        }
        if (Math.abs(speedY) > 0 && Math.abs(speedY) < airResistance) {
            speedY = 0;
        }
    }

    /**
     * Changes actor speed and direction upon collision with the outer walls.
     *
     * @param wall The wall that was hit.
     */
    public void wallBounce(String wall) {

        switch (wall) {

            // Right wall was hit.
            case "right":
                if (speedX > 0) {
                    speedX = speedX * (-bounceAmplifier);
//                    speedY = speedY * (bounceAmplifier);
                    act();
                    break;
                }

            // Lower wall was hit.
            case "lower":
                if (speedY > 0) {
//                    speedX = speedX * (bounceAmplifier);
                    speedY = speedY * (-bounceAmplifier);
                    act();
                    break;
                }

            // Left wall was hit.
            case "left":
                if (speedX < 0) {
                    speedX = speedX * (-bounceAmplifier);
//                    speedY = speedY * (bounceAmplifier);
                    act();
                    break;
                }

            // Upper wall was hit.
            case "upper":
                if (speedY < 0) {
//                    speedX = speedX * (bounceAmplifier);
                    speedY = speedY * (-bounceAmplifier);
                    act();
                    break;
                }
        }
        calculateSpeed();
        calculateDirection();
    }

    // Getters.
    public double getPositionX() {
        return positionX;
    }

    public double getPositionY() {
        return positionY;
    }

    public double getSpeedX() {
        return speedX;
    }

    public double getSpeedY() {
        return speedY;
    }

    public double getSpeedT() {
        return speedT;
    }

    public double getSpeedLimit() {
        return speedLimit;
    }

    public double getDirection() {
        return direction;
    }

    public float getAccelerationX() {
        return accelerationX;
    }

    public float getAccelerationY() {
        return accelerationY;
    }

    public float getAirResistance() {
        return airResistance;
    }

    public float getBounceDampening() {
        return bounceAmplifier;
    }

    // Setters.
    public void setPositionX(float positionX) {
        this.positionX = positionX;
    }

    public void setPositionY(float positionY) {
        this.positionY = positionY;
    }

    public void setSpeedX(float speedX) {
        this.speedX = speedX;
    }

    public void setSpeedY(float speedY) {
        this.speedY = speedY;
    }

    public void setSpeedLimit(double speedLimit) {
        this.speedLimit = speedLimit;
    }

    public void setAccelerationX(float accelerationX) {
        this.accelerationX = accelerationX;
    }

    public void setAccelerationY(float accelerationY) {
        this.accelerationY = accelerationY;
    }

    public void setAirResistance(float airResistance) {
        this.airResistance = airResistance;
    }

    public void setBounceDampening(float bounceDampening) {
        this.bounceAmplifier = bounceDampening;
    }
}
