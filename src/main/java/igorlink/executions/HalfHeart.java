package igorlink.executions;

import org.bukkit.entity.Player;

import static igorlink.service.Utils.announce;

public class HalfHeart implements IExecution{
    @Override
    public void execute(Player player, String donationUsername) {
        player.setHealth(1);
        announce(donationUsername, "оставил тебе лишь полсердечка...", "оставил лишь полсердечка", player, true);
    }
}
