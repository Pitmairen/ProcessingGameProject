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
                        actorSpawner.spawnFrigate(3);
                        actorSpawner.spawnCarrier(0);
                        setTimeToNextWave(10000);
                        currentWaveIsAutoSkippable = true;
                        break;
                    }
                    case 4: {
                        actorSpawner.spawnKamikazeDrone(6);
                        actorSpawner.spawnFrigate(4);
                        actorSpawner.spawnCarrier(0);
                        setTimeToNextWave(15000);
                        currentWaveIsAutoSkippable = true;
                        break;
                    }
                    case 6: {
                        actorSpawner.spawnSeekerLauncher();
                        setTimeToNextWave(5000);
                        currentWaveIsAutoSkippable = false;
                        break;
                    }
                    case 7: {
                        actorSpawner.spawnKamikazeDrone(0);
                        actorSpawner.spawnFrigate(8);
                        actorSpawner.spawnCarrier(0);
                        setTimeToNextWave(10000);
                        currentWaveIsAutoSkippable = true;
                        break;
                    }
                    case 8: {
                        actorSpawner.spawnKamikazeDrone(0);
                        actorSpawner.spawnFrigate(13);
                        actorSpawner.spawnCarrier(0);
                        setTimeToNextWave(15000);
                        currentWaveIsAutoSkippable = true;
                        break;
                    }
                    case 10: {
                        actorSpawner.spawnLaser();
                        setTimeToNextWave(5000);
                        currentWaveIsAutoSkippable = false;
                        break;
                    }
                    case 11: {
                        actorSpawner.spawnKamikazeDrone(10);
                        actorSpawner.spawnFrigate(0);
                        actorSpawner.spawnCarrier(0);
                        setTimeToNextWave(10000);
                        currentWaveIsAutoSkippable = true;
                        break;
                    }
                    case 12: {
                        actorSpawner.spawnKamikazeDrone(20);
                        actorSpawner.spawnFrigate(0);
                        actorSpawner.spawnCarrier(0);
                        setTimeToNextWave(10000);
                        currentWaveIsAutoSkippable = true;
                        break;
                    }
                    case 13: {
                        actorSpawner.spawnEMP();
                        setTimeToNextWave(5000);
                        currentWaveIsAutoSkippable = false;
                        break;
                    }
                    case 14: {
                        actorSpawner.spawnKamikazeDrone(4);
                        actorSpawner.spawnFrigate(7);
                        actorSpawner.spawnCarrier(0);
                        setTimeToNextWave(10000);
                        currentWaveIsAutoSkippable = true;
                        break;
                    }
                    case 15: {
                        actorSpawner.spawnKamikazeDrone(5);
                        actorSpawner.spawnFrigate(12);
                        actorSpawner.spawnCarrier(0);
                        setTimeToNextWave(10000);
                        currentWaveIsAutoSkippable = true;
                        break;
                    }
                    case 16: {
                        actorSpawner.spawnRocketLauncher();
                        setTimeToNextWave(5000);
                        currentWaveIsAutoSkippable = false;
                        break;
                    }
                    case 17: {
                        actorSpawner.spawnKamikazeDrone(3);
                        actorSpawner.spawnFrigate(5);
                        actorSpawner.spawnCarrier(1);
                        setTimeToNextWave(15000);
                        currentWaveIsAutoSkippable = true;
                        break;
                    }
                    case 18: {
                        actorSpawner.spawnKamikazeDrone(3);
                        actorSpawner.spawnFrigate(5);
                        actorSpawner.spawnCarrier(0);
                        setTimeToNextWave(10000);
                        currentWaveIsAutoSkippable = true;
                        break;
                    }
                    case 19: {
                        actorSpawner.spawnKamikazeDrone(3);
                        actorSpawner.spawnFrigate(5);
                        actorSpawner.spawnCarrier(0);
                        setTimeToNextWave(10000);
                        currentWaveIsAutoSkippable = true;
                        break;
                    }
                    case 20: {
                        actorSpawner.spawnKamikazeDrone(5);
                        actorSpawner.spawnFrigate(7);
                        actorSpawner.spawnCarrier(0);
                        currentWaveIsAutoSkippable = true;
                        onLastWave = true;
                        break;
                    }
                }
            }
        }
    }

}
