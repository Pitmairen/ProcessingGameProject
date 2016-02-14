package backend.collision;

import backend.actor.Actor;
import backend.main.GameEngine;

/**
 * Base implementation of the collision detection 
 * 
 * @author pitmairen
 */
public abstract class BaseDetection implements CollisionDetection {

    protected GameEngine gameEngine;
    private final CollisionListener listener;

    public BaseDetection(GameEngine engine, CollisionListener listener) {
        this.listener = listener;
        this.gameEngine = engine;
    }

    protected void checkWallCollision(Actor actor) {

        double posX = actor.getPosition().getX();
        double posY = actor.getPosition().getY();
        double width = gameEngine.getGuiHandler().width;
        double height = gameEngine.getGuiHandler().height;
        double wallWidth = gameEngine.getGuiHandler().getOuterWallThickness();

        double radius = actor.getHitBoxRadius();

        if (posX + radius >= width + wallWidth) {
            reportWallCollision(actor, Wall.EAST);
        } else if (posX - radius <= wallWidth) {
            reportWallCollision(actor, Wall.WEST);
        }

        if (posY + radius >= height - wallWidth) {
            reportWallCollision(actor, Wall.SOUTH);
        } else if (posY - radius <= wallWidth) {
            reportWallCollision(actor, Wall.NORTH);
        }
    }

    protected void checkCollision(Actor a, Actor b) {

        double distance = a.getPosition().dist(b.getPosition());

        if (distance < a.getHitBoxRadius() + b.getHitBoxRadius()) {
            reportCollision(a, b);
        }
    }

    protected void reportCollision(Actor a, Actor b) {
        listener.collisionDetected(a, b);
    }

    protected void reportWallCollision(Actor a, Wall wall) {
        listener.wallCollisionDetected(a, wall);
    }
}
