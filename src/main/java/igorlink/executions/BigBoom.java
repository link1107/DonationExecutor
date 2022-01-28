package igorlink.executions;

import igorlink.service.MainConfig;
import org.bukkit.entity.Player;

import static igorlink.service.Utils.announce;

public class BigBoom implements IExecution{
    @Override
    public void execute(Player player, String donationUsername) {
        announce(donationUsername, "сейчас тебя РАЗНЕСЕТ В КЛОЧЬЯ!!!", "сейчас РАЗНЕСЕТ В КЛОЧЬЯ", player, true);
        player.getWorld().createExplosion(player.getLocation(), MainConfig.bigBoomRadius, true);

    }
}
