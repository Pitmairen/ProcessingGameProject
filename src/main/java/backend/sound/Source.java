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
     * @param x x-position (range: -1.0 - 1.0)
     * @param y y-position (range: -1.0 - 1.0)
     * @param z z-position (range: -1.0 - 1.0)
     */
    public void play(float x, float y, float z);

    /**
     * Stops the sound.
     */
    public void stop();

    /**
     * Pause the sound.
     */
    public void pause();
}
