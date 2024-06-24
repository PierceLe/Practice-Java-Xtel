package Tanks;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;


public class TestLevel {

    private void printTest(String testName, boolean haveOutput) {
        System.out.println("***********************");
        System.out.printf("Start to run %s test!!%n", testName);
        if (haveOutput) {
            System.out.println("The output is: ");
        }
    }

    /**
     * This class use to check every method setter and getter of Level class
     */
    @Test
    public void testLevelClass() {
        printTest("testLevelClass", true);
        String layout = "level1.txt";
        String background = "snow.png";
        String foregroundColor = "255, 255, 255";
        String trees = "tree1.png";
        Level level1 = new Level(layout, background, foregroundColor, trees);
        Level level2 = new Level(layout, background, foregroundColor, trees);
        Level level3 = new Level(layout, background, foregroundColor, "tree2.png");
        Level level4 = new Level("level2.txt", background, "1,1,1", "tree3.png");
        Level level5 = new Level(layout, "desert.png", foregroundColor, "tree4.png");
        Level level6 = new Level(layout, background, "1,1,1", "tree5.png");
        Level level7 = new Level("level2.txt", background, foregroundColor, trees);
        boolean same = level1.equals(level2);
        boolean different1 = level1.equals(level3);
        boolean different2 = level1.equals(level4);
        boolean different3 = level1.equals(level5);
        boolean different4 = level1.equals(level6);
        boolean different5 = level1.equals(level7);
        boolean different6 = level1.equals("hi");
        assertTrue(same, "Equals method is wrong");
        assertFalse(different1, "Equals method is wrong");
        assertFalse(different2, "Equals method is wrong");
        assertFalse(different3, "Equals method is wrong");
        assertFalse(different4, "Equals method is wrong");
        assertFalse(different5, "Equals method is wrong");
        assertFalse(different6, "equals method is wrong");
        assertEquals(layout, level1.getLayout(), "Wrong layout");
        assertEquals(background, level1.getBackground(), "Wrong background");
        assertEquals(foregroundColor, level1.getForeGroundColor(), "Wrong foregroundColor");
        assertEquals(trees, level1.getTrees(), "Wrong tree");
        System.out.println("TestLevelClass is pass");
    }
}
