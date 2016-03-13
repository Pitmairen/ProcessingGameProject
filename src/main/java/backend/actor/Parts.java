package backend.actor;

import backend.actor.Item;
import backend.actor.Actor;
import backend.actor.Player;
import backend.main.GameEngine;
import backend.main.Vector;
import backend.resources.Sound;
import userinterface.Drawable;

/**
 * Parts dropped by defeated enemies. Increases current health.
 *
 * @author Kristian Honningsvag.
 */
public class Parts extends Item implements Drawable {

    // Color.
    private int[] bodyBackgroundRGBA = new int[]{10, 240, 10, 100};
    private int[] bodyFillRGBA = new int[]{10, 240, 10, 180};

    /**
     * Constructor.
     */
    public Parts(Vector position, GameEngine gameEngine) {

        super(position, gameEngine);

        hitBoxRadius = 7;
        pullDistance = gameEngine.getCurrentLevel().getPlayer().getHitBoxRadius() * 8;
    }

    @Override
    public void draw() {

        // Draw main body.
        guiHandler.strokeWeight(1);
        guiHandler.stroke(bodyBackgroundRGBA[0], bodyBackgroundRGBA[1], bodyBackgroundRGBA[2], bodyBackgroundRGBA[3]);
        guiHandler.fill(bodyFillRGBA[0], bodyFillRGBA[1], bodyFillRGBA[2], bodyFillRGBA[3]);
        guiHandler.ellipse((float) this.getPosition().getX(), (float) this.getPosition().getY(), (float) hitBoxRadius * 2, (float) hitBoxRadius * 2);
        guiHandler.noFill();
    }

    @Override
    public void pickup(Actor looter) {
        currentHitPoints = 0;
        if (looter instanceof Player) {
            Player player = (Player) looter;
            player.addHitPoints(5);
            gameEngine.getSoundManager().play(Sound.HEALTH_PICKUP, getPosition());
        }
    }

    @Override
    public void die() {
        gameEngine.getCurrentLevel().getItems().remove(this);
    }

}
