package Tanks;

import java.util.ArrayList;

/**
 * Represents an Ultimate bullet, a specialized form of {@link Bullet} with enhanced capabilities.
 * This class is used to create ultimate bullets, which are typically more powerful or have special effects
 * compared to regular bullets. It inherits from the {@code Bullet} class and can be used in similar ways but
 * with additional impact or features in the game.
 */
public class Ultimate extends Bullet {


    public Ultimate(Player tank, int diameter) {
        super(tank, diameter);
    }


    /**
     * Checks if the bullet has hit any player within its hitbox range and applies damage accordingly.
     * If a player's health drops to or below zero as a result of the hit, they are marked as not alive.
     *
     * @param playerMap An ArrayList containing all players in the game.
     */
    public void checkHitBox(ArrayList<Player> playerMap) {
        for (int i = 0; i < playerMap.size(); i++) {
            int x_tank = playerMap.get(i).getRow();
            double y_tank = playerMap.get(i).getCol();
            double distance = Math.sqrt(Math.pow((x_tank - this.getRow()), 2) + Math.pow((y_tank - this.getCol()), 2));
            int health = 0;

            if (distance <= (double) this.DIAMTER / 2) {
                health = (int) (-1 * distance + 60);
                if (playerMap.get(i).getHealth() <= health) {
                    health = playerMap.get(i).getHealth();
                }
                playerMap.get(i).setHealth(health);
                if (playerMap.get(i).getPower() >= playerMap.get(i).getHealth()) {
                    playerMap.get(i).setPower(playerMap.get(i).getHealth());
                }

                if (this.getPlayer() != playerMap.get(i)) {
                    this.getPlayer().updateScore(health);
                }
            }
            if (playerMap.get(i).getHealth() <= 0) {
                playerMap.get(i).setAlive(false);
            }
        }
    }
}

