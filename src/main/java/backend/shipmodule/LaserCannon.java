package backend.shipmodule;

import backend.actor.Actor;
import backend.actor.Enemy;
import backend.main.Vector;
import backend.resources.Image;
import backend.resources.Sound;
import processing.core.PImage;
import userinterface.GUIHandler;

/**
 * A laser. Fires a constant laser beam at the target.
 *
 * @author Kristian Honningsvag.
 */
public class LaserCannon extends OffensiveModule {

    // Shape and color.
    private int turretLength = 27;
    private int turretWidth = 4;
    private int[] turretRGBA = new int[]{200, 30, 30, 255};

    private PImage bgImage;
    private boolean soundActive = false;
    private PImage laserCannonImg;

    /**
     * Constructor.
     */
    public LaserCannon(Actor owner) {
        super("Laser Cannon", owner);

        bgImage = owner.getGameEngine().getResourceManager().getImage(Image.LASER_BEAM);
        projectileDamage = 0.04f;
        laserCannonImg = owner.getGameEngine().getResourceManager().getImage(Image.LASER_CANNON);
    }

    @Override
    public void draw() {

        // Draw the cannon.
        drawModule(laserCannonImg, 5, 0, defaultModuleWidth, defaultModuleHeight);
        
        //Old processing modules
//        owner.getGuiHandler().strokeWeight(turretWidth);
//        owner.getGuiHandler().stroke(turretRGBA[0], turretRGBA[1], turretRGBA[2]);
//        owner.getGuiHandler().fill(turretRGBA[0], turretRGBA[1], turretRGBA[2]);
//        owner.getGuiHandler().line((float) owner.getPosition().getX(), (float) owner.getPosition().getY(),
//                (float) owner.getPosition().getX() + (float) (turretLength * Math.cos(owner.getHeading().getAngle2D())),
//                (float) owner.getPosition().getY() + (float) (turretLength * Math.sin(owner.getHeading().getAngle2D())));

//        owner.getGuiHandler().pushMatrix();
//        owner.getGuiHandler().translate((float) owner.getPosition().getX(), (float) owner.getPosition().getY());
//        owner.getGuiHandler().rotate((float) owner.getHeading().getAngle2D());
//        owner.getGuiHandler().image(getImage(), - 10, - 10);
//        owner.getGuiHandler().popMatrix();

        // Draw the laser beam.
        if (this.moduleActive) {
            double screenWidth = owner.getGameEngine().getGuiHandler().getWidth();
            double screenHeight = owner.getGameEngine().getGuiHandler().getHeight();
            double screenDiagonalLength = Math.sqrt(Math.pow(screenWidth, 2) + Math.pow(screenHeight, 2));

            float laserLength = Math.min(findLaserLength(), (float) screenDiagonalLength);

            GUIHandler gui = owner.getGuiHandler();
            gui.pushMatrix();
            gui.tint(0xffff0000);
            gui.translate((float) owner.getPosition().getX(), (float) owner.getPosition().getY());
            gui.rotate((float) owner.getHeading().getAngle2D());
            gui.image(bgImage, 25, -5, (float) laserLength - 25, 10);
            gui.popMatrix();

            this.setModuleActive(false);

            if (!soundActive) {
                soundActive = true;
                owner.getGameEngine().getSoundManager().play(Sound.LASER, owner.getPosition());
            } else {
                owner.getGameEngine().getSoundManager().setPosition(Sound.LASER, owner.getPosition());

            }
        } else if (soundActive) {
            owner.getGameEngine().getSoundManager().stop(Sound.LASER);
            soundActive = false;
        }
    }

    /**
     * Fires the laser from the actor towards the current heading.
     */
    @Override
    public void activate() {
        this.setModuleActive(true);
    }

    /**
     * Finds and calculates the distance to the nearest target that the laser is
     * hitting.
     *
     *
     * This is done by calculating the determinant of the matrix that is created
     * by the heading vector of the laser (LH) and the vector from the laser to
     * the target actor (LT) like so: [LH.x LT.x] [LH.y LT.y]
     *
     * d = (LH.x*LT.y) - (LH.x*LT.y)
     *
     * (This is the same as the z value of cross product of the two vectors)
     *
     * The determinant value gives the area of the parallelogram created by the
     * two vectors. The sign of the value will be positive or negative depending
     * on which side of the laser heading the target is located.
     *
     * To detect a hit by the laser two lines that are parallel to the laser is
     * created and moved apart by a small distance, and the calculation above is
     * done for both lines. If the sign of the determinant is different in the
     * two calculations it means that the target is between the lines and there
     * is a hit.
     *
     * (Currently this also removes hit points from the target that is getting
     * hit. This design should probably be improved at some point.)
     *
     * @return the distance to the target that is being hit by the laser
     */
    private float findLaserLength() {

        Vector laserPos = owner.getPosition().copy();
        Vector laserHeading = owner.getHeading().copy().normalize();

        // The unit vector normal to the laser's heading used to move 
        // the two parallel lines apart.
        Vector normal = laserHeading.copy().rotate(Math.PI / 2);

        // The current shortes distance to a target. We start at infinity.
        double shortestDistance = Double.POSITIVE_INFINITY;
        Actor closest = null;

        for (Actor act : owner.getGameEngine().getCurrentLevel().getActors()) {

            // Only check agains the enemies
            if (act == owner || !(act instanceof Enemy)) {

                continue;
            }

            Vector targetPos = act.getPosition().copy();
            Vector laserToTarget = Vector.sub(targetPos, laserPos);

            // We only want to hit targets that are in front of the player, 
            // so we check the dot product between the laser heading and the
            // vector from the player to the target. If the result is negative
            // the target is behind the player so we skip this actor.
            if (laserHeading.dot(laserToTarget) < 0) {
                continue;
            }

            // Start position of the two parallel lines shifted ± the target hitbox radius
            // units in the normal direction.
            Vector line1Pos = Vector.add(laserPos, normal.copy().mult(act.getHitBoxRadius()));
            Vector line2Pos = Vector.add(laserPos, normal.copy().mult(-act.getHitBoxRadius()));

            // The cross product between the laserHeading and the vector from
            // each parallel position to the target.
            double d1 = Vector.cross(laserHeading, Vector.sub(targetPos, line1Pos)).getZ();
            double d2 = Vector.cross(laserHeading, Vector.sub(targetPos, line2Pos)).getZ();

            // If the sign is different the target is between the lines
            // so the laser is hitting the target.
            if (Math.signum(d1) != Math.signum(d2)) {

                double dist = laserToTarget.mag();
                // We are only interrested in the object thats is closest
                // to the laser. 
                if (dist < shortestDistance) {
                    closest = act;
                    shortestDistance = dist;
                }
            }
        }

        if (closest != null) {
            closest.removeHitPoints(projectileDamage);
        }

        return (float) shortestDistance;
    }
}
