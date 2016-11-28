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
    private int boardX;
    private int boardY;

    private boolean falling;
    private boolean jumping;

    public Player(int playerId, int startXPos, int startYPos, int size, Dimension boardDim) {
        //sets Graphics paramteres
        this.setPreferredSize(new Dimension(size, size));
        //
        this.playerSize = size;
        this.xPos = startXPos;
        this.yPos = startYPos;
        float red = (float) Math.random();
        float green = (float) Math.random();
        float blue = (float) Math.random();
        this.playerColor = new Color(red, green, blue);
        this.playerId = playerId;


    }

    void playerUpdatePosition(int newX, int newY) {
        if (validPos(newX, newY)) {
            this.xPos = newX;
            this.yPos = newY;
            this.setLocation(newX, newY);
            //System.out.println("I'm supposed to update the player position.");
        }
    }

    void jump() {
        this.jumping = true;
    }

    public String toString() {
        return "Player " + playerId + " is at x: " + (int) this.xPos +
                " - y: " + (int) this.yPos;
    }

    public void move(int direction) {
        switch (direction) {
            case KeyEvent.VK_LEFT:
                playerUpdatePosition(this.xPos - playerStep, this.yPos);
                break;

            case KeyEvent.VK_RIGHT:
                playerUpdatePosition(this.xPos + playerStep, this.yPos);
                break;

            default:
                break;

        }
    }

    public void checkFallingState() {
        if (jumping) return;
        if (falling) playerUpdatePosition(this.xPos, this.yPos + playerSize);
        falling = false;
    }

    public void checkJumpingState() {
        if (jumping) {
            playerUpdatePosition(this.xPos, this.yPos - this.playerSize);
            jumping = false;
            falling = true;
        }
    }

    public boolean getFalling() {
        return falling;
    }

    public boolean getJumping() {
        return jumping;
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

    public void setBoardSize(int x, int y) {
        this.boardX = x;
        this.boardY = y;
    }

    private boolean validPos(int x, int y) {
        return (x >=  0 && x <= (this.boardX - playerSize) && y >= 0 && y <= (boardY - playerSize));
    }
}
