package backend.shipmodule;

import backend.main.Timer;
import backend.actor.Actor;
import backend.actor.KamikazeDrone;
import backend.actor.DroneAI;
import backend.main.Vector;

/**
 * Launches drones.
 *
 * @author Kristian Honningsvag.
 */
public class DroneLauncher extends OffensiveModule {

    // Shape and color.
    private int turretLength = 60;
    private int turretWidth = 30;
    private int[] turretRGBA = new int[]{100, 100, 100, 255};

    private double timeBetweenShots = 600;
    private Timer timer = new Timer();

    private double spawnAngle = Math.PI / 4;
    private Vector spawnDirection1 = null;
    private Vector spawnDirection2 = null;
    private Vector spawnPosition1 = null;
    private Vector spawnPosition2 = null;
    private KamikazeDrone drone = null;

    /**
     * Constructor.
     */
    public DroneLauncher(Actor owner) {
        super("Drone Launcher", owner);
        updateVectors();
    }

    @Override
    public void draw() {

        updateVectors();

        owner.getGuiHandler().strokeWeight(turretWidth);
        owner.getGuiHandler().stroke(turretRGBA[0], turretRGBA[1], turretRGBA[2]);
        owner.getGuiHandler().fill(turretRGBA[0], turretRGBA[1], turretRGBA[2]);

        owner.getGuiHandler().pushMatrix();
        owner.getGuiHandler().translate((float) owner.getPosition().getX(), (float) owner.getPosition().getY());
        owner.getGuiHandler().rotate((float) spawnDirection1.getAngle2D());
        owner.getGuiHandler().line(0, 0, turretLength, 0);
        owner.getGuiHandler().popMatrix();

        owner.getGuiHandler().pushMatrix();
        owner.getGuiHandler().translate((float) owner.getPosition().getX(), (float) owner.getPosition().getY());
        owner.getGuiHandler().rotate((float) spawnDirection2.getAngle2D());
        owner.getGuiHandler().line(0, 0, turretLength, 0);
        owner.getGuiHandler().popMatrix();
    }

    /**
     * Updates vectors.
     */
    private void updateVectors() {
        spawnDirection1 = owner.getHeading().copy().normalize().rotate(spawnAngle);
        spawnDirection2 = owner.getHeading().copy().normalize().rotate(-spawnAngle);
        spawnPosition1 = owner.getPosition().copy().add(spawnDirection1.mult(owner.getHitBoxRadius()));
        spawnPosition2 = owner.getPosition().copy().add(spawnDirection2.mult(owner.getHitBoxRadius()));
    }

    /**
     * Launch a drone.
     */
    @Override
    public void activate() {

        if (timer.timePassed() >= timeBetweenShots) {   // Check fire rate.

            updateVectors();

            drone = new KamikazeDrone(spawnPosition1, this.getOwner().getGameEngine());
            drone.setAI(new DroneAI(owner.getGameEngine(), owner.getGameEngine().getCurrentLevel().getPlayer(), drone));
            owner.getGameEngine().getCurrentLevel().getEnemies().add(drone);
            owner.getGameEngine().getCurrentLevel().getActors().add(drone);

            drone = new KamikazeDrone(spawnPosition2, this.getOwner().getGameEngine());
            drone.setAI(new DroneAI(owner.getGameEngine(), owner.getGameEngine().getCurrentLevel().getPlayer(), drone));
            owner.getGameEngine().getCurrentLevel().getEnemies().add(drone);
            owner.getGameEngine().getCurrentLevel().getActors().add(drone);

            timer.restart();
        }
    }

}
