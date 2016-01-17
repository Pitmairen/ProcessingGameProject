package backend;

import backend.actor.Actor;
import backend.level.Level;
import backend.level.LevelTest;
import userinterface.GUIHandler;

import java.awt.event.KeyEvent;
import java.util.logging.Logger;

/**
 * Handles the simulation.
 *
 * @author Kristian Honningsvag.
 */
public class GameEngine implements Runnable {

    private GUIHandler guiHandler;
    private CollisionDetector collisionDetector;
    private Level currentLevel;
    private Timer timer;

    private Thread thread;
    private final String THREAD_NAME;
    private volatile String simulationState = "startScreen";
    private boolean applicationRunning = false;
    private int simulationSpeed = 1;           // Nr. of milliseconds between runs.

    // Key states.
    private volatile boolean up = false;
    private volatile boolean down = false;
    private volatile boolean left = false;
    private volatile boolean right = false;
    private volatile boolean firePrimary = false;
    private volatile boolean fireSecondary = false;
    private volatile boolean swapPrimary = false;
    private volatile boolean swapSecondary = false;
    private volatile boolean space = false;
    private volatile boolean tab = false;
    private volatile boolean enter = false;

    /**
     * Constructor.
     *
     * @param guiHandler
     */
    public GameEngine(GUIHandler guiHandler, String threadName) {

        this.guiHandler = guiHandler;
        this.THREAD_NAME = threadName;
        
        applicationRunning = true;        
        collisionDetector = new CollisionDetector(this);
        thread = new Thread(this, THREAD_NAME);
        thread.start();
    }

    /**
     * The main loop.
     */
    @Override
    public void run() {

        timer = new Timer();
        currentLevel = new LevelTest(this, guiHandler);

        while (applicationRunning) {

            switch (simulationState) {

                case "startScreen": {
                    if (enter) {
                        simulationState = "gameplay";
                    }
                    break;
                }

                case "gameplay": {
                    // Wait for timer for each round.
                    if (timer.timePassed() >= simulationSpeed) {
                        checkUserInput();
                        actAll();
                        collisionDetector.checkAll();
                        timer.restart();
                        currentLevel.getPlayer().fireLaser(fireSecondary);
                        break;
                    }
                }

                case "menuScreen": {
                    if (enter) {
                        simulationState = "gameplay";
                    }
                    break;
                }

                case "deathScreen": {
                    if (timer.timePassed() >= simulationSpeed) {
                        if (space) {
                            currentLevel = new LevelTest(this, guiHandler);
                            simulationState = "startScreen";
                        }
                        actAll();
                        collisionDetector.checkAll();
                        timer.restart();
                    }
                    break;
                }
             
            }
            try {
                Thread.sleep(1);
            } catch (InterruptedException ex) {
                Logger.getLogger(GameEngine.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    /**
     * Makes all actors act.
     */
    private void actAll() {
        // Make actors act.
        for (Actor actor : currentLevel.getActors()) {
            actor.act();
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
    private void checkUserInput() {
        if (up) {
            currentLevel.getPlayer().accelerate("up");
        }
        if (down) {
            currentLevel.getPlayer().accelerate("down");
        }
        if (left) {
            currentLevel.getPlayer().accelerate("left");
        }
        if (right) {
            currentLevel.getPlayer().accelerate("right");
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
