package backend.level;

import backend.main.GameEngine;
import backend.actor.Player;
import backend.main.FadingCanvasItemManager;
import backend.main.RocketManager;
import backend.main.Vector;

/**
 * The first level of the game.
 *
 * @author Kristian Honningsvag.
 */
public class Level1 extends Level {

    /**
     * Constructor.
     */
    public Level1(GameEngine gameEngine, RocketManager rocketManager, FadingCanvasItemManager itemManager) {

        super(gameEngine);

        this.rocketManager = rocketManager;
        this.fadingCanvasItems = itemManager;
        levelName = "1";
        baseTimeToNextWave = 4000;

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
                        actorSpawner.spawnFrigate(1);
                        setTimeToNextWave(10000);
                        break;
                    }

                    case 2: {
                        actorSpawner.spawnFrigate(3);
                        setTimeToNextWave(10000);
                        break;
                    }

                    case 40: {
                        onLastWave = true;
                        break;
                    }
                }
            }
        }
    }

}
