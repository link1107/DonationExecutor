package igorlink.command;

import igorlink.donationexecutor.DonationExecutor;
import igorlink.donationexecutor.playersmanagement.Donation;
import org.bukkit.command.CommandSender;

public class DonateSubCommand {
    public static void onDonateCommand(CommandSender sender, String[] args) {

        //Getting donation's amount
        String donationAmount = args[0];
        StringBuilder donationUsernameBuilder = new StringBuilder();
        StringBuilder donationMessageBuilder = new StringBuilder();

        int i;
        //Получаем имя донатера
        for (i = 1; i <= args.length - 1; i++) {
            if (args[i].equals("##")) {
                break;
            } else {
                if (i == 1) {
                    donationUsernameBuilder.append(args[i]);
                } else {
                    donationUsernameBuilder.append(" ").append(args[i]);
                }
            }
        }

        //Все, что после символов ## - это сообщение
        for (i++; i <= args.length - 1; i++) {
            donationMessageBuilder.append(args[i]).append(' ');
        }
            if (i == 1) {
                donationUsername.append(args[i]);
            } else {
                donationUsername.append(' ');
                donationUsername.append(args[i]);
            }
        }

        String donationUsername = donationUsernameBuilder.toString();
        String donationMessage = donationMessageBuilder.toString();

        //Отправляем донат на исполнение
        DonationExecutor.getInstance().streamerPlayersManager.addToDonationsQueue(
                new Donation(
                        sender,
                        donationUsername,
                        donationAmount + ".00",
                        donationMessage));
        DonationExecutor.getInstance().streamerPlayersManager.addToDonationsQueue(new Donation(sender, donationUsername, donationAmount+".00"));
    }
}
