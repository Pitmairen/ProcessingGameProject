package backend.actor;

import backend.main.GameEngine;
import backend.main.Vector;
import java.util.Random;
import userinterface.Drawable;

/**
 * Super class for all enemies in the game.
 *
 * @author Kristian Honningsvag.
 */
public abstract class Enemy extends Actor implements Drawable {

    // AI configuration.
    protected Random random = new Random();
    protected float attackDelay = 0;
    protected float attackDelayFactor = random.nextFloat() + 1;
    protected double lastTimeFired = 0;
    protected boolean isHostile = true;
    protected AI ai;

    /**
     * Constructor.
     */
    protected Enemy(Vector position, GameEngine gameEngine) {
        
        super(position, gameEngine);
        this.ai = null;
        
    }
    
    @Override
    public void act(double timePassed) {
        if(ai != null){
            ai.updateBehaviour(timePassed);
        }
        
        super.act(timePassed);
    }

    public void setAI(AI ai){
        this.ai = ai;
    }
}
