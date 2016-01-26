package backend.actor;

import backend.main.GameEngine;
import backend.main.NumberCruncher;
import backend.shipmodule.LightCannon;
import java.util.Random;
import userinterface.Drawable;

/**
 * A frigate. Small and fast.
 *
 * @author Kristian Honningsvag.
 */
public class Frigate extends Actor implements Drawable {

    // Color.
    private int[] bodyRGBA = new int[]{200, 30, 30, 255};

    // Modules.
    private LightCannon LightCannon = new LightCannon(this);

    Random random = new Random();
    private int attackDelayFactor = random.nextInt(100);

    /**
     * Constructor.
     */
    public Frigate(double positionX, double positionY, GameEngine gameEngine) {

        super(positionX, positionY, gameEngine);

        name = "Frigate";
        speedLimit = 0.4f;
        acceleration = 0.0015f;
        drag = 0.001f;
        hitBoxRadius = 15;
        bounceModifier = 0.6f;
        hitPoints = 10;
        mass = 30;
        collisionDamageToOthers = 2;
        attackDelay = 70;

        offensiveModules.add(LightCannon);
        currentOffensiveModule = LightCannon;
    }

    @Override
    public void act(double timePassed) {
        targetPlayer();
        fireAtPlayer();
        approachTarget(timePassed);
        super.act(timePassed);
    }

    @Override
    public void draw() {

        // Draw main body.
        guiHandler.strokeWeight(0);
        guiHandler.stroke(bodyRGBA[0], bodyRGBA[1], bodyRGBA[2]);
        guiHandler.fill(bodyRGBA[0], bodyRGBA[1], bodyRGBA[2]);
        guiHandler.ellipse((float) this.getPositionX(), (float) this.getPositionY(), (float) hitBoxRadius * 2, (float) hitBoxRadius * 2);

        // currentDefensiveModule.draw();
        currentOffensiveModule.draw();
    }

    /**
     * Sets heading towards the player.
     */
    private void targetPlayer() {
        double xVector = gameEngine.getCurrentLevel().getPlayer().getPositionX() - this.positionX;
        double yVector = gameEngine.getCurrentLevel().getPlayer().getPositionY() - this.positionY;
        heading = NumberCruncher.calculateAngle(xVector, yVector);
    }

    /**
     * Fires a bullet towards the player.
     */
    private void fireAtPlayer() {

        if (System.currentTimeMillis() - lastTimeFired > attackDelay * attackDelayFactor) {

            currentOffensiveModule.activate();
            lastTimeFired = System.currentTimeMillis();
            attackDelayFactor = random.nextInt(100);
        }
    }

    /**
     * Accelerate towards the target.
     */
    private void approachTarget(double timePassed) {

        if (speedT < speedLimit) {
            speedX = speedX + (acceleration * Math.cos(heading) * timePassed);
            speedY = speedY + (acceleration * Math.sin(heading) * timePassed);
        }
    }

}
