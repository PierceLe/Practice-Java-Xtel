package Tanks;
/**
 * Represents an explosion animation in the game.
 */
public class Explosion extends Animation {
    private final int DIAMETER;
    private int count;

    /**
     * Constructs an Explosion object with specified location, animation limit, and diameter.
     *
     * @param row The row position where the explosion starts.
     * @param col The column position where the explosion starts.
     * @param limit The number of frames the explosion should last before stopping.
     * @param diameter The maximum diameter the explosion can reach during the animation.
     */
    public Explosion(int row, float col, int limit, int diameter) {
        super(row, col, limit);
        this.count = 0;
        this.DIAMETER = diameter;
    }

    /**
     * Retrieves the current frame count of the explosion animation.
     *
     * @return The current count of how many times the explosion has been drawn.
     */
    public int getCount() {
        return this.count;
    }

    /**
     * Advances the explosion animation to the next frame, incrementing the frame count.
     */
    public void nextImage() {
        this.count += 1;
    }

    /**
     * Draws the explosion at its current stage.
     * The explosion is represented as three concentric ellipses with different sizes and colors.
     * The sizes of the ellipses increase with each frame to create an expanding effect.
     *
     * @param app The App object used for drawing the explosion on the screen.
     */
    public void draw(App app) {
        app.fill(255, 0, 0); // Set color to red.
        app.ellipse((float) this.row, this.col, (float) DIAMETER / 6 * count, (float) DIAMETER / 6 * count); // Draw the outermost ellipse.
        app.fill(255, (float) 255 / 2, 0); // Set color to orange.
        app.ellipse((float) this.row, this.col, (float) DIAMETER / 12 * count, (float) DIAMETER / 12 * count); // Draw the middle ellipse.
        app.fill(255, 255, (float) 255 / 2); // Set color to yellow.
        app.ellipse((float) this.row, this.col, (float) DIAMETER / 24 * count, (float) DIAMETER / 24 * count); // Draw the innermost ellipse.
        nextImage(); // Move to the next frame in the animation.
    }
}
