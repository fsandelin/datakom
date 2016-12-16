import javax.swing.*;
import java.awt.*;

public class Level {
    private Board board;
    private Obstruction o;

    public Level(Board board) {
	// Träd 1
	o = new Obstruction(40, 450, new Dimension(50, 50));
	o.setColor(new Color(22, 142, 42));
	board.addObstruction(o);
	o = new Obstruction(55, 500, new Dimension(20, 70));
	o.setColor(new Color(79, 46, 10));
	board.addObstruction(o);

	// Träd 2
	o = new Obstruction(610, 450, new Dimension(50, 50));
	o.setColor(new Color(22, 142, 42));
	board.addObstruction(o);
	o = new Obstruction(625, 500, new Dimension(20, 70));
	o.setColor(new Color(79, 46, 10));
	board.addObstruction(o);

	// Buske
	o = new Obstruction(120, 545, new Dimension(25, 25));
	o.setColor(new Color(22, 142, 42));
	board.addObstruction(o);
	
	// Buske 2
	o = new Obstruction(690, 545, new Dimension(25, 25));
	o.setColor(new Color(22, 142, 42));
	board.addObstruction(o);

	// Sol
	o = new Obstruction(650, 20, new Dimension(120, 120));
	o.setColor(Color.yellow);
	board.addObstruction(o);

	// Moln 1
	o = new Obstruction(210, 400, new Dimension(30, 30));
	o.setColor(Color.white);
	board.addObstruction(o);

	// Moln 2
	o = new Obstruction(380, 405, new Dimension(38, 30));
	o.setColor(Color.white);
	board.addObstruction(o);

	// Moln 3
	o = new Obstruction(595, 395, new Dimension(30, 30));
	o.setColor(Color.white);
	board.addObstruction(o);

	// Moln 4
	o = new Obstruction(710, 320, new Dimension(20, 20));
	o.setColor(Color.white);
	board.addObstruction(o);

	/* Moln 5
	o = new Obstruction(750, 290, new Dimension(20, 20));
	o.setColor(Color.white);
	board.addObstruction(o);*/

	// Moln 6
	o = new Obstruction(450, 240, new Dimension(140, 40));
	o.setColor(Color.white);
	board.addObstruction(o);

	// Moln 7
	o = new Obstruction(300, 240, new Dimension(60, 30));
	o.setColor(Color.white);
	board.addObstruction(o);
	//o = new Obstruction(300, 210, new Dimension(30, 30));
	//o.setColor(Color.white);
	//board.addObstruction(o);

	// Moln 8
	o = new Obstruction(120, 130, new Dimension(60, 30));
	o.setColor(Color.white);
	board.addObstruction(o);

	// Moln 9
	o = new Obstruction(30, 80, new Dimension(60, 30));
	o.setColor(Color.white);
	board.addObstruction(o);
    }
}
