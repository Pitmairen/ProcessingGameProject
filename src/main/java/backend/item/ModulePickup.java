/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package backend.item;

import backend.actor.Actor;
import backend.main.GameEngine;
import backend.shipmodule.RocketLauncher;
import backend.shipmodule.ShipModule;
import userinterface.Drawable;

/**
 * Container for a ship module that the player can pick up and equip.
 *
 * @author Kristian Honningsvag.
 */
public class ModulePickup extends Item implements Drawable {

    // Color.
    private int[] bodyRGBA = new int[]{20, 200, 200, 255};

    /**
     * Constructor.
     */
    public ModulePickup(double positionX, double positionY, GameEngine gameEngine) {

        super(positionX, positionY, gameEngine);

        hitBoxRadius = 30;
    }

    @Override
    public void draw() {
        // Draw main body.
        guiHandler.strokeWeight(0);
        guiHandler.stroke(bodyRGBA[0], bodyRGBA[1], bodyRGBA[2]);
        guiHandler.fill(bodyRGBA[0], bodyRGBA[1], bodyRGBA[2]);
        guiHandler.ellipse((float) this.getPositionX(), (float) this.getPositionY(), (float) hitBoxRadius * 2, (float) hitBoxRadius * 2);
    }

    @Override
    public ShipModule take(Actor looter) {
        hitPoints = 0;
        return new RocketLauncher(looter);
    }

    @Override
    protected void checkActorCollisions(double timePassed) {
        // Do nothing.
    }

}
