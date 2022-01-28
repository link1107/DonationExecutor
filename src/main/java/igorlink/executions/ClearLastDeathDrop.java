package igorlink.executions;

import igorlink.donationexecutor.DonationExecutor;
import org.bukkit.entity.Player;

import static igorlink.service.Utils.announce;

public class ClearLastDeathDrop implements IExecution{
    @Override
    public void execute(Player player, String donationUsername) {
        //Remove Last Death Dropped Items
        if (DonationExecutor.getInstance().listOfStreamerPlayers.getStreamerPlayer(player.getName()).removeDeathDrop()) {
            announce(donationUsername, "уничтожил твой посмертный дроп...", "уничтожил посмертный дроп", player, true);
        } else {
            announce(donationUsername, "безуспешно пытался уничтожить твой посмертный дроп...", "безуспешно пытался уничтожить посмертный дроп", player, true);
        }

    }
}
