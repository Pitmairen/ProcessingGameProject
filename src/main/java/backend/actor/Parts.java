package backend.actor;

import backend.actor.Item;
import backend.actor.Actor;
import backend.actor.Player;
import backend.main.GameEngine;
import backend.main.Vector;
import userinterface.Drawable;

/**
 * Parts dropped by defeated enemies. Increases current health.
 *
 * @author Kristian Honningsvag.
 */
public class Parts extends Item implements Drawable {

    // Color.
    private int[] bodyRGBA = new int[]{10, 240, 10, 255};

    /**
     * Constructor.
     */
    public Parts(Vector position, GameEngine gameEngine) {

        super(position, gameEngine);

        hitBoxRadius = 5;
        pullDistance = gameEngine.getCurrentLevel().getPlayer().getHitBoxRadius() * 8;
    }

    @Override
    public void draw() {
        // Draw main body.
        guiHandler.strokeWeight(0);
        guiHandler.stroke(bodyRGBA[0], bodyRGBA[1], bodyRGBA[2]);
        guiHandler.fill(bodyRGBA[0], bodyRGBA[1], bodyRGBA[2]);
        guiHandler.ellipse((float) this.getPosition().getX(), (float) this.getPosition().getY(), (float) hitBoxRadius * 2, (float) hitBoxRadius * 2);
    }

    @Override
    public void pickup(Actor looter) {
        currentHitPoints = 0;
        if (looter instanceof Player) {
            Player player = (Player) looter;
            player.addHitPoints(1);
        }
    }

    @Override
    public void die() {
        gameEngine.getCurrentLevel().getItems().remove(this);
    }

}
