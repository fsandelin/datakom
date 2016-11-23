/**
 * Created by falapen on 2016-11-14.
 */

import javax.swing.*;
import java.awt.*;
import java.util.*;
/**
All types are currently placeholders.
 */
public class GameGraphic {
    private ArrayList<Integer> fixedObjects;
    private ArrayList<Integer> dynamicObjects;
    private ArrayList<Integer> players;

    private JFrame window;
    private JPanel backgroundSurface;
    private JPanel drawingSurface;

    private int edgePadding = 40;

    public GameGraphic(int width, int height) {
        Color black = new Color(0,0,0);
        window = new JFrame("Best-motherfucking-game-ever-fucking-created-bitch");
        window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        window.setSize(new Dimension(width + edgePadding, height + edgePadding));

        backgroundSurface = new JPanel();
        window.getContentPane().setBackground(black);
        window.setVisible(true);

        drawingSurface = new JPanel();
        drawingSurface.setSize(width - 400, height - 400);
        drawingSurface.setBackground(new Color(255,255,255));

        window.getContentPane().add(drawingSurface);
        drawingSurface.add();

        fixedObjects = new ArrayList<>();
        dynamicObjects = new ArrayList<>();
        players = new ArrayList<>();
    }
}
