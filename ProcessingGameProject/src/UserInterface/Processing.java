package UserInterface;

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
 * Main class for handling the processing part of the application.
 *
 * @author Kristian Honningsvag.
 */
public class Processing extends PApplet {

    private final int windowSizeX = 1366;
    private final int windowSizeY = 768;
    private int outerWallThickness = 4;
    private int playerDiameter = 30;
    private int missileDiameter = 5;
    private int enemyFrigateDiameter = 20;

    private Player player;
    private EnemyFrigate enemyFrigate;
    private ArrayList<Missile> missiles = new ArrayList<Missile>();

    // Key presses.
    private boolean up = false;
    private boolean down = false;
    private boolean left = false;
    private boolean right = false;

    // Mouse presses.
    private boolean firePrimary = false;
    private boolean fireSecondary = false;

    // Colors.
    private int[] outerWallsRGBA = new int[]{153, 153, 153, 255};
    private int[] playerRGBA = new int[]{0, 70, 200, 255};
    private int[] missileRGBA = new int[]{80, 200, 0, 255};
    private int[] hudRGBA = new int[]{80, 150, 40, 255};
    private int[] enemyFrigateRGBA = new int[]{200, 50, 50, 255};

    /**
     * Processing initial setup.
     */
    @Override
    public void setup() {
        frameRate(60);
        cursor(CROSS);
        player = new Player(300, 250, 0, 0);
        enemyFrigate = new EnemyFrigate(1100, 600, 0, 0, player);
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
        drawHUD();
        drawPlayer();
        drawEnemyFrigate();
        drawMissiles();
        movePlayer();
        detectPlayerWallCollision();
        detectMissilesWallCollision();
    }

    /**
     * Key press events.
     */
    @Override
    public void keyPressed() {

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
    }

    /**
     * Mouse press events.
     */
    @Override
    public void mousePressed() {

        if (mouseButton == LEFT) {
            firePrimary = true;
            fireMissile();
        }
        if (mouseButton == RIGHT) {
            fireSecondary = true;
        }
    }

    /**
     * Mouse release events.
     */
    @Override
    public void mouseReleased() {

        if (mouseButton == LEFT) {
            firePrimary = false;
        }
        if (mouseButton == RIGHT) {
            fireSecondary = false;
        }
    }

    /**
     * Draws the outer walls.
     */
    private void drawOuterWalls() {
        strokeWeight(outerWallThickness);
        stroke(outerWallsRGBA[0], outerWallsRGBA[1], outerWallsRGBA[2]);
        fill(0);
        rect(0 + outerWallThickness / 2,
                0 + outerWallThickness / 2,
                windowSizeX - outerWallThickness,
                windowSizeY - outerWallThickness);
    }

    /**
     * Draws the HUD.
     */
    public void drawHUD() {
        DecimalFormat speedTextFormat = new DecimalFormat("00.0");
        DecimalFormat angleTextFormat = new DecimalFormat("0.0");

        String speedText = speedTextFormat.format(player.getSpeedT());
        String angleText = angleTextFormat.format(player.getDirection());

        PFont font = createFont("Arial", 14, true);
        textFont(font);

        strokeWeight(1);
        stroke(hudRGBA[0], hudRGBA[1], hudRGBA[2]);
        fill(hudRGBA[0], hudRGBA[1], hudRGBA[2]);

        text("Speed: " + speedText + " p/t", 14, 28);
        text("Angle: " + angleText + " rad", 14, 48);
        text("Active missiles: " + missiles.size(), 14, 68);
    }

    /**
     * Draws the player.
     */
    private void drawPlayer() {
        player.act();
        strokeWeight(0);
        stroke(playerRGBA[0], playerRGBA[1], playerRGBA[2]);
        fill(playerRGBA[0], playerRGBA[1], playerRGBA[2]);
        ellipse((float) player.getPositionX(), (float) player.getPositionY(), playerDiameter, playerDiameter);
    }

    /**
     * Draws the enemy frigate.
     */
    private void drawEnemyFrigate() {
        enemyFrigate.act();
        strokeWeight(0);
        stroke(enemyFrigateRGBA[0], enemyFrigateRGBA[1], enemyFrigateRGBA[2]);
        fill(enemyFrigateRGBA[0], enemyFrigateRGBA[1], enemyFrigateRGBA[2]);
        ellipse((float) enemyFrigate.getPositionX(), (float) enemyFrigate.getPositionY(), enemyFrigateDiameter, enemyFrigateDiameter);
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
     * Tells the player to accelerate in a given direction.
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
        if (fireSecondary) {
            fireLaser();
        }
    }

    /**
     * Detects missiles-wall collisions.
     */
    private void detectMissilesWallCollision() {

        Iterator<Missile> it = missiles.iterator();
        while (it.hasNext()) {

            Missile missile = it.next();

            if (missile.getPositionX() + (missileDiameter / 2) >= (windowSizeX - outerWallThickness)        // Right wall.
                    || missile.getPositionY() + (missileDiameter / 2) >= (windowSizeY - outerWallThickness) // Lower wall.
                    || missile.getPositionX() - (missileDiameter / 2) <= (0 + outerWallThickness)           // Left wall.
                    || missile.getPositionY() - (missileDiameter / 2) <= (0 + outerWallThickness))          // Upper wall
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
     * Fires the laser from the player to the mouse cursor.
     */
    private void fireLaser() {

        double xVector = mouseX - player.getPositionX();
        double yVector = mouseY - player.getPositionY();

        strokeWeight(2);
        stroke(255, 0, 0);
        line((float) player.getPositionX(), (float) player.getPositionY(), mouseX + 100 * (float) xVector, mouseY + 100 * (float) yVector);
    }

    /**
     * Fires a missile from the player to the mouse cursor.
     */
    private void fireMissile() {

        double xVector = mouseX - player.getPositionX();
        double yVector = mouseY - player.getPositionY();

        double angle = Math.atan(yVector / xVector);
        double targetAngle = 0;

        // If speed vector lies in processing quadrant 1
        if (xVector > 0 && yVector > 0) {
            targetAngle = angle;
        }
        // If speed vector lies in processing quadrant 2
        if (xVector < 0 && yVector > 0) {
            targetAngle = angle + Math.PI;
        }
        // If speed vector lies in processing quadrant 3
        if (xVector < 0 && yVector < 0) {
            targetAngle = angle + Math.PI;
        }
        // If speed vector lies in processing quadrant 4
        if (xVector > 0 && yVector < 0) {
            targetAngle = angle + 2 * Math.PI;
        }

        // If speed vector is straight to the right.
        if (xVector > 0 && yVector == 0) {
            targetAngle = 0;
        }
        // If speed vector is straight down.
        if (xVector == 0 && yVector > 0) {
            targetAngle = Math.PI / 2;
        }
        // If speed vector is straight to the left.
        if (xVector < 0 && yVector == 0) {
            targetAngle = Math.PI;
        }
        // If speed vector is straight up.
        if (xVector == 0 && yVector < 0) {
            targetAngle = (2 * Math.PI) - (Math.PI / 2);
        }
        // If standing still.
        if (xVector == 0 && yVector == 0) {
            targetAngle = 0;
        }

        Missile missile = new Missile(player.getPositionX(), player.getPositionY(), player.getSpeedX(), player.getSpeedY(), targetAngle);
        missiles.add(missile);
    }
}
