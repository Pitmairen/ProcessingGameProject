package backend.level;

import backend.actor.Actor;
import backend.actor.ModuleContainer;
import backend.actor.Player;
import backend.actor.enemy.Enemy;
import backend.actor.ai.DroneAI;
import backend.actor.enemy.Frigate;
import backend.actor.ai.SlayerAI;
import backend.actor.enemy.DroneCarrier;
import backend.actor.enemy.KamikazeDrone;
import backend.main.Vector;
import backend.shipmodule.AutoCannon;
import backend.shipmodule.EMPCannon;
import backend.shipmodule.LaserCannon;
import backend.shipmodule.RocketLauncher;
import backend.shipmodule.SeekerCannon;
import java.util.Random;

/**
 * Handles spawning actors.
 *
 * @author Kristian Honningsvag.
 */
public class ActorSpawner {

    Level currentLevel;
    private Enemy enemy;
    private ModuleContainer moduleContainer;

    /**
     * Constructor.
     *
     * @param currentLevel The current level.
     */
    public ActorSpawner(Level currentLevel) {
        this.currentLevel = currentLevel;
    }

    /**
     * Spawns a given number of Kamikaze drones at random locations.
     *
     * @param amount Number of Kamikaze drones to spawn.
     */
    public void spawnKamikazeDrone(int amount) {
        Random random = new Random();
        for (int i = 0; i < amount; i++) {
            enemy = new KamikazeDrone(new Vector(randX(random), randY(random), 0), currentLevel.getGameEngine());
            enemy.setAI(new DroneAI(enemy.getGameEngine(), currentLevel.getPlayer(), enemy));
            currentLevel.getGameEngine().getCurrentLevel().getEnemies().add(enemy);
            currentLevel.getGameEngine().getCurrentLevel().getActors().add(enemy);
            checkForCollisionWithPlayer(enemy);
        }
    }

    /**
     * Spawns a given number of Frigates at random locations.
     *
     * @param amount Number of Frigates to spawn.
     */
    public void spawnFrigate(int amount) {
        Random random = new Random();
        for (int i = 0; i < amount; i++) {
            enemy = new Frigate(new Vector(randX(random), randY(random), 0), currentLevel.getGameEngine());
            enemy.setAI(new SlayerAI(enemy, currentLevel.getPlayer()));
            currentLevel.getGameEngine().getCurrentLevel().getEnemies().add(enemy);
            currentLevel.getGameEngine().getCurrentLevel().getActors().add(enemy);
            checkForCollisionWithPlayer(enemy);
        }
    }

    /**
     * Spawns a given number of Carriers at random locations.
     *
     * @param amount Number of frigates to spawn.
     */
    public void spawnCarrier(int amount) {
        Random random = new Random();
        for (int i = 0; i < amount; i++) {
            enemy = new DroneCarrier(new Vector(randX(random), randY(random), 0), currentLevel.getGameEngine());
            enemy.setAI(new SlayerAI(enemy, currentLevel.getPlayer()));
            currentLevel.getGameEngine().getCurrentLevel().getEnemies().add(enemy);
            currentLevel.getGameEngine().getCurrentLevel().getActors().add(enemy);
            checkForCollisionWithPlayer(enemy);
        }
    }

    /**
     * Spawns a carrier at the given location.
     *
     * @param x Initial x-position.
     * @param y Initial y-position.
     */
    public void spawnCarrier(int x, int y) {
        enemy = new DroneCarrier(new Vector(x, y, 0), currentLevel.getGameEngine());
        enemy.setAI(new SlayerAI(enemy, currentLevel.getPlayer()));
        currentLevel.getGameEngine().getCurrentLevel().getEnemies().add(enemy);
        currentLevel.getGameEngine().getCurrentLevel().getActors().add(enemy);
    }

    /**
     * Spawns an Auto cannon at a random location.
     */
    public void spawnAutoCannon() {
        Random random = new Random();
        moduleContainer = new ModuleContainer(new Vector(randX(random), randY(random), 0), currentLevel.getGameEngine());
        moduleContainer.setModule(new AutoCannon(moduleContainer));
        currentLevel.getGameEngine().getCurrentLevel().getItems().add(moduleContainer);
        currentLevel.getGameEngine().getCurrentLevel().getActors().add(moduleContainer);
        checkForCollisionWithPlayer(moduleContainer);
    }

    /**
     * Spawns an auto cannon at the given location.
     *
     * @param x Initial x-position.
     * @param y Initial y-position.
     */
    public void spawnAutoCannon(int x, int y) {
        moduleContainer = new ModuleContainer(new Vector(x, y, 0), currentLevel.getGameEngine());
        moduleContainer.setModule(new AutoCannon(moduleContainer));
        currentLevel.getGameEngine().getCurrentLevel().getItems().add(moduleContainer);
        currentLevel.getGameEngine().getCurrentLevel().getActors().add(moduleContainer);
    }

    /**
     * Spawns a Rocket launcher at a random location.
     */
    public void spawnRocketLauncher() {
        Random random = new Random();
        moduleContainer = new ModuleContainer(new Vector(randX(random), randY(random), 0), currentLevel.getGameEngine());
        moduleContainer.setModule(new RocketLauncher(moduleContainer, currentLevel.getRocketManager()));
        currentLevel.getGameEngine().getCurrentLevel().getItems().add(moduleContainer);
        currentLevel.getGameEngine().getCurrentLevel().getActors().add(moduleContainer);
        checkForCollisionWithPlayer(moduleContainer);
    }

    /**
     * Spawns a rocket launcher at the given location.
     *
     * @param x Initial x-position.
     * @param y Initial y-position.
     */
    public void spawnRocketLauncher(int x, int y) {
        moduleContainer = new ModuleContainer(new Vector(x, y, 0), currentLevel.getGameEngine());
        moduleContainer.setModule(new RocketLauncher(moduleContainer, currentLevel.getRocketManager()));
        currentLevel.getGameEngine().getCurrentLevel().getItems().add(moduleContainer);
        currentLevel.getGameEngine().getCurrentLevel().getActors().add(moduleContainer);
    }

    /**
     * Spawns a Seeker launcher at a random location.
     */
    public void spawnSeekerLauncher() {
        Random random = new Random();
        moduleContainer = new ModuleContainer(new Vector(randX(random), randY(random), 0), currentLevel.getGameEngine());
        moduleContainer.setModule(new SeekerCannon(moduleContainer, currentLevel.getFadingCanvasItems()));
        currentLevel.getGameEngine().getCurrentLevel().getItems().add(moduleContainer);
        currentLevel.getGameEngine().getCurrentLevel().getActors().add(moduleContainer);
        checkForCollisionWithPlayer(moduleContainer);
    }

    /**
     * Spawns a seeker launcher at the given location.
     *
     * @param x Initial x-position.
     * @param y Initial y-position.
     */
    public void spawnSeekerLauncher(int x, int y) {
        moduleContainer = new ModuleContainer(new Vector(x, y, 0), currentLevel.getGameEngine());
        moduleContainer.setModule(new SeekerCannon(moduleContainer, currentLevel.getFadingCanvasItems()));
        currentLevel.getGameEngine().getCurrentLevel().getItems().add(moduleContainer);
        currentLevel.getGameEngine().getCurrentLevel().getActors().add(moduleContainer);
    }

    /**
     * Spawns a Laser cannon at a random location.
     */
    public void spawnLaser() {
        Random random = new Random();
        moduleContainer = new ModuleContainer(new Vector(randX(random), randY(random), 0), currentLevel.getGameEngine());
        moduleContainer.setModule(new LaserCannon(moduleContainer));
        currentLevel.getGameEngine().getCurrentLevel().getItems().add(moduleContainer);
        currentLevel.getGameEngine().getCurrentLevel().getActors().add(moduleContainer);
        checkForCollisionWithPlayer(moduleContainer);
    }

    /**
     * Spawns a laser cannon at the given location.
     *
     * @param x Initial x-position.
     * @param y Initial y-position.
     */
    public void spawnLaser(int x, int y) {
        moduleContainer = new ModuleContainer(new Vector(x, y, 0), currentLevel.getGameEngine());
        moduleContainer.setModule(new LaserCannon(moduleContainer));
        currentLevel.getGameEngine().getCurrentLevel().getItems().add(moduleContainer);
        currentLevel.getGameEngine().getCurrentLevel().getActors().add(moduleContainer);
    }

    /**
     * Spawns an EMP at a random location.
     */
    public void spawnEMP() {
        Random random = new Random();
        moduleContainer = new ModuleContainer(new Vector(randX(random), randY(random), 0), currentLevel.getGameEngine());
        moduleContainer.setModule(new EMPCannon(moduleContainer, currentLevel.getFadingCanvasItems()));
        currentLevel.getGameEngine().getCurrentLevel().getItems().add(moduleContainer);
        currentLevel.getGameEngine().getCurrentLevel().getActors().add(moduleContainer);
        checkForCollisionWithPlayer(moduleContainer);
    }

    /**
     * Spawns a EMP at the given location.
     *
     * @param x Initial x-position.
     * @param y Initial y-position.
     */
    public void spawnEMP(int x, int y) {
        moduleContainer = new ModuleContainer(new Vector(x, y, 0), currentLevel.getGameEngine());
        moduleContainer.setModule(new EMPCannon(moduleContainer, currentLevel.getFadingCanvasItems()));
        currentLevel.getGameEngine().getCurrentLevel().getItems().add(moduleContainer);
        currentLevel.getGameEngine().getCurrentLevel().getActors().add(moduleContainer);
    }

    /**
     * Checks whether the new actor spawned on top of the player. If it did,
     * then teleport the new actor to a random location.
     *
     * @param actor The actor to check.
     */
    private void checkForCollisionWithPlayer(Actor actor) {
        for (Actor actorInList : currentLevel.getGameEngine()
                .getCollisionDetector().detectActorCollision(enemy)) {
            if (actorInList instanceof Player) {
                actor.teleport();
            }
        }
    }

    // Return a random x-position.
    private int randX(Random rand) {
        return rand.nextInt(currentLevel.getGameEngine().getGuiHandler().getWidth() - 160) + 80;
    }

    // Return a random y-position.
    private int randY(Random rand) {
        return rand.nextInt(currentLevel.getGameEngine().getGuiHandler().getHeight() - 160) + 80;
    }

}
