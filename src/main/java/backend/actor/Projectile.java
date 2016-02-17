package backend.actor;

import backend.item.Item;
import backend.main.Vector;
import backend.shipmodule.Shield;
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
    public Projectile(Vector position, ShipModule shipModule) {

        super(position, shipModule.getOwner().getGameEngine());

        this.shipModule = shipModule;
    }

    @Override
    public abstract void draw();

    @Override
    protected void checkWallCollisions(double timePassed) {
        // Projectiles does not bounce off walls.
        ArrayList<String> wallCollisions = gameEngine.getCollisionDetector().detectWallCollision(this);
        if (!wallCollisions.isEmpty()) {  // A wall was hit.
            this.targetHit();
        }
    }

    @Override
    protected void checkActorCollisions(double timePassed) {

        ArrayList<Actor> collisions = collisionDetector.detectActorCollision(this);

        if (collisions.size() > 0) {
            for (Actor target : collisions) {

                if (target == this.shipModule.getOwner()) {
                    // Projectiles can't hit the actor which fired them.
                }
                else if (target instanceof Projectile) {
                    if (((Projectile) target).getShipModule().getOwner() == this.shipModule.getOwner()) {
                        // Projectiles can't hit other projectiles fired by the same actor.
                    }
                    else {
                        // Projectiles can't hit other projectiles.
                    }
                }
                else if(target instanceof Shield.ShieldActor && ((Shield.ShieldActor) target).getOwner() == this.shipModule.getOwner()){
                    // Projectiles can't hit the owner's shield
                }
                else if (target instanceof Enemy){
                    if (this.getShipModule().getOwner() instanceof Enemy) {
                        // NPC's cant shoot other NPC's.
                    }
                }
                else if (target instanceof Item) {
                        // Projectiles don't interact with items.
                }
                else {
                    elasticColision(this, target, timePassed);
                    this.removeHitPoints(target.getCollisionDamageToOthers());
                    target.removeHitPoints(this.collisionDamageToOthers);
                    this.targetHit();
                }
            }
        }
    }

    /**
     * Call this function when the projectile have hit something.
     */
    public void targetHit() {
        this.currentHitPoints = 0;
    }

    // Getters.
    public ShipModule getShipModule() {
        return shipModule;
    }

}
