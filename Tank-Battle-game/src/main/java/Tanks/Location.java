package Tanks;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;

/**
 * Manages the loading and parsing of the game level's layout from a file,
 * providing details about the terrain and the placement of trees.
 */
public class Location {
    private final String FILE_NAME; // The file path of the level layout
    private String[][] board; // Represents the game board as a grid of strings
    private ArrayList<Integer> locationTree; // List of x-coordinates for tree positions
    private float[] heightTerrain; // Array representing the vertical height of terrain across the horizontal game space
    private ArrayList<Player> playersMap;

    /**
     * Constructs a Location object, initializing the terrain and tree locations.
     *
     * @param fileName The file path to read the map layout from.
     * @param readJson Give the data in JSON file such as map txt color of player and name of player
     */
    public Location(String fileName, ReadJson readJson) {
        this.board = new String[App.BOARD_HEIGHT][App.BOARD_WIDTH + 1];
        this.heightTerrain = new float[(App.BOARD_WIDTH + 1) * App.CELLSIZE];
        this.FILE_NAME = fileName;
        this.board = initialiseBoard();
        this.heightTerrain = initialiseTerrain();
        this.locationTree = initialiseTree();
        this.playersMap = initPlayersMap(readJson);
    }


    /**
     * Reads the layout from a file and initializes the board array.
     * Populates the board with terrain features and tree locations based on file data.
     */
    private String[][] initialiseBoard() {
        try {
            File f = new File(FILE_NAME);
            Scanner sc = new Scanner(f);
            // Fill the board with empty spaces initially
            for (String[] strings : board) {
                Arrays.fill(strings, " ");
            }
            // Populate the board with symbols from the file
            for (int i = 0; i < board.length; i++) {
                if (!sc.hasNextLine()) {
                    continue;
                }

                String now = sc.nextLine();
                for (int j = 0; j < now.length(); j++) {
                    if (j < 28) {
                        board[i][j] = now.charAt(j) + "";
                    }

                }
            }
            sc.close();
            return board;

        }
        //catch errors
        catch (FileNotFoundException e) {
            return null;
        }
    }

    private ArrayList<Integer> initialiseTree() {
        if (board == null) {
            return null;
        }
        Random random = new Random();
        ArrayList<Integer> locTree = new ArrayList<>();
        for (int i = 0; i < board[0].length; i++) {
            for (String[] strings : board) {

                // Find location of tree in board
                if (strings[i].equals("T")) {
                    // Calculate and store random tree locations
                    int loc = (i * 32) - 15 + random.nextInt(30);
                    loc = Math.max(0, Math.min(loc, 864));
                    locTree.add(loc);
                }
            }
        }
        return locTree;
    }


    /**
     * Smooths the height data for the terrain using a moving average method.
     */
    private void movingAverage(float[] heightTerrain) {
        for (int i = 0; i < heightTerrain.length - App.CELLSIZE; i++) {
            float sum = 0;
            for (int index = i; index < i + App.CELLSIZE; index++) {
                sum += heightTerrain[index];
            }
            heightTerrain[i] = sum / App.CELLSIZE;
        }
    }

    /**
     * Generates the height data for the terrain based on the locations of terrain obstructions.
     *
     * @return A float array of terrain heights.
     */
    private float[] initialiseTerrain() {
        ArrayList<int[]> locationTerrain = new ArrayList<>();
        if (this.board == null) {
            return null;
        }
        for (int i = 0; i < board[0].length; i++) {
            for (int j = 0; j < board.length; j++) {
                if (board[j][i].equals("X")) {
                    // Save the location of terrain obstructions
                    locationTerrain.add(new int[]{j, i});
                }
            }
        }
        for (int i = 0; i < heightTerrain.length; i++) {
            heightTerrain[i] = (App.BOARD_HEIGHT - locationTerrain.get(i / App.CELLSIZE)[0]) * App.CELLSIZE;
        }
        movingAverage(heightTerrain); // Apply the moving average to smooth the terrain heights
        movingAverage(heightTerrain);  // Apply a second pass to ensure smoother transitions

        return heightTerrain;
    }

    /**
     * Providing an array list of player in game
     * @param readJson use to take players data in JSON file such as color and name of player
     * @return Return an arraylist of players in map @see Player with alphabet order
     */
    private ArrayList<Player> initPlayersMap(ReadJson readJson) {
        if (board == null) {
            return null;
        }
        Map<String, Player> players = readJson.getPlayers();
        ArrayList<Player> playerMap = new ArrayList<>();
        for (int i = 0; i < board[0].length; i++) {
            for (String[] strings : board) {
                if (strings[i].equals("X")
                        || strings[i].equals("T")
                        || strings[i].equals(" ")
                        || strings[i].equals("\t")) {

                    continue;
                }

                // the sign, which is different from the "X" and "T"
                if (players.containsKey(strings[i])) {
                    Player player = players.get(strings[i]);
                    player.setName(strings[i]);
                    player.setColor(players.get(strings[i]).getColor());
                    player.setRow((i * App.CELLSIZE) + 16);
                    player.setCol(640 - heightTerrain[(i * 32) + 16]);
                    playerMap.add(player);
                }
            }
        }
        playerMap.sort(Comparator.comparing(Player::getName));
        return playerMap;
    }

    /**
     * Retrieves a list of tree locations on the map.
     *
     * @return A list of integer x-coordinates where trees are located.
     */
    public ArrayList<Integer> getLocationTree() {
        return this.locationTree;
    }


    public float[] getHeightTerrain() {
        return this.heightTerrain;
    }

    public ArrayList<Player> getPlayersMap() {
        return this.playersMap;
    }

    public String[][] getBoard() {
        return this.board;
    }
}
