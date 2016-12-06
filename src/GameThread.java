import java.awt.*;
import java.awt.Graphics2D;
import java.awt.image.BufferStrategy;
import java.awt.Graphics;
import javax.swing.*;
import java.util.HashSet;
import java.awt.event.KeyEvent;
import java.util.*;

public class GameThread extends Thread{

    private Board board;
    private ArrayList<Player> playerArray;
    private Player player;
    private KeyboardController keyboardController;
    private long clock;
    private int timestep = 33;

    private boolean win;
    private static final int playerSize = 30;

    /**
     * Game setup
     *
     * @param xSize Width of the game-board
     * @param ySize Heigth of the game-board
     */
    public GameThread(int xSize, int ySize) {
	super("GameThread");
        this.board = new Board(xSize, ySize);
        int padding = 5;
        this.win = false;

        //Initialize keyboardcontrols
        keyboardController = new KeyboardController();
        board.initKeyboard(keyboardController);

        //Creating and adding playerarray and 1 player and setting the 0-index as current player
	playerArray = new ArrayList<Player>();
        player = new Player("Snoop Dogg", 150, ySize - playerSize - 100, playerSize, board);
	playerArray.add(player);
        board.addPlayer(player);
        //

        //Adding some obstructions to the game
        for (int i = 0; i < 1; i++) {
            Obstruction o = new Obstruction(400, 450, new Dimension(150, 30));
            o.setColor(new Color(120, 120, 120));
            board.addObstruction(o);
        }
        //

        clock = System.currentTimeMillis();
    }

    public Player getPlayer() {
	return this.player;
    }

    public ArrayList<Player> getPlayerArray() {
	return this.playerArray;
    }
    
    public String toString() {
        return "Current game is running";
    }

    /**
     * Following block is for keyboard control and ticks
     */

    private void manageKeys() {
        HashSet<Integer> currentKeys = keyboardController.getActiveKeys();
//        System.out.println(currentKeys);

        if (currentKeys.contains(KeyEvent.VK_RIGHT) || currentKeys.contains(KeyEvent.VK_D)) {
            player.move(KeyEvent.VK_RIGHT);
        } else if (currentKeys.contains(KeyEvent.VK_LEFT) || currentKeys.contains(KeyEvent.VK_A)) {
            player.move(KeyEvent.VK_LEFT);
        } else {
            player.move(0);
        }

        if (currentKeys.contains(KeyEvent.VK_SPACE) || currentKeys.contains(KeyEvent.VK_UP)) {
            player.jump();
        }
        //System.out.println("Player Moved");
    }
    
    
    public void run() {
        // Game loop
	while (!this.checkWinState()) {
	    if ((System.currentTimeMillis() - clock) >= timestep) {
		manageKeys();
		player.checkJumping();
		player.updatePosition();
		win = board.win();
		clock = System.currentTimeMillis();
	    }
	    this.updateBoard();
        }
	
    }

    public int[] addPlayerToServer(String alias, Color playerColor) {

        int[] validPosition = board.getValidPlayerPosition();
        int x = validPosition[0];
        int y = validPosition[1];

        //public Player(String playerId, int startXPos, int startYPos, int size, Board b)

        Player p = new Player(alias, x, y, playerSize, board);
        playerArray.add(p);
        board.addPlayer(p);

        return validPosition;
    }

    public void addPlayerToClient(int x, int y, String alias) {
        Player p = new Player(alias, x, y, playerSize, board);
        playerArray.add(p);
        board.addPlayer(p);

    }

    public void updateBoard() {
        this.board.update();
    }

    public boolean checkWinState() {
        return win;
    }
}
