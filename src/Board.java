import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import javax.swing.BorderFactory;

import static java.lang.Math.abs;


/**
 * Created by falapen on 2016-11-14.
 */
public class Board {
    private Rectangle boardRect;

    private ArrayList<Obstruction> fixedObjects;
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

    private int maxVVelocity = 21;
    private int minVVelocity = -maxVVelocity;
    private int maxHVelocity = 10;
    private int minHVelocity = -maxHVelocity;


    public Board(int xSize, int ySize) {
        boardRect = new Rectangle(new Dimension(xSize, ySize));
        players = new Player[4];
        fixedObjects = new ArrayList<Obstruction>();
        drawingSurface = new JPanel();
        drawingSurface.setPreferredSize(new Dimension(xSize, ySize));
        this.addWalls(xSize, ySize);


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
            if (o != null) {
                o.repaint();
            }
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

    public int[] getValidVelocity(int[] v) {
        Player p = players[0];
        Rectangle nextPos = new Rectangle(p.getX() + v[0], p.getY() + v[1], p.getPlayerSize(), p.getPlayerSize());
        //System.out.println(nextPos);
        Rectangle intersection;

        int tentXVelocity = v[0];
        int tentYVelocity = v[1];

        if (v[0] > maxHVelocity) {
            tentXVelocity = maxHVelocity;
        }
        if (v[0] < minHVelocity) {
            tentXVelocity = minHVelocity;
        }

        int[] returnV = new int[2];
        returnV[0] = tentXVelocity;
        returnV[1] = tentYVelocity;

        for (Obstruction o : fixedObjects) {
            if (nextPos.intersects(o.getRect())) {
                intersection = nextPos.intersection(o.getRect());
                if (intersection.getWidth() > intersection.getHeight()) {
                    if (v[1] > 0) {
                        tentYVelocity = tentYVelocity - ((int) intersection.getHeight());
                        if (tentYVelocity < returnV[1]) {
                            returnV[1] = tentYVelocity;
                        }
                    } else if(v[1] < 0){
                        tentYVelocity = tentYVelocity + ((int) intersection.getHeight());
                        if (tentYVelocity > returnV[1]) {
                            returnV[1] = tentYVelocity;
                        }
                    } else { returnV[1] = 1;}

//                    if (tentYVelocity <= returnV[1] && tentYVelocity >= -returnV[1]) {
//                        returnV[1] = tentYVelocity;
//                    }
                } else {
                    if (v[0] >= 0) {
                        tentXVelocity = tentXVelocity - ((int) intersection.getWidth());
                        if (tentXVelocity < returnV[0]) {
                            returnV[0] = tentXVelocity;
                        }
                    } else {
                        tentXVelocity = tentXVelocity + ((int) intersection.getWidth());
                        if (tentXVelocity > returnV[0]) {
                            returnV[0] = tentXVelocity;
                        }
                    }
                }
            }
        }
        // System.out.printf("%d | %d |||| %d | %d\n", returnV[0], returnV[1], v[0], v[1]);
        return returnV;
    }

    public void addWalls(int xSize, int ySize) {
        Obstruction floor = new Obstruction(0, (int) boardRect.getHeight() - 30, new Dimension((int) boardRect.getWidth(), 30));
        floor.setColor(Color.black);
        Obstruction left = new Obstruction(0, 0, new Dimension(30, (int) boardRect.getHeight()));
        left.setColor(Color.black);
        Obstruction top = new Obstruction(33, 0, new Dimension(((int) boardRect.getWidth() - 75), 30));
        top.setColor(Color.black);
        Obstruction right = new Obstruction( (int) boardRect.getWidth() - 30, 0, new Dimension(30, ((int) boardRect.getHeight())));
        right.setColor(Color.black);
        this.addObstruction(floor);
        this.addObstruction(left);
        this.addObstruction(right);
        //this.addObstruction(top);

    }

}
