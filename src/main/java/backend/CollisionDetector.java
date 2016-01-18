package backend;

import backend.actor.Actor;
import java.util.Iterator;

/**
 * Handles collisions.
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
     * Checks for all collisions.
     */
    public void checkAll(double timePassed) {
        detectWallCollision(timePassed);
        checkMissileHit();
        detectPlayerEnemyCollision();
    }

    /**
     * Wall collisions.
     */
    private void detectWallCollision(double timePassed) {

        // Projectiles.
        Iterator<Actor> it = gameEngine.getCurrentLevel().getProjectiles().iterator();
        while (it.hasNext()) {

            Actor actor = it.next();

            if (actor.getPositionX() + (actor.getHitBoxRadius()) >= (gameEngine.getGuiHandler().getWidth() - gameEngine.getGuiHandler().getOuterWallThickness()) // Right wall.
                    || actor.getPositionY() + (actor.getHitBoxRadius()) >= (gameEngine.getGuiHandler().getHeight() - gameEngine.getGuiHandler().getOuterWallThickness()) // Lower wall.
                    || actor.getPositionX() - (actor.getHitBoxRadius()) <= (0 + gameEngine.getGuiHandler().getOuterWallThickness()) // Left wall.
                    || actor.getPositionY() - (actor.getHitBoxRadius()) <= (0 + gameEngine.getGuiHandler().getOuterWallThickness())) // Upper wall
            {
                it.remove();
                gameEngine.getCurrentLevel().getActors().remove(actor);
            }
        }

        // Right wall.
        if (gameEngine.getCurrentLevel().getPlayer().getPositionX() + (gameEngine.getCurrentLevel().getPlayer().getHitBoxRadius()) >= (gameEngine.getGuiHandler().getWidth() - gameEngine.getGuiHandler().getOuterWallThickness())) {
            gameEngine.getCurrentLevel().getPlayer().wallBounce("right", timePassed);
        }
        for (Actor actor : gameEngine.getCurrentLevel().getEnemies()) {
            if (actor.getPositionX() + actor.getHitBoxRadius() >= (gameEngine.getGuiHandler().getWidth() - gameEngine.getGuiHandler().getOuterWallThickness())) {
                actor.wallBounce("right", timePassed);
            }
        }
        // Lower wall.
        if (gameEngine.getCurrentLevel().getPlayer().getPositionY() + gameEngine.getCurrentLevel().getPlayer().getHitBoxRadius() >= (gameEngine.getGuiHandler().getHeight() - gameEngine.getGuiHandler().getOuterWallThickness())) {
            gameEngine.getCurrentLevel().getPlayer().wallBounce("lower", timePassed);
        }
        for (Actor actor : gameEngine.getCurrentLevel().getEnemies()) {
            if (actor.getPositionY() + actor.getHitBoxRadius() >= (gameEngine.getGuiHandler().getHeight() - gameEngine.getGuiHandler().getOuterWallThickness())) {
                actor.wallBounce("lower", timePassed);
            }
        }
        // Left wall.
        if (gameEngine.getCurrentLevel().getPlayer().getPositionX() - gameEngine.getCurrentLevel().getPlayer().getHitBoxRadius() <= (0 + gameEngine.getGuiHandler().getOuterWallThickness())) {
            gameEngine.getCurrentLevel().getPlayer().wallBounce("left", timePassed);
        }
        for (Actor actor : gameEngine.getCurrentLevel().getEnemies()) {
            if (actor.getPositionX() - actor.getHitBoxRadius() <= (0 + gameEngine.getGuiHandler().getOuterWallThickness())) {
                actor.wallBounce("left", timePassed);
            }
        }
        // Upper wall.
        if (gameEngine.getCurrentLevel().getPlayer().getPositionY() - gameEngine.getCurrentLevel().getPlayer().getHitBoxRadius() <= (0 + gameEngine.getGuiHandler().getOuterWallThickness())) {
            gameEngine.getCurrentLevel().getPlayer().wallBounce("upper", timePassed);
        }
        for (Actor actor : gameEngine.getCurrentLevel().getEnemies()) {
            if (actor.getPositionY() - actor.getHitBoxRadius() <= (0 + gameEngine.getGuiHandler().getOuterWallThickness())) {
                actor.wallBounce("upper", timePassed);
            }
        }
    }

    /**
     * Detects player and enemy collisions.
     */
    private void detectPlayerEnemyCollision() {

        for (Actor actor : gameEngine.getCurrentLevel().getEnemies()) {

            if ((Math.abs(gameEngine.getCurrentLevel().getPlayer().getPositionX() - actor.getPositionX()) < gameEngine.getCurrentLevel().getPlayer().getHitBoxRadius() + actor.getHitBoxRadius())
                    && (Math.abs(gameEngine.getCurrentLevel().getPlayer().getPositionY() - actor.getPositionY()) < gameEngine.getCurrentLevel().getPlayer().getHitBoxRadius() + actor.getHitBoxRadius())) {
                gameEngine.setSimulationState("deathScreen");
            }
        }
    }

    /**
     * Counts missile hits on the enemy frigate by the player.
     */
    private void checkMissileHit() {
        Iterator<Actor> projectilesIterator = gameEngine.getCurrentLevel().getProjectiles().iterator();
        while (projectilesIterator.hasNext()) {

            Actor projectile = projectilesIterator.next();

            Iterator<Actor> enemiesIterator = gameEngine.getCurrentLevel().getEnemies().iterator();

            while (enemiesIterator.hasNext()) {

                Actor enemy = enemiesIterator.next();

                if ((Math.abs(projectile.getPositionX() - enemy.getPositionX()) < projectile.getHitBoxRadius() + enemy.getHitBoxRadius())
                        && (Math.abs(projectile.getPositionY() - enemy.getPositionY()) < projectile.getHitBoxRadius() + enemy.getHitBoxRadius())) {

                    gameEngine.getCurrentLevel().getPlayer().increaseScore(1);
                    projectilesIterator.remove();
                    gameEngine.getCurrentLevel().getActors().remove(projectile);
                }
            }
        }
    }

}
