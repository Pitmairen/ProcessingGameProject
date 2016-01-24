package backend.level;

import backend.main.GameEngine;
import backend.actor.Actor;
import backend.actor.Player;
import java.util.ArrayList;

/**
 * Super class for all levels in the game.
 *
 * @author Kristian Honningsvag.
 */
public abstract class Level {

    public static java.util.logging.Level SEVERE;

    protected String levelName;
    protected GameEngine gameEngine;
    protected ActorSpawner actorSpawner;

    protected ArrayList<Actor> actors = new ArrayList<Actor>();
    protected ArrayList<Actor> enemies = new ArrayList<Actor>();
    protected ArrayList<Actor> projectiles = new ArrayList<Actor>();
    protected ArrayList<Actor> items = new ArrayList<Actor>();
    protected Player player;

    /**
     * Constructor.
     *
     * @param gameEngine
     */
    protected Level(GameEngine gameEngine) {

        this.gameEngine = gameEngine;
        this.actorSpawner = new ActorSpawner(this);
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

    public ArrayList<Actor> getEnemies() {
        return enemies;
    }

    public ArrayList<Actor> getProjectiles() {
        return projectiles;
    }

    public ArrayList<Actor> getItems() {
        return items;
    }

    public ArrayList<Actor> getActors() {
        return actors;
    }

    public ActorSpawner getActorSpawner() {
        return actorSpawner;
    }

}
