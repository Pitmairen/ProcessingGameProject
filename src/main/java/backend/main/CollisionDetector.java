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
     * Detects actor vs outer wall collisions and returns an array list of
     * strings corresponding to the walls that where hit. Up to 2 walls can be
     * hit Simultaneously in rare cases. Returns an empty array list if no walls
     * where hit.
     *
     * @param movingActor The actor to be checked.
     * @return The walls that was hit.
     */
    public ArrayList<String> detectWallCollision(Actor movingActor) {

        ArrayList<String> wallCollision = new ArrayList<String>();

        if (movingActor.getPosition().getX() + movingActor.getHitBoxRadius() >= (gameEngine.getGuiHandler().getWidth() - gameEngine.getGuiHandler().getOuterWallThickness())) {
            wallCollision.add("east");
        }
        if (movingActor.getPosition().getY() + (movingActor.getHitBoxRadius()) >= (gameEngine.getGuiHandler().getHeight() - gameEngine.getGuiHandler().getOuterWallThickness())) {
            wallCollision.add("south");
        }
        if (movingActor.getPosition().getX() - (movingActor.getHitBoxRadius()) <= (0 + gameEngine.getGuiHandler().getOuterWallThickness())) {
            wallCollision.add("west");
        }
        if (movingActor.getPosition().getY() - (movingActor.getHitBoxRadius()) <= (0 + gameEngine.getGuiHandler().getOuterWallThickness())) {
            wallCollision.add("north");
        }
        return wallCollision;
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

            if (actorInList != movingActor) {  // Actors do not collide with themselves.

                double distanceBetweenActors = movingActor.getPosition().dist(actorInList.getPosition());

                // Detection between to round objects.
                if (distanceBetweenActors < movingActor.getHitBoxRadius() + actorInList.getHitBoxRadius()) {
                    collisions.add(actorInList);
                }

//                // Detection between two square objects.
//                if ((Math.abs(movingActor.getPosition().getX() - actorInList.getPosition().getX()) < movingActor.getHitBoxRadius() + actorInList.getHitBoxRadius())
//                        && (Math.abs(movingActor.getPosition().getY() - actorInList.getPosition().getY()) < movingActor.getHitBoxRadius() + actorInList.getHitBoxRadius())) {
//                    collisions.add(actorInList);
//                }
            }
        }
        return collisions;
    }

}
