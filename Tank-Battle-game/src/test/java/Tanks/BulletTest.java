package Tanks;

import processing.core.PApplet;

import org.junit.jupiter.api.Test;
import processing.event.KeyEvent;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

public class BulletTest {

    public void printTest(String testName, boolean haveOutput) {
        System.out.println("***********************");
        System.out.printf("Start to run %s test!!%n", testName);
        if (haveOutput) {
            System.out.println("The output is: ");
        }
    }

    /**
     * This test use to test the trajectory of bullet
     */
    @Test
    public void testTrajectoryWithDynamicWind() {
        printTest("testTrajectorywithDynamicWindAndExplodeTerrain", true);
        App app = new App();
        app.setConfigPath("testFolder/test.json");
        app.loop();
        PApplet.runSketch(new String[]{"App"}, app);
        app.delay(1000);
        ArrayList<Player> playersMap = app.getLocation().getPlayersMap();
        Player  playerNow = playersMap.get(0);
        app.setWind(0);
        app.keyPressed(new KeyEvent(null, 0, 0, 0, ' ',32));
        app.setWind(35);
        app.delay(5000);
        assertEquals(100, playerNow.getHealth(), "Because of dynamic wind, player cannot hited by bullet");
        System.out.println("testTrajectorywithDynamicWind finished");
        app.noLoop();
        app.dispose();
    }

    /**
     * This test use to check the dame of the bullet correctly or not in both larger projectile and normal projectile
     * I will revive the parachute each time because I dont want to take the falling dame in this test
     * At the same time, I will also test the change level without key in this test to reduce CPU
     * And we also set wind to 0 to make it easier to test
     * Situation 1: normal projectile hit other tank
     * Situation 2: normal projectile hit owner tank so score remain unchange
     * Situation 3: dame larger than health have, score will change with the difference will be health of that player
     * Situation 4, 5, 6: Same as 1, 2, 3 but with larger projectile (Extension)
     */
    @Test
    public void testDamageAndScoreWithoutDameFalling() {
        printTest("testDamageAndScoreWithoutDameFalling", true);
        App app = new App();
        app.setConfigPath("testFolder/changeLevel.json");
        app.loop();
        PApplet.runSketch(new String[]{"App"}, app);
        app.delay(1000);
        ArrayList<Player> playersMap = app.getLocation().getPlayersMap();
        Player curPlayer = playersMap.get(0);
        Player nextPlayer = playersMap.get(1);
        app.setWind(0);
        app.keyPressed(new KeyEvent(null, 0, 0, 0, ' ',38));
        app.keyPressed(new KeyEvent(null, 0, 0, 0, ' ',38));
        app.keyPressed(new KeyEvent(null, 0, 0, 0, ' ',38));
        app.delay(550);
        app.keyPressed(new KeyEvent(null, 0, 0, 0, ' ',32));
        app.setWind(0);
        app.delay(5000);
        assertEquals(23, curPlayer.getScore(), "The score of player gain is not correct");
        assertEquals(23, 100 - nextPlayer.getHealth(), "Damage is not correct");
        assertEquals(curPlayer.getScore(), 100 - nextPlayer.getHealth(), "The dame gain and score have is not equal");
        System.out.println("Calculate dame of bullet and score is correct");

        //Situation hit by itself, dame still have but score do not have
        int healthNow = nextPlayer.getHealth();
        nextPlayer.setPower(0);
        nextPlayer.setnumParachute(1); // set back to 1 to make sure that this testcase is only take bullet dame
        app.setWind(0); // make this one to sure that the dame will gain 60 because without wind, the bullet will back and hit with constant dame
        app.keyPressed(new KeyEvent(null, 0, 0, 0, ' ',32));
        app.setWind(0); // band dynamic wind
        app.delay(2000);
        assertEquals(healthNow - 57, nextPlayer.getHealth(), "Damage is not correct");
        assertEquals(0, nextPlayer.getScore(), "Score is not correct, hit by itself is not taken into account");
        System.out.println("Calculate damage of bullet and score when hit by itself is correct");
        app.delay(2000);

        // test situation when dame is larger than health, then the maximum will be the score of that player. that player will die
        curPlayer.setPower(30);
        nextPlayer.setnumParachute(1); // take only dame, we do this one to overlook the dame of falling at this testcase
        healthNow = nextPlayer.getHealth();
        int scoreNow = curPlayer.getScore();
        app.setWind(0);
        app.keyPressed(new KeyEvent(null, 0, 0, 0, ' ',32));
        app.setWind(0);
        app.delay(3000);
        assertEquals(true, !nextPlayer.getIsAlive(), "player have to die");
        assertEquals(healthNow + scoreNow, curPlayer.getScore(), "Score is not correct");
        System.out.println("Calculate damage and score when dame is larger than health is correct");
        app.delay(2000);


        //Now at level 2, we will test with larger bullet
        playersMap = app.getLocation().getPlayersMap();
        curPlayer = playersMap.get(0);
        scoreNow = curPlayer.getScore();
        nextPlayer = playersMap.get(1);
        app.setWind(0);
        app.keyPressed(new KeyEvent(null, 0, 0, 0, ' ',38));
        app.keyPressed(new KeyEvent(null, 0, 0, 0, ' ',38));
        app.keyPressed(new KeyEvent(null, 0, 0, 0, ' ',38));
        app.keyPressed(new KeyEvent(null, 0, 0, 0, 'X',88));
        app.delay(550);
        app.keyPressed(new KeyEvent(null, 0, 0, 0, ' ',32));
        app.setWind(0);
        app.delay(5000);
        assertEquals(59, nextPlayer.getHealth(), "Damage of ultimate is not correct");
        assertEquals(scoreNow - 20 + 41, curPlayer.getScore(), "Score when hit by using ultimate is not correct");
        System.out.println("Calculate damage of larger bullet (ultimate) and score when hit by itself is correct");
        app.delay(2000);
        System.out.println("Change level after 1 second is correct");

        //Test ultimate hit by itself
        nextPlayer.setHealth(-100); // set health to 100 to test hit dame but the tank is not die, because we want to check that tank's score
        healthNow = nextPlayer.getHealth();
        nextPlayer.updateScore(20); // update score by 20 to by ultimate to test hit by itself
        nextPlayer.setPower(0);
        nextPlayer.setnumParachute(1); // set back to 1 to make sure that this testcase is only take bullet dame
        app.keyPressed(new KeyEvent(null, 0, 0, 0, 'X',88));
        app.delay(1000);
        app.setWind(0); // make this one to sure that the dame will gain 60 because without wind, the bullet will back and hit with constant dame
        app.keyPressed(new KeyEvent(null, 0, 0, 0, ' ',32));
        app.setWind(0); // band dynamic wind
        app.delay(5000);
        assertEquals(healthNow - 59, nextPlayer.getHealth(), "Damage of Ultimate when hit itself is not correct");
        assertEquals(0, nextPlayer.getScore(), "Score for Ultimate is not correct, hit by itself is not taken into account");
        System.out.println("Calculate damage of larger bullet(Ultimate) and score when hit by itself is correct");

        // test situation when dame is larger than health, then the maximum will be the score of that player. that player will die
        curPlayer.setPower(30);
        nextPlayer.setnumParachute(1); // take only dame, we band the dame because of falling
        healthNow = nextPlayer.getHealth();
        app.keyPressed(new KeyEvent(null, 0, 0, 0, 'X',88));
        scoreNow = curPlayer.getScore();
        app.setWind(0);
        app.keyPressed(new KeyEvent(null, 0, 0, 0, ' ',32));
        app.setWind(0);
        app.delay(3000);
        assertEquals(true, !nextPlayer.getIsAlive(), "player have to die");
        assertEquals(scoreNow + healthNow, curPlayer.getScore(), "Score when dame larger than health is not correct");
        System.out.println("Calculate damage and score for larger projectile when dame is larger than health is correct");
        app.delay(5000);
        app.noLoop();
        app.dispose();
    }
}
