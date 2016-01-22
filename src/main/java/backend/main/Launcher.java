package backend.main;

import userinterface.GUIHandler;

/**
 * Launches the application.
 *
 * @author Kristian Honningsvag.
 */
public class Launcher {

    /**
     * Main method.
     *
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        GUIHandler.main("userinterface.GUIHandler");  // Needed in order for processing to run in NetBeans.
    }

}
