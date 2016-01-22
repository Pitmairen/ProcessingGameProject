package backend.level;

import backend.main.GameEngine;
import backend.actor.Actor;
import backend.actor.FireballCanon;
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

        FireballCanon canon = new FireballCanon(gameEngine);
        actors.add(canon);

        player = new Player(300, 250, gameEngine, canon);

        actors.add(player);

        Actor enemy = new Frigate(1100, 600, gameEngine);
        enemies.add(enemy);
        actors.add(enemy);

    }

}
