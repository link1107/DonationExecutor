package igorlink.command;

import igorlink.donationexecutor.DonationExecutor;
import igorlink.donationexecutor.playersmanagement.Donation;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;

public class DonateSubCommand {
    public static void onDonateCommand(@NotNull CommandSender sender, @NotNull String @NotNull [] args) {
        int i;

        //Getting donation's amount
        String donationAmount;
        StringBuilder donationUsername = new StringBuilder();

        //Getting donation's amount
        donationAmount = args[0];

        //Получаем имя донатера
        for (i = 1; i <= args.length - 1; i++) {
            if (i == 1) {
                donationUsername.append(args[i]);
            } else {
                donationUsername.append(' ');
                donationUsername.append(args[i]);
            }
        }

        //Отправляем донат на исполнение
        DonationExecutor.getInstance().streamerPlayersManager.addToDonationsQueue(new Donation(sender, donationUsername.toString(), donationAmount+".00"));
    }
}
