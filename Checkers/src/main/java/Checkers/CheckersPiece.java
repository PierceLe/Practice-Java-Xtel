// General Description
// Package: Checkers
// Purpose: Represents a single piece in the checkers game, holding information about the piece's color ('w' for white, 'b' for black) and its position on the board.
// Methods:
// Constructors to set the piece's color.
// getColour() and setPosition(Cell p) for accessing and updating the piece's color and position.
// getPosition() for retrieving the current position.
// getAvailableMoves(Cell[][] board) (not implemented) intended to calculate possible moves.
// capture() and promote() methods (not fully implemented) for handling captures and promotions of pieces.
// draw(App app) for drawing the piece on the board using Processing methods.

package Checkers;

import java.util.HashSet;
import java.util.HashMap;
import java.util.Map;

public class CheckersPiece {

	// The color of the checkers piece ('w' for white, 'b' for black)
	private char colour;

	// The current position of the piece on the board
	private Cell position;

	// Constructor: Initializes a new piece with a given color
	public CheckersPiece(char c) {
		this.colour = c;
	}


	// Returns the color of the piece
	public char getColour() {
		return this.colour;
	}

	// Sets the position of the piece to a given cell
	public void setPosition(Cell p) {
		this.position = p;
	}

	// Returns the current position of the piece
	public Cell getPosition() {
		return this.position;
	}
	
	public HashSet<Cell> getAvailableMoves(Cell[][] board) {
		//TODO: Get available moves for this piece depending on the board layout, and whether this piece is a king or not
		//How to record if the move is a capture or not? Maybe make a new class 'Move' that stores this information, along with the captured piece?
		HashSet<Cell> all_moves = new HashSet<Cell>();
		Move moving = new Move(this, board);
		HashMap<Integer[], String> valid_moves = moving.all_possible();
		for (Map.Entry<Integer[], String> entry : valid_moves.entrySet()) {
			Integer[] coordinate = entry.getKey();
			all_moves.add(board[coordinate[0]][coordinate[1]]);
			
		}
		return all_moves;
	}
	
	public void capture() {
		//capture this piece
	}
	
	public void promote() {
		if (this.colour == 'w' && this.position.getY() == 7) {
			this.colour = 'W';
		}
		else if (this.colour == 'b' && this.position.getY() == 0) {
			this.colour = 'B';
		}
	}
	
	// Draws the piece on the board using the Processing library.
    // This method takes an instance of the App class, which extends PApplet from Processing, to access drawing methods.
	public void draw(App app)
	{
		// Set the stroke weight for the outline of the piece
		

		if (colour == 'w') {
			// White piece
			app.strokeWeight(5.0f);
			app.fill(255); // white fill
			app.stroke(0); // black stroke
			app.ellipse(position.getX()*App.CELLSIZE + App.CELLSIZE/2, position.getY()*App.CELLSIZE + App.CELLSIZE/2, App.CELLSIZE*0.8f, App.CELLSIZE*0.8f);
		// Disable the stroke for subsequent drawings
			app.noStroke();
		} else if (colour == 'b') {
		    // Black piece
			app.strokeWeight(5.0f);
			app.fill(0); // black fill
			app.stroke(255);// white stroke
			app.ellipse(position.getX()*App.CELLSIZE + App.CELLSIZE/2, position.getY()*App.CELLSIZE + App.CELLSIZE/2, App.CELLSIZE*0.8f, App.CELLSIZE*0.8f);
		// Disable the stroke for subsequent drawings
			app.noStroke();
		} 
		else if (colour == 'W') {
			app.noFill();
			app.stroke(0);
			app.strokeWeight((float) 9);
			app.ellipse((float)(48*position.getX() + 24), (float)(48*position.getY() + 24), (float) 39, (float) 39);
			app.noFill();
			app.stroke(255);
			app.strokeWeight(10);
			app.ellipse((float)(48*position.getX() + 24), (float)(48*position.getY() + 24), (float) 29, (float) 29);
			app.fill(255);
			app.stroke(0);
			app.strokeWeight(7);
			app.ellipse((float)(48*position.getX() + 24), (float)(48*position.getY() + 24), (float) 22, (float) 22);
			app.noStroke();
		}
		else if (colour == 'B') {
			app.noFill();
			app.stroke(255);
			app.strokeWeight((float) 9);
			app.ellipse((float)(48*position.getX() + 24), (float)(48*position.getY() + 24), (float) 39, (float) 39);
			app.noFill();
			app.stroke(0);
			app.strokeWeight(10);
			app.ellipse((float)(48*position.getX() + 24), (float)(48*position.getY() + 24), (float) 29, (float) 29);
			app.fill(0);
			app.stroke(255);
			app.strokeWeight(7);
			app.ellipse((float)(48*position.getX() + 24), (float)(48*position.getY() + 24), (float) 22, (float) 22);
			app.noStroke();

		}

		// Draw the piece as an ellipse (circle) at the piece's position, adjusting the coordinates based on the cell size
		// The method elipse takes 4 parameters
		// Syntax:  ellipse(a, b, c, d)	
		// Parameters
		// a	(float)	x-coordinate of the ellipse
		// b	(float)	y-coordinate of the ellipse
		// c	(float)	width of the ellipse by default
		// d	(float)	height of the ellipse by default
		
	}
}