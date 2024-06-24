package Tanks;
import java.util.*;


/**
 * Represents the animation for the Ultimate ability in the game.
 * This class extends {@link Animation} to provide a colorful, pulsating effect
 * typically associated with powerful in-game abilities. It cycles through a predefined
 * set of colors to create a dynamic visual effect.
 */

public class UltimateAnimation extends Animation{
    private final ArrayList<ArrayList<Integer>> colors;

    /**
     * Constructs an UltimateAnimation with specified position and frame limit.
     * Initializes a color sequence that changes over time to create a pulsating effect.
     *
     * @param row The row position for the animation.
     * @param col The column position for the animation.
     * @param limit The maximum number of frames before the animation resets.
     */

    public UltimateAnimation(int row, float col, int limit) {
        super(row, col, limit);
        colors = new ArrayList<>();
        this.colors.add(new ArrayList<>(Arrays.asList(255, 0, 0)));
        this.colors.add(new ArrayList<>(Arrays.asList(255, 128, 0)));
        this.colors.add(new ArrayList<>(Arrays.asList(255, 255, 128)));
    }


    /**
     * Updates the animation to the next frame, cycling colors if the end of the frame limit is reached.
     * This method manages the progression of frames and adjusts the color sequence accordingly,
     * ensuring a continuous animation loop.
     */

    public void nextImage() {
        if (this.frameNow >= this.limit) {
            colors.add(colors.remove(0));
            this.frameNow = 0;
        } else {
            this.frameNow++;
        }

    }

    /**
     * Draws the animation on the given application context.
     * This method renders the animation at the current position with the current color set,
     * creating a circular pattern that decreases in size to create a pulsating effect.
     *
     * @param app The application context used for drawing the animation.
     */

    public void draw(App app) {
        app.noStroke();
        for (int radius = 30; radius > 0; radius -= 10) {
            int colorIndex = (30 - radius) / 10;
            app.fill(colors.get(colorIndex).get(0), colors.get(colorIndex).get(1), colors.get(colorIndex).get(2));
            app.ellipse(row, col, radius, radius);
        }
        nextImage();
    }
}
