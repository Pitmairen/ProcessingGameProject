package backend.level;

import backend.actor.enemy.Boss;
import backend.actor.enemy.Enemy;
import backend.main.GameEngine;
import backend.actor.Player;
import backend.actor.enemy.Slayer;
import backend.main.FadingCanvasItemManager;
import backend.main.RocketManager;
import backend.main.Vector;

/**
 * First official game level. 
 * The waves increment when all of the enemies from the previous wave are defeated.
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
     * @param gameEngine 
     * @param rocketManager
     * @param itemManager
     */
    public Level1(GameEngine gameEngine, RocketManager rocketManager, FadingCanvasItemManager itemManager) {
        super(gameEngine);
        
        levelName = "Level 1";
        player = new Player(new Vector(300, 250, 0), gameEngine);
        actors.add(player);
        this.moduleSpawner = new ModuleSpawner(gameEngine, rocketManager, itemManager);
    }

    @Override
    public void nextWave() {

        if (!onLastWave) {
            
            int enemiesLeft = gameEngine.getCurrentLevel().getEnemies().size();
            if (enemiesLeft == 0) {

                currentWave++;

                switch (currentWave) {
                    case 1: {
                        spawnRandomModule();
                        spawn(slayer, 10);
                        break;
                    }
                    case 2: {
                        spawn(slayer, 15);
                        break;
                    }
                    case 3: {
                        spawnRandomModule();
                        spawn(boss,1);
                        spawn(slayer, 5);
                        break;
                    }
                    case 4: {
                        spawn(boss,3);
                        break;
                    }
                    case 5: {
                        spawnRandomModule();
                        spawn(slayer, 30);
                        break;
                    }
                    case 6: {
                        spawn(boss, 2);
                        spawn(slayer, 25);
                        break;
                    }
                    case 7: {
                        spawnRandomModule();
                        spawn(boss,2);
                        spawn(slayer, 30);
                        onLastWave = true;
                        break;
                    }
                }
            }
        }
    }
    
    /**
     * 
     * @param enemy Decides what enemy type should be spawned.
     * @param amount Decides how many of the specified enemy to spawn.
     */
    private void spawn(Enemy enemy, int amount){
        actorSpawner.spawn(enemy, amount);
    }
    
    /**
     * Spawns a random module somewhere on screen.
     */
    private void spawnRandomModule(){
        moduleSpawner.spawnRandomModule();
    }
}
