package ProcessingGameProject;

/**
 * The player.
 *
 * @author Kristian Honningsvag.
 */
public class Player {

    int playerPositionX;
    int playerPositionY;
    int playerSpeedX;
    int playerSpeedY;

    /**
     * Constructor.
     *
     * @param positionX Players x position.
     * @param positionY Players y position.
     * @param speedX Players speed in x direction.
     * @param speedY Players speed in y direction.
     */
    Player(int positionX, int positionY, int speedX, int speedY) {
        this.playerPositionX = positionX;
        this.playerPositionY = positionY;
        this.playerSpeedX = speedX;
        this.playerSpeedY = speedY;
    }

    public void accelerate() {
        playerSpeedX++;
    }

    public void setPlayerPositionX(int playerPositionX) {
        this.playerPositionX = playerPositionX;
    }

    public void setPlayerPositionY(int playerPositionY) {
        this.playerPositionY = playerPositionY;
    }

    public void setPlayerSpeedX(int playerSpeedX) {
        this.playerSpeedX = playerSpeedX;
    }

    public void setPlayerSpeedY(int playerSpeedY) {
        this.playerSpeedY = playerSpeedY;
    }

    public int getPlayerPositionX() {
        return playerPositionX;
    }

    public int getPlayerPositionY() {
        return playerPositionY;
    }

    public int getPlayerSpeedX() {
        return playerSpeedX;
    }

    public int getPlayerSpeedY() {
        return playerSpeedY;
    }
}
