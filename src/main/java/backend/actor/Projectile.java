package backend.actor;

import backend.shipmodule.ShipModule;
import java.util.ArrayList;
import userinterface.Drawable;

/**
 * Super class for all projectiles.
 *
 * @author Kristian Honningsvag.
 */
public abstract class Projectile extends Actor implements Drawable {

    protected ShipModule shipModule;  // From constructor.

    /**
     * Constructor.
     */
    public Projectile(double positionX, double positionY, ShipModule shipModule) {

        super(positionX, positionY, shipModule.getOwner().getGameEngine());

        this.shipModule = shipModule;
    }

    @Override
    public abstract void draw();

    @Override
    protected void checkWallCollisions(double timePassed) {
        // Projectiles does not bounce off walls.
        String wallCollision = gameEngine.getCollisionDetector().detectWallCollision(this);
        if (wallCollision != null) {
            this.targetHit();
        }
    }

    @Override
    protected void checkActorCollisions(double timePassed) {

        ArrayList<Actor> collisions = collisionDetector.detectActorCollision(this);

        if (collisions.size() > 0) {

            for (Actor actorInList : collisions) {

                if (actorInList == this.shipModule.getOwner()) {
                    // Projectiles can't hit the actor which fired them.
                } else if (actorInList instanceof Projectile) {
                    if (((Projectile) actorInList).getShipModule().getOwner() == this.shipModule.getOwner()) {
                        // Projectiles can't hit other projectiles fired by the same actor.
                    } else {
                        elasticColision(this, actorInList, timePassed);
                        this.removeHitPoints(actorInList.getCollisionDamageToOthers());
                        actorInList.removeHitPoints(this.collisionDamageToOthers);
                        this.targetHit();
                    }
                } else {
                    elasticColision(this, actorInList, timePassed);
                    this.removeHitPoints(actorInList.getCollisionDamageToOthers());
                    actorInList.removeHitPoints(this.collisionDamageToOthers);
                    this.targetHit();
                }
            }
        }
    }

    /**
     * Call this function when the projectile have hit something.
     */
    public void targetHit() {
        this.hitPoints = 0;
    }

    // Getters.
    public ShipModule getShipModule() {
        return shipModule;
    }

}
