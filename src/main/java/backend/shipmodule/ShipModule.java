package backend.shipmodule;

import backend.actor.Actor;
import backend.resources.Image;
import processing.core.PApplet;
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
    
    protected float defaultModuleWidth = 46.29f;
    protected float defaultModuleHeight = 23.92f;
    
    protected PImage moduleImage;

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
        this.moduleImage = owner.getGuiHandler().loadImage(image);
    }

    /**
     * Draws the module on the player 
     * 
     * @param moduleImage 
     * @param posX int X-Position from the center of the player
     * @param posY int Y-Position from the center of the player
     * @param width float Width of the module
     * @param height float Width of the module
     */
    public void drawModule(PImage moduleImage, int posX, int posY, float width, float height) {
        PApplet gui = owner.getGuiHandler();
        gui.tint(255);
        gui.pushMatrix();
        gui.translate((float) owner.getPosition().getX(), (float) owner.getPosition().getY());
        gui.rotate((float) (owner.getHeading().getAngle2D()));
        gui.imageMode(PImage.CENTER);
        gui.image(moduleImage, posX, posY, width, height);
        gui.imageMode(PImage.CORNER);
        gui.popMatrix();
    }
    

    public PImage getImageFromResourceManager(Image img){
        return owner.getGameEngine().getResourceManager().getImage(img);
    }

    /**
     * Should when the module is no longer the currently loaded module. 
     * 
     * The module can do any cleanup that is needed when it is unloaded in 
     * this method.
     */
    public void deactivated(){
        
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
        return moduleImage;
    }

    // Setters.
    public void setOwner(Actor owner) {
        this.owner = owner;
    }

    
}
