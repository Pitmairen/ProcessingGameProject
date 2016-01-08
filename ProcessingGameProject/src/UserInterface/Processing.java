package UserInterface;

import Backend.Player;
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

    private Player player;
    private ArrayList enemies;

    private final int windowSizeX = 1366;
    private final int windowSizeY = 768;

    private int outerWallThickness = 4;
    private int playerDiameter = 20;

    // Key presses.
    private boolean up = false;
    private boolean down = false;
    private boolean left = false;
    private boolean right = false;

    /**
     * Processing initial setup.
     */
    @Override
    public void setup() {
        frameRate(60);
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
        clear();
        cursor(CROSS);
        drawOuterWalls();
        drawPlayer();
        displayMovement();
        if (mousePressed && (mouseButton == LEFT)) {
            fire();
        }
        movePlayer();
        detectWallCollision();
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
            player.accelerate("down");
        }
        // Accelerate left.
        if (keyCode == KeyEvent.VK_S) {
            left = true;
            player.accelerate("left");
        }
        // Accelerate right.
        if (keyCode == KeyEvent.VK_F) {
            right = true;
            player.accelerate("right");
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
     * Displays the players current speed and direction.
     */
    public void displayMovement() {
        DecimalFormat speedTextFormat = new DecimalFormat("00.0");
        DecimalFormat angleTextFormat = new DecimalFormat("0.0");

        PFont f = createFont("Arial", 12, true);
        textFont(f, 16);
        text("Speed: " + speedTextFormat.format(player.getSpeedT()) + " p/t", 10, 30);
        text("Angle: " + angleTextFormat.format(player.getDirection()) + " rad", 10, 50);
    }

    /**
     * Draws the outer walls.
     */
    private void drawOuterWalls() {
        strokeWeight(outerWallThickness);
        stroke(153, 153, 5);
        fill(0);
        rect(0 + outerWallThickness / 2,
                0 + outerWallThickness / 2,
                windowSizeX - outerWallThickness,
                windowSizeY - outerWallThickness);
    }

    /**
     * Draws the player.
     */
    private void drawPlayer() {
        player.act();
        strokeWeight(1);
        stroke(0, 200, 200);
        fill(0, 200, 200);
        ellipse(player.getPositionX(), player.getPositionY(), playerDiameter, playerDiameter);
    }

    /**
     * Fires the weapon from the player to the mouse cursor.
     */
    private void fire() {
        strokeWeight(2);
        stroke(255, 0, 0);
        line(player.getPositionX(), player.getPositionY(), mouseX, mouseY);
    }
}
