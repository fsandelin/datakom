import javax.swing.*;
import java.awt.*;
import java.awt.Rectangle.*;

/**
 * Created by falapen on 2016-12-02.
 */


public class Goal extends JComponent {

    Rectangle goal;
    int xPos;
    int yPos;
    int size;

    public Goal(int xPos, int yPos, int size) {
        this.xPos = xPos;
        this.yPos = yPos;
        this.size = size;

        this.goal = new Rectangle(xPos, yPos, size, size);
        this.setPreferredSize(new Dimension(size, size));
    }


    public void paintComponent(Graphics g) {
        this.setLocation(xPos, yPos);
        g.setColor(Color.green);
        g.fillRect(0, 0, size, size);
    }

    /*public void draw(Graphics g) {
    g.setColor(Color.green);
	g.fillRect(xPos, yPos, size, size);
    }*/

    public boolean win(Player p) {
        Rectangle playerRect = new Rectangle(p.getPlayerX(), p.getPlayerY(), p.getPlayerSize(), p.getPlayerSize());
        return this.goal.intersects(playerRect);
    }
}
