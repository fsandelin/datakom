import java.awt.*;
import java.awt.Graphics2D;
import java.awt.image.BufferStrategy;
import java.awt.Graphics;
import javax.swing.*;
import java.util.HashSet;
import java.awt.event.KeyEvent;
import java.util.*;

public class GameThread extends Thread {

    private Board board;
    private Player player;
    private KeyboardController keyboardController;
    private long clock;
    private int timestep = 33;

    private boolean win;
    private static final int playerSize = 30;

    private boolean render;

    /**
     * Game setup
     *
     * @param xSize Width of the game-board
     * @param ySize Heigth of the game-board
     */
    public GameThread(int xSize, int ySize, String alias) {
        super("GameThread");
        this.board = new Board(xSize, ySize);
        this.render = true;
        int padding = 5;
        this.win = false;

        //Initialize keyboardcontrols
	
        keyboardController = new KeyboardController();
        board.initKeyboard(keyboardController);

        player = new Player(alias, 0, 150, ySize - playerSize - 100, playerSize, board);
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

    public GameThread(int xSize, int ySize, String alias, boolean render) {
        this(xSize, ySize, alias);
        this.render = render;
    }

    public Player getPlayer() {
        return this.player;
    }

    public void setPlayerId(int id) {
        this.player.setPlayerId(id);
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
                if(render) {
                    this.updateBoard();
                }
                clock = System.currentTimeMillis();
            }
        }
	System.out.println("Someone won, WOHO GZ!, remove");

    }

    public int[] addPlayerToServer(String alias, Color playerColor, int id) {
        System.out.println("---------------Got add player to server with id-----------------");
        System.out.println(id);
        System.out.println("------------------------------------------------------------");
        int[] validPosition = board.getValidPlayerPosition();
        int x = validPosition[0];
        int y = validPosition[1];
        Player p = new Player(alias, id, x, y, playerSize, board);
        board.addPlayer(p);
        p.setPlayerColor(playerColor);
        return validPosition;
    }

    public void addPlayerToClient(int x, int y, String alias, int id, Color playerColor) {
        System.out.println("---------------Got add player to client on-----------------");
        System.out.println(x);
        System.out.println(y);
        System.out.println(id);
        System.out.println("------------------------------------------------------------");
        Player p = new Player(alias, id, x, y, playerSize, board);
        p.setPlayerColor(playerColor);
        board.addPlayer(p);

    }

    public void updateBoard() {
        this.board.update();
    }

    public boolean checkWinState() {
        return win;
    }

    public void setPlayerX(int x) {
        this.player.setX(x);
    }

    public void setPlayerY(int y) {
        this.player.setY(y);
    }
    
    public void setWin(boolean winState) {
	this.win = winState;
    }
    

    public int getPlayerX() {
        return this.player.getPlayerX();
    }

    public int getPlayerY() {
        return this.player.getPlayerY();
    }

    public short getPlayerXShort() {
        return this.player.getPlayerXShort();
    }

    public short getPlayerYShort() {
        return this.player.getPlayerYShort();
    }

    //Denna här för att dett inte skall "läcka". 
    public void updatePlayerList(ArrayList<PlayerInfo> list) {
        this.board.updatePlayerList(list);
    }

    //skickar vidare till board
    public void updatePlayer(int x, int y, int id) {
        this.board.updatePlayer(x, y, id);
    }
    
}
