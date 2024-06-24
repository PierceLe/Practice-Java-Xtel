package Tanks;

/**
 * Abstract class representing an animation based on coordinates such explosion animation on top left corner of the game.
 * This one will have limit, which is the time of having that animation in game. This class extend from Coordinate
 * @see Coordinate
 */
public abstract class Animation extends Coordinate {
    protected int frameNow; // Current frame
    protected int limit;    // Maximum number of frames for the animation, after that the animation will stop

    /**
     * Constructs an Animation object positioned at specified row and column with a frame limit.
     *
     * @param row   The row position of the animation.
     * @param col   The column position of the animation.
     * @param limit The maximum number of frames.
     */
    public Animation(int row, float col, int limit) {
        super(row, col); // call superclass
        this.frameNow = 0; // first frame = 0.
        this.limit = limit; // Set the limit.
    }

    /**
     * Returns the the time limit of animation.
     *
     * @return The time limit of the animation.
     */
    public int getLimit() {
        return this.limit;
    }

    /**
     * Abstract method to define what nextframe of that object will looks like
     */
    public abstract void nextImage();
}
