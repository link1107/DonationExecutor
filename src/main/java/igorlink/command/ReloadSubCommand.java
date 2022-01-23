package igorlink.command;

import igorlink.service.MainConfig;
import igorlink.service.Utils;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.Objects;

public class ReloadSubCommand {
    public static void onReloadCommand(CommandSender sender){
        MainConfig.loadMainConfig(true);
        Utils.logToConsole("Настройки успешно обновлены!");
        if (sender instanceof Player) {
            Utils.sendSysMsgToPlayer(Objects.requireNonNull(((Player) sender).getPlayer()), "Настройки успешно обновлены!");
        }
    }
}
