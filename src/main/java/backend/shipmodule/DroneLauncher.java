package backend.shipmodule;

import backend.main.Timer;
import backend.actor.Actor;
import backend.actor.enemy.KamikazeDrone;
import backend.actor.ai.DroneAI;
import backend.main.Vector;
import backend.resources.Image;
import processing.core.PImage;

/**
 * Launches drones.
 *
 * @author Kristian Honningsvag.
 */
public class DroneLauncher extends OffensiveModule {

    // Shape and color.
    private int turretWidth = 30;
    private int[] turretRGBA = new int[]{100, 100, 100, 255};

    private double timeBetweenShots = 600;
    private Timer timer = new Timer();

    private double spawnAngle = Math.PI / 4;
    private Vector spawnPosition1 = null;
    private Vector spawnPosition2 = null;
    private KamikazeDrone drone = null;
    private final PImage laucherImg;

    /**
     * Constructor.
     */
    public DroneLauncher(Actor owner) {
        super("Drone Launcher", owner);
        updateVectors();
        laucherImg = owner.getGameEngine().getResourceManager().getImage(Image.DRONE_LAUNCHER);
    }

    @Override
    public void draw() {

        updateVectors();

        owner.getGuiHandler().strokeWeight(turretWidth);
        owner.getGuiHandler().stroke(turretRGBA[0], turretRGBA[1], turretRGBA[2]);
        owner.getGuiHandler().fill(turretRGBA[0], turretRGBA[1], turretRGBA[2]);

        owner.getGuiHandler().pushMatrix();
        owner.getGuiHandler().translate((float) owner.getPosition().getX(), (float) owner.getPosition().getY());
        owner.getGuiHandler().rotate((float) (owner.getHeading().getAngle2D() + Math.PI / 2));
        owner.getGuiHandler().imageMode(PImage.CENTER);
        owner.getGuiHandler().image(laucherImg, 0, 0, 181.4f, 84.0f);
        owner.getGuiHandler().imageMode(PImage.CORNER);
        owner.getGuiHandler().popMatrix();
    }

    /**
     * Updates vectors.
     */
    private void updateVectors() {
        Vector spawnDirection1 = owner.getHeading().copy().normalize().rotate(spawnAngle);
        Vector spawnDirection2 = owner.getHeading().copy().normalize().rotate(-spawnAngle);
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

            timer.reset();
        }
    }

}
