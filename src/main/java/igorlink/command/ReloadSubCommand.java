package igorlink.command;

import igorlink.service.MainConfig;
import igorlink.service.Utils;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.Objects;

import static org.dark0ghost.annotations.configs.Text.RU.ReloadSubCommandText.UPDATE_SETTINGS_TEXT;

public class ReloadSubCommand {
    public static void onReloadCommand(CommandSender sender){
        MainConfig.loadMainConfig(true);
        Utils.logToConsole(UPDATE_SETTINGS_TEXT);
        if (sender instanceof Player) {
            Utils.sendSysMsgToPlayer(Objects.requireNonNull(((Player) sender).getPlayer()), UPDATE_SETTINGS_TEXT);
        }
    }
}
