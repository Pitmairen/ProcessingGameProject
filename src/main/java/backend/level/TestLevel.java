package backend.level;

import backend.main.GameEngine;
import backend.actor.Player;
import backend.main.FadingCanvasItemManager;
import backend.main.RocketManager;
import backend.main.Vector;

/**
 * A custom level created specifically for testing purposes.
 *
 * @author Kristian Honningsvag.
 */
public class TestLevel extends Level {

    /**
     * Constructor.
     */
    public TestLevel(GameEngine gameEngine, RocketManager rocketManager, FadingCanvasItemManager itemManager) {

        super(gameEngine);

        this.rocketManager = rocketManager;
        this.fadingCanvasItems = itemManager;
        levelName = "TestLevel";
        baseTimeToNextWave = 999999;

        player = new Player(new Vector(300, 250, 0), gameEngine);
        actors.add(player);
    }

    @Override
    public void nextWave() {

        if (!onLastWave) {

            timeToNextWave = baseTimeToNextWave - timer.timePassed();

            if (timeToNextWave <= 0) {

                currentWave++;

                switch (currentWave) {

                    case 1: {
                        actorSpawner.spawnRocketLauncher(250, 400);
                        setTimeToNextWave(999999);
                        break;
                    }

                    case 2: {
                        actorSpawner.spawnSeekerLauncher(250, 400);
                        setTimeToNextWave(999999);
                        break;
                    }

                    case 3: {
                        actorSpawner.spawnLaser(250, 400);
                        setTimeToNextWave(999999);
                        break;
                    }

                    case 4: {
                        actorSpawner.spawnEMP(250, 400);
                        setTimeToNextWave(999999);
                        break;
                    }
                    case 5: {
                        onLastWave = true;
                        break;
                    }
                }
            }
        }
    }

}
