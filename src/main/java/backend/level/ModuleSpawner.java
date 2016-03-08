/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package backend.level;

import backend.actor.ModuleContainer;
import backend.main.FadingCanvasItemManager;
import backend.main.GameEngine;
import backend.main.RocketManager;
import backend.main.Vector;
import backend.shipmodule.EMPCannon;
import backend.shipmodule.LaserCannon;
import backend.shipmodule.RocketLauncher;
import backend.shipmodule.SeekerCannon;
import backend.shipmodule.ShipModule;
import java.util.ArrayList;
import java.util.Arrays;

/**
 *
 * @author dogsh
 */
public class ModuleSpawner {

    private GameEngine gameEngine;
    private ModuleContainer moduleContainer = null;
    private RocketManager rocketManager;
    private FadingCanvasItemManager fadingCanvasItems;
    
    private ArrayList<ShipModule> modulesAvailable;
    private ArrayList<Integer> modulesObtained;
    

    public ModuleSpawner(GameEngine gameEngine, RocketManager rocketManager, FadingCanvasItemManager fadingCanvasItems) {
        this.gameEngine = gameEngine;

        this.rocketManager = rocketManager;
        this.fadingCanvasItems = fadingCanvasItems;
        
        modulesAvailable = new ArrayList();
        modulesObtained = new ArrayList();
    }

    /**
     * Spawns a random module on somewhere on screen.
     */
    public void spawnRandomModule() {
        setModuleContainerAtRandomLocation();
        updateAvailableModules();
        displayUniqueModule();
    }

    /**
     * Gives a random value from 10 possible values
     *
     * @return A integer representing a x-value within your resolution.
     */
    private int randomXPos() {
        return (int) (gameEngine.getGuiHandler().getWidth() * Math.random());
    }

    /**
     * Gives a random value from 10 possible values
     *
     * @return A integer representing a y-value within your resolution.
     */
    private int randomYPos() {
        return (int) (gameEngine.getGuiHandler().getHeight() * Math.random());
    }

    /**
     * Sets the position of the Module container to a random location on the
     * screen
     */
    private void setModuleContainerAtRandomLocation() {
        int x = randomXPos();
        int y = randomYPos();
        moduleContainer = new ModuleContainer(new Vector(x, y, 0), gameEngine);
    }

    /**
     * Updates the available modules
     */
    private void updateAvailableModules() {
        modulesAvailable.clear();
        modulesAvailable.addAll(Arrays.asList(
                new RocketLauncher(moduleContainer, rocketManager),
                new SeekerCannon(moduleContainer, fadingCanvasItems),
                new LaserCannon(moduleContainer),
                new EMPCannon(moduleContainer, fadingCanvasItems)));
    }

    /**
     * Displays a module that haven't been displayed before. If the player
     * already have all obtainable modules, then no modules is displayed.
     */
    private void displayUniqueModule() {

        int valueToGet = (int) (Math.floor(Math.random() * modulesAvailable.size()));
        //System.out.println("Module to get: " + valueToGet);
        boolean moduleObtainedBefore = modulesObtained.contains(valueToGet);
        //System.out.println("Do i have that module: " + moduleObtainedBefore);
        if (modulesObtained.size() == modulesAvailable.size()) {
            //System.out.println("All modules has been obtained");
            //Do nothing, all modules has been obtained.
        } else if (!moduleObtainedBefore) {
            //System.out.println("Module is aquired: " + valueToGet);
            modulesObtained.add(valueToGet);
            moduleContainer.setModule(modulesAvailable.get(valueToGet));
            gameEngine.getCurrentLevel().getItems().add(moduleContainer);
            gameEngine.getCurrentLevel().getActors().add(moduleContainer);
        } else if (moduleObtainedBefore) {
            //System.out.println("Method is called again");
            spawnRandomModule();
        }

    }
}
