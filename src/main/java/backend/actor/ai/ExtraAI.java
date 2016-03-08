/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package backend.actor.ai;

import backend.actor.Actor;
import backend.actor.enemy.Enemy;
import backend.main.GameEngine;

/**
 *
 * @author dogsh
 */
public class ExtraAI extends SimpleAI{

    public ExtraAI(GameEngine gameEngine, Actor target, Enemy enemy) {
        super(gameEngine, target, enemy);
    }
    
    @Override
    protected void createMovement(double timePassed){
        approachTarget();
    }
    
}
