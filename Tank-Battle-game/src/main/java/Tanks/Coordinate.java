package Tanks;

/**
 * Represents a coordinate in 2D of processing screen.
 * The positions with a row and a column in a two-dimensional space. Most of object in game will extend from this class
 * such as player, bullet,...
 */
public abstract class Coordinate {
    protected int row;   // The row
    protected float col; // The column.

    /**
     * Default constructor that initializes the coordinate to the origin (0,0).
     */
    public Coordinate() {
        this.row = 0;
        this.col = 0.0f;
    }

    /**
     * Constructs a new coordinate with the specified row and column.
     * 
     * @param row The row number of the coordinate.
     * @param col The column number of the coordinate.
     */
    public Coordinate(int row, float col) {
        this.row = row;
        this.col = col;
    }

    /**
     * Abstract method to draw the coordinate on the given application interface.
     * @param app The application interface where the coordinate will be drawn (app).
     */
    public abstract void draw(App app);

    /**
     * Sets the row of the coordinate.
     * 
     * @param row The new row value to set.
     */
    public void setRow(int row) {
        this.row = Math.max(0, Math.min(row, App.WIDTH - 1));
    }

    /**
     * Sets the column of the coordinate.
     * 
     * @param col The new column value to set.
     */
    public void setCol(float col) {
        this.col = col;
    }

    /**
     * Gets the current row of the coordinate.
     * 
     * @return The current row value.
     */
    public int getRow() {
        return this.row;
    }

    /**
     * Gets the current column of the coordinate.
     * 
     * @return The current column value.
     */
    public float getCol() {
        return this.col;
    }
}
