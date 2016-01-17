package backend.level;

import backend.GameEngine;
import backend.actor.Actor;
import backend.actor.FireballCanon;
import backend.actor.Frigate;
import backend.actor.Player;
import userinterface.GUIHandler;

/**
 * A level for testing purposes.
 *
 * @author Kristian Honningsvag.
 */
public class LevelTest extends Level {

    /**
     * Constructor.
     */
    public LevelTest(GameEngine gameEngine, GUIHandler guiHandler) {

        super(gameEngine, guiHandler);

        levelName = "Test level";
        
        FireballCanon canon = new FireballCanon(gameEngine, guiHandler);
        allEntities.add(canon);
        
        player = new Player(300, 250, gameEngine, guiHandler, canon);
        allEntities.add(player);

        Actor enemy = new Frigate(1100, 600, gameEngine, guiHandler);
        enemies.add(enemy);
        allEntities.add(enemy);
        

    }

}
