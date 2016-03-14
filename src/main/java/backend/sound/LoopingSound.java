package backend.sound;

/**
 * A looping audio source.
 *
 * @author pitmairen
 */
public class LoopingSound implements Source {

    private final OpenAL al;
    private final int sourceID;

    /**
     * Create a new source
     *
     * @param bufferID the buffer id that she sources will be connected to
     * @param al the OpenAl object
     *
     * @throws backend.sound.OpenAL.ALError
     */
    public LoopingSound(int bufferID, OpenAL al) throws OpenAL.ALError {
        this.al = al;
        sourceID = al.addSource(bufferID, true);
    }

    /**
     * Play the source at the specified position.
     */
    @Override
    public void play() {
        al.play(sourceID);
    }

    /**
     * Stops the sound.
     */
    @Override
    public void stop() {
        al.stop(sourceID);
    }

    /**
     * Pause the sound.
     */
    @Override
    public void pause() {
        al.pause(sourceID);
    }
    
    /**
     * Set the position of the sound
     * 
     * @param x x-position (range: -1.0 - 1.0)
     * @param y y-position (range: -1.0 - 1.0)
     * @param z z-position (range: -1.0 - 1.0)
     */
    @Override
    public void setPosition(float x, float y, float z) {
        al.setPosision(sourceID, x, y, z);
    }
    
    /**
     * Set the volume of the source
     * 
     * @param volume the volume (range: 0.0 - 1.0)
     */
    @Override
    public void setVolume(float volume){
        al.setGain(sourceID, volume);
    }
}
