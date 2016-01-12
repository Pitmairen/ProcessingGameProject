package Backend;

/**
 * Handles reporting on the passage of time.
 *
 * @author Kristian Honningsvag.
 */
public class Timer {

    private double time = 0;

    /**
     * Constructor.
     */
    public Timer() {
        restart();
    }

    /**
     * Restarts the timer.
     */
    public void restart() {
        time = System.currentTimeMillis();
    }

    /**
     * Returns the time passed since last restart.
     *
     * @return
     */
    public double timePassed() {

        double timePassed = System.currentTimeMillis() - time;
        return timePassed;
    }

}
