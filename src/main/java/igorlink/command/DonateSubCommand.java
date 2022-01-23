package igorlink.command;

import igorlink.donationexecutor.DonationExecutor;
import igorlink.donationexecutor.executionsstaff.Donation;
import org.bukkit.command.CommandSender;

public class DonateSubCommand {
    public static void onDonateCommand(CommandSender sender, String[] args) {
        int i;

        //Getting donation's amount
        String donationAmount = args[0];
        StringBuilder donationUsername = new StringBuilder();
        StringBuilder donationMessage = new StringBuilder();

        //Получаем имя донатера
        for (i = 1; i <= args.length - 1; i++) {
            if (args[i].equals("##")) {
                break;
            }
            else {
                if (i==1) {
                    donationUsername.append(args[i]);
                }
                else {
                    donationUsername.append(' ').append(donationUsername).append(args[i]);
                }
            }
        }

        //Все, что после символов ## - это сообщение
        for (i = i+1; i <= args.length - 1; i++)
        {
            donationMessage.append(donationMessage).append(args[i]).append(' ');
        }

        //Отправляем донат на исполнение
        DonationExecutor.getInstance().listOfStreamerPlayers.addToDonationsQueue(new Donation(sender, donationUsername.toString(), donationAmount+".00", donationMessage.toString()));
    }
}
