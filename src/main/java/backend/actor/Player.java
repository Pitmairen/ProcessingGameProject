package backend.actor;

import backend.shipmodule.RocketLauncher;
import backend.main.GameEngine;
import backend.main.NumberCruncher;
import backend.main.Timer;
import backend.shipmodule.AutoCannon;
import backend.shipmodule.LaserCannon;
import backend.shipmodule.ShipModule;
import userinterface.Drawable;

/**
 * The player.
 *
 * @author Kristian Honningsvag.
 */
public class Player extends Actor implements Drawable {

    // Shape.
    private int turretLength = 27;
    private int turretWidth = 4;

    // Color.
    private int[] bodyRGBA = new int[]{0, 70, 200, 255};
    private int[] turretRGBA = new int[]{30, 30, 200, 255};
    private final int backgroundColor; // Set in constructor.

    // Modules.
    private AutoCannon autoCannon = new AutoCannon(this);
    private LaserCannon laserCannon = new LaserCannon(this);
    private RocketLauncher rocketLauncher = new RocketLauncher(this);

    private ShipModule currentOffensiveModule = autoCannon;
    private ShipModule currentDefensiveModule = null;
    private Timer offensiveModuleTimer = new Timer();
    private Timer defensiveModuleTimer = new Timer();
    private double offensiveModuleSwapSpeed = 800;
    private double defensiveModuleSwapSpeed = 800;

    /**
     * Constructor.
     */
    public Player(double positionX, double positionY, GameEngine gameEngine) {

        super(positionX, positionY, gameEngine);

        name = "Player";
        speedLimit = 0.6f;
        acceleration = 0.002f;
        drag = 0.001f;
        hitBoxRadius = 20;
        bounceModifier = 0.6f;
        hitPoints = 30;
        mass = 100;
        backgroundColor = guiHandler.color(bodyRGBA[0], bodyRGBA[1], bodyRGBA[2], 255);
        collisionDamageToOthers = 4;

        offensiveModules.add(autoCannon);
        offensiveModules.add(rocketLauncher);
        offensiveModules.add(laserCannon);
    }

    @Override
    public void draw() {

        // Set heading.
        double xVector = guiHandler.mouseX - positionX;
        double yVector = guiHandler.mouseY - positionY;
        heading = NumberCruncher.calculateAngle(xVector, yVector);

        // Draw main body.
        guiHandler.strokeWeight(0);
        guiHandler.stroke(bodyRGBA[0], bodyRGBA[1], bodyRGBA[2]);
        guiHandler.fill(bodyRGBA[0], bodyRGBA[1], bodyRGBA[2]);
        guiHandler.ellipse((float) this.getPositionX(), (float) this.getPositionY(), (float) hitBoxRadius * 2, (float) hitBoxRadius * 2);

        // Draw turret.
        guiHandler.strokeWeight(turretWidth);
        guiHandler.stroke(turretRGBA[0], turretRGBA[1], turretRGBA[2]);
        guiHandler.fill(turretRGBA[0], turretRGBA[1], turretRGBA[2]);
        guiHandler.line((float) this.getPositionX(), (float) this.getPositionY(),
                (float) this.getPositionX() + (float) (turretLength * Math.cos(heading)),
                (float) this.getPositionY() + (float) (turretLength * Math.sin(heading)));
    }

    /**
     * Activates the selected offensive ship module.
     */
    public void activateOffensiveModule() {
        currentOffensiveModule.activate();
    }

    /**
     * Activates the selected defensive ship module.
     */
    public void activateDefensiveModule() {
        currentDefensiveModule.activate();
    }

    /**
     * Changes to the next offensive module.
     */
    public void swapPrimaryModule() {
        // Wait for timer for each swap.
        if (offensiveModuleTimer.timePassed() >= offensiveModuleSwapSpeed) {
            currentOffensiveModule = offensiveModules.get((offensiveModules.indexOf(currentOffensiveModule) + 1) % offensiveModules.size());
            offensiveModuleTimer.restart();
        }
    }

    // Getters.
    public RocketLauncher getRocketLauncher() {
        return rocketLauncher;
    }

    public int getBackgroundColor() {
        return backgroundColor;
    }

}
