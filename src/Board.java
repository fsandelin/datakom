/**
 * Created by falapen on 2016-11-14.
 */
public class Board {
    private int xSize;
    private int ySize;

    private Object graphRep = new Object(); //Placeholder for graphics representation

    private int[] obstacles = new int[10];

    public void draw(Player[] players) {
        for(int i = 0; i < players.length; i++) {
            System.out.println(players[i]);
        }
    }

    public Board(int xSize, int ySize) {
        this.xSize = xSize;
        this.ySize = ySize;
    }
}
