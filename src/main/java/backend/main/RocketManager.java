/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package backend.main;

import backend.actor.Rocket;
import backend.resources.Image;
import backend.resources.ResourceManager;
import java.util.ArrayList;
import java.util.Iterator;
import processing.core.PGraphics;
import processing.core.PImage;

/**
 * The rocket manager is responsible for drawing the rockets to the fading
 * canvas
 *
 * All rockets that should be drawn to the canvas must be added to the rocket
 * manager
 *
 * @author pitmairen
 */
public class RocketManager implements FadingCanvas.Drawable {

    private final ArrayList<Rocket> rockets;
    private final PImage rocketImage;

    /**
     * Creates a new rocket manager
     */
    public RocketManager(ResourceManager resources) {
        this.rocketImage = resources.getImage(Image.ROCKET);
        this.rockets = new ArrayList<>();
    }

    /**
     * Adds a rocket to the rocket manager
     *
     * The rocket will be drawn to the fading canvas as long as it is alive.
     *
     * @param rocket the rocket to add
     */
    public void addRocket(Rocket rocket) {
        rockets.add(rocket);
    }
    
    /**
     * Removes all the rockets managed by the rocket manager
     */
    public void clear(){
        rockets.clear();
    }
    
    @Override
    public void draw(PGraphics canvas) {

        // Add causes the colors of objects that are drawn on top of eachother
        // to be added together which creates a nice visual effect.
        canvas.blendMode(PGraphics.ADD);

        // Draw the rockets.
        Iterator<Rocket> it = this.rockets.iterator();
        while (it.hasNext()) {

            Rocket rocket = it.next();

            rocket.draw(canvas, rocketImage);

            if (rocket.isHasExploded() || rocket.getCurrentHitPoints() <= 0) {
                it.remove(); // Remove the rockets after they have exploded.
            }
        }

        canvas.blendMode(PGraphics.BLEND); // Reset blendMode
    }

}
