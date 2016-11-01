/**
 * 
 *
 * @author Fredrik Sandelin
 */
import java.awt.*;
import java.awt.Graphics2D;
import java.awt.image.BufferStrategy;
import java.awt.Graphics;
import javax.swing.JFrame;

public class Game {
    public static void main (String [] args) {
	
	int exit = 0;
	Color grey_color = new Color(105,105,105);


	
	JFrame screen = new JFrame();
	screen.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	screen.setSize(800, 600);
	screen.setTitle("Game");

	//screen.setIgnoreRepaint(true);
	Canvas canvas = new Canvas();
	canvas.setIgnoreRepaint(true);
	canvas.setSize(800,600);
	canvas.setBackground(grey_color);
	    
	screen.add(canvas);
	screen.pack();
	screen.setVisible(true);

	while (true) {
	    canvas.repaint();
	}	
    }
}
