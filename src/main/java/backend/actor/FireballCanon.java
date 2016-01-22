package backend.actor;

import backend.main.GameEngine;
import backend.main.ParticleEmitter;
import java.util.ArrayList;
import java.util.Iterator;
import processing.core.PGraphics;
import processing.core.PImage;
import processing.core.PVector;

/**
 * The fire ball canon manages all the active fire balls.
 *
 * It uses a offscreen PGraphics objects for the drawing to be able to 
 * create a fading effect for the balls and the particles when the ball explodes.
 * 
 * It uses the particle emitter object to draw the particles when a 
 * ball is exploding. 
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
    public void draw() {

        this.canvas.beginDraw();

        // Creates the fading tail effect. The second number (40) creates the 
        // tail effect, a lower number means a longer tail. This affects both
        // particles and the fireballs.
        this.canvas.fill(0, 40);
        this.canvas.rect(0, 0,
                this.guiHandler.getWidth(), this.guiHandler.getHeight());

        // Add causes the colors of objects that are drawn on top of eachother
        // to be added together which creates a nice visual effect.
        this.canvas.blendMode(PGraphics.ADD);
        this.canvas.noStroke();

        // Draw the particles 
        particles.draw(this.canvas);

        // Draw the balls
        Iterator<Fireball> it = this.balls.iterator();
        while (it.hasNext()) {
            
            Fireball ball = it.next();
            
            ball.draw(this.canvas);
            
            if (ball.hasExploded || ball.getHitPoints() <= 0) {
                it.remove(); // Remove the balls after it has exploded
            }
        }

        this.canvas.blendMode(PGraphics.BLEND);
        this.canvas.endDraw();
        this.guiHandler.image(this.canvas, 0, 0);
    }

    
    /**
     * The fireballs us implemented as a internal class because 
     * they need to be drawn on a offscreen graphics object to create
     * the trail/fading effect.
     * 
     * It does not implement the normal draw method, but instead it 
     * uses a special draw method that is called from the FireballCanon class.
     * which passes in the graphics object that the balls are drawn onto.
     * 
     */
    public class Fireball extends Actor {

        private final int backgroundColor;
        private final float radius = 8.0f;
        private final float speed = 0.8f;
        private boolean hasExploded = false;

        public Fireball(double positionX, double positionY, GameEngine gameEngine, double targetAngle) {
            super(positionX, positionY, gameEngine);
            accelerationX = 0;
            accelerationY = 0;
            hitBoxRadius = 8;
            drag = 0;
            hitPoints = 1;
            mass = 3;
            setSpeed(targetAngle);
            backgroundColor = guiHandler.color(guiHandler.random(10, 255),
                    guiHandler.random(10, 255), guiHandler.random(10, 255), 255);

        }

        @Override
        public void draw() {
            //Do nothing
        }

        @Override
        public void act(double timePassed) {
            super.addFriction(timePassed);
            super.updatePosition(timePassed);
            checkWallCollisions(timePassed);
            checkActorCollisions(timePassed);
        }

        @Override
        protected void checkWallCollisions(double timePassed) {
            if (hasExploded) {
                return;
            }

            String wallCollision = gameEngine.getCollisionDetector().detectWallCollision(this);

            if (wallCollision != null) {
                setHitPoints(0);
                explode();
            }
        }
    
        @Override
        protected void checkActorCollisions(double timePassed) {

            if(hasExploded){
                return;
            }

            ArrayList<Actor> collisions = collisionDetector.detectActorCollision(this);

            if (collisions.size() > 0) {
                for (Actor actorInList : collisions) {
                    if ((!(actorInList instanceof Player)) && !(actorInList instanceof Fireball)) {
                        elasticColision(this, actorInList, timePassed);
                        setHitPoints(0);
                        explode();
                        if ((actorInList instanceof Frigate)) {
                            actorInList.modifyHitPoints(-3);
                            gameEngine.getCurrentLevel().getPlayer().increaseScore(1);
                        }
                    }
                }
            }
        }
        

        public void draw(PGraphics canvas) {
            if (hasExploded) {
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
        
        public void explode(){
            hasExploded = true;
            FireballCanon.this.particles.emitParticles(50,
                    new PVector((float) positionX, (float) positionY),
                    backgroundColor);
        }

    }

}
