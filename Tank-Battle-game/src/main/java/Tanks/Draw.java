package Tanks;


import java.util.ArrayList;
import java.util.Collections;
import java.util.Map;
import processing.core.PImage;

/**
 * Represents a drawing utility class in the Tanks game.
 * This class is responsible for encapsulating drawing operations, including animations.
 */
public class Draw {

    private final App APP;
    private final UltimateAnimation ULTIMATE_ANIMATION;

    /**
     * Constructs a Draw object.
     *
     * @param app The main application window where drawing is performed.
     */
    public Draw(App app) {
        this.APP = app;
        this.ULTIMATE_ANIMATION = new UltimateAnimation(155, 75, 10);
    }

    /**
     * Draws the terrain for the game based on the provided location and color settings.
     * @param location The location object that holds terrain height information.
     * @param color A string array containing RGB color values as strings. This color present the color of terrain in game
     */
    public void drawTerrain(Location location, String[] color) {
        int r = Integer.parseInt(color[0]);
        int g = Integer.parseInt(color[1]);
        int b = Integer.parseInt(color[2]);
        for (int i = 0; i < location.getHeightTerrain().length; i++) {
            APP.stroke(r, g, b);
            APP.rect(i, (int) (640 - location.getHeightTerrain()[i]), 1, 640 - (int) (640 - location.getHeightTerrain()[i]));
        }
    }

    /**
     * Draws players on the game canvas and manages their state transitions based on their conditions
     * such as dead or live is falling or not,..
     * @param playersMap The list of players currently in the game.
     * @param location The location object that represent location of every object in game.
     * @param parachute The image of the parachute.
     * @param exploding The list of explosions.
     */
    public void drawPlayers(ArrayList<Player> playersMap, Location location, PImage parachute, ArrayList<Explosion> exploding) {
        for (int i = 0; i < playersMap.size(); i++) {
            if (playersMap.get(i).getCol() >= 639) {
                Explosion a = new Explosion(playersMap.get(i).getRow(), playersMap.get(i).getCol(), 6, 30);
                exploding.add(a);
                playersMap.remove(i);
                continue;
            }
            if (playersMap.get(i).getFall()) {
                if (playersMap.get(i).getParachute() > 0) {
                    APP.image(parachute, (float)(playersMap.get(i).getRow() - 32), playersMap.get(i).getCol() - 64, 64, 64);
                    playersMap.get(i).Fall(location.getHeightTerrain());
                } else {
                    playersMap.get(i).Fall(location.getHeightTerrain());
                    if (playersMap.get(i).getPower() >= playersMap.get(i).getHealth()) {
                        playersMap.get(i).setPower(playersMap.get(i).getHealth());
                    }
                    if (playersMap.get(i).getHealth() <= 0) {
                        playersMap.get(i).setAlive(false);
                    }
                }
            }
            if (playersMap.get(i).getIsAlive() || playersMap.get(i).getFall()) {
                playersMap.get(i).draw(APP);
            } else {
                Explosion a = new Explosion(playersMap.get(i).getRow(), playersMap.get(i).getCol(), 6, 30);
                exploding.add(a);
                playersMap.remove(i);
            }
        }
    }

    /**
     * Draws and updates boxes on the game
     * @param box The list of box objects currently in the game.
     * @param heightTerrain An array representing the height of the terrain.
     */
    public void drawBox(ArrayList<Box> box, float[] heightTerrain) {
        for (int i = 0; i < box.size(); i++) {
            Box boxNow = box.get(i);
            if (boxNow.getEaten()) {
                box.remove(boxNow);
                i -= 1;
                continue;
            }
            if (!boxNow.isFinish(heightTerrain)) {
                boxNow.draw(APP);
            } else {
                boxNow.setCol(640 - heightTerrain[boxNow.getRow()]);
                boxNow.drawKit(APP);
            }
        }
    }

    /**
     * Draws and updates bullets on the game.
     *
     * @param bullets The list of bullets.
     * @param location The location object that holds location of every object.
     * @param playersMap The list of players currently in the game.
     * @param exploding The list of active explosions, to which new explosions are added when bullets collide with the terrain.
     * @param wind The current wind speed affecting bullet trajectory.
     */
    public void drawBullets(ArrayList<Bullet> bullets, Location location, ArrayList<Player> playersMap, ArrayList<Explosion> exploding, float wind) {
        if (!bullets.isEmpty()) {
            for (int i = 0; i < bullets.size(); i++) {
                Bullet bullet = bullets.get(i);
                if (bullet.collision(location.getHeightTerrain())) {
                    bullet.checkHitBox(playersMap);
                    bullet.explode(playersMap, location.getHeightTerrain());
                    Explosion a = new Explosion(bullet.getRow(), bullet.getCol(), 6, bullet.getDIAMTER());
                    exploding.add(a);
                    bullets.remove(i);
                } else { 
                    bullet.draw(APP);
                    bullet.updateVelocity(wind);
                    bullet.updateLocation();
                    if (bullet.getRow() >= App.WIDTH || bullet.getRow() <= 0) {
                        bullets.remove(i);
                    }
                }
            }
        }
    }


    /**
     * Draws the animation of bullets when explode
     * @param exploding The list of explosions currently active in the game.
     */
    public void drawExplosion(ArrayList<Explosion> exploding) {
        for (int i = 0; i < exploding.size(); i++) {
            Explosion explosion = exploding.get(i);
            if (explosion.getCount() >= explosion.getLimit()) {
                exploding.remove(i);
                i--;
            } else {
                explosion.draw(APP);
            }
        }
    }

    /**
     * Draws tree images on the game canvas at specified locations.
     * @param location The location object containing data about where trees are located and the height of the terrain.
     * @param tree The image of the tree to be drawn.
     */
    public void drawTree(Location location, PImage tree) {
        for (int i = 0; i < location.getLocationTree().size(); i++) {
            APP.image(tree, location.getLocationTree().get(i), 640 - location.getHeightTerrain()[location.getLocationTree().get(i) + 16] - 32, 32, 32);
        }
    }

    /**
     * Draws the HUD for the game such as the power of wind, power of player, player turn, score of players, health of player, number of
     * parachute
     * @param currentPlayer The player whose turn it is currently.
     * @param graphics A map of string keys to PImage values
     * @param copyPlayers The list of all players in the game, used to display scores.
     * @param wind The current wind speed
     */
    public void drawHUD(Player currentPlayer, Map<String, PImage> graphics, ArrayList<Player> copyPlayers, float wind) {
        APP.textSize(14);
        APP.fill(0);
        APP.textAlign(App.LEFT - 4, App.BOTTOM + 5);
        APP.text("Player " + currentPlayer.getName() + "'s turn", 25, 25);
        PImage fuelImage = graphics.get("fuel");
        APP.image(fuelImage, 140, 8, (float) APP.width /App.CELLSIZE, (float) APP.height /App.CELLSIZE + 5);
        int fuelTextX = 140 + fuelImage.width/App.CELLSIZE + 20;
        int fuelTextY = 8 + fuelImage.height/App.CELLSIZE + 10;
        APP.text(currentPlayer.getFuel(), fuelTextX, fuelTextY);

        // Display the parachute status.
        PImage parachute = graphics.get("parachute");
        APP.image(parachute, 140, fuelTextY - 10 + 20, (float) APP.width /App.CELLSIZE, (float) APP.height /App.CELLSIZE);
        APP.text(currentPlayer.getParachute(), fuelTextX, fuelTextY + 25);

        // Display the ultimate readiness status.
        ULTIMATE_ANIMATION.draw(APP);
        String ultiStatus = currentPlayer.getIsUlti() ? "IS READY NOW" : "NOT READY (X to start)";
        APP.text(ultiStatus, 180, 80);

        APP.stroke(0);
        APP.fill(0, 0,0);
        APP.stroke(0);
        APP.text("Health:", 390, 30);
        APP.text(currentPlayer.getHealth(), 600, 30);
        //power letter
        APP.fill(0,0,0);
        APP.text("Power:", 390, 60);
        APP.text((int)currentPlayer.getPower(), 440, 60);
        

        //white box
        APP.fill(255, 255, 255);
        APP.rect(440, 15, 150, 20);
        //health box
        String[] color = currentPlayer.getColor().split(",");
        APP.fill(Integer.parseInt(color[0]), Integer.parseInt(color[1]), Integer.parseInt(color[2]));
        APP.rect(440, 15, (150*((float)currentPlayer.getHealth()/100)), 20);
        // rect(200, 15, (150*((float)currentPlayer.getHealth()/100)), 20);

        //power
        APP.stroke(127);
        APP.rect(440, 15, (int)(150*(currentPlayer.getPower()/100)), 20);
        APP.stroke(255, 0, 0);
        APP.strokeWeight(1);
        APP.line(440 + (int)(150*(currentPlayer.getPower()/100)), 10, 440 + (int)(150*(currentPlayer.getPower()/100)), 40);
        PImage winds;
        if (wind >= 0) {
            winds = graphics.get("wind");
        }

        else {
            winds = graphics.get("wind_left");
        }
        APP.image(winds, 780, 8, ((float) APP.width /20), ((float) APP.height /20));

        APP.fill(0,0,0);
        APP.text((int)(Math.abs(wind)), 835, 30);
        //Score board
        APP.noFill();
        APP.strokeWeight(5);
        APP.stroke(0,0,0);
        APP.rect(720, 50, 135, 24);
        APP.fill(0, 0, 0); 
        APP.textSize(14);
        APP.text("Scores", 730, 70);

        APP.noFill();
        APP.strokeWeight(5);
        APP.stroke(0,0,0);
        APP.rect(720, 50 + 24, 135, copyPlayers.size() * 24);
        int count = 0;
        for (Player i : copyPlayers) {
            String[] cols = i.getColor().split(",");
            APP.fill(Integer.parseInt(cols[0]), Integer.parseInt(cols[1]), Integer.parseInt(cols[2])); 
            APP.textSize(14);
            APP.text("Player " + i.getName(), 730, 95 + 22 *(count));
            APP.fill(0,0,0);
            APP.text(i.getScore(), 730 + 90, 95 + 22 *(count));
            count += 1;
        }
    }

    /**
     * Draws the final score screen, sorting players by score and highlighting the winner. this one is dynamic in size
     * depending on the number of players
     * @param copyPlayers The list of players to be sorted and displayed.
     */
    public void drawFinalScore(ArrayList<Player> copyPlayers) {
        Collections.sort(copyPlayers);

        String[] col = copyPlayers.get(0).getColor().split(",");
        int r = Integer.parseInt(col[0]);
        int g = Integer.parseInt(col[1]);
        int b = Integer.parseInt(col[2]);

        // Draw the main scoreboard rectangle.
        APP.fill(r, g, b, 80);
        APP.strokeWeight(6);
        APP.stroke(0, 0, 0);
        APP.rect((float) App.WIDTH / 2 - (float) 430 / 2, (float) App.HEIGHT / 2 - (float) (copyPlayers.size() * 45) / 2, 430, copyPlayers.size() * 45);

        // Draw the header box above the main scoreboard.
        APP.fill(r, g, b, 80); // Set the fill color with some transparency.
        APP.rect((float) App.WIDTH / 2 - (float) 430 / 2, (float) App.HEIGHT / 2 - (float) (copyPlayers.size() * 45) / 2 - 50, 430, 50);

        // Display the winning player's name.
        APP.textSize(20); // Set the text size for the player name.
        APP.fill(Integer.parseInt(col[0]), Integer.parseInt(col[1]), Integer.parseInt(col[2])); // Set the text color to the top player's color.
        APP.text("Player " + copyPlayers.get(0).getName() + " wins!", (float) App.WIDTH / 2 - (float) 430 / 2, (float) App.HEIGHT / 2 - (float) (copyPlayers.size() * 45) / 2 - 80);

        // Set up for displaying the "Final Scores" text.
        APP.fill(0, 0, 0); // Set the text color to black.
        APP.text("Final Scores", (float) App.WIDTH / 2 - (float) 430 / 2 + 10, (float) App.HEIGHT / 2 - (float) (copyPlayers.size() * 45) / 2 - 10);
        APP.noFill(); // Disable fill to end drawing.     
    }
    /**
     * Draws the scores of players up to a specified index on the game canvas. this one will make animation
     * for display each players in 0.7 sec
     * @param copyPlayers The list of players whose scores are to be displayed.
     * @param index The param will show the score of the player from 1 to that index
     */
    public void drawPlayerScore(int index, ArrayList<Player> copyPlayers) {
        for (int i = 0; i <= index; i++) {
            String[] col = copyPlayers.get(i).getColor().split(",");
            int r = Integer.parseInt(col[0]);
            int g = Integer.parseInt(col[1]);
            int b = Integer.parseInt(col[2]);
            APP.fill(r, g, b);
            APP.text("Player " + copyPlayers.get(i).getName(), 
                    (float) App.WIDTH /2 - (float) 430 /2 + 10,
                    (float) App.HEIGHT /2 - (float) (copyPlayers.size() * 45) / 2 + i * 40 + 25);
            APP.text(copyPlayers.get(i).getScore(), 
                    (float) App.WIDTH /2 - (float) 430 /2 + 310,
                    (float) App.HEIGHT /2 - (float) (copyPlayers.size() * 45) / 2 + i * 40 + 25);
        }   
    }
}
