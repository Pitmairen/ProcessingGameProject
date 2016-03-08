package backend.item;

import backend.actor.Actor;
import backend.main.GameEngine;
import backend.main.Vector;
import backend.shipmodule.DefensiveModule;
import backend.shipmodule.OffensiveModule;
import backend.shipmodule.ShipModule;
import backend.shipmodule.TacticalModule;
import userinterface.Drawable;

/**
 * Container for a power up or ship module that the player can pick up and
 * equip.
 *
 * @author Kristian Honningsvag.
 */
public class ModuleContainer extends Item implements Drawable {

    // Color.
    private int[] bodyRGBA = new int[]{200, 200, 20, 55};

    private ShipModule shipModule = null;

    /**
     * Constructor.
     */
    public ModuleContainer(Vector position, GameEngine gameEngine) {

        super(position, gameEngine);
        
        hitBoxRadius = 20;
        pullDistance = gameEngine.getCurrentLevel().getPlayer().getHitBoxRadius() * 5;
    }
    
    @Override
    public void draw() {
        // Draw container.
        guiHandler.strokeWeight(1);
        guiHandler.stroke(bodyRGBA[0], bodyRGBA[1], bodyRGBA[2], bodyRGBA[3]);
        guiHandler.fill(bodyRGBA[0], bodyRGBA[1], bodyRGBA[2], bodyRGBA[3]);
        guiHandler.ellipse((float) this.getPosition().getX(), (float) this.getPosition().getY(), (float) hitBoxRadius * 2, (float) hitBoxRadius * 2);
        guiHandler.noFill();
        // Draw cargo.
        if (shipModule != null) {
            shipModule.draw();
        }
    }
    
    @Override
    public void pickup(Actor looter) {
        if (currentHitPoints > 0) {  // Only add a module if this container is alive.
            shipModule.setOwner(looter);
            if (shipModule instanceof OffensiveModule) {
                looter.getOffensiveModules().add(shipModule);
            }
            if (shipModule instanceof DefensiveModule) {
                looter.getDefensiveModules().add(shipModule);
            }
            if (shipModule instanceof TacticalModule) {
                looter.setTacticalModule(shipModule);
            }
            currentHitPoints = 0;
        }
    }

    @Override
    public void die() {
        gameEngine.getCurrentLevel().getItems().remove(this);
    }

    /**
     * Adds a ship module to this container. Overwrites the previous module if
     * one was present when a new one was added.
     *
     * @param shipModule To be added.
     */
    public void setModule(ShipModule shipModule) {
        this.shipModule = shipModule;
    }

}
