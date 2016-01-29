package backend.level;

import backend.main.GameEngine;
import backend.actor.Player;
import backend.item.Item;
import backend.item.ModuleContainer;
import backend.main.RocketManager;
import backend.shipmodule.RocketLauncher;

/**
 * A level for testing purposes.
 *
 * @author Kristian Honningsvag.
 */
public class LevelTest extends Level {

    private RocketManager rocketManager;
    
    /**
     * Constructor.
     */
    public LevelTest(GameEngine gameEngine, RocketManager rocketManager) {

        super(gameEngine);

        this.rocketManager = rocketManager;
        
        levelName = "Test level";

        player = new Player(300, 250, gameEngine);
        actors.add(player);
    }

    @Override
    public void nextWave() {

        currentWave++;

        switch (currentWave) {
            case 1: {
                Item modulePickup = new ModuleContainer(200, 300, gameEngine, new RocketLauncher(player, rocketManager));
                items.add(modulePickup);
                actors.add(modulePickup);
                actorSpawner.spawnFrigate(1);
                break;
            }
            case 2: {
                actorSpawner.spawnFrigate(3);
                break;
            }
            case 3: {
                actorSpawner.spawnFrigate(9);
                break;
            }
            case 4: {
                actorSpawner.spawnFrigate(25);
                break;
            }
            case 5: {
                actorSpawner.spawnFrigate(45);
                break;
            }
            case 6: {
                actorSpawner.spawnFrigate(100);
                break;
            }
        }
    }

}
