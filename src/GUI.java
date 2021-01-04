import javax.swing.*;
import java.util.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;

public class GUI extends JFrame {
	private static final long serialVersionUID = 6750531310758601391L;
	
	public boolean resetter = false;
	
	Date startDate = new Date();
	Date endDate = new Date();
	
	// Spacing between each box
	public int spacing = 5;
	
	// mouse x and y coordinates
	public int mx, my = -100;
	
	public int numberOfMines = 0;
	public int mineCounter = 0;
	public int numberOfNeighbors = 0;
	
	// Smile face variables
	public int smileX = 605;
	public int smileY = 5;
	public int smileCenterX = smileX + 42;
	public int smileCenterY = smileY + 64;
	public boolean smile = true;
	
	// Time counter variables
	public int timeX = 1125;
	public int timeY = 5;
	public int seconds = 0;
	
	public boolean victory = false;
	public boolean defeat = false;
	
	// Victory message
	public int victoryMesX = 750;
	public int victoryMesY = -50;
	String victoryMessage = "Empty!";
	
	Random random = new Random();
	
	// All grid arrays
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
		
		for (int i = 0; i < 16; i++) {
			for (int j = 0; j < 9; j++) {
				// We want 1/5 squares to be a mine
				if (random.nextInt(100) < 16) {
					mines[i][j] = 1;
					// Keep track of total mines
					numberOfMines++;
					mineCounter++;
				} else {
					mines[i][j] = 0;
				}
				
				// Initialize all to revealed and flagged false
				revealed[i][j] = false;
				flagged[i][j] = false;
			}
		}
		
		// Setting up the number of neighbors
		for (int i = 0; i < 16; i++) {
			for (int j = 0; j < 9; j++) {
				numberOfNeighbors = 0;
				for (int m = 0; m < 16; m++) {
					for (int n = 0; n < 9; n++) {
						if (!(m == i && n == j)) {
							if(isNeighbor(i,j,m,n)) {
								numberOfNeighbors++;
							}
						}
					}
				}
				neighbors[i][j] = numberOfNeighbors;
			}
		}
		
		Board board = new Board();
		this.setContentPane(board);
		
		Move move = new Move();
		this.addMouseMotionListener(move);
		
		Click click = new Click();
		this.addMouseListener(click);
	}
	
	
	// Inside contents of the screen
	public class Board extends JPanel {
		private static final long serialVersionUID = 3021297228657490474L;

		public void paintComponent(Graphics g) {
			g.setColor(Color.DARK_GRAY);
			g.fillRect(0, 0, 1290, 810);
			for (int i = 0; i < 16; i++) {
				for (int j = 0; j < 9; j++) {
					g.setColor(Color.gray);
					
					/*if (mines[i][j] == 1) {
						g.setColor(Color.yellow);
					}*/
					
					if (revealed[i][j] == true) {
						// Revealed and does not contain mine
						g.setColor(Color.white);
						// Revealed and contains mine
						if (mines[i][j] == 1) {
							g.setColor(Color.red);
						}
					}
					
					// If you are hovering a box
					if (mx >= spacing+i*80+6 && mx < i*80+6+80 && my >= spacing+j*80+80+29 && my < spacing+j*80+29+80+80-2*spacing) {
						g.setColor(Color.lightGray);
					}
					g.fillRect(spacing+i*80, spacing+j*80+80, 80-2*spacing, 80-2*spacing);
					
					if (revealed[i][j] == true) {
						g.setColor(Color.black);
						// If not a mine you want to display number of neighbors
						if (mines[i][j] == 0 && neighbors[i][j] != 0) {
							if(neighbors[i][j] == 1) {
								g.setColor(Color.blue);
							} else if(neighbors[i][j] == 2) {
								g.setColor(Color.green);
							} else if(neighbors[i][j] == 3) {
								g.setColor(Color.red);
							} else if(neighbors[i][j] == 4) {
								g.setColor(new Color(0,0,128));
							} else if(neighbors[i][j] == 5) {
								g.setColor(new Color(178,34,34));
							} else if(neighbors[i][j] == 6) {
								g.setColor(new Color(72,209,204));
							} else if(neighbors[i][j] == 8) {
								g.setColor(Color.darkGray);
							}
							
							g.setFont(new Font("Tahoma", Font.BOLD, 40));
							g.drawString(Integer.toString(neighbors[i][j]), i*80+27, j*80+80+55);
						} else if(mines[i][j] == 1) {
							g.fillRect(i*80+10+20, j*80+80+20, 20, 40);
							g.fillRect(i*80+20, j*80+80+10+20, 40, 20);
							g.fillRect(i*80+5+20, j*80+80+5+20, 30, 30);
							g.fillRect(i*80+38, j*80+80+15, 4, 50);
							g.fillRect(i*80+15, j*80+80+38, 50, 4);
						}
					}
				}
			}
			
			
			// Generating the Smile Face
			g.setColor(Color.yellow);
			g.fillOval(smileX, smileY, 70, 70);
			
			// Eyes
			g.setColor(Color.black);
			g.fillOval(smileX+15, smileY+20, 10, 10);
			g.fillOval(smileX+45, smileY+20, 10, 10);
			
			// Smile
			if (smile == true) {
				g.fillRect(smileX+21, smileY+50, 30, 5);
				g.fillRect(smileX+17, smileY+45, 7, 5);
				g.fillRect(smileX+47, smileY+45, 7, 5);
			} else {
				g.fillRect(smileX+21, smileY+45, 30, 5);
				g.fillRect(smileX+17, smileY+50, 7, 5);
				g.fillRect(smileX+47, smileY+50, 7, 5);
			}
			
			
			// Time counter box
			g.setColor(Color.black);
			g.fillRect(timeX, timeY, 150, 70);
			
			// Get time
			if (!victory && !defeat) {
				seconds = (int) ((new Date().getTime() - startDate.getTime()) / 1000);
			}
			
			// If time is over 9999 seconds, then stop counting
			if (seconds > 9999) {
				seconds = 9999;
			}
			
			// Set time color
			g.setColor(Color.white);
			
			if (victory) {
				g.setColor(Color.green);
			} else if(defeat) {
				g.setColor(Color.red);
			}
			
			g.setFont(new Font("Tahoma", Font.PLAIN, 65));
			
			if (seconds < 10) {
				g.drawString("000" + Integer.toString(seconds), timeX+3, timeY+60);
			} else if (seconds < 100) {
				g.drawString("00" + Integer.toString(seconds), timeX+3, timeY+60);
			} else if (seconds < 1000) {
				g.drawString("0" + Integer.toString(seconds), timeX+3, timeY+60);
			} else {
				g.drawString(Integer.toString(seconds), timeX+3, timeY+60);
			}
			
			
			// Victory message
			if (victory) {
				g.setColor(Color.green);
				victoryMessage = "You Win!";
			} else if (defeat) {
				g.setColor(Color.red);
				victoryMessage = "You Lose!";
			}
			
			if (victory || defeat) {
				victoryMesY = -50 + (int) (new Date().getTime() - endDate.getTime()) / 10;
				if (victoryMesY > 70) {
					victoryMesY = 70;
				}
				g.setFont(new Font("Tahoma", Font.PLAIN, 70));
				g.drawString(victoryMessage, victoryMesX, victoryMesY);
			}
		}
	}
	
	
	// When the mouse is moved
	public class Move implements MouseMotionListener {
		@Override
		public void mouseDragged(MouseEvent e) {
			mx = e.getX();
			my = e.getY();
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
			mx = e.getX();
			my = e.getY();
			
			if (inBoxX() != -1 && inBoxY() != -1) {
				revealed[inBoxX()][inBoxY()] = true;
				System.out.println("The mouse is in the [" + inBoxX() + ", " + inBoxY() + "], Number of mine neighbors: " + neighbors[inBoxX()][inBoxY()]);
			}
			
			if (clickInSmile()) {
				resetAll();
			}
		}

		@Override
		public void mousePressed(MouseEvent e) {
			mx = e.getX();
			my = e.getY();
		}

		@Override
		public void mouseReleased(MouseEvent e) {
			mx = e.getX();
			my = e.getY();
		}

		@Override
		public void mouseEntered(MouseEvent e) {
			mx = e.getX();
			my = e.getY();
		}

		@Override
		public void mouseExited(MouseEvent e) {
			mx = e.getX();
			my = e.getY();
		}
	}
	
	
	public int inBoxX() {
		for (int i = 0; i < 16; i++) {
			for (int j = 0; j < 9; j++) {
				if (mx >= spacing+i*80+6 && mx < i*80+6+80 && my >= spacing+j*80+80+29 && my < spacing+j*80+29+80+80-2*spacing) {
					return i;
				}
			}
		}
		
		return -1;
	}
	
	
	public int inBoxY() {
		for (int i = 0; i < 16; i++) {
			for (int j = 0; j < 9; j++) {
				if (mx >= spacing+i*80+6 && mx < i*80+6+80 && my >= spacing+j*80+80+29 && my < spacing+j*80+29+80+80-2*spacing) {
					return j;
				}
			}
		}
		
		return -1;
	}
	
	
	public boolean isNeighbor(int mX, int mY, int cX, int cY) {
		if (mX - cX < 2 && mX - cX > -2 && mY - cY < 2 && mY - cY > -2 && mines[cX][cY] == 1) {
			return true;
		}
		
		return false;
	}
	
	
	// Method for when you click on smile
	public void resetAll() {
		resetter = true;
		startDate = new Date();
		victoryMesY = -50;
		victoryMessage = "Empty";
		smile = true;
		victory = false;
		defeat = false;
		mineCounter = 0;
		numberOfMines = 0;
		numberOfNeighbors = 0;
		
		for (int i = 0; i < 16; i++) {
			for (int j = 0; j < 9; j++) {
				// We want 1/5 squares to be a mine
				if (random.nextInt(100) < 16) {
					mines[i][j] = 1;
					// Keep track of total mines
					numberOfMines++;
				} else {
					mines[i][j] = 0;
				}
				
				// Initialize all to revealed and flagged false
				revealed[i][j] = false;
				flagged[i][j] = false;
			}
		}
		
		// Setting up the number of neighbors
		for (int i = 0; i < 16; i++) {
			for (int j = 0; j < 9; j++) {
				numberOfNeighbors = 0;
				for (int m = 0; m < 16; m++) {
					for (int n = 0; n < 9; n++) {
						if (!(m == i && n == j)) {
							if(isNeighbor(i,j,m,n)) {
								numberOfNeighbors++;
							}
						}
					}
				}
				neighbors[i][j] = numberOfNeighbors;
			}
		}
		
		resetter = false;
	}
	
	
	// Check to see of the user clicked the Smile Face
	public boolean clickInSmile() {
		int difference = (int) Math.sqrt((Math.abs(mx - smileCenterX) * Math.abs(mx - smileCenterX)) + (Math.abs(my - smileCenterY) * Math.abs(my - smileCenterY)));
		
		if (difference <= 35) {
			return true;
		}
		
		return false;
	}
	
	
	// Check to see if the player has won the game
	public void checkVictory() {
		if (!defeat) {
			for (int i = 0; i < 16; i++) {
				for (int j = 0; j < 9; j++) {
					if(revealed[i][j] && mines[i][j] == 1) {
						defeat = true;
						smile = false;
						endDate = new Date();
					}
				}
			}
		}
		
		if ((totalBoxesRevealed() >= (144 - numberOfMines)) && !victory) {
			victory = true;
			endDate = new Date();
		}
	}
	
	
	// Get the total number of boxes revealed
	public int totalBoxesRevealed() {
		int total = 0;
		
		for (int i = 0; i < 16; i++) {
			for (int j = 0; j < 9; j++) {
				if(revealed[i][j]) {
					total++;
				}
			}
		}
		
		return total;
	}
}
