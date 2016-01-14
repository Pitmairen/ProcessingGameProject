package backend;

import userinterface.Drawable;
import userinterface.GUIHandler;

/**
 * Super class for all actors.
 *
 * @author Kristian Honningsvag.
 */
public abstract class InteractableEntity implements Drawable {

    // Position.
    protected double positionX;     // pixels
    protected double positionY;     // pixels

    protected GameEngine gameEngine;
    protected GUIHandler guiHandler;

    /**
     * Constructor.
     *
     * @param positionX Actors X-position in pixels.
     * @param positionY Actors Y-position in pixels.
     * @param gameEngine
     * @param guiHandler
     */
    protected InteractableEntity(double positionX, double positionY, GameEngine gameEngine, GUIHandler guiHandler) {

        // Common for all interactable entitys
        this.positionX = positionX;
        this.positionY = positionY;
        this.gameEngine = gameEngine;
        this.guiHandler = guiHandler;

    }

    @Override
    public abstract void draw();

}
