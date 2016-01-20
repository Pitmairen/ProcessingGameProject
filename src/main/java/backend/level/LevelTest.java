package backend.level;

import backend.GameEngine;
import backend.actor.Actor;
import backend.actor.Frigate;
import backend.actor.Player;

/**
 * A level for testing purposes.
 *
 * @author Kristian Honningsvag.
 */
public class LevelTest extends Level {

    /**
     * Constructor.
     */
    public LevelTest(GameEngine gameEngine) {

        super(gameEngine);

        levelName = "Test level";

        player = new Player(300, 250, gameEngine);
        actors.add(player);

        Actor enemy = new Frigate(1100, 600, gameEngine);
        enemies.add(enemy);
        actors.add(enemy);
    }

}
