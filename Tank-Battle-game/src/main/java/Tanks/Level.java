package Tanks;

/**
 * Represents a level in the Tanks game.
 */
public class Level {
    private String layout;
    private String background;
    private String foregroundColor;
    private String trees;


    /**
     * Constructs a new Level object with specified attribute.
     *
     * @param layout          the layout configuration of the level, the path to the map
     * @param background      the background color or image of the level
     * @param foregroundColor the color of terrain
     * @param trees           the image of tree
     */
    public Level(String layout, String background, String foregroundColor, String trees) {
        this.layout = layout;
        this.background = background;
        this.foregroundColor = foregroundColor;
        this.trees = trees;
    }

    /**
     * Default constructor for Level class.
     */
    public Level() {

    }

    /**
     * Get the layout file path of the level.
     *
     * @return The layout file path.
     */
    public String getLayout() {
        return this.layout;
    }

    /**
     * Set the layout file path of the level.
     *
     * @param layout The layout file path.
     */
    public void setLayout(String layout) {
        this.layout = layout;
    }

    /**
     * Get the background image path of the level.
     *
     * @return The background image path.
     */
    public String getBackground() {
        return this.background;
    }

    /**
     * Set the background image path of the level.
     *
     * @param background The background image path.
     */
    public void setBackground(String background) {
        this.background = background;
    }

    /**
     * Get the color of the foreground elements in the level.
     *
     * @return The foreground color.
     */
    public String getForeGroundColor() {
        return this.foregroundColor;
    }

    /**
     * Get the image path of the trees in the level.
     *
     * @return The image path of the trees.
     */
    public String getTrees() {
        return this.trees;
    }

    /**
     * Set the image path of the trees in the level.
     *
     * @param trees The image path of the trees.
     */
    public void setTrees(String trees) {
        this.trees = trees;
    }

    /**
     * Set the color of the foreground elements in the level.
     *
     * @param foregroundColor The foreground color.
     */
    public void setForegroundColour(String foregroundColor) {
        this.foregroundColor = foregroundColor;
    }

    @Override
    public boolean equals(Object p) {
		if (!this.getClass().equals(p.getClass())) {
			return false;
		}
		Level levelP = (Level) p;
        return this.foregroundColor.equals(levelP.foregroundColor)
                && this.layout.equals(levelP.layout)
                && this.background.equals(levelP.background)
                && this.trees.equals(levelP.trees);
    }
}
