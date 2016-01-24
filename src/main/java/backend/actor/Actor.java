package backend.actor;

import backend.shipmodule.ShipModule;
import backend.main.CollisionDetector;
import backend.main.GameEngine;
import backend.main.NumberCruncher;
import java.util.ArrayList;
import userinterface.Drawable;
import userinterface.GUIHandler;

/**
 * Super class for all actors.
 *
 * @author Kristian Honningsvag.
 */
public abstract class Actor implements Drawable {

    // Position. (pixels)
    protected double positionX; // From constructor.
    protected double positionY; // From constructor.

    // Direction. (radians)
    protected double heading = 0;
    protected double course; // Derived value.

    // Speed. (pixels/ms)
    protected double speedX = 0;
    protected double speedY = 0;
    protected double speedT; // Derived value.
    protected double speedLimit = 0;

    // Acceleration. (pixels/ms^2)
    protected double accelerationX = 0;
    protected double accelerationY = 0;
    protected double drag = 0;

    // Attributes.
    protected double hitBoxRadius = 0;
    protected double bounceModifier = 0;
    protected double hitPoints = 0;
    protected double mass = 0;
    protected double momentum; // Derived value.
    protected double collisionDamageToOthers = 0;
    protected int score = 0;
    protected ArrayList<ShipModule> shipModules = new ArrayList<>();

    // Environment variables.
    protected boolean primaryWeaponState = false;
    protected boolean secondaryWeaponState = false;

    // Simulation.
    protected GameEngine gameEngine;               // From constructor.
    protected GUIHandler guiHandler;               // Set in constructor.
    protected CollisionDetector collisionDetector; // Set in constructor.

    /**
     * Constructor.
     *
     * @param positionX Actors X-position in pixels.
     * @param positionY Actors Y-position in pixels.
     * @param gameEngine The game engine in charge of the simulation.
     */
    protected Actor(double positionX, double positionY, GameEngine gameEngine) {

        this.positionX = positionX;
        this.positionY = positionY;
        this.gameEngine = gameEngine;

        guiHandler = gameEngine.getGuiHandler();
        collisionDetector = gameEngine.getCollisionDetector();

        updateVectors();
    }

    @Override
    public abstract void draw();

    /**
     * Updates the actors state. Should be called once each cycle of the
     * simulation.
     *
     * @param timePassed Time passed since last simulation cycle in
     * milliseconds. Used in calculations.
     */
    public void act(double timePassed) {
        addFriction(timePassed);
        updatePosition(timePassed);
        checkWallCollisions(timePassed);
        checkActorCollisions(timePassed);
    }

    /**
     * Updates the actors derived variables.
     */
    protected void updateVectors() {
        course = NumberCruncher.calculateAngle(speedX, speedY);
        speedT = Math.sqrt(Math.pow(speedX, 2) + Math.pow(speedY, 2));
        momentum = mass * speedT;
    }

    /**
     * Updates the actors position.
     *
     * @param timePassed Number of milliseconds since the previous simulation
     * cycle.
     */
    protected void updatePosition(double timePassed) {
        positionX = positionX + speedX * timePassed;   // s = s0 + v*dt
        positionY = positionY + speedY * timePassed;
    }

    /**
     * Return actor to the previous position.
     *
     * @param timePassed Number of milliseconds since the previous simulation
     * cycle.
     */
    protected void rollbackPosition(double timePassed) {
        positionX = positionX - speedX * timePassed;
        positionY = positionY - speedY * timePassed;
    }

    /**
     * Makes the actor gradually come to a halt if no acceleration is applied.
     *
     * @param timePassed Number of milliseconds since the previous simulation
     * cycle.
     */
    protected void addFriction(double timePassed) {

        if (speedX > 0) {
            if (Math.abs(speedX) < drag * Math.cos(course) * timePassed) {
                speedX = 0;
            } else {
                speedX = speedX - drag * Math.cos(course) * timePassed;
            }
        }

        if (speedX < 0) {
            if (Math.abs(speedX) < drag * Math.cos(course) * timePassed) {
                speedX = 0;
            } else {
                speedX = speedX - drag * Math.cos(course) * timePassed;
            }
        }

        if (speedY > 0) {
            if (Math.abs(speedY) < drag * Math.sin(course) * timePassed) {
                speedY = 0;
            } else {
                speedY = speedY - drag * Math.sin(course) * timePassed;
            }
        }

        if (speedY < 0) {
            if (Math.abs(speedY) < drag * Math.sin(course) * timePassed) {
                speedY = 0;
            } else {
                speedY = speedY - drag * Math.sin(course) * timePassed;
            }
        }
        updateVectors();
    }

    /**
     * Check for wall collisions and react to them.
     *
     * @param timePassed Number of milliseconds since the previous simulation
     * cycle.
     */
    protected void checkWallCollisions(double timePassed) {

        String wallCollision = gameEngine.getCollisionDetector().detectWallCollision(this);

        if (wallCollision != null) {
            wallBounce(wallCollision, timePassed);
        }
    }

    /**
     * Changes actor speed and direction upon collision with the outer walls.
     *
     * @param wall The wall that was hit.
     * @param timePassed Number of milliseconds since the previous simulation
     * cycle.
     */
    protected void wallBounce(String wall, double timePassed) {

        rollbackPosition(timePassed);    // First move the actor out of the wall.

        switch (wall) {

            case "east":
                if (speedX > 0) {
                    speedX = speedX * (-bounceModifier);
//                    speedY = speedY * (bounceModifier);
                    break;
                }

            case "south":
                if (speedY > 0) {
//                    speedX = speedX * (bounceModifier);
                    speedY = speedY * (-bounceModifier);
                    break;
                }

            case "west":
                if (speedX < 0) {
                    speedX = speedX * (-bounceModifier);
//                    speedY = speedY * (bounceModifier);
                    break;
                }

            case "north":
                if (speedY < 0) {
//                    speedX = speedX * (bounceModifier);
                    speedY = speedY * (-bounceModifier);
                    break;
                }

        }
        updateVectors();
        updatePosition(timePassed);
    }

    /**
     * Check for actor collisions and react to them.
     *
     * @param timePassed Number of milliseconds since the previous simulation
     * cycle.
     */
    protected void checkActorCollisions(double timePassed) {

        ArrayList<Actor> collisions = collisionDetector.detectActorCollision(this);

        if (collisions.size() > 0) {

            for (Actor actorInList : collisions) {

                if ((actorInList instanceof Projectile)) {
                    if (((Projectile) actorInList).getShipModule().getOwner() == this) {
                        // Actors can't collide with their own projectiles.
                    } else {
                        // You got hit by an unfriendly projectile.
                        elasticColision(this, actorInList, timePassed);
                        this.removeHitPoints(actorInList.getCollisionDamageToOthers());
                        actorInList.removeHitPoints(this.collisionDamageToOthers);
                        ((Projectile) actorInList).targetHit();
                    }
                } else if (actorInList.getClass() == this.getClass()) {
                    // No collision damage when hitting an actor of the same type.
                    elasticColision(this, actorInList, timePassed);
                } else {
                    // You collided with an actor.
                    elasticColision(this, actorInList, timePassed);
                    this.removeHitPoints(actorInList.getCollisionDamageToOthers());
                    actorInList.removeHitPoints(this.collisionDamageToOthers);
                }
            }
        }
    }

    /**
     * Calculates the resulting speed and direction after a fully elastic head
     * on collision between two actors.
     *
     * @param a First actor.
     * @param b Second actor.
     * @param timePassed Number of milliseconds since the previous simulation
     * cycle.
     */
    protected void elasticColision(Actor a, Actor b, double timePassed) {

        rollbackPosition(timePassed);

        double speedFinalAx;
        double speedFinalAy;
        double speedFinalBx;
        double speedFinalBy;

        speedFinalAx = (a.getMass() - b.getMass()) / (a.getMass() + b.getMass()) * a.getSpeedX()
                + (2 * b.getMass()) / (a.getMass() + b.getMass()) * (b.getSpeedX());

        speedFinalAy = (a.getMass() - b.getMass()) / (a.getMass() + b.getMass()) * a.getSpeedY()
                + (2 * b.getMass()) / (a.getMass() + b.getMass()) * (b.getSpeedY());

        speedFinalBx = (2 * a.getMass()) / (a.getMass() + b.getMass()) * a.getSpeedX()
                - (a.getMass() - b.getMass()) / (a.getMass() + b.getMass()) * (b.getSpeedX());

        speedFinalBy = (2 * a.getMass()) / (a.getMass() + b.getMass()) * a.getSpeedY()
                - (a.getMass() - b.getMass()) / (a.getMass() + b.getMass()) * (b.getSpeedY());

        a.setSpeedX(speedFinalAx);
        a.setSpeedY(speedFinalAy);
        b.setSpeedX(speedFinalBx);
        b.setSpeedY(speedFinalBy);

        updateVectors();
        updatePosition(timePassed);
    }

    /**
     * Accelerates the actor in the given direction.
     *
     * @param direction The direction of the acceleration.
     * @param timePassed Number of milliseconds since the previous simulation
     * cycle.
     */
    public void accelerate(String direction, double timePassed) {

        // Accelerate upwards.
        if (direction.equalsIgnoreCase("up")) {
            if (speedY > (-speedLimit)) {
                speedY = speedY - accelerationY * timePassed;
            }
        }
        // Accelerate downwards.
        if (direction.equalsIgnoreCase("down")) {
            if (speedY < (speedLimit)) {
                speedY = speedY + accelerationY * timePassed;
            }
        }
        // Accelerate left.
        if (direction.equalsIgnoreCase("left")) {
            if (speedX > (-speedLimit)) {
                speedX = speedX - accelerationX * timePassed;
            }
        }
        // Accelerate right.
        if (direction.equalsIgnoreCase("right")) {
            if (speedX < (speedLimit)) {
                speedX = speedX + accelerationX * timePassed;
            }
        }
        updateVectors();
    }

    /**
     * Increases an actors hit points by an set amount.
     *
     * @param healing Number of hit points to add.
     */
    public void addHitPoints(double healing) {
        this.hitPoints = this.hitPoints + healing;
    }

    /**
     * Decreases an actors hit points by an set amount.
     *
     * @param damage Number of hit points to subtract.
     */
    public void removeHitPoints(double damage) {
        this.hitPoints = this.hitPoints - damage;
    }

    /**
     * Increases the actors score by an given amount.
     *
     * @param points How much to increase the score by.
     */
    public void increaseScore(int points) {
        this.score = score + points;
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

    public double getCourse() {
        return course;
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

    public double getAccelerationX() {
        return accelerationX;
    }

    public double getAccelerationY() {
        return accelerationY;
    }

    public double getDrag() {
        return drag;
    }

    public double getHitBoxRadius() {
        return hitBoxRadius;
    }

    public double getBounceModifier() {
        return bounceModifier;
    }

    public double getHitPoints() {
        return hitPoints;
    }

    public double getMass() {
        return mass;
    }

    public double getMomentum() {
        return momentum;
    }

    public boolean isPrimaryWeaponState() {
        return primaryWeaponState;
    }

    public boolean isSecondaryWeaponState() {
        return secondaryWeaponState;
    }

    public GameEngine getGameEngine() {
        return gameEngine;
    }

    public GUIHandler getGuiHandler() {
        return guiHandler;
    }

    public CollisionDetector getCollisionDetector() {
        return collisionDetector;
    }

    public double getCollisionDamageToOthers() {
        return collisionDamageToOthers;
    }

    public int getScore() {
        return score;
    }

    public ArrayList<ShipModule> getShipModules() {
        return shipModules;
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

    public void setDrag(double drag) {
        this.drag = drag;
    }

    public void setHitBoxRadius(double hitBoxRadius) {
        this.hitBoxRadius = hitBoxRadius;
    }

    public void setBounceModifier(double bounceModifier) {
        this.bounceModifier = bounceModifier;
    }

    public void setHitPoints(double hitPoints) {
        this.hitPoints = hitPoints;
    }

    public void setMass(double mass) {
        this.mass = mass;
    }

    public void setPrimaryWeaponState(boolean primaryWeaponState) {
        this.primaryWeaponState = primaryWeaponState;
    }

    public void setSecondaryWeaponState(boolean secondaryWeaponState) {
        this.secondaryWeaponState = secondaryWeaponState;
    }

    public void setGameEngine(GameEngine gameEngine) {
        this.gameEngine = gameEngine;
    }

    public void setGuiHandler(GUIHandler guiHandler) {
        this.guiHandler = guiHandler;
    }

}
