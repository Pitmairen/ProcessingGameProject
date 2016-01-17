package backend.actor;

import backend.GameEngine;
import backend.ParticleEmitter;
import java.util.ArrayList;
import java.util.Iterator;
import processing.core.PGraphics;
import processing.core.PImage;
import processing.core.PVector;
import userinterface.GUIHandler;

/**
 * The fire ball canon manages all the active fire balls.
 *
 * It uses a PGraphics objects for the drawing to be able to create a fading
 * effect for the balls and the particles when the ball explodes.
 *
 * @author pitmairen
 */
public class FireballCanon extends Actor {

    private final ArrayList<Fireball> balls;
    private final PGraphics canvas;
    private final PImage ballImage;
    private final ParticleEmitter particles; // Used for explosions

    // Used to calculate timeDelta for the particle emitter.
    private float timer = 0.0f;

    public FireballCanon(GameEngine gameEngine, GUIHandler guiHandler) {
        super(guiHandler.getWidth() / 2, guiHandler.getHeight() / 2,
                gameEngine, guiHandler);

        this.canvas = guiHandler.createGraphics(guiHandler.getWidth(),
                guiHandler.getHeight(), PGraphics.P2D);

        this.ballImage = guiHandler.loadImage("particle.png");
        this.balls = new ArrayList<>();
        this.canvas.ellipseMode(PGraphics.CENTER);
        this.canvas.imageMode(PGraphics.CENTER);
        this.particles = new ParticleEmitter(guiHandler);
    }

    /**
     * Fires a new fireball
     *
     * @param posX the start x position
     * @param posY the start y position
     * @param targetAngle the direction
     */
    public void fire(double posX, double posY, double targetAngle) {

        Fireball ball = new Fireball(posX, posY, this.gameEngine,
                this.guiHandler, targetAngle);

        this.gameEngine.getCurrentLevel().getProjectiles().add(ball);
        this.gameEngine.getCurrentLevel().getActors().add(ball);
        this.balls.add(ball);

    }

    @Override
    public void act() {

        // Do nothing
    }

    @Override
    public void draw() {

        this.canvas.beginDraw();

        // Creates the trail effect
        this.canvas.fill(0, 40);
        this.canvas.rect(0, 0,
                this.guiHandler.getWidth(), this.guiHandler.getHeight());

        this.canvas.blendMode(PGraphics.ADD);
        this.canvas.noStroke();

        float timeDelta = this.guiHandler.millis() - this.timer;
        particles.update(timeDelta / 25.0f);
        particles.draw(this.canvas);

        timer = this.guiHandler.millis();
        
        Iterator<Fireball> it = this.balls.iterator();
        while (it.hasNext()) {
            
            Fireball ball = it.next();
            
            ball.updatePosition();
            ball.draw(this.canvas);
            
            if (!ball.isAlive) {
                it.remove();
            }
        }

        this.canvas.blendMode(PGraphics.BLEND);
        this.canvas.endDraw();
        this.guiHandler.image(this.canvas, 0, 0);
    }

    private class Fireball extends Actor {

        private final int backgroundColor;
        private final float radius = 8.0f;
        private final float speed = 15.2f;
        private boolean isAlive = true;
        private boolean hasExploded = false;

        public Fireball(double positionX, double positionY, GameEngine gameEngine, GUIHandler guiHandler, double targetAngle) {
            super(positionX, positionY, gameEngine, guiHandler);
            accelerationX = 0;
            accelerationY = 0;
            hitBoxRadius = 8;
            drag = 0;
            hitPoints = 1;
            setSpeed(targetAngle);
            backgroundColor = guiHandler.color(guiHandler.random(10, 255),
                    guiHandler.random(10, 255), guiHandler.random(10, 255), 255);

        }

        @Override
        public void draw() {
            //Do nothing
        }

        @Override
        public void act() {

        }

        @Override
        public void collide(boolean isObject) {
            if (hasExploded) {
                return;
            }
            //if(isObject){
            hasExploded = true;
            FireballCanon.this.particles.emitParticles(50, new PVector((float) positionX, (float) positionY), backgroundColor);
            //}
            isAlive = false;
        }

        public void updatePosition() {
            positionX = positionX + speedX;
            positionY = positionY + speedY;
        }

        public void draw(PGraphics canvas) {
            if (!isAlive) {
                return;
            }

            canvas.tint(this.backgroundColor, 255);
            canvas.fill(this.backgroundColor, 200);

            canvas.image(FireballCanon.this.ballImage,
                    (float) this.positionX, (float) this.positionY,
                    (float) this.radius * 2, (float) this.radius * 2);
            canvas.ellipse((float) this.positionX, (float) this.positionY,
                    (float) this.radius / 2, (float) this.radius / 2);
        }

        private void setSpeed(double targetAngle) {
            speedX = speed * Math.cos(targetAngle);
            speedY = speed * Math.sin(targetAngle);
        }

    }

}
