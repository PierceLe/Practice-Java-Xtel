package Tanks;


import java.util.ArrayList;

/**
 * Represents a bullet in the game, which can affect both terrain(destroy terrain) and players(score and health).
 * This class manages the bullet's physics, including its motion and interaction with game elements.
 */
public class Bullet extends Coordinate {
    protected static final int LOCATION_TURRET = 15;
protected static final double G = 0.12;
    protected final int DIAMTER;
    private final Player TANK;
    private float velo_x;
    private float velo_y;

    /**
     * Constructor to initialize a Bullet object.
     * 
     * @param tank The player's tank from which the bullet is fired.
     * @param diameter The diameter of the bullet when explode, affecting the size of impact such as terrain and dame.
     */
    public Bullet(Player tank, int diameter) {
        super(tank.getRow(), tank.getCol() - LOCATION_TURRET);
        this.TANK = tank;
        float velocity = (float) (1 + 0.08 * (tank.getPower()));
        //create method
        this.velo_x = velocity * (float) (Math.cos(tank.getRotationAngle()));
        this.velo_y = velocity * (float) (Math.sin(tank.getRotationAngle()));
        this.DIAMTER = diameter;

    }

    /**
     * Gets the diameter of the bullet when explode.
     * 
     * @return The diameter of the bullet when explode.
     */
    public int getDIAMTER() {
        return this.DIAMTER;
    }

    /**
     * Updates the location of the bullet based on its velocity components.
     * The bullet's position will change depending on horizontal velocity and vertical velocity.
     */
    public void updateLocation() {
        this.row = (int) (this.row + this.velo_x);
        this.col = (this.col + this.velo_y);
    }

    /**
     * Simulates the explosion of the bullet, affecting terrain and players within its in radius.
     *
     * @param playersMap    An ArrayList containing all players in the game.
     * @param heightTerrain An array of heights representing the true height of the terrain at various points.
     */
    public void explode(ArrayList<Player> playersMap, float[] heightTerrain) {
        if (collision(heightTerrain)) {
            double row_low = this.row - (double) this.DIAMTER / 2;
            double row_high = this.row + (double) this.DIAMTER / 2;
            row_low = Math.max(0, row_low);
            row_high = Math.min(row_high, 864);
            for (int i = (int) row_low; i <= (int) row_high; i++) {
                double dest = getDest(heightTerrain, i);
                heightTerrain[i] -= (float) dest;
            }
            for (Player i : playersMap) {
                if (i.getCol() < (App.HEIGHT - heightTerrain[i.getRow()]) && i.getIsAlive()) {
                    i.setFall(true);
                    if (i.getParachute() <= 0) {

                        int losingHealth = (int) (Math.abs(i.getCol() - (App.HEIGHT - heightTerrain[i.getRow()])));
                        if (losingHealth > i.getHealth()) {
                            losingHealth = i.getHealth();
                        }
                        i.setHealth(losingHealth);
                        if (i.getPower() >= i.getHealth()) {
                            i.setPower(i.getHealth());
                        }
                        if (this.TANK != i) {
                            this.TANK.updateScore(losingHealth);
                        }
                    }
                }
            }
        }
    }

    /**
     * this one will check the difference between and after the terrain being impacted by projectile
     * @param heightTerrain An array of float values representing the height of terrain.
     * @param i i is the point in diameter of a circle of explosion
     * @return The calculated delta, which is the change of terrain height at location i
     */
    private double getDest(float[] heightTerrain, int i) {
        double y_high = -(Math.sqrt(Math.pow((double) this.DIAMTER / 2, 2) - Math.pow((this.row - i), 2))) + this.col;
        double y_low = Math.sqrt(Math.pow((double) this.DIAMTER / 2, 2) - Math.pow((this.row - i), 2)) + this.col;
        double t_high = 640 - heightTerrain[i];
        double dest = 0;
        if (y_high < t_high && y_low >= t_high) {
            dest = (y_low - t_high);
        } else if (y_low > t_high){
            dest = (y_low - y_high);
        }
        return dest;
    }

    /**
     * Updates the bullet's velocity depending wind and G.
     * 
     * @param wind The current wind speed affecting horizontal motion.
     */
    public void updateVelocity(float wind) {
        this.velo_x += (float) ((wind * 0.03) / 30);
        this.velo_y += (float) G;
    }

    /**
     * Find the owner of the projectile.
     * 
     * @return The player's tank associated with this bullet.
     */
    public Player getPlayer() {
        return this.TANK;
    }

    /**
     * Checks for collision between terrain and bullets.
     * if false we continue draw bullets, else we will destroy the terrain using explode method
     * @param heightTerrain Array representing the height of the terrain.
     * @return true if there is a collision, false otherwise.
     */
    public boolean collision(float[] heightTerrain) {
        // System.out.println(this.col);
        return (App.HEIGHT - heightTerrain[this.row] <= this.col);
    }

    /**
     * Checks if the bullet has hit any player within its hitbox range and applies damage accordingly.
     * If a player's health drops to or below zero as a result of the hit, they are marked as not alive.
     *
     * @param playerMap An ArrayList containing all players in the game.
     */
    public void checkHitBox(ArrayList<Player> playerMap) {
        for (Player player : playerMap) {
            int x_tank = player.getRow();
            double y_tank = player.getCol();
            double distance = Math.sqrt(Math.pow((x_tank - this.row), 2) + Math.pow((y_tank - this.col), 2));
            int health;
            if (distance <= (double) this.DIAMTER / 2) {
                health = (int) (-2 * distance + 60);
                if (player.getHealth() <= health) {
                    health = player.getHealth();
                }
                player.setHealth(health);
                if (player.getPower() >= player.getHealth()) {
                    player.setPower(player.getHealth());
                }
                if (this.getPlayer() != player) {
                    this.getPlayer().updateScore(health);
                }
            }
            if (player.getHealth() <= 0) {
                player.setAlive(false);
            }
        }
    }

    /**
     * Draws the bullet on the screen.
     *
     * @param app The App object used for rendering graphics.
     */
    public void draw(App app) {
        String[] color = TANK.getColor().split(",");
        app.fill(0, 0, 0);
        app.stroke(Integer.parseInt(color[0]), Integer.parseInt(color[1]), Integer.parseInt(color[2]));
        app.ellipse((float) this.row, this.col, 6, 6);
        app.noStroke();
    }
}
