/**
 * @author Fredrik Sandelin
 */

import java.awt.*;
import java.awt.Graphics2D;
import java.awt.image.BufferStrategy;
import java.awt.Graphics;
import javax.swing.*;

public class Game {
    public static void main(String[] args) {

        int exit = 0;
        Color grey_color = new Color(105, 105, 105);
        Color red_color = new Color(255, 0, 0);
        Color blue_color = new Color(0, 255, 0);
        Color green_color = new Color(0, 0, 255);


        JFrame screen = new JFrame();
        screen.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        screen.setSize(800, 600);
        screen.setTitle("Game");

        //screen.setIgnoreRepaint(true);


        /*
        Canvas canvas = new Canvas();
        canvas.setSize(800, 600);
        canvas.setBackground(grey_color);

        screen.add(canvas);
        screen.pack();
        screen.setVisible(true);
        */



        JPanel panel = new JPanel();
        panel.setSize(800, 600);
        screen.add(panel);
        //screen.pack();
        screen.setVisible(true);
        panel.setBackground(red_color);
        panel.repaint();
        Player self = new Player(300, 300, 20, 20);


        int i = 0;
        /*while (true) {
            canvas.repaint();
            switch (i) {
                case 0:
                    canvas.setBackground(red_color);
                    i++;
                    break;
                case 1:
                    canvas.setBackground(blue_color);
                    i++;
                    break;
                default:
                    canvas.setBackground(green_color);
                    i = 0;
                    break;
            }
            try {
                Thread.sleep(100);
            }
            catch(Exception e) {
                System.out.println(e);
            }
        }
        */
        while (true) {
            self.playerUpdatePosition();
            try {
                Thread.sleep(100);
            } catch (Exception e) {
                System.out.println(e);
            }
            break; //Added to remove warning
        }
    }
}
