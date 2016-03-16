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
        levelName = "Invaded";
        setTimeToNextWave(4000);

        player = new Player(new Vector(300, 250, 0), gameEngine);
        actors.add(player);
//        currentWave = 3;
    }

    @Override
    public void nextWave() {

        if (!onLastWave) {

            timeToNextWave = baseTimeToNextWave - timer.timePassed();

            if (timeToNextWave <= 0 || (enemies.isEmpty() && currentWaveIsAutoSkippable)) {

                currentWave++;

                switch (currentWave) {

                    case 1: {
                        actorSpawner.spawnKamikazeDrone(0);
                        actorSpawner.spawnFrigate(1);
                        actorSpawner.spawnCarrier(0);
                        setTimeToNextWave(10000);
                        currentWaveIsAutoSkippable = true;
                        break;
                    }

                    case 2: {
                        actorSpawner.spawnKamikazeDrone(0);
                        actorSpawner.spawnFrigate(3);
                        actorSpawner.spawnCarrier(0);
                        setTimeToNextWave(10000);
                        currentWaveIsAutoSkippable = true;
                        break;
                    }

                    case 3: {
                        actorSpawner.spawnKamikazeDrone(2);
                        actorSpawner.spawnFrigate(1);
                        actorSpawner.spawnCarrier(0);
                        setTimeToNextWave(10000);
                        currentWaveIsAutoSkippable = true;
                        break;
                    }

                    case 4: {
                        actorSpawner.spawnKamikazeDrone(4);
                        actorSpawner.spawnFrigate(3);
                        actorSpawner.spawnCarrier(0);
                        setTimeToNextWave(15000);
                        currentWaveIsAutoSkippable = true;
                        break;
                    }

                    case 5: {
                        actorSpawner.spawnKamikazeDrone(6);
                        actorSpawner.spawnFrigate(5);
                        actorSpawner.spawnCarrier(0);
                        setTimeToNextWave(20000);
                        currentWaveIsAutoSkippable = true;
                        break;
                    }

                    case 6: {
                        actorSpawner.spawnSeekerLauncher(200, 700);
                        setTimeToNextWave(5000);
                        currentWaveIsAutoSkippable = false;
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
