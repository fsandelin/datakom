/**
 * Created by falapen on 2016-11-13.
 */

import javax.swing.*;
import javax.swing.border.Border;
import java.awt.*;
import java.awt.Component;
import java.awt.Rectangle;
import java.awt.event.KeyEvent;

public class Player extends Component {
    private Rectangle pRect;

    private int playerId;
    private Color playerColor;
    private int playerStep = 4;
    private int xPos;
    private int yPos;

    private boolean falling;
    private boolean jumping;

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
	this.jumping = true;
    }

    public String toString() {
        return "Player " + playerId + " is at x: " + (int) this.pRect.getX() + " - y: " + (int) this.pRect.getY();
    }

     public void move(int direction) {
	switch(direction) {
	case KeyEvent.VK_LEFT:
	    playerUpdatePosition(this.xPos-playerStep, this.yPos);
	    break;
	    
	case KeyEvent.VK_RIGHT:
	    playerUpdatePosition(this.xPos+playerStep, this.yPos);
	    break;
	    
	default:
	    break;
	    
	}
    }

    public void checkFallingState() {
	if (jumping) return;
	if (falling) playerUpdatePosition(this.xPos, this.yPos+playerStep);
	falling = false;
    }

    public void checkJumpingState() {
	if (jumping) {
	    playerUpdatePosition(this.xPos, this.yPos-playerStep);
	    jumping = false;
	    falling = true;
	}
    }
}
