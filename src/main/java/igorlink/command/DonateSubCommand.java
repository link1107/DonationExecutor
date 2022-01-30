package igorlink.command;

import igorlink.donationexecutor.DonationExecutor;
import igorlink.donationexecutor.playersmanagement.Donation;
import org.bukkit.command.CommandSender;

public class DonateSubCommand {
    public static void onDonateCommand(CommandSender sender, String[] args) {
        int i;

        //Getting donation's amount
        String donationAmount = new String();
        String donationUsername = new String();
        String donationMessage = new String();

        //Getting donation's amount
        donationAmount = args[0];

        //Получаем имя донатера
        for (i = 1; i <= args.length - 1; i++) {
            if (args[i].equals("##")) {
                break;
            }
            else {
                if (i==1) {
                    donationUsername = donationUsername + args[i];
                }
                else {
                    donationUsername = ' ' + donationUsername + args[i];
                }
            }
        }

        //Все, что после символов ## - это сообщение
        for (i = i+1; i <= args.length - 1; i++)
        {
            donationMessage = donationMessage + args[i] + ' ';
        }

        //Отправляем донат на исполнение
        DonationExecutor.getInstance().streamerPlayersManager.addToDonationsQueue(new Donation(sender, donationUsername, donationAmount+".00", donationMessage));
    }
}
