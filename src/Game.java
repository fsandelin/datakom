/**
 * @author Fredrik Sandelin
 */

import java.awt.*;
import java.awt.Graphics2D;
import java.awt.image.BufferStrategy;
import java.awt.Graphics;
import javax.swing.*;

public class Game {

    private Board board;
    private Player[] playerArray = new Player[4];

    public Game(int xSize, int ySize) {
        this.board = new Board(800, 600);
        int playerSize = 10;
        int padding = 5;

        for (int i = 0; i < 4; i++) {
            this.playerArray[i] = new Player(i, 10 + (playerSize + padding)*i, 10, playerSize);
        }
        board.draw(this.playerArray);
    }

    public String toString() {
        return "Current game is running";
    }

    public static void main(String[] args) {
        Game runningGame = new Game(800, 600);
        System.out.println(runningGame);
        System.out.println("Finished");
    }

}
