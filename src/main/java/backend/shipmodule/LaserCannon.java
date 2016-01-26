package backend.shipmodule;

import backend.actor.Actor;
import userinterface.Drawable;

/**
 * A laser. Fires a constant laser beam at the target.
 *
 * @author Kristian Honningsvag.
 */
public class LaserCannon extends ShipModule implements Drawable {

    // Shape and color.
    private int turretLength = 27;
    private int turretWidth = 4;
    private int[] turretRGBA = new int[]{200, 30, 30, 255};

    /**
     * Constructor.
     */
    public LaserCannon(Actor owner) {
        super("Laser Cannon", owner);

        projectileDamage = 1;
    }

    @Override
    public void draw() {

        // Draw the cannon.
        owner.getGuiHandler().strokeWeight(turretWidth);
        owner.getGuiHandler().stroke(turretRGBA[0], turretRGBA[1], turretRGBA[2]);
        owner.getGuiHandler().fill(turretRGBA[0], turretRGBA[1], turretRGBA[2]);
        owner.getGuiHandler().line((float) owner.getPositionX(), (float) owner.getPositionY(),
                (float) owner.getPositionX() + (float) (turretLength * Math.cos(owner.getHeading())),
                (float) owner.getPositionY() + (float) (turretLength * Math.sin(owner.getHeading())));

        // Draw the laser beam.
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
