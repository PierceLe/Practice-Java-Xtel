// General Description
// Package: Checkers
// Purpose: The main game application class that extends PApplet from Processing.
// --> It sets up the game's graphical user interface, initializes the game state, and handles user input and game updates.
// Features:
// Static fields for defining game constants like cell size and board dimensions.
// An array for the game board (Cell[][] board) and collections for managing the pieces in play.
// Methods settings(), setup(), and draw() for initializing the window, setting up the game state, 
// --> and drawing the game frame by frame, respectively.
// mousePressed(MouseEvent e) to handle piece selection and moves based on user clicks.
// Utility methods like setFill(int colourCode, int blackOrWhite) for graphical operations.
// Each class is tailored to handle specific aspects of the game: 
// CheckersPiece and Cell manage the game's logical state, while App integrates these components
// -->  with Processing to manage the game's graphical interface and user interactions. Together, they form 
// --> a cohesive structure allowing for the development of a checkers game.

package Checkers;

// Utilizes HashMap for storing collections of objects, where each item has a key and value. 
// Used here for managing pieces in play with their respective colors as keys.
import java.util.HashMap;
// Utilizes HashSet for storing unique elements, ensuring no duplicates. 
// Used for managing cells and pieces where uniqueness is essential, such as tracking selected cells or pieces in play.
import java.util.HashSet;
import java.util.Map;

//import org.reflections.Reflections;
//import org.reflections.scanners.Scanners;

// Importing core PApplet class from the Processing library, which is the basis for creating drawing windows and handling events.
// Processing library imports for graphical interface and interactions
import processing.core.PApplet;
// Import for handling mouse events, enabling interactive components like clicking on pieces or cells.
import processing.event.MouseEvent;

public class App extends PApplet {

    // Constants for game configuration, defining the visual and structural aspects of the checkers board.
    public static final int CELLSIZE = 48; // Size of each cell on the board, affecting the overall scale of the game's visual representation.
    public static final int SIDEBAR = 0; // Width of an unused sidebar, potentially reserved for future features like game stats or controls.
    public static final int BOARD_WIDTH = 8; // The width and height of the checkers board, defining an 8x8 grid.
    public static final int[] BLACK_RGB = {181, 136, 99}; // RGB values for the color of black cells, contributing to the board's visual design.
    public static final int[] WHITE_RGB = {240, 217, 181}; // RGB values for the color of white cells, complementing the board's aesthetics.
    
    // Color schemes for the board and pieces, allowing for visual customization beyond the traditional black and white.
    // Array to hold custom color schemes for the board (white & black, green, blue)
    public static final float[][][] coloursRGB = new float[][][] {
        // Default scheme with white and black cells.
        {
            {WHITE_RGB[0], WHITE_RGB[1], WHITE_RGB[2]},
            {BLACK_RGB[0], BLACK_RGB[1], BLACK_RGB[2]}
        },
        // Green scheme, potentially for highlighting or alternative themes.
        {
            {105, 138, 76}, // Green for white cells
            {105, 138, 76}  // Green for black cells
        },
        // Blue scheme, offering another alternative visual theme.
        {
            {196,224,232}, // Light blue
            {170,210,221}  // Slightly darker blue
        }
    };


    // Static variables to determine the window size
    public static int WIDTH = CELLSIZE*BOARD_WIDTH+SIDEBAR;
    public static int HEIGHT = BOARD_WIDTH*CELLSIZE;


    public static final int FPS = 60 ; // Frames per second for the animation

	/* --------------------------------------- */
	// DATA STORAGE
	/* --------------------------------------- */

    // Storing the game's state, including the layout of the board, current and selected pieces, and which pieces are in play.

    private Cell[][] board; // Represents the game board as a grid of cells.
    private CheckersPiece currentSelected; // The currently selected piece, if any.
    private HashSet<Cell> selectedCells; // Tracks cells highlighted for potential moves.
    private HashMap<Character, HashSet<CheckersPiece>> piecesInPlay = new HashMap<>(); // Active pieces, differentiated by color ('w' for white, 'b' for black).
    private char currentPlayer = 'w'; // Tracks the current turn, alternating between 'w' (white) and 'b' (black).
    private HashSet<CheckersPiece> w = new HashSet<>();
    private HashSet<CheckersPiece> b = new HashSet<>();
    // Default constructor
    public App() {
        
    }



    public void initial_board() {

        piecesInPlay.put('w', w);
        piecesInPlay.put('b', b);
        for (int i = 0; i < board.length; i++) {
            for (int i2 = 0; i2 < board[i].length; i2++) {
                board[i][i2] = new Cell(i2,i);
                board[i][i2].setPiece(new CheckersPiece('E'));
                // Place white and black pieces on the board in their initial positions
                if ((i2+i) % 2 == 1) {
                    if (i < 3) {
                        // Initialize white pieces in the first three rows
                        board[i][i2].setPiece(new CheckersPiece('w'));
                        w.add(board[i][i2].getPiece());
                    } else if (i >= 5) {
                        // Initialize black pieces in the last three rows
                        board[i][i2].setPiece(new CheckersPiece('b'));
                        b.add(board[i][i2].getPiece());
                    }
                }
            }
        }
    }

    public void moving() {

    }

    // Essential Processing methods for setting up the window, initializing the game state, and drawing the board and pieces.

    // Setup method to initialize window settings
	@Override
    public void settings() {
        size(WIDTH, HEIGHT); // Set the size of the application window based on the board dimensions.
    }

    // Initial setup for the game, executed once at the beginning
	@Override
    public void setup() {
        frameRate(FPS); // Set the frame rate

        // Initialize the board and populate it with pieces
		//Set up the data structures used for storing data in the game
		this.board = new Cell[BOARD_WIDTH][BOARD_WIDTH];
        initial_board();

        // Populate the board with pieces in initial positions
        
    }

    /**
     * Receive key pressed signal from the keyboard.
    */
	@Override
    public void keyPressed(){

    }
    
    /**
     * Receive key released signal from the keyboard.
    */
	@Override
    public void keyReleased(){

    }

    @Override
    public void mousePressed(MouseEvent e) {
        
        for (Cell[] cl: board) {
            for (Cell c : cl) {
                System.out.print(c.toString() + " ");
            }

            System.out.println();
        }
        System.out.println();

        //Check if the user clicked on a piece which is theirs - make sure only whoever's current turn it is, can click on pieces
		int x = e.getX();
		int y = e.getY();
        System.out.printf("You are now clicking to (%d, %d)%n", y/App.CELLSIZE, x/App.CELLSIZE);
		if (x < 0 || x >= App.WIDTH || y < 0 || y >= App.HEIGHT) return;
		
		Cell clicked = board[y/App.CELLSIZE][x/App.CELLSIZE];
		if (clicked.getPiece() != null && Character.toLowerCase(clicked.getPiece().getColour()) == currentPlayer) {
			//valid piece to click
            // if it is already selected
			if (clicked.getPiece() == currentSelected) {
				currentSelected = null;
                selectedCells = null;
			} else {
            // select it
				currentSelected = clicked.getPiece();
                selectedCells = clicked.getPiece().getAvailableMoves(board);
			}
            
        //     for (Cell i : selectedCells) {
        //         if (i == null) {
        //             continue;
        //         }
        //         System.out.printf("(%d, %d)%n", i.getY(), i.getX());
        //         // print(i == null);
        //     }
        // //TODO: highlight available moves for current piece
        //TODO: Check if user clicked on an available move - move the selected piece there. 
		//TODO: Remove captured pieces from the board
		//TODO: Check if piece should be promoted and promote it
		//TODO: Then it's the other player's turn.
		}
        else if (selectedCells != null && selectedCells.contains(clicked)) {
            // Check if user clicked on an available move - move the selected piece there
            movePiece(currentSelected, clicked);
            // Reset selection after the move
            currentSelected = null;
            selectedCells = null;
            switchTurn();
        }


		
		
    }



    // Method to move the selected piece to the clicked cell
    private void movePiece(CheckersPiece piece, Cell destination) {
        Move moving = new Move(piece, board);
        HashMap<Integer[], String> all_move = moving.all_possible();

        Integer destination_col = destination.getX();
        Integer destination_row = destination.getY();
        Integer[] coor = null;

        // Find the coordinate in all_move that matches the destination cell
        for (Map.Entry<Integer[], String> entry : all_move.entrySet()) {
            Integer[] coordinates = entry.getKey();
            if (coordinates[0].equals(destination_row) && coordinates[1].equals(destination_col)) {
                coor = coordinates;
                break;
            }
        }
        if(all_move.get(coor).equals("normal")) {
            

            // Clear the previous position of the piece
            piece.getPosition().setPiece(new CheckersPiece('E'));
            destination.setPiece(piece);

            // Update the piece's position to the destination cell
            piece.setPosition(destination);
            piece.promote();
        }
        else if (all_move.get(coor).equals(("capture"))) {
            Integer index_removed_x = (Math.abs(piece.getPosition().getY() + destination_row) / 2);
            Integer index_removed_y = (Math.abs(piece.getPosition().getX() + destination_col) / 2);
            if (board[index_removed_x][index_removed_y].getPiece().getColour() == 'w' 
            || board[index_removed_x][index_removed_y].getPiece().getColour() == 'W') {

                w.remove(board[index_removed_x][index_removed_y].getPiece());
            }
            else {
                b.remove(board[index_removed_x][index_removed_y].getPiece());
            }
            board[index_removed_x][index_removed_y].setPiece(new CheckersPiece('E'));

            piece.getPosition().setPiece(new CheckersPiece('E'));
            destination.setPiece(piece);

            // Update the piece's position to the destination cell
            piece.setPosition(destination);
            piece.promote();


            


        }
        
    }
    private void switchTurn() {
        currentPlayer = (currentPlayer == 'w') ? 'b' : 'w';
    }

    @Override
    public void mouseDragged(MouseEvent e) {
        
    }

    /**
     * Draw all elements in the game by current frame. 
    */
    public void draw() {
        this.noStroke(); // Disable drawing outlines to prepare for drawing filled shapes.
        background(WHITE_RGB[0], WHITE_RGB[1], WHITE_RGB[2]); // Set the background color of the board.

        // Draw the board and the pieces.
        for (int i = 0; i < board.length; i++) {
            for (int i2 = 0; i2 < board[i].length; i2++) {
                if (currentSelected != null && board[i][i2].getPiece() == currentSelected) {
                    // Highlight the selected cell if it contains the current selected piece.
                    this.setFill(1, (i2+i) % 2);
                    this.rect(i2*App.CELLSIZE, i*App.CELLSIZE, App.CELLSIZE, App.CELLSIZE);
                }
                
                else if ((i2+i) % 2 == 1) {
                    this.fill(BLACK_RGB[0], BLACK_RGB[1], BLACK_RGB[2]);
                    this.rect(i2*App.CELLSIZE, i*App.CELLSIZE, App.CELLSIZE, App.CELLSIZE);
                
                    }
                if (selectedCells == null) {
                    board[i][i2].draw(this);
                    continue;
                }
                for (Cell c : selectedCells) {
                    this.fill(196,224,232);
                    this.rect(c.getX()*App.CELLSIZE, c.getY()*App.CELLSIZE, App.CELLSIZE, App.CELLSIZE);
                }
                board[i][i2].draw(this);
            }
            
        }
        
    
        // Check for end game condition where one player has no more pieces.
        if (piecesInPlay.get('w').size() == 0 || piecesInPlay.get('b').size() == 0) {
            // Display the winner.
            fill(255);
            stroke(0);
            strokeWeight(4.0f);
            rect(App.WIDTH*0.2f-5, App.HEIGHT*0.4f-25, 150, 40); // Draw a rectangle for the text background.
            fill(200,0,200);
            textSize(24.0f); // Set text size
            if (piecesInPlay.get('w').size() == 0) {
                text("Black wins!", App.WIDTH*0.2f, App.HEIGHT*0.4f);
            } else if (piecesInPlay.get('b').size() == 0) {
                text("White wins!", App.WIDTH*0.2f, App.HEIGHT*0.4f);
            }
        }
    }

    /**
     * Set fill colour for cell background
     * @param colourCode The colour to set
     * @param blackOrWhite Depending on if 0 (white) or 1 (black) then the cell may have different shades
     */
	public void setFill(int colourCode, int blackOrWhite) {
        // Set the fill color for drawing cells, allowing for different themes or highlights.
		this.fill(coloursRGB[colourCode][blackOrWhite][0], coloursRGB[colourCode][blackOrWhite][1], coloursRGB[colourCode][blackOrWhite][2]);
	}


    public static void main(String[] args) {
        PApplet.main("Checkers.App"); // Launch the Processing application.
    }
}
