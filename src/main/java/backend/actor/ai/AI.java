/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package backend.actor.ai;

/**
 *
 * @author dogsh
 */
public interface AI {

    /**
     *
     * @param timePassed
     */
    public void updateBehaviour(double timePassed);
} 
