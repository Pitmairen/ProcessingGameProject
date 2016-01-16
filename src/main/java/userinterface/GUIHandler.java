package userinterface;

import backend.GameEngine;
import backend.actor.Actor;

import java.text.DecimalFormat;
import java.util.concurrent.CopyOnWriteArrayList;
import processing.core.PApplet;
import processing.core.PFont;

/**
 * Handles rendering the GUI. Also listens for user input and sends it to the
 * game engine.
 *
 * @author Kristian Honningsvag.
 */
public class GUIHandler extends PApplet {

    // Main GUI.
    private int outerWallThickness = 4;
    private int[] outerWallsRGBA = new int[]{153, 153, 153, 255};
    private int[] backgroundRGBA = new int[]{0, 0, 0, 255};

    // HUD.
    private int[] hudRGBA = new int[]{80, 150, 40, 255};
    private int[] deathScreenRGBA = new int[]{200, 50, 40, 255};

    private GameEngine gameEngine;

    /**
     * Processing initial setup.
     */
    @Override
    public void setup() {
        frameRate(60);
        cursor(CROSS);
        gameEngine = new GameEngine(this, "Game Engine");
    }

    /**
     * Processing rendering settings.
     */
    @Override
    public void settings() {
        fullScreen(P2D);
    }

    /**
     * Processing draw loop.
     */
    @Override
    public void draw() {

        switch (gameEngine.getSimulationState()) {

            case "startScreen": {
                drawOuterWalls();
                drawStartScreen();
                break;
            }

            case "gameplay": {
                drawOuterWalls();
                drawEntities();
                drawHUD();
                break;
            }

            case "menuScreen": {
                drawOuterWalls();
                drawMenuScreen();
                break;
            }

            case "deathScreen": {
                drawOuterWalls();
                drawEntities();
                drawDeathScreen();
                break;
            }
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
                width - outerWallThickness,
                height - outerWallThickness);
    }

    /**
     * Draws the HUD.
     */
    public void drawHUD() {

        DecimalFormat format1 = new DecimalFormat("0");
        DecimalFormat format2 = new DecimalFormat("00");
        DecimalFormat format3 = new DecimalFormat("000");
        DecimalFormat format4 = new DecimalFormat("0.0");
        DecimalFormat format5 = new DecimalFormat("00.0");
        DecimalFormat format6 = new DecimalFormat("000.0");

        String playerSpeed = format2.format(gameEngine.getCurrentLevel().getPlayer().getSpeedT() * 100);
        String playerAngle = format4.format(gameEngine.getCurrentLevel().getPlayer().getCourse());
        String playerHP = format2.format(gameEngine.getCurrentLevel().getPlayer().getHitPoints());
        PFont font = createFont("Arial", 14, true);
        textFont(font);

        strokeWeight(1);
        stroke(hudRGBA[0], hudRGBA[1], hudRGBA[2]);
        fill(hudRGBA[0], hudRGBA[1], hudRGBA[2]);

        text("HP: " + playerHP, 14, 28);
        text("Speed: " + playerSpeed + " m/s", 14, 48);
        text("Angle: " + playerAngle + " rad", 14, 68);
        text("Projectiles on screen: " + gameEngine.getCurrentLevel().getProjectiles().size(), 14, 108);
        text("Score: " + gameEngine.getCurrentLevel().getPlayer().getScore(), 14, 128);
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

        text("Press \"Enter\" to start."
                + "\n" + "Press  \"Escape\" to quit."
                + "\n"
                + "\n" + "Keybindings:"
                + "\n" + "Acceleration: E, S, D, F"
                + "\n" + "Fire primary: Left mouse button"
                + "\n" + "Fire Secondary: Right mouse button"
                + "\n" + "Pause: Space", 500, 300);
    }

    /**
     * Draws the menu screen.
     */
    public void drawMenuScreen() {

        PFont font = createFont("Arial", 20, true);
        textFont(font);

        strokeWeight(1);
        stroke(hudRGBA[0], hudRGBA[1], hudRGBA[2]);
        fill(hudRGBA[0], hudRGBA[1], hudRGBA[2]);

        text("PAUSED"
                + "\n"
                + "\n" + "Press \"Enter\" to unpause."
                + "\n" + "Press  \"Escape\" to quit.", 500, 300);
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

        text("You Were Defeated"
                + "\n" + "Your score where " + gameEngine.getCurrentLevel().getPlayer().getScore()
                + "\n"
                + "\n" + "Press \"Space\" to return to Start Menu."
                + "\n" + "Press  \"Escape\" to quit.", 500, 300);
    }

    /**
     * Draw all actors.
     */
    private void drawEntities() {
        // Draw entities.
        for (Actor actor : (CopyOnWriteArrayList<Actor>) gameEngine.getCurrentLevel().getActors().clone()) {
            if (actor != null) {
                actor.draw();
            }
        }
    }

    /**
     * Detect key press events.
     */
    @Override
    public void keyPressed() {
        gameEngine.userInput(keyCode, true);
    }

    /**
     * Detect key release events.
     */
    @Override
    public void keyReleased() {
        gameEngine.userInput(keyCode, false);
    }

    /**
     * Detect mouse press events.
     */
    @Override
    public void mousePressed() {
        gameEngine.userInput(mouseButton, true);
    }

    /**
     * Detect mouse release events.
     */
    @Override
    public void mouseReleased() {
        gameEngine.userInput(mouseButton, false);
    }

    // Getters.
    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    public int getOuterWallThickness() {
        return outerWallThickness;
    }

}
