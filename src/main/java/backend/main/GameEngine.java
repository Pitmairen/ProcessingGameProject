package backend.main;

import backend.actor.Actor;
import backend.level.Level;
import backend.level.LevelTest;
import backend.resources.Image;
import backend.resources.ResourceManager;
import backend.resources.Shader;
import userinterface.GUIHandler;

import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.Iterator;

/**
 * Handles the simulation.
 *
 * @author Kristian Honningsvag.
 */
public class GameEngine {

    private GUIHandler guiHandler;
    private CollisionDetector collisionDetector;
    private Level currentLevel;
    private ExplosionManager explosionManager;
    private RocketManager rocketManager;
    private FadingCanvasItemManager fadingCanvasItems;
    private FadingCanvas fadingCanvas;
    private String simulationState = "menuScreen";
    private ResourceManager resourceManager;

    // Key states.
    private boolean up = false;
    private boolean down = false;
    private boolean left = false;
    private boolean right = false;
    private boolean offensiveModule = false;
    private boolean defensiveModule = false;
    private boolean tacticalModule = false;
    private boolean swapOffensive = false;
    private boolean swapDefensive = false;
    private boolean pause = false;
    private Timer pauseTimer = new Timer();
    private boolean enter = false;
    private boolean spawnEnemies = false;

    /**
     * Constructor.
     *
     * @param guiHandler
     */
    public GameEngine(GUIHandler guiHandler) {

        this.guiHandler = guiHandler;
        collisionDetector = new CollisionDetector(this);

        resourceManager = new ResourceManager(guiHandler);
        loadResources();

        fadingCanvasItems = new FadingCanvasItemManager();
        explosionManager = new ExplosionManager(new ParticleEmitter(resourceManager));
        rocketManager = new RocketManager(resourceManager);
        fadingCanvas = new FadingCanvas(guiHandler, resourceManager);

        fadingCanvas.add(explosionManager);
        fadingCanvas.add(rocketManager);
        fadingCanvas.add(fadingCanvasItems);

        resetLevel();

    }

    /**
     * The main loop of the simulation.
     */
    public void run(double timePassed) {

        switch (simulationState) {

            case "menuScreen": {
                checkUserInput(timePassed);
                break;
            }

            case "gameplay": {
                checkUserInput(timePassed);
                cleanup(timePassed);
                actAll(timePassed);
                break;
            }

            case "pauseScreen": {
                checkUserInput(timePassed);
                break;
            }

            case "deathScreen": {
                checkUserInput(timePassed);
                cleanup(timePassed);
                actAll(timePassed);
                break;
            }
        }
    }

    /**
     * Remove dead actors.
     */
    private void cleanup(double timePassed) {

        this.explosionManager.update(timePassed);

        // Remove dead actors.
        ArrayList<Actor> deadActors = new ArrayList<Actor>();
        Iterator<Actor> it = currentLevel.getActors().iterator();

        while (it.hasNext()) {
            Actor actorInList = it.next();
            if (actorInList.getCurrentHitPoints() <= 0) {
                actorInList.die();
                // it.remove();
                deadActors.add(actorInList);
            }
        }
        currentLevel.getActors().removeAll(deadActors);
    }

    /**
     * Make all actors act.
     */
    private void actAll(double timePassed) {

        // Make all actors act.
        for (Actor actor : currentLevel.getActors()) {
            actor.act(timePassed);
        }
        // Spawn the next wave if the timer has run out.
        currentLevel.nextWave();
    }

    /**
     * Handles user input.
     *
     * @param keyCode The button that was pressed.
     * @param keyState Whether the button was pressed or released.
     */
    public void userInput(int keyCode, boolean keyState) {

        if (keyCode == KeyEvent.VK_E) {
            up = keyState;
        }
        if (keyCode == KeyEvent.VK_D) {
            down = keyState;
        }
        if (keyCode == KeyEvent.VK_S) {
            left = keyState;
        }
        if (keyCode == KeyEvent.VK_F) {
            right = keyState;
        }
        if (keyCode == 37) {
            offensiveModule = keyState;
        }
        if (keyCode == 39) {
            defensiveModule = keyState;
        }
        if (keyCode == KeyEvent.VK_SPACE) {
            tacticalModule = keyState;
        }
        if (keyCode == KeyEvent.VK_W) {
            swapOffensive = keyState;
        }
        if (keyCode == KeyEvent.VK_R) {
            swapDefensive = keyState;
        }
        if (keyCode == KeyEvent.VK_TAB) {
            pause = keyState;
        }
        if (keyCode == KeyEvent.VK_ENTER) {
            enter = keyState;
        }
        if (keyCode == KeyEvent.VK_Q) {
            spawnEnemies = keyState;
        }
    }

    /**
     * Checks the user inputs and acts accordingly.
     */
    private void checkUserInput(double timePassed) {

        switch (simulationState) {

            case "menuScreen": {
                if (offensiveModule) {
                    simulationState = "gameplay";
                }
                break;
            }

            case "gameplay": {
                if (up) {
                    currentLevel.getPlayer().accelerate("up", timePassed);
                }
                if (down) {
                    currentLevel.getPlayer().accelerate("down", timePassed);
                }
                if (left) {
                    currentLevel.getPlayer().accelerate("left", timePassed);
                }
                if (right) {
                    currentLevel.getPlayer().accelerate("right", timePassed);
                }
                if (offensiveModule) {
                    currentLevel.getPlayer().activateOffensiveModule();
                }
                if (defensiveModule) {
                    currentLevel.getPlayer().activateDefensiveModule();
                }
                if (tacticalModule) {
                    currentLevel.getPlayer().activateTacticalModule();
                }
                if (swapOffensive) {
                    currentLevel.getPlayer().swapOffensiveModule();
                }
                if (swapDefensive) {
                    currentLevel.getPlayer().swapDefensiveModule();
                }
                if (pause) {
                    if (pauseTimer.timePassed() >= 200) {
                        simulationState = "pauseScreen";
                        pauseTimer.restart();
                    }
                }
                if (spawnEnemies) {
                    currentLevel.getActorSpawner().spawnFrigate(1);
                }
                break;
            }

            case "pauseScreen": {
                if (pause) {
                    if (pauseTimer.timePassed() >= 200) {
                        simulationState = "gameplay";
                        pauseTimer.restart();
                    }
                }
                break;
            }

            case "deathScreen": {
                if (enter) {
                    resetLevel();
                    simulationState = "menuScreen";
                }
                break;
            }

        }
    }

    /**
     * Creates the currentLevel.
     */
    private void resetLevel() {
        currentLevel = new LevelTest(this, rocketManager, fadingCanvasItems);
    }

    private void loadResources() {

        resourceManager.add(Image.PARTICLE, "particle.png");
        resourceManager.add(Image.ROCKET, "particle.png");
        resourceManager.add(Image.LASER_BEAM, "laser.png");
        resourceManager.add(Image.SEEKER_MISSILE, "particle.png");
        resourceManager.add(Image.BACKGROUND_IMAGE, "background.png");
        resourceManager.add(Image.BG_SHADER_NOISE, "shader_noise.png");
        resourceManager.add(Image.SHIELD_NOISE, "shieldNoise.png");
        resourceManager.add(Image.SHIELD_BACKGROUND, "shield.png");
        
        resourceManager.add(Shader.SHIELD_SHADER, "shield.glsl");
        resourceManager.add(Shader.BG_SHADER, "background.glsl");
    }

    // Getters.
    public GUIHandler getGuiHandler() {
        return guiHandler;
    }

    public String getSimulationState() {
        return simulationState;
    }

    public Level getCurrentLevel() {
        return currentLevel;
    }

    public CollisionDetector getCollisionDetector() {
        return collisionDetector;
    }

    public FadingCanvas getFadingCanvas() {
        return fadingCanvas;
    }

    public ExplosionManager getExplosionManager() {
        return explosionManager;
    }

    public ResourceManager getResourceManager() {
        return resourceManager;
    }

    // Setters.
    public void setSimulationState(String simulationState) {
        this.simulationState = simulationState;
    }
}
