package Tanks;

import processing.core.PImage;

/**
 * Represents a box in the game, which could be either an airdrop(when falling) or a health kit(on terrain).
 */
public class Box extends Coordinate {
    private final PImage airdrop; // Image for the airdrop box
    private final PImage kits;    // Image for the health kit box
    private boolean eaten; // Flag to check if the box has been collected

    /**
     * Constructor for the Box class.
     *
     * @param row     The initial row position of the box.
     * @param col     The initial column position of the box.
     * @param airdrop The image for the airdrop.
     * @param kits    The image for the kits.
     */
    public Box(int row, float col, PImage airdrop, PImage kits) {
        super(row, col);
        this.airdrop = airdrop;
        this.kits = kits;
        this.eaten = false;
    }

    /**
     * receive the status of kits that have been eaten or not
     * @return true if the object has been eaten, false otherwise.
     */
    public boolean getEaten() {
        return this.eaten;
    }

    /**
     * Sets the eaten status of the object.
     * @param eaten The eaten status to set (true if eaten, false otherwise).
     */
    public void setEaten(boolean eaten) {
        this.eaten = eaten;
    }


    /**
     * Determines if the box finish falling or not.
     *
     * @param heightTerrain Array representing the height of the terrain.
     * @return true if the box approach terrain, false otherwise.
     */
    public boolean isFinish(float[] heightTerrain) {
        return (640 - heightTerrain[this.row] <= this.col);
    }

    /**
     * Updates the location of the box, simulating movement.
     */
    public void updateLocation() {
        this.col += 4;
    }

    /**
     * Draws the kit image on the game canvas.
     *
     * @param app Reference to the main applet to draw on.
     */
    public void drawKit(App app) {
        app.imageMode(App.CENTER);
        app.image(kits, row, col, 24, 24);
        app.imageMode(App.CORNER);
    }

    /**
     * Draws the airdrop image and updates the location of the box.
     *
     * @param app Reference to the main applet to draw on.
     */
    public void draw(App app) {
        app.imageMode(App.CENTER);
        app.image(airdrop, row, col, 36, 36);
        app.imageMode(App.CORNER);
        updateLocation(); // Move the box during each frame draw
    }
}

