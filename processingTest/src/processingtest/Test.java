package processingtest;

import java.awt.event.KeyEvent;
import java.util.ArrayList;
import processing.core.PApplet;

/**
 * Testing out features in processing.
 *
 * @author Kristian Honningsvag.
 */
public class Test extends PApplet {

    private int playerPositionX;
    private int playerPositionY;

    private ArrayList enemies;

    /**
     * Needed to start the processing application in NetBeans.
     *
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        Test.main("processingtest.Test");
    }

    @Override
    public void setup() {
        playerPositionX = 300;
        playerPositionY = 300;
    }

    @Override
    public void settings() {
        size(800, 600);
    }

    @Override
    public void draw() {
        clear();
        drawPlayer();
        if (mousePressed) {
            fire();
        }
    }

    @Override
    public void keyPressed() {
        if (keyPressed == true) {
            // Move up.
            if (keyCode == KeyEvent.VK_E) {
                playerPositionY = playerPositionY - 10;
            }
            // Move down.
            if (keyCode == KeyEvent.VK_D) {
                playerPositionY = playerPositionY + 10;
            }
            // Move left.
            if (keyCode == KeyEvent.VK_S) {
                playerPositionX = playerPositionX - 10;
            }
            // Move right.
            if (keyCode == KeyEvent.VK_F) {
                playerPositionX = playerPositionX + 10;
            }
        }
    }

    /**
     * Draws the player.
     */
    private void drawPlayer() {
        strokeWeight(1);
        stroke(0, 0, 255);
        fill(0, 200, 200);
        ellipse(playerPositionX, playerPositionY, 20, 20);
    }

    /**
     * Fires the weapon from the player to the mouse cursor.
     */
    private void fire() {
        strokeWeight(2);
        stroke(255, 0, 0);
        line(playerPositionX, playerPositionY, mouseX, mouseY);
    }
}
