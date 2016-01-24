package backend.shipmodule;

import backend.actor.Actor;
import userinterface.Drawable;

/**
 * A laser. Fires a constant laser beam at the target.
 *
 * @author Kristian Honningsvag.
 */
public class LaserCannon extends ShipModule implements Drawable {

    /**
     * Constructor.
     */
    public LaserCannon(Actor owner) {
        super("Laser Cannon", owner);

        projectileDamage = 1;
    }

    @Override
    public void draw() {
        // If the laser is being fired.
        if (this.moduleActive) {

            double screenWidth = owner.getGameEngine().getGuiHandler().getWidth();
            double screenHeight = owner.getGameEngine().getGuiHandler().getHeight();
            double screenDiagonalLength = Math.sqrt(Math.pow(screenWidth, 2) + Math.pow(screenHeight, 2));

            owner.getGameEngine().getGuiHandler().strokeWeight(2);
            owner.getGameEngine().getGuiHandler().stroke(255, 0, 0);
            owner.getGameEngine().getGuiHandler().line((float) owner.getPositionX(), (float) owner.getPositionY(),
                    (float) owner.getPositionX() + (float) (screenDiagonalLength * Math.cos(owner.getHeading())),
                    (float) owner.getPositionY() + (float) (screenDiagonalLength * Math.sin(owner.getHeading())));
            this.setModuleActive(false);
        }
    }

    /**
     * Fires the laser from the actor towards the current heading.
     */
    @Override
    public void activate() {
        this.setModuleActive(true);
    }

}
