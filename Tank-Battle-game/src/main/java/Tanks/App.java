package Tanks;

// PApplet is the base class for Processing sketches.

import processing.core.PApplet;
import processing.core.PImage;
import processing.event.KeyEvent;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.Map;

/**
 * The App class extends PApplet will be the main class for the Tanks game.
 * It will covers environment settings, key press, and the main game loop.
 */
public class App extends PApplet {
    // Constants defining the game's graphical and operational parameters.
    public static final int WIDTH = 864;            // Width of the game window.
    public static final int HEIGHT = 640;           // Height of the game window.
    public static final int CELLSIZE = 32;          // Size of each cell in the game grid.
    public static final int BOARD_HEIGHT = 20;      // Height of the game board in terms of cells.
    public static final int FPS = 30;               // Frames per second, game update rate.
    public static final int BOARD_WIDTH = WIDTH / CELLSIZE;  // Width of the game board derived from window width and cell size.
    public static final int INITIAL_PARACHUTE = 1; // Initial number of parachutes for each Player have in the game.
    private final ArrayList<Explosion> EXPLODING;   // List to represent all current explosions.
    private final ArrayList<Bullet> BULLETS;        // List to represent all bullets in the game including larger projectile(Extension).
    private final ArrayList<Player> COPY_PLAYER_MAP; // used for copying state for displaying board, changing level without losing score.)
    private final Draw DRAWING;                     // Object responsible for all drawing (e.g. draw terrain, draw players,...).
    private final ArrayList<Box> BOX;               // List to represent all box(airdrop) (Extension) for game
    private ReadJson readJson;                // Object for reading JSON configuration files, and also read the png image (PNG file) in resources.
    private Location location;                       // Object representing a location in the game (e.g. location of players, terrain, tree, ...).
    private boolean isUltimate;                     // Flag indicating if an "ultimate" game mode is active (Larger projectile) (Extension)
    private String configPath = "config.json";  // Path to the config file, this one will setup.
    private float wind;                             // Current wind effect in the game environment. This one will random each time
    private Map<String, PImage> graphics;           // Map linkingPImage objects (load all png image to PImage object, this one is in readjson object).
    private int levelNow;                           // the level we are playing now starting from 1
    private boolean changeLevel;                    // Flag indicating if the game is transitioning to a new level. flag for key when we press immediately
    private int delayLevel;                         // Delay counter for level transitions. 1 seconds
    private int delayReset;                         // wait all score of each players have when end game, after that we can press R to reset game.
    private boolean isFinishGame;                   // Flag indicating if the game has finished.
    private boolean isEndGame;                      // Flag indicating if the game has ended (which is different from isFinishGame because when game is end, we need to display final score then game is finish).
    private int delayBoard;                         // Delay counter for board updates.(this one make the board display 0.7s for each player for final board)
    private boolean[] haveInBoard;                  // Array tracking the presence of elements on the game board. (final scoreboard that display their score or not)
    private int turns;                              // Counter for the number of turns taken in the game. (take to randomly have airdrop)

    /**
     * Constructor initializes the game with default configurations.
     */
    public App() {
        this.EXPLODING = new ArrayList<>();
        this.BULLETS = new ArrayList<>();
        this.BOX = new ArrayList<>();
        this.COPY_PLAYER_MAP = new ArrayList<>();
        this.levelNow = 1;
        this.changeLevel = false;
        this.delayLevel = 0;
        this.delayReset = 0;
        this.isFinishGame = false;
        this.isEndGame = false;
        this.delayBoard = 0;
        this.isUltimate = false;
        this.turns = 1;
        this.DRAWING = new Draw(this);
    }

    /**
     * Main method to play the tank game.
     *
     * @param args Commandline arguments
     */
    public static void main(String[] args) {
        PApplet.main("Tanks.App");
    }

    /**
     * Initial window size.
     */
    @Override
    public void settings() {
        size(WIDTH, HEIGHT);
    }
    
    /**
     * Sets the configuration path for the application.
     *
     * @param configPath The path to the configuration file (JSON file).
     */
    public void setConfigPath(String configPath) {
        this.configPath = configPath;
    }

    /**
     * Sets the wind speed for the game.
     *
     * @param wind The wind speed want to set.
     */
    public void setWind(int wind) {
        this.wind = wind;
    }

    /**
     * Retrieves the location object.
     * @return The current location as a Location object.
     */
    public Location getLocation() {
        return location;
    }


    /**
     * Sets up the initial game environment including loading resources and initializing game elements such as
     * wind, location of tree, terrain, players. Loading map, get every image file, init copy players for the first time
     * It sets the frame rate, loads graphical assets,
     * initializes the game map, sorts players, and prepares player states for the game board.
     */
    @Override
    public void setup() {
        this.wind = random(-35, 36);              // Initialize wind effect with a random value within specified range.
        this.readJson = new ReadJson(configPath); // load every information in json file, player, color of terrain, .....
        frameRate(FPS); // Set the frame rate
        graphics = readJson.getGraphics(this);
        String fileNameMap = readJson.getLevels().get(levelNow).getLayout();
        location = new Location(fileNameMap, readJson);
        location.getPlayersMap().sort(Comparator.comparing(Player::getName));
        COPY_PLAYER_MAP.addAll(location.getPlayersMap());
        haveInBoard = new boolean[COPY_PLAYER_MAP.size()];
    }

    /**
     * Resets the game to its initial state(Type key R when game is finish), preparing it for a new game or level.
     */
    private void resetGame() {
        levelNow = 1;
        isEndGame = false;
        isFinishGame = false;
        delayBoard = 0;
        haveInBoard = new boolean[COPY_PLAYER_MAP.size()];
        this.wind = random(-35, 36);
        BULLETS.clear();
        EXPLODING.clear();
        BOX.clear();
        location.getPlayersMap().clear();
        String fileNameMap = readJson.getLevels().get(levelNow).getLayout();
        location = new Location(fileNameMap, readJson);
        ArrayList<Player> playersMap = location.getPlayersMap();
        Player.revivalPlayers(playersMap);
        turns = 1;
        delayReset = 0;
    }

    /**
     * Changes the current level of the game to the next level. we will have delay 1 second or in 1 second if we press space, it will change immediately
     */
    private void changeLevel() {
        this.wind = random(-35, 36);
        BULLETS.clear();
        EXPLODING.clear();
        BOX.clear();
        turns = 1;
        location.getPlayersMap().clear();
        levelNow++;
        String fileNameMap = readJson.getLevels().get(levelNow).getLayout();
        location = new Location(fileNameMap, readJson);
        ArrayList<Player> playersMap = location.getPlayersMap();
        Player.nextLevel(playersMap);
    }

    /**
     * Handles key press events to control various aspects of the game, including player movement,
     * game resets, and power adjustments, turn on ultimate or shopping by using score,...
     * @param event representing the event of a key being pressed.
     */
    @Override
    public void keyPressed(KeyEvent event) {
        int keyCode = event.getKeyCode();
        // Key 'R' (KeyCode 82) to reset the game if it has ended and finished.
        if (keyCode == 82) {
            if (isEndGame && isFinishGame)
                resetGame();
        }
        // Retrieve the current list of players from the location object.
        ArrayList<Player> playersMap = location.getPlayersMap();
        Player activePlayer;
        // Ensure there is at least one player to interact with.
        if (!playersMap.isEmpty()) {
            activePlayer = playersMap.get(0); // Assume the first player is the active one.
            // Respond to key presses.
            switch (keyCode) {
                case 83: // 'S' key
                    // Decrease the player's power slightly.
                    activePlayer.setPower((activePlayer.getPower() + (double) -36 / 30));
                    break;
                case 87: // 'W' key
                    // Increase the player's power slightly.
                    activePlayer.setPower((activePlayer.getPower() + (double) 36 / 30));
                    break;
                case 32: // Space bar
                    // Change to the next player in the list.
                    changePlayer(activePlayer, playersMap);
                    break;
                case 37: // Left arrow
                    // Move the player left unless they are falling.
                    if (!activePlayer.getFall()) {
                        movePlayer(activePlayer, -2, BOX);
                    }
                    break;
                case 39: // Right arrow
                    // Move the player right unless they are falling.
                    if (!activePlayer.getFall()) {
                        movePlayer(activePlayer, 2, BOX);
                    }
                    break;
                case UP:
                    // Rotate the player's angle slightly upwards.
                    activePlayer.setRotationAngle(0.1f);
                    break;
                case DOWN:
                    // Rotate the player's angle slightly downwards.
                    activePlayer.setRotationAngle(-0.1f);
                    break;
                case 82: // 'R' key again for checking inside the switch
                    // Allow resetting the game or buying health depending on game state.
                    activePlayer.buyingHealth();
                    break;
                case 70: // 'F' key
                    // Player buys fuel.
                    activePlayer.buyingFuel();
                    break;
                case 80: // 'P' key
                    // Player buys a parachute.
                    activePlayer.buyingParachute();
                    break;
                case 88: // 'X' key
                    // Activate 'ultimate' mode if conditions are met.
                    if (activePlayer.getScore() >= 20 && !isUltimate) {
                        activePlayer.updateScore(-20);
                        activePlayer.setUlti(true);
                        isUltimate = true;
                    }
                    break;
            }
        }
    }

    /**
     * Moves the active player and gain more HP if they gain healthKits
     *
     * @param activePlayer The player being moved.
     * @param delta        The amount of movement
     * @param box          Represent the location of health kits
     */
    private void movePlayer(Player activePlayer, int delta, ArrayList<Box> box) {
        if (activePlayer.getFuel() > 0) {
            int newRow = activePlayer.getRow() + delta;
            newRow = Math.max(0, Math.min(newRow, WIDTH - 1));
            activePlayer.setRow(newRow);
            activePlayer.setCol((HEIGHT - location.getHeightTerrain()[newRow]));
            activePlayer.setFuel(2);

            for (Box i : box) {
                if ((Math.abs(i.getRow() - activePlayer.getRow()) <= 5 && Math.abs((int) i.getCol() - (int) activePlayer.getCol()) <= 5)) {
                    if (activePlayer.getHealth() <= 90) {
                        activePlayer.setHealth(-10);
                    }
                    i.setEaten(true);
                }
            }
        }
    }

    /**
     * Change player, we will use the algorithm similar to the properties of Queue, we remove top and add to the tail
     * When we change player, we start count time appear of arrow pointer and fire projectile of old players
     * @param activePlayer The currently active player.
     * @param playersMap   The list of all players in the game.
     */
    private void changePlayer(Player activePlayer, ArrayList<Player> playersMap) {
        this.changeLevel = true;
        activePlayer.setTimePointer(0);
        playersMap.add(activePlayer);
        playersMap.remove(0);
        if (!isUltimate) {
            Bullet bullet = new Bullet(activePlayer, 60);
            BULLETS.add(bullet);
        } else {
            Ultimate ultimate = new Ultimate(activePlayer, 120);
            BULLETS.add(ultimate);
            activePlayer.setUlti(false);
            isUltimate = false;
        }
        this.turns += 1;
        wind += random(-5, 6);
    }

    /**
     * Renders the game's graphical elements for each frame. This method is responsible for drawing terrain,
     * trees, players, bullets, explosions, and HUD elements based on the game's current state. It handles
     * game progression, level changes, and end-game scenarios.
     */
    @Override
    public void draw() {
        ArrayList<Player> playersMap = location.getPlayersMap();

        if (playersMap.size() <= 1) {
            if (this.levelNow >= readJson.getNumbersLevel()) {
                isEndGame = true;
            } else if (delayLevel >= 30 || changeLevel) {
                delayLevel = 0;
                changeLevel();
                changeLevel = false;
            } else {
                delayLevel++;
            }
        }
        changeLevel = false;
        image(graphics.get(readJson.getLevels().get(levelNow).getBackground()), 0, 0);
        String[] color = readJson.getLevels().get(levelNow).getForeGroundColor().split(",");
        DRAWING.drawTerrain(location, color);
        String nameFileTree = readJson.getLevels().get(levelNow).getTrees();
        if (!nameFileTree.equals("null")) {
            PImage tree = graphics.get(nameFileTree);
            DRAWING.drawTree(location, tree);
        }
        if (!playersMap.isEmpty()) {
            playersMap.get(0).drawPointer(this);
        }
        PImage parachute = graphics.get("parachute");
        DRAWING.drawPlayers(playersMap, location, parachute, EXPLODING);
        DRAWING.drawBullets(BULLETS, location, playersMap, EXPLODING, wind);
        DRAWING.drawExplosion(EXPLODING);
        if (turns % 3 == 0 && BOX.size() <= playersMap.size()) {
            int column = (int) random(3, WIDTH - 5);
            Box airdrop = new Box(column, 0, graphics.get("airdrop"), graphics.get("kits"));
            BOX.add(airdrop);
            turns += 1;
        }
        DRAWING.drawBox(BOX, location.getHeightTerrain());
        if (!playersMap.isEmpty()) {
            DRAWING.drawHUD(playersMap.get(0), graphics, COPY_PLAYER_MAP, this.wind);
        }
        if (isEndGame) {
            DRAWING.drawFinalScore(COPY_PLAYER_MAP);
            if (delayBoard == 21) {
                for (int i = 0; i < haveInBoard.length; i++) {
                    if (!haveInBoard[i]) {
                        haveInBoard[i] = true;
                        delayBoard = 0;
                        delayReset = i;
                        break;
                    }
                }
                delayBoard = 0;
            }
            DRAWING.drawPlayerScore(delayReset, COPY_PLAYER_MAP);
            if (delayReset == haveInBoard.length - 1) {
                isFinishGame = true;
            }
            delayBoard++;
        }
    }
}