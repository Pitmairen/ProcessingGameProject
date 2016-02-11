package userinterface;

import backend.main.GameEngine;
import backend.main.Timer;
import backend.actor.Actor;
import backend.main.Vector;
import static java.awt.event.KeyEvent.*;

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
    private PFont hudFont;

    // Debug HUD.
    private int[] debugHudRGBA = new int[]{255, 255, 255, 255};
    private PFont debugHUDFont;

    // Main menu.
    private int mainMenuOuterBoxThickness = 5;
    private int[] mainMenuOuterBoxRGBA = new int[]{153, 153, 253, 255};
    private int[] mainMenuBackgroundRGBA = new int[]{0, 0, 0, 255};
    private int[] mainMenuRGBA = new int[]{153, 153, 253, 255};
    private PFont menuFont;

    // Pause screen.
    private int[] pauseScreenRGBA = new int[]{40, 160, 30, 255};

    // Death screen.
    private int[] deathScreenRGBA = new int[]{200, 50, 40, 255};

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

    private boolean debugMode = true;

    /**
     * Processing initial setup.
     */
    @Override
    public void setup() {
        timer = new Timer();
        // Font sizes.
        hudFont = createFont("Arial", 22, true);
        debugHUDFont = createFont("Arial", 16, true);
        menuFont = createFont("Arial", 28, true);
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
        fullScreen(P3D);
        // size(1920, 1080, P3D);
    }

    /**
     * Processing draw loop.
     */
    @Override
    public void draw() {

        Vector playerPos = gameEngine.getCurrentLevel().getPlayer().getPosition();

        gameEngine.run(timer.timePassed());
        timer.restart();

        switch (gameEngine.getSimulationState()) {

            case "menuScreen": {
                drawStartScreen();
                break;
            }

            case "gameplay": {
                
                camera((float)playerPos.getX(), (float)playerPos.getY(), 1000.0f,
                        (float)playerPos.getX(), (float)playerPos.getY(), 0.0f, 
                       0.0f, 1.0f, 0.0f);

                drawOuterWalls();
                drawActors();
                drawHUD((float)playerPos.getX() - width/2 + 100, (float)playerPos.getY() - height/2 + 100);
                break;
            }

            case "pauseScreen": {
                drawOuterWalls();
                drawActors();
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
        if (debugMode) {
            
            if(gameEngine.getSimulationState().equals("gameplay")){
                drawDebugHud((float)playerPos.getX() + width/2 - 500, (float)playerPos.getY() - height/2 + 100);
            }else{
                drawDebugHud();
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
        rect(-1000 + outerWallThickness / 2,
                -1000 + outerWallThickness / 2,
                2000 + width - outerWallThickness,
                2000 + height - outerWallThickness);
    }

    /**
     * Draws the HUD.
     */
    private void drawHUD(float x, float y) {

        strokeWeight(1);
        stroke(hudRGBA[0], hudRGBA[1], hudRGBA[2]);
        fill(hudRGBA[0], hudRGBA[1], hudRGBA[2]);

        textFont(hudFont);
        text("Level: " + gameEngine.getCurrentLevel().getLevelName()
                + "\n"
                + "\n" + "Current Wave: " + gameEngine.getCurrentLevel().getCurrentWave()
                + "\n" + "Next Wave: " + format4.format(gameEngine.getCurrentLevel().getTimeToNextWave() / 1000)
                + "\n"
                + "\n" + "Kill chain: " + gameEngine.getCurrentLevel().getPlayer().getKillChain()
                + "\n" + "Score: " + format1.format(gameEngine.getCurrentLevel().getPlayer().getScore()), x, y);
    }
    
    /**
     * Draws the HUD.
     */
    private void drawHUD() {
        drawHUD(14, 40);
    }
    
    
    /**
     * Draws the debugging HUD.
     */
    private void drawDebugHud(float x, float y) {
    Vector p = gameEngine.getCurrentLevel().getPlayer().getPosition();

        strokeWeight(1);
        stroke(debugHudRGBA[0], debugHudRGBA[1], debugHudRGBA[2]);
        fill(debugHudRGBA[0], debugHudRGBA[1], debugHudRGBA[2]);

        textFont(debugHUDFont);
        text("FPS: " + format2.format((int) frameRate)
                + "\n"
                + "\n" + "Total actors: " + format1.format(gameEngine.getCurrentLevel().getActors().size())
                + "\n" + "enemies: " + format1.format(gameEngine.getCurrentLevel().getEnemies().size())
                + "\n" + "projectiles: " + format1.format(gameEngine.getCurrentLevel().getProjectiles().size())
                + "\n" + "items: " + format1.format(gameEngine.getCurrentLevel().getItems().size())
                + "\n"
                + "\n" + "posX: " + format5.format(gameEngine.getCurrentLevel().getPlayer().getPosition().getX())
                + "\n" + "posY: " + format5.format(gameEngine.getCurrentLevel().getPlayer().getPosition().getY())
                + "\n" + "speed: " + format10.format(gameEngine.getCurrentLevel().getPlayer().getSpeedT().mag())
                + "\n" + "heading: " + format7.format(gameEngine.getCurrentLevel().getPlayer().getHeading().getAngle2D()) + " rad"
                + "\n" + "course: " + format7.format(gameEngine.getCurrentLevel().getPlayer().getSpeedT().getAngle2D()) + " rad", x, y);
    }
    /**
     * Draws the debugging HUD.
     */
    private void drawDebugHud() {
        drawDebugHud(width - 500, 100);
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
                + "\n" + "Press \"LMB\" to start."
                + "\n"
                + "\n"
                + "\n" + "Movement: E, S, D, F"
                + "\n" + "Activate offensive: LMB"
                + "\n" + "Activate defensive: RMB"
                + "\n" + "Activate tactical: SPACE"
                + "\n" + "Cycle primary: W"
                + "\n" + "Cycle secondary: R"
                + "\n" + "Pause: TAB"
                + "\n" + "Quit: ESC"
                + "\n"
                + "\n" + "Debug HUD: Z"
                + "\n" + "Spawn enemies: Q", width / 2 - 300, 300);
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
                + "\n" + "Press \"TAB\" to unpause.", width / 2 - 300, 300);
    }

    /**
     * Draws the death screen.
     */
    private void drawDeathScreen() {

        strokeWeight(1);
        stroke(deathScreenRGBA[0], deathScreenRGBA[1], deathScreenRGBA[2]);
        fill(deathScreenRGBA[0], deathScreenRGBA[1], deathScreenRGBA[2]);
        textFont(menuFont);
        
        String hitByName = "N/A";
        Actor lastHitBy = gameEngine.getCurrentLevel().getPlayer().getWhoHitMeLast();
        if(lastHitBy != null){
            hitByName = lastHitBy.getName();
        }
        
        text("You Were Defeated By " + hitByName
                + "\n" + "Press \"ENTER\" to return to Start Menu."
                + "\n"
                + "\n"
                + "\n" + "Your score where " + gameEngine.getCurrentLevel().getPlayer().getScore(), width / 2 - 300, 300);
    }

    /**
     * Draw all actors.
     */
    private void drawActors() {
        gameEngine.getFadingCanvas().draw();
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
        if (keyCode == VK_Z) {
            this.toggleDebugMode();
        }
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

    /**
     * Toggles debug mode.
     */
    private void toggleDebugMode() {
        if (debugMode == false) {
            debugMode = true;
        } else {
            debugMode = false;
        }
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
