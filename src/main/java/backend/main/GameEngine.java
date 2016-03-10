package backend.main;

import backend.actor.Actor;
import backend.level.Level;
import backend.level.Level1;
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
    private SimulationState simulationState = SimulationState.MENU_SCREEN;
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

        soundManager = new SoundManager(guiHandler, resourceManager);
        
        try {
            loadSounds();
        } catch (OpenAL.ALError ex) {
            System.out.println("Failed to load sounds");
            System.exit(1);
        }
        resetLevel();
    }
    
    /**
     * Ends the current game and resets the level.
     */
    public void endCurrentGame(){
        resetLevel();
    }

    /**
     * The main loop of the simulation.
     */
    public void run(double timePassed) {

        checkUserInput(timePassed);
        
        switch (simulationState) {
            case GAMEPLAY: {
                cleanup(timePassed);
                actAll(timePassed);
                break;
            }
            case DEATH_SCREEN: {
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

        if (keyCode == KeyEvent.VK_W) {
            up = keyState;
        }
        if (keyCode == KeyEvent.VK_S) {
            down = keyState;
        }
        if (keyCode == KeyEvent.VK_A) {
            left = keyState;
        }
        if (keyCode == KeyEvent.VK_D) {
            right = keyState;
        }
        if (keyCode == 37) {
            offensiveModule = keyState;
        }
        if (keyCode == KeyEvent.VK_E) {
            defensiveModule = keyState;
        }
        if (keyCode == KeyEvent.VK_SPACE) {
            tacticalModule = keyState;
        }
        if (keyCode == KeyEvent.VK_Q) {
            swapOffensive = keyState;
        }
        if (keyCode == KeyEvent.VK_R) {
            swapDefensive = keyState;
        }
        if (keyCode == KeyEvent.VK_ESCAPE) {
            pause = keyState;
        }
        if (keyCode == KeyEvent.VK_ENTER) {
            enter = keyState;
        }
        if (keyCode == KeyEvent.VK_I) {
            spawnEnemies = keyState;
        }
        if (keyCode == KeyEvent.VK_M && keyState) {
            soundManager.toggleMuted();
        }
        if (keyCode == KeyEvent.VK_TAB && keyState) {
            if (currentLevel.getInitialTimeToNextWave() > 0) {
                currentLevel.setInitialTimeToNextWave(0);
            }
        }
    }

    /**
     * Checks the user inputs and acts accordingly.
     */
    private void checkUserInput(double timePassed) {

        switch (simulationState) {

            case GAMEPLAY: {
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
                        setSimulationState(SimulationState.PAUSE_SCREEN);
                        guiHandler.showPauseMenu();
                        pauseTimer.restart();
                    }
                }
                if (spawnEnemies) {
                    currentLevel.getActorSpawner().spawnFrigate(1);
                }
                break;
            }

            case DEATH_SCREEN: {
                if (enter) {
                    resetLevel();
                    setSimulationState(SimulationState.MENU_SCREEN);
                    guiHandler.showMainMenu();
                }
                break;
            }
            case HELP_SCREEN: {
                if (enter) {
                    setSimulationState(SimulationState.MENU_SCREEN);
                    guiHandler.showMainMenu();
                }
                break;
            }
            case HELP_SCREEN_PAUSED: {
                if (enter) {
                    setSimulationState(SimulationState.PAUSE_SCREEN);
                    guiHandler.showPauseMenu();
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
        rocketManager.clear();
        currentLevel = new LevelTest(this, rocketManager, fadingCanvasItems);
        soundManager.stop(Sound.GAME_MUSIC);
    }

    private void loadResources() {

        resourceManager.add(Image.PARTICLE, "particle.png");
        resourceManager.add(Image.ROCKET, "particle.png");
        resourceManager.add(Image.LASER_BEAM, "laser.png");
        resourceManager.add(Image.SEEKER_MISSILE, "particle.png");
        resourceManager.add(Image.BACKGROUND_IMAGE, "background.png");
        resourceManager.add(Image.BG_SHADER_NOISE, "shader_noise.png");
        resourceManager.add(Image.SHIELD_MODULE, "ResizedImages/ShieldModuleV2.png");
        resourceManager.add(Image.SHIELD_NOISE, "shieldNoise.png");
        resourceManager.add(Image.SHIELD_BACKGROUND, "shield.png");
        resourceManager.add(Image.DRONE_LAUNCHER, "HighResAssets/DroneLauncher.png");
        resourceManager.add(Image.ROCKET_LAUNCHER, "HighResAssets/RocketLauncherV2.png");
        resourceManager.add(Image.SEEKER_CANNON, "ResizedImages/BombLauncherRed.png");
        resourceManager.add(Image.EMP_CANNON, "ResizedImages/BombLauncherV2.png");
        resourceManager.add(Image.LIGHT_CANNON, "ResizedImages/LightCannonV2.png");
        resourceManager.add(Image.LASER_CANNON, "ResizedImages/Laser.png");

        resourceManager.add(Shader.SHIELD_SHADER, "shield.glsl");
        resourceManager.add(Shader.BG_SHADER, "background.glsl");

        resourceManager.add(Sound.EXPLOSION, "audio/sfx/explosion01.wav");
        resourceManager.add(Sound.BOSS_DEATH, "audio/sfx/explosion02.wav");
        resourceManager.add(Sound.BULLET_IMPACT, "audio/sfx/explosion03.wav");
        resourceManager.add(Sound.CURSOR, "audio/sfx/cursor.wav");
        resourceManager.add(Sound.CURSOR2, "audio/sfx/cursor2.wav");
        
        resourceManager.add(Sound.AUTO_CANNON, "audio/sfx/fire.wav");
        resourceManager.add(Sound.LASER, "audio/sfx/laser3.wav");
        resourceManager.add(Sound.COLLISION, "audio/sfx/collision.wav");
        resourceManager.add(Sound.EMP, "audio/sfx/emp01.wav");
        resourceManager.add(Sound.GAMEOVER, "audio/sfx/gameover.wav");
        resourceManager.add(Sound.MISSILE_LAUNCH, "audio/sfx/missile_launch1.wav");
        resourceManager.add(Sound.MISSILE_EXPLOSION, "audio/sfx/explosion04.wav");
        resourceManager.add(Sound.POWERUP, "audio/sfx/powerup.wav");
        resourceManager.add(Sound.PICKUP, "audio/sfx/pickup.wav");
        resourceManager.add(Sound.ACTIVATE_SHIELD, "audio/sfx/pickup.wav");
        resourceManager.add(Sound.HEALTH_PICKUP, "audio/sfx/health.wav");
        resourceManager.add(Sound.GAME_MUSIC, "audio/sfx/placeholdertune.WAV");
        resourceManager.add(Sound.GAMEOVER, "audio/sfx/gameover.wav");

    }
    
    private void loadSounds() throws OpenAL.ALError {

        soundManager.addSound(Sound.EXPLOSION, 5);              //to be played when an actor dies
        soundManager.addLoopingSound(Sound.LASER);              //laser weapon sfx
        soundManager.addSound(Sound.AUTO_CANNON, 5);            //default weapon module sound
        soundManager.addSound(Sound.COLLISION, 1);              //player bumping into objects
        soundManager.addSound(Sound.CURSOR, 5);                 //moving cursor on the menus
        soundManager.addSound(Sound.CURSOR2, 5);                //moving cursor on the menus
        soundManager.addSound(Sound.GAMEOVER, 1);
        
        soundManager.addLoopingSound(Sound.GAME_MUSIC);         //game tune

        soundManager.addSound(Sound.EMP, 5);
        soundManager.addSound(Sound.PICKUP, 5);                 //picking up power up modules
        soundManager.addSound(Sound.POWERUP, 5);                //swapping between powerup modules
        soundManager.addSound(Sound.BOSS_DEATH, 5);            //to be played when a large drone dies
        soundManager.addSound(Sound.GAMEOVER, 5);               //gameover tune, plays when player dies
        soundManager.addSound(Sound.MISSILE_LAUNCH, 5);         //missile regular/heat seeking launching
        soundManager.addSound(Sound.MISSILE_EXPLOSION, 5);      //exploding missile
        soundManager.addSound(Sound.ACTIVATE_SHIELD, 1);       //activate the shield
        soundManager.addSound(Sound.BULLET_IMPACT, 5);          //sound of default weapon projectiles hitting enemy
        soundManager.addSound(Sound.HEALTH_PICKUP, 5);          //sound of picking up health orbs
    }
    
    // Getters.
    public GUIHandler getGuiHandler() {
        return guiHandler;
    }

    public SimulationState getSimulationState() {
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
    public void setSimulationState(SimulationState simulationState) {
        this.simulationState = simulationState;
        updateMusic(simulationState);
    }
    
    private void updateMusic(SimulationState newState){

        switch(newState){
            
            case GAMEPLAY:
                soundManager.play(Sound.GAME_MUSIC, new Vector(guiHandler.width/2, guiHandler.height/2, 0));
                break;
            case PAUSE_SCREEN:
                soundManager.pause(Sound.GAME_MUSIC);
                break;
            case MENU_SCREEN:
            case DEATH_SCREEN:
                soundManager.stop(Sound.GAME_MUSIC);
                break;
        }
        
    }
}
