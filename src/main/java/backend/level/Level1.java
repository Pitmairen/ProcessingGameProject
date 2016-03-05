package backend.level;

import backend.actor.Boss;
import backend.actor.Enemy;
import backend.main.GameEngine;
import backend.actor.Player;
import backend.actor.Slayer;
import backend.main.FadingCanvasItemManager;
import backend.main.RocketManager;
import backend.main.Vector;

/**
 * First offical game level.
 *
 * @author dogsh
 */
public class Level1 extends Level {
    private ModuleSpawner moduleSpawner;
    
    //One instanse of each spawnable enemies is needed for the actorSpawner to work properly
    private Boss boss = new Boss(new Vector(0,0,0), gameEngine);
    private Slayer slayer = new Slayer(new Vector(0,0,0), gameEngine);

    /**
     * Constructor.
     */
    public Level1(GameEngine gameEngine, RocketManager rocketManager, FadingCanvasItemManager itemManager) {

        super(gameEngine);
        
        levelName = "Test level";
        initialTimeToNextWave = 0;  // Time to the first wave spawns.

        player = new Player(new Vector(300, 250, 0), gameEngine);
        actors.add(player);
        this.moduleSpawner = new ModuleSpawner(gameEngine, rocketManager, itemManager);
    }

    @Override
    public void nextWave() {

        if (!onLastWave) {

            //timeToNextWave = initialTimeToNextWave - timer.timePassed();
            int enemiesLeft = gameEngine.getCurrentLevel().getEnemies().size();
            if (enemiesLeft == 0) {

                currentWave++;

                switch (currentWave) {
                    case 1: {
                        spawnRandomModule();
                        spawn(slayer, 10);
//                        initialTimeToNextWave = 15000;
//                        timer.restart();
                        break;
                    }
                    case 2: {
                        spawn(slayer, 15);
//                        initialTimeToNextWave = 15000;
//                        timer.restart();
                        break;
                    }
                    case 3: {
                        spawnRandomModule();
                        spawn(boss,1);
                        spawn(slayer, 5);
//                        initialTimeToNextWave = 35000;
//                        timer.restart();
                        break;
                    }
                    case 4: {
                        spawn(boss,3);
//                        initialTimeToNextWave = 35000;
//                        timer.restart();
                        break;
                    }
                    case 5: {
                        spawnRandomModule();
                        spawn(slayer, 30);
//                        initialTimeToNextWave = 35000;
//                        timer.restart();
                        break;
                    }
                    case 6: {
                        spawn(boss, 2);
                        spawn(slayer, 25);
//                        initialTimeToNextWave = 35000;
//                        timer.restart();
                        break;
                    }
                    case 7: {
                        spawnRandomModule();
                        spawn(boss,2);
                        spawn(slayer, 30);
                        onLastWave = true;
//                        initialTimeToNextWave = 0;
//                        timeToNextWave = 0;
//                        timer.restart();
                        break;
                    }
                }
            }
        }
    }
    
    private void spawn(Enemy enemy, int amount){
        actorSpawner.spawn(enemy, amount);
    }
    
    private void spawnRandomModule(){
        moduleSpawner.spawnRandomModule();
    }
    


}
