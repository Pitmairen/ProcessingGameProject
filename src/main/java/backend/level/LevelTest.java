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
import backend.shipmodule.SeekerCannon;

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
        timeToNextWave = 0;  // Time to the first wave spawns.

        player = new Player(new Vector(300, 250, 0), gameEngine);
        actors.add(player);
    }

    @Override
    public void nextWave() {

        if (timer.timePassed() >= timeToNextWave) {

            currentWave++;

            switch (currentWave) {
                case 1: {
                    Item modulePickup = new ModuleContainer(new Vector(200, 400, 0), gameEngine, new RocketLauncher(player, rocketManager));
                    items.add(modulePickup);
                    actors.add(modulePickup);

                    modulePickup = new ModuleContainer(new Vector(200, 500, 0), gameEngine, new SeekerCannon(player, fadingCanvasItems));
                    items.add(modulePickup);
                    actors.add(modulePickup);

                    modulePickup = new ModuleContainer(new Vector(200, 600, 0), gameEngine, new LaserCannon(player));
                    items.add(modulePickup);
                    actors.add(modulePickup);

                    modulePickup = new ModuleContainer(new Vector(200, 700, 0), gameEngine, new EMPCannon(player, fadingCanvasItems));
                    items.add(modulePickup);
                    actors.add(modulePickup);

                    actorSpawner.spawnFrigate(1);

                    timeToNextWave = 5000;
                    timer.restart();
                    break;
                }
                case 2: {

                    actorSpawner.spawnFrigate(3);
                    timeToNextWave = 5000;
                    timer.restart();
                    break;
                }
                case 3: {

                    actorSpawner.spawnFrigate(9);
                    timeToNextWave = 5000;
                    timer.restart();
                    break;
                }
                case 4: {
                    actorSpawner.spawnFrigate(9);
                    timeToNextWave = 5000;
                    timer.restart();
                    break;
                }
                case 5: {
                    actorSpawner.spawnFrigate(25);
                    timeToNextWave = 5000;
                    timer.restart();
                    break;
                }
                case 6: {
                    actorSpawner.spawnFrigate(25);
                    timeToNextWave = 5000;
                    timer.restart();
                    break;
                }
                case 7: {
                    actorSpawner.spawnFrigate(50);
                    timeToNextWave = 5000;
                    timer.restart();
                    break;
                }
            }
        }
    }

}
