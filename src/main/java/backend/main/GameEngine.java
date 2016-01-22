package backend.main;

import backend.actor.Actor;
import backend.actor.Bullet;
import backend.actor.FireballCanon;
import backend.actor.Frigate;
import backend.actor.Player;
import backend.level.Level;
import backend.level.LevelTest;
import userinterface.GUIHandler;

import java.awt.event.KeyEvent;
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

    private String simulationState = "startScreen";

    // Key states.
    private boolean up = false;
    private boolean down = false;
    private boolean left = false;
    private boolean right = false;
    private boolean firePrimary = false;
    private boolean fireSecondary = false;
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
        currentLevel = new LevelTest(this);
    }

    /**
     * The main loop of the simulation.
     */
    public void run(double timePassed) {

        switch (simulationState) {

            case "startScreen": {
                checkUserInput(timePassed);
                break;
            }

            case "gameplay": {
                checkUserInput(timePassed);
                actAll(timePassed);
                break;
            }

            case "menuScreen": {
                checkUserInput(timePassed);
                break;
            }

            case "deathScreen": {
                checkUserInput(timePassed);
                actAll(timePassed);
                break;
            }

        }
    }

    /**
     * Remove dead actors and make all remaining actors act.
     */
    private void actAll(double timePassed) {

        // Remove dead actors.
        Iterator<Actor> it = currentLevel.getActors().iterator();
        while (it.hasNext()) {

            Actor actorInList = it.next();
            if (actorInList.getHitPoints() <= 0) {

                if ((actorInList instanceof Player)) {
                    simulationState = "deathScreen";
                }
                if ((actorInList instanceof Frigate)) {
                    currentLevel.getEnemies().remove(actorInList);
                    it.remove();
                }
                if ((actorInList instanceof Bullet)) {
                    currentLevel.getProjectiles().remove(actorInList);
                    it.remove();
                }
                if ((actorInList instanceof FireballCanon.Fireball)) {
                    currentLevel.getProjectiles().remove(actorInList);
                    it.remove();
                }
            }
        }

        // Make all actors act.
        for (Actor actor : currentLevel.getActors()) {
            actor.act(timePassed);
        }
    }

    /**
     * Handles user input.
     *
     * @param keyCode The button that was pressed.
     * @param keyState Whether the button was pressed or released.
     */
    public void userInput(int keyCode, boolean keyState) {

        // Accelerate upwards.
        if (keyCode == KeyEvent.VK_E) {
            up = keyState;
        }
        // Accelerate downwards.
        if (keyCode == KeyEvent.VK_D) {
            down = keyState;
        }
        // Accelerate left.
        if (keyCode == KeyEvent.VK_S) {
            left = keyState;
        }
        // Accelerate right.
        if (keyCode == KeyEvent.VK_F) {
            right = keyState;
        }
        // Fire primary weapon.
        if (keyCode == 37) {
            firePrimary = keyState;
        }
        // Fire secondary weapon.
        if (keyCode == 39) {
            fireSecondary = keyState;
        }
        // Swap primary weapon.
        if (keyCode == KeyEvent.VK_W) {
            swapPrimary = keyState;
        }
        // Swap secondary weapon.
        if (keyCode == KeyEvent.VK_R) {
            swapSecondary = keyState;
        }
        // Use auxiliary.
        if (keyCode == KeyEvent.VK_SPACE) {
            space = keyState;
        }
        // Open menu / pause game.
        if (keyCode == KeyEvent.VK_TAB) {
            tab = keyState;
        }
        // Confirm.
        if (keyCode == KeyEvent.VK_ENTER) {
            enter = keyState;
        }
    }

    /**
     * Checks the user inputs and acts accordingly.
     */
    private void checkUserInput(double timePassed) {

        switch (simulationState) {

            case "startScreen": {
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
                if (firePrimary) {
                    currentLevel.getPlayer().firePrimary();
                }
                if (fireSecondary) {
                    currentLevel.getPlayer().fireSecondary();
                }
                if (space) {
                    simulationState = "menuScreen";
                }
                if (tab) {
                    currentLevel.getActorSpawner().spawnFrigate();

                }
                break;
            }

            case "menuScreen": {
                if (enter) {
                    simulationState = "gameplay";
                }
                break;
            }

            case "deathScreen": {
                if (space) {
                    currentLevel = new LevelTest(this);
                    simulationState = "startScreen";
                }
                break;
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

    // Setters.
    public void setSimulationState(String simulationState) {
        this.simulationState = simulationState;
    }

}
