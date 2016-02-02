package backend.shipmodule;

import backend.actor.Actor;
import backend.actor.Enemy;
import processing.core.PImage;
import userinterface.Drawable;
import userinterface.GUIHandler;

/**
 * A laser. Fires a constant laser beam at the target.
 *
 * @author Kristian Honningsvag.
 */
public class LaserCannon extends ShipModule implements Drawable {

    // Shape and color.
    private int turretLength = 27;
    private int turretWidth = 4;
    private int[] turretRGBA = new int[]{200, 30, 30, 255};

    private PImage bgImage;
    
    /**
     * Constructor.
     */
    public LaserCannon(Actor owner) {
        super("Laser Cannon", owner);

        bgImage = owner.getGuiHandler().loadImage("laser.png");
        projectileDamage = 100;
    }

    @Override
    public void draw() {

        // Draw the cannon.
        owner.getGuiHandler().strokeWeight(turretWidth);
        owner.getGuiHandler().stroke(turretRGBA[0], turretRGBA[1], turretRGBA[2]);
        owner.getGuiHandler().fill(turretRGBA[0], turretRGBA[1], turretRGBA[2]);
        owner.getGuiHandler().line((float) owner.getPositionX(), (float) owner.getPositionY(),
                (float) owner.getPositionX() + (float) (turretLength * Math.cos(owner.getHeading())),
                (float) owner.getPositionY() + (float) (turretLength * Math.sin(owner.getHeading())));

        // Draw the laser beam.
        if (this.moduleActive) {
            double screenWidth = owner.getGameEngine().getGuiHandler().getWidth();
            double screenHeight = owner.getGameEngine().getGuiHandler().getHeight();
            double screenDiagonalLength = Math.sqrt(Math.pow(screenWidth, 2) + Math.pow(screenHeight, 2));

            float laserLength = Math.min(findLaserLength(), (float)screenDiagonalLength);

            GUIHandler gui = owner.getGuiHandler();
            gui.pushMatrix();
            gui.tint(0xffff0000);
            gui.translate((float) owner.getPositionX(), (float) owner.getPositionY());
            gui.rotate((float)owner.getHeading());
            gui.image(bgImage, 25, -5, (float)laserLength - 25, 10);
            gui.popMatrix();

            this.setModuleActive(false);
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
     * two vectors. The sign of the value will be positive of negative depending
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
        // The laser line
        float x1 = (float) owner.getPositionX();
        float y1 = (float) owner.getPositionY();
        float x2 = x1 + (float) Math.cos(owner.getHeading());
        float y2 = y1 + (float) Math.sin(owner.getHeading());

        // The unit vector normal to the laser's heading used to move 
        // the two parallel lines apart.
        float nX = -((y2 - y1));
        float nY = (x2 - x1);

        // Parallel Line 1 shifted 15 positive units
        float line1_x1 = x1 + nX * 15;
        float line1_y1 = y1 + nY * 15;
        float line1_x2 = x2 + nX * 15;
        float line1_y2 = y2 + nY * 15;

        // Parallel Line 2 shifted 15 negative units
        float line2_x1 = x1 + nX * -15;
        float line2_y1 = y1 + nY * -15;
        float line2_x2 = x2 + nX * -15;
        float line2_y2 = y2 + nY * -15;

        // The calculation of the lines can be moved into the actor loop below
        // and then use the target actors hitbox radius as the number 
        // of units to move the two lines. This will make the laser more 
        // accurate if there are targets with different hitbox sizes.
        // The current shortes distance to a target. We start at infinity.
        float shortestDistance = Float.POSITIVE_INFINITY;
        Actor closest = null;

        for (Actor act : owner.getGameEngine().getCurrentLevel().getActors()) {

            // Only check agains the enemies
            if (act == owner || !(act instanceof Enemy)) {
                continue;
            }

            float x = (float) act.getPositionX();
            float y = (float) act.getPositionY();

            // We only want to hit targets that are in fron of the player, 
            // so we check the dot product between the laser heading and the
            // vector from the player to the target. If the result is negative
            // the target is behind the player so we skip this actor.
            if (((x2 - x1) * (x - x1) + (y2 - y1) * (y - y1)) < 0) {
                continue;
            }

            // This calculates the determinant of the matrix:
            // [ L_H.x L_T.x ]
            // [ L_H.y L_T.y ]
            // Where L_H is the heading vector of the laser and L_T is the vector
            // from the laser to the target actor. This is calculated for 
            // both the parallel lines.
            float d1 = (line1_x2 - line1_x1) * (y - line1_y1) - (line1_y2 - line1_y1) * (x - line1_x1);
            float d2 = (line2_x2 - line2_x1) * (y - line2_y1) - (line2_y2 - line2_y1) * (x - line2_x1);

            // If the sign is different the target is between the lines
            // so the laser is hitting the target.
            if (Math.signum(d1) != Math.signum(d2)) {

                float dist = (x - x1) * (x - x1) + (y - y1) * (y - y1);
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

        return (float) Math.sqrt(shortestDistance);
    }

}
