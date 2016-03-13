package backend.shipmodule;

import backend.actor.Actor;
import backend.actor.projectile.EMPPulse;
import backend.main.FadingCanvasItemManager;
import backend.main.Timer;
import backend.resources.Image;
import backend.resources.Sound;

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

    private double timeBetweenShots = 2000;
    private Timer timer = new Timer();
    private FadingCanvasItemManager fadingCanvasItems;

    /**
     * Constructor.
     */
    public EMPCannon(Actor owner, FadingCanvasItemManager itemManager) {
        super("EMP Cannon", owner);

        this.fadingCanvasItems = itemManager;
        projectileDamage = 2;

        moduleImage = getImageFromResourceManager(Image.EMP_CANNON);
    }

    @Override
    public void draw() {
        drawModule(moduleImage, 0, 15, defaultModuleWidth / 2, defaultModuleHeight / 2);
        drawModule(moduleImage, 0, -15, defaultModuleWidth / 2, defaultModuleHeight / 2);
    }

    /**
     * Fires a new EMP pulse.
     */
    @Override
    public void activate() {

        // Check if there is enough energy to create a pulse
        if (owner.getCurrentEnergy() < 60) {
            return;
        }

        if (timer.timePassed() >= timeBetweenShots) {   // Check fire rate.

            owner.getGameEngine().getSoundManager().play(Sound.EMP, owner.getPosition());

            EMPPulse pulse = new EMPPulse(owner.getPosition().copy(), this);

            owner.getGameEngine().getCurrentLevel().getProjectiles().add(pulse);
            owner.getGameEngine().getCurrentLevel().getActors().add(pulse);
            fadingCanvasItems.add(pulse);

            timer.restart();

            owner.removeEnergy(60);
        }
    }

}
