package igorlink.executions;

import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;

import static igorlink.service.Utils.announce;

public class SpawnCreeper implements IExecution{
    @Override
    public void execute(Player player, String donationUsername) {
        //Spawn Creepers
        Vector direction = player.getLocation().getDirection();
        announce(donationUsername, "прислал тебе в подарок крипера...", "прислал крипера в подарок", player, true);
        direction.setY(0);
        direction.normalize();
        player.getWorld().spawnEntity(player.getLocation().clone().subtract(direction.multiply(1)), EntityType.CREEPER);

    }
}
