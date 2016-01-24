package backend.actor;

import backend.shipmodule.RocketLauncher;
import backend.main.GameEngine;
import backend.main.NumberCruncher;
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
    private int turretLength = 23;
    private int turretWidth = 3;

    // Color.
    private int[] bodyRGBA = new int[]{0, 70, 200, 255};
    private int[] turretRGBA = new int[]{30, 30, 200, 255};

    // Weapons.
    private AutoCannon autoCannon = new AutoCannon(this);
    private LaserCannon laserCannon = new LaserCannon(this);
    private RocketLauncher rocketLauncher = new RocketLauncher(this);

    private ShipModule selectedPrimaryModule = autoCannon;
    private ShipModule selectedSecondaryModule = rocketLauncher;

    /**
     * Constructor.
     */
    public Player(double positionX, double positionY, GameEngine gameEngine) {

        super(positionX, positionY, gameEngine);

        speedLimit = 0.6f;
        accelerationX = 0.002f;
        accelerationY = 0.002f;
        drag = 0.001f;
        hitBoxRadius = 20;
        bounceModifier = 0.6f;
        hitPoints = 30;
        mass = 100;

        collisionDamageToOthers = 4;

        shipModules.add(autoCannon);
        shipModules.add(rocketLauncher);
        //  shipModules.add(laser);
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
     * Activates the selected primary ship module.
     */
    public void activatePrimaryModule() {
        selectedPrimaryModule.activate();
    }

    /**
     * Activates the selected secondary ship module.
     */
    public void activateSecondaryModule() {
        selectedSecondaryModule.activate();
    }

    public RocketLauncher getRocketLauncher() {
        return rocketLauncher;
    }

}
