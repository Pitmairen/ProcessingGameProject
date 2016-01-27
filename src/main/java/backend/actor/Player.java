package backend.actor;

import backend.main.GameEngine;
import backend.main.NumberCruncher;
import backend.main.Timer;
import backend.shipmodule.AutoCannon;
import backend.shipmodule.LaserCannon;
import backend.shipmodule.ShipModule;
import java.util.ArrayList;
import userinterface.Drawable;

/**
 * The player.
 *
 * @author Kristian Honningsvag.
 */
public class Player extends Actor implements Drawable {

    // Color.
    private int[] bodyRGBA = new int[]{0, 70, 200, 255};
    private final int backgroundColor;  // Set in constructor.

    // Modules.
    private ArrayList<ShipModule> offensiveModules = new ArrayList<>();
    private ArrayList<ShipModule> defensiveModules = new ArrayList<>();
    private Timer offensiveModuleTimer = new Timer();
    private Timer defensiveModuleTimer = new Timer();
    private double offensiveModuleSwapDelay = 800;
    private double defensiveModuleSwapDelay = 800;

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

        offensiveModules.add(new AutoCannon(this));  // Starting weapon.
        offensiveModules.add(new LaserCannon(this));
        currentOffensiveModule = offensiveModules.get(0);
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

        // Draw modules.
        if (currentOffensiveModule != null) {
            currentOffensiveModule.draw();
        }
        if (currentDefensiveModule != null) {
            currentDefensiveModule.draw();
        }
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
    public void swapOffensiveModule() {
        // Wait for timer for each swap.
        if (offensiveModuleTimer.timePassed() >= offensiveModuleSwapDelay) {
            currentOffensiveModule = offensiveModules.get((offensiveModules.indexOf(currentOffensiveModule) + 1) % offensiveModules.size());
            offensiveModuleTimer.restart();
        }
    }

    /**
     * Changes to the next defensive module.
     */
    public void swapDefensiveModule() {
        // Wait for timer for each swap.
        if (defensiveModuleTimer.timePassed() >= defensiveModuleSwapDelay) {
            currentDefensiveModule = defensiveModules.get((defensiveModules.indexOf(currentDefensiveModule) + 1) % defensiveModules.size());
            defensiveModuleTimer.restart();
        }
    }

    public int getBackgroundColor() {
        return backgroundColor;
    }

}
