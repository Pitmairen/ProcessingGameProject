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
        levelName = "Demonstration";
        baseTimeToNextWave = 600000;

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
                        actorSpawner.spawnRocketLauncher(200, 400);
                        setTimeToNextWave(600000);
                        break;
                    }

                    case 2: {
                        actorSpawner.spawnSeekerLauncher(200, 400);
                        setTimeToNextWave(600000);
                        break;
                    }

                    case 3: {
                        actorSpawner.spawnLaser(200, 400);
                        setTimeToNextWave(600000);
                        break;
                    }

                    case 4: {
                        actorSpawner.spawnEMP(200, 400);
                        setTimeToNextWave(600000);
                        break;
                    }

                    case 5: {
                        onLastWave = true;
                        setTimeToNextWave(0);
                        break;
                    }
                }
            }
        }
    }

    /**
     * Effectively skips to the next wave. This is a quick fix only intended to
     * be used for testing purposes.
     */
    @Deprecated
    public void forceNextWave() {
        setTimeToNextWave(500);
    }

}
