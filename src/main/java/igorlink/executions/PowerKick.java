package igorlink.executions;

import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;

import static igorlink.service.Utils.announce;

public class PowerKick implements IExecution{
    @Override
    public void execute(Player player, String donationUsername) {
        announce(donationUsername, "дал тебе смачного пинка под зад", "дал смачного пинка под зад", player, true);
        Vector direction = player.getLocation().getDirection();
        direction.setY(0);
        direction.normalize();
        direction.setY(0.5);
        player.setVelocity(direction.multiply(1.66));
        if (player.getHealth() > 3.0D) {
            player.setHealth(player.getHealth() - 3);
        } else {
            player.setHealth(0);
        }
        player.playSound(player.getLocation(), Sound.ENTITY_PLAYER_HURT, 1, 1);

    }
}
