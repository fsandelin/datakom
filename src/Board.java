//import sun.awt.windows.WingDings;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;

import static java.lang.Math.abs;


/**
 * Created by falapen on 2016-11-14.
 */
public class Board {
    private Rectangle boardRect;

    private ArrayList<Obstruction> fixedObjects;
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

    private int maxVVelocity = 21;
    private int minVVelocity = -maxVVelocity;
    private int maxHVelocity = 10;
    private int minHVelocity = -maxHVelocity;

    private int boardLowerXBounds;
    private int pSize = 30;

    private Goal goal;

    public Board(int xSize, int ySize) {
        boardRect = new Rectangle(new Dimension(xSize, ySize));
        players = new ArrayList<Player>();
        fixedObjects = new ArrayList<Obstruction>();
        drawingSurface = new JPanel();
        drawingSurface.setPreferredSize(new Dimension(xSize, ySize));
        this.addWalls(xSize, ySize);

        this.addGoal(450, 350, 50);

        window = new JFrame("Best-Mother-Fucking-Game-Ever (TM)");
        window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        window.setPreferredSize(new Dimension(xSize + 100, ySize + 100));
        window.setResizable(true);
        window.pack();
        window.add(drawingSurface);
        window.setVisible(true);

        //drawingSurface.setBorder(BorderFactory.createLineBorder(black, borderThickness));
        drawingSurface.setBackground(white);
    }


    public void addPlayer(Player p) {
        players.add(p);
        this.drawingSurface.add(p);
	System.out.println("Det finns nu " + Integer.toString(players.size()) + " i boards listan.");
        //System.out.println(players[0]);
    }

    public void addObstruction(Obstruction o) {
        this.fixedObjects.add(o);
        this.drawingSurface.add(o);
        System.out.println("Added obstruction");
    }

    public void update() {
        drawingSurface.setVisible(true);
        goal.repaint();
	System.out.println("Size på ritnings listan: " + Integer.toString(players.size()));
        for (Player p : players) {
            if (p != null) {
		System.out.println(p.toString());
		p.setVisible(true);
                p.repaint();
            }
        }
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
        Player p = players.get(0);
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
                    } else if (v[1] < 0) {
                        tentYVelocity = tentYVelocity + ((int) intersection.getHeight());
                        if (tentYVelocity > returnV[1]) {
                            returnV[1] = tentYVelocity;
                        }
                    } else {
                        returnV[1] = 1;
                    }

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
        boardLowerXBounds = (int) boardRect.getHeight() - 30;
        Obstruction left = new Obstruction(0, 0, new Dimension(30, (int) boardRect.getHeight()));
        left.setColor(Color.black);
        Obstruction top = new Obstruction(33, 0, new Dimension(((int) boardRect.getWidth() - 75), 30));
        top.setColor(Color.black);
        Obstruction right = new Obstruction((int) boardRect.getWidth() - 30, 0, new Dimension(30, ((int) boardRect.getHeight())));
        right.setColor(Color.black);
        this.addObstruction(floor);
        this.addObstruction(left);
        this.addObstruction(right);
        //this.addObstruction(top);

    }

    public void addGoal(int xPos, int yPos, int size) {
        this.goal = new Goal(xPos, yPos, size);
        this.drawingSurface.add(goal);
        System.out.println("Added a goal");
    }

    public boolean win() {
        Player p = players.get(0);
        return goal.win(p);
    }

    public int[] getValidPlayerPosition() {
        int playerSize = this.pSize;
        int[] returnArray = new int[2];
        returnArray[0] =boardLowerXBounds;
        returnArray[1] = 500;
        return returnArray;

    }

}
