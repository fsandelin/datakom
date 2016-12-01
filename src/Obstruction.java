import javax.swing.*;
import java.awt.*;

/**
 * Created by falapen on 2016-11-29.
 */
public class Obstruction extends JComponent{

    private Rectangle bounds;
    private Color fill;


    public Obstruction(int x, int y, Dimension d) {
        this.bounds = new Rectangle( x, y, (int) d.getHeight(), (int) d.getWidth());
        this.setPreferredSize(new Dimension((int) d.getWidth(), (int) d.getHeight()));
        this.setLocation(x,y);
    }

    public void setColor(Color c) {
        this.fill = c;
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        setLocation((int) bounds.getX(), (int) bounds.getY());
        g.setColor(fill);
        g.fillRect(0,0, this.getWidth(), this.getHeight());
    }
}
