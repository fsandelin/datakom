//import sun.awt.windows.WingDings;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;


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
    private static final int aliasPadding = 0;

    private Goal goal;

    private final Lock _mutex = new ReentrantLock(true);

    public Board(int xSize, int ySize) {
        boardRect = new Rectangle(new Dimension(xSize, ySize));
        players = new ArrayList<Player>();
        fixedObjects = new ArrayList<Obstruction>();
        drawingSurface = new JPanel() {
            public void paintComponent(Graphics g) {
                super.paintComponent(g);
                for (Player p : players) {
                    p.draw(g);
                }
                for (Obstruction o : fixedObjects) {
                    o.draw(g);
                }
                //System.out.printf("\n");
//                repaint();
//                try {
//                    Thread.sleep(16);
//                } catch (Exception e) {
//                    System.out.println(e);
//                }
            }
        };
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
        _mutex.lock();
        players.add(p);
        System.out.println("Det finns nu " + Integer.toString(players.size()) + " i boards listan.");
        //System.out.println(players[0]);
        _mutex.unlock();
    }

    public void addObstruction(Obstruction o) {
        this.fixedObjects.add(o);
        System.out.println("Added obstruction");
    }

    public void update() {
        this.drawingSurface.revalidate();
        this.drawingSurface.repaint();
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
        Rectangle nextPos = new Rectangle(p.getPlayerX() + v[0], p.getPlayerY() + v[1] + aliasPadding, p.getPlayerSize(), p.getPlayerSize());
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
        returnArray[0] = boardLowerXBounds;
        returnArray[1] = 500;
        return returnArray;

    }

    /**
     * Uppdaterar spelaren med som har ID id med x och y coordinaterna.
     * Låser ArrayListan eftersom den inte är threadsafe.
     *
     * @param x Nya x-koordinaten
     * @param y Nya y-koordinaten
     * @param id Player Id av Player som skall uppdateras
     *
     */
    public void updatePlayer(int x, int y, int id) {
        _mutex.lock();
        for (int i = 0; i < this.players.size(); i++) {
            int playerId = this.players.get(i).getPlayerId();
            if (id == playerId) {
                this.players.get(i).setX(x);
                this.players.get(i).setY(y);
            }
        }
        _mutex.unlock();
    }

    //Uppdaterar listan som kommer som input med vad som finns i listan hos board.
    /**
     * Takes an arraylist of playerInfo and updates all x and y values to correspond to the values in a list of Player.
     * Each PlayerInfo has an ID and each PlayerInfo's x and y is only updates if the PlayerInfo id and Player id is equal.
     * Since ArrayLists aren't threadsade, the funtion locks the ArrayList of player
     *
     * @param list The ArrayList to update.
     *
     * @todo Also lock the ArraList of PlayerInfo since it is not thread safe.
     */
    public void updatePlayerList(ArrayList<PlayerInfo> list) {
        _mutex.lock();
        for (int i = 0; i < list.size(); i++) {
            int id = list.get(i).getId();
            for (int j = 0; j < this.players.size(); j++) {
                if (players.get(j).getPlayerId() == id) {
                    int x = players.get(j).getPlayerX();
                    int y = players.get(j).getPlayerY();
                    list.get(i).setX(x);
                    list.get(i).setY(y);
                    break;
                }
            }
        }
        _mutex.unlock();
    }

}
