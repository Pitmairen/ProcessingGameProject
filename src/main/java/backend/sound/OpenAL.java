package backend.sound;

import com.jogamp.openal.AL;
import com.jogamp.openal.ALFactory;
import com.jogamp.openal.util.ALut;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

/**
 * Wrapper around the OpenAL sound system-
 *
 * @author pitmairen
 */
public class OpenAL {

    private final AL al;

    // List of all the loaded buffers. 
    // A buffer coresponds to a single file.
    private final ArrayList<Integer> buffers = new ArrayList<>();
    
    // Stores the individual gain value for each source 
    private final HashMap<Integer, Float> sourceGain = new HashMap<>();

    // List of all the loaded sources
    // A source can be played and make sound. A source is connected 
    // to a buffer which holds the sound data. Multiple sources can be
    // connected to the same buffer. If the same sound should be played
    // multiple times simultaneously a source is needed for each of the 
    // simultaneous sounds. 
    private final ArrayList<Integer> sources = new ArrayList<>();

    // The position of the listener x, y, z (range: -1.0 - 1.0}
    private static final float[] listenerPos = {0.0f, 0.0f, 0.0f};
    // Velocity of the listener.
    private static final float[] listenerVel = {0.0f, 0.0f, 0.0f};
    // Orientation of the listener. (first 3 elements are "at", second 3 are "up")
    private static final float[] listenerOri = {0.0f, 0.0f, -1.0f, 0.0f, 1.0f, 0.0f};

    // Default velocity of the source.
    private static final float[] sourceVel = {0.0f, 0.0f, 0.0f};
    
    private float globalGain = 1.0f;

    /**
     * Initiates the sound system
     */
    public OpenAL() {
        al = ALFactory.getAL();
        init();
    }

    /**
     * Play the source with the given ID
     *
     * @param sourceId the ID of the source
     */
    public void play(int sourceId) {
        al.alSourcePlay(sourceId);
    }
    
    /**
     * Stop the source with the given ID
     *
     * @param sourceId the ID of the source
     */
    public void stop(int sourceId) {
        al.alSourceStop(sourceId);
    }
    
    /**
     * Pause the source with the given ID
     *
     * @param sourceId the ID of the source
     */
    public void pause(int sourceId) {
        al.alSourcePause(sourceId);
    }
    
    /**
     * Set the position of the source
     *
     * @param sourceId the source ID
     * @param x x-position (range: -1.0 - 1.0)
     * @param y y-position (range: -1.0 - 1.0)
     * @param z z-position (range: -1.0 - 1.0)
     */
    public void setPosision(int sourceId, float x, float y, float z) {
        float[] values = {x, y, z};
        al.alSourcefv(sourceId, AL.AL_POSITION, values, 0);
    }

    /**
     * Set the velocity of the source
     *
     * @param sourceId the source ID
     * @param x x-velocity (range: -1.0 - 1.0)
     * @param y y-velocity (range: -1.0 - 1.0)
     * @param z z-velocity (range: -1.0 - 1.0)
     */
    public void setVelocity(int sourceId, float x, float y, float z) {
        float[] values = {x, y, z};
        al.alSourcefv(sourceId, AL.AL_VELOCITY, values, 0);
    }

    /**
     * Set the gain of the source
     *
     * @param sourceId the source ID
     * @param gain the gain value (range: 0.0 - 1.0)
     */
    public void setGain(int sourceId, float gain) {
        al.alSourcef(sourceId, AL.AL_GAIN, gain);
        sourceGain.put(sourceId, gain);
    }
    
    /**
     * Create a new buffer.
     *
     * @param inputStream a stream of sound data
     * @return the buffer ID
     *
     * @throws backend.sound.OpenAL.ALError
     */
    public int createBuffer(InputStream inputStream) throws ALError {

        ByteBuffer[] data = new ByteBuffer[1];
        int[] buffer = new int[1];
        int[] format = new int[1];
        int[] size = new int[1];
        int[] freq = new int[1];
        int[] loop = new int[1];

        // Load wav data into a buffer.
        al.alGenBuffers(1, buffer, 0);

        if (al.alGetError() != AL.AL_NO_ERROR) {
            throw new ALError("Failed to create buffer");
        }

        ALut.alutLoadWAVFile(inputStream, format, data, size, freq, loop);
        al.alBufferData(buffer[0], format[0], data[0], size[0], freq[0]);

        if (al.alGetError() != AL.AL_NO_ERROR) {
            throw new ALError("Failed to load buffer data");
        }
        return buffer[0];
    }

    /**
     * Add a new source.
     *
     * The source is not looping
     * 
     * @param bufferID the buffer ID to connect the source to
     * @return the source ID
     *
     * @throws backend.sound.OpenAL.ALError
     */
    public int addSource(int bufferID) throws ALError {
        return addSource(bufferID, false);
    }

    
      /**
     * Add a new source.
     *
     * @param bufferID the buffer ID to connect the source to
     * @param loop true if the source should loop
     * @return the source ID
     *
     * @throws backend.sound.OpenAL.ALError
     */
    public int addSource(int bufferID, boolean loop) throws ALError {
        int[] source = new int[1];

        al.alGenSources(1, source, 0);

        if (al.alGetError() != AL.AL_NO_ERROR) {
            throw new ALError("Failed to create source");
        }

        // Set default values
        al.alSourcei(source[0], AL.AL_BUFFER, bufferID);
        al.alSourcef(source[0], AL.AL_PITCH, 1.0f);
        al.alSourcef(source[0], AL.AL_GAIN, globalGain);
        if(loop){
            al.alSourcei(source[0], AL.AL_LOOPING, AL.AL_TRUE);
        }else{
            al.alSourcei(source[0], AL.AL_LOOPING, AL.AL_FALSE);
        }
        al.alSourcefv(source[0], AL.AL_VELOCITY, sourceVel, 0);

        sources.add(source[0]);
        return source[0];
    
    }
    
    /**
     * Set the gain of all the sources.
     * 
     * @param value the gain values
     */
    public void setGlobalGain(float value){
        globalGain = value;
        for(int i : sources){
            al.alSourcef(i, AL.AL_GAIN, value * sourceGain.getOrDefault(i, 1.0f));
        }
    }
    
    /**
     * Generic error that id thrown when a error happens.
     */
    public static class ALError extends Exception {

        public ALError(String msg) {
            super(msg);
        }
    }
    
    private void init() {

        ALut.alutInit();
        al.alGetError(); // Reset the error state

        al.alListenerfv(AL.AL_POSITION, listenerPos, 0);
        al.alListenerfv(AL.AL_VELOCITY, listenerVel, 0);
        al.alListenerfv(AL.AL_ORIENTATION, listenerOri, 0);

        // Setup an exit procedure.
        Runtime runtime = Runtime.getRuntime();
        runtime.addShutdownHook(new Thread(() -> {
            cleanup();
        }));
    }

    private void cleanup() {

        Iterator<Integer> it = sources.iterator();
        while (it.hasNext()) {
            al.alDeleteSources(1, new int[]{it.next()}, 0);
        }
        sources.clear();

        it = buffers.iterator();
        while (it.hasNext()) {
            al.alDeleteBuffers(1, new int[]{it.next()}, 0);
        }
        buffers.clear();

        sourceGain.clear();
        
        ALut.alutExit();
    }

}
