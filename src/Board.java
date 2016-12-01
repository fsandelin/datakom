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
    private Player[] players;

    private JFrame window;
    private JPanel drawingSurface;
    private Graphics testGraphics;

    private int borderThickness = 10;

    private Color black = new Color(0, 0, 0);
    private Color white = new Color(255, 255, 255);
    private Color red = new Color(255, 0, 0);
    private Color green = new Color(0, 255, 0);
    private Color blue = new Color(0, 0, 255);

    private int maxVVelocity = 10;
    private int minVVelocity = -maxVVelocity;
    private int maxHVelocity = 10;
    private int minHVelocity = -maxHVelocity;


    public Board(int xSize, int ySize) {
        boardRect = new Rectangle(new Dimension(xSize, ySize));
        players = new Player[4];
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
        players[0] = p;
        this.drawingSurface.add(players[0]);
        //System.out.println(players[0]);
    }

    public void addObstruction(Obstruction o) {
        this.fixedObjects.add(o);
        this.drawingSurface.add(o);
        System.out.println("Added obstruction");
    }

    public void update() {
        drawingSurface.setVisible(true);
        players[0].repaint();
        for (Obstruction o : fixedObjects) {
            if(o != null) {o.repaint();}
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

    public int[] getValidHVelocity(int[] v) {
        Player p = players[0];
        Rectangle nextPos = new Rectangle(p.getPlayerSize()+v[0], p.getPlayerSize()+v[1], p.getX(), p.getY());
        Rectangle intersection;

        int tentXVelocity = v[0];
        int tentYVelocity = v[1];

        if(v[0] > maxHVelocity) {
            tentXVelocity = maxHVelocity;
        }
        if(v[0] < minHVelocity) {
            tentXVelocity = minHVelocity;
        }

        int[] returnV = new int[2];
        returnV[0] = tentXVelocity;
        returnV[1] = tentYVelocity;

        for (Obstruction o : fixedObjects) {
            if (nextPos.intersects(o.getRect())) {
                intersection = nextPos.intersection(o.getRect());
                if (intersection.getWidth() > intersection.getHeight()) {
                    if((tentYVelocity = tentYVelocity - ((int) intersection.getHeight()) + 1) <= returnV[1]) {
                        returnV[1] = tentYVelocity;
                    }
                } else {
                    if((tentXVelocity = tentXVelocity - ((int) intersection.getWidth()) + 1) < returnV[0]) {
                        returnV[0] = tentXVelocity;
                    }
                }
            }
        }
        return returnV;
    }
}
