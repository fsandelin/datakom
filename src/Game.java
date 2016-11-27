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

    /**
     * Game setup
     *
     * @param xSize Width of the game-board
     * @param ySize Heigth of the game-board
     */
    public Game(int xSize, int ySize) {
        this.board = new Board(xSize, ySize);
        int playerSize = 100;
        int padding = 5;
        keyboardController = new KeyboardController();

        for (int i = 0; i < 4; i++) {
            this.playerArray[i] = new Player(i, 10 + (playerSize + padding) * i,
                    100, playerSize, new Dimension(xSize, ySize));
            this.board.addPlayer(this.playerArray[i]);
            this.board.update();
        }
        this.player = this.playerArray[0];
    }

    public String toString() {
        return "Current game is running";
    }

    /**
     * Following block is for keyboard control and ticks
     */

    private void manageKeys() {
        HashSet<Integer> currentKeys = keyboardController.getActiveKeys();
        System.out.println(currentKeys);

        if (currentKeys.contains(KeyEvent.VK_RIGHT)) {
            player.move(KeyEvent.VK_RIGHT);
        } else if (currentKeys.contains(KeyEvent.VK_LEFT)) {
            player.move(KeyEvent.VK_LEFT);
        }

        if (currentKeys.contains(KeyEvent.VK_SPACE)) {
            // Check if player isn't jumping or falling atm
            if (!player.getJumping() && !player.getFalling()) {
                player.jump();
            }
        }
        //System.out.println("Player Moved");
    }


    private void run() {
        // Game loop
//    	while (true) {
            player.checkFallingState();
            player.checkJumpingState();
            manageKeys();
//    	}
    }


    /**
     * Main method
     *
     * @param args
     */
    public static void main(String[] args) {
        Game runningGame = new Game(800, 600);
        System.out.println(runningGame);
        for (int i = 0; i < 1000; i++) {
            //System.out.println("A game tick");
            runningGame.run();
            try {
                Thread.sleep(10);
            } catch (Exception e) {
                System.out.println(e);
            }
        }
        System.out.println("Finished");
    }
}
