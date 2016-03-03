package userinterface;

import backend.main.GameEngine;
import backend.main.Timer;
import backend.actor.Actor;
import backend.main.SimulationState;
import static java.awt.event.KeyEvent.*;

import java.text.DecimalFormat;
import processing.core.PApplet;
import processing.core.PFont;
import processing.core.PImage;

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
    private int[] deathScreenRGBA = new int[]{255, 50, 40, 255};

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

    private Menu mainMenu;

    private boolean debugMode = false;
    
    private PImage titleScreenImage;

    /**
     * Processing initial setup.
     */
    @Override
    public void setup() {
        timer = new Timer();
        // Font sizes.
        hudFont = createFont("hudFont.otf", 20, true);
        debugHUDFont = createFont("hudFont.otf", 16, true);
        menuFont = createFont("hudFont.otf", 28, true);
        frameRate(60);
        cursor(CROSS);
        
        keyRepeatEnabled = true;   // Needed for enhanced keyboard input reading.
        
        String OS = System.getProperty("os.name");
        if(OS.startsWith("Windows")){
            keyRepeatEnabled = false; //Needed to fix input error on windows
        }
        
        gameEngine = new GameEngine(this);

        mainMenu = new Menu("Xeno Blaster 4000", this);
        createMenuItems();
        mainMenu.show();
        titleScreenImage = loadImage("titleScreen.png");
    }

    /**
     * Processing rendering settings.
     */
    @Override
    public void settings() {
        fullScreen(P3D);
        //size(1920, 1080, P3D);
    }

    /**
     * Processing draw loop.
     */
    @Override
    public void draw() {
        
        gameEngine.run(timer.timePassed());
        timer.restart();

        switch (gameEngine.getSimulationState()) {

            case MENU_SCREEN: {
                gameEngine.getFadingCanvas().draw();
                // The menu draws itself.
                drawTitleScreenImage();
                break;
            }
            case HELP_SCREEN: {
                gameEngine.getFadingCanvas().draw();
                drawHelpScreen();
                break;
            }
            case GAMEPLAY: {
                gameEngine.getFadingCanvas().draw();
                drawOuterWalls();
                drawActors();
                drawHUD();
                break;
            }
            case PAUSE_SCREEN: {
                gameEngine.getFadingCanvas().draw();
                drawOuterWalls();
                drawActors();
                drawPauseScreen();
                break;
            }
            case DEATH_SCREEN: {
                gameEngine.getFadingCanvas().draw();
                drawOuterWalls();
                drawActors();
                drawDeathScreen();
                break;
            }
        }
        if (debugMode) {
            drawDebugHud();
        }
    }

    /**
     * Show the main menu
     */
    public void showMainMenu() {
        mainMenu.show();
    }

    /**
     * Draws the outer walls.
     */
    private void drawOuterWalls() {
        stroke(outerWallsRGBA[0], outerWallsRGBA[1], outerWallsRGBA[2]);
        strokeWeight(outerWallThickness);
        noFill();
        rect(0 + outerWallThickness / 2, 0 + outerWallThickness / 2,
                width - outerWallThickness, height - outerWallThickness);
    }

    /**
     * Draws the HUD.
     */
    private void drawHUD() {
        fill(hudRGBA[0], hudRGBA[1], hudRGBA[2]);
        textFont(hudFont);
        textLeading(22);
        textAlign(LEFT, TOP);
        text("Level: " + gameEngine.getCurrentLevel().getLevelName()
                + "\n"
                + "\n" + "Current Wave: " + gameEngine.getCurrentLevel().getCurrentWave()
                + "\n" + "Next Wave: " + format4.format(gameEngine.getCurrentLevel().getTimeToNextWave() / 1000)
                + "\n"
                + "\n" + "Kill chain: " + gameEngine.getCurrentLevel().getPlayer().getKillChain()
                + "\n" + "Score: " + format1.format(gameEngine.getCurrentLevel().getPlayer().getScore()), 20, 20);
        
    }

    /**
     * Draws the debugging HUD.
     */
    private void drawDebugHud() {
        fill(debugHudRGBA[0], debugHudRGBA[1], debugHudRGBA[2]);
        textFont(debugHUDFont);
        textLeading(18);
        textAlign(LEFT, TOP);
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
                + "\n" + "course: " + format7.format(gameEngine.getCurrentLevel().getPlayer().getSpeedT().getAngle2D()) + " rad", width - 200, 20);
    }

    /**
     * Draws the start screen.
     */
    private void drawStartMenu() {
//        // Background.
//        strokeWeight(mainMenuOuterBoxThickness);
//        stroke(mainMenuOuterBoxRGBA[0], mainMenuOuterBoxRGBA[1], mainMenuOuterBoxRGBA[2]);
//        fill(mainMenuBackgroundRGBA[0], mainMenuBackgroundRGBA[1], mainMenuBackgroundRGBA[2]);
//        rect(0 + mainMenuOuterBoxThickness / 2,
//                0 + mainMenuOuterBoxThickness / 2,
//                width - mainMenuOuterBoxThickness,
//                height - mainMenuOuterBoxThickness);
        // Text.
        fill(mainMenuRGBA[0], mainMenuRGBA[1], mainMenuRGBA[2]);
        textFont(menuFont);
        textAlign(CENTER, CENTER);
        text("Xeno Blaster 4000"
                + "\n"
                + "\n" + "Press \"LMB\" to start.", width / 2, height / 2 - 100);
    }
    
    /**
     * Draws the image for the start screen
     */
    private void drawTitleScreenImage(){
        image(titleScreenImage, 0, 0, width, height);
    }
    
    /**
     * Draws the help screen.
     */
    private void drawHelpScreen() {
        fill(pauseScreenRGBA[0], pauseScreenRGBA[1], pauseScreenRGBA[2]);
        textFont(menuFont);
        textAlign(CENTER, CENTER);
        text("Movement: E, S, D, F"
                + "\n" + "Activate offensive: LMB"
                + "\n" + "Activate defensive: RMB"
                + "\n" + "Activate tactical: SPACE"
                + "\n" + "Cycle offensive: W"
                + "\n" + "Cycle defensive: R"
                + "\n" + "Pause: TAB"
                + "\n" + "Quit: ESC"
                + "\n"
                + "\n" + "Toggle Sounds: M"
                + "\n" + "Debug HUD: Z"
                + "\n" + "Spawn enemies: Q", width / 2, height / 2 - 100);
    }

    /**
     * Draws the pause screen.
     */
    private void drawPauseScreen() {
        fill(pauseScreenRGBA[0], pauseScreenRGBA[1], pauseScreenRGBA[2]);
        textFont(menuFont);
        textAlign(CENTER, CENTER);
        text("PAUSED"
                + "\n"
                + "\n" + "Movement: E, S, D, F"
                + "\n" + "Activate offensive: LMB"
                + "\n" + "Activate defensive: RMB"
                + "\n" + "Activate tactical: SPACE"
                + "\n" + "Cycle offensive: W"
                + "\n" + "Cycle defensive: R"
                + "\n" + "Pause: TAB"
                + "\n" + "Quit: ESC"
                + "\n"
                + "\n" + "Toggle Sounds: M"
                + "\n" + "Debug HUD: Z"
                + "\n" + "Spawn enemies: Q", width / 2, height / 2 - 100);
    }

    /**
     * Draws the death screen.
     */
    private void drawDeathScreen() {

        String hitByName = "N/A";
        Actor lastHitBy = gameEngine.getCurrentLevel().getPlayer().getWhoHitMeLast();
        if (lastHitBy != null) {
            hitByName = lastHitBy.getName();
        }
        
        //stroke(255);
        //fill(0);
        //rect(width / 4, height / 3, width / 2, height / 7 + height / 100 );
        fill(deathScreenRGBA[0], deathScreenRGBA[1], deathScreenRGBA[2]);
        textFont(menuFont);
        textAlign(CENTER, CENTER);
        text("You Were Defeated By " + hitByName
                + "\n" + "Press \"ENTER\" to return to Start Menu."
                + "\n"
                + "\n" + "Your score: " + gameEngine.getCurrentLevel().getPlayer().getScore(), width / 2, height / 2 - 100);
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
     * Creates the main menu items
     */
    private void createMenuItems() {

        mainMenu.addItem("New Game", () -> {
            mainMenu.hide();
            gameEngine.setSimulationState(SimulationState.GAMEPLAY);
        });
        mainMenu.addItem("Help", () -> {
            mainMenu.hide();
            gameEngine.setSimulationState(SimulationState.HELP_SCREEN);
        });
        
        mainMenu.addItem("Quit", () -> {
            exit();
        });

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
