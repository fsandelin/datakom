/**
 * Created by falapen on 2016-11-13.
 */

import javax.swing.*;
import javax.swing.border.Border;
import java.awt.*;
import java.awt.Component;
import java.awt.Rectangle;

public class Player extends Component {
    private Rectangle pRect;

    private int playerId;
    private Color playerColor;

    public Player(int playerId, int startXPos, int startYPos, int size) {
        this.pRect = new Rectangle(new Point(startXPos, startYPos), new Dimension(size, size));

        float red = (float) Math.random();
        float green = (float) Math.random();
        float blue = (float) Math.random();
        this.playerColor = new Color(red, green, blue);

        this.playerId = playerId;
    }

    void playerUpdatePosition() {
        System.out.println("I'm supposed to update the player position.");
    }

    public String toString() {
        return "Player " + playerId + " is at x: " + (int) this.pRect.getX() + " - y: " + (int) this.pRect.getY();
    }
}
