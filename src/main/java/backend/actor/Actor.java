package backend.actor;

import backend.shipmodule.ShipModule;
import backend.main.CollisionDetector;
import backend.main.GameEngine;
import backend.main.Timer;
import backend.main.Vector;
import java.util.ArrayList;
import userinterface.Drawable;
import userinterface.GUIHandler;

/**
 * Super class for all actors. An actor is an entity that can actively interact
 * with the player.
 *
 * @author Kristian Honningsvag.
 */
public abstract class Actor implements Drawable {

    // Vectors.
    protected Vector position = new Vector();
    protected Vector speedT = new Vector();
    protected Vector forceT = new Vector();     // The sum of all the forces working on the actor.
    protected Vector accelerationT = new Vector();
    protected Vector heading = new Vector();    // Which direction the actor is currently pointing.

    // Attributes.
    protected String name = "NAME NOT SET";
    protected double hitBoxRadius = 0;
    protected double mass = 0;
    protected double engineThrust = 0;
    protected double frictionCoefficient = 0;
    protected double bounceModifier = 0;
    protected double maxHitPoints = 0;
    protected double currentHitPoints = 0;
    protected double collisionDamageToOthers = 0;

    // Score system.
    protected int killValue = 0;
    protected int killChain = 0;
    protected int score = 0;

    // Modules.
    protected ArrayList<ShipModule> offensiveModules = new ArrayList<>();
    protected ArrayList<ShipModule> defensiveModules = new ArrayList<>();
    protected ShipModule currentOffensiveModule = null;
    protected ShipModule currentDefensiveModule = null;

    // Simulation.
    protected GameEngine gameEngine;               // From constructor.
    protected GUIHandler guiHandler;               // Set in constructor.
    protected CollisionDetector collisionDetector; // Set in constructor.
    protected Actor whoHitMeLast = null;
    protected Timer timer = new Timer();

    /**
     * Constructor.
     *
     * @param position The actors position in vector form.
     * @param gameEngine The game engine in charge of the simulation.
     */
    protected Actor(Vector position, GameEngine gameEngine) {

        this.position = position;
        this.gameEngine = gameEngine;

        guiHandler = gameEngine.getGuiHandler();
        collisionDetector = gameEngine.getCollisionDetector();
    }

    @Override
    public abstract void draw();

    /**
     * This method handles what happens to an actor when it dies/is destroyed.
     */
    public abstract void die();

    /**
     * Updates the actors state. Should be called once each cycle of the
     * simulation.
     *
     * @param timePassed Time passed since last simulation cycle in
     * milliseconds. Used in calculations.
     */
    public void act(double timePassed) {
        addFriction();
        calcAcceleration();
        calcSpeed(timePassed);
        updatePosition(timePassed);
        checkWallCollisions(timePassed);
        checkActorCollisions(timePassed);
    }

    /**
     * Updates the actors position.
     *
     * @param timePassed Number of milliseconds since the previous simulation
     * cycle.
     */
    protected void updatePosition(double timePassed) {
        position.add(speedT.copy().mult(timePassed));    // s = s0 + v*dt
    }

    /**
     * Return actor to the previous position.
     *
     * @param timePassed Number of milliseconds since the previous simulation
     * cycle.
     */
    protected void rollbackPosition(double timePassed) {
        position.sub(speedT.copy().mult(timePassed));
    }

    /**
     * Calculates the new speed vector based on the old one and the current
     * acceleration.
     *
     * @param timePassed Number of milliseconds since the previous simulation
     * cycle.
     */
    protected void calcSpeed(double timePassed) {
        speedT.add(accelerationT.copy().mult(timePassed));    // v = v0 + a*dt
        // Set speed to zero if the current speed is close to zero.
        // This is to prevent the actors from never coming to a complete halt.
        if (speedT.mag() < 0.001) {
            speedT.setMag(0);
        }
        accelerationT.set(0, 0, 0);   // Reset the acceleration so it does not build up.
    }

    /**
     * Calculates the current acceleration based on the actors mass and the sum
     * of all of the forces applied.
     */
    protected void calcAcceleration() {
        accelerationT.add(forceT.copy().div(mass));     // f=m*a  =>  a=f/m
        forceT.set(0, 0, 0);     // Reset the forces so it does not build up.
    }

    /**
     * Adds a friction force in the opposite direction of the current speed
     * vector. The magnitude of the friction force increases as the speed
     * increases. This makes the actor gradually come to a halt if no
     * acceleration is applied, and effectively sets a top speed limit for the
     * actor.
     */
    protected void addFriction() {
        double frictionMagnitude = speedT.mag() * frictionCoefficient;
        Vector frictionDirection = speedT.copy().normalize();
        forceT.sub(frictionDirection.mult(frictionMagnitude));
    }

    /**
     * Accelerates the actor in the given direction.
     *
     * @param direction The direction of the accelerationTemp.
     * @param timePassed Number of milliseconds since the previous simulation
     * cycle.
     */
    public void accelerate(String direction, double timePassed) {

        // Accelerate upwards.
        if (direction.equalsIgnoreCase("up")) {
            this.forceT.add(new Vector(0, -1, 0).mult(engineThrust));
        }
        // Accelerate downwards.
        if (direction.equalsIgnoreCase("down")) {
            this.forceT.add(new Vector(0, 1, 0).mult(engineThrust));
        }
        // Accelerate left.
        if (direction.equalsIgnoreCase("left")) {
            this.forceT.add(new Vector(-1, 0, 0).mult(engineThrust));
        }
        // Accelerate right.
        if (direction.equalsIgnoreCase("right")) {
            this.forceT.add(new Vector(1, 0, 0).mult(engineThrust));
        }
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
     * Changes actor speedT and direction upon collision with the outer walls.
     *
     * @param wall The wall that was hit.
     * @param timePassed Number of milliseconds since the previous simulation
     * cycle.
     */
    protected void wallBounce(String wall, double timePassed) {

        rollbackPosition(timePassed);    // First move the actor out of the wall.

        switch (wall) {

            case "east":
                if (this.speedT.getX() > 0) {
                    this.getSpeedT().setX(this.getSpeedT().getX() * (-bounceModifier));
                    break;
                }

            case "south":
                if (this.speedT.getY() > 0) {
                    this.getSpeedT().setY(this.getSpeedT().getY() * (-bounceModifier));
                    break;
                }

            case "west":
                if (this.speedT.getX() < 0) {
                    this.getSpeedT().setX(this.getSpeedT().getX() * (-bounceModifier));
                    break;
                }

            case "north":
                if (this.speedT.getY() < 0) {
                    this.getSpeedT().setY(this.getSpeedT().getY() * (-bounceModifier));
                    break;
                }

        }
        updatePosition(timePassed);
    }

    /**
     * Check for collisions with other actors and react to them.
     *
     * @param timePassed Number of milliseconds since the previous simulation
     * cycle.
     */
    protected void checkActorCollisions(double timePassed) {
        ArrayList<Actor> collisions = collisionDetector.detectActorCollision(this);

        if (collisions.size() > 0) {

            for (Actor target : collisions) {
                // This actor ran into another actor.
                elasticColision(this, target, timePassed);
                this.collision(target);
                target.collision(this);
            }
        }
    }

    /**
     * Calculates the resulting speedT and direction after a fully elastic head
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

        speedFinalAx = (a.getMass() - b.getMass()) / (a.getMass() + b.getMass()) * a.getSpeedT().getX()
                + (2 * b.getMass()) / (a.getMass() + b.getMass()) * (b.getSpeedT().getX());

        speedFinalAy = (a.getMass() - b.getMass()) / (a.getMass() + b.getMass()) * a.getSpeedT().getY()
                + (2 * b.getMass()) / (a.getMass() + b.getMass()) * (b.getSpeedT().getY());

        speedFinalBx = (2 * a.getMass()) / (a.getMass() + b.getMass()) * a.getSpeedT().getX()
                - (a.getMass() - b.getMass()) / (a.getMass() + b.getMass()) * (b.getSpeedT().getX());

        speedFinalBy = (2 * a.getMass()) / (a.getMass() + b.getMass()) * a.getSpeedT().getY()
                - (a.getMass() - b.getMass()) / (a.getMass() + b.getMass()) * (b.getSpeedT().getY());

        a.getSpeedT().set(speedFinalAx, speedFinalAy, 0);
        b.getSpeedT().set(speedFinalBx, speedFinalBy, 0);

        updatePosition(timePassed);
    }

    /**
     * Increases an actors hit points by an set amount.
     *
     * @param healing Number of hit points to add.
     */
    public void addHitPoints(double healing) {
        this.currentHitPoints = this.currentHitPoints + healing;
    }

    /**
     * Decreases an actors hit points by an set amount. Taking damage also
     * resets the kill chain.
     *
     * @param damage Number of hit points to subtract.
     */
    public void removeHitPoints(double damage) {
        this.currentHitPoints = this.currentHitPoints - damage;
        if (currentHitPoints < 0) {
            currentHitPoints = 0;
        }
        killChain = 0;
    }

    /**
     * Handles what will happen to this actor upon collision with other actors.
     *
     * @param actor The actor which this actor collided with.
     */
    public void collision(Actor actor) {
        removeHitPoints(actor.getCollisionDamageToOthers());
        whoHitMeLast = actor;
    }

    /**
     * Increases the actors score by an given amount.
     *
     * @param points How much to increase the score by.
     */
    public void increaseScore(int points) {
        this.score = score + points;
    }

    /**
     * Increases the actors kill chain by an set amount.
     *
     * @param points How much to increase the kill chain by.
     */
    public void increaseKillChain(int points) {
        killChain = killChain + 1;
    }

    // Getters.
    public double getHitBoxRadius() {
        return hitBoxRadius;
    }

    public double getBounceModifier() {
        return bounceModifier;
    }

    public double getCurrentHitPoints() {
        return currentHitPoints;
    }

    public double getMass() {
        return mass;
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

    public ArrayList<ShipModule> getOffensiveModules() {
        return offensiveModules;
    }

    public int getKillValue() {
        return killValue;
    }

    public int getKillChain() {
        return killChain;
    }

    public ArrayList<ShipModule> getDefensiveModules() {
        return defensiveModules;
    }

    public Actor getWhoHitMeLast() {
        return whoHitMeLast;
    }

    public String getName() {
        return name;
    }

    public double getMaxHitPoints() {
        return maxHitPoints;
    }

    public ShipModule getCurrentOffensiveModule() {
        return currentOffensiveModule;
    }

    public ShipModule getCurrentDefensiveModule() {
        return currentDefensiveModule;
    }

    public Vector getPosition() {
        return position;
    }

    public Vector getSpeedT() {
        return speedT;
    }

    public Vector getForceT() {
        return forceT;
    }

    public Vector getAccelerationT() {
        return accelerationT;
    }

    public double getEngineThrust() {
        return engineThrust;
    }

    public double getFrictionCoefficient() {
        return frictionCoefficient;
    }

    public Vector getHeading() {
        return heading;
    }

    public Timer getTimer() {
        return timer;
    }

    // Setters.
    @Deprecated
    public void setCurrentHitPoints(double currentHitPoints) {
        this.currentHitPoints = currentHitPoints;
    }

}
