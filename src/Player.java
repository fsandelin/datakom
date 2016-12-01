/**
 * Created by falapen on 2016-11-13.
 */

import javax.swing.*;
import javax.swing.border.Border;
import java.awt.*;
import java.awt.Component;
import java.awt.Rectangle;
import java.awt.event.KeyEvent;

public class Player extends JComponent {
    private int playerSize;

    private int playerId;
    private Color playerColor;
    private static final int playerStep = 7;
    private int xPos;
    private int yPos;
    private int vVelocity;
    private int hVelocity;
    private Board board;
    
    public Player(int playerId, int startXPos, int startYPos, int size, Board b) {
        //sets Graphics paramteres
        this.setPreferredSize(new Dimension(size, size));
        this.board = b;
        this.playerSize = size;
        this.xPos = startXPos;
        this.yPos = startYPos;
        float red = (float) Math.random();
        float green = (float) Math.random();
        float blue = (float) Math.random();
        this.playerColor = new Color(red, green, blue);
        this.playerId = playerId;


    }

    void jump() {
        // this.jumping = true;
    }

    public String toString() {
        return "Player " + playerId + " is at x: " + (int) this.xPos +
                " - y: " + (int) this.yPos;
    }

    public void move(int direction) {
        switch (direction) {
            case KeyEvent.VK_LEFT:
                this.hVelocity = board.getValidHVelocity(-1);
                break;

            case KeyEvent.VK_RIGHT:
		this.hVelocity = board.getValidHVelocity(1);
                break;

            default:
		this.hVelocity = board.getValidHVelocity(0);
                break;

        }
    }

    //===============================================Below this are methods for graphics rendering


    public void paintComponent(Graphics g) {

        this.setLocation(this.xPos, this.yPos);
//        System.out.println("Player: " + playerId + " is at: xpos: " + xPos + ", yPos: " + yPos);
        g.setColor(this.playerColor);
        g.fillRect(0, 0, this.playerSize, this.playerSize);
    }

    public int getPlayerId() {
        return this.playerId;
    }

    public void updatePosition() {
	this.xPos = this.xPos + hVelocity;
	this.yPos = this.yPos + vVelocity;
    }
}
