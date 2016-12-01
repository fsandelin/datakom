import src.Obstruction;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import javax.swing.BorderFactory;


/**
 * Created by falapen on 2016-11-14.
 */
public class Board {
    private Rectangle boardRect;

    private ArrayList<Obstruction> fixedObjects;
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
        fixedObjects = new ArrayList<Obstruction>();
        dynamicObjects = new ArrayList<Integer>();

        drawingSurface = new JPanel();
        drawingSurface.setPreferredSize(new Dimension(xSize, ySize));

        window = new JFrame("Best-Mother-Fucking-Game-Ever (TM)");
        window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        window.setPreferredSize(new Dimension(xSize, ySize));
        window.setResizable(false);
        window.pack();
        window.add(drawingSurface);
        window.setVisible(true);

        //drawingSurface.setBorder(BorderFactory.createLineBorder(black, borderThickness));
        drawingSurface.setBackground(white);
    }


    public void addPlayer(Player p) {
        this.players.add(p);
        this.drawingSurface.add(p);
        System.out.println("Added player: " + p.getPlayerId());
    }

    public void addObstruction(Obstruction o) {
        this.fixedObjects.add(o);
        this.drawingSurface.add(o);
        System.out.println("Added obstruction");
    }

    public void update() {
        drawingSurface.setVisible(true);
        for(Player p: players) {
            p.repaint();
        }
        for(Obstruction o: fixedObjects) {
            o.repaint();
        }
    }

    public void initKeyboard(KeyboardController key) {
        this.drawingSurface.setRequestFocusEnabled(true);
        this.drawingSurface.setFocusable(true);
        this.drawingSurface.addKeyListener(key);
    }

    public int getBorderThickness() {
        return this.borderThickness;
    }

    public Dimension getBoardDimension() {
        return this.boardRect.getSize();
    }
}
