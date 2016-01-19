package backend;

import backend.actor.Actor;
import backend.level.Level;
import backend.level.LevelTest;
import userinterface.GUIHandler;

import java.awt.event.KeyEvent;

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
        currentLevel = new LevelTest(this, guiHandler);
    }

    /**
     * The main loop.
     */
    public void run(double timePassed) {

        switch (simulationState) {

            case "startScreen": {
                if (enter) {
                    simulationState = "gameplay";
                }
                break;
            }

            case "gameplay": {
                checkUserInput(timePassed);
                actAll(timePassed);
                collisionDetector.checkAll(timePassed);
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
                    currentLevel = new LevelTest(this, guiHandler);
                    simulationState = "startScreen";
                }
                actAll(timePassed);
                collisionDetector.checkAll(timePassed);
                break;
            }

        }
    }

    /**
     * Makes all actors act.
     */
    private void actAll(double timePassed) {
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
     * Checks the user inputs.
     */
    private void checkUserInput(double timePassed) {
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
            currentLevel.getPlayer().fireBullet();
        }
        if (fireSecondary) {
            currentLevel.getPlayer().fireLaser(fireSecondary);
        }
        if (space) {
            simulationState = "menuScreen";
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

    // Setters.
    public void setSimulationState(String simulationState) {
        this.simulationState = simulationState;
    }

}
