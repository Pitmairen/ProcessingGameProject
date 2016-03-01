package backend.shipmodule;

import backend.actor.Actor;
import backend.actor.EMPPulse;
import backend.main.FadingCanvasItemManager;
import backend.main.Timer;
import backend.resources.Image;
import processing.core.PApplet;
import processing.core.PImage;
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

    private double timeBetweenShots = 200;
    private Timer timer = new Timer();
    private FadingCanvasItemManager fadingCanvasItems;

    private final PImage empCannonImg;

    /**
     * Constructor.
     */
    public EMPCannon(Actor owner, FadingCanvasItemManager itemManager) {
        super("EMP Cannon", owner);

        this.fadingCanvasItems = itemManager;
        projectileDamage = 2;

        empCannonImg = owner.getGameEngine().getResourceManager().getImage(Image.EMP_CANNON);

    }

    @Override
    public void draw() {
        drawModule(empCannonImg, 0, 15, 20f, 10f);
        drawModule(empCannonImg, 0, -15, 20f, 10f);
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
