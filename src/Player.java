/**
 * Created by falapen on 2016-11-13.
 */

import javax.swing.*;
import javax.swing.border.Border;
import java.awt.*;
import java.awt.Component;
import java.awt.Rectangle;
import java.awt.event.KeyEvent;

public class Player {
    private int playerSize;
    private String playerAlias;
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

    private static final int aliasDisplacement = 20;

    public Player(String playerAlias, int playerId, int startXPos, int startYPos, int size, Board b) {
        //sets Graphics paramteres
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
        this.playerAlias = playerAlias;
        this.playerId = playerId;
        System.out.println("Player created with id: " + Integer.toString(this.playerId));
    }

    /**
     * Om den vertikala hastigheten är 0 och vi inte hoppar just nu,
     * då kan vi hoppa, dvs sätta den vertikala hastigheten till
     * -playerJumpStep och kolla ifall vi får hoppa så långt.
     * Om vi försöker hoppa när vi redan hoppat så sätter vi jumping till
     * false. Detta löser edge caset när vi är i den hösta punkten
     * i vårt hopp och hastigheten är 0.
     */
    public void jump() {
        if (this.velocity[1] == 0 && !this.jumping) {
            velocity[1] = -playerJumpStep;
            this.velocity = board.getValidVelocity(velocity);
            this.jumping = true;
        } else if (this.velocity[1] == 0 && this.jumping) {
            this.jumping = false;
        }
    }

    /**
     * Försök att öka den vertikala hastigheten (neråt) med 2.
     * Detta gör så att spelaren faller när den hoppar eller går av en kant.
     */
    public void checkJumping() {
        velocity[1] = velocity[1] + 2;
        this.velocity = board.getValidVelocity(velocity);
    }

    public String toString() {
        return "Player " + playerAlias + " is at x: " + (int) this.xPos + " - y: " + (int) this.yPos + " and has id: " + (int) this.playerId;
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

    public void draw(Graphics g) {
        g.setColor(this.playerColor);
        g.fillRect(xPos, yPos, playerSize, playerSize);
	g.setColor(Color.black);
	g.drawString(playerAlias, xPos, yPos - 10);
    }

    public String getPlayerAlias() {
        return this.playerAlias;
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

    public void setPlayerId(int id) {
        this.playerId = id;
    }

    public int getPlayerId() {
        return this.playerId;
    }


    public void updatePosition() {
        //System.out.printf("%d | %d\n", xPos, yPos);
        // System.out.println("My vertical velocity is: " + velocity[1]);
        this.xPos = this.xPos + velocity[0];
        this.yPos = this.yPos + velocity[1];
    }

    public Color getPlayerColor() {
        return this.playerColor;
    }

    public void setPlayerColor(Color c) {
        this.playerColor = c;
    }

    public Rectangle generateRectangle() {
        Rectangle rect = new Rectangle(this.xPos, this.yPos, this.playerSize, this.playerSize);
        return rect;
    }
}
