import javax.swing.*;
import java.awt.*;

/**
 * Created by falapen on 2016-11-29.
 */
public class Obstruction {

    private Rectangle bounds;
    private Color fill;


    public Obstruction(int x, int y, Dimension d) {
        this.bounds = new Rectangle( x, y, (int) d.getWidth(), (int) d.getHeight());
        this.fill = Color.black;
    }

    public void draw(Graphics g) {
        g.setColor(fill);
        g.fillRect((int) bounds.getX(), (int) bounds.getY(), (int) bounds.getWidth(), (int) bounds.getHeight());
    }

    public void setColor(Color c) {
        this.fill = c;
    }

    public Rectangle getRect() {
        return bounds;
    }
}
