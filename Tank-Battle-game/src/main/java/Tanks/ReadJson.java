package Tanks;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import processing.core.PImage;

import java.io.FileReader;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

/**
 * Handles the reading and initialization of game data from a JSON file and png file.
 * This class is responsible for loading and parsing level configurations and player attributes
 * from a specified JSON file. It provides methods to retrieve the initialized game data,
 * such as levels and player details, to be used within the game.
 * <p>
 * The {@code ReadJson} class supports:
 * <ul>
 *     <li>Reading level data, including layout, background, and optional elements like trees.</li>
 *     <li>Loading player data, specifically player colors, which can be set to random for variability.</li>
 *     <li>Loading graphical assets for the game based on the configurations provided in the png file (make this one cleaner in setup).</li>
 * </ul>
 * <p>
 * Usage includes creating an instance with the path to the JSON configuration file. This instance then
 * reads and parses the file to initialize game levels and players, which can be accessed through getter methods.
 *
 * @see Level
 * @see Player
 */
public class ReadJson {
    private final String filePath;
    private final Map<Integer, Level> levels;
    private final Map<String, Player> players;
    private int numbersLevel;

    public ReadJson(String filePath) {
        this.filePath = filePath;
        levels = initLevels();
        players = initPlayers();
    }

    /**
     * Initializes a Level object from a specified index in a JSON array.
     * Parses attributes including layout, background, and foreground color.
     * Optional attributes like trees are also handled if present.
     *
     * @param levelsArray The JSONArray containing level configurations.
     * @param i The index to extract the level data from.
     * @return The initialized Level object.
     */
    private Level getLevel(JSONArray levelsArray, int i) {
        JSONObject levelObj = (JSONObject) levelsArray.get(i);
        Level level = new Level();

        level.setLayout((String) levelObj.get("layout"));
        level.setBackground((String) levelObj.get("background"));
        level.setForegroundColour((String) levelObj.get("foreground-colour"));

        // Trees is optional, so check if it exists before setting it
        if (levelObj.containsKey("trees")) {
            level.setTrees((String) levelObj.get("trees"));
        } else {
            level.setTrees("null");
        }
        return level;
    }

    /**
     * Loads graphical assets and maps them by their filenames.
     * Initializes images from specified resources and stores them in a map for game use.
     *
     * @param app The application context used for loading images.
     * @return A map of image filenames to their corresponding PImage objects.
     */
    public Map<String, PImage> getGraphics(App app) {
        Map<String, PImage> initialGraphics = new HashMap<>();
        // setup all the graphics
        PImage airdrop = app.loadImage("src/main/resources/Tanks/airdrop.png");
        PImage basicLandScape = app.loadImage("src/main/resources/Tanks/basic.png");
        PImage desertLandscape = app.loadImage("src/main/resources/Tanks/desert.png");
        PImage forestLandscape = app.loadImage("src/main/resources/Tanks/forest.png");
        PImage fuel = app.loadImage("src/main/resources/Tanks/fuel.png");
        PImage hills = app.loadImage("src/main/resources/Tanks/hills.png");
        PImage parachute = app.loadImage("src/main/resources/Tanks/parachute.png");
        PImage snow = app.loadImage("src/main/resources/Tanks/snow.png");
        PImage tree1 = app.loadImage("src/main/resources/Tanks/tree1.png");
        PImage tree2 = app.loadImage("src/main/resources/Tanks/tree2.png");
        PImage wind_left = app.loadImage("src/main/resources/Tanks/wind-1.png");
        PImage wind = app.loadImage("src/main/resources/Tanks/wind.png");
        PImage kits = app.loadImage("src/main/resources/Tanks/kits.png");
        initialGraphics.put("basic.png", basicLandScape);
        initialGraphics.put("desert.png", desertLandscape);
        initialGraphics.put("forest.png", forestLandscape);
        initialGraphics.put("fuel", fuel);
        initialGraphics.put("hills", hills);
        initialGraphics.put("parachute", parachute);
        initialGraphics.put("snow.png", snow);
        initialGraphics.put("tree1.png", tree1);
        initialGraphics.put("tree2.png", tree2);
        initialGraphics.put("wind_left", wind_left);
        initialGraphics.put("wind", wind);
        initialGraphics.put("airdrop", airdrop);
        initialGraphics.put("kits", kits);
        return initialGraphics;
    }

    /**
     * Initializes player configurations from a JSON file.
     * Reads player color data and other attributes, handling random color generation as needed.
     *
     * @return A map of player IDs to Player objects, or null if an error occurs.
     */
    private Map<Integer, Level> initLevels() {
        try {
            Map<Integer, Level> initialLevels = new HashMap<>();
            Object obj = new JSONParser().parse(new FileReader(filePath));
            JSONObject jsonObject = (JSONObject) obj;
            JSONArray levelsArray = (JSONArray) jsonObject.get("levels");
            for (int i = 0; i < levelsArray.size(); i++) {
                Level level = getLevel(levelsArray, i);

                initialLevels.put(i + 1, level);
                this.numbersLevel += 1;
            }
            return initialLevels;

        } catch (Exception e) {
            return null;
        }

    }

    /**
     * Retrieves the initialized player map.
     *
     * @return A map of player IDs to their corresponding Player objects.
     */
    private Map<String, Player> initPlayers() {
        try {
            Random random = new Random(); // no need to
            Map<String, Player> initialPlayers = new HashMap<>();
            Object obj = new JSONParser().parse(new FileReader(filePath));
            JSONObject jsonObject = (JSONObject) obj;
            JSONObject playerObj = (JSONObject) jsonObject.get("player_colours");
            for (Object key : playerObj.keySet()) {
                String playerID = (String) key;
                // System.out.println(playerID);
                StringBuilder color = new StringBuilder((String) playerObj.get(playerID));

                Player player = new Player();

                // Handle the random color
                if (color.toString().equals("random")) {
                    String[] strColor = new String[3];
                    for (int i = 0; i < strColor.length; i++) {
                        strColor[i] = Integer.toString(random.nextInt(255));
                    }
                    color = new StringBuilder();
                    for (String i : strColor) {
                        color.append(i).append(",");
                    }
                }
                player.setColor(color.toString());
                initialPlayers.put(playerID, player);
            }
            return initialPlayers;

        } catch (Exception e) {
            return null;
        }
    }

    /**
     * Retrieves the initialized player map.
     *
     * @return A map of player IDs to their corresponding Player objects.
     */
    public Map<String, Player> getPlayers() {
        return this.players;
    }

    /**
     * Retrieves the initialized level map.
     *
     * @return A map of level numbers to their corresponding Level objects.
     */

    public Map<Integer, Level> getLevels() {
        return this.levels;
    }

    /**
     * Returns the total number of levels initialized.
     *
     * @return The number of levels.
     */

    public int getNumbersLevel() {
        return this.numbersLevel;
    }
}
 