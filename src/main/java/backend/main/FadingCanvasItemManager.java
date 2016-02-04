package backend.main;

import java.util.ArrayList;
import java.util.Iterator;
import processing.core.PGraphics;

/**
 * Manages generic items that should be drawn to the fading canvas.
 * 
 * This should be used for items that are dynamically added and removed from
 * the canvas during the game.
 * 
 * @author pitmairen
 */
public class FadingCanvasItemManager implements FadingCanvas.Drawable {


    /**
     * Represents a item that can be managed
     */
    public interface Item {

        /**
         * Draw the item to the canvas
         * 
         * @param canvas the canvas to draw to
         */
        public void draw(PGraphics canvas);
        
        /**
         * Returns true as long as the item is active and should be drawn 
         * to the canvas.
         * 
         * @return true if the item is alive
         */
        public boolean isAlive();
    }


    private final ArrayList<Item> items;
    
    /**
     * Constructs the manager
     */
    public FadingCanvasItemManager(){
        items = new ArrayList<>();
    }
    
    
    /** 
     * Adds an item that is to be managed
     * 
     * @param item the item to add
     */
    public void add(Item item){
        items.add(item);
    }
    
    /**
     * Draws all the items
     * 
     * @param canvas 
     */
    @Override
    public void draw(PGraphics canvas) {

        Iterator<Item> it = items.iterator();
        while (it.hasNext()) {

            Item item = it.next();

            item.draw(canvas);

            if(!item.isAlive()){
                it.remove();
            }
        }
    }
}
