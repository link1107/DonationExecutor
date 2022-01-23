package igorlink.command;

import igorlink.service.MainConfig;
import igorlink.service.Utils;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import static org.dark0ghost.annotations.configs.Text.RU.FilterSubCommandText.FILTER_OFF_TEXT;
import static org.dark0ghost.annotations.configs.Text.RU.FilterSubCommandText.FILTER_ON_TEXT;


public class FilterSubCommand {
    public static void onFilterCommand(CommandSender sender, String[] args) {
        if (args[0].equalsIgnoreCase("on")) {
            MainConfig.turnFilterOn();
            Utils.logToConsole(FILTER_ON_TEXT);
            if (sender instanceof Player) {
                Utils.sendSysMsgToPlayer((Player) sender, FILTER_ON_TEXT);
            }
        } else {
            MainConfig.turnFilterOff();
            Utils.logToConsole(FILTER_OFF_TEXT);
            if (sender instanceof Player) {
                Utils.sendSysMsgToPlayer((Player) sender, FILTER_OFF_TEXT);
            }
        }
    }
}
