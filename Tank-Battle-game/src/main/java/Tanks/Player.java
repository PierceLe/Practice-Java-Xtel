package Tanks;

import java.util.ArrayList;

/**
 * Represents a player in the tank game. This class extends {@link Coordinate}
 * to manage the player's position and adds game-specific properties and behaviors
 * such as health, fuel, score, and abilities like parachutes and ultimate powers.
 */
public class Player extends Coordinate implements Comparable<Player> {
    private String color;
    private String name;
    private float rotationAngle;
    private int fuel;
    private int health;
    private double powers;
    private int parachute;
    private int score;
    private boolean isAlive;
    private boolean isFall;
    private int timePointer;
    private boolean isUlti;


    public Player() {
       this.rotationAngle = -(float) Math.PI / 2;
       this.fuel = 250;
       this.health = 100;
       this.powers = (double) health / 2;
       this.parachute = App.INITIAL_PARACHUTE;
       this.score = 0;
       this.isAlive = true;
       this.isFall = false;
       this.timePointer = 0;
       this.isUlti = false;
    }

    /**
     * Revives and resets all players to their initial state.
     * This static method iterates through a list of players and resets each player's health, fuel,
     * powers, and other status indicators to their default values. It is typically used to prepare
     * players for a new game or level by setting all attributes to their starting conditions.
     * <p>
     * This method will support when we reset game
     * @param players An ArrayList of {@link Player} objects that will be revived and reset.
     */
    public static void revivalPlayers(ArrayList<Player> players) {
        for (Player player : players) {
            player.health = 100;
            player.isAlive = true;
            player.rotationAngle = -(float) Math.PI / 2;
            player.fuel = 250;
            player.powers = (double) player.health / 2;
            player.parachute = App.INITIAL_PARACHUTE;
            player.score = 0;
            player.timePointer = 0;
            player.isFall = false;
            player.isUlti = false;
        }
    }

    /**
     * Prepares all players for the next level by resetting health and other attributes.
     * This static method is used when transitioning to a new level in the game. It resets
     * each player's health, rotation angle, fuel, powers, and several status flags, but does
     * not reset scores, which typically persist across levels.
     * @param players An ArrayList of {@link Player} objects that are to be prepared for the next level.
     */
    public static void nextLevel(ArrayList<Player> players) {
        for (Player player : players) {
            player.health = 100;
            player.isAlive = true;
            player.rotationAngle = -(float) Math.PI / 2;
            player.fuel = 250;
            player.powers = (double) player.health / 2;
            player.isUlti = false;
            player.timePointer = 0;
            player.isFall = false;
        }
    }

    /**
     * Retrieves the current score of the player.
     * This method returns the player's score, which represents the numerical
     * value accumulated by the player's performance in the game. Scores are typically
     * used to track progress, determine rewards, and compare player achievements.
     *
     * @return The current score of the player as an integer.
     */
    public int getScore() {
        return this.score;
    }

    /**
     * Retrieves the current number of parachutes available to the player.
     * This method returns the number of parachutes, which are used in the game to
     * mitigate fall damage or perform specific maneuvers. Managing parachutes is crucial
     * for strategies involving height or fall-related game mechanics.
     *
     * @return The number of parachutes the player currently has as an integer.
     */

    public int getParachute() {
        return this.parachute;
    }

    /**
     * Sets the number of parachutes available to the player.
     * This method updates the player's parachute count to the specified number. It is important
     * to manage the parachute count accurately as it affects the player's ability to mitigate
     * fall impacts or execute specific maneuvers in the game. Ensure the input value (`num`)
     * adheres to game rules and constraints regarding parachute limits.
     *
     * @param num The new number of parachutes the player should have. This value should be
     *            non-negative and within any game-specific limits.
     */

    public void setnumParachute(int num) {
        this.parachute = num;
    }

    /**
     * Updates the player's score by the specified amount.
     * Positive values increase the score, while negative values decrease it.
     *
     * @param score The amount to add to the current score.
     */

    public void updateScore(int score) {
        this.score += score;
    }

    /**
     * Sets the time pointer for the player. This could represent a cooldown timer,
     * time until a special ability is ready, or any other time-related mechanic.
     *
     * @param timePointer The new value of the time pointer.
     */
    public void setTimePointer(int timePointer) {
        this.timePointer = timePointer;
    }

    /**
     * Retrieves the player's name.
     *
     * @return The current name of the player.
     */

    public String getName() {
        return this.name;
    }

    /**
     * Sets the player's name.
     *
     * @param name The new name for the player.
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Retrieves the current fuel level of the player.
     *
     * @return The amount of fuel the player currently has.
     */

    public int getFuel() {
        return this.fuel;
    }


    /**
     * Updates the player's fuel level by subtracting the specified amount.
     * Fuel cannot drop below 0.
     *
     * @param fuel The amount of fuel to use.
     */

    public void setFuel(int fuel) {
        int newFuel = this.fuel - fuel;
        if (newFuel <= 0) {
            newFuel = 0;
        }
        this.fuel = newFuel;
    }


    /**
     * Retrieves the current power level of the player.
     *
     * @return The current power of the player.
     */

    public double getPower() {
        return this.powers;
    }


    /**
     * Sets the player's power level, ensuring it does not exceed the player's health
     * and does not fall below zero.
     *
     * @param currentPower The new power level for the player.
     */
    public void setPower(double currentPower) {
        if (currentPower >= this.health) {
            currentPower = this.health;
        } else if (currentPower <= 0) {
            currentPower = 0;
        }
        this.powers = currentPower;
    }

    /**
     * Retrieves the current health of the player.
     *
     * @return The current health of the player.
     */

    public int getHealth() {
        return this.health;
    }


    /**
     * Adjusts the player's health by subtracting the specified amount.
     * Health is constrained between 0 and 100.
     *
     * @param health The amount of damage to apply or health to restore.
     */

    public void setHealth(int health) {
        int newHealth = this.health - health;
        if (newHealth <= 0) {
            newHealth = 0;
        }
        if (newHealth >= 100) {
            newHealth = 100;
        }
        this.health = newHealth;
    }


    /**
     * Retrieves the current rotation angle of the player.
     *
     * @return The current rotation angle in radians.
     */

    public float getRotationAngle() {
        return this.rotationAngle;
    }



    /**
     * Adjusts the player's rotation angle by adding the specified angle.
     * The rotation angle is constrained between -π and 0 radians.
     *
     * @param angle The angle in radians to add to the current rotation.
     */

    public void setRotationAngle(float angle) {
        float newAngle = rotationAngle + angle;
        if (newAngle > 0) {
            newAngle = 0;
        } else if (newAngle < -(float) Math.PI) {
            newAngle = -(float) Math.PI;
        }
        this.rotationAngle = newAngle;
    }


    /**
     * Retrieves the player's color.
     *
     * @return The current color of the player.
     */

    public String getColor() {
        return color;
    }



    /**
     * Sets the player's color.
     *
     * @param color The new color for the player.
     */
    public void setColor(String color) {
        this.color = color;
    }


    /**
     * Retrieves the fall status of the player.
     *
     * @return true if the player is falling, false otherwise.
     */
    public boolean getFall() {
        return this.isFall;
    }


    /**
     * Sets the fall status of the player.
     *
     * @param fall true to indicate the player is falling, false otherwise.
     */

    public void setFall(boolean fall) {
        this.isFall = fall;
    }

    /**
     * Sets the status of the player's ultimate ability.
     *
     * @param ulti true to indicate the ultimate ability is activated, false otherwise.
     */
    public void setUlti(boolean ulti) {
        this.isUlti = ulti;
    }


    /**
     * Retrieves the status of the player's ultimate ability.
     * This method checks if the ultimate ability of the player is active.
     * An active ultimate ability might provide significant advantages in gameplay.
     *
     * @return true if the ultimate ability is active; false if it is not.
     */
    public boolean getIsUlti() {
        return this.isUlti;
    }


    /**
     * Sets the alive status of the player.
     * This method updates whether the player is considered alive in the game.
     * Setting this to false typically means the player has been eliminated from gameplay.
     *
     * @param alive true if the player is alive; false otherwise.
     */

    public void setAlive(boolean alive) {
        this.isAlive = alive;
    }


    /**
     * Retrieves the alive status of the player.
     * This method returns whether the player is currently alive. Being alive usually means
     * the player can still participate and perform actions within the game.
     *
     * @return true if the player is currently alive; false if they are not.
     */
    public boolean getIsAlive() {
        return this.isAlive;
    }


    /**
     * Compares this player with another player primarily based on scores, and secondarily on names.
     * This method is used for sorting or comparing players, for example in leaderboards or ranking systems.
     * A negative value indicates this player is ranked lower than the other player, zero indicates equal ranking,
     * and a positive value indicates a higher ranking.
     *
     * @param otherPlayer The other player to compare against.
     * @return Negative if this player's score is less, zero if equal, positive if greater.
     *         If scores are equal, comparison is based on the lexicographical order of names.
     */
    @Override
    public int compareTo(Player otherPlayer) {
        int comparingScore = Integer.compare(this.score, otherPlayer.score) * -1;
        if (comparingScore != 0) {
            return comparingScore;
        }
        return this.name.compareTo(otherPlayer.name);
    }


    /**
     * Buys additional health for the player using their score points.
     * Health is increased by 20 unless doing so would exceed the maximum health limit (100),
     * in which case it is topped up to the maximum. Costs 20 score points.
     */

    public void buyingHealth() {
        if (this.score >= 20 && this.health < 100) {
            if (this.health <= 80) {
                this.health += 20;
            }
            else {
                this.health += 100 - this.health;
            }
        this.score -= 20;
        }
        
    }


    /**
     * Buys additional fuel for the player using their score points.
     * Fuel is increased by 200 units. Costs 10 score points.
     */

    public void buyingFuel() {
        if (this.score >= 10) {
            this.fuel += 200;
            this.score -= 10;
        }
    }


    /**
     * Buys an additional parachute for the player using their score points.
     * Increases the count of parachutes by one. Costs 15 score points.
     */

    public void buyingParachute() {
        if (this.score >= 15) {
            this.parachute += 1;
            this.score -= 15;
        }
    }


    /**
     * Handles the falling mechanics for the player when they are airborne.
     * Depending on whether the player has a parachute, the fall rate and effects differ.
     * If the player lands on the ground (terrain height), falling is stopped.
     *
     * @param heightTerrain Array representing the height of terrain at various positions.
     */

    public void Fall(float[] heightTerrain) {
        if (this.parachute > 0) {
            if (this.col <= 640 - heightTerrain[this.row]) {
                this.col = this.col + 2;
                return;
            }
            this.parachute -= 1;
        } else {
            if (this.col <= 640 - heightTerrain[this.row]) {
                this.col = this.col + 4;
                return;
            }
        }
        this.isFall = false;
    }


    /**
     * Draws a pointer above the player to indicate their position on the screen.
     * The pointer is drawn for a limited time or until a certain condition is met.
     *
     * @param app The application context used for drawing.
     */
    public void drawPointer(App app) {
        if (timePointer < 60) {
            app.stroke(0);
            app.line(this.row, this.col - 140, this.row, this.col - 90);
            app.line(this.row, this.col - 90, this.row - 10, this.col - 100);
            app.line(this.row, this.col - 90, this.row + 10, this.col - 100);
            timePointer++;

        }
    }

    /**
     * Draws the player on the game canvas, including the player's tank barrel and body.
     * The player's color and orientation are taken into account.
     *
     * @param app The application context used for drawing graphical elements.
     */
    public void draw(App app) {
        app.fill(0, 0, 0);
        app.strokeWeight(4);
        app.stroke(0, 0, 0);
        app.line((float) this.getRow(), this.getCol() - 4, (float) (this.getRow() + 19 * Math.cos(this.getRotationAngle())), (float) (this.getCol() - 4 + 19 * Math.sin(this.getRotationAngle())));
        String[] color = this.getColor().split(",");
        int index_x_1 = this.getRow() - 9;
        int index_x_2 = this.getRow() - 7;
        double index_y_1 = this.getCol() - 4;
        double index_y_2 = this.getCol() - 8;
        app.noStroke();
        app.fill(Integer.parseInt(color[0]), Integer.parseInt(color[1]), Integer.parseInt(color[2]));
        app.rect(index_x_1, (int) index_y_1, 18, 4);
        app.noStroke();
        app.fill(Integer.parseInt(color[0]), Integer.parseInt(color[1]), Integer.parseInt(color[2]));
        app.rect(index_x_2, (int) index_y_2, 14, 4);
    }

    
}