package userinterface;

import backend.main.GameEngine;
import backend.main.Timer;
import backend.actor.Actor;
import backend.shipmodule.ShipModule;

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
    private PFont hudFont;
    private PFont menuFont;
    private String playerHP;

    // Decimal formats.
    private DecimalFormat format1 = new DecimalFormat("0");
    private DecimalFormat format2 = new DecimalFormat("00");
    private DecimalFormat format3 = new DecimalFormat("000");
    private DecimalFormat format4 = new DecimalFormat("0.0");
    private DecimalFormat format5 = new DecimalFormat("00.0");
    private DecimalFormat format6 = new DecimalFormat("000.0");
    private DecimalFormat format7 = new DecimalFormat("00.0000");

    private GameEngine gameEngine;
    private Timer timer;

    /**
     * Processing initial setup.
     */
    @Override
    public void setup() {
        timer = new Timer();
        // Font sizes.
        hudFont = createFont("Arial", 16, true);
        menuFont = createFont("Arial", 20, true);
        frameRate(60);
        cursor(CROSS);
        keyRepeatEnabled = true;   // Needed for enhanced keyboard input reading.
        gameEngine = new GameEngine(this);
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

        playerHP = format2.format(gameEngine.getCurrentLevel().getPlayer().getHitPoints());

        strokeWeight(1);
        stroke(hudRGBA[0], hudRGBA[1], hudRGBA[2]);
        fill(hudRGBA[0], hudRGBA[1], hudRGBA[2]);

        textFont(hudFont);
        text("HP: " + playerHP, 14, 38);
        text("Score: " + gameEngine.getCurrentLevel().getPlayer().getScore(), 14, 58);
    }

    /**
     * Draws the debugging HUD.
     */
    private void drawDebugHud() {

        strokeWeight(1);
        stroke(debugHudRGBA[0], debugHudRGBA[1], debugHudRGBA[2]);
        fill(debugHudRGBA[0], debugHudRGBA[1], debugHudRGBA[2]);

        textFont(hudFont);
        text("DEBUG INFO"
                + "\n"
                + "\n" + "FPS: " + format3.format((int) frameRate)
                + "\n"
                + "\n" + "Active actors: " + format3.format(gameEngine.getCurrentLevel().getActors().size())
                + "\n" + "Active enemies: " + format3.format(gameEngine.getCurrentLevel().getEnemies().size())
                + "\n" + "Active projectiles: " + format3.format(gameEngine.getCurrentLevel().getProjectiles().size())
                + "\n"
                + "\n" + "Player posX: " + format7.format(gameEngine.getCurrentLevel().getPlayer().getPositionX()) + " pixel"
                + "\n" + "Player posY: " + format7.format(gameEngine.getCurrentLevel().getPlayer().getPositionY()) + " pixel"
                + "\n" + "Player speedT: " + format7.format(gameEngine.getCurrentLevel().getPlayer().getSpeedT()) + " pixel/ms"
                + "\n" + "Player heading: " + format7.format(gameEngine.getCurrentLevel().getPlayer().getHeading()) + " rad"
                + "\n" + "Player course: " + format7.format(gameEngine.getCurrentLevel().getPlayer().getCourse()) + " rad", width - 500, 100);
    }

    /**
     * Draws the start screen.
     */
    private void drawStartScreen() {

        strokeWeight(1);
        stroke(hudRGBA[0], hudRGBA[1], hudRGBA[2]);
        fill(hudRGBA[0], hudRGBA[1], hudRGBA[2]);

        textFont(menuFont);
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

        strokeWeight(1);
        stroke(hudRGBA[0], hudRGBA[1], hudRGBA[2]);
        fill(hudRGBA[0], hudRGBA[1], hudRGBA[2]);

        textFont(menuFont);
        text("PAUSED"
                + "\n"
                + "\n" + "Press \"Enter\" to unpause."
                + "\n" + "Press  \"Escape\" to quit.", 500, 300);
    }

    /**
     * Draws the death screen.
     */
    private void drawDeathScreen() {

        strokeWeight(1);
        stroke(deathScreenRGBA[0], deathScreenRGBA[1], deathScreenRGBA[2]);
        fill(deathScreenRGBA[0], deathScreenRGBA[1], deathScreenRGBA[2]);

        textFont(menuFont);
        text("You Were Defeated"
                + "\n" + "Your score where " + gameEngine.getCurrentLevel().getPlayer().getScore()
                + "\n"
                + "\n" + "Press \"Space\" to return to Start Menu."
                + "\n" + "Press  \"Escape\" to quit.", 500, 300);
    }

    /**
     * Draw all actors and modules.
     */
    private void drawActors() {

        for (Actor actor : gameEngine.getCurrentLevel().getActors()) {
            if (actor != null) {
                actor.draw();

                for (ShipModule shipModule : actor.getShipModules()) {
                    if (shipModule != null) {
                        shipModule.draw();

                    }
                }
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
