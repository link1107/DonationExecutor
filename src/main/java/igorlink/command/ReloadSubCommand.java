package igorlink.command;

import igorlink.donationexecutor.DonationExecutor;
import igorlink.service.MainConfig;
import igorlink.service.Utils;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class ReloadSubCommand {
    public static void onReloadCommand(CommandSender sender) throws InterruptedException {
        MainConfig.loadMainConfig(true);
        DonationExecutor.getInstance().streamerPlayersManager.reload();
        Utils.logToConsole("Настройки успешно обновлены!");
        if (sender instanceof Player player) {
            Utils.sendSysMsgToPlayer(player.getPlayer(), "Настройки успешно обновлены!");
        }
    }
}
