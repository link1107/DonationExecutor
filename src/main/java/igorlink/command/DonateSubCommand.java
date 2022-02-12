package igorlink.command;

import igorlink.donationexecutor.DonationExecutor;
import igorlink.donationexecutor.playersmanagement.Donation;
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
            if (i != 1) {
                donationUsername.append(' ');
            }
            donationUsername.append(args[i]);
        }


        //Отправляем донат на исполнение
        DonationExecutor.getInstance().streamerPlayersManager.addToDonationsQueue(new Donation(sender, donationUsername.toString(), donationAmount+".00"));
    }
}
