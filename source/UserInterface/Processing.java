package UserInterface;

import Backend.NumberCruncher;
import Backend.EnemyFrigate;
import Backend.Player;
import Backend.Missile;

import java.awt.event.KeyEvent;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Iterator;
import processing.core.PApplet;
import processing.core.PFont;

/**
 * Main class for handling the processing part of the application. Draws the
 * GUI. Listens for user input.
 *
 * @author Kristian Honningsvag.
 */
public class Processing extends PApplet {

    // Main GUI.
    private final int windowSizeX = 1366;
    private final int windowSizeY = 768;
    private int outerWallThickness = 4;
    private int[] outerWallsRGBA = new int[]{153, 153, 153, 255};
    private int[] backgroundRGBA = new int[]{0, 0, 0, 255};

    // HUD.
    private int[] hudRGBA = new int[]{80, 150, 40, 255};
    private int[] deathScreenRGBA = new int[]{200, 50, 40, 255};

    // Player.
    private int playerDiameter = 30;
    private int[] playerRGBA = new int[]{0, 70, 200, 255};
    private int playerTurretLength = 20;
    private int playerTurretWidth = 3;
    private int[] playerTurretRGBA = new int[]{30, 30, 200, 255};

    // Enemies.
    private int enemyFrigateDiameter = 20;
    private int[] enemyFrigateRGBA = new int[]{200, 30, 30, 255};
    private int enemyFrigateTurretLength = 10;
    private int enemyFrigateTurretWidth = 3;
    private int[] enemyFrigateTurretRGBA = new int[]{70, 100, 100, 255};

    // Objects.
    private int missileDiameter = 5;
    private int[] missileRGBA = new int[]{80, 200, 0, 255};

    // Key presses.
    private boolean up = false;
    private boolean down = false;
    private boolean left = false;
    private boolean right = false;
    private boolean firePrimary = false;
    private boolean fireSecondary = false;
    private boolean swapPrimary = false;
    private boolean swapSecondary = false;
    private boolean auxiliary = false;
    private boolean menu = false;
    private boolean restart = false;

    private Player player;
    private EnemyFrigate enemyFrigate;
    private ArrayList<Missile> missiles = new ArrayList<Missile>();
    private boolean gameRunning = false;
    private boolean deathScreen = false;
    private int enemyFrigateHit = 0;

    /**
     * Initial processing setup.
     */
    @Override
    public void setup() {
        frameRate(60);  // Never change this. Value should be 60.
        cursor(CROSS);
        restartGame();
    }

    /**
     * Processing rendering settings.
     */
    @Override
    public void settings() {
        size(windowSizeX, windowSizeY);
//        fullScreen();
    }

    /**
     * The processing draw loop.
     */
    @Override
    public void draw() {

        drawOuterWalls();

        if (gameRunning) {
            drawPlayer();
            drawEnemyFrigate();
            detectPlayerEnemyFrigateCollision();
            drawMissiles();
            drawHUD();
            movePlayer();
            detectPlayerWallCollision();
            checkMissileHit();
            detectMissilesWallCollision();
        }
        if (deathScreen) {
            drawDeathScreen();
        }
        // Start screen.
        if (!gameRunning && !deathScreen) {
            drawStartScreen();
        }
    }

    /**
     * Restarts the game.
     */
    private void restartGame() {
        player = new Player(300, 250, 0, 0);
        enemyFrigate = new EnemyFrigate(1100, 600, 0, 0, player);
        deathScreen = false;
        gameRunning = false;
        missiles = new ArrayList<Missile>();
        enemyFrigateHit = 0;
    }

    /**
     * Key press events.
     */
    @Override
    public void keyPressed() {
        if (gameRunning) {
            // Accelerate upwards.
            if (keyCode == KeyEvent.VK_E) {
                up = true;
            }
            // Accelerate downwards.
            if (keyCode == KeyEvent.VK_D) {
                down = true;
            }
            // Accelerate left.
            if (keyCode == KeyEvent.VK_S) {
                left = true;
            }
            // Accelerate right.
            if (keyCode == KeyEvent.VK_F) {
                right = true;
            }
            // Swap primary weapon.
            if (keyCode == KeyEvent.VK_W) {
                swapPrimary = true;
            }
            // Swap secondary weapon.
            if (keyCode == KeyEvent.VK_R) {
                swapSecondary = true;
            }
            // Use auxiliary.
            if (keyCode == KeyEvent.VK_SPACE) {
                auxiliary = true;
            }
            // Open menu.
            if (keyCode == KeyEvent.VK_TAB) {
                menu = true;
            }
        } else if (deathScreen) {
            // Restart game.
            if (keyCode == KeyEvent.VK_ENTER) {
                restart = true;
                restartGame();
            }
        } // Start screen.
        else {
            gameRunning = true;
        }
    }

    /**
     * Key release events.
     */
    @Override
    public void keyReleased() {
        // Accelerate upwards.
        if (keyCode == KeyEvent.VK_E) {
            up = false;
        }
        // Accelerate downwards.
        if (keyCode == KeyEvent.VK_D) {
            down = false;
        }
        // Accelerate left.
        if (keyCode == KeyEvent.VK_S) {
            left = false;
        }
        // Accelerate right.
        if (keyCode == KeyEvent.VK_F) {
            right = false;
        }
        // Swap primary weapon.
        if (keyCode == KeyEvent.VK_W) {
            swapPrimary = false;
        }
        // Swap secondary weapon.
        if (keyCode == KeyEvent.VK_R) {
            swapSecondary = false;
        }
        // Use auxiliary.
        if (keyCode == KeyEvent.VK_SPACE) {
            auxiliary = false;
        }
        // Open menu.
        if (keyCode == KeyEvent.VK_TAB) {
            menu = false;
        }
        // Restart game.
        if (keyCode == KeyEvent.VK_ENTER) {
            restart = false;
        }
    }

    /**
     * Mouse press events.
     */
    @Override
    public void mousePressed() {
        if (gameRunning) {
            // Fire primary weapon.
            if (mouseButton == LEFT) {
                firePrimary = true;
                fireMissile();
            }
            // Fire secondary weapon.
            if (mouseButton == RIGHT) {
                fireSecondary = true;
            }
        } else if (deathScreen) {
        } // Start screen.
        else {
            gameRunning = true;
        }
    }

    /**
     * Mouse release events.
     */
    @Override
    public void mouseReleased() {
        // Fire primary weapon.
        if (mouseButton == LEFT) {
            firePrimary = false;
        }
        // Fire secondary weapon.
        if (mouseButton == RIGHT) {
            fireSecondary = false;
        }
    }

    /**
     * Sends the user commands to the player class.
     */
    private void movePlayer() {
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
            fireLaser();
        }
        if (swapPrimary) {
        }
        if (swapSecondary) {
        }
        if (auxiliary) {
        }
    }

    /**
     * Draws the outer walls.
     */
    private void drawOuterWalls() {
        strokeWeight(outerWallThickness);
        stroke(outerWallsRGBA[0], outerWallsRGBA[1], outerWallsRGBA[2]);
        fill(backgroundRGBA[0], backgroundRGBA[1], backgroundRGBA[2]);
        rect(0 + outerWallThickness / 2,
                0 + outerWallThickness / 2,
                windowSizeX - outerWallThickness,
                windowSizeY - outerWallThickness);
    }

    /**
     * Draws the HUD.
     */
    public void drawHUD() {
        DecimalFormat format1 = new DecimalFormat("00.0");
        DecimalFormat format2 = new DecimalFormat("0.0");

        String playerSpeed = format1.format(player.getSpeedT());
        String playerAngle = format2.format(player.getDirection());
        String playerHP = format2.format(player.getHitPoints());

        PFont font = createFont("Arial", 14, true);
        textFont(font);

        strokeWeight(1);
        stroke(hudRGBA[0], hudRGBA[1], hudRGBA[2]);
        fill(hudRGBA[0], hudRGBA[1], hudRGBA[2]);

        text("HP: " + playerHP, 14, 28);
        text("Speed: " + playerSpeed + " p/t", 14, 48);
        text("Angle: " + playerAngle + " rad", 14, 68);
        text("Active missiles: " + missiles.size(), 14, 108);
        text("Missile hits: " + enemyFrigateHit, 14, 128);
    }

    /**
     * Draws the start screen.
     */
    public void drawStartScreen() {

        PFont font = createFont("Arial", 20, true);
        textFont(font);

        strokeWeight(1);
        stroke(hudRGBA[0], hudRGBA[1], hudRGBA[2]);
        fill(hudRGBA[0], hudRGBA[1], hudRGBA[2]);

        text("Press any key to start", 500, 300);
        text("Acceleration: E, S, D, F"
                + "\n" + "Fire primary: Left mouse button"
                + "\n" + "Fire Secondary: Right mouse button", 500, 350);
    }

    /**
     * Draws the death screen.
     */
    public void drawDeathScreen() {

        PFont font = createFont("Arial", 20, true);
        textFont(font);

        strokeWeight(1);
        stroke(deathScreenRGBA[0], deathScreenRGBA[1], deathScreenRGBA[2]);
        fill(deathScreenRGBA[0], deathScreenRGBA[1], deathScreenRGBA[2]);

        text("You Were Defeated", 500, 300);
        text(" Your score were " + enemyFrigateHit, 500, 330);
        text(" Press \"Enter\" to return to Start Menu", 426, 370);
    }

    /**
     * Draws the player.
     */
    private void drawPlayer() {

        player.act();

        // Draw main body.
        strokeWeight(0);
        stroke(playerRGBA[0], playerRGBA[1], playerRGBA[2]);
        fill(playerRGBA[0], playerRGBA[1], playerRGBA[2]);
        ellipse((float) player.getPositionX(), (float) player.getPositionY(), playerDiameter, playerDiameter);

        // Draw turret.
        double xVector = mouseX - player.getPositionX();
        double yVector = mouseY - player.getPositionY();
        double targetAngle = NumberCruncher.calculateAngle(xVector, yVector);

        strokeWeight(playerTurretWidth);
        stroke(playerTurretRGBA[0], playerTurretRGBA[1], playerTurretRGBA[2]);
        fill(playerTurretRGBA[0], playerTurretRGBA[1], playerTurretRGBA[2]);
        line((float) player.getPositionX(), (float) player.getPositionY(),
                (float) player.getPositionX() + (float) (playerTurretLength * Math.cos(targetAngle)),
                (float) player.getPositionY() + (float) (playerTurretLength * Math.sin(targetAngle)));
    }

    /**
     * Draws the enemy frigate.
     */
    private void drawEnemyFrigate() {

        enemyFrigate.act();

        // Draw main body.
        strokeWeight(0);
        stroke(enemyFrigateRGBA[0], enemyFrigateRGBA[1], enemyFrigateRGBA[2]);
        fill(enemyFrigateRGBA[0], enemyFrigateRGBA[1], enemyFrigateRGBA[2]);
        ellipse((float) enemyFrigate.getPositionX(), (float) enemyFrigate.getPositionY(), enemyFrigateDiameter, enemyFrigateDiameter);

        // Draw turret.
        strokeWeight(enemyFrigateTurretWidth);
        stroke(enemyFrigateTurretRGBA[0], enemyFrigateTurretRGBA[1], enemyFrigateTurretRGBA[2]);
        fill(enemyFrigateTurretRGBA[0], enemyFrigateTurretRGBA[1], enemyFrigateTurretRGBA[2]);
        line((float) enemyFrigate.getPositionX(), (float) enemyFrigate.getPositionY(),
                (float) enemyFrigate.getPositionX() + (float) (enemyFrigateTurretLength * Math.cos(enemyFrigate.getDirection())),
                (float) enemyFrigate.getPositionY() + (float) (enemyFrigateTurretLength * Math.sin(enemyFrigate.getDirection())));
    }

    /**
     * Draws the missiles.
     */
    private void drawMissiles() {

        for (Missile missile : missiles) {
            missile.act();
            strokeWeight(0);
            stroke(missileRGBA[0], missileRGBA[1], missileRGBA[2]);
            fill(missileRGBA[0], missileRGBA[1], missileRGBA[2]);
            ellipse((float) missile.getPositionX(), (float) missile.getPositionY(), missileDiameter, missileDiameter);
        }
    }

    /**
     * Detects missiles-wall collisions.
     */
    private void detectMissilesWallCollision() {

        Iterator<Missile> it = missiles.iterator();
        while (it.hasNext()) {

            Missile missile = it.next();

            if (missile.getPositionX() + (missileDiameter / 2) >= (windowSizeX - outerWallThickness)         // Right wall.
                    || missile.getPositionY() + (missileDiameter / 2) >= (windowSizeY - outerWallThickness)  // Lower wall.
                    || missile.getPositionX() - (missileDiameter / 2) <= (0 + outerWallThickness)            // Left wall.
                    || missile.getPositionY() - (missileDiameter / 2) <= (0 + outerWallThickness))           // Upper wall
            {
                it.remove();
            }
        }
    }

    /**
     * Detects player and enemy frigate wall collisions.
     */
    private void detectPlayerWallCollision() {
        // Right wall.
        if (player.getPositionX() + (playerDiameter / 2) >= (windowSizeX - outerWallThickness)) {
            player.wallBounce("right");
        }
        if (enemyFrigate.getPositionX() + (enemyFrigateDiameter / 2) >= (windowSizeX - outerWallThickness)) {
            enemyFrigate.wallBounce("right");
        }
        // Lower wall.
        if (player.getPositionY() + (playerDiameter / 2) >= (windowSizeY - outerWallThickness)) {
            player.wallBounce("lower");
        }
        if (enemyFrigate.getPositionY() + (enemyFrigateDiameter / 2) >= (windowSizeY - outerWallThickness)) {
            enemyFrigate.wallBounce("lower");
        }
        // Left wall.
        if (player.getPositionX() - (playerDiameter / 2) <= (0 + outerWallThickness)) {
            player.wallBounce("left");
        }
        if (enemyFrigate.getPositionX() - (enemyFrigateDiameter / 2) <= (0 + outerWallThickness)) {
            enemyFrigate.wallBounce("left");
        }
        // Upper wall.
        if (player.getPositionY() - (playerDiameter / 2) <= (0 + outerWallThickness)) {
            player.wallBounce("upper");
        }
        if (enemyFrigate.getPositionY() - (enemyFrigateDiameter / 2) <= (0 + outerWallThickness)) {
            enemyFrigate.wallBounce("upper");
        }
    }

    /**
     * Detects player and enemy frigate collisions.
     */
    private void detectPlayerEnemyFrigateCollision() {

        if ((Math.abs(player.getPositionX() - enemyFrigate.getPositionX()) < (playerDiameter / 2) + (enemyFrigateDiameter / 2))
                && (Math.abs(player.getPositionY() - enemyFrigate.getPositionY()) < (playerDiameter / 2) + (enemyFrigateDiameter / 2))) {
            gameRunning = false;
            deathScreen = true;
        }
    }
    
    /**
     * Counts missile hits on the enemy frigate by the player.
     */
    private void checkMissileHit() {
        Iterator<Missile> it = missiles.iterator();
        while (it.hasNext()) {

            Missile missile = it.next();

            if ((Math.abs(missile.getPositionX() - enemyFrigate.getPositionX()) < (missileDiameter / 2) + (enemyFrigateDiameter / 2))
                    && (Math.abs(missile.getPositionY() - enemyFrigate.getPositionY()) < (missileDiameter / 2) + (enemyFrigateDiameter / 2))) {

                enemyFrigateHit++;
                it.remove();
            }
        }
    }

    /**
     * Fires the laser from the player to the mouse cursor.
     */
    private void fireLaser() {

        double xVector = 100 * (mouseX - player.getPositionX());
        double yVector = 100 * (mouseY - player.getPositionY());

        strokeWeight(2);
        stroke(255, 0, 0);
        line((float) player.getPositionX(), (float) player.getPositionY(),
                mouseX + (float) xVector, mouseY + (float) yVector);
    }

    /**
     * Fires a missile from the player to the mouse cursor.
     */
    private void fireMissile() {

        double xVector = mouseX - player.getPositionX();
        double yVector = mouseY - player.getPositionY();
        double targetAngle = NumberCruncher.calculateAngle(xVector, yVector);

        Missile missile = new Missile(player.getPositionX(), player.getPositionY(),
                player.getSpeedX(), player.getSpeedY(), targetAngle);

        missiles.add(missile);
    }
}
