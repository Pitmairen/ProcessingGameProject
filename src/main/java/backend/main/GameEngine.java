package backend.main;

import backend.actor.Actor;
import backend.actor.Enemy;
import backend.actor.Rocket;
import backend.actor.Frigate;
import backend.actor.Player;
import backend.item.Item;
import backend.level.Level;
import backend.level.LevelTest;
import backend.shipmodule.RocketLauncher;
import backend.shipmodule.ShipModule;
import userinterface.GUIHandler;

import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.Iterator;

/**
 * Handles the simulation.
 *
 * @author Kristian Honningsvag.
 */
public class GameEngine {

    private GUIHandler guiHandler;
    private CollisionDetector collisionDetector;
    private Level currentLevel;
    private ExplosionManager explosionManager;
    private FadingCanvas fadingCanvas;
    private String simulationState = "menuScreen";

    // Key states.
    private boolean up = false;
    private boolean down = false;
    private boolean left = false;
    private boolean right = false;
    private boolean activatePrimary = false;
    private boolean activateSecondary = false;
    private boolean swapPrimary = false;
    private boolean swapSecondary = false;
    private boolean space = false;
    private boolean tab = false;
    private boolean enter = false;

    /**
     * Constructor.
     *
     * @param guiHandler
     */
    public GameEngine(GUIHandler guiHandler) {

        this.guiHandler = guiHandler;
        collisionDetector = new CollisionDetector(this);

        explosionManager = new ExplosionManager(new ParticleEmitter(guiHandler));

        resetLevel();
    }

    /**
     * The main loop of the simulation.
     */
    public void run(double timePassed) {

        switch (simulationState) {

            case "menuScreen": {
                checkUserInput(timePassed);
                break;
            }

            case "gameplay": {
                checkUserInput(timePassed);
                cleanup(timePassed);
                actAll(timePassed);
                break;
            }

            case "pauseScreen": {
                checkUserInput(timePassed);
                break;
            }

            case "deathScreen": {
                checkUserInput(timePassed);
                cleanup(timePassed);
                actAll(timePassed);
                break;
            }
        }
    }

    /**
     * Remove dead actors.
     */
    private void cleanup(double timePassed) {

        this.explosionManager.update(timePassed);

        // Remove dead actors.
        ArrayList<Actor> deadActors = new ArrayList<Actor>();
        Iterator<Actor> it = currentLevel.getActors().iterator();

        while (it.hasNext()) {
            Actor actorInList = it.next();
            if (actorInList.getHitPoints() <= 0) {
                actorInList.die();
                // it.remove();
                deadActors.add(actorInList);
            }
        }
        currentLevel.getActors().removeAll(deadActors);
    }

    /**
     * Make all actors act.
     */
    private void actAll(double timePassed) {

        // Make all actors act.
        for (Actor actor : currentLevel.getActors()) {
            actor.act(timePassed);
        }

        // Spawn new wave if current wave have been defeated.
        if (currentLevel.getEnemies().isEmpty()) {
            currentLevel.nextWave();
        }
    }

    /**
     * Handles user input.
     *
     * @param keyCode The button that was pressed.
     * @param keyState Whether the button was pressed or released.
     */
    public void userInput(int keyCode, boolean keyState) {

        if (keyCode == KeyEvent.VK_E) {
            up = keyState;
        }
        if (keyCode == KeyEvent.VK_D) {
            down = keyState;
        }
        if (keyCode == KeyEvent.VK_S) {
            left = keyState;
        }
        if (keyCode == KeyEvent.VK_F) {
            right = keyState;
        }
        if (keyCode == 37) {
            activatePrimary = keyState;
        }
        if (keyCode == 39) {
            activateSecondary = keyState;
        }
        if (keyCode == KeyEvent.VK_W) {
            swapPrimary = keyState;
        }
        if (keyCode == KeyEvent.VK_R) {
            swapSecondary = keyState;
        }
        if (keyCode == KeyEvent.VK_SPACE) {
            space = keyState;
        }
        if (keyCode == KeyEvent.VK_TAB) {
            tab = keyState;
        }
        if (keyCode == KeyEvent.VK_ENTER) {
            enter = keyState;
        }
    }

    /**
     * Checks the user inputs and acts accordingly.
     */
    private void checkUserInput(double timePassed) {

        switch (simulationState) {

            case "menuScreen": {
                if (enter) {
                    simulationState = "gameplay";
                }
                break;
            }

            case "gameplay": {
                if (up) {
                    currentLevel.getPlayer().accelerate("up", timePassed);
                }
                if (down) {
                    currentLevel.getPlayer().accelerate("down", timePassed);
                }
                if (left) {
                    currentLevel.getPlayer().accelerate("left", timePassed);
                }
                if (right) {
                    currentLevel.getPlayer().accelerate("right", timePassed);
                }
                if (activatePrimary) {
                    currentLevel.getPlayer().activateOffensiveModule();
                }
                if (activateSecondary) {
                }
                if (swapPrimary) {
                    currentLevel.getPlayer().swapOffensiveModule();
                }
                if (space) {
                    simulationState = "pauseScreen";
                }
                if (tab) {
                    currentLevel.getActorSpawner().spawnFrigate(1);
                }
                break;
            }

            case "pauseScreen": {
                if (enter) {
                    simulationState = "gameplay";
                }
                break;
            }

            case "deathScreen": {
                if (space) {
                    resetLevel();
                    simulationState = "menuScreen";
                }
                break;
            }

        }
    }

    /**
     * Creates the currentLevel.
     */
    private void resetLevel() {
        currentLevel = new LevelTest(this);
        // The fading canvas must be reset because a new player object
        // is created and thus there fireball cannon is also new and 
        // must be added to the fading canvas.
        fadingCanvas = new FadingCanvas(guiHandler);
        fadingCanvas.add(explosionManager);

        for (ShipModule module : currentLevel.getPlayer().getOffensiveModules()) {
            if (module instanceof RocketLauncher) {
                fadingCanvas.add((RocketLauncher) module);
            }
        }
    }

    // Getters.
    public GUIHandler getGuiHandler() {
        return guiHandler;
    }

    public String getSimulationState() {
        return simulationState;
    }

    public Level getCurrentLevel() {
        return currentLevel;
    }

    public CollisionDetector getCollisionDetector() {
        return collisionDetector;
    }

    public FadingCanvas getFadingCanvas() {
        return fadingCanvas;
    }

    public ExplosionManager getExplosionManager() {
        return explosionManager;
    }

    // Setters.
    public void setSimulationState(String simulationState) {
        this.simulationState = simulationState;
    }

}
