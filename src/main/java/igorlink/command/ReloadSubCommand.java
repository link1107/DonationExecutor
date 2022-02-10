package igorlink.command;

import igorlink.donationexecutor.DonationExecutor;
import igorlink.service.MainConfig;
import igorlink.service.Utils;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.Objects;

public class ReloadSubCommand {
    public static void onReloadCommand(CommandSender sender) throws InterruptedException {
        MainConfig.loadMainConfig(true);
        DonationExecutor.getInstance().streamerPlayersManager.reload();
        Utils.logToConsole("Настройки успешно обновлены!");
        if (sender instanceof Player) {
            Utils.sendSysMsgToPlayer(Objects.requireNonNull(((Player) sender).getPlayer()), "Настройки успешно обновлены!");
        }
    }
}
