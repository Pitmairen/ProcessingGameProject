package backend.level;

import backend.main.GameEngine;
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
    }

    @Override
    public void nextWave() {

        currentWave++;

        switch (currentWave) {
            case 1: {
                actorSpawner.spawnFrigate(1);
                break;
            }
            case 2: {
                actorSpawner.spawnFrigate(3);
                break;
            }
            case 3: {
                actorSpawner.spawnFrigate(5);
                break;
            }
            case 4: {
                actorSpawner.spawnFrigate(9);
                break;
            }
            case 5: {
                actorSpawner.spawnFrigate(17);
                break;
            }
            case 6: {
                actorSpawner.spawnFrigate(25);
                break;
            }
            case 7: {
                actorSpawner.spawnFrigate(160);
                break;
            }
        }
    }

}
