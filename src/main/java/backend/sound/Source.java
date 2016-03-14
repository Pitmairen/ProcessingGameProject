package backend.sound;

/**
 * Represents a audio source
 *
 * @author pitmairen
 */
public interface Source {

    /**
     * Plays the source at the given position.
     *

     */
    public void play();

    /**
     * Stops the sound.
     */
    public void stop();

    /**
     * Pause the sound.
     */
    public void pause();
    
    
    /**
     * Set the position of the source
     * 
     * @param x x-position (range: -1.0 - 1.0)
     * @param y y-position (range: -1.0 - 1.0)
     * @param z z-position (range: -1.0 - 1.0)
     */
    public void setPosition(float x, float y, float z);
    
    /**
     * Set the volume of the source
     * 
     * @param volume the volume (range: 0.0 - 1.0)
     */
    public void setVolume(float volume);
}
