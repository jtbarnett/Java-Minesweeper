import javax.swing.*;
import java.util.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;

public class GUI extends JFrame {
	
	int spacing = 5;
	public int mx, my = -100;
	
	int[][] mines = new int[16][9];
	int[][] neighbors = new int[16][9];
	boolean[][] revealed = new boolean[16][9];
	boolean[][] flagged = new boolean[16][9];
	
	// Set up the screen
	public GUI() {
		this.setTitle("Minesweeper");
		this.setSize(1296, 839);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setVisible(true);
		this.setResizable(false);
		
		Board board = new Board();
		this.setContentPane(board);
		
		Move move = new Move();
		this.addMouseMotionListener(move);
		
		Click click = new Click();
		this.addMouseListener(click);
	}
	
	// Inside contents of the screen
	public class Board extends JPanel {
		public void paintComponent(Graphics g) {
			g.setColor(Color.DARK_GRAY);
			g.fillRect(0, 0, 1290, 810);
			for (int i = 0; i < 16; i++) {
				for (int j = 0; j < 9; j++) {
					g.setColor(Color.gray);
					if (mx >= spacing+i*80+6 && mx < i*80+6+80 && my >= spacing+j*80+80+29 && my < spacing+j*80+29+80+80-2*spacing) {
						g.setColor(Color.red);
					}
					g.fillRect(spacing+i*80, spacing+j*80+80, 80-2*spacing, 80-2*spacing);
				}
			}
		}
	}
	
	// When the mouse is moved
	public class Move implements MouseMotionListener {
		@Override
		public void mouseDragged(MouseEvent e) {
			
		}

		@Override
		public void mouseMoved(MouseEvent e) {
			mx = e.getX();
			my = e.getY();
		}
	}
	
	// When there is any sort of mouse click
	public class Click implements MouseListener {
		@Override
		public void mouseClicked(MouseEvent e) {
			
		}

		@Override
		public void mousePressed(MouseEvent e) {
			
		}

		@Override
		public void mouseReleased(MouseEvent e) {
			
		}

		@Override
		public void mouseEntered(MouseEvent e) {
			
		}

		@Override
		public void mouseExited(MouseEvent e) {
			
		}
	}
}
