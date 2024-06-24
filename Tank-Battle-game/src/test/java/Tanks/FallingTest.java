package Tanks;
import org.junit.jupiter.api.Test;
import processing.core.PApplet;
import processing.event.KeyEvent;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

public class FallingTest {


    /**
     * This test will check the falling dame only. Thus I will set parachute to 0. we have 2 main situation
     * Falling dame is smaller than health
     * Falling dame is larger than HP of that tank
     */
    @Test
    public void testFallingDameOnly() {
        App app = new App();
        app.setConfigPath("testFolder/falling.json");
        app.loop();
        PApplet.runSketch(new String[]{"App"}, app);
        app.delay(1000);
        ArrayList<Player> playersMap = app.getLocation().getPlayersMap();
        for (Player i : playersMap) {
            i.setnumParachute(0);
        }
        Player curPlayer = playersMap.get(0);
        Player secondPlayer = playersMap.get(1);
        Player thirdPlayer = playersMap.get(2);
        secondPlayer.setRotationAngle((float) Math.PI / 2);
        app.delay(1000);
        curPlayer.setPower(100);
        app.keyPressed(new KeyEvent(null, 0, 0, 0, ' ', 38));
        curPlayer.updateScore(20);
        app.keyPressed(new KeyEvent(null, 0, 0, 0, 'X',88));
        app.delay(200);
        for (int i = 0; i < 5; i++) {
            app.delay(20);
            app.keyPressed(new KeyEvent(null, 0, 0, 0, ' ',38));
        }
        app.setWind(0);
        app.keyPressed(new KeyEvent(null, 0, 0, 0, ' ',32));
        app.setWind(0);
        app.keyPressed(new KeyEvent(null, 0, 0, 0, ' ',32));
        app.setWind(0);
        app.delay(3000);
        assertEquals(9, secondPlayer.getHealth(), "Dame falling is not correct");
        assertEquals(91, curPlayer.getScore(), "Score for make opponent falling is not correct");
        System.out.println("Test dame falling is correct (Pass)");

        // Setup for situation that dame of falling is larger than health
        for (int i = 0; i < 8; i++) {
            app.delay(20);
            app.keyPressed(new KeyEvent(null, 0, 0, 0, ' ',40));
        }
        thirdPlayer.setPower(100);
        int health = secondPlayer.getHealth();
        app.keyPressed(new KeyEvent(null, 0, 0, 0, ' ',32));
        app.delay(3000);
        assertTrue(!secondPlayer.getIsAlive(), "When dame of falling is larger than health, that player have to be die");
        assertEquals(health, thirdPlayer.getScore(), "the score gain from that shot is equals to the health of player being fired");
        System.out.println("Test dame falling is correct at the situation than dame of falling is larger than health");
    }
}
