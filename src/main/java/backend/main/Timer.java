package backend.main;

/**
 * Handles reporting on the passage of time.
 *
 * @author Kristian Honningsvag.
 */
public class Timer {

    private double time = 0;
    private double pauseTime = 0;
    private boolean paused = false;

    /**
     * Constructor.
     */
    public Timer() {
        reset();
    }

    /**
     * Resets the timer.
     */
    public void reset() {
        time = System.currentTimeMillis();
        paused = false;
    }

    /**
     * Returns the time passed since last reset.
     */
    public double timePassed() {

        double timePassed;

        if (!paused) {
            timePassed = System.currentTimeMillis() - time;
        } else {
            timePassed = pauseTime - time;
        }

        return timePassed;
    }

    /**
     * Pauses the timer.
     */
    public void pause() {
        if (!paused) {
            pauseTime = System.currentTimeMillis();
            paused = true;
        }
    }

    /**
     * Starts the timer.
     */
    public void start() {
        if (paused) {
            time = time + (System.currentTimeMillis() - pauseTime);
            paused = false;
        }
    }
}
