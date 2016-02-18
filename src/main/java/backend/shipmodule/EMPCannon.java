package backend.shipmodule;

import backend.actor.Actor;
import backend.actor.EMPPulse;
import backend.main.FadingCanvasItemManager;
import backend.main.Timer;
import userinterface.GUIHandler;

/**
 * Fires EMP Pulses
 *
 * @author pitmairen
 */
public class EMPCannon extends TacticalModule {

    // Shape and color.
    private int weaponLength = 8;
    private int weaponWidth = 4;
    private int[] weaponRGBA = new int[]{10, 10, 140, 255};

    private double timeBetweenShots = 3000;
    private Timer timer = new Timer();
    private FadingCanvasItemManager fadingCanvasItems;

    /**
     * Constructor.
     */
    public EMPCannon(Actor owner, FadingCanvasItemManager itemManager) {
        super("EMP Cannon", owner);

        this.fadingCanvasItems = itemManager;
        projectileDamage = 6;
    }

    @Override
    public void draw() {
        GUIHandler gui = owner.getGuiHandler();
        gui.pushMatrix();
        gui.tint(0xffff0000);
        gui.translate((float) owner.getPosition().getX(), (float) owner.getPosition().getY());
        gui.rotate((float) owner.getHeading().getAngle2D());

        owner.getGuiHandler().strokeWeight(weaponWidth);
        owner.getGuiHandler().stroke(weaponRGBA[0], weaponRGBA[1], weaponRGBA[2]);
        owner.getGuiHandler().fill(weaponRGBA[0], weaponRGBA[1], weaponRGBA[2]);
        owner.getGuiHandler().rect((float) -owner.getHitBoxRadius() / 2, -weaponLength / 2, weaponWidth, weaponLength, 2);
        gui.popMatrix();
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
