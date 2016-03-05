package backend.level;

import backend.actor.Boss;
import backend.actor.Enemy;
import backend.actor.KamikazeDrone;
import backend.actor.Player;
import backend.actor.Slayer;
import backend.actor.SlayerAI;
import backend.main.GameEngine;
import backend.main.Vector;
import java.lang.reflect.InvocationTargetException;
import java.util.Random;
import java.util.logging.Logger;
import jdk.nashorn.internal.objects.annotations.Constructor;

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
     * Spawns a given number of frigates at random locations.
     *
     * @param amount Number of frigates to spawn.
     */
    public void spawnFrigate(int amount) {
        Random random = new Random();
        for (int i = 0; i < amount; i++) {
            Enemy enemy = new Slayer(new Vector(randX(random), randY(random), 0), getGameEngine());
            enemy.setAI(new SlayerAI(enemy, getPlayer()));
            addToActorsAndEnemies(enemy);
        }
    }

    /**
     * Spawns a specified enemy as many times specified
     *
     * @param enemy the enemy that will be spawned
     * @param amount the amount of enemies to be spawned
     */
    public void spawn(Enemy enemy, int amount) {
        for (int i = 0; i < amount; i++) {
            checkEnemyClass(enemy);
        }
    }

    private void checkEnemyClass(Enemy enemy) {
        Random random = new Random();
        if (classCheck(enemy, Boss.class)) {
            enemy = new Boss(randomVector(random), getGameEngine());
            enemy.setAI(new SlayerAI(enemy, getPlayer()));
        } else if (classCheck(enemy, Slayer.class)) {
            enemy = new Slayer(randomVector(random), getGameEngine());
            enemy.setAI(new SlayerAI(enemy, getPlayer()));
        }
        addToActorsAndEnemies(enemy);
    }

    private boolean classCheck(Enemy enemy, Class enemyClass) {
        return enemy.getClass().isAssignableFrom(enemyClass);
    }

    private Player getPlayer() {
        return currentLevel.getPlayer();
    }

    private GameEngine getGameEngine() {
        return currentLevel.getGameEngine();
    }

    private void addToActorsAndEnemies(Enemy enemy) {
        currentLevel.getGameEngine().getCurrentLevel().getEnemies().add(enemy);
        currentLevel.getGameEngine().getCurrentLevel().getActors().add(enemy);
    }

    private Vector randomVector(Random rand) {
        return new Vector(randX(rand), randY(rand), 0);
    }

    private int randX(Random rand) {
        return rand.nextInt(currentLevel.getGameEngine().getGuiHandler().getWidth() - 200) + 100;
    }

    private int randY(Random rand) {
        return rand.nextInt(currentLevel.getGameEngine().getGuiHandler().getHeight() - 200) + 100;
    }

}
