package Tanks;

import com.jogamp.nativewindow.ToolkitLock;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import processing.core.PApplet;
import processing.event.KeyEvent;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

public class AppTest {
    private void printTest(String testName, boolean haveOutput) {
        System.out.println("***********************");
        System.out.printf("Start to run %s test!!%n", testName);
        if (haveOutput) {
            System.out.println("The output is: ");
        }
    }

    /**
     * this test will check change player correctly, after we press space
     */
    @Test
    public void testChangePlayer() {
        printTest("testChangePlayer", true);
        App app = new App();
        app.setConfigPath("testFolder/bullet.json");
        app.loop();
        PApplet.runSketch(new String[]{"App"}, app);
        app.delay(1000);
        ArrayList<Player> playersMap = app.getLocation().getPlayersMap();
        for (Player i : playersMap) {
            i.setnumParachute(0);
        }
        Player curPlayer = playersMap.get(0);
        curPlayer.setPower(10);
        app.setWind(0);
        app.keyPressed(new KeyEvent(null, 0, 0, 0, ' ',32));
        app.setWind(0);
        app.delay(5000);
        assertEquals(playersMap.get(1), curPlayer);

        System.out.println("Test change Player pass");
        playersMap.get(0).setRotationAngle((float) Math.PI / 6);
        playersMap.get(0).setPower(100);
        app.setWind(0);
        app.keyPressed(new KeyEvent(null, 0, 0, 0, ' ',32));
        app.setWind(0);
        app.delay(2000);
        app.noLoop();
        app.dispose();
    }


    /**
     * This test check when the game reach end, we press R to reset. However we have to wait
     * until the final socre finish, then we can press R to reset game
     */
    @Test
    public void testEndGameAndReset() {
        App app = new App();
        app.setConfigPath("testFolder/test.json");
        app.loop();
        PApplet.runSketch(new String[]{"App"}, app);
        app.delay(5000);
        ArrayList<Player> playersMap = app.getLocation().getPlayersMap();
        for (Player i : playersMap) {
            i.setnumParachute(0);
        }
        Player curPlayer = playersMap.get(0);
        curPlayer.updateScore(50);
        curPlayer.setPower(0);
        app.keyPressed(new KeyEvent(null, 0, 0, 0, 'X',88));
        app.setWind(0);
        app.keyPressed(new KeyEvent(null, 0, 0, 0, ' ',32));
        app.setWind(0);
        app.delay(2000);
        app.keyPressed(new KeyEvent(null, 0, 0, 0, 'R',82)); // cant reset until scoreboard finish
        app.delay(5000);
        app.keyPressed(new KeyEvent(null, 0, 0, 0, 'R',82));
        app.delay(5000);
        playersMap = app.getLocation().getPlayersMap();
        curPlayer = playersMap.get(0);
        assertEquals(2, playersMap.size(), "Test game reset is wrong");
        assertEquals(100, curPlayer.getHealth(), "Test game reset is not correct");
        System.out.println("Test end game correct!");
        app.noLoop();
        app.dispose();
    }


    @Test
    /**
     * This test will test change level correctly, we will use space immediately when only one player in the game
     * then check the game have been reseted or not
     */
    public void testChangeLevelWithKey() {
        printTest("testChangeLevelWithKey", true);
        App app = new App();
        app.setConfigPath("testFolder/changeLevel.json");
        app.loop();
        PApplet.runSketch(new String[]{"App"}, app);
        app.delay(5000);
        ArrayList<Player> playersMap = app.getLocation().getPlayersMap();
        for (Player i : playersMap) {
            i.setnumParachute(0);
        }
        Player curPlayer = playersMap.get(0);
        curPlayer.updateScore(50);
        curPlayer.setPower(0);
        app.keyPressed(new KeyEvent(null, 0, 0, 0, 'X',88));
        app.keyPressed(new KeyEvent(null, 0, 0, 0, ' ',38));
        app.keyPressed(new KeyEvent(null, 0, 0, 0, ' ',38));
        app.keyPressed(new KeyEvent(null, 0, 0, 0, ' ',38));
        app.setWind(0);
        app.keyPressed(new KeyEvent(null, 0, 0, 0, ' ',32));
        app.delay(550);
        app.setWind(0);
        app.delay(1000);
        app.keyPressed(new KeyEvent(null, 0, 0, 0, ' ',32));
        app.delay(5000);
        playersMap = app.getLocation().getPlayersMap();
        curPlayer = playersMap.get(0);
        assertEquals(2, playersMap.size(), "Test change level is wrong");
        assertEquals(100, curPlayer.getHealth(), "Test change level is not correct");
        System.out.println("Test change level correct");
        app.noLoop();
    }

    @Test
    public void MainTests() {
        App.main(new String[]{});
        System.out.println("Successfully called .main");
    }
}
