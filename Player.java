/**
 * Created by falapen on 2016-11-13.
 */
import java.awt.*;
import java.awt.Component;

public class Player {
    private int xPos;
    private int yPos;

    private int xSize;
    private int ySize;

    private Color playerColor;

    public Player(int startXPos, int startYPos, int xSize, int ySize) {
        this.xPos = startXPos;
        this.yPos = startYPos;
        this.xSize = xSize;
        this.ySize = ySize;

        float red = (float) Math.random();
        float green = (float) Math.random();
        float blue = (float )Math.random();
        this.playerColor = new Color(red, green, blue);
    }

    void playerUpdatePosition() {
        System.out.println("Just a test");
    }

}
