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
	public int spacing = 1;
	
	// Max number of mines
	public static final int MINES = 99;
	
	// Total number of squares
	public static final int SQUARES = 480;
	
	// mouse x and y coordinates
	public int mx, my = -100;
	
	public int numberOfMines = 0;
	public int numberOfNeighbors = 0;
	public int squareNumber = 0;
	public int minesRemaining = 0;
	public int squaresRemaining = 0;
	
	// Smile face variables
	public int smileX = 564;
	public int smileY = 5;
	public int smileCenterX = smileX + 42;
	public int smileCenterY = smileY + 64;
	public boolean smile = true;
	
	// Time counter variables
	public int timeX = 1049;
	public int timeY = 5;
	public int seconds = 0;
	
	// Mine counter variables
	public int mineX = 1;
	public int mineY = 5;
	public int mineCounter = 0;
	
	public boolean victory = false;
	public boolean defeat = false;
	
	// Victory message
	public int victoryMesX = 690;
	public int victoryMesY = -50;
	String victoryMessage = "Empty!";
	
	// To randomly assign mines to squares
	Random random = new Random();
	
	// All grid arrays
	int[][] mines = new int[30][16];
	int[][] neighbors = new int[30][16];
	boolean[][] blank = new boolean[30][16];
	boolean[][] revealed = new boolean[30][16];
	boolean[][] flagged = new boolean[30][16];
	
	
	// Set up the screen
	public GUI() {
		this.setTitle("Minesweeper");
		this.setSize(1216, 759);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setVisible(true);
		this.setResizable(false);
		
		for (int i = 0; i < 30; i++) {
			for (int j = 0; j < 16; j++) {				
				// Check if we need more mines
				if (numberOfMines != MINES) {
					// Randomly assign a mine
					if (random.nextInt(5) == 0) {
						mines[i][j] = 1;
						// Keep track of total mines
						numberOfMines++;
						mineCounter++;
					} else {
						mines[i][j] = 0;
					}
				}
				
				// Initialize all to revealed and flagged false
				blank[i][j] = false;
				revealed[i][j] = false;
				flagged[i][j] = false;
			}
		}
		
		// Setting up the number of neighbors
		for (int i = 0; i < 30; i++) {
			for (int j = 0; j < 16; j++) {
				
				numberOfNeighbors = 0;
				
				for (int m = 0; m < 30; m++) {
					for (int n = 0; n < 16; n++) {
						if (!(m == i && n == j)) {
							if(isNeighbor(i,j,m,n)) {
								numberOfNeighbors++;
							}
						}
					}
				}
				
				if (numberOfNeighbors == 0) {
					blank[i][j] = true;
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
			g.fillRect(0, 0, 1210, 730);
			for (int i = 0; i < 30; i++) {
				for (int j = 0; j < 16; j++) {
					g.setColor(Color.gray);
					
					if (revealed[i][j] == true) {
						// Revealed and does not contain mine
						g.setColor(Color.white);
						// Revealed and contains mine
						if (mines[i][j] == 1) {
							g.setColor(Color.red);
						}
					}
					
					// If you are hovering a box
					if (!victory && !defeat && !revealed[i][j]) {
						if (mx >= spacing+i*40+8 && mx < i*40+8+40 && my >= spacing+j*40+80+32 && my < spacing+j*40+32+80+40-2*spacing) {
							g.setColor(Color.lightGray);
						}
					}
					g.fillRect(spacing+i*40, spacing+j*40+80, 40-2*spacing, 40-2*spacing);
					
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
							
							g.setFont(new Font("Tahoma", Font.BOLD, 20));
							g.drawString(Integer.toString(neighbors[i][j]), i*40+13, j*40+80+27);
						} else if(mines[i][j] == 1) {
							g.fillRect(i*40+15, j*40+80+10, 10, 20);
							g.fillRect(i*40+10, j*40+80+15, 20, 10);
							g.fillRect(i*40+12, j*40+80+12, 15, 15);
							g.fillRect(i*40+19, j*40+80+8, 2, 25);
							g.fillRect(i*40+7, j*40+80+19, 25, 2);
						}
					}
					
					// Flags
					if (flagged[i][j]) {
						g.setColor(Color.black);
						g.fillRect(i*40+20, j*40+80+10, 4, 20);
						g.fillRect(i*40+14, j*40+80+27, 15, 4);
						g.setColor(Color.red);
						g.fillRect(i*40+13, j*40+80+10, 10, 7);
						g.setColor(Color.black);
						g.drawRect(i*40+13, j*40+80+10, 10, 7);
						g.drawRect(i*40+13, j*40+80+11, 9, 6);
						g.drawRect(i*40+14, j*40+80+11, 8, 5);
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
			
			// Set time number color
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
			
			
			// Mine counter box
			g.setColor(Color.black);
			g.fillRect(mineX, mineY, 112, 70);
			
			// Set mine number color
			g.setColor(Color.white);
			
			g.setFont(new Font("Tahoma", Font.PLAIN, 65));
			
			if ((mineCounter < 10) && (mineCounter >= 0)) {
				g.drawString("00" + Integer.toString(mineCounter), mineX+3, mineY+60);
			} else if ((mineCounter < 100) && (mineCounter >= 10)) {
				g.drawString("0" + Integer.toString(mineCounter), mineX+3, mineY+60);
			} else if ((mineCounter < 0) && (mineCounter >= -9)) {
				g.drawString("-0" + Integer.toString(Math.abs(mineCounter)), mineX+3, mineY+60);
			} else if (mineCounter < -9) {
				g.drawString("-" + Integer.toString(Math.abs(mineCounter)), mineX+3, mineY+60);
			} else {
				g.drawString(Integer.toString(mineCounter), mineX+3, mineY+60);
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
	
	
	// Used to reveal all surrounding boxes of a blank box that is clicked
	// This is a recursive algorithm that will go through all cases of blank squares
	// that are adjacent to each other
	public void revealAllBlanks(int x, int y) {
		// Need to start with initial blank and get Left, Right, Bottom, Top
		
		// Left
		if (x-1 >= 0) {
			if (!revealed[x-1][y]) {
				revealed[x-1][y] = true;
				if (blank[x-1][y]) {
					revealAllBlanks(x-1, y);
				}
			}
		}
		
		// Right
		if (x+1 <= 29) {
			if (!revealed[x+1][y]) {
				revealed[x+1][y] = true;
				if (blank[x+1][y]) {
					revealAllBlanks(x+1, y);
				}
			}
		}
		
		// Top
		if (y-1 >= 0) {
			if (!revealed[x][y-1]) {
				revealed[x][y-1] = true;
				if (blank[x][y-1]) {
					revealAllBlanks(x, y-1);
				}
			}
		}
		
		// Bottom
		if (y+1 <= 15) {
			if (!revealed[x][y+1]) {
				revealed[x][y+1] = true;
				if (blank[x][y+1]) {
					revealAllBlanks(x, y+1);
				}
			}
		}
		
		// Top Left
		if (x-1 >= 0 && y-1 >= 0) {
			if (!revealed[x-1][y-1]) {
				revealed[x-1][y-1] = true;
				if (blank[x-1][y-1]) {
					revealAllBlanks(x-1, y-1);
				}
			}
		}
		
		// Top Right
		if (x+1 <= 29 && y-1 >= 0) {
			if (!revealed[x+1][y-1]) {
				revealed[x+1][y-1] = true;
				if (blank[x+1][y-1]) {
					revealAllBlanks(x+1, y-1);
				}
			}
		}
		
		// Bottom Left
		if (x-1 >= 0 && y+1 <= 15) {
			if (!revealed[x-1][y+1]) {
				revealed[x-1][y+1] = true;
				if (blank[x-1][y+1]) {
					revealAllBlanks(x-1, y+1);
				}
			}
		}
		
		// Bottom Right
		if (x+1 <= 29 && y+1 <= 15) {
			if (!revealed[x+1][y+1]) {
				revealed[x+1][y+1] = true;
				if (blank[x+1][y+1]) {
					revealAllBlanks(x+1, y+1);
				}
			}
		}
	}
	
	
	// When there is any sort of mouse click
	public class Click implements MouseListener {
		@Override
		public void mouseClicked(MouseEvent e) {
			
			mx = e.getX();
			my = e.getY();
			
			// Left click
	        if(e.getButton() == MouseEvent.BUTTON1) {
	        	if (!victory && !defeat) {
					if (inBoxX() != -1 && inBoxY() != -1) {
						if (!revealed[inBoxX()][inBoxY()]) {
							// If not revealed and not flagged, then reveal box
							if (!flagged[inBoxX()][inBoxY()]) {
								revealed[inBoxX()][inBoxY()] = true;
								
								// If the box is blank, then we want to reveal all other boxes around it until
								// we can't find any more blank boxes
								if (blank[inBoxX()][inBoxY()]) {
									revealAllBlanks(inBoxX(), inBoxY());
								}
							}
						}
					}
	        	}
	        }
	        
	        // Right click
	        if(e.getButton() == MouseEvent.BUTTON3) {
				if (!victory && !defeat) {
					if (inBoxX() != -1 && inBoxY() != -1) {
						// We want to flag the box, not reveal it
						if (!revealed[inBoxX()][inBoxY()]) {
							// if not flagged already, we want to flag
							if (!flagged[inBoxX()][inBoxY()]) {
								flagged[inBoxX()][inBoxY()] = true;
								mineCounter--;
							} else {
								flagged[inBoxX()][inBoxY()] = false;
								mineCounter++;
							}
						}
					}
				}
	        }
			
			// Check to see if click in smile
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
		for (int i = 0; i < 30; i++) {
			for (int j = 0; j < 16; j++) {
				if (mx >= spacing+i*40+8 && mx < i*40+8+40 && my >= spacing+j*40+80+32 && my < spacing+j*40+32+80+40-2*spacing) {
					return i;
				}
			}
		}
		
		return -1;
	}
	
	
	public int inBoxY() {
		for (int i = 0; i < 30; i++) {
			for (int j = 0; j < 16; j++) {
				if (mx >= spacing+i*40+8 && mx < i*40+8+40 && my >= spacing+j*40+80+32 && my < spacing+j*40+32+80+40-2*spacing) {
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
		
		// We want to have 99 mines every game
		for (int i = 0; i < 30; i++) {
			for (int j = 0; j < 16; j++) {				
				// Check if we need more mines
				if (numberOfMines != MINES) {
					// Randomly assign a mine
					if (random.nextInt(5) == 0) {
						mines[i][j] = 1;
						// Keep track of total mines
						numberOfMines++;
						mineCounter++;
					} else {
						mines[i][j] = 0;
					}
				}
				
				// Initialize all to revealed and flagged false
				blank[i][j] = false;
				revealed[i][j] = false;
				flagged[i][j] = false;
			}
		}
		
		// Setting up the number of neighbors
		for (int i = 0; i < 30; i++) {
			for (int j = 0; j < 16; j++) {
				
				numberOfNeighbors = 0;
				
				for (int m = 0; m < 30; m++) {
					for (int n = 0; n < 16; n++) {
						if (!(m == i && n == j)) {
							if(isNeighbor(i,j,m,n)) {
								numberOfNeighbors++;
							}
						}
					}
				}
				
				if (numberOfNeighbors == 0) {
					blank[i][j] = true;
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
		if (!defeat && !victory) {
			// Victory!
			if (totalBoxesRevealed() == (480 - numberOfMines)) {
				victory = true;
				endDate = new Date();
				
				for (int m = 0; m < 30; m++) {
					for (int n = 0; n < 16; n++) {
						flagged[m][n] = false;
						revealed[m][n] = true;
					}
				}
			} else { // Not Victory, could be defeat though :)
				for (int i = 0; i < 30; i++) {
					for (int j = 0; j < 16; j++) {
						if(revealed[i][j] && mines[i][j] == 1) {
							defeat = true;
							smile = false;
							endDate = new Date();
							
							for (int m = 0; m < 30; m++) {
								for (int n = 0; n < 16; n++) {
									flagged[m][n] = false;
									revealed[m][n] = true;
								}
							}
						}
					}
				}
			}
		}
	}
	
	
	// Get the total number of boxes revealed
	public int totalBoxesRevealed() {
		int total = 0;
		
		for (int i = 0; i < 30; i++) {
			for (int j = 0; j < 16; j++) {
				if(revealed[i][j]) {
					total++;
				}
			}
		}
		
		return total;
	}
}




/*public class GUI extends JFrame {
	private static final long serialVersionUID = 6750531310758601391L;
	
	public boolean resetter = false;
	
	Date startDate = new Date();
	Date endDate = new Date();
	
	// Spacing between each box
	public int spacing = 1;
	
	// mouse x and y coordinates
	public int mx, my = -100;
	
	public int numberOfMines = 0;
	public int numberOfNeighbors = 0;
	
	// Smile face variables
	public int smileX = 605;
	public int smileY = 5;
	public int smileCenterX = smileX + 42;
	public int smileCenterY = smileY + 64;
	public boolean smile = true;
	
	// Time counter variables
	public int timeX = 1126;
	public int timeY = 5;
	public int seconds = 0;
	
	// Mine counter variables
	public int mineX = 3;
	public int mineY = 5;
	public int mineCounter = 0;
	
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
	boolean[][] blank = new boolean[16][9];
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
				if (random.nextInt(100) < 18) {
					mines[i][j] = 1;
					// Keep track of total mines
					numberOfMines++;
					mineCounter++;
				} else {
					mines[i][j] = 0;
				}
				
				// Initialize all to revealed and flagged false
				blank[i][j] = false;
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
				
				if (numberOfNeighbors == 0) {
					blank[i][j] = true;
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
					
					if (revealed[i][j] == true) {
						// Revealed and does not contain mine
						g.setColor(Color.white);
						// Revealed and contains mine
						if (mines[i][j] == 1) {
							g.setColor(Color.red);
						}
					}
					
					// If you are hovering a box
					if (!victory && !defeat && !revealed[i][j]) {
						if (mx >= spacing+i*80+6 && mx < i*80+6+80 && my >= spacing+j*80+80+29 && my < spacing+j*80+29+80+80-2*spacing) {
							g.setColor(Color.lightGray);
						}
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
					
					// Flags
					if (flagged[i][j]) {
						g.setColor(Color.black);
						g.fillRect(i*80+40, j*80+80+20, 7, 40);
						g.fillRect(i*80+28, j*80+80+55, 30, 6);
						g.setColor(Color.red);
						g.fillRect(i*80+26, j*80+80+20, 20, 15);
						g.setColor(Color.black);
						g.drawRect(i*80+26, j*80+80+20, 20, 15);
						g.drawRect(i*80+27, j*80+80+21, 18, 13);
						g.drawRect(i*80+28, j*80+80+22, 16, 11);
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
			
			// Set time number color
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
			
			
			// Mine counter box
			g.setColor(Color.black);
			g.fillRect(mineX, mineY, 112, 70);
			
			// Set mine number color
			g.setColor(Color.white);
			
			g.setFont(new Font("Tahoma", Font.PLAIN, 65));
			
			if ((mineCounter < 10) && (mineCounter >= 0)) {
				g.drawString("00" + Integer.toString(mineCounter), mineX+3, mineY+60);
			} else if ((mineCounter < 100) && (mineCounter >= 10)) {
				g.drawString("0" + Integer.toString(mineCounter), mineX+3, mineY+60);
			} else if ((mineCounter < 0) && (mineCounter >= -9)) {
				g.drawString("-0" + Integer.toString(Math.abs(mineCounter)), mineX+3, mineY+60);
			} else if (mineCounter < -9) {
				g.drawString("-" + Integer.toString(Math.abs(mineCounter)), mineX+3, mineY+60);
			} else {
				g.drawString(Integer.toString(mineCounter), mineX+3, mineY+60);
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
	
	
	// Used to reveal all surrounding boxes of a blank box that is clicked
	public void revealAllBlanks(int x, int y) {
		// Need to start with initial blank and get Left, Right, Bottom, Top
		
		// Left
		if (x-1 >= 0) {
			if (!revealed[x-1][y]) {
				revealed[x-1][y] = true;
				if (blank[x-1][y]) {
					revealAllBlanks(x-1, y);
				}
			}
		}
		
		// Right
		if (x+1 <= 15) {
			if (!revealed[x+1][y]) {
				revealed[x+1][y] = true;
				if (blank[x+1][y]) {
					revealAllBlanks(x+1, y);
				}
			}
		}
		
		// Top
		if (y-1 >= 0) {
			if (!revealed[x][y-1]) {
				revealed[x][y-1] = true;
				if (blank[x][y-1]) {
					revealAllBlanks(x, y-1);
				}
			}
		}
		
		// Bottom
		if (y+1 <= 8) {
			if (!revealed[x][y+1]) {
				revealed[x][y+1] = true;
				if (blank[x][y+1]) {
					revealAllBlanks(x, y+1);
				}
			}
		}
		
		// Top Left
		if (x-1 >= 0 && y-1 >= 0) {
			if (!revealed[x-1][y-1]) {
				revealed[x-1][y-1] = true;
				if (blank[x-1][y-1]) {
					revealAllBlanks(x-1, y-1);
				}
			}
		}
		
		// Top Right
		if (x+1 <= 15 && y-1 >= 0) {
			if (!revealed[x+1][y-1]) {
				revealed[x+1][y-1] = true;
				if (blank[x+1][y-1]) {
					revealAllBlanks(x+1, y-1);
				}
			}
		}
		
		// Bottom Left
		if (x-1 >= 0 && y+1 <= 8) {
			if (!revealed[x-1][y+1]) {
				revealed[x-1][y+1] = true;
				if (blank[x-1][y+1]) {
					revealAllBlanks(x-1, y+1);
				}
			}
		}
		
		// Bottom Right
		if (x+1 <= 15 && y+1 <= 8) {
			if (!revealed[x+1][y+1]) {
				revealed[x+1][y+1] = true;
				if (blank[x+1][y+1]) {
					revealAllBlanks(x+1, y+1);
				}
			}
		}
	}
	
	
	// When there is any sort of mouse click
	public class Click implements MouseListener {
		@Override
		public void mouseClicked(MouseEvent e) {
			
			mx = e.getX();
			my = e.getY();
			
			// Left click
	        if(e.getButton() == MouseEvent.BUTTON1) {
	        	if (!victory && !defeat) {
					if (inBoxX() != -1 && inBoxY() != -1) {
						if (!revealed[inBoxX()][inBoxY()]) {
							// If not revealed and not flagged, then reveal box
							if (!flagged[inBoxX()][inBoxY()]) {
								revealed[inBoxX()][inBoxY()] = true;
								
								// If the box is blank, then we want to reveal all other boxes around it until
								// we can't find any more blank boxes
								if (blank[inBoxX()][inBoxY()]) {
									revealAllBlanks(inBoxX(), inBoxY());
								}
							}
						}
					}
	        	}
	        }
	        
	        // Right click
	        if(e.getButton() == MouseEvent.BUTTON3) {
				if (!victory && !defeat) {
					if (inBoxX() != -1 && inBoxY() != -1) {
						// We want to flag the box, not reveal it
						if (!revealed[inBoxX()][inBoxY()]) {
							// if not flagged already, we want to flag
							if (!flagged[inBoxX()][inBoxY()]) {
								flagged[inBoxX()][inBoxY()] = true;
								mineCounter--;
							} else {
								flagged[inBoxX()][inBoxY()] = false;
								mineCounter++;
							}
						}
					}
				}
	        }
			
			// Check to see if click in smile
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
				if (random.nextInt(100) < 18) {
					mines[i][j] = 1;
					// Keep track of total mines
					mineCounter++;
					numberOfMines++;
				} else {
					mines[i][j] = 0;
				}
				
				// Initialize all to revealed and flagged false
				blank[i][j] = false;
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
				
				if (numberOfNeighbors == 0) {
					blank[i][j] = true;
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
		if (!defeat && !victory) {
			for (int i = 0; i < 16; i++) {
				for (int j = 0; j < 9; j++) {
					if(revealed[i][j] && mines[i][j] == 1) {
						defeat = true;
						smile = false;
						endDate = new Date();
						
						for (int m = 0; m < 16; m++) {
							for (int n = 0; n < 9; n++) {
								flagged[m][n] = false;
								revealed[m][n] = true;
							}
						}
					}
				}
			}
		}
		
		if ((totalBoxesRevealed() >= (144 - numberOfMines)) && !victory && !defeat) {
			victory = true;
			endDate = new Date();
			
			for (int m = 0; m < 16; m++) {
				for (int n = 0; n < 9; n++) {
					flagged[m][n] = false;
					revealed[m][n] = true;
				}
			}
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
}*/
