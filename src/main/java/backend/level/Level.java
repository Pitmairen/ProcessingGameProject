package backend.level;

import backend.main.GameEngine;
import backend.actor.Actor;
import backend.actor.enemy.Enemy;
import backend.actor.Player;
import backend.actor.projectile.Projectile;
import backend.actor.Item;
import backend.main.FadingCanvasItemManager;
import backend.main.RocketManager;
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
    protected int previousWave = 0;
    protected double baseTimeToNextWave = 0;
    protected double timeToNextWave = 0;
    protected Timer timer = new Timer();
    protected boolean onLastWave = false;
    protected boolean currentWaveIsAutoSkippable = false;
    protected GameEngine gameEngine;     // From constructor parameter.
    protected ActorSpawner actorSpawner; // From constructor parameter.

    protected Player player;             // Set in eah levels constructor.
    protected CopyOnWriteArrayList<Actor> actors = new CopyOnWriteArrayList<Actor>();
    protected ArrayList<Enemy> enemies = new ArrayList<Enemy>();
    protected ArrayList<Item> items = new ArrayList<Item>();
    protected ArrayList<Projectile> projectiles = new ArrayList<Projectile>();

    protected RocketManager rocketManager;
    protected FadingCanvasItemManager fadingCanvasItems;

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

    // Set the time until the next wave spawns.
    protected void setTimeToNextWave(int timeToNextWave) {
        baseTimeToNextWave = timeToNextWave;
        timer.reset();
    }

    /**
     * Prematurely spawn the next wave.
     */
    public void forceNextWave() {
        if (timeToNextWave > 400) {
            setTimeToNextWave(400);
        }
    }

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

    public double getInitialTimeToNextWave() {
        return baseTimeToNextWave;
    }

    public Timer getTimer() {
        return timer;
    }

    public boolean isOnLastWave() {
        return onLastWave;
    }

    public double getTimeToNextWave() {
        return timeToNextWave;
    }

    public int getPreviousWave() {
        return previousWave;
    }

    public double getBaseTimeToNextWave() {
        return baseTimeToNextWave;
    }

    public RocketManager getRocketManager() {
        return rocketManager;
    }

    public FadingCanvasItemManager getFadingCanvasItems() {
        return fadingCanvasItems;
    }

}
