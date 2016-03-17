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
        baseTimeToNextWave = 120000;

        player = new Player(new Vector(390, 370, 0), gameEngine);
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
                        actorSpawner.spawnSeekerLauncher(200, 500);
                        actorSpawner.spawnLaser(200, 600);
                        actorSpawner.spawnEMP(200, 700);
                        setTimeToNextWave(120000);
                        break;
                    }

                    case 2: {
                        onLastWave = true;
                        break;
                    }
                }
            }
        }
    }

}
