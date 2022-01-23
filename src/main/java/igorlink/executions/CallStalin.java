package igorlink.executions;

import igorlink.donationexecutor.DonationExecutor;
import org.bukkit.entity.Player;

import static igorlink.service.Utils.announce;

public class CallStalin implements IExecution{
    @Override
    public void execute(Player player, String donationUsername) {
        announce(donationUsername, "призвал Сталина разобраться с тобой!", "призвал Сталина разобраться с", player, true);
        DonationExecutor.giantMobManager.addMob(player.getLocation(), "§cИосиф Сталин");

    }
}
