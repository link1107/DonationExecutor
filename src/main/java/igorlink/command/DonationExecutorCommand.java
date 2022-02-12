package igorlink.command;

import igorlink.service.Utils;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import static igorlink.service.Utils.logToConsole;

public class DonationExecutorCommand extends AbstractCommand {
    public DonationExecutorCommand() {
        super("donationexecutor");
    }

    @Override
    public boolean execute(@NotNull CommandSender sender, @NotNull String label, @NotNull String @NotNull [] args) {
        String[] newArgs;

        if (args.length == 0) {
            return false;
        }

        try {

            //Если команда - это reload, где не должно быть доп аргументов, то вызываем функцию релоуда конфига
            switch (args[0]) {
                case "reload" -> {
                    if ((sender != Bukkit.getConsoleSender()) && (!sender.hasPermission("de.reload")) && (!sender.isOp())) {
                        Utils.sendSysMsgToPlayer((Player) sender, "У вас недостаточно прав для выполнения данной\nкоманды!");
                        return true;
                    }
                    if (args.length == 1) {
                        ReloadSubCommand.onReloadCommand(sender);
                        return true;
                    }
                }
                case "donate" -> {
                    //Инициализируем список аргментов для новой сабфункции
                    //Если команда - donate, где нужен минимум 1 доп аргумент, создаем новый массив аргументов со смещением 1, и вызываем функцию обработки доната
                    if ((sender != Bukkit.getConsoleSender()) && (!sender.hasPermission("de.donate")) && (!sender.isOp())) {
                        Utils.sendSysMsgToPlayer((Player) sender, "У вас недостаточно прав для выполнения данной\nкоманды!");
                        return true;
                    }
                    if (args.length >= 2) {
                        //Инициализируем список новых аргументов для субкоманды
                        newArgs = new String[args.length - 1];
                        //Создаем новый список аргументов, копируя старый со смещением 1
                        System.arraycopy(args, 1, newArgs, 0, args.length - 1);
                        //Вызываем обработку доната
                        DonateSubCommand.onDonateCommand(sender, newArgs);
                        //Возвращаем true, к все прошло успешно
                        return true;
                    }
                }
                case "filter" -> {
                    if ((sender != Bukkit.getConsoleSender()) && (!sender.hasPermission("de.filter")) && (!sender.isOp())) {
                        Utils.sendSysMsgToPlayer((Player) sender, "У вас недостаточно прав для выполнения данной\nкоманды!");
                        return true;
                    }
                    if ((args.length == 2) && ((args[1].equals("on")) || (args[1].equals("off")))) {
                        //Инициализируем список новых аргументов для субкоманды
                        newArgs = new String[args.length - 1];
                        //Создаем новый список аргументов, копируя старый со смещением 1
                        System.arraycopy(args, 1, newArgs, 0, args.length - 1);
                        //Вызываем обработку доната
                        FilterSubCommand.onFilterCommand(sender, newArgs);
                        //Возвращаем true, к все прошло успешно
                        return true;
                    }
                }
                case "sum" -> {
                    logToConsole("Суммарно: §b" + Utils.getSum() + "§f руб.");
                    return true;
                }

            }

        } catch (Exception e) {

            //Если получили exception, сообщаем о нем и выдаем сообщение об ошибке в консоль
            e.printStackTrace();
            logToConsole("Произошла неизвестная ошибка при выполнении команды!");
            return false;

        }

        //Если ничего не выполнилось - в команде была ошибка
        return false;
    }

}
