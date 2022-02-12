package igorlink.command;

import igorlink.donationexecutor.DonationExecutor;
import igorlink.service.MainConfig;
import igorlink.service.Utils;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class ReloadSubCommand {
    public static void onReloadCommand(@NotNull CommandSender sender) {
        MainConfig.loadMainConfig(true);
        DonationExecutor.getInstance().streamerPlayersManager.reload();
        Utils.logToConsole("Настройки успешно обновлены!");
        if (sender instanceof Player player) {
            Utils.sendSysMsgToPlayer(player, "Настройки успешно обновлены!");
        }
    }
}
