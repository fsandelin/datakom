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

    private int maxVVelocity = 21;
    private int minVVelocity = -maxVVelocity;
    private int maxHVelocity = 10;
    private int minHVelocity = -maxHVelocity;

    private int boardLowerXBounds;
    private int pSize = 30;
    private static final int aliasPadding = 0;

    private long clock;
    private int ii;
    
    private Goal goal;

    private final Lock _mutex = new ReentrantLock(true);

    public enum Direction {
        Q1, Q2, Q3, Q4
    }

    public Board(int xSize, int ySize) {
        boardRect = new Rectangle(new Dimension(xSize, ySize));
        players = new ArrayList<Player>();
        fixedObjects = new ArrayList<Obstruction>();
        drawingSurface = new JPanel();
        drawingSurface.setPreferredSize(new Dimension(xSize, ySize));
        drawingSurface.setIgnoreRepaint(true);
        this.addWalls(xSize, ySize);
        this.addGoal(450, 350, 50);

        window = new JFrame("Best-Mother-Fucking-Game-Ever (TM)");
        window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        window.setPreferredSize(new Dimension(xSize + 100, ySize + 100));
        window.setResizable(true);
        window.pack();
        window.add(drawingSurface);
        window.setVisible(true);
        window.setIgnoreRepaint(true);

	this.clock = System.currentTimeMillis();
	this.ii = 0;

        //drawingSurface.setBorder(BorderFactory.createLineBorder(black, borderThickness));
        drawingSurface.setBackground(Color.white);
    }

    public void render() {
        Graphics g = this.drawingSurface.getGraphics();
        this.drawBackground(g);
        for (Player p : players) {
            p.draw(g);
        }
        for (Obstruction o : fixedObjects) {
            o.draw(g);
        }
        this.drawingSurface.paintComponents(g);
	this.drawingSurface.revalidate();
	//long delta = System.currentTimeMillis() - this.clock;
	//this.clock = System.currentTimeMillis();
	//System.out.println("FPS: " + Long.toString(1000/delta));
	//System.out.println(ii);
	//this.ii = this.ii + 1;
        g.dispose();
	
    }

    public void drawBackground(Graphics g) {
        g.setColor(Color.white);
        g.fillRect(0, 0, this.boardRect.width, this.boardRect.height);
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
        this.render();
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
        long time = System.currentTimeMillis();
        Player p = players.get(0);
        Rectangle nextPos = new Rectangle(p.getPlayerX() + v[0], p.getPlayerY() + v[1] + aliasPadding, p.getPlayerSize(), p.getPlayerSize());

        int objects = this.players.size() + this.fixedObjects.size() - 1;
        int i = 0;


        Rectangle[] rects = new Rectangle[objects];
        for (int k = 1; k < players.size(); k++) {
            rects[i] = players.get(k).generateRectangle();
            i++;
        }
        for (int k = 0; k < fixedObjects.size(); k++) {
            rects[i] = fixedObjects.get(k).getRect();
            i++;
        }

        int[] returnV = collisionDetect(rects, nextPos, v[0], v[1]);
//        System.out.println("CollisionDetectionTime: " + (System.currentTimeMillis() - time));
        return returnV;
    }

    private int[] collisionDetect(Rectangle[] rects, Rectangle nextPos, int xVel, int yVel) {

        Rectangle intersection;
        if (xVel > maxHVelocity) {
            xVel = maxHVelocity;
        } else if (xVel < minHVelocity) {
            xVel = minHVelocity;
        }

        Direction d = getDirection(xVel, yVel);

        int[] tentV = new int[2];
        int[] returnV = new int[2];
        returnV[0] = xVel;
        returnV[1] = yVel;

        for (Rectangle r : rects) {
            if (nextPos.intersects(r)) {
                intersection = nextPos.intersection(r);
                switch (d) {
                    case Q1:
                        tentV = Q1CollisionDetect(xVel, yVel, intersection);
                        break;
                    case Q2:
                        tentV = Q2CollisionDetect(xVel, yVel, intersection);
                        break;
                    case Q3:
                        tentV = Q3CollisionDetect(xVel, yVel, intersection);
                        break;
                    case Q4:
                        tentV = Q4CollisionDetect(xVel, yVel, intersection);
                        break;
                    default:
                        break;
                }
                if (abs(tentV[0]) < abs(returnV[0])) {
                    returnV[0] = tentV[0];
                }
                if (abs(tentV[1]) < abs(returnV[1])) {
                    returnV[1] = tentV[1];
                }
            }
        }
//        System.out.println("Return from collisiondetection - X: " + returnV[0] + " | Y: " + returnV[1]);
        return returnV;
    }

    public Direction getDirection(int x, int y) {
        if (x >= 0) {
            if (y >= 0) {
                return Direction.Q3;
            } else {
                return Direction.Q2;
            }
        } else {
            if (y >= 0) {
                return Direction.Q4;
            } else {
                return Direction.Q1;
            }
        }
    }

    private int[] Q1CollisionDetect(int x, int y, Rectangle intersection) {
        int[] velocityVector = {x, y};
        int intX = intersection.width;
        int intY = intersection.height;
        float intersectionRatio = ((float) intY) / ((float) intX);
        float newPosRatio = ((float) y) / ((float) x);

        if (newPosRatio >= intersectionRatio) {
            velocityVector[0] = x;
            velocityVector[1] = y + intersection.height;
            return velocityVector;
        } else {
            velocityVector[0] = x + intersection.width;
            velocityVector[1] = y;
        }

        return velocityVector;
    }

    private int[] Q2CollisionDetect(int x, int y, Rectangle intersection) {
        int[] velocityVector = {x, y};
        int intX = intersection.width;
        int intY = intersection.height;
        float intersectionRatio = ((float) abs(intY)) / ((float) abs(intX));
        float newPosRatio = ((float) abs(y)) / ((float) abs(x));

        if (newPosRatio >= intersectionRatio) {
            velocityVector[0] = x;
            velocityVector[1] = y + intersection.height;
            return velocityVector;
        } else {
            velocityVector[0] = x - intersection.width;
            velocityVector[1] = y;
        }

        return velocityVector;
    }

    private int[] Q3CollisionDetect(int x, int y, Rectangle intersection) {
//        System.out.println(intersection);
        int[] velocityVector = {x, y};
        int intX = intersection.width;
        int intY = intersection.height;
        float intersectionRatio = ((float) abs(intY)) / ((float) abs(intX));
        float newPosRatio = ((float) abs(y)) / ((float) abs(x));
        if (y == 0) {
            newPosRatio = 0;
        }

        if (newPosRatio >= intersectionRatio) {
            velocityVector[0] = x;
            velocityVector[1] = y - intersection.height;
        } else {
            velocityVector[0] = x - intersection.width;
            velocityVector[1] = y;
        }
        return velocityVector;
    }

    private int[] Q4CollisionDetect(int x, int y, Rectangle intersection) {
        int[] velocityVector = {x, y};
        int intX = intersection.width;
        int intY = intersection.height;
        float intersectionRatio = ((float) abs(intY)) / ((float) abs(intX));
        float newPosRatio = ((float) abs(y)) / ((float) abs(x));

        if (newPosRatio >= intersectionRatio) {
            velocityVector[0] = x;
            velocityVector[1] = y - intersection.height;
//            return velocityVector;
        } else {
            velocityVector[0] = x + intersection.width;
            velocityVector[1] = y;
        }

        return velocityVector;
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
