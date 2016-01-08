package UserInterface;

import Backend.Player;
import Backend.Missile;

import java.awt.event.KeyEvent;
import java.text.DecimalFormat;
import java.util.ArrayList;
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
    private int playerDiameter = 20;
    private int missileDiameter = 5;

    private Player player;
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

    /**
     * Processing initial setup.
     */
    @Override
    public void setup() {
        frameRate(60);
        cursor(CROSS);
        player = new Player(300, 250, 0, 0);
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
        drawMissiles();
        movePlayer();
        detectWallCollision();
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
        }
        if (mouseButton == RIGHT) {
            fireSecondary = true;
            fireMissile();
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
    }

    /**
     * Draws the player.
     */
    private void drawPlayer() {
        player.act();
        strokeWeight(0);
        stroke(playerRGBA[0], playerRGBA[1], playerRGBA[2]);
        fill(playerRGBA[0], playerRGBA[1], playerRGBA[2]);
        ellipse(player.getPositionX(), player.getPositionY(), playerDiameter, playerDiameter);
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
            ellipse(missile.getPositionX(), missile.getPositionY(), missileDiameter, missileDiameter);
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
        if (firePrimary) {
            fireLaser();
        }
    }

    /**
     * Detects wall collisions.
     */
    private void detectWallCollision() {
        // Right wall.
        if (player.getPositionX() + (playerDiameter / 2) >= (windowSizeX - outerWallThickness)) {
            player.wallBounce("right");
        }
        // Lower wall.
        if (player.getPositionY() + (playerDiameter / 2) >= (windowSizeY - outerWallThickness)) {
            player.wallBounce("lower");
        }
        // Left wall.
        if (player.getPositionX() - (playerDiameter / 2) <= (0 + outerWallThickness)) {
            player.wallBounce("left");
        }
        // Upper wall.
        if (player.getPositionY() - (playerDiameter / 2) <= (0 + outerWallThickness)) {
            player.wallBounce("upper");
        }
    }

    /**
     * Fires the laser from the player to the mouse cursor.
     */
    private void fireLaser() {

        float xVector = mouseX - player.getPositionX();
        float yVector = mouseY - player.getPositionY();

        strokeWeight(2);
        stroke(255, 0, 0);
        line(player.getPositionX(), player.getPositionY(), mouseX + 100 * xVector, mouseY + 100 * yVector);
    }

    /**
     * Fires a missile from the player to the mouse cursor.
     */
    private void fireMissile() {

        float xVector = mouseX - player.getPositionX();
        float yVector = mouseY - player.getPositionY();

        Missile missile = new Missile(player.getPositionX(), player.getPositionY(), xVector * 0.01f, yVector * 0.01f);
        missiles.add(missile);
    }
}
