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

    // Game field.
    private int outerWallThickness = 0;
    private int[] outerWallsRGBA = new int[]{153, 153, 253, 255};
    private int[] backgroundRGBA = new int[]{0, 0, 0, 255};

    // Player HUD.
    private int[] hudRGBA = new int[]{80, 150, 40, 255};

    // Debug HUD.
    private int[] debugHudRGBA = new int[]{255, 255, 255, 255};

    // Main menu.
    private int mainMenuOuterBoxThickness = 5;
    private int[] mainMenuOuterBoxRGBA = new int[]{153, 153, 253, 255};
    private int[] mainMenuBackgroundRGBA = new int[]{0, 0, 0, 255};
    private int[] mainMenuRGBA = new int[]{153, 153, 253, 255};

    // Pause screen.
    private int[] pauseScreenRGBA = new int[]{40, 160, 30, 255};

    // Death screen.
    private int[] deathScreenRGBA = new int[]{200, 50, 40, 255};

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
    private DecimalFormat format7 = new DecimalFormat("0.00");
    private DecimalFormat format8 = new DecimalFormat("00.00");
    private DecimalFormat format9 = new DecimalFormat("000.00");
    private DecimalFormat format10 = new DecimalFormat("0.000");
    private DecimalFormat format11 = new DecimalFormat("00.000");
    private DecimalFormat format12 = new DecimalFormat("000.000");
    private DecimalFormat format13 = new DecimalFormat("0.0000");
    private DecimalFormat format14 = new DecimalFormat("00.0000");
    private DecimalFormat format15 = new DecimalFormat("000.0000");

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
                drawPauseScreen();
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
        text("FPS: " + format2.format((int) frameRate)
                + "\n"
                + "\n" + "Active actors: " + format1.format(gameEngine.getCurrentLevel().getActors().size())
                + "\n" + "Active enemies: " + format1.format(gameEngine.getCurrentLevel().getEnemies().size())
                + "\n" + "Active projectiles: " + format1.format(gameEngine.getCurrentLevel().getProjectiles().size())
                + "\n"
                + "\n" + "Player posX: " + format5.format(gameEngine.getCurrentLevel().getPlayer().getPositionX()) + " pixel"
                + "\n" + "Player posY: " + format5.format(gameEngine.getCurrentLevel().getPlayer().getPositionY()) + " pixel"
                + "\n" + "Player speedT: " + format10.format(gameEngine.getCurrentLevel().getPlayer().getSpeedT()) + " pixel/ms"
                + "\n" + "Player heading: " + format7.format(gameEngine.getCurrentLevel().getPlayer().getHeading()) + " rad"
                + "\n" + "Player course: " + format7.format(gameEngine.getCurrentLevel().getPlayer().getCourse()) + " rad", width - 500, 100);
    }

    /**
     * Draws the start screen.
     */
    private void drawStartScreen() {

        // Background.
        strokeWeight(mainMenuOuterBoxThickness);
        stroke(mainMenuOuterBoxRGBA[0], mainMenuOuterBoxRGBA[1], mainMenuOuterBoxRGBA[2]);
        fill(mainMenuBackgroundRGBA[0], mainMenuBackgroundRGBA[1], mainMenuBackgroundRGBA[2]);
        rect(0 + mainMenuOuterBoxThickness / 2,
                0 + mainMenuOuterBoxThickness / 2,
                width - mainMenuOuterBoxThickness,
                height - mainMenuOuterBoxThickness);

        // Text.
        strokeWeight(1);
        stroke(mainMenuRGBA[0], mainMenuRGBA[1], mainMenuRGBA[2]);
        fill(mainMenuRGBA[0], mainMenuRGBA[1], mainMenuRGBA[2]);
        textFont(menuFont);
        text("Title Screen"
                + "\n" + "Press \"Enter\" to start."
                + "\n"
                + "\n"
                + "\n" + "Acceleration: E, S, D, F"
                + "\n" + "Offensive module: Left mouse button"
                + "\n" + "Cycle between offensive modules: W"
                + "\n" + "Spawn enemies: TAB"
                + "\n" + "Pause: Space"
                + "\n" + "Quit: ESC", width / 2 - 300, 300);
    }

    /**
     * Draws the pause screen.
     */
    private void drawPauseScreen() {

        strokeWeight(1);
        stroke(pauseScreenRGBA[0], pauseScreenRGBA[1], pauseScreenRGBA[2]);
        fill(pauseScreenRGBA[0], pauseScreenRGBA[1], pauseScreenRGBA[2]);
        textFont(menuFont);
        text("PAUSED"
                + "\n" + "Press \"Enter\" to unpause.", width / 2 - 300, 300);
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
                + "\n" + "Press \"Space\" to return to Start Menu."
                + "\n"
                + "\n"
                + "\n" + "Your score where " + gameEngine.getCurrentLevel().getPlayer().getScore(), width / 2 - 300, 300);
    }

    /**
     * Draw all actors and modules.
     */
    private void drawActors() {

        gameEngine.getFadingCanvas().draw();

        for (Actor actor : gameEngine.getCurrentLevel().getActors()) {
            if (actor != null) {
                actor.draw();

                for (ShipModule shipModule : actor.getOffensiveModules()) {
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
