package userinterface;

import backend.GameEngine;
import backend.Timer;
import backend.actor.Actor;

import java.text.DecimalFormat;
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
    private int[] outerWallsRGBA = new int[]{153, 153, 253, 255};
    private int[] backgroundRGBA = new int[]{0, 0, 0, 255};

    // HUD.
    private int[] hudRGBA = new int[]{80, 150, 40, 255};
    private int[] deathScreenRGBA = new int[]{200, 50, 40, 255};
    private int[] debugHudRGBA = new int[]{255, 255, 255, 255};

    DecimalFormat format1 = new DecimalFormat("0");
    DecimalFormat format2 = new DecimalFormat("00");
    DecimalFormat format3 = new DecimalFormat("000");
    DecimalFormat format4 = new DecimalFormat("0.0");
    DecimalFormat format5 = new DecimalFormat("00.0");
    DecimalFormat format6 = new DecimalFormat("000.0");

    private GameEngine gameEngine;
    private Timer timer;

    /**
     * Processing initial setup.
     */
    @Override
    public void setup() {
        timer = new Timer();
        frameRate(60);
        cursor(CROSS);
        gameEngine = new GameEngine(this);
    }

    /**
     * Processing rendering settings.
     */
    @Override
    public void settings() {
        fullScreen();
    }

    /**
     * Processing draw loop.
     */
    @Override
    public void draw() {

        gameEngine.run(timer.timePassed());
        timer.restart();

        switch (gameEngine.getSimulationState()) {

            case "startScreen": {
                drawOuterWalls();
                drawStartScreen();
                break;
            }

            case "gameplay": {
                drawOuterWalls();
                drawActors();
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
                drawActors();
                drawDeathScreen();
                break;
            }
        }
        drawDebugHud();
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
    private void drawHUD() {

        String playerHP = format2.format(gameEngine.getCurrentLevel().getPlayer().getHitPoints());
        PFont font = createFont("Arial", 14, true);
        textFont(font);

        strokeWeight(1);
        stroke(hudRGBA[0], hudRGBA[1], hudRGBA[2]);
        fill(hudRGBA[0], hudRGBA[1], hudRGBA[2]);

        text("HP: " + playerHP, 14, 38);
        text("Score: " + gameEngine.getCurrentLevel().getPlayer().getScore(), 14, 58);
    }

    /**
     * Draws the debugging HUD.
     */
    private void drawDebugHud() {

        String playerSpeed = format2.format(gameEngine.getCurrentLevel().getPlayer().getSpeedT() * 100);
        String playerHeading = format4.format(gameEngine.getCurrentLevel().getPlayer().getHeading());
        String playerCourse = format4.format(gameEngine.getCurrentLevel().getPlayer().getCourse());
        PFont font = createFont("Arial", 14, true);
        textFont(font);

        strokeWeight(1);
        stroke(debugHudRGBA[0], debugHudRGBA[1], debugHudRGBA[2]);
        fill(debugHudRGBA[0], debugHudRGBA[1], debugHudRGBA[2]);

        text("Debug info:"
                + "\n" + "Player speed: " + playerSpeed + " m/s"
                + "\n" + "Heading: " + playerHeading + " rad"
                + "\n" + "Course: " + playerCourse + " rad"
                + "\n" + "Projectiles on screen: " + gameEngine.getCurrentLevel().getProjectiles().size() + "\n"
                + "FPS: " + (int) frameRate, width - 500, 100);
    }

    /**
     * Draws the start screen.
     */
    private void drawStartScreen() {

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
    private void drawMenuScreen() {

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
    private void drawDeathScreen() {

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
    private void drawActors() {
        for (Actor actor : gameEngine.getCurrentLevel().getActors()) {
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
