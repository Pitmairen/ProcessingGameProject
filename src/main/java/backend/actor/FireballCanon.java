package backend.actor;

import backend.GameEngine;
import backend.ParticleEmitter;
import java.util.ArrayList;
import java.util.Iterator;
import processing.core.PGraphics;
import processing.core.PImage;
import processing.core.PVector;

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

    public FireballCanon(GameEngine gameEngine) {
        super(100, 100, gameEngine);

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

        Fireball ball = new Fireball(posX, posY, this.gameEngine, targetAngle);

        this.gameEngine.getCurrentLevel().getProjectiles().add(ball);
        this.gameEngine.getCurrentLevel().getActors().add(ball);
        this.balls.add(ball);

    }

    @Override
    public void act(double timePassed) {
        particles.update((float) timePassed / 25.0f);
    }
    
    
    @Override
    protected void checkActorCollisions() {
        // THis object does not interract with the other actors.
    }
    

    @Override
    protected void checkWallCollisions(double timePassed) {
        // THis object does not interract with the walls
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

        particles.draw(this.canvas);

        Iterator<Fireball> it = this.balls.iterator();
        while (it.hasNext()) {
            
            Fireball ball = it.next();
            
            ball.draw(this.canvas);
            
            if (!ball.isAlive) {
                it.remove();
            }
        }

        this.canvas.blendMode(PGraphics.BLEND);
        this.canvas.endDraw();
        this.guiHandler.image(this.canvas, 0, 0);
    }

    public class Fireball extends Actor {

        private final int backgroundColor;
        private final float radius = 8.0f;
        private final float speed = 0.8f;
        private boolean isAlive = true;
        private boolean hasExploded = false;

        public Fireball(double positionX, double positionY, GameEngine gameEngine, double targetAngle) {
            super(positionX, positionY, gameEngine);
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
        protected void checkWallCollisions(double timePassed) {
            if(hasExploded){
                return;
            }
            
            String wallCollision = gameEngine.getCollisionDetector().detectWallCollision(this);

            if (wallCollision != null) {
                setHitPoints(0);
                explode();
            }
        }
    
        @Override
        protected void checkActorCollisions() {

            if(hasExploded){
                return;
            }
            
            ArrayList<Actor> collisions = collisionDetector.detectActorCollision(this);

            if (collisions.size() > 0) {
                for (Actor actorInList : collisions) {
                    if (!(actorInList instanceof Player)) {
                        setHitPoints(0);
                    }
                    if ((actorInList instanceof Frigate)) {
                        gameEngine.getCurrentLevel().getPlayer().increaseScore(1);
                        setHitPoints(0);
                        explode();
                        return;
                    }
                }
            }
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
        
        private void explode(){
            hasExploded = true;
            FireballCanon.this.particles.emitParticles(50,
                    new PVector((float) positionX, (float) positionY),
                    backgroundColor);
            isAlive = false;
        }

    }

}
