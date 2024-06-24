package Tanks;
import java.util.*;
import org.junit.jupiter.api.Test;

import static java.lang.Math.abs;
import static org.junit.jupiter.api.Assertions.*;

public class TestLocation {
    
    private void printTest(String testName, boolean haveOutput) {
        System.out.println("***********************");
        System.out.printf("Start to run %s test!!%n", testName);
        if (haveOutput) {
            System.out.println("The output is: ");
        }
    }

    /**
     * This test we will recieve txt board correctly and check the location of tree and
     * terrain and player is correct or not
     */
    @Test
    public void testValidBoard() {
        printTest("testValidBoard", true);
        ReadJson readJson = new ReadJson("config.json");
        Location location = new Location("level1.txt", readJson);
        assertEquals(location.getBoard()[13][0], "X", "the location of terrain in the matrix is wrong");
        assertEquals(location.getBoard()[10][3], "T", "The location of tree in the matrix is wrong");
        assertEquals(location.getBoard()[11][2], "A", "The location of player in the matrix is wrong");
        boolean emptySpace = location.getBoard()[16][0].equals(" ") || location.getBoard()[16][3].equals("\t");
        assertTrue(emptySpace, "The location of empty space is wrong");
        System.out.println("testValidBoard passed");
    }

    /**
     * This test we will test the txt file contain undefine sign in board and check it will run without any errors or not
     */
    @Test
    public void testBoardWithUndefineSign() {
        printTest("testBoardWithUndefineSign", true);
        ReadJson readJson = new ReadJson("testFolder/locationtest.json");
        Location location = new Location("testFolder/location.txt", readJson);
        String[][]  board = location.getBoard();
        assertEquals(board[12][3], "%", "the board is not work when the sign is undefined in config file, you have to skip this sign to continue");
        System.out.println("testBoardWithUndefineSign passed");
    }


    /**
     * This test will test the board dont have enough line, so we have to check the game will run sucessfully
     * or not
     */
    @Test
   public void testNotEnoughLine() {
        printTest("testNotEnoughLine", true);
       ReadJson readJson = new ReadJson("config.json");
       Location location = new Location("testFolder/level0.txt", readJson);
       assertEquals(location.getBoard()[13][0], "X", "the location of terrain in the matrix is wrong");
       assertEquals(location.getBoard()[12][1], "T", "The location of tree in the matrix is wrong.");
       assertEquals(location.getBoard()[12][0], "A", "The location of player in the matrix is wrong.");
       assertEquals(location.getBoard()[12][3], "%", "undefine sign also have to be in board");
       boolean emptySpace = location.getBoard()[5][0].equals(" ") || location.getBoard()[16][3].equals("\t");
       assertTrue(emptySpace, "The location of empty space is wrong");
       System.out.println("testNotEnoughLine passed");
   }

    /**
     * This test will test the txt file have more line and we also have to ensure that the game still run correctly
     */
   @Test
   public void LineExceed() {
        printTest("testLineExceed", true);
        ReadJson readJson = new ReadJson("testFolder/exceedMap.json");
        Location location = new Location("testFolder/exceed.txt", readJson);
        assertEquals(location.getBoard()[13][0], "X", "the location of terrain in the matrix is wrong");
        assertEquals(location.getBoard()[12][1], "T", "The location of tree in the matrix is wrong.");
        assertEquals(location.getBoard()[12][0], "A", "The location of player in the matrix is wrong.");
        assertEquals(location.getBoard()[12][3], "%", "undefine sign also have to be in board");
        boolean emptySpace = location.getBoard()[5][0].equals(" ") || location.getBoard()[16][3].equals("\t");
        assertTrue(emptySpace, "The location of empty space is wrong");
        System.out.println("testLineExceed passed");
   }

    /**
     * This test we will test the situation that the txt file exceed with col larger than 28, but the
     * game also have to run smoothly
     */
   @Test
   public void exceedCol() {
        printTest("testExceedCol", true);
       ReadJson readJson = new ReadJson("testFolder/exceedMap.json");
       Location location = new Location("testFolder/exceedCol.txt", readJson);
       assertEquals(location.getBoard()[13][0], "X", "the location of terrain in the matrix is wrong");
       assertEquals(location.getBoard()[12][1], "T", "The location of tree in the matrix is wrong.");
       assertEquals(location.getBoard()[12][0], "A", "The location of player in the matrix is wrong.");
       assertEquals(location.getBoard()[12][3], "%", "undefine sign also have to be in board");
       boolean emptySpace = location.getBoard()[5][0].equals(" ") || location.getBoard()[16][3].equals("\t");
       assertTrue(emptySpace, "The location of empty space is wrong");
       System.out.println("testExceedCol passed");
    }

    /**
     * this test will test the txt is non exist
     */
   @Test
   public void testNonExistMap() {
    printTest("testNonExistMap", true);
    ReadJson readJson = new ReadJson("config.json");
    Location location = new Location("non", readJson);
    assertNull(location.getBoard(), "The board have to be null");
    System.out.println("testNonExistMap pass");
   }

    /**
     * This test we will test the location of tree when the map dont have tree
     */
   @Test
    public void testMapWithoutTreeInBothConfigAndMap() {
       printTest("testMapWithoutTreeInBothConfigAndMap", true);
       ReadJson readJson = new ReadJson("config.json");
       Location location = new Location("level2.txt", readJson);
       ArrayList<Integer> locationTree = location.getLocationTree();
       assertEquals(0, locationTree.size(), "The size of location of tree have to be 0 because it dont have in map");
       System.out.println("testMapWithoutTreeInBothConfigAndMap pass");


       printTest("testMapWithoutTreeButDefineInConfig", true);
       readJson = new ReadJson("testFolder/locationtest.json");
       location = new Location("level2.txt", readJson);
       locationTree = location.getLocationTree();
       assertEquals(0, locationTree.size(), "The size of location of tree have to be 0 because it dont have in map");
       System.out.println("testMapWithoutTreeButDefineInConfig pass");


       printTest("testMapWithTreeInMapButNotInConfig", true);
       readJson = new ReadJson("testFolder/locationtest.json");
       location = new Location("testFolder/level0.txt", readJson);
       locationTree = location.getLocationTree();
       assertNotEquals(0, locationTree.size(), "Tree also have in map so we still need to have the location of them but when draw we will have to skip them (drawTree method)");
       System.out.println("testMapWithTreeInMapButNotInConfig pass");
   }


    /**
     * This test we will test the map have tree and also define in the config file and ensure that the tree
     * is random correctly
     */
    @Test
    public void testMapWithTree() {
        printTest("testMapWithTree", true);
        ReadJson readJson = new ReadJson("testFolder/locationtest.json");
        Location location = new Location("testFolder/level0.txt", readJson);
        ArrayList<Integer> locationTreeActual = location.getLocationTree();
        String[][] board = location.getBoard();
        ArrayList<Integer> locTree = new ArrayList<>();
        for (int i = 0; i < board[0].length; i++) {
            for (String[] strings : board) {
                // Find location of tree in board
                if (strings[i].equals("T")) {
                    int loc = (i * 32);
                    loc = Math.max(0, Math.min(loc, 864));
                    locTree.add(loc);
                }
            }
        }

        for (int i = 0; i < locTree.size(); i++) {
            boolean check = Math.abs(locTree.get(i) - locationTreeActual.get(i)) <= 15;
            assertTrue(check, "The location of tree " + locationTreeActual.get(i) + " is wrong");
        }
        System.out.println("testMapWithTree pass");
    }

    /**
     * This test we will test the height terrain of the map will correct or not and moving average is correct or not
     */
    @Test
    public void testheightTerrain() {
        printTest("testheightTerrain", true);
        ReadJson readJson = new ReadJson("config.json");
        Location location = new Location("level1.txt", readJson);
        float[] heightTerrainActual = location.getHeightTerrain();
        assertEquals(255.0f, heightTerrainActual[0], "Height terrain at col 0 is wrong");
        assertEquals(257.0f, heightTerrainActual[1], "Height terrain at col 1 is wrong");
        assertEquals(288.0f, heightTerrainActual[96], "Height terrain at col 96 is wrong");
        assertEquals(288.0f, heightTerrainActual[97], "Height terrain at col 97 is wrong");
        assertEquals(198.0f, heightTerrainActual[296], "Height terrain at col 296 is wrong");
        System.out.println("testheightTerrain pass");
   }


    /**
     * The test we will test the location of players render in the game is correct or not
     */
    @Test
    public void testLocationPlayer() {
        printTest("testLocationPlayer", true);
        ReadJson readJson = new ReadJson("config.json");
        Location location = new Location("level1.txt", readJson);
        ArrayList<Player> players = location.getPlayersMap();
        assertEquals(80, players.get(0).getRow(), "The location of player " + players.get(0).getName() + " is wrong");
        assertEquals(356.25f, players.get(0).getCol(), "The location of player " + players.get(0).getName() + " is wrong");
        assertEquals(432, players.get(1).getRow(), "The location of player " + players.get(1).getName() + " is wrong");
        assertEquals(360.5f, players.get(1).getCol(), "The location of player " + players.get(1).getName() + " is wrong");
        assertEquals(656, players.get(2).getRow(), "The location of player " + players.get(2).getName() + " is wrong");
        assertEquals(391.0f, players.get(2).getCol(), "The location of player " + players.get(2).getName() + " is wrong");
        assertEquals(784, players.get(3).getRow(), "The location of player " + players.get(3).getName() + " is wrong");
        assertEquals(363.75, players.get(3).getCol(), "The location of player " + players.get(3).getName() + " is wrong");
        System.out.println("testLocationPlayer pass");
    }


    /**
     * this test use to test the order of players and in scoreboard is in alphabet order or not
     */
    @Test
    public void testCorrectOrder() {
        printTest("testCorrectOrder", true);
        ReadJson readJson = new ReadJson("testFolder/nonOrder.json");
        Location location = new Location("testFolder/nonOrder.txt", readJson);
        ArrayList<Player> playersMap = location.getPlayersMap();
        assertEquals("A", playersMap.get(0).getName(), "The order of game is not correct");
        assertEquals("B", playersMap.get(1).getName(), "The order of game is not correct");
        assertEquals("C", playersMap.get(2).getName(), "the order of game is not correct");
        assertEquals("D", playersMap.get(3).getName(), "the order of game is not correct");
        System.out.println("testCorrectOrder pass");
    }
}
