package backend.level;

import backend.actor.Actor;
import backend.actor.Frigate;
import java.util.Random;

/**
 * Handles spawning enemies.
 *
 * @author Kristian Honningsvag.
 */
public class ActorSpawner {

    Level currentLevel;

    /**
     * Constructor.
     *
     * @param currentLevel The current level.
     */
    public ActorSpawner(Level currentLevel) {
        this.currentLevel = currentLevel;
    }

    /**
     * Spawns a frigate at a fixed position.
     */
    public void spawnFrigate() {

        Random random = new Random();
        int randX = random.nextInt(currentLevel.getGameEngine().getGuiHandler().getWidth() - 200) + 100;
        int randY = random.nextInt(currentLevel.getGameEngine().getGuiHandler().getHeight() - 200) + 100;

        Actor enemy = new Frigate(randX, randY, currentLevel.getGameEngine());
        currentLevel.getGameEngine().getCurrentLevel().getEnemies().add(enemy);
        currentLevel.getGameEngine().getCurrentLevel().getActors().add(enemy);
    }

}
