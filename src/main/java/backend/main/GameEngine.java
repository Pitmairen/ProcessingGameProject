package backend.main;

import backend.actor.Actor;
import backend.level.Level;
import backend.level.LevelTest;
import backend.resources.Image;
import backend.resources.ResourceManager;
import backend.resources.Shader;
import backend.resources.Sound;
import backend.sound.OpenAL;
import backend.sound.SoundManager;
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
    private SoundManager soundManager;

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

        
        soundManager = new SoundManager(guiHandler, resourceManager);
        
        try {
            loadSounds();
        } catch (OpenAL.ALError ex) {
            System.out.println("Failed to load sounds");
            System.exit(1);
        }
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
            
            case "helpScreen": {
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
        if (keyCode == KeyEvent.VK_M && keyState) {
            soundManager.toggleMuted();
        }
    }

    /**
     * Checks the user inputs and acts accordingly.
     */
    private void checkUserInput(double timePassed) {

        switch (simulationState) {

            case "menuScreen": {
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
                        setSimulationState("pauseScreen");
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
                        setSimulationState("gameplay");
                        pauseTimer.restart();
                    }
                }
                break;
            }

            case "deathScreen": {
                if (enter) {
                    resetLevel();
                    setSimulationState("menuScreen");
                    guiHandler.showMainMenu();
                }
                break;
            }
            case "helpScreen": {
                if (enter) {
                    setSimulationState("menuScreen");
                    guiHandler.showMainMenu();
                }
                break;
            }
        }
    }

    /**
     * Creates the currentLevel.
     */
    private void resetLevel() {
        fadingCanvasItems.clear();
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
        resourceManager.add(Image.DRONE_LAUNCHER, "HighResAssets/DroneLauncher.png");
        resourceManager.add(Image.ROCKET_LAUNCHER, "HighResAssets/RocketLauncherV2.png");

        resourceManager.add(Shader.SHIELD_SHADER, "shield.glsl");
        resourceManager.add(Shader.BG_SHADER, "background.glsl");

        resourceManager.add(Sound.EXPLOSION, "audio/sfx/death.wav");
        resourceManager.add(Sound.AUTO_CANNON, "audio/sfx/fire.wav");
        resourceManager.add(Sound.LASER, "audio/sfx/laser3.wav");
        resourceManager.add(Sound.COLLISION, "audio/sfx/collision.wav");
        resourceManager.add(Sound.CURSOR, "audio/sfx/cursor.wav");
        resourceManager.add(Sound.CURSOR2, "audio/sfx/cursor2.wav");
//        resourceManager.add(Sound.EMP, "audio/sfx/emp02.wav");
//        resourceManager.add(Sound.DEATH, "audio/sfx/death.wav");
//        resourceManager.add(Sound.GAMEOVER, "audio/sfx/lose.wav");

        resourceManager.add(Sound.GAME_MUSIC, "audio/sfx/placeholdertune.WAV");
    }
    
    private void loadSounds() throws OpenAL.ALError {

        soundManager.addSound(Sound.EXPLOSION, 5);
        soundManager.addLoopingSound(Sound.LASER);
        soundManager.addSound(Sound.AUTO_CANNON, 5);
        soundManager.addSound(Sound.COLLISION, 1);
        soundManager.addSound(Sound.CURSOR, 5);
        soundManager.addSound(Sound.CURSOR2, 5);
        soundManager.addLoopingSound(Sound.GAME_MUSIC);
        
//        soundManager.addSound(Sound.EMP, 5);
//        soundManager.addSound(Sound.DEATH, 5);
//        soundManager.addSound(Sound.GAMEOVER, 5);
//        soundManager.addSound(Sound.MISSILE_LAUNCH, 5);
//        soundManager.addSound(Sound.MISSILEMOD_LOAD, 5);
//        soundManager.addSound(Sound.GAMEOVER, 5);
//        soundManager.addSound(Sound.MISSILE_LAUNCH, 5);
//        soundManager.addSound(Sound.MISSILE_EXPLOSION, 5);
//        soundManager.addSound(Sound.ENEMY_SPAWN, 5);
//        soundManager.addSound(Sound.CANNONMOD_LOAD, 5);
//        soundManager.addSound(Sound.LASERMOD_LOAD, 5);
//        soundManager.addSound(Sound.BULLET_IMPACT, 5);
//        soundManager.addSound(Sound.MISSILE_IMPACT, 5);
//        soundManager.addSound(Sound.MODULE_PICKUP, 5);
//        soundManager.addSound(Sound.TACTICALMOD_LOAD, 5);

   
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
    
    public SoundManager getSoundManager() {
        return soundManager;
    }
    
    // Setters.
    public void setSimulationState(String simulationState) {
        this.simulationState = simulationState;
        updateMusic(simulationState);
    }
    
    private void updateMusic(String newState){

        switch(newState){
            
            case "gameplay":
                soundManager.play(Sound.GAME_MUSIC, new Vector(guiHandler.width/2, guiHandler.height/2, 0));
                break;
            case "pauseScreen":
                soundManager.pause(Sound.GAME_MUSIC);
                break;
           case "deathScreen":
                soundManager.stop(Sound.GAME_MUSIC);
                break;
        }
        
    }
}
