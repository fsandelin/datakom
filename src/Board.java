import java.awt.*;

/**
 * Created by falapen on 2016-11-14.
 */
public class Board {
    private Rectangle boardRect;
    private GameGraphic graphRep; //Placeholder for graphics representation

    private int[] obstacles = new int[10];

    public void draw(Player[] players) {
        graphRep = new GameGraphic((int) boardRect.getWidth(), (int) boardRect.getHeight());
        for(int i = 0; i < players.length; i++) {
            System.out.println(players[i]);
        }
    }

    public Board(int xSize, int ySize) {
        boardRect = new Rectangle(new Dimension(xSize, ySize));
    }
}
