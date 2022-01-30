package igorlink.command;

import org.bukkit.command.CommandSender;
import static igorlink.service.Utils.logToConsole;

public class DonationExecutorCommand extends AbstractCommand {

    public DonationExecutorCommand() {
        super("donationexecutor");
    }

    @Override
    public Boolean execute(CommandSender sender, String label, String[] args) {
        String[] newArgs;

        if (args.length == 0) {
            return false;
        }

        try {

            //Если команда - это reload, где не должно быть доп аргументов, то вызываем функцию релоуда конфига
            if (args[0].equals("reload")) {
                if (args.length == 1) {
                    ReloadSubCommand.onReloadCommand(sender);
                    return true;
                }
            } else if (args[0].equals("donate")) {
                //Инициализируем список аргментов для новой сабфункции
                //Если команда - donate, где нужен минимум 1 доп аргумент, создаем новый массив аргументов со смещением 1, и вызываем функцию обработки доната
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
            } else if (args[0].equals("filter")) {
                if ((args.length == 2) && (args[1].equals("on")) || (args[1].equals("off"))) {
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
