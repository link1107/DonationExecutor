package igorlink.command;

import igorlink.service.MainConfig;
import igorlink.service.Utils;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class ReloadSubCommand {
    public static void onReloadCommand(CommandSender sender){
        MainConfig.loadMainConfig(true);
        Utils.logToConsole("Настройки успешно обновлены!");
        if (sender instanceof Player) {
            Utils.sendSysMsgToPlayer(((Player) sender).getPlayer(), "Настройки успешно обновлены!");
        }
    }
}
