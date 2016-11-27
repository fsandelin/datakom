import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import javax.swing.BorderFactory;


/**
 * Created by falapen on 2016-11-14.
 */
public class Board {
    private Rectangle boardRect;

    private ArrayList<Integer> fixedObjects;
    private ArrayList<Integer> dynamicObjects;
    private ArrayList<Player> players;

    private JFrame window;
    private JPanel drawingSurface;
    private Graphics testGraphics;

    private int borderThickness = 10;

    private Color black = new Color(0, 0, 0);
    private Color white = new Color(255, 255, 255);
    private Color red = new Color(255, 0, 0);
    private Color green = new Color(0, 255, 0);
    private Color blue = new Color(0, 0, 255);

    public Board(int xSize, int ySize) {
        boardRect = new Rectangle(new Dimension(xSize, ySize));
        players = new ArrayList<Player>();
        fixedObjects = new ArrayList<Integer>();
        dynamicObjects = new ArrayList<Integer>();

        drawingSurface = new JPanel();
        drawingSurface.setPreferredSize(new Dimension(xSize + borderThickness, ySize + borderThickness));

        window = new JFrame("Best-Mother-Fucking-Game-Ever (TM)");
        window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        window.setPreferredSize(new Dimension(xSize + borderThickness, ySize + borderThickness));
        window.setResizable(false);
        window.pack();
        window.add(drawingSurface);
        window.setVisible(true);

        drawingSurface.setBorder(BorderFactory.createLineBorder(black, borderThickness));
        drawingSurface.setBackground(white);
    }


    public void addPlayer(Player p) {
        this.players.add(p);
        this.drawingSurface.add(p);
        System.out.println("Added player: " + p.getPlayerId());
    }

    public void update() {
        drawingSurface.setVisible(true);
        for(Player p: players) {
            p.repaint();
        }
    }
}
