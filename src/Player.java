/**
 * Created by falapen on 2016-11-13.
 */
import javax.swing.*;
import javax.swing.border.Border;
import java.awt.*;
import java.awt.Component;
import java.awt.Rectangle;

public class Player extends Component{
    private int xPos;
    private int yPos;

    private int size;
    private int playerId;

    private Color playerColor;

    public Player(int playerId, int startXPos, int startYPos, int size) {
        this.xPos = startXPos;
        this.yPos = startYPos;
        this.size = size;

        float red = (float) Math.random();
        float green = (float) Math.random();
        float blue = (float )Math.random();
        this.playerColor = new Color(red, green, blue);

        this.playerId = playerId;
    }

    void playerUpdatePosition() {
        System.out.println("I'm supposed to update the player position.");
    }

    int getXPos() {
        return this.xPos;
    }

    int getyPos() {
        return this.yPos;
    }

    public String toString() {
        return "Player " + playerId + "is at x: " + xPos + " - y: " + yPos;
    }
}
