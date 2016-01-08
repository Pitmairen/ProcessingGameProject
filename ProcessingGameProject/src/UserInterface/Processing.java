package UserInterface;

import Backend.Player;
import java.awt.event.KeyEvent;
import java.text.DecimalFormat;
import java.util.ArrayList;
import processing.core.PApplet;

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
    }

    /**
     * The processing draw loop.
     */
    @Override
    public void draw() {
        clear();
        drawOuterWalls();
        drawPlayer();
        displayMovement();
        if (mousePressed) {
            fire();
        }
        detectWallCollision();
    }

    /**
     * Key press events.
     */
    @Override
    public void keyPressed() {
        if (keyPressed == true) {
            // Accelerate upwards.
            if (keyCode == KeyEvent.VK_E) {
                player.accelerate("up");
            }
            // Accelerate downwards.
            if (keyCode == KeyEvent.VK_D) {
                player.accelerate("down");
            }
            // Accelerate left.
            if (keyCode == KeyEvent.VK_S) {
                player.accelerate("left");
            }
            // Accelerate right.
            if (keyCode == KeyEvent.VK_F) {
                player.accelerate("right");
            }
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
        DecimalFormat df = new DecimalFormat("0.00");
        System.out.println("Speed: " + df.format(player.getSpeedT()) + " p/t");
        System.out.println("Angle: " + df.format(player.getDirection()) + " rad");
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
