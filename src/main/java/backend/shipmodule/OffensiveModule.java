package backend.shipmodule;

import backend.actor.Actor;
import processing.core.PImage;

/**
 * Super class for all offensive ship modules.
 *
 * @author Kristian Honningsvag.
 */
public abstract class OffensiveModule extends ShipModule {

    private PImage weapon;
    
    public OffensiveModule(String name, Actor owner) {
        super(name, owner);
    }

    @Override
    public void activate() {
    }

    @Override
    public void draw() {
        
    }
    
    /**
     * Method for setting the offensive graphics for a enemy
     * @param image 
     */
    public void setOffensiveModule(String image){
        weapon = owner.getGuiHandler().loadImage(image);
    }
    
    public PImage getWeapon(){
        return weapon;
    }

}
