package backend.sound;

import java.util.ArrayList;

/**
 * Manages a pool of reusable sources to make it possible to play the same sound
 * multiple times simultaneously.
 *
 * It creates a circular pool of sources, and each time a sound is played it
 * will use the next available source. When it hits the last source it will wrap
 * around and reuse the first source.
 *
 * @author pitmairen
 */
public class SourcePool implements Source {

    private final OpenAL al;
    private final ArrayList<Integer> sources;
    private int currentPosition;

    /**
     * Create a new source pool
     *
     * @param bufferID the buffer id that she sources will be connected to
     * @param sourceCount the number of sources in the pool
     * @param al the OpenAl object
     *
     * @throws backend.sound.OpenAL.ALError
     */
    public SourcePool(int bufferID, int sourceCount, OpenAL al) throws OpenAL.ALError {
        this.al = al;
        currentPosition = 0;
        sources = new ArrayList<>(sourceCount);
        createPool(bufferID, sourceCount);
    }
    
    /**
     * Play the next available source.
     */
    @Override
    public void play() {
        al.play(sources.get(currentPosition));
        currentPosition = (currentPosition + 1) % sources.size();
    }
    
    /**
     * Stops all the sounds in the pool
     */
    @Override
    public void stop() {
        for(int i : sources){
            al.stop(i);
        }
    }
    
    /**
     * The pool does not support pausing
     */
    @Override
    public void pause() {
        // Not supported
    }
    
    /**
     * Set the position of the current source
     * 
     * @param x x-position (range: -1.0 - 1.0)
     * @param y y-position (range: -1.0 - 1.0)
     * @param z z-position (range: -1.0 - 1.0)
     */
    @Override
    public void setPosition(float x, float y, float z) {
        al.setPosision(sources.get(currentPosition), x, y, z);
    }
    
    private void createPool(int bufferID, int count) throws OpenAL.ALError {
        for (int i = 0; i < count; i++) {
            sources.add(al.addSource(bufferID, false));
        }
    }


}
