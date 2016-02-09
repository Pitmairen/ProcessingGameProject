package backend.main;

import backend.resources.Image;
import backend.resources.ResourceManager;
import backend.resources.Shader;
import java.util.ArrayList;
import processing.core.PGraphics;
import processing.core.PImage;
import processing.opengl.PShader;
import userinterface.GUIHandler;

/**
 * The items drawn to the fading canvas get a fading trail when they move.
 *
 * This is used for the fireballs and the explosions.
 *
 * @author pitmairen
 */
public class FadingCanvas {

    /**
     * Represents an item that can be managed by the fading canvas.
     */
    public static interface Drawable {

        /**
         * Draws the item
         *
         * @param canvas the graphics object to use for the drawing
         */
        public void draw(PGraphics canvas);
    }

    private final PGraphics canvas;
    private final GUIHandler gui;
    private final ArrayList<Drawable> items;
    private final PShader bgShader;
    private final PImage bgNoise;
    
    // The background image
    private final PImage background; 
    
    /**
     * Constructor.
     */
    public FadingCanvas(GUIHandler gui, ResourceManager resources) {
        this.gui = gui;
        this.canvas = gui.createGraphics(gui.getWidth(),
                gui.getHeight(), PGraphics.P3D);
        this.items = new ArrayList<>();
        
        // Loads the background image from the src/main/resources/data folder.
        background = resources.getImage(Image.BACKGROUND_IMAGE);
        
        
        bgShader = resources.getShader(Shader.BG_SHADER);
        bgNoise = resources.getImage(Image.BG_SHADER_NOISE);
        bgShader.set("resolution", (float)gui.width, (float)gui.height);
    }

    /**
     * Adds an item that should be managed by the fading canvas.
     *
     * The fading canvas will make sure the item is drawn.
     *
     * @param item the item to add
     */
    public void add(Drawable item) {
        items.add(item);
    }

    /**
     * Draws all the items that are managed by the fading canvas.
     *
     * Draw must be called on each draw update. Because this canvas will draw
     * over the entire screen it must be called before any other drawing so that
     * nothing gets drawn over.
     */
    public void draw() {
        
        bgShader.set("currentTime", gui.millis()/1000.0f);
        gui.imageMode(PGraphics.CORNER);
        gui.shader(bgShader);
        gui.image(bgNoise, 0, 0, gui.width, gui.height);
        gui.resetShader();
        
        this.canvas.beginDraw();

        // Creates the fading tail effect. The second number (40) creates the 
        // tail effect, a lower number means a longer tail. This affects both
        // particles and the rockets.
        
        // Code for no background image
        this.canvas.fill(0, 40);
        this.canvas.rect(0, 0, this.canvas.width, this.canvas.height);
        
        // Code for background image
        this.canvas.tint(255, 40);
        this.canvas.imageMode(PGraphics.CORNER);
        this.canvas.image(background, 0, 0, this.canvas.width, this.canvas.height);

        
        for (Drawable it : items) {
            it.draw(this.canvas);
        }

        this.canvas.endDraw();
        gui.tint(255); // Reset tint 
        gui.blendMode(PGraphics.ADD);
        gui.image(this.canvas, 0, 0);
        gui.blendMode(PGraphics.BLEND);
    }

}
