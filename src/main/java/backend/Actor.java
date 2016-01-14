package backend;

import userinterface.Drawable;
import userinterface.GUIHandler;

/**
 * Super class for all actors.
 *
 * @author Kristian Honningsvag.
 */
public abstract class Actor extends InteractableEntity implements Drawable {

    // Direction.
    protected double heading;       // radians
    protected double course;        // radians    (derived value)

    // Speed.
    protected double speedX;        // pixels/ms
    protected double speedY;        // pixels/ms
    protected double speedT;        // pixels/ms  (derived value)
    protected double speedLimit;    // pixels/ms

    // Acceleration.
    protected double accelerationX;   // pixels/ms^2
    protected double accelerationY;   // pixels/ms^2

    // Attributes.
    protected double hitBoxRadius;    // pixels
    protected double drag;            // pixels/ms^2
    protected double bounceModifier;
    protected double hitPoints;

    /**
     * Constructor.
     */
    protected Actor(double positionX, double positionY, GameEngine gameEngine, GUIHandler guiHandler) {

        super(positionX, positionY, gameEngine, guiHandler);

        // Default values. Overwrite as necessary.
        heading = 0;
        speedX = 0;
        speedY = 0;
        speedLimit = 0.5f;
        accelerationX = 0.001f;
        accelerationY = 0.001f;
        hitBoxRadius = 20;
        drag = 0.0005f;
        bounceModifier = 1.2f;
        hitPoints = 10;

        updateVectors();
    }

    @Override
    public abstract void draw();

    /**
     * Updates the actors total speed and direction of the current movement.
     */
    protected void updateVectors() {
        speedT = Math.sqrt(Math.pow(speedX, 2) + Math.pow(speedY, 2));
        course = NumberCruncher.calculateAngle(speedX, speedY);
    }

    /**
     * Updates the actors state. Should be called once each millisecond.
     */
    protected void act() {
        updatePosition();
        addFriction();
        updateVectors();
    }

    /**
     * Updates the actors position.
     */
    private void updatePosition() {
        // s = s0 + v*t, t=1
        positionX = positionX + speedX;
        positionY = positionY + speedY;
    }

    /**
     * Makes the actor gradually come to a halt if no acceleration is applied.
     */
    private void addFriction() {

        if (speedX > 0 && speedX > drag) {
            speedX = speedX - drag;
        }
        if (speedX < 0 && Math.abs(speedX) > drag) {
            speedX = speedX + drag;
        }
        if (speedY > 0 && speedY > drag) {
            speedY = speedY - drag;
        }
        if (speedY < 0 && Math.abs(speedY) > drag) {
            speedY = speedY + drag;
        }

//        // Complete halt if speed is very low.
//        if (Math.abs(speedX) > 0 && Math.abs(speedX) < 0.0001) {
//            speedX = 0;
//        }
//        if (Math.abs(speedY) > 0 && Math.abs(speedY) < 0.0001) {
//            speedY = 0;
//        }
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
                    speedX = speedX * (-bounceModifier);
//                    speedY = speedY * (bounceAmplifier);
                    act();
                    break;
                }
            // Lower wall was hit.
            case "lower":
                if (speedY > 0) {
//                    speedX = speedX * (bounceAmplifier);
                    speedY = speedY * (-bounceModifier);
                    act();
                    break;
                }
            // Left wall was hit.
            case "left":
                if (speedX < 0) {
                    speedX = speedX * (-bounceModifier);
//                    speedY = speedY * (bounceAmplifier);
                    act();
                    break;
                }
            // Upper wall was hit.
            case "upper":
                if (speedY < 0) {
//                    speedX = speedX * (bounceAmplifier);
                    speedY = speedY * (-bounceModifier);
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

    public double getHeading() {
        return heading;
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

    public double getCourse() {
        return course;
    }

    public double getAccelerationX() {
        return accelerationX;
    }

    public double getAccelerationY() {
        return accelerationY;
    }

    public double getHitBoxRadius() {
        return hitBoxRadius;
    }

    public double getDrag() {
        return drag;
    }

    public double getBounceModifier() {
        return bounceModifier;
    }

    public double getHitPoints() {
        return hitPoints;
    }

    public GameEngine getGameEngine() {
        return gameEngine;
    }

    // Setters.
    public void setPositionX(double positionX) {
        this.positionX = positionX;
    }

    public void setPositionY(double positionY) {
        this.positionY = positionY;
    }

    public void setHeading(double heading) {
        this.heading = heading;
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

    public void setAccelerationX(double accelerationX) {
        this.accelerationX = accelerationX;
    }

    public void setAccelerationY(double accelerationY) {
        this.accelerationY = accelerationY;
    }

    public void setHitBoxRadius(double hitBoxRadius) {
        this.hitBoxRadius = hitBoxRadius;
    }

    public void setDrag(double drag) {
        this.drag = drag;
    }

    public void setBounceModifier(double bounceModifier) {
        this.bounceModifier = bounceModifier;
    }

    public void setHitPoints(double hitPoints) {
        this.hitPoints = hitPoints;
    }

}
