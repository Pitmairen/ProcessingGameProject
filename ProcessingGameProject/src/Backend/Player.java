package Backend;

/**
 * The player.
 *
 * @author Kristian Honningsvag.
 */
public class Player {

    private float playerPositionX;
    private float playerPositionY;
    private float playerSpeedX;
    private float playerSpeedY;

    /**
     * Constructor.
     *
     * @param positionX Players x position.
     * @param positionY Players y position.
     * @param speedX Players speed in x direction.
     * @param speedY Players speed in y direction.
     */
    public Player(int positionX, int positionY, int speedX, int speedY) {
        this.playerPositionX = positionX;
        this.playerPositionY = positionY;
        this.playerSpeedX = speedX;
        this.playerSpeedY = speedY;
    }

    public void move() {
        playerPositionX = playerPositionX + playerSpeedX;
        playerPositionY = playerPositionY + playerSpeedY;
    }

    public void accelerate() {
        playerSpeedX++;
    }

    public void setPlayerPositionX(float playerPositionX) {
        this.playerPositionX = playerPositionX;
    }

    public void setPlayerPositionY(float playerPositionY) {
        this.playerPositionY = playerPositionY;
    }

    public void setPlayerSpeedX(float playerSpeedX) {
        this.playerSpeedX = playerSpeedX;
    }

    public void setPlayerSpeedY(float playerSpeedY) {
        this.playerSpeedY = playerSpeedY;
    }

    public float getPlayerPositionX() {
        return playerPositionX;
    }

    public float getPlayerPositionY() {
        return playerPositionY;
    }

    public float getPlayerSpeedX() {
        return playerSpeedX;
    }

    public float getPlayerSpeedY() {
        return playerSpeedY;
    }
}
