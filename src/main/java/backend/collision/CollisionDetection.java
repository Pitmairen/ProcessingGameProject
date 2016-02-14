package backend.collision;

/**
 * Interface for the collision detection.
 *
 * @author pitmairen
 */
public interface CollisionDetection {

    /**
     * Should be called after every actor update.
     */
    public void detectCollisions();

    /**
     * Returns the number of collision checks that was done during the last
     * detection run.
     *
     * @return the number of collision checks that was done.
     */
    public int getCollisionCheckCount();

}
