package UserInterface;

import Backend.Player;
import java.awt.event.KeyEvent;
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

    private final int playFieldSizeX = 1366;
    private final int playFieldSizeY = 768;

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
        size(playFieldSizeX, playFieldSizeY);
    }

    /**
     * Processing draw loop.
     */
    @Override
    public void draw() {
        clear();
        drawPlayer();
        if (mousePressed) {
            fire();
        }
    }

    /**
     * Key events.
     */
    @Override
    public void keyPressed() {
        if (keyPressed == true) {
            // Move up.
            if (keyCode == KeyEvent.VK_E) {
                player.setPlayerSpeedY(player.getPlayerSpeedY() - 0.1f);
            }
            // Move down.
            if (keyCode == KeyEvent.VK_D) {
                player.setPlayerSpeedY(player.getPlayerSpeedY() + 0.1f);
            }
            // Move left.
            if (keyCode == KeyEvent.VK_S) {
                player.setPlayerSpeedX(player.getPlayerSpeedX() - 0.1f);
            }
            // Move right.
            if (keyCode == KeyEvent.VK_F) {
                player.setPlayerSpeedX(player.getPlayerSpeedX() + 0.1f);
            }
        }
    }

    /**
     * Draws the player.
     */
    private void drawPlayer() {
        player.move();
        strokeWeight(1);
        stroke(0, 0, 255);
        fill(0, 200, 200);
        ellipse(player.getPlayerPositionX(), player.getPlayerPositionY(), 20, 20);
    }

    /**
     * Fires the weapon from the player to the mouse cursor.
     */
    private void fire() {
        strokeWeight(2);
        stroke(255, 0, 0);
        line(player.getPlayerPositionX(), player.getPlayerPositionY(), mouseX, mouseY);
    }
}
