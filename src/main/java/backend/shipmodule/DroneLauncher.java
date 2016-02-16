package backend.shipmodule;

import backend.main.Timer;
import backend.actor.Actor;
import backend.actor.Bullet;
import backend.actor.Drone;
import backend.actor.DroneAI;
import backend.actor.SlayerAI;

/**
 * Launches drones.
 *
 * @author Kristian Honningsvag.
 */
public class DroneLauncher extends OffensiveModule {

    // Shape.
    private int turretLength = 10;
    private int turretWidth = 3;
    private int[] turretRGBA = new int[]{70, 100, 100, 255};

    private double timeBetweenShots = 100;
    private Timer timer = new Timer();

    /**
     * Constructor.
     */
    public DroneLauncher(Actor owner) {
        super("Auto Cannon", owner);

        launchVelocity = 0.8;
        projectileDamage = 3;
    }

    @Override
    public void draw() {
        // MISSING.
    }

    /**
     * Launch a drone.
     */
    @Override
    public void activate() {

        if (timer.timePassed() >= timeBetweenShots) {   // Check fire rate.

            Drone drone = new Drone(owner.getPosition().copy().add(owner.getHeading().copy().normalize().mult(owner.getHitBoxRadius())), this.getOwner().getGameEngine());
            drone.setAI(new DroneAI(owner.getGameEngine(), owner.getGameEngine().getCurrentLevel().getPlayer(), drone));

            owner.getGameEngine().getCurrentLevel().getEnemies().add(drone);
            owner.getGameEngine().getCurrentLevel().getActors().add(drone);

            timer.restart();
        }
    }

}
