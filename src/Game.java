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

    public Game(int xSize, int ySize) {
        this.board = new Board(xSize, ySize);
        int playerSize = 10;
        int padding = 5;

        for (int i = 0; i < 4; i++) {
            this.playerArray[i] = new Player(i, 10 + (playerSize + padding)*i,
					     10, playerSize);
        }
        board.draw(this.playerArray);
    }

    public String toString() {
        return "Current game is running";
    }

    public static void main(String[] args) {
        Game runningGame = new Game(800, 600);
        System.out.println(runningGame);
        for(int i = 0; i < 1000; i++) {
            System.out.println("A game tick");
            try {
                Thread.sleep(1);
            }
            catch(Exception e) {
                System.out.println(e);
            }
        }
        System.out.println("Finished");
    }

    private void manageKeys() {
	HashSet<Integer> currentKeys = KeyboardController.getActiveKeys();
	
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
    }

    private void run() {
	// Game loop
	while (true) {
	    player.checkFallingState();
	    player.checkJumpingState();
	    manageKeys();
	}
    }

}
