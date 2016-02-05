package backend.level;

import backend.main.GameEngine;
import backend.actor.Player;
import backend.item.Item;
import backend.item.ModuleContainer;
import backend.main.FadingCanvasItemManager;
import backend.main.RocketManager;
import backend.main.Vector;
import backend.shipmodule.EMPCannon;
import backend.shipmodule.LaserCannon;
import backend.shipmodule.RocketLauncher;

/**
 * A level for testing purposes.
 *
 * @author Kristian Honningsvag.
 */
public class LevelTest extends Level {

    private RocketManager rocketManager;
    private FadingCanvasItemManager fadingCanvasItems;
    /**
     * Constructor.
     */
    public LevelTest(GameEngine gameEngine, RocketManager rocketManager, FadingCanvasItemManager itemManager) {

        super(gameEngine);

        this.rocketManager = rocketManager;
        this.fadingCanvasItems = itemManager;
        levelName = "Test level";

        player = new Player(new Vector(300, 250, 0), gameEngine);
        actors.add(player);
    }

    @Override
    public void nextWave() {

        currentWave++;

        switch (currentWave) {
            case 1: {
                Item modulePickup = new ModuleContainer(new Vector(200, 300, 0), gameEngine, new RocketLauncher(player, rocketManager));
                items.add(modulePickup);
                actors.add(modulePickup);
                actorSpawner.spawnFrigate(1);
                break;
            }
            case 2: {
                Item modulePickup = new ModuleContainer(new Vector(300, 300, 0), gameEngine, new EMPCannon(player, fadingCanvasItems));
                items.add(modulePickup);
                actors.add(modulePickup);
                actorSpawner.spawnFrigate(3);
                break;
            }
            case 3: {
                Item modulePickup = new ModuleContainer(new Vector(400, 300, 0), gameEngine, new LaserCannon(player));
                items.add(modulePickup);
                actors.add(modulePickup);
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
