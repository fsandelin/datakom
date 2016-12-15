import javax.swing.*;
import java.awt.*;
import java.awt.Rectangle.*;

/**
 * Created by falapen on 2016-12-02.
 */


public class Dummy extends JComponent{

    Rectangle goal;
    int xPos;
    int yPos;
    int size;

    public Dummy(int xPos, int yPos, int size) {
        this.xPos = xPos;
        this.yPos = yPos;
        this.size = size;
        this.setPreferredSize(new Dimension(size, size));
    }


    public void paintComponent(Graphics g) {
        this.setLocation(xPos, yPos);
        g.setColor(Color.green);
        g.fillRect(0, 0, size, size);
    }
}
