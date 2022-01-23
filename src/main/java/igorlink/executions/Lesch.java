package igorlink.executions;

import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;

import static igorlink.service.Utils.announce;

public class Lesch implements IExecution{
    @Override
    public void execute(Player player, String donationUsername) {
        announce(donationUsername, "дал тебе леща", "дал леща", player, true);
        Vector direction = player.getLocation().getDirection();
        direction.setY(0);
        direction.normalize();
        direction.setY(0.3);
        player.setVelocity(direction.multiply(0.8));
        if (player.getHealth() > 2.0D) {
            player.setHealth(player.getHealth() - 2);
        } else {
            player.setHealth(0);
        }
        player.playSound(player.getLocation(), Sound.ENTITY_PLAYER_HURT, 1, 1);

    }
}
