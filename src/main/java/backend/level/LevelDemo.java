package backend.level;

import backend.actor.enemy.Boss;
import backend.actor.enemy.Enemy;
import backend.main.GameEngine;
import backend.actor.Player;
import backend.actor.ai.SlayerAI;
import backend.actor.ModuleContainer;
import backend.main.FadingCanvasItemManager;
import backend.main.RocketManager;
import backend.main.Vector;
import backend.shipmodule.EMPCannon;
import backend.shipmodule.LaserCannon;
import backend.shipmodule.RocketLauncher;
import backend.shipmodule.SeekerCannon;

/**
 * A custom level created specifically for the project presentation.
 *
 * @author Kristian Honningsvag.
 */
public class LevelDemo extends Level {

    private RocketManager rocketManager;
    private FadingCanvasItemManager fadingCanvasItems;

    /**
     * Constructor.
     */
    public LevelDemo(GameEngine gameEngine, RocketManager rocketManager, FadingCanvasItemManager itemManager) {

        super(gameEngine);

        this.rocketManager = rocketManager;
        this.fadingCanvasItems = itemManager;
        levelName = "Test level";
        initialTimeToNextWave = 0;  // Time to the first wave spawns.

        player = new Player(new Vector(300, 250, 0), gameEngine);
        actors.add(player);
    }

    @Override
    public void nextWave() {

        if (!onLastWave) {

            timeToNextWave = initialTimeToNextWave - timer.timePassed();

            if (timeToNextWave <= 0) {

                currentWave++;

                switch (currentWave) {

                    case 1: {

                        initialTimeToNextWave = 50000;
                        timer.restart();
                        break;
                    }
                    case 2: {

                        actorSpawner.spawnFrigate(1);
                        initialTimeToNextWave = 5000;
                        timer.restart();
                        break;
                    }
                    case 3: {
                        actorSpawner.spawnFrigate(1);
                        onLastWave = true;
                        initialTimeToNextWave = 0;
                        timeToNextWave = 0;
                        timer.restart();
                        break;
                    }
                }
            }
        }
    }

}
