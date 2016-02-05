package backend.shipmodule;

import backend.actor.Actor;
import backend.actor.EMPPulse;
import backend.main.FadingCanvasItemManager;
import backend.main.Timer;
import backend.main.Vector;

/**
 * Fires EMP Pulses
 * 
 * @author pitmairen
 */
public class EMPCannon extends ShipModule {

    // Shape and color.
    private int turretLength = 27;
    private int turretWidth = 4;
    private int[] turretRGBA = new int[]{30, 30, 200, 255};

    private double timeBetweenShots = 3000;
    private Timer timer = new Timer();
    private FadingCanvasItemManager fadingCanvasItems;

    /**
     * Constructor.
     */
    public EMPCannon(Actor owner, FadingCanvasItemManager itemManager) {
        super("EMP Cannon", owner);

        this.fadingCanvasItems = itemManager;
        projectileDamage = 1000;
    }

    @Override
    public void draw() {
        owner.getGuiHandler().strokeWeight(turretWidth);
        owner.getGuiHandler().stroke(turretRGBA[0], turretRGBA[1], turretRGBA[2]);
        owner.getGuiHandler().fill(turretRGBA[0], turretRGBA[1], turretRGBA[2]);
        owner.getGuiHandler().line((float) owner.getPosition().getX(), (float) owner.getPosition().getY(),
                (float) owner.getPosition().getX() + (float) (turretLength * Math.cos(owner.getHeading().getAngle2D())),
                (float) owner.getPosition().getY() + (float) (turretLength * Math.sin(owner.getHeading().getAngle2D())));
    }

    /**
     * Fires a new EMP pulse.
     */
    @Override
    public void activate() {

        if (timer.timePassed() >= timeBetweenShots) {   // Check fire rate.
            EMPPulse pulse = new EMPPulse(owner.getPosition().copy(), this);

            owner.getGameEngine().getCurrentLevel().getProjectiles().add(pulse);
            owner.getGameEngine().getCurrentLevel().getActors().add(pulse);
            fadingCanvasItems.add(pulse);
            
            timer.restart();
        }
    }

}
