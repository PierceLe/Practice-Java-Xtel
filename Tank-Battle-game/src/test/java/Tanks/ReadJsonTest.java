package Tanks;

import org.junit.jupiter.api.Test;
import processing.core.PApplet;
import processing.core.PImage;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import java.util.ArrayList;
import java.util.HashMap;

public class ReadJsonTest {
    String filePath = "config.json";

    //helper function
    public void printTest(String testName, boolean haveOutput) {
        System.out.println("***********************");
        System.out.printf("Start to run %s test!!%n", testName);
        if (haveOutput) {
            System.out.println("The output is: ");
        }
    }

    /**
     * this test is test when the test is load the image file sucessfully or not by testing that the graphics is null or not
     */
    @Test
    public void testLoadGraphics() {
        printTest("testLoadGraphics", true);
        App app = new App();
        app.setConfigPath("config.json");
        app.loop();
        PApplet.runSketch(new String[]{"App"}, app);
        app.delay(1000);

        ReadJson readJson = new ReadJson(filePath);
        Map<String, PImage> testGraphics = readJson.getGraphics(app);
        assertNotNull(testGraphics.get("basic.png"), "Failed to load basic.png");
        assertNotNull(testGraphics.get("desert.png"), "Failed to load desert.png");
        assertNotNull(testGraphics.get("forest.png"), "Failed to load forest.png");
        assertNotNull(testGraphics.get("fuel"), "Failed to load fuel");
        assertNotNull(testGraphics.get("hills"), "Failed to load hills");
        assertNotNull(testGraphics.get("parachute"), "Failed to load parachute");
        assertNotNull(testGraphics.get("snow.png"), "Failed to load snow.png");
        assertNotNull(testGraphics.get("tree1.png"), "Failed to load tree1.png");
        assertNotNull(testGraphics.get("tree2.png"), "Failed to load tree2.png");
        assertNotNull(testGraphics.get("wind_left"), "Failed to load wind_left");
        assertNotNull(testGraphics.get("wind"), "Failed to load wind");
        System.out.println("testLoadGraphics pass!");
    }

    /**
     * this test use to test the situation that we read non exist config file
     */
    @Test
    public void testNonExistsConfig() {
        printTest("testNonExistsConfig", true);
        ReadJson readJson = new ReadJson("nonexist.json");
        assertNull(readJson.getLevels(), "testNonExistConfig for level fail!");
        assertNull(readJson.getPlayers(), "testNonExistConfig for player fail!");
        System.out.println("testNonExistsConfig pass");
    }


    /**
     * test the situation that the map dont have tree and it still run sucessfully
     */
    @Test
    public void testLevelWithoutTree() {
        printTest("testNonExistsConfig", true);
        ReadJson readJson = new ReadJson("config.json");
        Map<Integer, Level> actualLevels = readJson.getLevels();
        String trees = actualLevels.get(2).getTrees();
        assertEquals("null",trees, "Trees in level 2 have to be null");
        System.out.println("testLevelWithoutTree pass");
    }

    /**
     * this test use to check the properties of each level when we read is correct or not
     */
    @Test
    public void testCorrectPropertiesLevel() {
        printTest("testCorrectPropertiesLevel", true);
        ReadJson readJson = new ReadJson("config.json");
        Map<Integer, Level> actualLevels = readJson.getLevels();
        String trees = actualLevels.get(1).getTrees();
        String foregroundColour = actualLevels.get(1).getForeGroundColor();
        String background = actualLevels.get(1).getBackground();
        String layout = actualLevels.get(1).getLayout();
        assertEquals("level1.txt", layout, "Layout pass");
        assertEquals("snow.png", background, "background pass");
        assertEquals("255,255,255",foregroundColour, "foreGroundColor pass");
        assertEquals("tree2.png", trees, "tree pass");
        System.out.println("testCorrectPropertiesLevel pass");
    }

    /**
     * test when we take the level larger than max level and it have to be null
     */
    @Test
    public void testAcessOutOfRangeLevel() {
        printTest("testAccessOutOfRange", true);
        ReadJson readJson = new ReadJson("config.json");
        Map<Integer, Level> actualLevels = readJson.getLevels();
        Level actual = actualLevels.get(4);
        assertNull(actual, "Max level is 3 so 4 have to be null");
        System.out.println("testAcessOutOfRangeLevel pass");
    }


    /**
     * test the situation that the properties of player in JSON file is correctly
     */
    @Test
    public void testCorrectPropertiesPlayers() {
        printTest("testCorrectPropertiesPlayers", true);
        ReadJson readJson = new ReadJson("config.json");
        Map<String, Player> actualPlayers = readJson.getPlayers();
        Player A = actualPlayers.get("A");
        assertEquals(A.getColor(),"0,0,255", "Color of player A is not correct");
        Player OutRange = actualPlayers.get("-.-");
        assertNull(OutRange, "This sign is not in config file so it have to be null");
        System.out.println("testCorrectPropertiesPlayers");
    }

    /**
     * we test the properties of each level is correct or not by using equals assert,
     * and we create new object with everything same to compare
     */
    @Test
    public void testInitLevel() {
        printTest("testInitLevel", true);
        ReadJson readJson = new ReadJson(filePath);
        Map<Integer, Level> actual = readJson.getLevels();
        Level level1 = new Level("level1.txt", "snow.png", "255,255,255", "tree2.png");
        Level level2 = new Level("level2.txt", "desert.png", "234,221,181", "null");
        Level level3 = new Level("level3.txt", "basic.png", "120,171,0", "tree1.png");
        ArrayList<Level> levels = new ArrayList<>();
        levels.add(level1);
        levels.add(level2);
        levels.add(level3);
        for (int i = 1; i <= readJson.getNumbersLevel(); i++) {
            assertEquals(actual.get(i), levels.get(i - 1), "init Level is wrong at level" + i);
        }
        System.out.println("testInitLevel pass!");
    }

    /**
     * test the color and the name of players in players list in JSON file is correct or not
     */
    @Test
    public void testInitPlayers() {
        printTest("testInitPlayers", true);
        ReadJson readJson = new ReadJson(filePath);
        Map<String, Player> actual = readJson.getPlayers();
        Map<String, Player> expected = new HashMap<>();
        Player A = new Player();
        A.setColor("0,0,255");
        Player I = new Player();
        expected.put("A", A);
        assertEquals(expected.get("A").getColor(), actual.get("A").getColor(), "initPlayer is fail");
        System.out.println("testInitPlayers pass!");
    }

    /**
     * We test with situation that random players will render random color or not by using regex to ensure the form still
     * correct
     */
    @Test
    public void testRandomPlayers() {
        printTest("testRandomPlayers", true);
        ReadJson readJson = new ReadJson("config.json");
        Map<String, Player> actualPlayers = readJson.getPlayers();
        Player G = actualPlayers.get("G");
        assertTrue(G.getColor().matches("\\d{1,3},\\d{1,3},\\d{1,3},"), "Color of player G is not correct.");
    }
}
