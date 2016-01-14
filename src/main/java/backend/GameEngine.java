package backend;

import userinterface.GUIHandler;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Handles the simulation.
 *
 * @author Kristian Honningsvag.
 */
public class GameEngine implements Runnable {

    private Player player;
    private ArrayList<Actor> enemies;
    private ArrayList<Actor> projectiles;
    private ArrayList<Actor> items;

    private GUIHandler guiHandler;

    private Thread thread;
    private final String THREAD_NAME;

    // Environment variable.
    private volatile String simulationState = "startScreen";
    private int simulationSpeed = 1;  // Milliseconds between rounds.

    private Timer timer;
    private double timeSinceLastCalculation;

    // Key presses.
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
        simulationClear();

        while (true) {
            
            switch (simulationState) {

                case "startScreen": {
                    if (enter) {
                        simulationState = "gameplay";
                    }
                    break;
                }

                case "gameplay": {
                    if (timer.timePassed() >= simulationSpeed) {
                        if (up) {
                            player.accelerate("up");
                        }
                        if (down) {
                            player.accelerate("down");
                        }
                        if (left) {
                            player.accelerate("left");
                        }
                        if (right) {
                            player.accelerate("right");
                        }
                        if (firePrimary) {

                            player.fireBullet();
                        }
                        if (fireSecondary) {
                            player.fireLaser(fireSecondary);
                        }
                        if (swapPrimary) {
                        }
                        if (swapSecondary) {
                        }
                        if (space) {
                            simulationState = "menuScreen";
                        }
                        if (tab) {
                        }
                        if (enter) {
                        }

                        player.act();

                        for (Actor actor : enemies) {
                            actor.act();
                        }

                        for (Actor actor : projectiles) {
                            actor.act();
                        }

                        for (Actor actor : items) {
                            actor.act();
                        }
                        detectWallCollision();
                        checkMissileHit();
                        detectPlayerEnemyCollision();
                        timer.restart();

                        player.fireLaser(fireSecondary);

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
                            simulationClear();
                            simulationState = "startScreen";
                        }

                        player.act();

                        for (Actor actor : enemies) {
                            actor.act();
                        }

                        for (Actor actor : projectiles) {
                            actor.act();
                        }

                        for (Actor actor : items) {
                            actor.act();
                        }
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
     * Clears and re-initializes the simulation.
     */
    private void simulationClear() {

        player = new Player(300, 250, this, guiHandler);

        enemies = new ArrayList<Actor>();
        enemies.add(new Frigate(1100, 600, this, guiHandler));

        projectiles = new ArrayList<Actor>();
        items = new ArrayList<Actor>();
    }

    /**
     * Handles keyboard input.
     *
     * @param keyCode The button that was pressed.
     * @param keyState Whether the button was pressed or released.
     */
    public void keyboardInput(int keyCode, boolean keyState) {

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
     * Handles mouse input.
     *
     * @param mouseButton The button that was pressed.
     * @param keyState Whether the button was pressed or released.
     */
    public void mouseInput(int mouseButton, boolean keyState) {

        // Fire primary weapon.
        if (mouseButton == 37) {
            firePrimary = keyState;
        }
        // Fire secondary weapon.
        if (mouseButton == 39) {
            fireSecondary = keyState;
        }
    }

    /**
     * Detects wall collisions.
     */
    private void detectWallCollision() {

        // Projectiles.
        Iterator<Actor> it = projectiles.iterator();
        while (it.hasNext()) {

            Actor actor = it.next();

            if (actor.getPositionX() + (actor.getHitBoxRadius()) >= (guiHandler.getWindowSizeX() - guiHandler.getOuterWallThickness()) // Right wall.
                    || actor.getPositionY() + (actor.getHitBoxRadius()) >= (guiHandler.getWindowSizeY() - guiHandler.getOuterWallThickness()) // Lower wall.
                    || actor.getPositionX() - (actor.getHitBoxRadius()) <= (0 + guiHandler.getOuterWallThickness()) // Left wall.
                    || actor.getPositionY() - (actor.getHitBoxRadius()) <= (0 + guiHandler.getOuterWallThickness())) // Upper wall
            {
                it.remove();
            }
        }

        // Right wall.
        if (player.getPositionX() + (player.getHitBoxRadius()) >= (guiHandler.getWindowSizeX() - guiHandler.getOuterWallThickness())) {
            player.wallBounce("right");
        }
        for (Actor actor : enemies) {
            if (actor.getPositionX() + actor.getHitBoxRadius() >= (guiHandler.getWindowSizeX() - guiHandler.getOuterWallThickness())) {
                actor.wallBounce("right");
            }
        }
        // Lower wall.
        if (player.getPositionY() + player.getHitBoxRadius() >= (guiHandler.getWindowSizeY() - guiHandler.getOuterWallThickness())) {
            player.wallBounce("lower");
        }
        for (Actor actor : enemies) {
            if (actor.getPositionY() + actor.getHitBoxRadius() >= (guiHandler.getWindowSizeY() - guiHandler.getOuterWallThickness())) {
                actor.wallBounce("lower");
            }
        }
        // Left wall.
        if (player.getPositionX() - player.getHitBoxRadius() <= (0 + guiHandler.getOuterWallThickness())) {
            player.wallBounce("left");
        }
        for (Actor actor : enemies) {
            if (actor.getPositionX() - actor.getHitBoxRadius() <= (0 + guiHandler.getOuterWallThickness())) {
                actor.wallBounce("left");
            }
        }
        // Upper wall.
        if (player.getPositionY() - player.getHitBoxRadius() <= (0 + guiHandler.getOuterWallThickness())) {
            player.wallBounce("upper");
        }
        for (Actor actor : enemies) {
            if (actor.getPositionY() - actor.getHitBoxRadius() <= (0 + guiHandler.getOuterWallThickness())) {
                actor.wallBounce("upper");
            }
        }
    }

    /**
     * Detects player and enemy collisions.
     */
    private void detectPlayerEnemyCollision() {

        for (Actor actor : enemies) {

            if ((Math.abs(player.getPositionX() - actor.getPositionX()) < player.getHitBoxRadius() + actor.getHitBoxRadius())
                    && (Math.abs(player.getPositionY() - actor.getPositionY()) < player.getHitBoxRadius() + actor.getHitBoxRadius())) {
                simulationState = "deathScreen";
            }
        }
    }

    /**
     * Counts missile hits on the enemy frigate by the player.
     */
    private void checkMissileHit() {
        Iterator<Actor> projectilesIterator = projectiles.iterator();
        while (projectilesIterator.hasNext()) {

            Actor projectile = projectilesIterator.next();

            Iterator<Actor> enemiesIterator = enemies.iterator();

            while (enemiesIterator.hasNext()) {

                Actor enemy = enemiesIterator.next();

                if ((Math.abs(projectile.getPositionX() - enemy.getPositionX()) < projectile.getHitBoxRadius() + enemy.getHitBoxRadius())
                        && (Math.abs(projectile.getPositionY() - enemy.getPositionY()) < projectile.getHitBoxRadius() + enemy.getHitBoxRadius())) {

                    player.increaseScore(1);
                    projectilesIterator.remove();
                }
            }
        }
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

}
