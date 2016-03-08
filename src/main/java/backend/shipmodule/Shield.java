package backend.shipmodule;

import backend.actor.Actor;
import backend.main.GameEngine;
import backend.main.Vector;
import backend.resources.Image;
import backend.resources.Shader;
import processing.core.PApplet;
import processing.core.PImage;
import processing.opengl.PShader;

/**
 * A defensive shield
 * 
 * @author pitmairen
 */
public class Shield extends DefensiveModule {
    
    private final PShader shieldShader;
    
    private ShieldActor shield;
    private final PImage shieldNoise;
    private final PImage shieldBG;
    
    
    public Shield(Actor owner) {
        super("Shield", owner);
        
        PApplet gui = owner.getGuiHandler();
        shieldShader = owner.getGameEngine().getResourceManager().getShader(Shader.SHIELD_SHADER); 
        shieldShader.set("resolution", (float)800f, (float)600f);
        shieldNoise = owner.getGameEngine().getResourceManager().getImage(Image.SHIELD_NOISE);
        shieldBG = owner.getGameEngine().getResourceManager().getImage(Image.SHIELD_BACKGROUND);
        
        moduleImage = getImageFromResourceManager(Image.SHIELD_MODULE);
    }
    
    @Override
    public void draw(){
        drawModule(moduleImage, -15, 0, defaultModuleWidth/2, defaultModuleHeight/2);
    }
    
    @Override
    public void activate() {

        // Create the shield if it has not been avtivated before
        if(shield == null){
            shield = new ShieldActor(owner.getPosition().copy(), owner.getGameEngine());
            owner.getGameEngine().getCurrentLevel().getActors().add(shield);
        }
        // Else reset the shield if it has died
        else if(shield.getCurrentHitPoints() <= 0){
            shield.reset();
            if(!owner.getGameEngine().getCurrentLevel().getActors().contains(shield))
                owner.getGameEngine().getCurrentLevel().getActors().add(shield);
        }
    }
    
    
    /**
     * The actor that represents the shield.
     * 
     * It is only public so it can be used to check the instance of 
     * the class in the collisions.
     */
    public class ShieldActor extends Actor {

        
        public ShieldActor(Vector position, GameEngine gameEngine) {
            super(position, gameEngine);
            hitBoxRadius = 80;
            collisionDamageToOthers = 40;
            reset();
        }
        

        /**
         * Returns the owner of the shield
         * 
         * @return the owner 
         */
        public Actor getOwner(){
            return owner;
        }
        
        @Override
        public void act(double timePassed){
            this.getPosition().set(owner.getPosition().copy());
        }
        
        @Override
        public void draw() {
            
            PApplet gui = getGuiHandler();
            Vector pos = getPosition();
            float x = (float)pos.getX();
            float y = (float)pos.getY();
            
            gui.blendMode(PApplet.ADD);
            gui.shader(shieldShader);  
            
            shieldShader.set("position", x, (gui.height - y), 0f, 0f);
            shieldShader.set("time", (float)gui.millis()*0.00004f);

            gui.imageMode(PApplet.CENTER);
            gui.image(shieldNoise, x, y, 165, 165);
   
            gui.resetShader();
            gui.tint(0xff44ff22, 115);
            gui.image(shieldBG, x, y, 165, 165);
            gui.imageMode(PApplet.CORNER);
            gui.blendMode(PApplet.BLEND);
        }

        @Override
        public void die() {
            setCurrentHitPoints(0);
        }
        
        @Override
        public void collision(Actor actor) {
            removeHitPoints(actor.getCollisionDamageToOthers());
        }
 
        /**
         * Reset the hit points so the shield can be used again
         */
        private void reset(){
            currentHitPoints = 40;
        }
    }
}
