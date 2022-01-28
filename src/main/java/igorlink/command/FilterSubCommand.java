package igorlink.command;

import igorlink.executions.CallNKVD;
import igorlink.service.MainConfig;
import igorlink.service.Utils;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.Locale;


public class FilterSubCommand {
    public static void onFilterCommand(CommandSender sender, String[] args) {
        if (args.length != 1) {
            sender.sendMessage("Допустимые аргументы: §aon§7/§coff");
            return;
        }

        switch (args[0].toLowerCase(Locale.ROOT)) {
            case "on":
                MainConfig.turnFilter(true);
                break;
            case "off":
                MainConfig.turnFilter(false);
                break;
            default:
                if (sender instanceof Player) {
                    new CallNKVD().execute(((Player) sender), "СЕРВЕР");
                    sender.sendMessage("§7Вы ввели недопустимые аргументы команды. За это вы будете наказаны §cсотрудником НКВД§7.");
                }
                sender.sendMessage("§eДопустимые аргументы команды: §aon§7/§coff");
                break;
        }

        Utils.logToConsole("Фильтр никнеймов донатеров" + (MainConfig.twitchFilter ? "§aВКЛЮЧЕН" : "§cВЫКЛЮЧЕН"));
        if (sender instanceof Player) {
            Utils.sendSysMsgToPlayer((Player) sender, "Фильтр никнеймов донатеров" + (MainConfig.twitchFilter ? "§aВКЛЮЧЕН" : "§cВЫКЛЮЧЕН"));
        }
    }
}
