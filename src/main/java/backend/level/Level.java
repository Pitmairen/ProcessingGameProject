package backend.level;

import backend.main.GameEngine;
import backend.actor.Actor;
import backend.actor.Enemy;
import backend.actor.Player;
import backend.actor.Projectile;
import backend.item.Item;
import backend.main.Timer;
import java.util.ArrayList;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * Super class for all levels in the game.
 *
 * @author Kristian Honningsvag.
 */
public abstract class Level {

    protected String levelName = "NAME NOT SET";
    protected int currentWave = 0;
    protected double timeToNextWave = 0;
    protected Timer timer = new Timer();
    protected GameEngine gameEngine;     // From constructor parameter.
    protected ActorSpawner actorSpawner; // From constructor parameter.

    protected Player player;             // Set in eah levels constructor.
    protected CopyOnWriteArrayList<Actor> actors = new CopyOnWriteArrayList<Actor>();
    protected ArrayList<Enemy> enemies = new ArrayList<Enemy>();
    protected ArrayList<Item> items = new ArrayList<Item>();
    protected ArrayList<Projectile> projectiles = new ArrayList<Projectile>();

    /**
     * Constructor.
     *
     * @param gameEngine The game engine.
     */
    protected Level(GameEngine gameEngine) {

        this.gameEngine = gameEngine;
        this.actorSpawner = new ActorSpawner(this);
    }

    /**
     * Spawns the next wave.
     */
    public abstract void nextWave();

    // Getters.
    public String getLevelName() {
        return levelName;
    }

    public GameEngine getGameEngine() {
        return gameEngine;
    }

    public Player getPlayer() {
        return player;
    }

    public ArrayList<Enemy> getEnemies() {
        return enemies;
    }

    public ArrayList<Item> getItems() {
        return items;
    }

    public ArrayList<Projectile> getProjectiles() {
        return projectiles;
    }

    public CopyOnWriteArrayList<Actor> getActors() {
        return actors;
    }

    public ActorSpawner getActorSpawner() {
        return actorSpawner;
    }

    public int getCurrentWave() {
        return currentWave;
    }

}
