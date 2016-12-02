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
    }


    public void drawComponent(Graphics g) {
        this.setLocation(xPos, yPos);
        this.setPreferredSize(new Dimension(size, size));
        g.setColor(Color.yellow);
        g.drawRect(xPos, yPos, size, size);
    }

    public boolean win(Player p) {
        Rectangle playerRect = new Rectangle(p.getPlayerX(), p.getPlayerY(), p.getPlayerSize(), p.getPlayerSize());
        return this.goal.intersects(playerRect);
    }
}
