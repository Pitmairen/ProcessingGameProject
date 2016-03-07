package backend.actor.projectile;

import backend.main.FadingCanvasItemManager;
import backend.main.Vector;
import backend.resources.Image;
import backend.shipmodule.ShipModule;
import processing.core.PGraphics;
import processing.core.PImage;


/**
 * The seeker missile locks on to a target and hits it with almost certainty.
 * 
 * @author pitmairen
 */
public class SeekerMissile extends Projectile implements FadingCanvasItemManager.Item {

    /**
     * Defines the target that the missile should lock on to
     */
    public interface Target{
        /**
         * Should return the current position of the target
         * @return 
         */
        public Vector getPosition();
        
        /**
         * Should return true as long as the target is alive
         * 
         * @return true if alive
         */
        public boolean isAlive();
    }
    
    
    private final Target target;
    private final PImage bg;
    
    
    public SeekerMissile(Vector position, Target target, ShipModule shipModule) {
        super(position, shipModule);
        this.target = target;
        
        hitBoxRadius = 5;
        currentHitPoints = 1;
        mass = 2;
        frictionCoefficient = 0.01;
        collisionDamageToOthers = shipModule.getProjectileDamage();
        
        bg =  gameEngine.getResourceManager().getImage(Image.SEEKER_MISSILE);
        
    }
    
    
    @Override
    public void act(double timePassed) {
        seekTarget();
        super.act(timePassed);
    }
    
    @Override
    public void draw() {
        // Draws to the fading canvas
    }
    
    @Override
    protected void checkWallCollisions(double timePassed) {
        // The seeker can go outside the walls
    }
   
    
    
    @Override
    public void draw(PGraphics canvas) {

        canvas.tint(0xffff0000);
        canvas.pushMatrix();
        canvas.translate((float)getPosition().getX(), (float) getPosition().getY());
        canvas.rotate(guiHandler.millis()/20.0f); 
        canvas.imageMode(PImage.CENTER);
        canvas.image(bg, 0, 0, 20, 50);
        canvas.popMatrix();
        canvas.imageMode(PImage.CORNER);

    }
    
    // Used by fading canvas item manage
    @Override
    public boolean isAlive() {
        return getCurrentHitPoints() > 0;
    }
    
    @Override
    public void die() {
        setCurrentHitPoints(0);
        gameEngine.getCurrentLevel().getProjectiles().remove(this);
    }
    
    
    /**
     * Make the missile go towards the target.
     * 
     */
    private void seekTarget(){
        
        if(!target.isAlive()){
            die();
            return;
        }
        Vector force = Vector.sub(target.getPosition(), getPosition());
        force.normalize().mult(0.01);
        applyForce(force);
    }
    
}
