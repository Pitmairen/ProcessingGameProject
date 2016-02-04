package backend.resources;

import java.util.HashMap;
import processing.core.PApplet;
import processing.core.PImage;

/**
 * Manages the various data resources that the game requires.
 *
 * This can be images, sounds etc.
 *
 * @author pitmairen
 */
public class ResourceManager {

    private final PApplet app;
    private final HashMap<Image, PImage> images;

    /**
     * Constructs the resource manager
     *
     * @param app the processing app
     */
    public ResourceManager(PApplet app) {
        this.app = app;
        images = new HashMap<>();

    }

    /**
     * Register a new image to the manager
     *
     * @param imageID the image id
     * @param path the image path
     */
    public void add(Image imageID, String path) {
        images.put(imageID, app.loadImage(path));
    }

    /**
     * returns the image associated with a specific ID
     *
     * @param imageID the id of the image
     * @return the image object
     */
    public PImage getImage(Image imageID) {
        return images.get(imageID);
    }

}
