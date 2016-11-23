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
    private int playerStep = 4;

    public Player(int playerId, int startXPos, int startYPos, int size) {
        this.pRect = new Rectangle(new Point(startXPos, startYPos), new Dimension(size, size));

        float red = (float) Math.random();
        float green = (float) Math.random();
        float blue = (float) Math.random();
        this.playerColor = new Color(red, green, blue);

        this.playerId = playerId;
    }

    void playerUpdatePosition(int newX, int newY) {
	this.xPos = newX;
	this.yPos = newY;
        System.out.println("I'm supposed to update the player position.");
    }
    
    void jump() {
	System.out.println("Player jumped!");
    }

    public String toString() {
        return "Player " + playerId + " is at x: " + (int) this.pRect.getX() + " - y: " + (int) this.pRect.getY();
    }

     public void move(int direction) {
	switch(direction) {
	case KeyEvent.VK_LEFT:
	    playerUpdatePosition(getXPos()-playerStep, getYPos());
	    break;
	    
	case KeyEvent.VK_RIGHT:
	    playerUpdatePosition(getXPos()+playerStep, getYPos());
	    break;
	    
	default:
	    break;
	    
	}
    }
}
