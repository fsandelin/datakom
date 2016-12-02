/**
 * @author Fredrik Sandelin
 */

import java.awt.*;
import java.awt.Graphics2D;
import java.awt.image.BufferStrategy;
import java.awt.Graphics;
import javax.swing.*;
import java.util.HashSet;
import java.awt.event.KeyEvent;

public class Game {

    private Board board;
    private Player[] playerArray = new Player[4];
    private Player player = playerArray[0];
    private KeyboardController keyboardController;
    private long clock;
    private int timestep = 33;

    /**
     * Game setup
     *
     * @param xSize Width of the game-board
     * @param ySize Heigth of the game-board
     */
    public Game(int xSize, int ySize) {
        this.board = new Board(xSize, ySize);
        int playerSize = 30;
        int padding = 5;

        //Initialize keyboardcontrols
        keyboardController = new KeyboardController();
        board.initKeyboard(keyboardController);

        //Creating and adding 4 players and setting the 0-index as current player
        player = new Player(0, 150, ySize - playerSize - 10, playerSize, board);
        board.addPlayer(player);
        //

        //Adding some obstructions to the game
        for (int i = 0; i < 1; i++) {
            Obstruction o = new Obstruction(400, 500, new Dimension(150, 30));
            o.setColor(new Color(120, 120, 120));
            board.addObstruction(o);
        }
        //

        clock = System.currentTimeMillis();
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

        if (currentKeys.contains(KeyEvent.VK_RIGHT)) {
            player.move(KeyEvent.VK_RIGHT);
        } else if (currentKeys.contains(KeyEvent.VK_LEFT)) {
            player.move(KeyEvent.VK_LEFT);
        } else {
	    player.move(0);
	}

        if (currentKeys.contains(KeyEvent.VK_SPACE)) {
	    player.jump();
        }
        //System.out.println("Player Moved");
    }


    private void run() {
        // Game loop

        if ((System.currentTimeMillis() - clock) >= timestep) {
            manageKeys();
	    player.checkJumping();
	    player.updatePosition();
            clock = System.currentTimeMillis();
        }
        this.updateBoard();
    }

    public void updateBoard() {
        this.board.update();
    }


    /**
     * Main method
     *
     * @param args
     */
    public static void main(String[] args) {
        Game runningGame = new Game(800, 600);
        System.out.println(runningGame);
        while (true) {
            runningGame.run();
        }
    }
}
