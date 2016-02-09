package backend.shipmodule;

import backend.actor.Actor;
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

    // Setter.
    public void setModuleActive(boolean moduleActive) {
        this.moduleActive = moduleActive;
    }

}
