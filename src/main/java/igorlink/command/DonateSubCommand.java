package igorlink.command;

import igorlink.donationexecutor.DonationExecutor;
import igorlink.donationexecutor.executionsstaff.Donation;
import org.bukkit.command.CommandSender;

public class DonateSubCommand {
    public static void onDonateCommand(CommandSender sender, String[] args) {
        int i;

        //Getting donation's amount
        String donationAmount;
        StringBuilder donationUsername = new StringBuilder();
        StringBuilder donationMessage = new StringBuilder();

        //Getting donation's amount
        donationAmount = args[0];

        //Получаем имя донатера
        for (i = 1; i <= args.length - 1; i++) {
            if (args[i].equals("##")) {
                break;
            } else {
                if (i == 1) {
                    donationUsername.append(args[i]);
                } else {
                    donationUsername = new StringBuilder(' ' + donationUsername.toString() + args[i]);
                }
            }
        }

        //Все, что после символов ## - это сообщение
        for (i = i + 1; i <= args.length - 1; i++) {
            donationMessage.append(args[i]).append(' ');
        }

        //Отправляем Донат на исполнение
        DonationExecutor.getInstance().listOfStreamerPlayers.addToDonationsQueue(new Donation(sender, donationUsername.toString(), donationAmount + ".00", donationMessage.toString()));
    }
}
