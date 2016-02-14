package backend.main;

import backend.actor.Actor;
import backend.actor.Enemy;
import backend.actor.Player;
import backend.actor.Projectile;
import backend.actor.SeekerMissile;
import backend.collision.CollisionListener;
import backend.collision.Wall;
import backend.item.Item;
import backend.shipmodule.Shield;

/**
 * Calculates all the responses for all the collisions in the game.
 *
 * @author pitmairen
 */
public class CollisionResponse implements CollisionListener {

    /**
     * A collision between has been detected between actor a and b
     *
     * @param a colliding actor a
     * @param b colliding actor b
     */
    @Override
    public void collisionDetected(Actor a, Actor b) {

        checkPlayerCollision(a, b);

        if (checkEnemyCollision(a, b)) {
            // Two enemies collided
        } else if (checkShieldCollision(a, b)) {
            // Actor collided with its own shield
        } else if (checkProjectileCollision(a, b)) {
            // A projectile collided with something
        } else {

            elasticColision(a, b);
            a.collision(b);
            b.collision(a);
        }
    }

    /**
     * An actor has collided with the wall.
     *
     * @param actor the colliding actor
     * @param wall the wall that the actor collided with
     */
    @Override
    public void wallCollisionDetected(Actor actor, Wall wall) {
        if (actor instanceof SeekerMissile) {
            // The seeker missile can go outside the walls
        } else if (actor instanceof Projectile) {
            // Kill the projectile
            ((Projectile) actor).targetHit();
        } else {
            wallBounce(actor, wall);
        }
    }

    /**
     * Check if the player has collided with something.
     *
     * @param a potential player
     * @param b potential player
     */
    private void checkPlayerCollision(Actor a, Actor b) {
        checkInstance(Player.class, a, b, (Player player, Actor other) -> {
            if ((other instanceof Item)) {
                // This item crashed into a player.
                ((Item) other).pickup(player);
            }
            return true;

        });
    }

    /**
     * Check if two enemies has collided.
     *
     * @param a potential enemy
     * @param b potential enemy
     * @return true if two enemies has collided
     */
    private boolean checkEnemyCollision(Actor a, Actor b) {
        if (a instanceof Enemy && b instanceof Enemy) {
            elasticColision(a, b);
            return true;
        }
        return false;
    }

    /**
     * Check if a projectile is colliding with another actor.
     *
     * @param a potential projectile
     * @param b potential projectile
     * @return true if a projectile collided with another actor
     */
    private boolean checkProjectileCollision(Actor a, Actor b) {

        return checkInstance(Projectile.class, a, b, (Projectile pro, Actor target) -> {
            if (pro.getShipModule().getOwner() == target) {
                // An actor can't shoot itself
            } else if (target instanceof Projectile) {
                // Two projectiles can't collide
            } else if (target instanceof Shield.ShieldActor && ((Shield.ShieldActor) target).getOwner() == pro.getShipModule().getOwner()) {
                // Projectiles can't hit the owner's shield
            } else if (target instanceof Enemy && (pro.getShipModule().getOwner() instanceof Enemy)) {
                // NPC's cant shoot other NPC's.
            } else if (target instanceof Item) {
                // Projectiles don't interact with items.
            } else {
                elasticColision(pro, target);
                pro.removeHitPoints(target.getCollisionDamageToOthers());
                target.removeHitPoints(pro.getCollisionDamageToOthers());
                pro.targetHit();
            }
            return true;
        });
    }

    /**
     * Check if an actor is colliding with its own shield
     *
     * @param a potential shield / owner of shield
     * @param b potential shield / owner of shield
     * @return true if an actor is colliding with its own shield
     */
    private boolean checkShieldCollision(Actor a, Actor b) {
        return checkInstance(Shield.ShieldActor.class, a, b, (Shield.ShieldActor shield, Actor other) -> {
            // An actor can't collide with its own shield
            return shield.getOwner() == other;
        });
    }

    /**
     * Bounce of the walls.
     *
     * A small force is applied to the actor outwards from the wall to push the
     * actor back into game window. This should prevent any actors from being
     * stuck outside of the walls.
     *
     * @param actor the actor that has hit the wall
     * @param wall the wall that was hit
     */
    private void wallBounce(Actor actor, Wall wall) {
        Vector speed = actor.getSpeedT();
        if (wall.equals(Wall.EAST)) {
            if (speed.getX() > 0) {
                speed.setX(speed.getX() * (-actor.getBounceModifier()));
            }
            actor.applyForce(new Vector(-actor.getEngineThrust() * 1.1, 0, 0));
        } else if (wall.equals(Wall.WEST)) {
            if (speed.getX() < 0) {
                speed.setX(speed.getX() * (-actor.getBounceModifier()));
            }
            actor.applyForce(new Vector(actor.getEngineThrust() * 1.1, 0, 0));
        }
        if (wall.equals(Wall.NORTH)) {
            if (speed.getY() < 0) {
                speed.setY(speed.getY() * (-actor.getBounceModifier()));
            }
            actor.applyForce(new Vector(0, actor.getEngineThrust() * 1.1, 0));
        } else if (wall.equals(Wall.SOUTH)) {
            if (speed.getY() > 0) {
                speed.setY(speed.getY() * (-actor.getBounceModifier()));
            }
            actor.applyForce(new Vector(0, -actor.getEngineThrust() * 1.1, 0));
        }
    }

    /**
     * Calculate the new velocities after an elastic collision between two
     * actors.
     *
     * @param a colliding actor
     * @param b colliding actor
     */
    private void elasticColision(Actor a, Actor b) {

        Vector collisionNormal = Vector.sub(a.getPosition(), b.getPosition());

        // If the dot product is positive the objects are moving away from each other,
        // so we don't do anything. This should prevent objects from sticking 
        // together
        if (Vector.sub(a.getSpeedT(), b.getSpeedT()).dot(collisionNormal) > 0) {
            return;
        }

        double speedFinalAx;
        double speedFinalAy;
        double speedFinalBx;
        double speedFinalBy;

        speedFinalAx = (a.getMass() - b.getMass()) / (a.getMass() + b.getMass()) * a.getSpeedT().getX()
                + (2 * b.getMass()) / (a.getMass() + b.getMass()) * (b.getSpeedT().getX());

        speedFinalAy = (a.getMass() - b.getMass()) / (a.getMass() + b.getMass()) * a.getSpeedT().getY()
                + (2 * b.getMass()) / (a.getMass() + b.getMass()) * (b.getSpeedT().getY());

        speedFinalBx = (2 * a.getMass()) / (a.getMass() + b.getMass()) * a.getSpeedT().getX()
                - (a.getMass() - b.getMass()) / (a.getMass() + b.getMass()) * (b.getSpeedT().getX());

        speedFinalBy = (2 * a.getMass()) / (a.getMass() + b.getMass()) * a.getSpeedT().getY()
                - (a.getMass() - b.getMass()) / (a.getMass() + b.getMass()) * (b.getSpeedT().getY());

        a.getSpeedT().set(speedFinalAx, speedFinalAy, 0);
        b.getSpeedT().set(speedFinalBx, speedFinalBy, 0);

    }

    /**
     * Helper method that will check the type of the two actors and cast one of
     * them to the correct type so it can be used for collision checking.
     *
     * This method checks if one of the two actors a and b is of the expected
     * type. If one of them is the correct type it is cast two the expected type
     * and passed into the checker instance which should do the actual collision
     * checking.
     *
     * @param <T> the class type to check for
     * @param type the class type to check for
     * @param a colliding actor a
     * @param b colliding actor b
     * @param checker the checker instance
     *
     * @return true if the check was performed
     */
    private <T extends Actor> boolean checkInstance(Class<T> type, Actor a, Actor b, Checker<T, Actor> checker) {
        if (type.isInstance(a)) {
            return checker.check(type.cast(a), b);
        } else if (type.isInstance(b)) {
            return checker.check(type.cast(b), a);
        }
        return false;
    }

    /**
     * Interface used by the checkIntance method above.
     *
     * @param <T1>
     * @param <T2>
     */
    private static interface Checker<T1 extends Actor, T2 extends Actor> {

        public boolean check(T1 a, T2 b);

    }

}
