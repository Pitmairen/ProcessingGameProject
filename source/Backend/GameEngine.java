package Backend;

import UserInterface.GUIHandler;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.Iterator;

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

    // Environment variables.
    private volatile boolean playerAlive;
    private volatile boolean simulationRunning;

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
        simulationSetMode("menuScreen");

        while (true) {

            if (enter) {
                simulationSetMode("gameplay");
            }

            while (simulationRunning && playerAlive) {

                if (timer.timePassed() >= 1) {

                    // User input.
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
                    }
                    if (fireSecondary) {
                    }
                    if (swapPrimary) {
                    }
                    if (swapSecondary) {
                    }
                    if (space) {
                    }
                    if (tab) {
                    }
                    if (enter) {
                    }

                    // Make actors act.
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
            }

        }
    }
    
    

    /**
     * Clears and re-initializes the simulation.
     */
    private void simulationClear() {

        player = new Player(300, 250, this);

        enemies = new ArrayList<Actor>();
        enemies.add(new Frigate(1100, 600, this));

        projectiles = new ArrayList<Actor>();
        items = new ArrayList<Actor>();
    }



    /**
     * Sets the mode of the simulation.
     *
     * @param mode Name of the mode.
     */
    private void simulationSetMode(String mode) {
        if (mode.equalsIgnoreCase("startScreen")) {
            simulationRunning = false;
            playerAlive = false;
        }
        if (mode.equalsIgnoreCase("gameplay")) {
            simulationRunning = true;
            playerAlive = true;
        }
        if (mode.equalsIgnoreCase("pauseScreen")) {
            simulationRunning = false;
            playerAlive = true;
        }
        if (mode.equalsIgnoreCase("deathScreen")) {
            simulationRunning = true;
            playerAlive = false;
        }
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
                simulationSetMode("deathScreen");
            }
        }
    }

    /**
     * Counts missile hits on the enemy frigate by the player.
     */
    private void checkMissileHit() {
        Iterator<Actor> it = projectiles.iterator();
        while (it.hasNext()) {

            Actor projectile = it.next();

            while (it.hasNext()) {

                Actor enemy = it.next();

                if ((Math.abs(projectile.getPositionX() - enemy.getPositionX()) < projectile.getHitBoxRadius() + enemy.getHitBoxRadius())
                        && (Math.abs(projectile.getPositionY() - enemy.getPositionY()) < projectile.getHitBoxRadius() + enemy.getHitBoxRadius())) {

                    player.increaseScore(1);
                    it.remove();
                }
            }
        }
    }
//    /**
//     * Fires the laser from the player to the mouse cursor.
//     */
//    private void fireLaser() {
//
//        double xVector = 100 * (mouseX - player.getPositionX());
//        double yVector = 100 * (mouseY - player.getPositionY());
//
//        strokeWeight(2);
//        stroke(255, 0, 0);
//        line((float) player.getPositionX(), (float) player.getPositionY(),
//                mouseX + (float) xVector, mouseY + (float) yVector);
//    }
//
//    /**
//     * Fires a missile from the player to the mouse cursor.
//     */
//    private void fireMissile() {
//
//        double xVector = guiHandler.mouseX - player.getPositionX();
//        double yVector = guiHandler.mouseY - player.getPositionY();
//        double targetAngle = NumberCruncher.calculateAngle(xVector, yVector);
//
//        Actor bullet = new Bullet(player.getPositionX(), player.getPositionY(), 0,
//                player.getSpeedX(), player.getSpeedY(), 20, 0, 0, targetAngle);
//
//        missiles.add(missile);
//    }

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

    public boolean isSimulationRunning() {
        return simulationRunning;
    }

    public boolean isPlayerAlive() {
        return playerAlive;
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

}
