package backend.actor;

import backend.main.FadingCanvasItemManager;
import backend.main.Vector;
import backend.shipmodule.ShipModule;
import processing.core.PConstants;
import processing.core.PGraphics;

/**
 * A EMP pulse that kills the enemies that are within the pulse radius.
 * 
 * @author pitmairen
 */
public class EMPPulse extends Projectile implements FadingCanvasItemManager.Item {

    // How fast the pulse radius grows
    private final int pulseStepSize = 10;
    private final int pulseRadiusLimit = 350;
    
    public EMPPulse(Vector position, ShipModule shipModule){
        super(position, shipModule);

        name = "EMPPulse";
        
        hitBoxRadius = 0; // The pulse starts at 0
        
        // The pulse doesn't take hits. So we just set the hitpoints to a 
        // arbitrary number above 0
        currentHitPoints = 100; 
        
        mass = 100;
        collisionDamageToOthers = shipModule.getProjectileDamage();
    }

    @Override
    public void act(double timePassed) {
        
        hitBoxRadius += pulseStepSize;
        if(hitBoxRadius > pulseRadiusLimit){
            die();
        }
    }

    @Override
    public void draw() {
        // It is drawn to the fading canvas by the fading canvas item manager
    }

    @Override
    public void die() {
        setCurrentHitPoints(0);
        gameEngine.getCurrentLevel().getProjectiles().remove(this);
    }

    @Override
    public void targetHit() {
        setCurrentHitPoints(100); // We don't take any hits.
    }

    /**
     * Implements the fading canvas item manager interface.
     * 
     * Draws the pulse
     * 
     * @param canvas 
     */
    @Override
    public void draw(PGraphics canvas) {

        canvas.blendMode(PGraphics.ADD);
        canvas.ellipseMode(PConstants.CENTER);
        canvas.stroke(0xff0000ff);
        canvas.fill(0, 0);
        //Gradually fade out the stroke
        // (pulseRadiusLimit/pulseStepSize) = the number of steps that the pulse will grow before it dies
        int fadingStepSize = 255/(pulseRadiusLimit/pulseStepSize);
        int currentStep = (int)hitBoxRadius/pulseStepSize;
        
        canvas.stroke(0xff2D91EF, 255 - fadingStepSize*currentStep);
        canvas.strokeWeight(8);
        canvas.ellipse((float)getPosition().getX(), (float)getPosition().getY(),
                (float)hitBoxRadius*2, (float)hitBoxRadius*2);
        
        canvas.strokeWeight(0);
        canvas.blendMode(PGraphics.BLEND);
    }

    /**
     * Implements the fading canvas item manager interface.
     * 
     * @return 
     */
    @Override
    public boolean isAlive() {
        return getCurrentHitPoints() > 0;
    }
    
}
