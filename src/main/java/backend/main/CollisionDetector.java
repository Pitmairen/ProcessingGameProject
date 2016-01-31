package backend.main;

import backend.actor.Actor;
import java.util.ArrayList;
import java.util.Iterator;

/**
 * Handles collision detection.
 *
 * @author Kristian Honningsvag.
 */
public class CollisionDetector {

    GameEngine gameEngine;

    /**
     * Constructor.
     *
     * @param gameEngine
     */
    public CollisionDetector(GameEngine gameEngine) {
        this.gameEngine = gameEngine;
    }

    /**
     * Detects actor vs outer wall collisions.
     *
     * @param movingActor The actor to be checked.
     * @return The wall that was hit.
     */
    public String detectWallCollision(Actor movingActor) {

        String wallLocation = null;

        if (movingActor.getPositionX() + movingActor.getHitBoxRadius() >= (gameEngine.getGuiHandler().getWidth() - gameEngine.getGuiHandler().getOuterWallThickness())) {
            wallLocation = "east";
        }
        if (movingActor.getPositionY() + (movingActor.getHitBoxRadius()) >= (gameEngine.getGuiHandler().getHeight() - gameEngine.getGuiHandler().getOuterWallThickness())) {
            wallLocation = "south";
        }
        if (movingActor.getPositionX() - (movingActor.getHitBoxRadius()) <= (0 + gameEngine.getGuiHandler().getOuterWallThickness())) {
            wallLocation = "west";
        }
        if (movingActor.getPositionY() - (movingActor.getHitBoxRadius()) <= (0 + gameEngine.getGuiHandler().getOuterWallThickness())) {
            wallLocation = "north";
        }
        return wallLocation;
    }

    /**
     * Detects actor vs actor collisions.
     *
     * @param movingActor The actor to be checked.
     * @return List of all actors that was hit.
     */
    public ArrayList<Actor> detectActorCollision(Actor movingActor) {

        ArrayList<Actor> collisions = new ArrayList<Actor>();

        Iterator<Actor> it = gameEngine.getCurrentLevel().getActors().iterator();
        while (it.hasNext()) {

            Actor actorInList = it.next();

            if (actorInList != movingActor) {

                // Detection between to round objects.
                double dx = Math.abs(actorInList.getPositionX() - movingActor.getPositionX());
                double dy = Math.abs(actorInList.getPositionY() - movingActor.getPositionY());
                double distanceBetweenActors = Math.sqrt(Math.pow(dx, 2) + Math.pow(dy, 2));
                if (distanceBetweenActors < movingActor.getHitBoxRadius() + actorInList.getHitBoxRadius()) {
                    collisions.add(actorInList);
                }

//                // Detection between two square objects.
//                if ((Math.abs(movingActor.getPositionX() - actorInList.getPositionX()) < movingActor.getHitBoxRadius() + actorInList.getHitBoxRadius())
//                        && (Math.abs(movingActor.getPositionY() - actorInList.getPositionY()) < movingActor.getHitBoxRadius() + actorInList.getHitBoxRadius())) {
//                    collisions.add(actorInList);
//                }
            }
        }
        return collisions;
    }

}
