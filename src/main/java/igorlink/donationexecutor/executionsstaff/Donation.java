package igorlink.donationexecutor.executionsstaff;


import kotlin.Suppress;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;

import static org.dark0ghost.configs.Text.RU.DonationText.DEFAULT_NAME;

public class Donation {
    @Getter
    private CommandSender sender;
    @Getter
    private String username;
    @Getter
    private String amount;
    @Getter
    private String message;
    @Getter
    @Setter
    private String executionName = null;

    @Suppress(names = "Unused constructor")
    public Donation(String username, String amount, String message) {
        new Donation(Bukkit.getConsoleSender(), username, amount, message);
    }

    public Donation(CommandSender sender, String username, String amount, String message) {
        this.sender = sender;
        this.amount = amount;
        this.message = message;
        if (username.equals("")) {
            this.username = DEFAULT_NAME;
            return;
        }
        this.username = username;
    }
}