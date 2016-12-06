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
    private static final int playerJumpStep = 21; // Has to be odd.
    private static final int playerStep = 2;
    private int playerAcceleration = 0;
    private int maxAcceleration = 8;
    private int xPos;
    private int yPos;
    private int velocity[];
    private Board board;
    private boolean jumping;

    public Player(int playerId, int startXPos, int startYPos, int size, Board b) {
        //sets Graphics paramteres
        this.setPreferredSize(new Dimension(size, size));
        //
        velocity = new int[2];
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

    public void jump() {
        if (this.velocity[1] == 0 && !this.jumping) {
            velocity[1] = -playerJumpStep;
            this.velocity = board.getValidVelocity(velocity);
            this.jumping = true;
        } else if (this.velocity[1] == 0 && this.jumping) {
            this.jumping = false;
        }
    }

    public void checkJumping() {
        velocity[1] = velocity[1] + 2;
        this.velocity = board.getValidVelocity(velocity);
    }

    public String toString() {
        return "Player " + playerId + " is at x: " + (int) this.xPos +
                " - y: " + (int) this.yPos;
    }

    public void move(int direction) {
        switch (direction) {
            case KeyEvent.VK_A:
            case KeyEvent.VK_LEFT:
                if (this.playerAcceleration < maxAcceleration) {
                    this.playerAcceleration++;
                }
                velocity[0] = -playerStep - playerAcceleration;
                this.velocity = board.getValidVelocity(velocity);
                break;
            case KeyEvent.VK_D:
            case KeyEvent.VK_RIGHT:
                if (this.playerAcceleration < maxAcceleration) {
                    this.playerAcceleration++;
                }
                velocity[0] = playerStep + playerAcceleration;
                this.velocity = board.getValidVelocity(velocity);
                break;

            case 0:
                this.playerAcceleration = 0;
                velocity[0] = 0;
                this.velocity = board.getValidVelocity(velocity);
                break;

            default:
                break;
        }
    }

    //===============================================Below this are methods for graphics rendering


    public void paintComponent(Graphics g) {
        this.setLocation(this.xPos, this.yPos);
        //System.out.println("Player: " + playerId + " is at: xpos: " + xPos + ", yPos: " + yPos);
        g.setColor(this.playerColor);
        g.fillRect(0, 0, this.playerSize, this.playerSize);
    }

    public int getPlayerId() {
        return this.playerId;
    }

    public int getPlayerSize() {
        return this.playerSize;
    }

    public int getPlayerX() {
        return this.xPos;
    }
    public int getPlayerY() {
        return this.yPos;
    }
    public short getPlayerXShort() {
        return (short) this.xPos;
    }
    public short getPlayerYShort() {
        return (short) this.yPos;
    }
    public void setX(int x) {
	this.xPos = x;
    }
    public void setY(int y) {
	this.yPos = y;
    }
    
    public void updatePosition() {
        //System.out.printf("%d | %d\n", xPos, yPos);
        // System.out.println("My vertical velocity is: " + velocity[1]);
        this.xPos = this.xPos + velocity[0];
        this.yPos = this.yPos + velocity[1];
    }
}
