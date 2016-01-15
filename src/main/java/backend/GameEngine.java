package backend;

import backend.actor.Actor;
import backend.level.Level;
import backend.level.LevelTest;
import userinterface.GUIHandler;

import java.awt.event.KeyEvent;
import java.util.Iterator;
import java.util.logging.Logger;

/**
 * Handles the simulation.
 *
 * @author Kristian Honningsvag.
 */
public class GameEngine implements Runnable {

    private Level currentLevel;

    private GUIHandler guiHandler;

    private Thread thread;
    private final String THREAD_NAME;

    // Environment variable.
    private volatile String simulationState = "startScreen";
    private int simulationSpeed = 1;  // Milliseconds between rounds.

    private Timer timer;
    private double timeSinceLastCalculation;

    // If the key is currently beeing pressed.
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

        // Main loop.
        while (true) {

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
                        detectWallCollision();
                        checkMissileHit();
                        detectPlayerEnemyCollision();
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
                        detectWallCollision();
                        checkMissileHit();
                        detectPlayerEnemyCollision();
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
     * Checks user input.
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
     * Detects wall collisions.
     */
    private void detectWallCollision() {

        // Projectiles.
        Iterator<Actor> it = currentLevel.getProjectiles().iterator();
        while (it.hasNext()) {

            Actor actor = it.next();

            if (actor.getPositionX() + (actor.getHitBoxRadius()) >= (guiHandler.getWidth() - guiHandler.getOuterWallThickness()) // Right wall.
                    || actor.getPositionY() + (actor.getHitBoxRadius()) >= (guiHandler.getHeight() - guiHandler.getOuterWallThickness()) // Lower wall.
                    || actor.getPositionX() - (actor.getHitBoxRadius()) <= (0 + guiHandler.getOuterWallThickness()) // Left wall.
                    || actor.getPositionY() - (actor.getHitBoxRadius()) <= (0 + guiHandler.getOuterWallThickness())) // Upper wall
            {
                it.remove();
                currentLevel.getActors().remove(actor);
            }
        }

        // Right wall.
        if (currentLevel.getPlayer().getPositionX() + (currentLevel.getPlayer().getHitBoxRadius()) >= (guiHandler.getWidth() - guiHandler.getOuterWallThickness())) {
            currentLevel.getPlayer().wallBounce("right");
        }
        for (Actor actor : currentLevel.getEnemies()) {
            if (actor.getPositionX() + actor.getHitBoxRadius() >= (guiHandler.getWidth() - guiHandler.getOuterWallThickness())) {
                actor.wallBounce("right");
            }
        }
        // Lower wall.
        if (currentLevel.getPlayer().getPositionY() + currentLevel.getPlayer().getHitBoxRadius() >= (guiHandler.getHeight() - guiHandler.getOuterWallThickness())) {
            currentLevel.getPlayer().wallBounce("lower");
        }
        for (Actor actor : currentLevel.getEnemies()) {
            if (actor.getPositionY() + actor.getHitBoxRadius() >= (guiHandler.getHeight() - guiHandler.getOuterWallThickness())) {
                actor.wallBounce("lower");
            }
        }
        // Left wall.
        if (currentLevel.getPlayer().getPositionX() - currentLevel.getPlayer().getHitBoxRadius() <= (0 + guiHandler.getOuterWallThickness())) {
            currentLevel.getPlayer().wallBounce("left");
        }
        for (Actor actor : currentLevel.getEnemies()) {
            if (actor.getPositionX() - actor.getHitBoxRadius() <= (0 + guiHandler.getOuterWallThickness())) {
                actor.wallBounce("left");
            }
        }
        // Upper wall.
        if (currentLevel.getPlayer().getPositionY() - currentLevel.getPlayer().getHitBoxRadius() <= (0 + guiHandler.getOuterWallThickness())) {
            currentLevel.getPlayer().wallBounce("upper");
        }
        for (Actor actor : currentLevel.getEnemies()) {
            if (actor.getPositionY() - actor.getHitBoxRadius() <= (0 + guiHandler.getOuterWallThickness())) {
                actor.wallBounce("upper");
            }
        }
    }

    /**
     * Detects player and enemy collisions.
     */
    private void detectPlayerEnemyCollision() {

        for (Actor actor : currentLevel.getEnemies()) {

            if ((Math.abs(currentLevel.getPlayer().getPositionX() - actor.getPositionX()) < currentLevel.getPlayer().getHitBoxRadius() + actor.getHitBoxRadius())
                    && (Math.abs(currentLevel.getPlayer().getPositionY() - actor.getPositionY()) < currentLevel.getPlayer().getHitBoxRadius() + actor.getHitBoxRadius())) {
                simulationState = "deathScreen";
            }
        }
    }

    /**
     * Counts missile hits on the enemy frigate by the player.
     */
    private void checkMissileHit() {
        Iterator<Actor> projectilesIterator = currentLevel.getProjectiles().iterator();
        while (projectilesIterator.hasNext()) {

            Actor projectile = projectilesIterator.next();

            Iterator<Actor> enemiesIterator = currentLevel.getEnemies().iterator();

            while (enemiesIterator.hasNext()) {

                Actor enemy = enemiesIterator.next();

                if ((Math.abs(projectile.getPositionX() - enemy.getPositionX()) < projectile.getHitBoxRadius() + enemy.getHitBoxRadius())
                        && (Math.abs(projectile.getPositionY() - enemy.getPositionY()) < projectile.getHitBoxRadius() + enemy.getHitBoxRadius())) {

                    currentLevel.getPlayer().increaseScore(1);
                    projectilesIterator.remove();
                    currentLevel.getActors().remove(projectile);
                }
            }
        }
    }

    // Getters.
    public GUIHandler getGuiHandler() {
        return guiHandler;
    }

    public double getTimeSinceLastCalculation() {
        return timeSinceLastCalculation;
    }

    public String getTHREAD_NAME() {
        return THREAD_NAME;
    }

    public Thread getThread() {
        return thread;
    }

    public Timer getTimer() {
        return timer;
    }

    public String getSimulationState() {
        return simulationState;
    }

    public Level getCurrentLevel() {
        return currentLevel;
    }

}
