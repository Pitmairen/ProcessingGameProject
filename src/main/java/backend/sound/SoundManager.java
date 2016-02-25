package backend.sound;

import backend.main.Vector;
import backend.resources.ResourceManager;
import backend.resources.Sound;
import java.util.HashMap;
import userinterface.GUIHandler;

/**
 * Manages the all the sounds
 *
 * @author pitmairen
 */
public class SoundManager {

    private final OpenAL al;
    private final HashMap<Sound, Source> sounds;
    private final ResourceManager resources;
    private final GUIHandler gui;
    private boolean muted;

    /**
     * Create a new sound manager.
     *
     * It should only be a single instance of this class.
     *
     * @param gui the GUI handler
     * @param resources the resource manager
     */
    public SoundManager(GUIHandler gui, ResourceManager resources) {
        al = new OpenAL();
        sounds = new HashMap<>();
        this.resources = resources;
        this.gui = gui;
        muted = false;
    }

    /**
     * Plays the sound at the specified position
     *
     * @param soundID the sound ID
     * @param pos the position in normal coordinates (e.g: actor.getPosition())
     */
    public void play(Sound soundID, Vector pos) {

        if (muted) {
            return;
        }

        float x = (2 * (float) pos.getX() - gui.width) / gui.width;
        float y = (2 * (float) pos.getY() - gui.height) / gui.height;
        sounds.get(soundID).play(x, y, 0f);
    }
    
    /**
     * Stops the specified sound
     *
     * @param soundID the sound ID
     */
    public void stop(Sound soundID) {
        sounds.get(soundID).stop();
    }
    
    /**
     * Stops the specified sound
     *
     * @param soundID the sound ID
     */
    public void pause(Sound soundID) {
        sounds.get(soundID).pause();
    }
    
    /**
     * Add a new sound to the manager
     *
     * @param soundID the sound ID
     * @param numSimultaneousPlays the number of simultaneous plays that this
     * sound should support
     *
     * @throws backend.sound.OpenAL.ALError
     */
    public void addSound(Sound soundID, int numSimultaneousPlays) throws OpenAL.ALError {
        int bufferID = al.createBuffer(resources.getSound(soundID));
        SourcePool pool = new SourcePool(bufferID, numSimultaneousPlays, al);
        sounds.put(soundID, pool);
    }
    
    /**
     * Add a new sound to the manager
     *
     * @param soundID the sound ID
     *
     * @throws backend.sound.OpenAL.ALError
     */
    public void addLoopingSound(Sound soundID) throws OpenAL.ALError {
        int bufferID = al.createBuffer(resources.getSound(soundID));
        Source src = new LoopingSound(bufferID, al);
        sounds.put(soundID, src);
    }
    
    /**
     * Set the muted state of the sound manager
     *
     * @param muted if true no sound will be played
     */
    public void setMuted(boolean muted) {
        this.muted = muted;
    }

    /**
     * Toggle the muted state of the sound manager
     */
    public void toggleMuted() {
        muted = !muted;
    }
}
