package Tanks;

import org.junit.jupiter.api.*;
import processing.core.PApplet;
import processing.event.KeyEvent;


import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import java.util.ArrayList;


public class PlayerTest {

    public void printTest(String testName, boolean haveOutput) {
        System.out.println("***********************");
        System.out.printf("Start to run %s test!!%n", testName);
        if (haveOutput) {
            System.out.println("The output is: ");
        }
    }

    /**
     * We test moving players with 3 main situation for moving left and moving right
     * moving without fuel (cant move)
     * moving with enough fuel
     * and moving when falling (cant move)
     */
    @Test
    public void testMovingPlayer() {
        printTest("testMovingPlayer", true);
        printTest("MovingPlayersWithoutFuel", true);
        App app = new App();
        app.setConfigPath("testFolder/falling.json");
        app.loop();
        PApplet.runSketch(new String[]{"App"}, app);
        app.delay(1000);
        ArrayList<Player> playersMap = app.getLocation().getPlayersMap();
        Player curPlayer = playersMap.get(0);

        // Can not move without fuel
        int row = curPlayer.getRow();
        curPlayer.setFuel(250); // set fuel to current player to 0
        for (int i = 0; i < 10; i++) {
            app.delay(20);
            app.keyPressed(new KeyEvent(null, 0, 0, 0, ' ', 39));
        }
        app.delay(500);
        assertEquals(row, curPlayer.getRow(), "Player can't move right without fuel(Fail)");
        for (int i = 0; i < 10; i++) {
            app.delay(20);
            app.keyPressed(new KeyEvent(null, 0, 0, 0, ' ', 37));
        }
        app.delay(500);
        assertEquals(row, curPlayer.getRow(), "Player can't also move left without fuel(Fail)");
        app.delay(500);
        System.out.println("Player can't move without fuel(Pass)");


        //Move with fuel
        curPlayer.setFuel(-500);
        row = curPlayer.getRow() + 20;
        for (int i = 0; i < 10; i++) {
            app.delay(20);
            app.keyPressed(new KeyEvent(null, 0, 0, 0, ' ', 39));
        }
        app.delay(500);
        assertEquals(row, curPlayer.getRow(), "Player can also move right with enough fuel(Fail)");
        curPlayer.setFuel(-250);
        row = curPlayer.getRow() - 20;
        for (int i = 0; i < 10; i++) {
            app.delay(20);
            app.keyPressed(new KeyEvent(null, 0, 0, 0, ' ', 37));
        }
        app.delay(1000);
        assertEquals(row, curPlayer.getRow(), "Player can also move left with enough fuel(Fail)");
        System.out.println("Player can move both left and right with enough fuel(Pass)");


        // move when falling
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
        app.delay(2000);
        Player playerNow = playersMap.get(0);
        int rowNow = playerNow.getRow();
        app.delay(20);
        app.keyPressed(new KeyEvent(null, 0, 0, 0, ' ', 39));
        app.delay(20);
        app.keyPressed(new KeyEvent(null, 0, 0, 0, ' ', 39));
        app.delay(20);
        app.keyPressed(new KeyEvent(null, 0, 0, 0, ' ', 39));
        app.delay(2000);
        assertEquals(rowNow, playerNow.getRow(), "Player can't move when they are falling");
        System.out.println("Player can't move when they are falling (Pass)");

        //MOVING TURRET

        app.noLoop();
        app.dispose();
    }


    /**
     * this test will test changing power we will have 4 main situation
     * situation 1: increase power
     * situation 2: decrease power
     * situation 3: decrease power when power is 0
     * situation 4: increase power when power is 100
     */
    @Test
    public void testChangingPower() {
        printTest("changingPower", true);
        App app = new App();
        app.setConfigPath("config.json");
        app.loop();
        PApplet.runSketch(new String[]{"App"}, app);
        app.delay(1000);
        ArrayList<Player> playersMap = app.getLocation().getPlayersMap();
        Player curPlayer = playersMap.get(0);
        double power = curPlayer.getPower();
        double afterFrame = 0;
        for (int i = 0; i < 29; i++) {
            app.delay(10);
            afterFrame += (double) 36 / 30;
            app.keyPressed(new KeyEvent(null, 0, 0, 0, ' ', 87));
        }
        app.delay(500);
        assertEquals((int) (power + afterFrame), (int) (curPlayer.getPower()), "increasing power is not correct");

        // increase when power equal to their health
        curPlayer.setPower(curPlayer.getHealth());
        double powerNow = curPlayer.getPower();
        for (int i = 0; i < 29; i++) {
            app.delay(10);
            app.keyPressed(new KeyEvent(null, 0, 0, 0, 'W', 87));
        }
        assertEquals(powerNow, curPlayer.getPower(), "cant increase power when power equals to Player's health");
        System.out.println("Increasing power pass");



        //test decrese
        power = curPlayer.getPower();
        afterFrame = 0;
        for (int i = 0; i < 29; i++) {
            app.delay(10);
            afterFrame -= (double) 36 / 30;
            app.keyPressed(new KeyEvent(null, 0, 0, 0, ' ', 83));
        }
        app.delay(1000);
        assertEquals((int) (power + afterFrame), (int) curPlayer.getPower(), "Decreasing power is not correct");
        // decrease when power equals to 0
        curPlayer.setPower(0);
        for (int i = 0; i < 29; i++) {
            app.delay(10);
            app.keyPressed(new KeyEvent(null, 0, 0, 0, 'S', 83));
        }
        app.delay(1000);
        assertEquals(0, curPlayer.getPower(), "cant reducing power when power equals to 0");
        System.out.println("Decreasing power pass");
        app.noLoop();
        app.dispose();
    }


    /**
     * This test will test buying health, parachute, larger projectile, fuel as repair part in game
     * we will have some situation
     * --buying health
     *  - buying health when dont have enough score
     *  - buying health when health smaller than 80 and enough score to buy
     *  - buying health when HP larger than 80 and enough score to buy
     * -- buying parachute
     *  - buying with enough score
     *  - buying when we dont have enough score
     * -- buying fuel
     *  - buying when we dont have enough score
     *  - buying when we have enough score
     * --- buying ultimate
     *  -- buying when enough score
     *   - try from on to on
     *   - try from off to on
     *  -- buying when dont have enough score
     *  -- try to fire larger projectile to check after that it will turn off
     */
    @Test
    public void testShopping() {
        printTest("buyingHealth", true);
        App app = new App();
        app.setConfigPath("config.json");
        app.loop();
        PApplet.runSketch(new String[]{"App"}, app);
        app.delay(1000);
        ArrayList<Player> playersMap = app.getLocation().getPlayersMap();
        Player curPlayer = playersMap.get(0);

        //test buying health
        curPlayer.updateScore(20);
        curPlayer.setHealth(50);
        int healthNow = curPlayer.getHealth();
        int scoreNow = curPlayer.getScore();
        app.delay(1000);
        //test buying health for the case health smaller than 80 and score larger than 20
        app.keyPressed(new KeyEvent(null, 0, 0, 0, 'R',82));
        assertEquals(healthNow + 20, curPlayer.getHealth(), "Buying health is wrong");
        assertEquals(scoreNow - 20, curPlayer.getScore(), "Buying health is wrong");
        app.delay(200);
        //test buying health for the case score smaller than 20.
        healthNow = curPlayer.getHealth();
        scoreNow = curPlayer.getScore();
        assertEquals(healthNow, curPlayer.getHealth());
        assertEquals(scoreNow, curPlayer.getScore());
        app.delay(200);
        //test buying health for the case score larger than 80 and score larger than 20. However, at this step we will increase the health maximum to 100 and score is being lossed by 20
        curPlayer.setHealth(-20);
        curPlayer.updateScore(20);
        healthNow = curPlayer.getHealth();
        scoreNow = curPlayer.getScore();
        app.keyPressed(new KeyEvent(null, 0, 0, 0, 'R',82));
        assertEquals(100, curPlayer.getHealth());
        assertEquals(0, curPlayer.getScore());
        app.delay(2000);
        System.out.println("Buying health is correct");

        //buying fuel
        printTest("buyingFuel", true);
        curPlayer.updateScore(10);
        int fuelNow = curPlayer.getFuel();
        scoreNow = curPlayer.getScore();
        app.keyPressed(new KeyEvent(null, 0, 0, 0, 'F',70));
        app.delay(200);
        //case when score larger than 10
        assertEquals(scoreNow - 10, curPlayer.getScore());
        assertEquals(fuelNow + 200, curPlayer.getFuel());
        app.delay(200);
        app.keyPressed(new KeyEvent(null, 0, 0, 0, 'F',70));
        assertEquals(fuelNow + 200, curPlayer.getFuel());
        assertEquals(scoreNow - 10, curPlayer.getScore());
        app.delay(200);
        System.out.println("Buying fuel is correct");

        //buying parachute
        printTest("buyingParachute", true);
        int parachuteNow = curPlayer.getParachute();
        curPlayer.updateScore(15);
        scoreNow = curPlayer.getScore();
        app.keyPressed(new KeyEvent(null, 0, 0, 0, 'P',80));
        assertEquals(scoreNow - 15, curPlayer.getScore());
        assertEquals(parachuteNow + 1, curPlayer.getParachute());
        app.delay(200);
        app.keyPressed(new KeyEvent(null, 0, 0, 0, 'P',80));
        assertEquals(parachuteNow + 1, curPlayer.getParachute());
        assertEquals(scoreNow - 15, curPlayer.getScore());
        app.delay(200);
        System.out.println("Buying parachute is correct");

        // buying Ultimate and use Ultimate to check after use it have to be off
        printTest("buyingUltimate", true);
        curPlayer.updateScore(40);
        scoreNow = curPlayer.getScore();
        app.keyPressed(new KeyEvent(null, 0, 0, 0, 'X',88));
        assertEquals(scoreNow - 20, curPlayer.getScore());
        assertTrue(curPlayer.getIsUlti());
        app.delay(20);
        app.keyPressed(new KeyEvent(null, 0, 0, 0, 'X',88));
        // on to on does not change anything
        assertEquals(scoreNow - 20, curPlayer.getScore());
        assertTrue(curPlayer.getIsUlti());
        System.out.println("buying Ultimate is correct");
        app.delay(200);
        app.setWind(0);
        app.keyPressed(new KeyEvent(null, 0, 0, 0, ' ',32));
        app.delay(5000);
        assertTrue(!curPlayer.getIsUlti());
        System.out.println("Using ultimate is correct");
        app.noLoop();
        app.dispose();
    }

    /**
     * This one we will test rotate the turret that does not excees 180 degree
     */
    @Test
    public void testRotateTurret() {
        App app = new App();
        app.setConfigPath("config.json");
        app.loop();
        PApplet.runSketch(new String[]{"App"}, app);
        app.delay(1000);
        ArrayList<Player> playersMap = app.getLocation().getPlayersMap();
        Player curPlayer;
        if (!playersMap.isEmpty()) {
            // test move up turret
            curPlayer = playersMap.get(0);
            float dirTurretNow = curPlayer.getRotationAngle();
            for (int i = 0; i < 10; i++) {
                app.delay(10);
                app.keyPressed(new KeyEvent(null, 0, 0, 0, ' ',38));
                dirTurretNow += 0.1f;
            }
            assertEquals(dirTurretNow, curPlayer.getRotationAngle());
            dirTurretNow = curPlayer.getRotationAngle();
            //test move down turret
            for (int i = 0; i < 10; i++) {
                app.delay(10);
                app.keyPressed(new KeyEvent(null, 0, 0, 0, ' ', 40));
                dirTurretNow -= 0.1f;
            }
            assertEquals(dirTurretNow, curPlayer.getRotationAngle());

            //test move the turret just around 180 degree
            for (int i = 0 ; i < 16; i++) {
                app.delay(10);
                app.keyPressed(new KeyEvent(null, 0, 0, 0, ' ',38));
            }
            assertEquals(0, curPlayer.getRotationAngle());

            for (int i = 0; i < 36; i++) {
                app.delay(10);
                app.keyPressed(new KeyEvent(null, 0, 0, 0, ' ',40));
            }
            assertEquals(-(float)Math.PI, curPlayer.getRotationAngle());
            app.noLoop();
            app.dispose();
        }
    }

    /**
     * This test we test player it box in 2 situation
     * we will provide enough fuel for player to move all location of terrain two times (because box repair kits is random)
     * when health lower than 90
     * when health larger than 90
     */
    @Test
    public void testEatKits() {
        printTest("eatKits", true);
        App app = new App();
        app.setConfigPath("config.json");
        app.loop();
        PApplet.runSketch(new String[]{"App"}, app);
        app.delay(5000);
        ArrayList<Player> playersMap = app.getLocation().getPlayersMap();
        app.delay(200);
        app.keyPressed(new KeyEvent(null, 0, 0, 0, ' ',32));
        app.keyPressed(new KeyEvent(null, 0, 0, 0, ' ',32));
        // move from 0 to end of the map
        Player curPlayer = playersMap.get(0);
        curPlayer.setHealth(50);
        int healthNow = curPlayer.getHealth();
        curPlayer.updateScore(900);
        app.delay(2000);
        while (curPlayer.getScore() >= 10) {
            app.delay(20);
            app.keyPressed(new KeyEvent(null, 0, 0, 0, 'F',70));
        }
        while (curPlayer.getRow() >= 2) {
            app.delay(40);
            app.keyPressed(new KeyEvent(null, 0, 0, 0, ' ',37));
        }
        while (curPlayer.getRow() <= 860 ) {
            app.delay(40);
            app.keyPressed(new KeyEvent(null, 0, 0, 0, ' ',39));
        }
        assertEquals(healthNow + 10, curPlayer.getHealth(), "When player eat kits health have increase");

        app.delay(100);
        app.keyPressed(new KeyEvent(null, 0, 0, 0, ' ',32));
        app.delay(20);
        app.keyPressed(new KeyEvent(null, 0, 0, 0, ' ',32));
        curPlayer = playersMap.get(0);
        curPlayer.setFuel(-5000);
        curPlayer.setHealth(-100); // make player health equals to 100 so when eatbox, they have not recieved anything (miss branch)
        app.delay(2000);
        while (curPlayer.getRow() >= 2) {
            app.delay(40);
            app.keyPressed(new KeyEvent(null, 0, 0, 0, ' ',37));
        }
        while (curPlayer.getRow() <= 860 ) {
            app.delay(40);
            app.keyPressed(new KeyEvent(null, 0, 0, 0, ' ', 39));
        }
        assertEquals(100, curPlayer.getHealth(), "When player's health is 100 and player eats kits health haven't increase");
        System.out.println("testEatKits pass");
        app.noLoop();
        app.dispose();
    }

    /**
     * test player when moving to the hole they have to be die so we create a hole then move player to that hole
     */
    @Test
    public void testWhenRunOutOfMap() {
        printTest("whenRunOutOfMap", true);
        App app = new App();
        app.setConfigPath("testFolder/outrange.json");
        app.loop();
        PApplet.runSketch(new String[]{"App"}, app);
        app.delay(1000);
        ArrayList<Player> playersMap = app.getLocation().getPlayersMap();
        app.setWind(0);
        app.keyPressed(new KeyEvent(null, 0, 0, 0, ' ',32));
        app.setWind(0);
        app.delay(2000);
        app.keyPressed(new KeyEvent(null, 0, 0, 0, ' ',32));
        app.setWind(0);
        app.delay(2000);
        Player playerNow = playersMap.get(0);
        playerNow.setRotationAngle((float) -Math.PI/4);
        app.delay(200);
        Player nextPlayer = playersMap.get(1);
        nextPlayer.setRotationAngle((float) Math.PI/2);
        app.delay(2000);
        app.setWind(0);
        app.keyPressed(new KeyEvent(null, 0, 0, 0, ' ',32));
        app.setWind(0);
        app.delay(2000);
        for (int i = 0; i < 10; i++) {
            app.delay(20);
            app.keyPressed(new KeyEvent(null, 0, 0, 0, ' ',37));
        }
        for (int i = 0; i < 100; i++) {
            app.delay(20);
            app.setWind(0);
            app.keyPressed(new KeyEvent(null, 0, 0, 0, ' ',32));
            app.setWind(0);
        }
        app.delay(2000);
        for (int i = 0; i < 100; i++) {
            app.delay(20);
            app.keyPressed(new KeyEvent(null, 0, 0, 0, ' ',39));
        }
        app.delay(2000);
        assertTrue(nextPlayer.getIsAlive(), "Player have to be die because that tank go out of range of the map");
        System.out.println("testRunOutOfMap pass");
        app.noLoop();
        app.dispose();
    }
}
