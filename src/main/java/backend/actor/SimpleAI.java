/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package backend.actor;

import backend.main.GameEngine;
import backend.main.NumberCruncher;
import backend.main.Timer;
import java.util.Random;

/**
 *
 * @author dogsh
 */
public class SimpleAI implements AI {

    private double xVector = 0;
    private double yVector = 0;
    private double timeReference = 0;
    private GameEngine gameEngine;
    private Actor target;
    private double heading;
    private Enemy enemy;
    
    private double lastTimeFired = 0;
    private double attackDelayFactor = 1;
    private double attackDelay = 2000;
    private Random random;
    
    public SimpleAI(GameEngine gameEngine, Actor target, Enemy enemy) {
        this.gameEngine = gameEngine;
        this.target = target;
        this.enemy = enemy;
        random = new Random();
    }
    
    
    @Override
    public void updateBehaviour(double timePassed) {
        targetPlayerLocation();
        fireAtPlayer();
        createMovement(timePassed);
    }
    
    /**
     * Sets heading towards the players location.
     */
    private void targetPlayerLocation() {

        xVector = target.getPositionX() - enemy.getPositionX();
        yVector = target.getPositionY() - enemy.getPositionY();
        heading = NumberCruncher.calculateAngle(xVector, yVector);
        enemy.setHeading(heading);
    }

    /**
     * Fires a bullet towards the player.
     */
    private void fireAtPlayer() {

        if (System.currentTimeMillis() - lastTimeFired > attackDelay * attackDelayFactor) {
            
            enemy.getCurrentOffensiveModule().activate();
            lastTimeFired = System.currentTimeMillis();
            attackDelayFactor = random.nextFloat() + 1;
        }
    }

    /**
     * Accelerate into the target.
     */
    protected void approachTarget(double timePassed) {
        if (enemy.getSpeedT() < enemy.getSpeedLimit()) {
            setSpeeds(timePassed);
        }
    }

    /**
     * Accelerate near the target, but not into the target.
     *
     * @param timePassed
     */
    private void circleAroundTarget(double timePassed, int minDistance) {

        double distance = Math.sqrt(Math.pow(xVector, 2) + Math.pow(yVector, 2)); //Hypotenus calculated

        if (distance > minDistance) {
            timeReference = timePassed;
            approachTarget(timePassed);
        } else if (distance <= minDistance) {
            heading -= Math.PI / 4;
            setSpeeds(timePassed);
        }
    }

    private void setSpeeds(double timePassed) {
        double speedX = enemy.getSpeedX() + (enemy.getAcceleration() * Math.cos(heading) * timePassed);
        enemy.setSpeedX(speedX); 
        double speedY = enemy.getSpeedY() + (enemy.getAcceleration() * Math.sin(heading) * timePassed);
        enemy.setSpeedY(speedY); 
    }
    
    protected void createMovement(double timePassed){
        circleAroundTarget(timePassed, 600);
    }
}
