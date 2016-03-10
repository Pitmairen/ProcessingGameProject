package backend.shipmodule;

import backend.actor.Actor;
import backend.actor.enemy.Enemy;
import backend.actor.projectile.SeekerMissile;
import backend.main.FadingCanvasItemManager;
import backend.main.Timer;
import backend.main.Vector;
import backend.resources.Image;
import backend.resources.Sound;
import java.util.ArrayList;
import processing.core.PApplet;
import processing.core.PImage;

/**
 * The seeker cannon selects the closes target and fires a seeker missile
 *
 * @author pitmairen
 */
public class SeekerCannon extends OffensiveModule {

    private final int turretLength = 20;
    private final int turretWidth = 7;
    private final int turretRGBA = 0xff441599;

    private final Timer timer = new Timer();
    private final double timeBetweenShots = 700;
    private final FadingCanvasItemManager fadingCanvasItems;
    private TargetSelector selector;

    public SeekerCannon(Actor owner, FadingCanvasItemManager itemManager) {
        super("SeekerCannon", owner);

        projectileDamage = 10;
        fadingCanvasItems = itemManager;

        moduleImage = getImageFromResourceManager(Image.SEEKER_CANNON);
    }

    @Override
    public void draw() {
        drawModule(moduleImage, 0, 0, defaultModuleWidth, defaultModuleHeight);

        updateSelector();
    }

    @Override
    public void activate() {
        if (owner.getGameEngine().getCurrentLevel().getEnemies().size() > 0) {
            if (timer.timePassed() >= timeBetweenShots) {
                selector = findTarget();
                timer.restart();
            }
        }
    }

    private void updateSelector() {
        if (selector == null) {
            return;
        }

        selector.update();

        // If the target has not been found just draw the selector
        if (!selector.ready()) {
            selector.draw();
            return;
        }

        // Fire the missile
        SeekerMissile.Target target = selector.getTarget();

        if (target != null) {
            
            owner.getGameEngine().getSoundManager().play(Sound.MISSILE_LAUNCH, owner.getPosition());

            SeekerMissile missile = new SeekerMissile(owner.getPosition().copy(), selector.getTarget(), this);

            owner.getGameEngine().getCurrentLevel().getProjectiles().add(missile);
            owner.getGameEngine().getCurrentLevel().getActors().add(missile);

            missile.getSpeedT().set(owner.getHeading().copy().normalize());
            fadingCanvasItems.add(missile);
        }

        selector = null; // Remove the selector

    }

    /**
     * Sorts the elements by distance and returns a target selector
     *
     * @return the target selector
     */
    private TargetSelector findTarget() {

        ArrayList<Enemy> enemies = new ArrayList<>(getOwner().getGameEngine().getCurrentLevel().getEnemies());

        Vector pos = owner.getPosition();

        // Sort by distance
        enemies.sort((Enemy a, Enemy b) -> {
            Vector da = Vector.sub(a.getPosition(), pos);
            Vector db = Vector.sub(b.getPosition(), pos);
            return Double.compare(da.mag(), db.mag());
        });

        return new TargetSelector(enemies);

    }

    /**
     * The target selector draws a selector around the targets to indicate the
     * search pattern of the seeker. This is done in a pattern going through the
     * n-th closest enemies starting at the one farthest away and moving towards
     * the nearest enemy.
     */
    private class TargetSelector {

        private final ArrayList<Enemy> enemies;

        private int currentPosition;
        private int updateCounter = 0;

        public TargetSelector(ArrayList<Enemy> actors) {
            this.enemies = actors;

            // Currenlty we only hightlight the closest enemy.
            currentPosition = 0;
        }

        public void update() {
            updateCounter++;
            // Update the position every 20rd frame
            if (updateCounter % 20 == 0) {
                currentPosition--;
            }
        }

        public void draw() {

            if (currentPosition == 0) { // We are at the final target
                owner.getGuiHandler().fill(0xffff0000, 220);
                owner.getGuiHandler().stroke(0xffff0000);
            } else { // Its not the final target
                owner.getGuiHandler().fill(0xff00ffff, 120);
                owner.getGuiHandler().stroke(0xffffff00);

            }
            owner.getGuiHandler().strokeWeight(3);
            owner.getGuiHandler().ellipse((float) enemies.get(currentPosition).getPosition().getX(),
                    (float) enemies.get(currentPosition).getPosition().getY(), 60, 60);

        }

        /**
         * Returns true when the target has been located
         *
         * @return true when target has been located
         */
        public boolean ready() {
            return currentPosition == -1;
        }

        /**
         * Returns the target
         *
         * @return the target
         */
        public SeekerMissile.Target getTarget() {
            if (enemies.isEmpty()) {
                return null;
            }

            final Enemy act = enemies.get(0);
            return new SeekerMissile.Target() {
                @Override
                public Vector getPosition() {

                    return act.getPosition().copy();
                }

                @Override
                public boolean isAlive() {
                    return act.getCurrentHitPoints() > 0;
                }
            };

        }

    }

}
