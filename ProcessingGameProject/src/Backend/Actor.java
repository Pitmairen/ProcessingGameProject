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
    protected float hitPoints;

    /**
     * Constructor.
     */
    protected Actor(double positionX, double positionY, double speedX, double speedY) {

        this.positionX = positionX;
        this.positionY = positionY;
        this.speedX = speedX;
        this.speedY = speedY;

        updateVectors();

        speedLimit = 20;
        accelerationX = 0;
        accelerationY = 0;
        airResistance = 0;
        bounceAmplifier = 1;
        hitPoints = 10;
    }

    /**
     * Updates the actors total speed and direction of the current movement.
     */
    protected void updateVectors() {
        speedT = Math.sqrt(Math.pow(speedX, 2) + Math.pow(speedY, 2));
        direction = NumberCruncher.calculateAngle(speedX, speedY);

    }

    /**
     * Call this function for each turn in the simulation.
     */
    protected void act() {
        positionX = positionX + speedX;
        positionY = positionY + speedY;
        friction();
        updateVectors();
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
        updateVectors();
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

    public float getBounceAmplifier() {
        return bounceAmplifier;
    }

    public float getHitPoints() {
        return hitPoints;
    }

    // Setters.
    public void setPositionX(double positionX) {
        this.positionX = positionX;
    }

    public void setPositionY(double positionY) {
        this.positionY = positionY;
    }

    public void setSpeedX(double speedX) {
        this.speedX = speedX;
    }

    public void setSpeedY(double speedY) {
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

    public void setBounceAmplifier(float bounceAmplifier) {
        this.bounceAmplifier = bounceAmplifier;
    }

    public void setHitPoints(float hitPoints) {
        this.hitPoints = hitPoints;
    }
}
