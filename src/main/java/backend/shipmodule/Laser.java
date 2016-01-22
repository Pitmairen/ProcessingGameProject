package backend.shipmodule;

import backend.actor.Actor;
import userinterface.Drawable;

/**
 * A laser. Fires a constant laser beam at the target.
 *
 * @author Kristian Honningsvag.
 */
public class Laser extends ShipModule implements Drawable {

    private double timeBetweenShots = 0;
    private int damage = 1;
    private Actor actor;

    /**
     * Constructor.
     *
     * @param actor The actor who owns this weapon.
     */
    public Laser(Actor actor) {
        this.actor = actor;
    }

    @Override
    public void draw() {
        // If the laser is being fired.
        if (actor.isSecondaryWeaponState()) {

            double screenDiagonalLength = Math.sqrt(Math.pow(actor.getGameEngine().getGuiHandler().getWidth(), 2) + Math.pow(actor.getGameEngine().getGuiHandler().getHeight(), 2));
            double targetAngle = actor.getHeading();

            actor.getGameEngine().getGuiHandler().strokeWeight(2);
            actor.getGameEngine().getGuiHandler().stroke(255, 0, 0);
            actor.getGameEngine().getGuiHandler().line((float) actor.getPositionX(), (float) actor.getPositionY(),
                    (float) actor.getPositionX() + (float) (screenDiagonalLength * Math.cos(targetAngle)),
                    (float) actor.getPositionY() + (float) (screenDiagonalLength * Math.sin(targetAngle)));
            actor.setSecondaryWeaponState(false);
        }
    }

    /**
     * Fires the laser from the actor towards the current heading.
     */
    public void activate() {
        actor.setSecondaryWeaponState(true);
    }

}
