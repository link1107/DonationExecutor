package igorlink.command;

import igorlink.service.MainConfig;
import igorlink.service.Utils;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class FilterSubCommand {
    public static void onFilterCommand(@NotNull CommandSender sender, @NotNull String @NotNull [] args) {
        if (args[0].equalsIgnoreCase("on")) {
            MainConfig.turnFilterOn();
            Utils.logToConsole("Фильтр никнеймов донатеров §bВКЛЮЧЕН");
            if (sender instanceof Player) {
                Utils.sendSysMsgToPlayer((Player) sender, "Фильтр никнеймов донатеров §bВКЛЮЧЕН");
            }
        } else {
            MainConfig.turnFilterOff();
            Utils.logToConsole("Фильтр никнеймов донатеров §bВЫКЛЮЧЕН");
            if (sender instanceof Player) {
                Utils.sendSysMsgToPlayer((Player) sender,"Фильтр никнеймов донатеров §bВЫКЛЮЧЕН");
            }
        }
    }
}
