package userinterface;

import java.util.ArrayList;
import processing.core.PApplet;
import processing.core.PFont;
import processing.event.KeyEvent;
import processing.event.MouseEvent;

/**
 * A simple reusable menu
 *
 * @author pitmairen
 */
public class Menu {

    /**
     * A menu item listener that will be called when a menu item is selected.
     */
    public interface ItemListener {

        /**
         * Called when the item associated with this listener is selected.
         */
        public void selected();
    }

    private final PApplet app;
    private final ArrayList<Item> items;

    private final PFont titleFont;
    private final PFont itemFont;

    // Menu bounds
    private float x;
    private float y;
    private float width;
    private float height;

    // Current item index. Can be -1 when no item is selected to prevent 
    // the selected menu item to be selected when the menu is shown after
    // the player has died. This happens because the "enter" key is used 
    // to activate the menu after the player dies and because "enter" is 
    // used to activate the current item in the menu this "enter" key press
    // will immediately triggers the selection of the selected item. The 
    // currentItem index is initially set to -1 to prevent this.
    private int currentItem;

    // Font size for the title
    private int titleSize = 60;

    // True if the menu is currently active
    private boolean isActive = false;

    // The title string
    private final String menuTitle;
    
    private final static float TOP_PADDING = 100f;
    private final static float TITLE_HEIGHT = 120f;
    private final static float TITLE_MARGIN = 80f; // Distance from title to first item.
    private final static float ITEM_HEIGHT = 80f;

    
    /**
     * Creates a new menu.
     *
     * @param title the menu title
     * @param app the processing app
     */
    public Menu(String title, PApplet app) {
        items = new ArrayList<>();
        this.app = app;
        titleFont = app.createFont("titleFont.ttf", titleSize);
        itemFont = app.createFont("menuItemFont.ttf", 40);
        currentItem = 0;
        this.menuTitle = title;
        init();
    }

    /**
     * Adds a new item to the menu.
     *
     * @param title the item title
     * @param listener the listener that will be called when the item is
     * selected
     */
    public void addItem(String title, ItemListener listener) {

        items.add(new Item(title, listener, calculateItemWidth(title)));

    }

    /**
     * Set the bounds that will limit the size of the menu.
     *
     * @param x the x-position of the upper left corner of the menu.
     * @param y the y-position of the upper left corner of the menu.
     * @param width the width of the menu
     * @param height the height of the menu.
     */
    public void setBounds(float x, float y, float width, float height) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        calculateTitleSize();
    }

    /**
     * Show the menu.
     *
     * The menu will start to draw itself and listen for input events after this
     * method has been called.
     */
    public void show() {

        // Don't activate multiple times
        if (isActive) {
            return;
        }

        currentItem = -1;
        registerMethods();
        isActive = true;
    }

    /**
     * Hide the menu
     */
    public void hide() {
        unregisterMethods();
        isActive = false;
    }

    /**
     * Draws the menu. Only public because it is required by processing.
     *
     * This should not be called externally, the menu will draw register itself
     * to the processing drawing system.
     *
     */
    public void draw() {

        app.noStroke();
        app.pushMatrix();
        app.translate(x, y);

        app.translate(0, TOP_PADDING);
        drawTitle(menuTitle);
        app.translate(0, TITLE_HEIGHT + TITLE_MARGIN);

        for (Item item : items) {
            drawItem(item);
            app.translate(0, 80);
        }
        app.popMatrix();

    }

    /**
     * Get key events. Only public because it is required by processing.
     *
     * @param event
     */
    public void keyEvent(KeyEvent event) {
        if (event.getAction() != KeyEvent.RELEASE || event.isAutoRepeat()) {
            return;
        }

        switch (event.getKeyCode()) {
            case 40: // Up key
                stepCurrentItem(1);
                break;
            case 38: // Down key
                stepCurrentItem(-1);
                break;
            case 10: { // Enter
                if (currentItem != -1) {
                    items.get(currentItem).select();
                } else {
                    stepCurrentItem(1);
                }
                break;
            }
        }
    }

    /**
     * Get mouse events. Only public because it is required by processing.
     *
     * @param event
     */
    public void mouseEvent(MouseEvent event) {

        float firstItem = y + TOP_PADDING + TITLE_HEIGHT + TITLE_MARGIN;

        if (event.getY() < firstItem) {
            return;
        }

        float diff = event.getY() - firstItem;

        int index = (int) (diff / ITEM_HEIGHT);

        if (index < items.size()) {

            float midDiff = Math.abs((width / 2) - event.getX());
            if (midDiff < ((items.get(index).width / 2) + 50)) {
                setCurrentItem(index);
                if (event.getAction() == MouseEvent.RELEASE) {
                    items.get(index).select();
                }
            }
        }

    }

    /**
     * Calculates the size of the title font so that it fits on the screen.
     */
    private void calculateTitleSize() {

        app.textFont(titleFont);
        while (app.textWidth(menuTitle) > (width - 50)) {
            titleSize -= 5;
            app.textSize(titleSize);
        }

    }

    private void stepCurrentItem(int step) {
        currentItem = Math.floorMod(currentItem + step, items.size());
    }

    private void setCurrentItem(int index) {
        currentItem = index;
    }

    private void drawTitle(String title) {
        app.fill(0xdd333333);
        app.rect(0, 0, width, TITLE_HEIGHT);
        app.fill(0xffD0604C);
        app.textFont(titleFont);
        app.textSize(titleSize);
        app.textAlign(PApplet.CENTER, PApplet.CENTER);
        app.text(title, width / 2, TITLE_HEIGHT/2);
    }

    private void drawItem(Item item) {
        app.textFont(itemFont);
        app.textAlign(PApplet.CENTER, PApplet.CENTER);
        if (currentItem != -1 && item == items.get(currentItem)) {
            // Draw current selection rectangle
            app.fill(0xdd333333);
            app.rectMode(PApplet.CENTER);
            app.rect(width / 2, ITEM_HEIGHT/2, app.textWidth(item.title) * 1.2f, ITEM_HEIGHT*0.6f);
            app.rectMode(PApplet.CORNER);

            app.fill(0xffD09328);
        } else {
            app.fill(0xffCB8F26);
            app.fill(0xff999999);
        }

        app.text(item.title, width / 2, ITEM_HEIGHT/2);
    }

    private void registerMethods() {
        app.registerMethod("draw", this);
        app.registerMethod("keyEvent", this);
        app.registerMethod("mouseEvent", this);
    }

    private void unregisterMethods() {
        app.unregisterMethod("draw", this);
        app.unregisterMethod("keyEvent", this);
        app.unregisterMethod("mouseEvent", this);
    }

    private float calculateItemWidth(String title) {
        app.textFont(itemFont);
        return app.textWidth(title);
    }

    private void init() {
        setBounds(0, 0, app.width, app.height);
    }

    /**
     * Represents a menu item
     */
    private class Item {

        private final String title;
        private final ArrayList<ItemListener> listeners;
        private final float width;

        /**
         * Creates a new item
         *
         * @param title the item title
         * @param listener the item listener
         * @param width the width of the item used to calculate when the mouse
         * enters the item.
         */
        public Item(String title, ItemListener listener, float width) {
            listeners = new ArrayList<>();
            this.title = title;
            this.width = width;
            listeners.add(listener);
        }

        /**
         * Calls the listener that is connected to this item.
         */
        public void select() {
            for (ItemListener listener : listeners) {
                listener.selected();
            }
        }
    }
}
