package Backend;

import UserInterface.GUIHandler;

/**
 * All objects that shall be rendered must implement this interface.
 *
 * @author Kristian Honningsvag.
 */
public interface Drawable {

    /**
     * Renders the object in it's current state.
     *
     * @param guiHandler The GUI handler that will do the rendering.
     */
    public void draw(GUIHandler guiHandler);

}
