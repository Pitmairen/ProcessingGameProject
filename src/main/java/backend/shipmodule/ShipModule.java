package backend.shipmodule;

import backend.actor.Actor;
import processing.core.PImage;
import userinterface.Drawable;

/**
 * Super class for all ship modules.
 *
 * @author Kristian Honningsvag.
 */
public abstract class ShipModule implements Drawable {

    protected String name;  // From constructor.
    protected Actor owner;  // From constructor.
    protected double launchVelocity = 0;
    protected double projectileDamage = 0;
    protected boolean moduleActive = false;

    private PImage image;

    /**
     * Constructor.
     *
     * @param name Name of the module.
     * @param owner The actor that has the module attached.
     */
    public ShipModule(String name, Actor owner) {
        this.name = name;
        this.owner = owner;
    }

    /**
     * Activates the ship module.
     */
    public abstract void activate();

    /**
     * Sets the flag which indicates whether the module is currently active or
     * not.
     *
     * @param mode If the module is currently active or not.
     */
    public void setModuleActive(boolean mode) {
        this.moduleActive = mode;
    }

    /**
     * Method for setting the graphics for the module.
     *
     * @param image File path to the image that should be loaded.
     */
    public void setImage(String image) {
        this.image = owner.getGuiHandler().loadImage(image);
    }

    // Getters.
    public String getName() {
        return name;
    }

    public Actor getOwner() {
        return owner;
    }

    public double getLaunchVelocity() {
        return launchVelocity;
    }

    public double getProjectileDamage() {
        return projectileDamage;
    }

    public boolean isModuleActive() {
        return moduleActive;
    }

    public PImage getImage() {
        return image;
    }

    // Setters.
    public void setOwner(Actor owner) {
        this.owner = owner;
    }

}
